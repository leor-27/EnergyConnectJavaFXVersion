package com.example.energyconnectjavafx.DAO;

import com.example.energyconnectjavafx.DB.DBConnection;
import com.example.energyconnectjavafx.Model.Program;

import java.sql.*;
import java.util.*;

public class ProgramDAO {

    public static List<Program> getPrograms() {
        List<Program> list = new ArrayList<>();
        String sql = "SELECT * FROM Program";

        try (Connection con = DBConnection.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                Program p = new Program(
                        rs.getInt("ID"),
                        rs.getString("TITLE"),
                        rs.getString("TYPE"),
                        rs.getString("START_TIME"),
                        rs.getString("END_TIME"),
                        rs.getString("DESCRIPTION"),
                        rs.getInt("ADMIN_ID"),
                        null,
                        null
                );
                list.add(p);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public static List<Program> getFeaturedProgramsWithHosts() {
        List<Program> list = new ArrayList<>();

        String sql = """
        SELECT p.*,
               GROUP_CONCAT(
                   DISTINCT dt.DAY_TYPE
                   ORDER BY dt.ID
                   SEPARATOR ', '
               ) AS DAYS,
               GROUP_CONCAT(
                   DISTINCT COALESCE(d.STAGE_NAME, UPPER(d.REAL_NAME))
                   SEPARATOR ', '
               ) AS HOSTS
        FROM Program p
        JOIN Program_Day_Type pdt ON pdt.PROGRAM_ID = p.ID
        JOIN Day_Type dt ON dt.ID = pdt.DAY_TYPE_ID
        JOIN Program_Anchor_Assignment pa ON pa.PROGRAM_ID = p.ID
        JOIN DJ_Profile d ON d.ID = pa.DJ_ID
        GROUP BY p.ID
        LIMIT 6
    """;

        try (Connection con = DBConnection.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                Program p = new Program(
                        rs.getInt("ID"),
                        rs.getString("TITLE"),
                        rs.getString("TYPE"),
                        rs.getString("START_TIME"),
                        rs.getString("END_TIME"),
                        null,
                        1,
                        rs.getString("HOSTS"),
                        rs.getString("DAYS")
                );
                list.add(p);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public static List<Program> getProgramsWithDaysAndHosts() {
        List<Program> list = new ArrayList<>();

        String sql = """
        SELECT p.*,
               GROUP_CONCAT(DISTINCT dt.DAY_TYPE ORDER BY dt.ID SEPARATOR ', ') AS DAYS,
               GROUP_CONCAT(DISTINCT COALESCE(d.STAGE_NAME, UPPER(d.REAL_NAME)) SEPARATOR ', ') AS HOSTS
        FROM Program p
        LEFT JOIN Program_Day_Type pdt ON pdt.PROGRAM_ID = p.ID
        LEFT JOIN Day_Type dt ON dt.ID = pdt.DAY_TYPE_ID
        LEFT JOIN Program_Anchor_Assignment pa ON pa.PROGRAM_ID = p.ID
        LEFT JOIN DJ_Profile d ON d.ID = pa.DJ_ID
        GROUP BY p.ID
    """;

        try (Connection con = DBConnection.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                Program p = new Program(
                        rs.getInt("ID"),
                        rs.getString("TITLE"),
                        rs.getString("TYPE"),
                        rs.getString("START_TIME"),
                        rs.getString("END_TIME"),
                        rs.getString("DESCRIPTION"),
                        rs.getInt("ADMIN_ID"),
                        rs.getString("HOSTS"),
                        rs.getString("DAYS")
                );
                list.add(p);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public static void deleteProgram(int programId) {
        String sql = "DELETE FROM Program WHERE ID = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, programId);
            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void addProgram(Program p, List<String> days, List<String> hosts) {

        String insertProgram = """
        INSERT INTO Program (TITLE, TYPE, START_TIME, END_TIME, DESCRIPTION, ADMIN_ID)
        VALUES (?, ?, ?, ?, ?, ?)
    """;

        Connection con = null;

        try {
            con = DBConnection.getConnection();
            con.setAutoCommit(false);

            int programId;

            try (PreparedStatement ps =
                         con.prepareStatement(insertProgram, Statement.RETURN_GENERATED_KEYS)) {

                ps.setString(1, p.getTitle());
                ps.setString(2, p.getType());
                ps.setString(3, p.getStartTime());
                ps.setString(4, p.getEndTime());
                ps.setString(5, p.getDescription());
                ps.setInt(6, 1);

                ps.executeUpdate();

                ResultSet rs = ps.getGeneratedKeys();
                if (!rs.next()) {
                    throw new SQLException("Failed to retrieve generated program ID.");
                }

                programId = rs.getInt(1);
            }

            for (String dayId : days) {
                try (PreparedStatement ps = con.prepareStatement("""
                    INSERT INTO Program_Day_Type (PROGRAM_ID, DAY_TYPE_ID)
                    VALUES (?, ?)
                """)) {
                    ps.setInt(1, programId);
                    ps.setInt(2, Integer.parseInt(dayId));
                    ps.executeUpdate();
                }
            }

            for (String host : hosts) {
                try (PreparedStatement ps = con.prepareStatement("""
                INSERT INTO Program_Anchor_Assignment (PROGRAM_ID, DJ_ID)
                SELECT ?, ID FROM DJ_Profile
                WHERE STAGE_NAME = ? OR UPPER(REAL_NAME) = ?
            """)) {
                    ps.setInt(1, programId);
                    ps.setString(2, host);
                    ps.setString(3, host);
                    ps.executeUpdate();
                }
            }
            con.commit();

        } catch (Exception e) {
            if (con != null) {
                try {
                    con.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            throw new RuntimeException(e);

        } finally {
            if (con != null) {
                try {
                    con.setAutoCommit(true);
                    con.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static Program getProgramById(int id) {
        String sql = """
        SELECT p.*,
               GROUP_CONCAT(DISTINCT dt.DAY_TYPE SEPARATOR ', ') AS DAYS,
               GROUP_CONCAT(DISTINCT COALESCE(d.STAGE_NAME, UPPER(d.REAL_NAME)) SEPARATOR ', ') AS HOSTS
        FROM Program p
        LEFT JOIN Program_Day_Type pdt ON pdt.PROGRAM_ID = p.ID
        LEFT JOIN Day_Type dt ON dt.ID = pdt.DAY_TYPE_ID
        LEFT JOIN Program_Anchor_Assignment pa ON pa.PROGRAM_ID = p.ID
        LEFT JOIN DJ_Profile d ON d.ID = pa.DJ_ID
        WHERE p.ID = ?
        GROUP BY p.ID
    """;

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                Program p = new Program(
                        rs.getInt("ID"),
                        rs.getString("TITLE"),
                        rs.getString("TYPE"),
                        rs.getString("START_TIME"),
                        rs.getString("END_TIME"),
                        rs.getString("DESCRIPTION"),
                        1,
                        rs.getString("HOSTS"),
                        rs.getString("DAYS")
                );
                return p;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void updateProgram(int id, Program p, List<String> days, List<String> hosts) {

        String sql = """
        UPDATE Program
        SET TITLE=?, TYPE=?, START_TIME=?, END_TIME=?, DESCRIPTION=?
        WHERE ID=?
    """;

        try (Connection con = DBConnection.getConnection()) {
            con.setAutoCommit(false);

            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setString(1, p.getTitle());
                ps.setString(2, p.getType());
                ps.setString(3, p.getStartTime());
                ps.setString(4, p.getEndTime());
                ps.setString(5, p.getDescription());
                ps.setInt(6, id);
                ps.executeUpdate();
            }

            con.prepareStatement("DELETE FROM Program_Day_Type WHERE PROGRAM_ID=" + id).execute();
            con.prepareStatement("DELETE FROM Program_Anchor_Assignment WHERE PROGRAM_ID=" + id).execute();

            addRelations(con, id, days, hosts);

            con.commit();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    private static void addRelations(Connection con, int programId,
                                     List<String> days, List<String> hosts) throws SQLException {

        for (String dayId : days) {
            try (PreparedStatement ps = con.prepareStatement("""
            INSERT INTO Program_Day_Type (PROGRAM_ID, DAY_TYPE_ID)
            VALUES (?, ?)
        """)) {
                ps.setInt(1, programId);
                ps.setInt(2, Integer.parseInt(dayId));
                ps.executeUpdate();
            }
        }

        for (String host : hosts) {
            try (PreparedStatement ps = con.prepareStatement("""
            INSERT INTO Program_Anchor_Assignment (PROGRAM_ID, DJ_ID)
            SELECT ?, ID FROM DJ_Profile
            WHERE STAGE_NAME = ? OR UPPER(REAL_NAME) = ?
        """)) {
                ps.setInt(1, programId);
                ps.setString(2, host);
                ps.setString(3, host);
                ps.executeUpdate();
            }
        }
    }
}

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
                Program p = new Program();
                p.id = rs.getInt("ID");
                p.title = rs.getString("TITLE");
                p.type = rs.getString("TYPE");
                p.startTime = rs.getString("START_TIME");
                p.endTime = rs.getString("END_TIME");
                p.description = rs.getString("DESCRIPTION");
                p.admin_id = rs.getInt("ADMIN_ID");
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
                Program p = new Program();
                p.id = rs.getInt("ID");
                p.title = rs.getString("TITLE");
                p.startTime = rs.getString("START_TIME");
                p.endTime = rs.getString("END_TIME");
                p.days = rs.getString("DAYS");     // ✅
                p.hosts = rs.getString("HOSTS");   // ✅
                list.add(p);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }
}
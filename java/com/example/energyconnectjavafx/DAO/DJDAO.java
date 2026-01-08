package com.example.energyconnectjavafx.DAO;

import com.example.energyconnectjavafx.DB.DBConnection;
import com.example.energyconnectjavafx.Model.DJ;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class DJDAO {

    public static DJ getDJById(int id) {
        String sql = "SELECT * FROM DJ WHERE ID = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                DJ dj = new DJ();
                dj.setId(rs.getInt("ID"));
                dj.setRealName(rs.getString("REAL_NAME"));
                dj.setStageName(rs.getString("STAGE_NAME"));
                dj.setImagePath(rs.getString("IMAGE_PATH"));
                return dj;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}

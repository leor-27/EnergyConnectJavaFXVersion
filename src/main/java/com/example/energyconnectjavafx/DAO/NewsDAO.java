package com.example.energyconnectjavafx.DAO;

import com.example.energyconnectjavafx.DB.DBConnection;
import com.example.energyconnectjavafx.Model.News;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class NewsDAO {

    public static List<News> getLatestNews() {
        List<News> list = new ArrayList<>();
        String sql = """
            SELECT * FROM News
            ORDER BY DATE_POSTED DESC, ID DESC
            LIMIT 5
        """;

        try (Connection con = DBConnection.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                News n = new News();
                n.headline = rs.getString("HEADLINE");
                n.organization = rs.getString("ORGANIZATION");
                n.author = rs.getString("AUTHOR");
                n.imagePath = rs.getString("HEADLINE_IMAGE_PATH");
                n.sourceUrl = rs.getString("SOURCE_URL");
                n.datePosted = rs.getString("DATE_POSTED");
                n.summary = rs.getString("SUMMARY");
                n.admin_id = rs.getInt("ADMIN_ID");
                list.add(n);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}
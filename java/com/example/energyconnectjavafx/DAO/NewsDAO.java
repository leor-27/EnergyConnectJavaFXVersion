package com.example.energyconnectjavafx.DAO;

import com.example.energyconnectjavafx.DB.DBConnection;
import com.example.energyconnectjavafx.Model.News;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class NewsDAO {

    public List<News> getLatestNews() throws SQLException {
        List<News> newsList = new ArrayList<>();

        String sql = """
        SELECT
            n.*,
            GROUP_CONCAT(c.NAME ORDER BY c.NAME SEPARATOR ', ') AS CATEGORIES
        FROM News n
        LEFT JOIN News_Category nc ON n.ID = nc.NEWS_ID
        LEFT JOIN Category c ON nc.CATEGORY_ID = c.ID
        GROUP BY n.ID
        ORDER BY n.DATE_POSTED DESC, n.ID DESC
        LIMIT 5
        """;

        try (
                Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()
        ) {
            while (rs.next()) {
                newsList.add(mapRowToNews(rs));
            }
        }

        return newsList;
    }

    public List<News> getAllNewsForAdmin() throws SQLException {
        List<News> newsList = new ArrayList<>();

        String sql = """
        SELECT
            n.*,
            GROUP_CONCAT(c.NAME ORDER BY c.NAME SEPARATOR ', ') AS CATEGORIES
        FROM News n
        LEFT JOIN News_Category nc ON n.ID = nc.NEWS_ID
        LEFT JOIN Category c ON nc.CATEGORY_ID = c.ID
        GROUP BY n.ID
        ORDER BY n.DATE_POSTED DESC
        """;

        try (
                Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()
        ) {
            while (rs.next()) {
                newsList.add(mapRowToNews(rs));
            }
        }

        return newsList;
    }

    public News getNewsByIdForAdmin(int id) throws SQLException {

        String sql = """
        SELECT
            n.*,
            GROUP_CONCAT(c.NAME ORDER BY c.NAME SEPARATOR ', ') AS CATEGORIES
        FROM News n
        LEFT JOIN News_Category nc ON n.ID = nc.NEWS_ID
        LEFT JOIN Category c ON nc.CATEGORY_ID = c.ID
        WHERE n.ID = ?
        GROUP BY n.ID
        """;

        try (
                Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)
        ) {
            ps.setInt(1, id);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return mapRowToNews(rs);
            }
        }

        return null;
    }

    public List<String> getCategoriesForNews(int newsId) throws SQLException {
        List<String> categories = new ArrayList<>();

        String sql = """
        SELECT c.NAME
        FROM Category c
        JOIN News_Category nc ON c.ID = nc.CATEGORY_ID
        WHERE nc.NEWS_ID = ?
        """;

        try (
                Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)
        ) {
            ps.setInt(1, newsId);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                categories.add(rs.getString("NAME"));
            }
        }

        return categories;
    }

    public static void insertNews(
            int adminId,
            String sourceUrl,
            String imagePath,
            String headline,
            String author,
            String organization,
            String summary,
            List<String> categories
    ) {
        String sql = """
            INSERT INTO News
            (ADMIN_ID, SOURCE_URL, HEADLINE_IMAGE_PATH, HEADLINE, AUTHOR, ORGANIZATION, SUMMARY, DATE_POSTED)
            VALUES (?, ?, ?, ?, ?, ?, ?, NOW())
        """;

        try (Connection con = DBConnection.getConnection();
             var ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, adminId);
            ps.setString(2, sourceUrl);
            ps.setString(3, imagePath);
            ps.setString(4, headline);
            ps.setString(5, author);
            ps.setString(6, organization);
            ps.setString(7, summary);
            int rows = ps.executeUpdate();
            System.out.println("NEWS INSERT ROWS = " + rows);

            if (rows == 0) {
                throw new RuntimeException("News insert failed");
            }

            ResultSet keys = ps.getGeneratedKeys();
            if (keys.next()) {
                int newsId = keys.getInt(1);
                insertCategories(newsId, categories, con);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void updateNews(
            int id,
            String sourceUrl,
            String imagePath,
            String headline,
            String author,
            String organization,
            String summary,
            List<String> categories
    ) {
        String sql = """
        UPDATE News
        SET SOURCE_URL = ?, HEADLINE_IMAGE_PATH = ?, HEADLINE = ?, 
            AUTHOR = ?, ORGANIZATION = ?, SUMMARY = ?
        WHERE ID = ?
    """;

        try (Connection con = DBConnection.getConnection();
             var ps = con.prepareStatement(sql)) {

            ps.setString(1, sourceUrl);
            ps.setString(2, imagePath);
            ps.setString(3, headline);
            ps.setString(4, author);
            ps.setString(5, organization);
            ps.setString(6, summary);
            ps.setInt(7, id);
            ps.executeUpdate();

            deleteCategories(id, con);
            insertCategories(id, categories, con);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean deleteNews(int id) {
        try (Connection con = DBConnection.getConnection()) {

            deleteCategories(id, con);

            try (var ps = con.prepareStatement(
                    "DELETE FROM News WHERE ID = ?")) {

                ps.setInt(1, id);
                return ps.executeUpdate() > 0;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private static void insertCategories(int newsId, List<String> categories, Connection con) throws Exception {
        if (categories == null || categories.isEmpty()) return;

        String sql = """
        INSERT INTO News_Category (NEWS_ID, CATEGORY_ID)
        SELECT ?, ID FROM Category WHERE NAME = ?
    """;

        try (var ps = con.prepareStatement(sql)) {
            for (String cat : categories) {
                ps.setInt(1, newsId);
                ps.setString(2, cat);
                int rows = ps.executeUpdate();

                if (rows == 0) {
                    System.err.println("⚠️ Category NOT FOUND in DB: " + cat);
                }
            }
        }
    }

    private static void deleteCategories(int newsId, Connection con) throws Exception {
        try (var ps = con.prepareStatement(
                "DELETE FROM News_Category WHERE NEWS_ID = ?")) {
            ps.setInt(1, newsId);
            ps.executeUpdate();
        }
    }

    /* Rica */
    public List<News> getAllNews() throws SQLException {
        List<News> newsList = new ArrayList<>();

        String sql = """
                SELECT
                      n.*,
                      GROUP_CONCAT(c.NAME ORDER BY c.NAME SEPARATOR ', ') AS CATEGORIES
                FROM News n
                LEFT JOIN News_Category nc ON n.ID = nc.NEWS_ID
                LEFT JOIN Category c ON nc.CATEGORY_ID = c.ID
                GROUP BY n.ID
                ORDER BY n.DATE_POSTED DESC, n.ID DESC
                """;

        try (
                Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()
        ) {
            while(rs.next()) {
                newsList.add(mapRowToNews(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return newsList;
    }

    public News getFeaturedNews() throws SQLException {

        String sql = """
                SELECT * 
                FROM News
                ORDER BY DATE_POSTED DESC
                LIMIT 1
                """;

        try(
                Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()
        ) {

            if(rs.next()) {
                return new News(
                        rs.getInt("ID"),
                        rs.getString("HEADLINE"),
                        rs.getTimestamp("DATE_POSTED").toLocalDateTime(),
                        rs.getString("AUTHOR"),
                        rs.getString("ORGANIZATION"),
                        rs.getString("SOURCE_URL"),
                        rs.getString("HEADLINE_IMAGE_PATH"),
                        rs.getString("SUMMARY"),
                        rs.getInt("ADMIN_ID")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public List<News> searchNews(String keyword) throws SQLException {

        List<News> newsList = new ArrayList<>();

        String sql = """
                SELECT
                    n.*,
                    GROUP_CONCAT(c.NAME ORDER BY c.NAME SEPARATOR ', ') AS CATEGORIES
                FROM News n
                LEFT JOIN News_Category nc ON n.ID = nc.NEWS_ID
                LEFT JOIN Category c ON nc.CATEGORY_ID = c.ID
                WHERE
                    n.HEADLINE LIKE ?
                    OR n.SUMMARY LIKE ?
                    OR n.AUTHOR LIKE ?
                    OR n.ORGANIZATION LIKE ?
                    OR c.NAME LIKE ?
                GROUP BY n.ID
                ORDER BY n.DATE_POSTED DESC
                """;

        try(
                Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);
        ) {
            String matchKeyword = "%" + keyword + "%";

            for(int i = 1; i <= 5; i++) {
                ps.setString(i, matchKeyword);
            }

            ResultSet rs = ps.executeQuery();

            while(rs.next()) {
                News news = mapRowToNews(rs);
                newsList.add(news);
            }
        }

        return newsList;
    }

    public List<News> getSortedNews(String sortType) throws SQLException {
        List<News> newsList = new ArrayList<>();

        String orderBy;

        switch (sortType) {
            case "oldest":
                orderBy = "n.DATE_POSTED ASC";
                break;
            case "title-az":
                orderBy = "n.HEADLINE ASC";
                break;
            case "org-az":
                orderBy = "n.ORGANIZATION ASC";
                break;
            case "author-az":
                orderBy = "(n.AUTHOR IS NULL), n.AUTHOR ASC";
                break;
            case "newest":
            default:
                orderBy = "n.DATE_POSTED DESC";
                break;
        }

        String sql = """
            SELECT
                n.*,
                GROUP_CONCAT(c.NAME ORDER BY c.NAME SEPARATOR ', ') AS CATEGORIES
            FROM News n
            LEFT JOIN News_Category nc ON n.ID = nc.NEWS_ID
            LEFT JOIN Category c ON nc.CATEGORY_ID = c.ID
            GROUP BY n.ID
            ORDER BY %s
            """.formatted(orderBy);

        try (
                Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery();
        ) {
            while (rs.next()) {
                newsList.add(mapRowToNews(rs));
            }
        }

        return newsList;
    }

    // mapping logic for fetching data from db
    private News mapRowToNews(ResultSet rs) throws SQLException {
        News news = new News(
                rs.getInt("ID"),
                rs.getString("HEADLINE"),
                rs.getTimestamp("DATE_POSTED").toLocalDateTime(),
                rs.getString("AUTHOR"),
                rs.getString("ORGANIZATION"),
                rs.getString("SOURCE_URL"),
                rs.getString("HEADLINE_IMAGE_PATH"),
                rs.getString("SUMMARY"),
                rs.getInt("ADMIN_ID")

        );

        // handle categories
        String category = rs.getString("CATEGORIES");

        if(category != null) {
            news.setCategories(
                    Arrays.asList(category.split(","))
            );
        } else {
            news.setCategories(new ArrayList<>());
        }

        return news;
    }
}

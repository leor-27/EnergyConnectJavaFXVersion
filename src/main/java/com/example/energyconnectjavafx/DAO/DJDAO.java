package com.example.energyconnectjavafx.DAO;

import energyfm.energyconnect.energyconnectjavafxabout.DB.DBConnection;
import energyfm.energyconnect.energyconnectjavafxabout.Model.DJ;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class DJDAO {

   public static List<DJ> getAllDJs() {
      List<DJ> list = new ArrayList<>();
      String sql = "SELECT * FROM DJ_Profile ORDER BY ID";

      try (Connection con = DBConnection.getConnection();
           Statement st = con.createStatement();
           ResultSet rs = st.executeQuery(sql)) {

         while (rs.next()) {
            DJ dj = new DJ();
            dj.id = rs.getInt("ID");
            dj.realName = rs.getString("REAL_NAME");
            dj.stageName = rs.getString("STAGE_NAME");
            dj.imagePath = rs.getString("IMAGE_PATH");
            list.add(dj);
         }
      } catch (Exception e) {
         e.printStackTrace();
      }
      return list;
   }

   public static DJ getDJById(int id) {
      String sql = "SELECT * FROM DJ_Profile WHERE ID = ?";

      try (Connection con = DBConnection.getConnection();
           PreparedStatement ps = con.prepareStatement(sql)) {

         ps.setInt(1, id);
         ResultSet rs = ps.executeQuery();

         if (rs.next()) {
            DJ dj = new DJ();
            dj.id = rs.getInt("ID");
            dj.realName = rs.getString("REAL_NAME");
            dj.stageName = rs.getString("STAGE_NAME");
            dj.imagePath = rs.getString("IMAGE_PATH");
            return dj;
         }
      } catch (Exception e) {
         e.printStackTrace();
      }
      return null;
   }

   public static DJ getDJByStageName(String stageName) {
      String sql = "SELECT * FROM DJ_Profile WHERE STAGE_NAME = ?";

      try (Connection con = DBConnection.getConnection();
           PreparedStatement ps = con.prepareStatement(sql)) {

         ps.setString(1, stageName);
         ResultSet rs = ps.executeQuery();

         if (rs.next()) {
            DJ dj = new DJ();
            dj.id = rs.getInt("ID");
            dj.realName = rs.getString("REAL_NAME");
            dj.stageName = rs.getString("STAGE_NAME");
            dj.imagePath = rs.getString("IMAGE_PATH");
            return dj;
         }
      } catch (Exception e) {
         e.printStackTrace();
      }
      return null;
   }

   public static int getNextDJId(int currentId) {
      String sql = "SELECT ID FROM DJ_Profile WHERE ID > ? ORDER BY ID LIMIT 1";

      try (Connection con = DBConnection.getConnection();
           PreparedStatement ps = con.prepareStatement(sql)) {

         ps.setInt(1, currentId);
         ResultSet rs = ps.executeQuery();

         if (rs.next()) {
            return rs.getInt("ID");
         } else {
            // Circular: return first ID
            return getFirstDJId();
         }
      } catch (Exception e) {
         e.printStackTrace();
         return 1;
      }
   }

   public static int getPrevDJId(int currentId) {
      String sql = "SELECT ID FROM DJ_Profile WHERE ID < ? ORDER BY ID DESC LIMIT 1";

      try (Connection con = DBConnection.getConnection();
           PreparedStatement ps = con.prepareStatement(sql)) {

         ps.setInt(1, currentId);
         ResultSet rs = ps.executeQuery();

         if (rs.next()) {
            return rs.getInt("ID");
         } else {
            // Circular: return last ID
            return getLastDJId();
         }
      } catch (Exception e) {
         e.printStackTrace();
         return 6; // Fallback last ID
      }
   }

   private static int getFirstDJId() {
      String sql = "SELECT ID FROM DJ_Profile ORDER BY ID LIMIT 1";

      try (Connection con = DBConnection.getConnection();
           Statement st = con.createStatement();
           ResultSet rs = st.executeQuery(sql)) {

         if (rs.next()) {
            return rs.getInt("ID");
         }
      } catch (Exception e) {
         e.printStackTrace();
      }
      return 1;
   }

   private static int getLastDJId() {
      String sql = "SELECT ID FROM DJ_Profile ORDER BY ID DESC LIMIT 1";

      try (Connection con = DBConnection.getConnection();
           Statement st = con.createStatement();
           ResultSet rs = st.executeQuery(sql)) {

         if (rs.next()) {
            return rs.getInt("ID");
         }
      } catch (Exception e) {
         e.printStackTrace();
      }
      return 6;
   }
}
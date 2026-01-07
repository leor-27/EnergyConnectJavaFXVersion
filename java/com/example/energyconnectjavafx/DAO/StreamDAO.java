package com.example.energyconnectjavafx.DAO;

import com.example.energyconnectjavafx.Model.StreamItem;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class StreamDAO {

    public static ObservableList<StreamItem> fetchAll(Connection conn) {

        ObservableList<StreamItem> list = FXCollections.observableArrayList();

        String sql = """
            SELECT abl.ID, abl.DATE, abl.START_TIME, abl.END_TIME,
                   abl.AUDIO_FILE_PATH, p.TITLE
            FROM Audio_Broadcast_Log abl
            LEFT JOIN Program p ON abl.PROGRAM_ID = p.ID
            ORDER BY abl.DATE DESC, abl.START_TIME DESC
        """;

        try (Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                String time =
                        rs.getTime("START_TIME").toLocalTime() + " - " +
                                rs.getTime("END_TIME").toLocalTime();

                list.add(new StreamItem(
                        rs.getInt("ID"),
                        rs.getString("TITLE") != null ?
                                rs.getString("TITLE") : "No Specific Program",
                        rs.getDate("DATE").toString(),
                        time,
                        rs.getString("AUDIO_FILE_PATH")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}

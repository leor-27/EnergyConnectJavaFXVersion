package com.example.energyconnectjavafx.Service;

import java.io.File;
import java.sql.*;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.regex.*;

public class AudioIngestService {

    private static final String AUDIO_DIR =
            "/Applications/XAMPP/htdocs/EnergyConnect/audio_broadcasts/";

    private static final Pattern FILE_PATTERN =
            Pattern.compile("rec_(\\d{8})-(\\d{6})\\.mp3");

    public static void ingest(Connection conn) throws Exception {

        File folder = new File(AUDIO_DIR);
        if (!folder.exists()) return;

        for (File file : folder.listFiles((d, n) -> n.endsWith(".mp3"))) {

            Matcher matcher = FILE_PATTERN.matcher(file.getName());
            if (!matcher.matches()) continue;

            LocalDate date = LocalDate.parse(matcher.group(1),
                    DateTimeFormatter.ofPattern("yyyyMMdd"));
            LocalTime startTime = LocalTime.parse(matcher.group(2),
                    DateTimeFormatter.ofPattern("HHmmss"));
            LocalTime endTime = startTime.plusHours(1);

            String relativePath = AUDIO_DIR + file.getName();

            // prevent duplicates
            PreparedStatement check = conn.prepareStatement(
                    "SELECT ID FROM Audio_Broadcast_Log WHERE AUDIO_FILE_PATH = ?");
            check.setString(1, relativePath);
            ResultSet rs = check.executeQuery();
            if (rs.next()) continue;

            // determine day type
            DayOfWeek dow = date.getDayOfWeek();
            String dayType =
                    (dow.getValue() <= 5) ? "WEEKDAYS" :
                            (dow == DayOfWeek.SATURDAY) ? "SAT" : "SUN";

            // find program
            Integer programId = null;
            PreparedStatement prog = conn.prepareStatement("""
                SELECT p.ID
                FROM Program p
                JOIN Program_Day_Type pdt ON p.ID = pdt.PROGRAM_ID
                JOIN Day_Type dt ON pdt.DAY_TYPE_ID = dt.ID
                WHERE dt.DAY_TYPE = ?
                AND ? BETWEEN p.START_TIME AND p.END_TIME
                LIMIT 1
            """);
            prog.setString(1, dayType);
            prog.setTime(2, Time.valueOf(startTime));
            ResultSet pr = prog.executeQuery();
            if (pr.next()) programId = pr.getInt(1);

            PreparedStatement insert = conn.prepareStatement("""
                INSERT INTO Audio_Broadcast_Log
                (DATE, START_TIME, END_TIME, AUDIO_FILE_PATH, PROGRAM_ID)
                VALUES (?, ?, ?, ?, ?)
            """);
            insert.setDate(1, Date.valueOf(date));
            insert.setTime(2, Time.valueOf(startTime));
            insert.setTime(3, Time.valueOf(endTime));
            insert.setString(4, relativePath);
            if (programId == null) insert.setNull(5, Types.INTEGER);
            else insert.setInt(5, programId);

            insert.executeUpdate();
        }
    }
}

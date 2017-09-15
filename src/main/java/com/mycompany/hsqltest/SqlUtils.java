/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.hsqltest;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Koby
 */
public class SqlUtils {
    
    public static void runQuery(Connection conn, String query) throws SQLException {
        try (Statement stmnt = conn.createStatement()) {
            stmnt.execute(query);
        }
    }
    
    public static List<String> readSqlFile(File file) throws IOException {
        
        StringBuilder sb = new StringBuilder();
        String line;
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            while ((line = reader.readLine())!=null) {
                sb.append(line);
            }
        }
        
        String[] rawQueries = sb.toString().split(";");
        List<String> queryList = new ArrayList<>();
        for (String l : rawQueries) {
            queryList.add(l.trim());
        }
        return queryList;
    }
}

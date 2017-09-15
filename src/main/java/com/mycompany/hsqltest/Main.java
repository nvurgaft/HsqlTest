/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.hsqltest;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author nikolay <nikolay@acs.co.il>
 */
public class Main {

    public static void main(String[] args) {

        /**
         * Run some metadata methods
         */
        try (Connection conn = ConnectionManager.getConnection()) {
            DatabaseMetaData dbmd = conn.getMetaData();
            int dbVersionMajor = dbmd.getDatabaseMajorVersion();
            int dbVersionMinor = dbmd.getDatabaseMinorVersion();
            Logger.log("hsqldb version major: %d", dbVersionMajor);
            Logger.log("hsqldb version minor: %d", dbVersionMinor);
        } catch (SQLException e) {
            Logger.error("SQLException caught", e);
        }

        /**
         * Validate connection
         */
        try (Connection conn = ConnectionManager.getConnection()) {
            try (Statement stmnt = conn.createStatement()) {
                try (ResultSet rs = stmnt.executeQuery("SELECT 1 FROM INFORMATION_SCHEMA.SYSTEM_USERS")) {
                    while (rs.next()) {
                        Logger.log(">>> %d", rs.getInt(1));
                    }
                }
                conn.commit();
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            }
        } catch (SQLException ex) {
            Logger.error("SQLException caught", ex);
        }

        /**
         * Initialize a database schema from a file
         */
        try {
            File sqlfile = new File("./src/queries.sql");
            List<String> list = SqlUtils.readSqlFile(sqlfile);
            if (list != null && !list.isEmpty()) {
                try (Connection conn = ConnectionManager.getConnection()) {
                    for (String query : list) {
                        Logger.log("running query: %s", query);
                        try {
                            SqlUtils.runQuery(conn, query);
                            conn.commit();
                        } catch (SQLException e) {
                            conn.rollback();
                        }
                        Logger.log("OK...");
                    }
                    Logger.log("DONE!");
                }
            } else {
                Logger.log("File is empty");
            }
        } catch (IOException ex) {
            Logger.error("IOExceptio caught", ex);
        } catch (SQLException ex) {
            Logger.error("SQLException caught", ex);
        }

        /**
         * Read from a table
         */
        try (Connection conn = ConnectionManager.getConnection()) {
            try (Statement stmnt = conn.createStatement()) {
                try (ResultSet rs = stmnt.executeQuery("SELECT * FROM users")) {
                    List<User> users = new ArrayList<>();
                    while (rs.next()) {
                        User user = new User();
                        user.setUserId(rs.getInt(1));
                        user.setUserName(rs.getString(2));
                        users.add(user);
                    }
                    Logger.log("Reading user " + users);
                    for (User user : users) {
                        Logger.log(user.toString());
                    }
                }
                conn.commit();
            } catch (Throwable e) {
                conn.rollback();
            }
        } catch (SQLException ex) {
            Logger.error("SQLException caught", ex);
        }

        /**
         * then run the shutdown routine
         */
        try {
            ConnectionManager.shutdown();
        } catch (SQLException e) {
            Logger.error("SQLException caught", e);
        }
        
    }
}

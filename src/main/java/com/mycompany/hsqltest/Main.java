/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.hsqltest;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author nikolay <nikolay@acs.co.il>
 */
public class Main {

    public static void main(String[] args) {

        try (Connection conn = ConnectionManager.getConnection()) {
            DatabaseMetaData dbmd = conn.getMetaData();
            int dbVersionMajor = dbmd.getDatabaseMajorVersion();
            int dbVersionMinor = dbmd.getDatabaseMinorVersion();
            Logger.log("hsqldb version major: %d", dbVersionMajor);
            Logger.log("hsqldb version minor: %d", dbVersionMinor);
        } catch (SQLException e) {
            Logger.log("SQLException exception caught", e.getCause());
        }

        // do more stuff here
        try (Connection conn = ConnectionManager.getConnection()) {
            try (PreparedStatement stmnt = conn.prepareStatement("SELECT 1 FROM INFORMATION_SCHEMA.SYSTEM_USERS")) {
                try (ResultSet rs = stmnt.executeQuery()) {
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
            Logger.log("SQLException exception caught", ex.getCause());
        }

        // then run the shutdown routine
        try {
            ConnectionManager.shutdown();
        } catch (SQLException e) {
            Logger.log("SQLException exception caught", e.getCause());
        }
    }
}

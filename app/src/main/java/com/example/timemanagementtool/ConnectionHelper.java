package com.example.timemanagementtool;

import android.os.StrictMode;
import android.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;

public class ConnectionHelper {
    Connection con;
    String uname, pass, ip, port, db;

    public Connection connectionclass() {
        ip = "";
        db = "";
        uname = "";
        pass = "";
        port = "";

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Connection connection = null;
        String ConnectionURL = null;

        try {
            Class.forName("net.sourceforge.jtds-jdbc.Driver");
            ConnectionURL = "jdbc:jtds:sqlserver://" + ip + ":" + port + ";" + "databasename=" + db + ";user=" + uname + ";password=" + pass + ";";
            connection = DriverManager.getConnection(ConnectionURL);
        } catch (Exception ex) {
            Log.e("Error ", ex.getMessage());
        }
        return connection;
    }

}

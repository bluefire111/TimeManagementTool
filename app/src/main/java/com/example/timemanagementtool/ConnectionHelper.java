package com.example.timemanagementtool;

import android.annotation.SuppressLint;
import android.os.StrictMode;
import android.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;

public class ConnectionHelper {
    Connection con;
    String uname, pass, ip, port, db;
    @SuppressLint("App")
    public Connection connectionclass() {
        //TimeManagementDB
        //172.17.0.1
        ip = "192.168.2.220";
        db = "TimeManager";
        uname = "sa";
        pass = "Admin";
        port = "63518";

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Connection connection = null;
        String ConnectionURL = null;

        try {
            Class.forName("net.sourceforge.jtds.jdbc.Driver").newInstance();
            ConnectionURL= "jdbc:jtds:sqlserver://"+ ip + ":"+ port+";"+ "databasename="+ db +";user="+uname+";password="+pass+";";
            //ConnectionURL = "jdbc:sqlserver://" + ip + ":" + port + ";" + "databasename=" + db + ";user=" + uname + ";password=" + pass + ";";
            connection = DriverManager.getConnection(ConnectionURL);
            //connection = DriverManager.getConnection(ConnectionURL,uname,pass);
        } catch (Exception ex) {
            Log.e("Error ", ex.getMessage());
        }
        return connection;
    }

}

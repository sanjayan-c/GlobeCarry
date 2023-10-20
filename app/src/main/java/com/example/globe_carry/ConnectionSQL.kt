package com.example.globe_carry

import android.os.StrictMode
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException

class ConnectionSQL {

    fun conclass(callback: (Connection?) -> Unit) {

        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)

        GlobalScope.launch(Dispatchers.IO) {
            var connection: Connection? = null
            try {
                Class.forName("net.sourceforge.jtds.jdbc.Driver")
                val connectURL = "jdbc:mysql://sql12.freemysqlhosting.net/sql12653850?user=sql12653850&password=Nh48DS6Gx5"
                connection = DriverManager.getConnection(connectURL)
            } catch (e: ClassNotFoundException) {
                Log.e("Error is from SQL", "JDBC Driver not found")
            } catch (e: SQLException) {
                Log.e("Error is from SQL", "SQL Exception: " + e.message)
                e.printStackTrace()
            } catch (e: Exception) {
                Log.e("Error is from SQL", "Unknown Exception: " + e.message)
                e.printStackTrace()
            }
            callback(connection)
        }
    }

}
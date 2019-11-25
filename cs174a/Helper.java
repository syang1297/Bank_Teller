package cs174a;

import java.util.*;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Date;
import java.util.Properties;
import oracle.jdbc.pool.OracleDataSource;
import oracle.jdbc.OracleConnection;
import java.lang.StringBuilder;

public class Helper{
    private OracleConnection _connection;
    Helper(){
        final String DB_URL = "jdbc:oracle:thin:@cs174a.cs.ucsb.edu:1521/orcl";
		final String DB_USER = "c##andrewdoan";
		final String DB_PASSWORD = "3772365";

		// Initialize your system.  Probably setting up the DB connection.
		Properties info = new Properties();
		info.put( OracleConnection.CONNECTION_PROPERTY_USER_NAME, DB_USER );
		info.put( OracleConnection.CONNECTION_PROPERTY_PASSWORD, DB_PASSWORD );
		info.put( OracleConnection.CONNECTION_PROPERTY_DEFAULT_ROW_PREFETCH, "20" );

		try
		{
			OracleDataSource ods = new OracleDataSource();
			ods.setURL( DB_URL );
			ods.setConnectionProperties( info );
			_connection = (OracleConnection) ods.getConnection();

			// Get the JDBC driver name and version.
			DatabaseMetaData dbmd = _connection.getMetaData();

		} catch( SQLException e )
		{
			System.err.println( e.getMessage() );
		}
	}
    
    String getDate(){
        String res="";
        try {
            Statement stmt = _connection.createStatement();
            System.out.println("Getting date from DB");
			String sql ="SELECT * FROM GlobalDate";
			ResultSet query = stmt.executeQuery(sql);
            while (query.next()){
                res = query.getString("globalDate");
                }
            StringBuilder temp = new StringBuilder(res);
            res="";
            for(int i=0;i<temp.capacity();i++){
                res=res+temp.charAt(i);
                if(i==3 || i==5){
                    res=res+"-";
                } 
            }
            return res;
        } catch (Exception e) {
            System.out.println(e);
            return res;
        }
    }
    //verifies supplied taxID exists in database
    boolean verifyTaxIDExists(int taxID){
        // try {
        //     OracleDataSource ods = new OracleDataSource();
        //     OracleConnection _connection = (OracleConnection) ods.getConnection();
        //     Statement stmt = _connection.createStatement();
        // } catch (Exception e) {
        //     System.out.println("Failed to connect to database....");
        //     System.out.println(e);
        //     return false;
        // }
        return false;
    }

}

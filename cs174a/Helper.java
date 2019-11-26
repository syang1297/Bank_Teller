package cs174a;

import java.util.*;
import oracle.jdbc.OracleConnection;


public class Helper{
    private OracleConnection _connection;
    Helper(){

    }
    
    //add transaction function

    //verifies supplied taxID exists in database
    boolean verifyTaxIDExists(int taxID){
        // try {
        //     OracleDataSource ods = new OracleDataSource();
        //     OracleConnection _connection = (Oracleconnection) ods.getConnection();
        //     Statement stmt = _connection.createStatement();
        // } catch (Exception e) {
        //     System.out.println("Failed to connect to database....");
        //     System.out.println(e);
        //     return false;
        // }
        return false;
    }

}

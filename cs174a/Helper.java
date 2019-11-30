package cs174a;

import cs174a.Testable.*;
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

    enum TransactionType {
        DEPOSIT,
        TOPUP,
        WITHDRAWAL,
        PURCHASE,
        TRANSFER,
        COLLECT,
        PAYFRIEND,
        WIRE,
        WRITECHECK,
        ACCRUEINTEREST
    }
    
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
    
    //add transaction function

    OracleConnection getConnection(){
        return this._connection;
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
            for(int i=0;i<temp.length();i++){
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
    //checkNo = 0 means there's no check associated
    //return 0 means it failed (possibly due to incorrect accounttype with transaction type);
    String addTransaction(double amount, TransactionType transType, int checkNo,
                            String aID){
        String transactionID = this.newTransactionID();
        String fee = "0";
        String checkNumber = Integer.toString(checkNo);
        //TODO: check if this transaction is allowed according to account 
        //TODO: check if it's the first transaction of the month to add $5 fee
        //if it's a pocket account
        try {
            Statement stmt = _connection.createStatement();
            //check if transaction type is allowed for type of account
            try {
                String sql = "SELECT accountType " +
                                "FROM AccountPrimarilyOwns " +
                                "WHERE accountID = "+aID;
                ResultSet rs = stmt.executeQuery(sql);
                String acctType="";
                while(rs.next()){
					acctType = rs.getString("accountType");
				}
                // String acct = (String) acctType;
                switch(acctType){
                    case "STUDENT_CHECKING":                       
                    case "INTEREST_CHECKING":
                        if(transType == TransactionType.TOPUP || transType == TransactionType.PURCHASE || transType == TransactionType.PAYFRIEND
                        || transType == TransactionType.COLLECT){
                            System.out.println("Invalid transaction/account type combo");
                            return "0";
                        }                         
                        break;
                    case "SAVINGS":
                        if(transType == TransactionType.TOPUP || transType == TransactionType.PURCHASE || transType == TransactionType.PAYFRIEND
                        || transType == TransactionType.COLLECT || transType == TransactionType.WRITECHECK){
                            System.out.println("Invalid transaction/account type combo");
                            return "0";
                        }                         
                        break;
                    case "POCKET":
                        if(transType == TransactionType.TOPUP || transType == TransactionType.PURCHASE || transType == TransactionType.PAYFRIEND
                        || transType == TransactionType.COLLECT ){
                            //Shouldn't be necessary because we're adding feePaid before adding to transaction table
                            // sql = "SELECT feePaid " + 
                            //         "FROM PocketAccountLinkedWith " +
                            //         "WHERE accountID = aID";
                            // Resultset rs = stmt.executeQuery(sql);
                            // String feePaid = rs.getString("feePaid");
                            // if(feePaid.equals("0")){
                            //     amount += 5;
                            // }
                        }
                        else{
                            System.out.println("Invalid transaction/account type combo");
                            return "0";
                        }  
                        break;
                }
                    try {
                        System.out.println("Getting taxID...");
                        sql = "SELECT taxID " +
                                        "FROM AccountPrimarilyOwns " +
                                        "WHERE accountID = "+aID;
                        rs = stmt.executeQuery(sql);
                        int taxID=0;
                        while(rs.next()){
                            taxID = rs.getInt("taxID");
                        }
                        try {
                            System.out.println("Trying to add to transactions...");
                            sql = "INSERT INTO TransactionBelongs " +
                                    "VALUES (" + amount + ", " + fee + ", '" + transType + "', '" + 
                                    this.getDate() + "', " + checkNumber + ", " + transactionID + 
                                    ", " + aID + "," + taxID + ")"; 
                            stmt.executeUpdate(sql);
                            } catch (Exception e) {
                                System.out.println("Adding to transaction table failed");
                                System.out.println(e);
                                return "0";
                            }  
                    } catch (Exception e) {
                        System.out.println("Getting taxID failed");
                        System.out.println(e);
                        return "0";
                    }  
            } catch (Exception e) {
                System.out.println("Checking if transaction type allowed failed");
                System.out.println(e);
                return "0";
            }                   
        } catch (Exception e) {
            System.out.println("Creating statement failed");
            System.out.println(e);
            return "0";
        }
        System.out.println("Added transaction.");
        return "1";
    }

    //TODO: creates new transaction id, using random rn
    String newTransactionID(){
        int max = 10000; 
        int min = 1; 
        int range = max - min + 1; 
        int rand = (int)(Math.random() * range) + min; 
        return Integer.toString(rand);
    }

    boolean accountIdExists(String accountID, String table){
        return false;
    }
    String hashPin(int pin){
        String res="";
        while(pin>0){
            int temp=pin%10;
            temp=temp+33;
            res=Character.toString ((char) temp)+res;
            pin=pin/10;
        }
        return res;
    }

    int unhashPin(String hashedPin){
        String res="";
        for(int i=0;i<4;i++){
            res=res+Integer.toString((int)hashedPin.charAt(i)-33);
        }

        return Integer.parseInt(res);
    }

}

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
        ACCRUEINTEREST,
        FEE
    }
    
    private OracleConnection _connection;
    Helper(){
        final String DB_URL = "jdbc:oracle:thin:@cs174a.cs.ucsb.edu:1521/orcl";
		final String DB_USER = "c##syang01";
		final String DB_PASSWORD = "4621538";

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
    

    OracleConnection getConnection(){
        return this._connection;
    }

    void setBranch(String accountID, String branch){
        try {
            Statement stmt = _connection.createStatement();
            try{
                String sql = "UPDATE AccountPrimarilyOwns SET bankBranch = '"+branch+"' WHERE accountID = "+accountID;
                stmt.executeUpdate(sql);
            } catch(Exception e){
                System.out.println("couldn't set branch");
                System.out.println(e); 
            }
        } catch (Exception e) {
            System.out.println(e); 
        }
    }

    String getDate(){
        String res="";
        try {
            Statement stmt = _connection.createStatement();

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
        } catch (Exception e) {
            System.out.println(e); 
        }
        return res;
    }
    //checkNo = 0 means there's no check associated
    //return 0 means it failed (possibly due to incorrect accounttype with transaction type);
    String addTransaction(double amount, TransactionType transType, int checkNo,
                            String aID, String toAID){
        String transactionID = this.newTransactionID();
        String checkNumber = Integer.toString(checkNo);
        String acctType="";
        String sql = "";

        //if it's a pocket account
        try {
            Statement stmt = _connection.createStatement();
            //check if transaction type is allowed for type of account
            try {
                if(transType == TransactionType.TOPUP){
                    String sqlPocket = "SELECT accountType " +
                                    "FROM AccountPrimarilyOwns " +
                                    "WHERE accountID = " + toAID;
                    ResultSet rs = stmt.executeQuery(sqlPocket);
                        while(rs.next()){
                            acctType = rs.getString("accountType");
                        }
                }else{
                    sql = "SELECT accountType " +
                                    "FROM AccountPrimarilyOwns " +
                                    "WHERE accountID = "+aID;
                    ResultSet rs = stmt.executeQuery(sql);
                    while(rs.next()){
                        acctType = rs.getString("accountType");
                    }
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
           
                        if(transType != TransactionType.TOPUP && transType != TransactionType.PURCHASE && transType != TransactionType.PAYFRIEND
                        && transType != TransactionType.COLLECT && transType != TransactionType.FEE){
                            System.out.println("Invalid transaction/account type combo");
                            return "0";
                        } 
                        break;
                }
                    try {
                     
                        String sqlTaxID = "SELECT taxID " +
                                        "FROM AccountPrimarilyOwns " +
                                        "WHERE accountID = "+aID;
                        ResultSet rss = stmt.executeQuery(sqlTaxID);
                        int taxID=0;
                        while(rss.next()){
                            taxID = rss.getInt("taxID");
                        }
                        try {
                    
                            sql = "INSERT INTO TransactionBelongs " +
                                    "VALUES (" + amount  + ", '" + transType + "', '" + 
                                    this.getDate() + "', " + checkNumber + ", " + transactionID + 
                                    ", " + aID + "," + toAID + ", " + taxID + ")"; 
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
     
        return "1";
    }

    String newTransactionID(){
        int maxTID = 1;
        try {
            Statement stmt = _connection.createStatement();
            try {
                //checks if trans table empty
                String sql = "SELECT * FROM TransactionBelongs";
                ResultSet rs = stmt.executeQuery(sql);
                if(rs.next()){
                    try {
                        sql = "SELECT MAX(transactionID) FROM TransactionBelongs";
                        rs = stmt.executeQuery(sql);
                        if(rs.next()){
                            maxTID = rs.getInt(1);
                            maxTID+=1;
                        }
                        rs.close();
                    } catch (Exception e) {
                        System.out.println("Failed to get max transaction ID from TransactionBelongs");
                        System.out.println(e);
                        return "-1";
                    }
                }
            } catch (Exception e) {
                System.out.println("Failed to check if TransactionBelongs is empty");
                System.out.println(e);
                return "-1";
            }
        } catch (Exception e) {
            System.out.println("Failed to create statement");
            System.out.println(e);
            return "-1";
        }
        return Integer.toString(maxTID);
    }

    String newOwnsID(){
        int maxNumKey = 1;
        try {
            Statement stmt = _connection.createStatement();
            try {
                //checks if trans table empty
                String sql = "SELECT * FROM Owns";
                ResultSet rs = stmt.executeQuery(sql);
                if(rs.next()){
                    try {
                        sql = "SELECT MAX(numKey) FROM Owns";
                        rs = stmt.executeQuery(sql);
                        if(rs.next()){
                            maxNumKey = rs.getInt(1);
                            maxNumKey+=1;
                        }
                        rs.close();
                    } catch (Exception e) {
                        System.out.println("Failed to get max transaction ID from TransactionBelongs");
                        System.out.println(e);
                        return "-1";
                    }
                }
            } catch (Exception e) {
                System.out.println("Failed to check if TransactionBelongs is empty");
                System.out.println(e);
                return "-1";
            }
        } catch (Exception e) {
            System.out.println("Failed to create statement");
            System.out.println(e);
            return "-1";
        }
        return Integer.toString(maxNumKey);
    }

    String hashPin(int pin){
        String res="";
        while(pin>0){
            int temp=pin%10;
            temp=temp+65;
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

    void monthlyReset(){
        String sql="";
        try {
            Statement stmt = _connection.createStatement();
            try {
                sql = "SELECT * FROM AccountPrimarilyOwns";
                ResultSet rs = stmt.executeQuery(sql);
                if(rs.next() != false){
                    sql = "UPDATE AccountPrimarilyOwns SET interestAdded = 0";
                    stmt.executeUpdate(sql);
                }
                sql = "SELECT * FROM PocketAccountLinkedWith";
                rs = stmt.executeQuery(sql);
                if(rs.next() != false){
                    sql = "UPDATE PocketAccountLinkedWith SET feePaid = 0";
                    stmt.executeUpdate(sql);
                }
            } catch (Exception e) {
                System.out.println("Failed to reset");
                System.out.println(e);
            }
        } catch (Exception e) {
            System.out.println("Failed to create statement");
            System.out.println(e);
        }
    }
}

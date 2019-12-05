package cs174a;    

import cs174a.Customer.*;
import cs174a.Testable.*;
import cs174a.App.*;
import java.util.*;
import cs174a.Helper.*;

import java.sql.ResultSet;
import java.sql.Statement;
import oracle.jdbc.OracleConnection;
import oracle.jdbc.pool.OracleDataSource;
import java.sql.SQLException;

public class Teller {
    private Customer customer;
    private Helper helper;
    private App app;
    //constructor
    Teller(int taxID,App app){
        //TODO: creating actual customer from teller interface
        this.customer = new Customer(taxID);
        this.helper = new Helper();
        this.app = app;
    }

    //add check transaction to an account
    void depositCheck(int checkNo, int accountID, double amount){
        return;
    }

    //create monthly report for a customers accounts showing all transacation data in the last month,
    //names, addresses of accounts, initial and final account balance, message if accounts of customer is primary owner
    //exceeds 100000
    String generateMonthly(int taxID){
        String monthly = "";
        String own = "";
        Map<Integer, String> owners = new HashMap<Integer, String>();
        Map<Integer, String> accountTransactions = new HashMap<Integer, String>();
        List<Integer> res=customer.getAccountIDs(taxID,AccountType.STUDENT_CHECKING);
        // List<Triplet> allAccountBalances = new ArrayList<Triplet>();
        for(int i =0;i<res.size();i++){
            String customerInfo = "";
            List<Integer> accountCustomers = new ArrayList<Integer>();
            try {
                Statement stmt = helper.getConnection().createStatement();
                //get primary customer in AccountPrimarilyOwns that owns 
                try {
                    Double endBalance = 0.0;
                    Double initBalance = 0.0;
                    String sql = "SELECT * " +
                                    "FROM AccountPrimarilyOwns " +
                                    "WHERE accountID = " + Integer.toString(res.get(i));
                    ResultSet rs = stmt.executeQuery(sql);
                    while(rs.next()){
                        accountCustomers.add(rs.getInt("taxID"));
                        endBalance = rs.getDouble("balance");
                    }
                    rs.close();
                    //get all customers in Owns that co-owns that account
                    try {
                        sql = "SLELCT * " +
                                "FROM Owns " +
                                "WHERE aID = " + Integer.toString(res.get(i));
                        rs = stmt.executeQuery(sql);
                        while(rs.next()){
                            accountCustomers.add(rs.getInt("tID"));
                        }
                        rs.close();
                        try {
                            //get customer info
                            for(int j = 0; j < accountCustomers.size(); j++){
                                sql = "SELECT * " +
                                        "FROM Customer " +
                                        "WHERE taxID = " + Integer.toString(accountCustomers.get(j));
                                rs = stmt.executeQuery(sql);
                                customerInfo += rs.getString("name") + " " + rs.getString("taxID") + "\n";
                                rs.close();
                            }
                            owners.put(res.get(i), customerInfo);
                            try {
                                //get transactions for the account
                                sql = "SELECT * " +
                                        "FROM TransactionBelongs " +
                                        "WHERE aID = " + Integer.toString(res.get(i));
                                rs = stmt.executeQuery(sql);

                                while(rs.next()){
                                    String trans = "";
                                    trans += rs.getString("transType") + " " + rs.getString("transDate") + 
                                            " " + Double.toString(rs.getDouble("amount"));
                                    trans += "\n" + "Account Initial Balance: " + initBalance + "........" + "Account Final Balance: " + endBalance + "\n";
                                    double amt = rs.getDouble("amount");
                                    //TODO: check if adding/subtracting amount is correct after all transactions have been implemented
                                    switch(rs.getString("transType")){
                                        case "DEPOSIT":
                                            initBalance += amt;
                                            break;
                                        case "TRANSFER":
                                            initBalance += amt;
                                            break;
                                        case "WITHDRAWAL":
                                            initBalance -= amt;
                                            break;
                                        case "WIRE":
                                            initBalance += amt;
                                            break;
                                        case "WRITECHECK":
                                            initBalance += amt;
                                            break;
                                        case "ACCRUEINTEREST":
                                            initBalance += amt;
                                            break;
                                    }
                                    accountTransactions.put(res.get(i), trans);
                                    // Triplet<Integer, Double, Double> accountBalances = new Triplet<Integer, Double, Double>(res.get(i), endBalance, initBalance);
                                    // allAccountBalances.add(accountBalances);
                                }
                                rs.close();
                            } catch (Exception e) {
                                System.out.println("Failed to get transactions for accounts");
                                System.out.println(e);
                                return "1";
                            }
                        } catch (Exception e) {
                            System.out.println("Failed to get names and addresses of account owners");
                            System.out.println(e);
                            return "1";
                        }
                    } catch (Exception e) {
                        System.out.println("Failed to get all owners of an account from Owns");
                        System.out.println(e);
                        return "1";
                    }
                } catch (Exception e) {
                    System.out.println("Failed to get all owners of an account from AccountPrimarilyOwns");
                    System.out.println(e);
                    return "1";
                }
            } catch (Exception e) {
                System.out.println("Failed to create statement in generateMonthly");
                System.out.println(e);
                return "1";
            }
            // monthly.put(res.get(i), own);
        }
        //CHECK IF BALANCES EXCEED 100,000
        //PRINT STATEMENT
        //CHECK IF CAN ADD A LIST TO A LIST TO GET ALL ACCOUNTS TYPES
        // res = customer.getAccountIDs(taxID, AccountType.INTEREST_CHECKING);
        // res = customer.getAccountIDs(taxID, AccountType.SAVINGS);
        // res = customer.getAccountIDs(taxID, AccountType.POCKET);
        return "1";
    }

    //list accounts closed for a customer in the last month
    List<List<String>> listClosedAccounts(int taxID){
        return null;
    }

    //generate list of customers with deposits, transfers, and wires over 10000 in a month over opened and closed accounts
    List<List<String>> generateDTER(){
        return null;
    }

    //generate list of all accounts assoicated with a customer saying if open or closed
    // call Customer class get accountIDs
    List<String> customerReport(int taxID){
        ArrayList<String> res = new ArrayList<String>();
        String sql = "";
        try{
            Statement stmt = helper.getConnection().createStatement();
            try{
                System.out.println("Getting primarily owns...");
                sql = "SELECT * " + 
                    "FROM AccountPrimarilyOwns " + 
                    "WHERE taxID = " + Integer.toString(taxID);
                ResultSet rs = stmt.executeQuery(sql);
                while(rs.next()){
                    String account = rs.getString("accountID") + " : ";
                    if(rs.getInt("isClosed") == 0){
                        account = account + "OPEN";
                    } else {
                        account = account + "CLOSED";
                    }
                    res.add(account);
                }
                System.out.println("Getting secondaries...");
                sql = "SELECT * " + 
                    "FROM AccountPrimarilyOwns, Owns " + 
                    "WHERE AccountPrimarilyOwns.accountID = Owns.aID AND Owns.tID = " + Integer.toString(taxID);
                rs = stmt.executeQuery(sql);
                while(rs.next()){
                    String account = rs.getString("accountID") + " : ";
                    if(rs.getInt("isClosed") == 0){
                        account = account + "OPEN";
                    } else {
                        account = account + "CLOSED";
                    }
                    res.add(account);
                }
                System.out.println("Got info.");
            } catch(Exception e){
                System.out.println("Failed to get info.");
                System.out.println(e);
            }
        } catch(Exception e){
            System.out.println("Failed to create statement");
            System.out.println(e);
        }
        return res;
    }

    //add monthly interest to all open accounts if it hasnt been added yet
    //when logging interest is added to transaction table please make amount = the amount we are ADDING
    //NOT THE TOTAL AMOUNT
    //set interestAdded for month data
    //check if interest has been added... if so, generate warning and return
    //weighted avg based on days
    void addInterest(){
        String sql = "";
        try{
            Statement stmt = helper.getConnection().createStatement();
            final String DB_URL = "jdbc:oracle:thin:@cs174a.cs.ucsb.edu:1521/orcl";
            final String DB_USER = "c##andrewdoan";
            final String DB_PASSWORD = "3772365";

            // Initialize your system.  Probably setting up the DB connection.
            Properties info = new Properties();
            info.put( OracleConnection.CONNECTION_PROPERTY_USER_NAME, DB_USER );
            info.put( OracleConnection.CONNECTION_PROPERTY_PASSWORD, DB_PASSWORD );
            info.put( OracleConnection.CONNECTION_PROPERTY_DEFAULT_ROW_PREFETCH, "20" );
            try {
                OracleDataSource ods = new OracleDataSource();
                ods.setURL( DB_URL );
                ods.setConnectionProperties( info );
                OracleConnection _connection = (OracleConnection) ods.getConnection();
                Statement stmt2 = _connection.createStatement();
                try {
                    System.out.println("Getting accounts...");
                    sql = "SELECT * " +
                        "FROM AccountPrimarilyOwns" + 
                        " WHERE interestAdded = 0 AND accountType <> '" + AccountType.POCKET + "'";
                    ResultSet accounts= stmt.executeQuery(sql);
                    System.out.println("Adding interest...");
                    while(accounts.next()){
                        String currAccount = accounts.getString("accountID");
                        double oldBalance = accounts.getDouble("balance");
                        double interest = accounts.getDouble("interestRate")/100 * oldBalance;
                        try{
                            sql = "UPDATE AccountPrimarilyOwns " +
                                    "SET balance = " + (oldBalance + interest) + ", interestAdded = 1" + 
                                    " WHERE accountId = " + currAccount;
                            stmt2.executeUpdate(sql);
                            System.out.println("added interest to: " + currAccount);
                            helper.addTransaction(interest, TransactionType.ACCRUEINTEREST,0,currAccount);
                        } catch (Exception e){
                            System.out.println("Failed to add interest.");
                            System.out.println(e);
                            return;
                        }
                    }
                    System.out.println("Added interest to eligible accounts.");
                    } catch(Exception e){
                    System.out.println("Failed to get accounts");
                    System.out.println(e);
                }
            } catch( SQLException e )
                {
                    System.err.println( e.getMessage() );
                }
         } catch(Exception e){
            System.out.println("Failed to create statement");
            System.out.println(e);
        }
        return;
    }

    //create new account and store on db
    void createAccount(AccountType type, List<Integer> coOwners, double balance, String accountID, String linkedId){
        switch(type){
            case STUDENT_CHECKING:
                app.createCheckingSavingsAccount(AccountType.STUDENT_CHECKING, accountID, balance, Integer.toString(customer.getTaxID()), customer.getName(), customer.getAddress());
                break;
            case INTEREST_CHECKING:
                app.createCheckingSavingsAccount(AccountType.INTEREST_CHECKING, accountID, balance, Integer.toString(customer.getTaxID()), customer.getName(), customer.getAddress());
                break;
            case SAVINGS:
                app.createCheckingSavingsAccount(AccountType.SAVINGS, accountID, balance, Integer.toString(customer.getTaxID()), customer.getName(), customer.getAddress());
                break;
            case POCKET:
                if(Integer.parseInt(linkedId) < 1){
                    System.out.println("Invalid linkedID");
                    return;
                }
                app.createPocketAccount(accountID, linkedId, balance, Integer.toString(customer.getTaxID()));
                break;
        }
        if(coOwners.size() == 0){
            return;
        }
        System.out.println("Adding coOwners...");
        return;
    }

    //delete from db accounts that have been closed
    //should only be called at the end of the month
    String deleteClosedAccounts(){
        try {
            Statement stmt = helper.getConnection().createStatement();
            try {
                String sql = "DELETE FROM AccountPrimarilyOwns " +
                            "WHERE isClosed = 1";
                stmt.executeUpdate(sql);
                System.out.println("Deleted closed accounts");
            } catch (Exception e) {
                System.out.println("Failed to deleteClosedAccounts()");
                System.out.println(e);
                return "1";
            }
        } catch (Exception e) {
            System.out.println("Failed to create statement");
            System.out.println(e);
            return "1";
        }
        return "0";
    }

    //delete customers from db with no accounts
    void deleteCustomers(){
        String sql="";
        try{
            Statement stmt = helper.getConnection().createStatement();
            try {
                sql = "SELECT * " +
                        "FROM Customer";
                ResultSet customers = stmt.executeQuery(sql);
                final String DB_URL = "jdbc:oracle:thin:@cs174a.cs.ucsb.edu:1521/orcl";
                final String DB_USER = "c##andrewdoan";
                final String DB_PASSWORD = "3772365";

                // Initialize your system.  Probably setting up the DB connection.
                Properties info = new Properties();
                info.put( OracleConnection.CONNECTION_PROPERTY_USER_NAME, DB_USER );
                info.put( OracleConnection.CONNECTION_PROPERTY_PASSWORD, DB_PASSWORD );
                info.put( OracleConnection.CONNECTION_PROPERTY_DEFAULT_ROW_PREFETCH, "20" );
                try {
                    OracleDataSource ods = new OracleDataSource();
                    ods.setURL( DB_URL );
                    ods.setConnectionProperties( info );
                    OracleConnection _connection = (OracleConnection) ods.getConnection();
                    Statement stmt2 = _connection.createStatement();
                    while(customers.next()){
                        boolean noPrimary = false;
                        boolean noSecondary = false;
                        String taxID=customers.getString("taxID");
                        try{
                            sql = "SELECT * " +
                                    "FROM AccountPrimarilyOwns " +
                                    "WHERE taxID = " + taxID;
                            ResultSet rs = stmt2.executeQuery(sql);
                            if(rs.next() == false){
                                noPrimary = true;
                            }
                        } catch (Exception e) {
                            System.out.println("Failed to check AccountPrimarilyOwns");
                            System.out.println(e);
                        }
                        try{
                            sql = "SELECT * " +
                                    "FROM Owns " +
                                    "WHERE tID = " + taxID;
                            ResultSet rs = stmt2.executeQuery(sql);
                            if(rs.next() == false){
                                noSecondary = true;
                            }
                        } catch (Exception e) {
                            System.out.println("Failed to check Owns");
                            System.out.println(e);
                        }
                        if(noSecondary && noPrimary){
                            try{
                                sql = "DELETE FROM Customer " +
                                "WHERE taxID =" +taxID;
                                stmt2.executeUpdate(sql);
                                System.out.println("Deleted customer: "+taxID);
                            }catch(Exception e){
                                System.out.println("Failed to delete Customer");
                                System.out.println(e);
                            }
                        }
                        
                    }
                } catch( SQLException e )
                {
                    System.err.println( e.getMessage() );
                }
                customers.close();
            } catch(Exception e){
                System.out.println("Failed to get customers");
                System.out.println(e);
            }
         }catch(Exception e){
            System.out.println("Failed to create statement");
            System.out.println(e);
        }
        return;
    }

    //delete transactions from an account in prep for new month
    void deleteTransactionHistory(){
        String sql="";
        try{
            Statement stmt = helper.getConnection().createStatement();
            try{
                sql = "DELETE FROM TransactionBelongs";
                stmt.executeUpdate(sql);
                System.out.println("Transaction history deleted");
            }catch(Exception e){
                System.out.println("Failed to delete transactions");
                System.out.println(e);
            }
        }catch(Exception e){
                System.out.println("Failed to create statement");
                System.out.println(e);
        }
        return;
    }

    //change interest for an account
    void changeInterestRate(int accountID, double interestRate){
        boolean owns=false;
        String sql="";
        try{
                Statement stmt = helper.getConnection().createStatement();
            try {
                sql = "SELECT * " +
                                "FROM AccountPrimarilyOwns " +
                                "WHERE taxID = " + Integer.toString(customer.getTaxID()) +
                                "AND accountType = '" + AccountType.INTEREST_CHECKING + "'";
                ResultSet rs = stmt.executeQuery(sql);
                while(rs.next()){
                    if(Integer.toString(accountID).equals(rs.getString("accountID"))){
                        owns = true;
                    }
                }
            } catch (Exception e) {
                System.out.println("Failed to read from AccountPrimarilyOwns");
                System.out.println(e);
                return;
            }
            if(owns && interestRate>=0){
                try{
                    sql = "UPDATE AccountPrimarilyOwns " +
                                    "SET interestRate = " + Double.toString(interestRate) +
                                    " WHERE accountId = " + Integer.toString(accountID);
                    stmt.executeUpdate(sql);
                    System.out.println("Changed interest rate.");
                    return;
                } catch(Exception e){
                    System.out.println("Failed to update interest rate");
                    System.out.println(e);
                }
            } 
        } catch(Exception e){
                System.out.println("Failed to create statement");
                System.out.println(e);
            }
        System.out.println("Wrong account type or customer does not own account");
        return;
    }

    //add function to check if a customer owns the account
    //done in customer class
    boolean customerOwnsAccount(String tin, String id){
        boolean student = customer.acctBelongsToCustomer(Integer.parseInt(id), Integer.parseInt(tin), AccountType.STUDENT_CHECKING);
        boolean checking = customer.acctBelongsToCustomer(Integer.parseInt(id), Integer.parseInt(tin), AccountType.INTEREST_CHECKING);
        boolean pocket = customer.acctBelongsToCustomer(Integer.parseInt(id), Integer.parseInt(tin), AccountType.POCKET);
        boolean saving = customer.acctBelongsToCustomer(Integer.parseInt(id), Integer.parseInt(tin), AccountType.SAVINGS);
        return (student||checking||pocket||saving);
    }
}
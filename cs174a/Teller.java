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
// import oracle.jdbc.*;
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
    List<String> generateMonthly(int taxID){
        ArrayList<String> res = new ArrayList<String>();
        String sql = "";
        double totalBalance = 0;
        String stringMonth = "" + helper.getDate().charAt(5) + helper.getDate().charAt(6);
        String stringYear = "" + helper.getDate().charAt(0) + helper.getDate().charAt(1) + helper.getDate().charAt(2) + helper.getDate().charAt(3);
        int year = Integer.parseInt(stringYear);
        int month = Integer.parseInt(stringMonth);
        try{
            Statement stmt = helper.getConnection().createStatement();
            Helper helper2 = new Helper();
            Statement stmt2 = helper2.getConnection().createStatement();
            try{ 
                try{
                    System.out.println("Getting primary owner info...");
                    sql = "SELECT * FROM Customer WHERE taxID = " + taxID;
                    ResultSet primary = stmt.executeQuery(sql);
                    while(primary.next()){
                        String primaryInfo = "\n-------------MONTH " + stringMonth + " REPORT FOR CUSTOMER: " + primary.getString("name") + " (Address: "+primary.getString("addr") +")-------------\n\n";
                        res.add(primaryInfo);
                    }
                    
                } catch (Exception e){
                    System.out.println("Failed get primary owner");
                    System.out.println(e);
                }

                System.out.println("Getting account info...");
                sql = "SELECT * FROM AccountPrimarilyOwns WHERE taxID = "+taxID;
                ResultSet accounts = stmt.executeQuery(sql);
                while(accounts.next()){
                    String status=" (CLOSED)";
                    if(accounts.getInt("isClosed") == 0){
                        status = " (OPEN)";
                    } 
                    String accountInfo = "\n-------------TRANSACTIONS OF MONTH "+ stringMonth + " FOR ACCOUNT " + accounts.getString("accountId") + status +"-------------\n";
                    
                    
                    try{
                        sql = "SELECT * " + 
                        "FROM Customer, Owns " +
                        "WHERE Owns.aID = " + accounts.getString("accountID") + " AND Customer.taxID = Owns.tid";
                        ResultSet coOwns = stmt2.executeQuery(sql);
                        while(coOwns.next()){
                            accountInfo = accountInfo + "CO-OWNER: " + coOwns.getString("name") + " Address: " + coOwns.getString("addr") + "\n";
                        }
                        accountInfo = accountInfo + "\n";
                    } catch (Exception e){
                        System.out.println("Failed get coOwners");
                        System.out.println(e);
                    }

                    try{
                        boolean madeDeposit = false;
                        double initBalance = accounts.getDouble("balance");
                        sql = "SELECT * FROM TransactionBelongs WHERE aID = " + accounts.getString("accountID");
                        ResultSet transactions = stmt2.executeQuery(sql);
                        while(transactions.next()){
                            String tDate = transactions.getString("transDate");
                            String tMonth = "" + tDate.charAt(5) + tDate.charAt(6);
                            String tYear = "" + tDate.charAt(0) + tDate.charAt(1) + tDate.charAt(2) + tDate.charAt(3);
                            if(Integer.parseInt(stringMonth)!=month || Integer.parseInt(stringYear)!=year) {
                                continue;
                            }
                            double amt = transactions.getDouble("amount");
                            switch(transactions.getString("transType")){
                                case "DEPOSIT":
                                    if(tDate.equals(accounts.getString("madeOn")) && amt == 1000.00 && !madeDeposit){
                                        madeDeposit = true;
                                        continue;
                                    }
                                    initBalance += amt;
                                    accountInfo = accountInfo + "DEPOSIT: " + Double.toString(amt) + " | DATE: " + tDate + "\n"; 
                                    break;
                                case "TRANSFER":
                                    initBalance -= amt;
                                    accountInfo = accountInfo + "TRANSFER: " + Double.toString(amt) + " | DATE: " + tDate + "\n"; 
                                    break;
                                case "WITHDRAWAL":
                                    initBalance -= amt;
                                    accountInfo = accountInfo + "WITHDRAWAL: " + Double.toString(amt) + " | DATE: " + tDate + "\n"; 
                                    break;
                                case "WIRE":
                                    initBalance -= amt;
                                    accountInfo = accountInfo + "WIRE: " + Double.toString(amt) + " | DATE: " + tDate + "\n"; 
                                    break;
                                case "WRITECHECK":
                                    initBalance += amt;
                                    accountInfo = accountInfo + "WRITECHECK: " + Double.toString(amt) + " | DATE: " + tDate + "\n"; 
                                    break;
                                case "ACCRUEINTEREST":
                                    initBalance -= amt;
                                    accountInfo = accountInfo + "ACCRUEINTEREST: " + Double.toString(amt) + " | DATE: " + tDate + "\n"; 
                                    break;
                                case "COLLECT":
                                    initBalance -= amt;
                                    accountInfo = accountInfo + "COLLECT: " + Double.toString(amt) + " | DATE: " + tDate + "\n"; 
                                    break;
                                case "PAYFRIEND":
                                    initBalance -= amt;
                                    accountInfo = accountInfo + "PAYFRIEND: " + Double.toString(amt) + " | DATE: " + tDate + "\n"; 
                                    break;
                            }
                        }
                        accountInfo = accountInfo + "\nINITIAL BALANCE: " + Double.toString(initBalance) + "\tFINAL BALANCE: " + accounts.getDouble("balance")+ "\n\n";
                        totalBalance+=accounts.getDouble("balance");
                        res.add(accountInfo);
                    } catch (Exception e){
                        System.out.println("Failed get transactions");
                        System.out.println(e);
                    }
                }
            }
             catch(Exception e){
                System.out.println("Failed to get primary accounts");
                System.out.println(e);
            }
            try {
            System.out.println("Getting co-owned account info...");
            sql = "SELECT * FROM AccountPrimarilyOwns,Owns WHERE AccountPrimarilyOwns.accountID = Owns.aID AND Owns.tID = "+taxID ;
            ResultSet accounts = stmt.executeQuery(sql);
            while(accounts.next()){
                String status=" (CLOSED)";
                if(accounts.getInt("isClosed") == 0){
                    status = " (OPEN)";
                } 
                String accountInfo = "\n-------------TRANSACTIONS OF MONTH "+ stringMonth + " FOR ACCOUNT " + accounts.getString("accountId") + status +"-------------\n";

                try{
                    double initBalance = accounts.getDouble("balance");
                    sql = "SELECT * FROM TransactionBelongs WHERE aID = " + accounts.getString("accountID");
                    ResultSet transactions = stmt2.executeQuery(sql);
                    boolean madeDeposit = false;
                    while(transactions.next()){
                        String tDate = transactions.getString("transDate");
                        String tMonth = "" + tDate.charAt(5) + tDate.charAt(6);
                        String tYear = "" + tDate.charAt(0) + tDate.charAt(1) + tDate.charAt(2) + tDate.charAt(3);
                        if(Integer.parseInt(stringMonth)!=month || Integer.parseInt(stringYear)!=year) {
                            continue;
                        }           
                        double amt = transactions.getDouble("amount");
                        switch(transactions.getString("transType")){
                            case "DEPOSIT":
                                if(tDate.equals(accounts.getString("madeOn")) && amt == 1000.00 && !madeDeposit){
                                    madeDeposit = true;
                                    continue;
                                }
                                initBalance += amt;
                                accountInfo = accountInfo + "DEPOSIT: " + Double.toString(amt) + " | DATE: " + tDate + "\n"; 
                                break;
                            case "TRANSFER":
                                initBalance -= amt;
                                accountInfo = accountInfo + "TRANSFER: " + Double.toString(amt) + " | DATE: " + tDate + "\n"; 
                                break;
                            case "WITHDRAWAL":
                                initBalance -= amt;
                                accountInfo = accountInfo + "WITHDRAWAL: " + Double.toString(amt) + " | DATE: " + tDate + "\n"; 
                                break;
                            case "WIRE":
                                initBalance -= amt;
                                accountInfo = accountInfo + "WIRE: " + Double.toString(amt) + " | DATE: " + tDate + "\n"; 
                                break;
                            case "WRITECHECK":
                                initBalance += amt;
                                accountInfo = accountInfo + "WRITECHECK: " + Double.toString(amt) + " | DATE: " + tDate + "\n"; 
                                break;
                            case "ACCRUEINTEREST":
                                initBalance -= amt;
                                accountInfo = accountInfo + "ACCRUEINTEREST: " + Double.toString(amt) + " | DATE: " + tDate + "\n"; 
                                break;
                            case "COLLECT":
                                initBalance -= amt;
                                accountInfo = accountInfo + "COLLECT: " + Double.toString(amt) + " | DATE: " + tDate + "\n"; 
                                break;
                            case "PAYFRIEND":
                                initBalance -= amt;
                                accountInfo = accountInfo + "PAYFRIEND: " + Double.toString(amt) + " | DATE: " + tDate + "\n"; 
                                break;
                        }
                    }
                    accountInfo = accountInfo + "\nINITIAL BALANCE: " + Double.toString(initBalance) + "\tFINAL BALANCE: " + accounts.getDouble("balance")+ "\n\n";
                    totalBalance+=accounts.getDouble("balance");
                    res.add(accountInfo);
                    } catch (Exception e){
                        System.out.println("Failed get transactions");
                        System.out.println(e);
                    }
                }
            
             }catch(Exception e){
                System.out.println("Failed to get co-owned accounts");
                System.out.println(e);
            }
        } catch(Exception e){
            System.out.println("Failed to create statement");
            System.out.println(e);
        } 
        if(totalBalance>100000){
            res.add("WARNING: BALANCE EXCEEDS $100000, INSURANCE LIMIT REACHED");
        }          
        return res;
    }

    //list accounts closed in the last month
    String listClosedAccounts(){
        return app.listClosedAccounts();
    }

    //generate list of customers with deposits, transfers, and wires over 10000 in a month over opened and closed accounts
    List<String> generateDTER(){
        ArrayList<String> res = new ArrayList<String>();
        String sql = "";
        try{
            Statement stmt = helper.getConnection().createStatement();
            Helper helper2 = new Helper();
            Statement stmt2 = helper2.getConnection().createStatement();
            Helper helper3 = new Helper();
            Statement stmt3 = helper3.getConnection().createStatement();
            try{ 
                System.out.println("Getting customers...");
                String stringMonth = "" + helper.getDate().charAt(5) + helper.getDate().charAt(6);
                String stringYear = "" + helper.getDate().charAt(0) + helper.getDate().charAt(1) + helper.getDate().charAt(2) + helper.getDate().charAt(3);
                int year = Integer.parseInt(stringYear);
                int month = Integer.parseInt(stringMonth);
                res.add("\n-------------DTER FOR MONTH " + stringMonth + "-------------\n");
                sql = "SELECT * FROM Customer";
                ResultSet customers = stmt.executeQuery(sql);
                while(customers.next()){
                    double totalSum = 0;
                    try{
                        System.out.println("Getting primary account info...");
                        sql = "SELECT * FROM AccountPrimarilyOwns WHERE taxID = "+customers.getString("taxID");
                        ResultSet accounts = stmt2.executeQuery(sql);
                        while(accounts.next()){
                            try{
                                sql = "SELECT * FROM TransactionBelongs WHERE aID = " + accounts.getString("accountID");
                                ResultSet transactions = stmt3.executeQuery(sql);
                                while(transactions.next()){
                                    String tDate = transactions.getString("transDate");
                                    String tMonth = "" + tDate.charAt(5) + tDate.charAt(6);
                                    String tYear = "" + tDate.charAt(0) + tDate.charAt(1) + tDate.charAt(2) + tDate.charAt(3);
                                    if(Integer.parseInt(stringMonth)!=month || Integer.parseInt(stringYear)!=year) {
                                        continue;
                                    }
                                    double amt = transactions.getDouble("amount");
                                    switch(transactions.getString("transType")){
                                        case "DEPOSIT":
                                        case "TRANSFER":
                                        case "WIRE":
                                            totalSum += amt;
                                            break;
                                    }
                                }
                            } catch (Exception e){
                                System.out.println("Failed get transactions");
                                System.out.println(e);
                            }
                        }
                        } catch(Exception e) {
                                System.out.println("Failed to get primary accounts");
                            System.out.println(e);
                        }
                        try{
                            System.out.println("Getting co-owned account info...");
                            sql = "SELECT * " + 
                                "FROM AccountPrimarilyOwns, Owns " + 
                                "WHERE AccountPrimarilyOwns.accountID = Owns.aID AND Owns.tID = " + customers.getString("taxID");
                            ResultSet accounts = stmt2.executeQuery(sql);
                            while(accounts.next()){
                                try{
                                    sql = "SELECT * FROM TransactionBelongs WHERE aID = " + accounts.getString("accountID");
                                    ResultSet transactions = stmt3.executeQuery(sql);
                                    while(transactions.next()){
                                        String tDate = transactions.getString("transDate");
                                        String tMonth = "" + tDate.charAt(5) + tDate.charAt(6);
                                        String tYear = "" + tDate.charAt(0) + tDate.charAt(1) + tDate.charAt(2) + tDate.charAt(3);
                                        if(Integer.parseInt(stringMonth)!=month || Integer.parseInt(stringYear)!=year) {
                                            continue;
                                        }
                                        double amt = transactions.getDouble("amount");
                                        switch(transactions.getString("transType")){
                                            case "DEPOSIT":
                                            case "TRANSFER":
                                            case "WIRE":
                                                totalSum += amt;
                                                break;
                                        }
                                    }
                                } catch (Exception e){
                                    System.out.println("Failed get transactions");
                                    System.out.println(e);
                                }
                            }
                        } catch(Exception e) {
                                System.out.println("Failed to get co-owned accounts");
                            System.out.println(e);
                        }
                        if(totalSum>10000){
                            res.add("TAXID: " + customers.getString("taxID") + " | SUM OF DEPOSITS: " + totalSum);
                        }
                    }
                }catch(Exception e){
                System.out.println("Failed to get customers");
                System.out.println(e);
                }
            }catch(Exception e){
            System.out.println("Failed to create statement");
            System.out.println(e);  
            }
        res.add("\n");         
        return res;
    }

    //generate list of all accounts assoicated with a customer saying if open or closed
    // call Customer class get accountIDs
    List<String> customerReport(int taxID){
        ArrayList<String> res = new ArrayList<String>();
        String sql = "";
        res.add("\n-------------CUSTOMER REPORT FOR CUSTOMER " + Integer.toString(taxID) + "-------------\n");
        try{
            Statement stmt = helper.getConnection().createStatement();
            try{
                System.out.println("Getting primarily owns...");
                sql = "SELECT * " + 
                    "FROM AccountPrimarilyOwns " + 
                    "WHERE taxID = " + Integer.toString(taxID);
                ResultSet rs = stmt.executeQuery(sql);
                while(rs.next()){
                    String account = "ACCOUNTID: " + rs.getString("accountID");
                    if(rs.getInt("isClosed") == 0){
                        account = account + " (OPEN)";
                    } else {
                        account = account + " (CLOSED)";
                    }
                    res.add(account);
                }
                System.out.println("Getting secondaries...");
                sql = "SELECT * " + 
                    "FROM AccountPrimarilyOwns, Owns " + 
                    "WHERE AccountPrimarilyOwns.accountID = Owns.aID AND Owns.tID = " + Integer.toString(taxID);
                rs = stmt.executeQuery(sql);
                while(rs.next()){
                    String account = "ACCOUNTID: " + rs.getString("accountID");
                    if(rs.getInt("isClosed") == 0){
                        account = account + " (OPEN)";
                    } else {
                        account = account + " (CLOSED)";
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
        res.add("\n");
        return res;
    }

    //add monthly interest to all open accounts if it hasnt been added yet
    //when logging interest is added to transaction table please make amount = the amount we are ADDING
    //NOT THE TOTAL AMOUNT
    //set interestAdded for month data
    //check if interest has been added... if so, generate warning and return
    //weighted avg based on days
    String addInterest(){
        String sql = "";
        String currDate = helper.getDate();
        //index 0 = year, 1 = month, 2 = date
        String curSplitDate[] = currDate.split("-");
        //allocates array to strings to store month's trans info up to size of the current date
        Double[] transAmnts = new Double[Integer.parseInt(curSplitDate[2])];
        for(int i = 0; i < transAmnts.length; i++){
            transAmnts[i] = 0.0;
        }

        try{
            Statement stmt = helper.getConnection().createStatement();
            final String DB_URL = "jdbc:oracle:thin:@cs174a.cs.ucsb.edu:1521/orcl";
            final String DB_USER = "c##syang01";
            final String DB_PASSWORD = "4621538";

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
                        " WHERE interestAdded = 0 AND accountType <> '" + AccountType.POCKET + "' AND accountType <> '" + 
                        AccountType.STUDENT_CHECKING + "'";
                    ResultSet accounts= stmt.executeQuery(sql);
                    //TODO: Figure out what the check to add interest is
                    // if(accounts.next() == false){
                    //     System.out.println("Interest has already been added for this month. No action");
                    //     return "1";
                    // }
                    System.out.println("Adding interest...");
                    while(accounts.next()){
                        String currAccount = accounts.getString("accountID");
                        System.out.println(currAccount);
                        String acctType = accounts.getString("accountType");
                        double oldBalance = accounts.getDouble("balance");
                        double intRate = accounts.getDouble("interestRate");
                        double avgBalance = 0.0;
                        //used to calculate avg daily balance
                        sql = "SELECT * " +
                                "FROM TransactionBelongs " +
                                "WHERE aID = " + Integer.parseInt(currAccount);
                        ResultSet transactions = stmt2.executeQuery(sql);
                        while(transactions.next()){
                            String transType = transactions.getString("transType");
                            String transDate = transactions.getString("transDate");                            
                            Double amount = transactions.getDouble("amount");
                            String transSplitDate[] = transDate.split("-");
                            //trans happened in curr month
                            if(transSplitDate[0].equals(curSplitDate[0]) && transSplitDate[1].equals(curSplitDate[1])){
                                //TODO: change tables 
                                switch(transType){
                                    case "DEPOSIT":
                                    case "COLLECT":
                                    case "PAYFRIEND":
                                        amount = amount * -1;
                                        break;
                                    default:
                                        break;
                                    
                                }
                                transAmnts[Integer.parseInt(transSplitDate[2])] =  amount;
                            }
                        
                        //avgBalance = sum(amt_i*days_i)/sum(# days)
                        Double sumAmtDays = 0.0;
                        Double sumDays = Double.valueOf(transAmnts.length); 
                        int prevDay = transAmnts.length;
                        Double prevAmt = 0.0;
                        for(int i = transAmnts.length - 1; i >= 0; i--){
                            if(transAmnts[i] != 0){
                                sumAmtDays += (prevDay - i + 1) * (transAmnts[i] + prevAmt);
                                prevDay = i;
                            }
                        }
                        avgBalance = sumAmtDays/sumDays;
                        Double newBalance = avgBalance * intRate;
                        System.out.println("New balance about to be printed.........................................");
                        System.out.println(Double.toString(newBalance));
                        // double interest = accounts.getDouble("interestRate")/100 * oldBalance;
                        try{
                            sql = "UPDATE AccountPrimarilyOwns " +
                                    "SET balance = " + newBalance + ", interestAdded = 1" + 
                                    " WHERE accountId = " + currAccount;
                            stmt2.executeUpdate(sql);
                            System.out.println("added interest to: " + currAccount);
                            helper.addTransaction(intRate, TransactionType.ACCRUEINTEREST,0,currAccount);
                        } catch (Exception e){
                            System.out.println("Failed to add interest.");
                            System.out.println(e);
                            return "1";
                        }
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
        return "0";
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
                try {
                    Helper helper2 = new Helper();
                    Statement stmt2 = helper2.getConnection().createStatement();
                    System.out.println("Looking for customers...");
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
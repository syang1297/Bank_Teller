package cs174a;    

import cs174a.Customer.*;
import cs174a.Testable.*;
import cs174a.App.*;
import java.util.*;
import cs174a.Helper.*;
import java.lang.Math.*;
import java.sql.ResultSet;
import java.sql.Statement;
import oracle.jdbc.OracleConnection;
import oracle.jdbc.pool.OracleDataSource;
// import oracle.jdbc.*;
import java.sql.SQLException;

public class Teller {
    private Helper helper;
    private App app;
    //constructor
    Teller(App app){
        //TODO: creating actual customer from teller interface
        this.helper = new Helper();
        this.app = app;
    }

    //add check transaction to an account 
    void writeCheck(int accountID, double amount){
        int closed=0;
        String sql="";
        try{
                Statement stmt = helper.getConnection().createStatement();
                double newBalance = 0;
            try {
                sql = "SELECT * FROM AccountPrimarilyOwns WHERE accountID = "+accountID;
                ResultSet rs =stmt.executeQuery(sql);
                while(rs.next()){
                    newBalance = rs.getDouble("balance")-amount;
                    closed = rs.getInt("isClosed");
                }
            } catch(Exception e){
                System.out.println("Failed to get balance");
                System.out.println(e);
            }
            try {
                if(closed==1){
                    System.out.println("Account closed, can't write check");
                    return;
                }
                if(newBalance<0){
                    System.out.println("Not enough balance to write check");
                    return;
                }
                if(newBalance<=0.01){
                    sql = "UPDATE AccountPrimarilyOwns " +
                            "SET isClosed = 1 " +
                            "WHERE accountId = " + Integer.toString(accountID);
                    stmt.executeUpdate(sql);
                    sql = "UPDATE AccountPrimarilyOwns SET isClosed = 1 WHERE accountID = ("+
                    "SELECT aID FROM PocketAccountLinkedWith WHERE otherAccountID = " + Integer.toString(accountID) + ")";
                    stmt.executeUpdate(sql);
                }
                sql = "UPDATE AccountPrimarilyOwns SET balance = " + newBalance +
                " WHERE accountID = "+accountID;
                stmt.executeUpdate(sql);
                System.out.println("Wrote check.");
            } catch(Exception e){
            System.out.println("Failed to update balance");
            System.out.println(e);
            }
         }catch(Exception e){
            System.out.println("Failed to create statement");
            System.out.println(e);
        }
        //toAccount is 0 bc no account
        helper.addTransaction(amount, TransactionType.WRITECHECK, getCheckNumber(),Integer.toString(accountID), Integer.toString(-1));
        return;
    }

    int getCheckNumber(){
        int maxCheckNo = 1;
        try {
            Statement stmt = helper.getConnection().createStatement();
            try {
                //checks if trans table empty
                String sql = "SELECT * FROM TransactionBelongs";
                ResultSet rs = stmt.executeQuery(sql);
                if(rs.next()){
                    try {
                        sql = "SELECT MAX(checkNo) FROM TransactionBelongs";
                        rs = stmt.executeQuery(sql);
                        if(rs.next()){
                            maxCheckNo = rs.getInt(1);
                            maxCheckNo+=1;
                        }
                        rs.close();
                    } catch (Exception e) {
                        System.out.println("Failed to get max transaction ID from TransactionBelongs");
                        System.out.println(e);
                        return 0;
                    }
                }
            } catch (Exception e) {
                System.out.println("Failed to check if TransactionBelongs is empty");
                System.out.println(e);
                return 0;
            }
        } catch (Exception e) {
            System.out.println("Failed to create statement");
            System.out.println(e);
            return 0;
        }
        return maxCheckNo;
    }
    //create monthly report for a customers accounts showing all transacation data in the last month,
    //names, addresses of accounts, initial and final account balance, message if accounts of customer is primary owner
    //exceeds 100000
    List<String> generateMonthly(int taxID){
        ArrayList<String> res = new ArrayList<String>();
        int toAccount = 0;
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
                        sql = "SELECT * FROM TransactionBelongs WHERE aID = " + 
                                accounts.getString("accountID") + " OR toAID = " + accounts.getString("accountID");
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
                                    initBalance -= amt;
                                    accountInfo = accountInfo + "DEPOSIT: " + String.format(" %.2f",(amt)) + " | DATE: " + tDate + "\n"; 
                                    break;
                                case "TRANSFER":
                                    toAccount = transactions.getInt("toAID");
                                    if(Integer.toString(toAccount).equals(accounts.getString("accountID"))){
                                        initBalance -= amt;
                                    }else{
                                        initBalance += amt;
                                        amt=-1*amt;
                                    }
                                    accountInfo = accountInfo + "TRANSFER: " + String.format(" %.2f",(amt)) + " | DATE: " + tDate + " | TO ACCOUNT: " + toAccount + "\n"; 
                                    break;
                                case "WITHDRAWAL":
                                    initBalance += amt;
                                    amt=-1*amt;
                                    accountInfo = accountInfo + "WITHDRAWAL: " + String.format(" %.2f",(amt)) + " | DATE: " + tDate + "\n"; 
                                    break;
                                case "WIRE":
                                    toAccount = transactions.getInt("toAID");
                                    if(Integer.toString(toAccount).equals(accounts.getString("accountID"))){
                                        initBalance -= amt;
                                    }else{
                                        initBalance += amt;
                                        amt=-1*amt;
                                    }
                                    accountInfo = accountInfo + "WIRE: " + String.format(" %.2f",(amt)) + " | DATE: " + tDate + " | TO ACCOUNT: " + toAccount + "\n"; 
                                    break;
                                case "WRITECHECK":
                                    initBalance += amt;
                                    amt=-1*amt;
                                    accountInfo = accountInfo + "WRITECHECK: " + String.format(" %.2f",(amt)) + " | DATE: " + tDate + "\n"; 
                                    break;
                                case "ACCRUEINTEREST":
                                    initBalance -= amt;
                                    accountInfo = accountInfo + "ACCRUEINTEREST: " + String.format(" %.2f",(amt)) + " | DATE: " + tDate + "\n"; 
                                    break;
                                case "COLLECT":
                                    toAccount = transactions.getInt("toAID");
                                    if(Integer.toString(toAccount).equals(accounts.getString("accountID"))){
                                        initBalance -= amt;
                                    }else{
                                        initBalance += amt;
                                        amt=-1*amt;
                                    }
                                    accountInfo = accountInfo + "COLLECT: " + String.format(" %.2f",(amt)) + " | DATE: " + tDate + " | TO ACCOUNT: " + toAccount + "\n"; 
                                    break;
                                case "PAYFRIEND":
                                    toAccount = transactions.getInt("toAID");
                                    if(Integer.toString(toAccount).equals(accounts.getString("accountID"))){
                                        initBalance -= amt;
                                    }else{
                                        initBalance += amt;
                                        amt=-1*amt;
                                    }
                                    accountInfo = accountInfo + "PAYFRIEND: " + String.format(" %.2f",(amt)) + " | DATE: " + tDate + " | TO ACCOUNT: " + toAccount + "\n"; 
                                    break;
                                case "TOPUP":
                                    toAccount = transactions.getInt("toAID");
                                    if(Integer.toString(toAccount).equals(accounts.getString("accountID"))){
                                        initBalance -= amt;
                                    }else{
                                        initBalance += amt;
                                        amt=-1*amt;
                                    }
                                    accountInfo = accountInfo + "TOPUP: " + String.format(" %.2f",(amt)) + " | DATE: " + tDate + " | TO ACCOUNT: " + toAccount + "\n"; 
                                    break;
                                case "PURCHASE":
                                    initBalance += amt;
                                    amt=-1*amt;
                                    accountInfo = accountInfo + "PURCHASE: " + String.format(" %.2f",(amt)) + " | DATE: " + tDate + " | TO ACCOUNT: " + toAccount + "\n"; 
                                    break;
                                case "FEE":
                                    initBalance += amt;
                                    amt=-1*amt;
                                    accountInfo = accountInfo + "FEE: " + String.format(" %.2f",(amt)) + " | DATE: " + tDate + " | TO ACCOUNT: " + toAccount + "\n"; 
                                    break;
                            }
                        }
                        accountInfo = accountInfo + "\nINITIAL BALANCE: " + String.format(" %.2f",(initBalance)) + "\tFINAL BALANCE: " + String.format("%.2f",accounts.getDouble("balance"))+ "\n\n";
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
            //getting account info for customers' co-owned accounts
            try {
            System.out.println("Getting co-owned account info...");
            sql = "SELECT * FROM AccountPrimarilyOwns,Owns WHERE AccountPrimarilyOwns.accountID = Owns.aID AND Owns.tID = "+taxID ;
            ResultSet accounts = stmt.executeQuery(sql);
            if(accounts.next() != false){
                System.out.println("Owner is a co-owner");
                sql = "SELECT * FROM AccountPrimarilyOwns,Owns WHERE AccountPrimarilyOwns.accountID = Owns.aID AND Owns.tID = "+taxID ;
                accounts = stmt.executeQuery(sql);
                while(accounts.next()){
                    String status=" (CLOSED)";
                    if(accounts.getInt("isClosed") == 0){
                        status = " (OPEN)";
                    } 
                    String accountInfo = "\n-------------TRANSACTIONS OF MONTH "+ stringMonth + " FOR ACCOUNT " + accounts.getString("accountId") + status +"-------------\n";

                    try{
                        double initBalance = accounts.getDouble("balance");
                        sql = "SELECT * FROM TransactionBelongs WHERE aID = " + 
                                accounts.getString("accountID") + " OR toAID = " + accounts.getString("accountID");
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
                                    initBalance -= amt;
                                    accountInfo = accountInfo + "DEPOSIT: " + String.format(" %.2f",(amt)) + " | DATE: " + tDate + "\n"; 
                                    break;
                                case "TRANSFER":
                                    toAccount = transactions.getInt("toAID");
                                    if(Integer.toString(toAccount).equals(accounts.getString("accountID"))){
                                        initBalance -= amt;
                                    }else{
                                        initBalance += amt;
                                        amt=-1*amt;
                                    }
                                    accountInfo = accountInfo + "TRANSFER: " + String.format(" %.2f",(amt)) + " | DATE: " + tDate + " | TO ACCOUNT: " + toAccount + "\n"; 
                                    break;
                                case "WITHDRAWAL":
                                    initBalance += amt;
                                    amt=-1*amt;
                                    accountInfo = accountInfo + "WITHDRAWAL: " + String.format(" %.2f",(amt)) + " | DATE: " + tDate + "\n"; 
                                    break;
                                case "WIRE":
                                    toAccount = transactions.getInt("toAID");
                                    if(Integer.toString(toAccount).equals(accounts.getString("accountID"))){
                                        initBalance -= amt;
                                    }else{
                                        initBalance += amt;
                                        amt=-1*amt;
                                    }
                                    accountInfo = accountInfo + "WIRE: " + String.format(" %.2f",(amt)) + " | DATE: " + tDate + " | TO ACCOUNT: " + toAccount + "\n"; 
                                    break;
                                case "WRITECHECK":
                                    initBalance += amt;
                                    amt=-1*amt;
                                    accountInfo = accountInfo + "WRITECHECK: " + String.format(" %.2f",(amt)) + " | DATE: " + tDate + "\n"; 
                                    break;
                                case "ACCRUEINTEREST":
                                    initBalance -= amt;
                                    accountInfo = accountInfo + "ACCRUEINTEREST: " + String.format(" %.2f",(amt)) + " | DATE: " + tDate + "\n"; 
                                    break;
                                case "COLLECT":
                                    toAccount = transactions.getInt("toAID");
                                    if(Integer.toString(toAccount).equals(accounts.getString("accountID"))){
                                        initBalance -= amt;
                                    }else{
                                        initBalance += amt;
                                        amt=-1*amt;
                                    }
                                    accountInfo = accountInfo + "COLLECT: " + String.format(" %.2f",(amt)) + " | DATE: " + tDate + " | TO ACCOUNT: " + toAccount + "\n"; 
                                    break;
                                case "PAYFRIEND":
                                    toAccount = transactions.getInt("toAID");
                                    if(Integer.toString(toAccount).equals(accounts.getString("accountID"))){
                                        initBalance -= amt;
                                    }else{
                                        initBalance += amt;
                                        amt=-1*amt;
                                    }
                                    accountInfo = accountInfo + "PAYFRIEND: " + String.format(" %.2f",(amt)) + " | DATE: " + tDate + " | TO ACCOUNT: " + toAccount + "\n"; 
                                    break;
                                case "TOPUP":
                                    toAccount = transactions.getInt("toAID");
                                    if(Integer.toString(toAccount).equals(accounts.getString("accountID"))){
                                        initBalance -= amt;
                                    }else{
                                        initBalance += amt;
                                        amt=-1*amt;
                                    }
                                    accountInfo = accountInfo + "TOPUP: " + String.format(" %.2f",(amt)) + " | DATE: " + tDate + " | TO ACCOUNT: " + toAccount + "\n"; 
                                    break;
                                case "PURCHASE":
                                    initBalance += amt;
                                    amt=-1*amt;
                                    accountInfo = accountInfo + "PURCHASE: " + String.format(" %.2f",(amt)) + " | DATE: " + tDate + " | TO ACCOUNT: " + toAccount + "\n"; 
                                    break;
                                case "FEE":
                                    initBalance += amt;
                                    amt=-1*amt;
                                    accountInfo = accountInfo + "FEE: " + String.format(" %.2f",(amt)) + " | DATE: " + tDate + " | TO ACCOUNT: " + toAccount + "\n"; 
                                    break;
                            }
                        }
                        accountInfo = accountInfo + "\nINITIAL BALANCE: " + String.format("%.2f",initBalance) + "\tFINAL BALANCE: " + String.format("%.2f",accounts.getDouble("balance"))+ "\n\n";
                        totalBalance+=accounts.getDouble("balance");
                        res.add(accountInfo);
                        } catch (Exception e){
                            System.out.println("Failed get transactions");
                            System.out.println(e);
                        }
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
                                sql = "SELECT * FROM TransactionBelongs WHERE aID = " + 
                                accounts.getString("accountID") + " OR toAID = " + accounts.getString("accountID");
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
                                            totalSum += Math.abs(amt);
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
                            System.out.println("...");
                            sql = "SELECT * " + 
                                "FROM AccountPrimarilyOwns, Owns " + 
                                "WHERE AccountPrimarilyOwns.accountID = Owns.aID AND Owns.tID = " + customers.getString("taxID");
                            ResultSet accounts = stmt2.executeQuery(sql);
                            while(accounts.next()){
                                try{
                                    sql = "SELECT * FROM TransactionBelongs WHERE aID = " + 
                                accounts.getString("accountID") + " OR toAID = " + accounts.getString("accountID");
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
                            res.add("TAXID: " + customers.getString("taxID") + " | SUM OF TRANSACTIONS: " + totalSum);
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
        //allocates array to strings to store month's trans info up to size of the current date
        try{
            Statement stmt = helper.getConnection().createStatement();
            String stringMonth = "" + helper.getDate().charAt(5) + helper.getDate().charAt(6);
            String stringYear = "" + helper.getDate().charAt(0) + helper.getDate().charAt(1) + helper.getDate().charAt(2) + helper.getDate().charAt(3);
            String stringDay = "" + helper.getDate().charAt(8) + helper.getDate().charAt(9);
            int year = Integer.parseInt(stringYear);
            int month = Integer.parseInt(stringMonth);
            int day = Integer.parseInt(stringDay);
            try {
                Helper helper2 = new Helper();
                Statement stmt2 = helper2.getConnection().createStatement();
                try {
                    System.out.println("Getting accounts...");
                    sql = "SELECT * " +
                        "FROM AccountPrimarilyOwns" + 
                        " WHERE interestAdded = 0 AND accountType <> '" + AccountType.POCKET + "' AND accountType <> '" + 
                        AccountType.STUDENT_CHECKING + "'";
                    ResultSet accounts= stmt.executeQuery(sql);
                    //TODO: Figure out what the check to add interest is, should be last day of the month and no interest added
                    // if(accounts.next() == false){
                    //     System.out.println("Interest has already been added for this month. No action");
                    //     return "1";
                    // }
                    System.out.println("Adding interest...");
                    while(accounts.next()){
                        ArrayList<Double> dayWeights = new ArrayList<Double>();
                        ArrayList<Double> balances = new ArrayList<Double>();
                        String currAID = accounts.getString("accountID");
                        double initBalance = accounts.getDouble("balance");
                        //used to calculate avg daily balance
                        sql = "SELECT * " +
                                "FROM TransactionBelongs " +
                                "WHERE aID = " + currAID +
                                " ORDER BY transDate DESC";
                        ResultSet transactions = stmt2.executeQuery(sql);
                        double currDay = day;
                        while(transactions.next()){    
                            String tDate = transactions.getString("transDate");
                            String tMonth = "" + tDate.charAt(5) + tDate.charAt(6);
                            String tYear = "" + tDate.charAt(0) + tDate.charAt(1) + tDate.charAt(2) + tDate.charAt(3);
                            String tDay = "" + tDate.charAt(8) + tDate.charAt(9);
                            if(Integer.parseInt(stringMonth)!=month || Integer.parseInt(stringYear)!=year) {
                                continue;
                            }
                            double amt = transactions.getDouble("amount");
                            if(currDay-Double.parseDouble(tDay)!=0){
                                dayWeights.add(currDay-Double.parseDouble(tDay));
                                balances.add(initBalance);
                            } 
                            switch(transactions.getString("transType")){
                            case "DEPOSIT":
                            case "TRANSFER":
                            case "WIRE":
                            case "ACCRUEINTEREST":
                            case "PAYFRIEND":
                                initBalance -= amt;
                                break;
                            case "WRITECHECK":
                            case "WITHDRAWAL":
                            case "COLLECT":
                                initBalance += amt;
                                break;
                            }
                            currDay = Double.parseDouble(tDay);
                        }
                        double newBalance = accounts.getDouble("balance");
                        if(balances.size()!=0){
                            double bot = 0;
                            double top = 0;
                            for(int i=0;i<balances.size();i++){
                                System.out.println("dayweight: "+ dayWeights.get(i) + " balance: "+balances.get(i));
                                bot+=dayWeights.get(i);
                                top+=balances.get(i)*dayWeights.get(i);
                            }
                            newBalance = top/bot;
                        }
                        double interestAdded = newBalance * accounts.getDouble("interestRate")/100.0;
                        interestAdded = Double.parseDouble(String.format("%.2f",interestAdded));
                        newBalance = accounts.getDouble("balance") + interestAdded;
                        try{
                            sql = "UPDATE AccountPrimarilyOwns " +
                                    "SET balance = " + Double.toString(newBalance) + ", interestAdded = 1" + 
                                    " WHERE accountId = " + currAID;
                            stmt2.executeUpdate(sql);
                            System.out.println("New Balance: "+newBalance+" Added interest: "+ interestAdded +" To: " + currAID);
                            //0 for check and -1 for to account
                            helper.addTransaction(interestAdded, TransactionType.ACCRUEINTEREST,0,currAID, Integer.toString(-1));
                        } catch (Exception e){
                            System.out.println("Failed to add interest.");
                            System.out.println(e);
                            return "1";
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
    void createAccount(AccountType type, List<Integer> coOwners, double balance, String accountID, String taxID, String linkedId){
        Customer customer = new Customer(Integer.parseInt(taxID));
        switch(type){
            case STUDENT_CHECKING:
                app.createCheckingSavingsAccount(AccountType.STUDENT_CHECKING, accountID, balance, taxID, customer.getName(), customer.getAddress());
                break;
            case INTEREST_CHECKING:
                app.createCheckingSavingsAccount(AccountType.INTEREST_CHECKING, accountID, balance, taxID, customer.getName(), customer.getAddress());
                break;
            case SAVINGS:
                app.createCheckingSavingsAccount(AccountType.SAVINGS, accountID, balance, taxID, customer.getName(), customer.getAddress());
                break;
            case POCKET:
                if(Integer.parseInt(linkedId) < 1){
                    System.out.println("Invalid linkedID");
                    return;
                }
                app.createPocketAccount(accountID, linkedId, balance, taxID);
                break;
        }
        if(coOwners.size() == 0){
            return;
        }
        //TODO: add co-owners and check if account already exists
        System.out.println("Adding coOwners...");
        return;
    }

    //delete from db accounts that have been closed
    //should only be called at the end of the month
    String deleteClosedAccounts(){
        List<Integer> closedAccounts = new ArrayList<Integer>();
        try {
            Statement stmt = helper.getConnection().createStatement();
            try {
                String sql = "SELECT accountID " +
                            "FROM AccountPrimarilyOwns " +
                            "WHERE isClosed = 1";
                ResultSet closed = stmt.executeQuery(sql);
                while(closed.next()){
                    closedAccounts.add(closed.getInt("accountID"));
                }
            } catch (Exception e) {
                System.out.println("Failed to get accounts marked for closed into list");
                System.out.println(e);
                return "1";
            }
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
            try {
                for(int i = 0; i < closedAccounts.size(); i++){
                    String delOwns = "DELETE FROM Owns " +
                    "WHERE aID = " + closedAccounts.get(i);
                    stmt.executeUpdate(delOwns);
                }
            } catch (Exception e) {
                System.out.println("Failed to delete entry from owns table for closed accounts");
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
    void changeInterestRate(int accountID, int taxID, double interestRate){
        boolean owns=false;
        String sql="";
        try{
                Statement stmt = helper.getConnection().createStatement();
            try {
                sql = "SELECT * " +
                                "FROM AccountPrimarilyOwns " +
                                "WHERE taxID = " + Integer.toString(taxID) +
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
        Customer customer = new Customer(Integer.parseInt(tin));
        boolean student = customer.acctBelongsToCustomer(Integer.parseInt(id), AccountType.STUDENT_CHECKING);
        boolean checking = customer.acctBelongsToCustomer(Integer.parseInt(id), AccountType.INTEREST_CHECKING);
        boolean pocket = customer.acctBelongsToCustomer(Integer.parseInt(id), AccountType.POCKET);
        boolean saving = customer.acctBelongsToCustomer(Integer.parseInt(id), AccountType.SAVINGS);
        return (student||checking||pocket||saving);
    }
}
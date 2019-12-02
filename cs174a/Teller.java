package cs174a;    

import cs174a.Customer.*;
import cs174a.Testable.*;
import cs174a.App.*;
import java.util.*;

import java.sql.ResultSet;
import java.sql.Statement;
import oracle.jdbc.OracleConnection;


public class Teller {
    private Customer customer;
    private Helper helper;
    private App app;
    //constructor
    Teller(App app){
        //TODO: creating actual customer from teller interface
        this.customer = new Customer(1234);
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
        List<Triplet> allAccountBalances = new ArrayList<Triplet>();
        for(int i =0;i<res.size();i++){
            String customerInfo = "";
            List<Integer> accountCustomers = new ArrayList<Integer>();
            try {
                Statement stmt = helper.getConnection().createStatement();
                //get all customers in AccountPrimarilyOwns that owns that account
                try {
                    Double endBalance = "";
                    Double initBalance = "";
                    String sql = "SELECT * " +
                                    "FROM AccountPrimarilyOwns " +
                                    "WHERE accountID = " + Integer.toString(res.get(i));
                    ResultSet rs = stmt.executeQuery(sql);
                    while(rs.next()){
                        accountCustomers.add(rs.getInteger("taxID"));
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
                            accountCustomers.add(rs.getInteger("tID"));
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
                                            " " + Double.toString(rs.getDouble(amount));
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
                                    Triplet<Integer, Double, Double> accountBalances = new Triplet<Integer, Double, Double>(res.get(i), endBalance, initBalance);
                                    allAccountBalances.add(accountBalances);
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
            monthly.put(res.get(i), own);
        }
        //CHECK IF BALANCES EXCEED 100,000
        //PRINT STATEMENT
        //CHECK IF CAN ADD A LIST TO A LIST TO GET ALL ACCOUNTS TYPES
        // res = customer.getAccountIDs(taxID, AccountType.INTEREST_CHECKING);
        // res = customer.getAccountIDs(taxID, AccountType.SAVINGS);
        // res = customer.getAccountIDs(taxID, AccountType.POCKET);
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
    List<List<String>> customerReport(int taxID){
        return null;
    }

    //add monthly interest to all open accounts if it hasnt been added yet
    //when logging interest is added to transaction table please make amount = the amount we are ADDING
    //NOT THE TOTAL AMOUNT
    void addInterest(){
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
                app.createPocketAccount(accountID, linkedId, balance, Integer.toString(customer.getTaxID()));
                break;
        }
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
        return;
    }

    //delete transactions from an account in prep for new month
    void deleteTransactionHistory(){
        return;
    }

    //change interest for an account
    void changeInterestRate(int accountID, int interestRate){
        return;
    }

    //add function to check if a customer owns the account
    //done in customer class
    boolean customerOwnsAccount(String tin, String id){
        
        return false;
    }
}
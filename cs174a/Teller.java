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
    List<List<String>> generateMonthly(int taxID){
        return null;
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
    void addInterest(){
        return;
    }

    //create new account and store on db
    void createAccount(AccountType type, List<Integer> coOwners, double balance, String accountID, String linkedId){
        // switch(type){
        //     case STUDENT_CHECKING:
        //         createCheckingsSavingsAccount(AccountType.STUDENT_CHECKING, accountID, balance, Integer.toString(customer.getTaxID()), customer.getName(), customer.getAddress());
        //         break;
        //     case INTEREST_CHECKING:
        //         createCheckingsSavingsAccount(AccountType.INTEREST_CHECKING, accountID, balance, Integer.toString(customer.getTaxID()), customer.getName(), customer.getAddress());
        //         break;
        //     case SAVINGS:
        //         createCheckingsSavingsAccount(AccountType.SAVINGS, accountID, balance, Integer.toString(customer.getTaxID()), customer.getName(), customer.getAddress());
        //         break;
        //     case POCKET:
        //         createPocketAccount(accountID, linkedId, balance, Integer.toString(customer.getTaxID()));
        //         break;
        // }
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
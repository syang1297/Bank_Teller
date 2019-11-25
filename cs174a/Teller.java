package cs174a;    

import cs174a.Customer.*;
import java.util.*;

public class Teller {
    private Customer customer;
    private Helper helper;

    //constructor
    Teller(){

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
    void createAccount(String type, List<Integer> coOwners, double balance, String accountID){
        return;
    }

    //delete from db accounts that have been closed
    void deleteClosedAccounts(){
        return;
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
}
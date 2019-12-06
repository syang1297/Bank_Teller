package cs174a;    
import java.util.*;
import cs174a.Helper.*;
import cs174a.Testable.*;

import java.sql.ResultSet;
import java.sql.Statement;


//setter methods which return string returns 0 on success and 1 on failure

public class Customer {
    private int taxID;
    private Helper helper;
    
    //constructor
    Customer(int tax){
        this.taxID = tax;
        helper = new Helper();
    }

    //taxID getter
    int getTaxID(){
        return taxID;
    }


    //get name from db for the customer
    String getName(){
        String res="1";
        System.out.println("Getting name of customer...");
        try {
            Statement stmt = helper.getConnection().createStatement();
            try {
                String sql = "SELECT name " +
                            "FROM Customer " +
                            "WHERE taxID = " + Integer.toString(this.taxID);
                ResultSet rs = stmt.executeQuery(sql);
                while(rs.next()){
                    res = rs.getString("name");
                    
                }
                rs.close();
                System.out.println("Got customer name.");
                return res;
            } catch (Exception e) {
                System.out.println("Failed to get customer name");
                System.out.println(e);
                return "1";
            }
        } catch (Exception e) {
            System.out.println("Failed to create statement in getName()");
            System.out.println(e);
            return "1";
        }
    }

    //get address from db for customer
    String getAddress(){
        String res="1";
        System.out.println("Getting address of customer...");
        try {
            Statement stmt = helper.getConnection().createStatement();
            try {
                String sql = "SELECT addr " +
                            "FROM Customer " +
                            "WHERE taxID = " + Integer.toString(this.taxID);
                ResultSet rs = stmt.executeQuery(sql);
                 while(rs.next()){
                    res = rs.getString("addr");
                }
                rs.close();
                System.out.println("Got customer address.");
                return res;
            } catch (Exception e) {
                System.out.println("Failed to get customer address");
                System.out.println(e);
                return "1";
            }
        } catch (Exception e) {
            System.out.println("Failed to create statement in getAddress()");
            System.out.println(e);
            return "1";
        }
    }

    //get pin from db for customer and unhash
    int getPin(){
        int res=1;
        System.out.println("Getting pin of customer...");
        try {
            Statement stmt = helper.getConnection().createStatement();
            try {
                String sql = "SELECT pin " +
                            "FROM Customer " +
                            "WHERE taxID = " + Integer.toString(this.taxID);
                ResultSet rs = stmt.executeQuery(sql);
                 while(rs.next()){
                    String hashedPin = rs.getString("pin");
                    res = helper.unhashPin(hashedPin);
                }
                rs.close();
                System.out.println("Got customer pin.");
                return res;
            } catch (Exception e) {
                System.out.println("Failed to get customer pin");
                System.out.println(e);
                return 1;
            }
        } catch (Exception e) {
            System.out.println("Failed to create statement in getPin()");
            System.out.println(e);
            return 1;
        }
    }

    //set pin and hash and store to db for customer
    //check that pin is 4 digits
    String setPin(int unhashedPin){
        String pin = Integer.toString(unhashedPin);
        if(pin.length() != 4){
            System.out.println("Pin too short.");
            return "1";
        }
        String hashedPin = helper.hashPin(unhashedPin);
        try {
            Statement stmt = helper.getConnection().createStatement();
            try {
                String sql = "UPDATE Customer " +
                            "SET pin = '" + hashedPin + 
                            "' WHERE taxID = " + Integer.toString(this.taxID);
                stmt.executeUpdate(sql);
                System.out.println("Set pin.");
                return "0";
            } catch (Exception e) {
                System.out.println("Failed to set customer pin");
                System.out.println(e);
                return "1";
            }
        } catch (Exception e) {
            System.out.println("Failed to create statement in setPin()");
            System.out.println(e);
            return "1";
        }
    }

    //set address and store to db for customer
    String setAddress(String addr){
        try {
            Statement stmt = helper.getConnection().createStatement();
            try {
                String sql = "UPDATE Customer " +
                            "SET addr = '" + addr + 
                            "' WHERE taxID = " + Integer.toString(this.taxID);
                stmt.executeUpdate(sql);
                return "0";
            } catch (Exception e) {
                System.out.println("Failed to set customer addr");
                System.out.println(e);
                return "1";
            }
        } catch (Exception e) {
            System.out.println("Failed to create statement in setAddress()");
            System.out.println(e);
            return "1";
        }
    }

    //set name and store to db for customer
    String setName(String name){
         try {
             Statement stmt = helper.getConnection().createStatement();
             try {
                 String sql = "UPDATE Customer " +
                             "SET name = '" + name + 
                             "' WHERE taxID = " + Integer.toString(this.taxID);
                 stmt.executeUpdate(sql);
                 return "0";
             } catch (Exception e) {
                 System.out.println("Failed to set customer name");
                 System.out.println(e);
                 return "1";
             }
         } catch (Exception e) {
             System.out.println("Failed to create statement in setName()");
             System.out.println(e);
             return "1";
         }
    }
    //get accounts associated with customers taxID and the account type
    List<Integer> getAccountIDs(AccountType type){
        List<Integer> accountIDs = new ArrayList<Integer>();
        String pocket = "POCKET";
        String sql = "";
        try {
            Statement stmt = helper.getConnection().createStatement();
            switch(type){
                case STUDENT_CHECKING:
                case INTEREST_CHECKING:
                case SAVINGS:
                    try {
                        sql = "SELECT * " +
                                        "FROM AccountPrimarilyOwns " +
                                        "WHERE taxID = " + Integer.toString(this.taxID);
                        ResultSet rs = stmt.executeQuery(sql);
                        while(rs.next()){
                            if(!pocket.equals(rs.getString("accountType"))){
                                accountIDs.add(rs.getInt("accountID"));
                            }
                        }
                    } catch (Exception e) {
                        System.out.println("Failed to read from AccountPrimarilyOwns");
                        System.out.println(e);
                        return accountIDs;
                    }
                    try {
                        sql = "SELECT * " +
                                "FROM Owns " +
                                "WHERE tID = " + Integer.toString(this.taxID);
                        ResultSet rss = stmt.executeQuery(sql);
                        while(rss.next()){
                            accountIDs.add(rss.getInt("aID"));
                        }
                    } catch (Exception e) {
                        System.out.println("Failed to read from Owns");
                        System.out.println(e);
                        return accountIDs;
                    }
                    break;
                case POCKET:
                    try {
                        sql = "SELECT * " +
                                "FROM PocketAccountLinkedWith " +
                                "WHERE tID = " + Integer.toString(this.taxID);
                        ResultSet rsss = stmt.executeQuery(sql);
                        while(rsss.next()){
                            accountIDs.add(rsss.getInt("aID"));
                        }
                    } catch (Exception e) {
                        System.out.println("Failed to read from PocketAccountLinkedWith");
                        System.out.println(e);
                        return accountIDs;
                    }
                    break;
                default:
                    break;
            }
            
        } catch (Exception e) {
            System.out.println("Failed to create statement in getAccountIDs");
            System.out.println(e);
            return accountIDs;
        }
        return accountIDs;
    }

    boolean acctBelongsToCustomer(int accountId, AccountType accountType){
        List<Integer> accounts = getAccountIDs(accountType);
        int i = 0;
        while(i < accounts.size()){
            if(accounts.get(i) == accountId){
                return true;
            }
            i++;
        }
        return false;
    }

    

}
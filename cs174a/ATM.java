package cs174a; 

import cs174a.Helper.*;
import cs174a.Customer.*;
import cs174a.Testable.*;
import java.util.*;
import java.sql.ResultSet;
import java.sql.Statement;


public class ATM {

    private Customer customer;
    private Helper helper;
    private App app;

    ATM(int taxID, App app){
        customer = new Customer(taxID, "default name", "default addr");
        helper = new Helper();
        this.app = app;
    }

    boolean verifyPin(int pin){
        if(pin == customer.getPin()){
            return true;
        }
        return false;
    }
    void setPin(int pin){
        customer.setPin(pin);
    }
    List<List<Integer>> getCustomerAccounts(){
        List<List<Integer>> res = new ArrayList<List<Integer>>();
        res.add(customer.getAccountIDs(AccountType.STUDENT_CHECKING));
        res.add(customer.getAccountIDs(AccountType.POCKET));
        return res;
    }
    //checks if account can accept deposits and if so, deposits balance into accountID
    //if account is already marked as closed for the month, reject deposit
    //update account balance
    //check accountID belongs to customer and that it's checkings or savings account
    boolean deposit(int accountID, double balance){
        boolean stud = customer.acctBelongsToCustomer(accountID, AccountType.STUDENT_CHECKING);
        boolean check = customer.acctBelongsToCustomer(accountID, AccountType.INTEREST_CHECKING);
        boolean sav = customer.acctBelongsToCustomer(accountID, AccountType.SAVINGS);
        if(!stud && !check && !sav){
            System.out.println("Customer does not own account-deposit");
            return false;
        }
        if(app.deposit(Integer.toString(accountID), balance) == "0"){
            return true;
        }
        return false;
    }

    //checks accountID is for a pocket account
    //takes specified amount of money out of the account it's linked w/
    //make sure the account it's linked w/ won't close after money is moved (reject if so)
    //return if money transfer was successful or not
    //check accountID belongs to customer
    //check if feePaid, if not, add $5
    boolean topUp(int accountID, double amount){
        boolean pock = customer.acctBelongsToCustomer(accountID, AccountType.POCKET);
        if(!pock){
            System.out.println("Pocket account doesn't belong to customer");
            return false;
        }
        if(app.topUp(Integer.toString(accountID), amount) == "0"){
            return true;
        }
        return false;
    }

    //subtracts amount from account associated w/ accountID
    /* returns 1 if failed... if success, returns .1 oldBalance newBalance */
    String withdraw(String accountID, double amount){
        boolean stud = customer.acctBelongsToCustomer(Integer.parseInt(accountID), AccountType.STUDENT_CHECKING);
        boolean check = customer.acctBelongsToCustomer(Integer.parseInt(accountID), AccountType.INTEREST_CHECKING);
        boolean sav = customer.acctBelongsToCustomer(Integer.parseInt(accountID), AccountType.SAVINGS);
        if(!stud && !check && !sav){
            System.out.println("Customer does not own account");
            return "1";
        }
        int aid = 0;
		String dbID = "";
		boolean accountExists = false;
		double oldBalance = 0.00;
		double newBalance = 0.00;
		String acctType = "";
		String result = "1 ";
        int isClosed = 0;
        String accountType = "";
		try{
			Statement stmt = helper.getConnection().createStatement();
			try {
			
				String sql = "SELECT * " +
								"FROM AccountPrimarilyOwns";
				ResultSet rs = stmt.executeQuery(sql);
				while(rs.next()){
					aid = rs.getInt("accountID");
					dbID = Integer.toString(aid);
					if(accountID.equals(dbID)){
                        accountExists = true;
                        accountType = rs.getString("accountType");
						break;
					}
				}
				if(accountExists == false){
					System.out.println("Account doesn't exist");
					rs.close();
					return "1";
                }
                else if(accountType.equals("POCKET")){
                    System.out.println("Cannot withdraw from pocket account");
                    return "1";
                }
				else{

					isClosed = rs.getInt("isClosed");
					if(isClosed == 1){
						rs.close();
						return "1";
					}
					oldBalance = rs.getDouble("balance");
					acctType = rs.getString("accountType");
					rs.close();
					if(amount <= 0.00){
						System.out.println("Cannot withdraw negative amount");
						result += String.format("%.2f",oldBalance) + " " + String.format("%.2f",newBalance);
						return result;					
					}
					if(acctType.equals("POCKET")){
						System.out.println("Cannot withdraw from pocket");
					}
					try {
					
                        newBalance = oldBalance - amount;
                        System.out.println(Double.toString(newBalance));
                        if(newBalance < 0.00){
                            System.out.println("Cannot withdraw more money than there is.");
                            return "1";
                        }
						sql = "UPDATE AccountPrimarilyOwns " +
							"SET balance = " + Double.toString(newBalance) + 
							" " + "WHERE accountId = " + dbID;
                        stmt.executeUpdate(sql);
                        if(newBalance <= 0.01){
                            sql = "UPDATE AccountPrimarilyOwns " +
                                    "SET isClosed = 1 " +
                                    "WHERE accountId = " + dbID;
                            stmt.executeUpdate(sql); 
                            sql = "UPDATE AccountPrimarilyOwns SET isClosed = 1 WHERE accountID = ("+
                            "SELECT aID FROM PocketAccountLinkedWith WHERE otherAccountID = " + dbID + ")";
                            stmt.executeUpdate(sql);
                        }
						result = "0 " + Double.toString(oldBalance) + " " + Double.toString(newBalance);
					} catch (Exception e) {
						System.out.println("Failed to deposit and add new balance to table");
						System.out.println(e);
						result += String.format("%.2f",oldBalance) + " " + String.format("%.2f",newBalance);
						return result;
					}
				}
			}catch (Exception e){
				System.out.println("Failed to check if account exists");
				System.out.println(e);
				return result;
			}
		}catch (Exception e){
			System.out.println("Failed to create statement in showBalance");
			System.out.println(e);
			return result;
		}
        System.out.println("Withdrew from account");
        //0 for check number and -1 for toAccount
		helper.addTransaction(amount,TransactionType.WITHDRAWAL,0,accountID,Integer.toString(-1));
		return result;
	}

	

    //check accountID belongs to customer and is a pocket account
    //subtract amount from account balance
    //check if feepaid, if not, minus $5
    /* return 1 if failed, return 0 oldBalance newBalance if success*/
    String purchase(String accountID, double amount){
        boolean pock = customer.acctBelongsToCustomer(Integer.parseInt(accountID), AccountType.POCKET);
        if(!pock){
            System.out.println("Customer doesn't own pocket");
            return "1";
        }
        try {
            Statement stmt = helper.getConnection().createStatement();
            //checks if account belongs to customer and if it's a pocket account
            int feePaid = 0;
            if(customer.acctBelongsToCustomer(Integer.parseInt(accountID), AccountType.POCKET)){
                if(amount <= 0.0){
                    System.out.println("Cannot purchase using a negative amount");
                    return "1";
                }
                
                String balance = "";
                try {
                    int aid = 0;
                    String dbID = "";
                    boolean accountExists = false;
           
                    String sql = "SELECT * " +
                                    "FROM AccountPrimarilyOwns";
                    ResultSet rs = stmt.executeQuery(sql);
                    int isClosed = 0;
                    while(rs.next()){
                        aid = rs.getInt("accountID");
                        dbID = Integer.toString(aid);
                        if(accountID.equals(dbID)){
                            accountExists = true;
                            isClosed = rs.getInt("isClosed");
                            // balance = rs.getDouble("balance");
                            break;
                        }
                    }
                    if(isClosed == 1){
                        return "1";
                    }
                    // rs.close();
                    if(accountExists == false){
                        rs.close();
                        return "1";
                    }
                    else{
                        balance = rs.getString("balance");
                        rs.close();
                    }
                }catch (Exception e){
                    System.out.println("Failed to check if account exists");
                    System.out.println(e);
                    return "1";
                }
                Double oldBalance = Double.parseDouble(balance);
       
                String sql = "SELECT * " + 
                            "FROM PocketAccountLinkedWith";
                ResultSet rs = stmt.executeQuery(sql);
                while(rs.next()){
                    int aid = rs.getInt("aID");
                    String dbID = Integer.toString(aid);
                    if(accountID.equals(dbID)){
                        feePaid = rs.getInt("feePaid");
                    }
                }
                Double newBalance = oldBalance - amount;
                
                if(feePaid == 0){
                    try{
                        helper.addTransaction(5,TransactionType.FEE,0,accountID,"-1");
                         newBalance -= 5;
                        sql = "UPDATE PocketAccountLinkedWith " +
                                "SET feePaid = " + Integer.toString(1) +
                                "WHERE aId = " + accountID;
                        stmt.executeUpdate(sql);
                    } catch(Exception e) {
                        System.out.println("Failed to update feePaid.");
                        System.out.println(e);
                        return "1";
                    }
                }
                newBalance = Double.parseDouble(String.format("%.2f",newBalance));
                if(newBalance<0){
                    System.out.println("Not enough funds to purchase.");
                    return "1";
                }
                if(newBalance <= 0.01 ){
                    sql = "UPDATE AccountPrimarilyOwns SET isClosed = 1 WHERE accountID = " + accountID;
                    stmt.executeUpdate(sql);
                }
                
                sql = "UPDATE AccountPrimarilyOwns " +
                            "SET balance = " + Double.toString(newBalance) + 
                            "WHERE accountId = " + accountID;
                stmt.executeUpdate(sql);
                //0 for check number and -1 for toAccount
                helper.addTransaction(amount, TransactionType.PURCHASE, 0, accountID, Integer.toString(-1));
                return "0 " + String.format("%.2f",oldBalance) + " " +String.format("%.2f", newBalance);
                
            }
            else{
                System.out.println("Account does not belong to customer or is not a pocket account");
                return "1";
            }
        } catch (Exception e) {
            System.out.println("Failed to create statement.");
            System.out.println(e);
            return "1";
        }
    }

    //return 0 if successful or 1 if not successful
    String transfer(int accountID, int destinationID, double amount){
        boolean student0 = customer.acctBelongsToCustomer(accountID, AccountType.STUDENT_CHECKING);
        boolean student1 = customer.acctBelongsToCustomer(destinationID, AccountType.STUDENT_CHECKING);
        boolean checking0 = customer.acctBelongsToCustomer(accountID,  AccountType.INTEREST_CHECKING);
        boolean checking1 = customer.acctBelongsToCustomer(destinationID, AccountType.INTEREST_CHECKING);
        boolean saving0 = customer.acctBelongsToCustomer(accountID, AccountType.SAVINGS);
        boolean saving1 = customer.acctBelongsToCustomer(destinationID, AccountType.SAVINGS);
        Double fromBalance1=0.0, fromBalance2=0.0, toBalance1=0.0, toBalance2 =0.0;
        int isClosed1=0, isClosed2=0;
        if(amount <= 0.0 || amount>2000){
            System.out.println("Cannot transfer negative amount");
            return "1";
        }
        if(amount>2000){
            System.out.println("Cannot transfer more than $2,000");
            return "1";
        }
        if(student0 || checking0 || saving0){
            if(student1 || checking1 || saving1){
                try {
                    Statement stmt = helper.getConnection().createStatement();
                    try {
                        //accountID
                        String sql = "SELECT * " +
                                    "FROM AccountPrimarilyOwns " +
                                    "WHERE accountId = " + Integer.toString(accountID);
                        ResultSet rs = stmt.executeQuery(sql);
                        while(rs.next()){
                            fromBalance1 = rs.getDouble("balance");
                            isClosed1 = rs.getInt("isClosed");
                        }
                        fromBalance2 = fromBalance1 - amount;
                        if(isClosed1 == 1){
                            System.out.println("From account already marked for closed... Can't transfer");
                            return "1";
                        }
                        //check if destinationID isClosed
                        sql = "SELECT * " +
                            "FROM AccountPrimarilyOwns " +
                            "WHERE accountId = " + Integer.toString(destinationID);
                        rs = stmt.executeQuery(sql);
                        while(rs.next()){
                            toBalance1 = rs.getDouble("balance");
                            isClosed2 = rs.getInt("isClosed");
                        }
                        if(isClosed2 == 1){
                            System.out.println("Destination account already marked for closed... Can't transer");
                            return "1";
                        }
                        if(fromBalance2 < 0){
                            System.out.println("Not enough funds to transfer.");
                            return "1";
                        }
                        else if(fromBalance2 <= 0.01){
                            sql = "UPDATE AccountPrimarilyOwns " +
                                    "SET isClosed = 1 " +
                                    "WHERE accountId = " + Integer.toString(accountID);
                            stmt.executeUpdate(sql);
                            sql = "UPDATE AccountPrimarilyOwns SET isClosed = 1 WHERE accountID = ("+
                            "SELECT aID FROM PocketAccountLinkedWith WHERE otherAccountID = " + Integer.toString(accountID) + ")";
                            stmt.executeUpdate(sql);
                        } 
                        fromBalance2 = Double.parseDouble(String.format("%.2f",fromBalance2));
                        sql = "UPDATE AccountPrimarilyOwns " +
                                "SET balance = " + Double.toString(fromBalance2) +
                                "WHERE accountId = " + Integer.toString(accountID);
                        stmt.executeUpdate(sql);
                        //destinationID
                        toBalance2 = toBalance1 + amount;
                        toBalance2 = Double.parseDouble(String.format("%.2f",toBalance2));
                        sql = "UPDATE AccountPrimarilyOwns " +
                                "SET balance = " + Double.toString(toBalance2) +
                                "WHERE accountId = " + Integer.toString(destinationID);
                        stmt.executeUpdate(sql);
                        helper.addTransaction(amount,TransactionType.TRANSFER,0,Integer.toString(accountID), Integer.toString(destinationID));
                        System.out.println("Transferred funds.");
                        return "0";
                    } catch (Exception e) {
                        System.out.println("Failed to add withdraw to tables");
                        System.out.println(e);
                        return "1";
                    }
                } catch (Exception e) {
                    System.out.println("Failed to create statement");
                    System.out.println(e);
                    return "1";
                }
            }
            System.out.println("Accounts either dne, is not CHECKINGS/SAVINGS type, customer is not owner, or accounts don't have common owner");
            return "1";
        }
        System.out.println("Accounts either dne, is not CHECKINGS/SAVINGS type, customer is not owner, or accounts don't have common owner");
        return "1";
    }

    //move amount from pocket account into account
    //make sure account is the one linked w/ that pocket account
    //check amount is not more than pocket account balance
    //apply 3% fee
    //return 0 if successful or 1 if not successful
    String collect(int accountID, int pocketID, double amount){
        boolean student0 = customer.acctBelongsToCustomer(accountID, AccountType.STUDENT_CHECKING);
        boolean checking0 = customer.acctBelongsToCustomer(accountID, AccountType.INTEREST_CHECKING);
        boolean saving0 = customer.acctBelongsToCustomer(accountID, AccountType.SAVINGS);
        boolean pocket = customer.acctBelongsToCustomer(pocketID, AccountType.POCKET);
        Double fromBalance1=0.0, fromBalance2=0.0, pocketBalance1=0.0, pocketBalance2=0.0;
        int isClosed1=0, isClosed2 =0;
        boolean isLinked=false;
        int feePaid = 0;
        if(pocket){
            if(student0 || checking0 || saving0){
                try {
                    Statement stmt = helper.getConnection().createStatement();
                    //check accounts are linked
                    try {
   
                        String sql = "SELECT * " +
                                        "FROM PocketAccountLinkedWith " +
                                        "WHERE aID = " + pocketID;
                        ResultSet rs = stmt.executeQuery(sql);
                        while(rs.next()){
                            if(pocketID==rs.getInt("aID")){
                                feePaid = rs.getInt("feePaid");
                                if(accountID == rs.getInt("otherAccountID")){
                                sql = "SELECT * " +
                                        "FROM AccountPrimarilyOwns ";
                                ResultSet rss = stmt.executeQuery(sql);
                                while(rss.next()){
                                    if(accountID==rss.getInt("accountID")){
                                        isClosed1 = rss.getInt("isClosed");
                                        fromBalance1 = rss.getDouble("balance");
                                        break;
                                    }
                                }
                                if(isClosed1 == 1){
                                    System.out.println("Cannot collect because account marked for isClosed");
                                    return "1";
                                }
                                if(amount <= 0.0){
                                    System.out.println("Cannot collect negative balance");
                                    return "1";
                                }
                                break;
                            }else{
                                System.out.println("Accounts are not linked");
                                return "1";
                            }
                        }
                        }
                        //check amount is greater than pocket account balance
                        try {
                
                            sql = "SELECT * " +
                                    "FROM AccountPrimarilyOwns ";
                            rs = stmt.executeQuery(sql);
                            while(rs.next()){
                                if(pocketID==rs.getInt("accountID")){
                                    pocketBalance1 = rs.getDouble("balance");
                                    isClosed2 = rs.getInt("isClosed");
                                    break;
                                }
                            }
                            if(isClosed2 == 1){
                                System.out.println("Cannot collect because account marked for isClosed");
                                return "1";
                            }
                            if(pocketBalance1 < amount){
                                System.out.println("Can't collect bc amount is greater than pocket account balance: "+pocketBalance1);
                                return "1";
                            }
                            if(feePaid == 0){
                                helper.addTransaction(5,TransactionType.FEE,0,Integer.toString(pocketID),"-1");
                                if(pocketBalance1 < amount + 5){
                                    System.out.println("Can't collect bc amount with fee is greater than pocket account balance: "+pocketBalance1);
                                    return "1";                                    
                                }
                                pocketBalance2 = pocketBalance1 - amount - 5; 
                                sql = "UPDATE PocketAccountLinkedWith " +
                                "SET feePaid = 1" +
                                " WHERE aID = " + Integer.toString(pocketID);
                                stmt.executeUpdate(sql);  
                            }else{
                                pocketBalance2 = pocketBalance1 - amount;
                            }
                            fromBalance2 = fromBalance1 + (amount * .97);
                            helper.addTransaction(amount*.03,TransactionType.FEE,0,Integer.toString(accountID),"-1");
                            fromBalance2 = Double.parseDouble(String.format("%.2f",fromBalance2));
                            pocketBalance2 = Double.parseDouble(String.format("%.2f",pocketBalance2));
                            //update balances for pocket and account
                            if(pocketBalance2<0){
                                System.out.println("Can't collect bc not enough funds");
                                return "1";
                            }
                            if(pocketBalance2<=0.01){
                                sql = "UPDATE AccountPrimarilyOwns " +
                                    "SET isClosed = 1" +
                                    " WHERE accountId = " + Integer.toString(pocketID);
                                stmt.executeUpdate(sql);
                            }
               
                            sql = "UPDATE AccountPrimarilyOwns " +
                                    "SET balance = " + Double.toString(pocketBalance2) +
                                    " WHERE accountId = " + Integer.toString(pocketID);
                            stmt.executeUpdate(sql);
                            sql = "UPDATE AccountPrimarilyOwns " +
                                    "SET balance = " + Double.toString(fromBalance2) +
                                    " WHERE accountId = " + Integer.toString(accountID);
                            stmt.executeUpdate(sql);
                            helper.addTransaction(amount,TransactionType.COLLECT,0,Integer.toString(pocketID), Integer.toString(accountID));
                            System.out.println("Collected from pocket account.");
                            return "0";
                        } catch (Exception e) {
                            System.out.println("Failed to update balance for pocket or from account");
                            System.out.println(e);
                            return "1";
                        }
                    } catch (Exception e) {
                        System.out.println("Failed to check if accounts are linked");
                        System.out.println(e);
                        return "1";
                    }
                } catch (Exception e) {
                    System.out.println("Failed to create statement");
                    System.out.println(e);
                    return "1";
                }
            }
            System.out.println("AccountID is pocket account and cannot be collected into");
            return "1";
        }
        System.out.println("PocketID does not refer to a pocket account.");
        return "1";
    }

    //apply 2% fee
    //subtracts amount from accountID and adds to destinationId
    //close accountID if necessary
    String wire(int accountID, int destinationID, double amount){
   
        boolean student0 = customer.acctBelongsToCustomer(accountID, AccountType.STUDENT_CHECKING);
        boolean checking0 = customer.acctBelongsToCustomer(accountID, AccountType.INTEREST_CHECKING);
        boolean saving0 = customer.acctBelongsToCustomer(accountID, AccountType.SAVINGS);
        Double fromBalance1=0.0, fromBalance2=0.0, toBalance1=0.0, toBalance2=0.0;
        int isClosed1=0, isClosed2=0;
        String accountType = "";
        if(amount <= 0.0){
            System.out.println("Cannot wire negative amount");
            return "1";
        }
        if(student0 || checking0 || saving0){
                try {
                    Statement stmt = helper.getConnection().createStatement();
                    try {
                        
               
                        String sql = "SELECT * " +
                                    "FROM AccountPrimarilyOwns " +
                                    "WHERE accountId = " + Integer.toString(accountID);
                        ResultSet rs = stmt.executeQuery(sql);
                        while(rs.next()){
                            fromBalance1 = rs.getDouble("balance");
                            isClosed1 = rs.getInt("isClosed");
                        }
                        fromBalance2 = fromBalance1 - amount;
                        if(isClosed1 == 1){
                            System.out.println("From account already marked for closed... Can't transfer");
                            return "1";
                        }
                     
                        //check if destinationID isClosed
                        sql = "SELECT * " +
                            "FROM AccountPrimarilyOwns " +
                            "WHERE accountId = " + Integer.toString(destinationID);
                        rs = stmt.executeQuery(sql);
                        while(rs.next()){
                            toBalance1 = rs.getDouble("balance");
                            isClosed2 = rs.getInt("isClosed");
                            accountType = rs.getString("accountType");
                        }
                        if(isClosed2 == 1){
                            System.out.println("Destination account already marked for closed... Can't transfer");
                            return "1";
                        }
                        if(accountType.equals("POCKET")){
                            System.out.println("Can only wire into a checkings or savings account");
                            return "1";
                        }
               
                        if(fromBalance2 <= 0.0){
                            System.out.println("Can't transfer bc from account will have negative/0 balance");
                            return "1";
                        }
                        else if(fromBalance2 <= 0.01){
                            sql = "UPDATE AccountPrimarilyOwns " +
                                    "SET isClosed = 1 " +
                                    "WHERE accountId = " + Integer.toString(accountID);
                            stmt.executeUpdate(sql);
                            sql = "UPDATE AccountPrimarilyOwns SET isClosed = 1 WHERE accountID = ("+
                            "SELECT aID FROM PocketAccountLinkedWith WHERE otherAccountID = " + Integer.toString(accountID) + ")";
                            stmt.executeUpdate(sql);
                        }
                        fromBalance2 = Double.parseDouble(String.format("%.2f",fromBalance2));
                        sql = "UPDATE AccountPrimarilyOwns " +
                                "SET balance = " + Double.toString(fromBalance2) +
                                "WHERE accountId = " + Integer.toString(accountID);
                        stmt.executeUpdate(sql);
                        //destinationID
                        toBalance2 = toBalance1 + (amount * .98);
                        helper.addTransaction(amount*.02,TransactionType.FEE,0,Integer.toString(destinationID),"-1");
                        toBalance2 = Double.parseDouble(String.format("%.2f",toBalance2));
                        sql = "UPDATE AccountPrimarilyOwns " +
                                "SET balance = " + Double.toString(toBalance2) +
                                "WHERE accountId = " + Integer.toString(destinationID);
                        stmt.executeUpdate(sql);
                        helper.addTransaction(amount,TransactionType.WIRE,0,Integer.toString(accountID), Integer.toString(destinationID));
                        System.out.println("Wire successful.");
                        return "0";
                    } catch (Exception e) {
                        System.out.println("Failed to add withdraw to tables");
                        System.out.println(e);
                        return "1";
                    }
                } catch (Exception e) {
                    System.out.println("Failed to create statement");
                    System.out.println(e);
                    return "1";
                }
        }
        System.out.println("Can only wire from checkings or savings account");
        return "1";
    }

    //make sure both accounts are pocket accounts or else return false
    //take amount out of pocketID and add to friend TaxID
    boolean payFriend(int pocketID, int friendaccountID, double amount){
        boolean pock = customer.acctBelongsToCustomer(pocketID, AccountType.POCKET);
        if(!pock){
            System.out.println("Pocket account doesn't belong to customer");
            return false;
        }
        if(app.payFriend(Integer.toString(pocketID), Integer.toString(friendaccountID), amount) == "0"){
            return true;
        }
        return false;
    }


}
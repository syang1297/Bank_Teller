package cs174a; 

import cs174a.Helper.*;
import cs174a.Customer.*;
import cs174a.Testable.*;

import java.sql.ResultSet;
import java.sql.Statement;
import oracle.jdbc.OracleConnection;


public class ATM {

    private Customer customer;
    private Helper helper;
    private App app;

    ATM(){
        //TODO: how should we init customer?
        customer = new Customer(1234);
        helper = new Helper();
        app = new App();
    }

    //takes inserted pin and checks it against customer's getpin()
    boolean verifyPin(int pin){
        if(customer.hashPin(pin) == customer.getPin()){
            return true;
        }
        return false;
    }

    //checks if account can accept deposits and if so, deposits balance into accountID
    //if account is already marked as closed for the month, reject deposit
    //update account balance
    //check accountID belongs to customer and that it's checkings or savings account
    void deposit(double balance, int accountID){
        return;
    }

    //checks accountID is for a pocket account
    //takes specified amount of money out of the account it's linked w/
    //make sure the account it's linked w/ won't close after money is moved (reject if so)
    //return if money transfer was successful or not
    //check accountID belongs to customer
    //check if feePaid, if not, add $5
    boolean topUp(int accountID, double amount){
        return false;
    }

    //subtracts amount from account associated w/ accountID
    //TODO: check accountID belongs to customer and account is checkings or savings
    /* returns 1 if failed... if success, returns 0 oldBalance newBalance */
    String withdraw(String accountID, double amount){
        int aid = 0;
		String dbID = "";
		boolean accountExists = false;
		double oldBalance = 0.00;
		double newBalance = 0.00;
		String acctType = "";
		String result = "1 ";
		int isClosed = 0;
		try{
			Statement stmt = helper.getConnection().createStatement();
			try {
				System.out.println("Checking if accountID exists...");
				String sql = "SELECT * " +
								"FROM AccountPrimarilyOwns";
				ResultSet rs = stmt.executeQuery(sql);
				while(rs.next()){
					aid = rs.getInt("accountID");
					dbID = Integer.toString(aid);
					if(accountID.equals(dbID)){
						accountExists = true;
						break;
					}
				}
				if(accountExists == false){
					System.out.println("Account doesn't exist");
					rs.close();
					return "1";
				}
				else{
					System.out.println("Account exists");
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
						result += Double.toString(oldBalance) + " " + Double.toString(newBalance);
						return result;					
					}
					if(acctType.equals("POCKET")){
						System.out.println("Cannot withdraw from pocket");
					}
					try {
						System.out.println("Updating balances...");
                        newBalance = oldBalance - amount;
                        if(newBalance < 0.0){
                            System.out.println("Cannot withdraw more money than there is.")
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
                        }
						result = "0 " + Double.toString(oldBalance) + " " + Double.toString(newBalance);
					} catch (Exception e) {
						System.out.println("Failed to deposit and add new balance to table");
						System.out.println(e);
						result += Double.toString(oldBalance) + " " + Double.toString(newBalance);
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
		helper.addTransaction(amount,TransactionType.WITHDRAWAL,0,accountID);
		return result;
	}

	

    //check accountID belongs to customer and is a pocket account
    //subtract amount from account balance
    //check if feepaid, if not, minus $5
    //TODO: check if account belongs to customer
    /* return 1 if failed, return 0 oldBalance newBalance if success*/
    String purchase(String accountID, double amount){
        try {
            Statement stmt = helper.getConnection().createStatement();
            //checks if account belongs to customer and if it's a pocket account
            int feePaid = 0;
            if(customer.acctBelongsToCustomer(Integer.parseInt(accountID), customer.getTaxID(), AccountType.POCKET)){
                if(amount <= 0.0){
                    System.out.println("Cannot purchase using a negative amount");
                    return "1";
                }
                String balance = app.showBalance(accountID);
                Double oldBalance = Double.parseDouble(balance);
                String sql = "SELECT * " + 
                            "FROM PocketAccountLinkedWith";
                ResultSet rs = stmt.executeQuery(sql);
                while(rs.next()){
                    int aid = rs.getInt("accountId");
                    String dbID = Integer.toString(aid);
                    if(accountID.equals(dbID)){
                        feePaid = rs.getInt("feePaid");
                    }
                }
                Double newBalance = oldBalance - amount;
                if(feePaid == 0){
                    newBalance -= 5;
                    sql = "UPDATE AccountPrimarilyOwns " +
                            "SET feePaid = " + Integer.toString(1) +
                            "WHERE accountId = " + accountID;
                    stmt.executeUpdate(sql);
                }
                if(newBalance <= 0 ){
                    System.out.println("Purchase cannot be made bc not enough funds in pocket");
                    return "1";
                }
                sql = "UPDATE AccountPrimarilyOwns " +
                            "SET balance = " + Double.toString(newBalance) + 
                            "WHERE accountId = " + accountID;
                stmt.executeUpdate(sql);
                return "0 " + balance + " " + Double.toString(newBalance);
                //TODO: isClosed helper
                
            }
            else{
                return "1";
            }
        } catch (Exception e) {
            System.out.println("Failed to create statement.");
            System.out.println(e);
            return "1";
        }
    }

    //TODO: check both accounts belongs to customer
    //check that both accounts are either checkings or savings
    //check amount is able to be moved and mark account as closed if it needs to be
    //also make sure init account is not marked for closed
    //subtract amount from accountID and add to destination account
    //return is successful or not successful
    boolean transfer(int accountID, int destinationID, double amount){
        return false;
    }

    //move amount from pocket account into account
    //make sure account is the one linked w/ that pocket account
    //check amount is not more than pocket account balance
    //apply 3% fee
    //TODO: what happens if pocket balance = 0
    boolean collect(int accountID, int pocketID, double amount){
        return false;
    }

    //TODOL make sure current customer owns accountID and both accounts are checkings/savings
    //apply 2% fee
    //subtracts amount from accountID and adds to destinationId
    //close accountID if necessary
    boolean wire(int accountID, int destinationID, double amount){
        return false;
    }

    //make sure both accounts are pocket accounts or else return false
    //take amount out of pocketID and add to friend TaxID
    boolean payFriend(int pocketID, int friendTaxID, int friendID, double amount){
        return false;
    }


}
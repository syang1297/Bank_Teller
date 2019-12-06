package cs174a;                         // THE BASE PACKAGE FOR YOUR APP MUST BE THIS ONE.  But you may add subpackages.
//for debugging
import java.sql.Statement;
// DO NOT REMOVE THIS IMPORT.
import cs174a.Testable.*;
import cs174a.Helper.*;
import cs174a.Customer.*;
import cs174a.ATM.*;
import cs174a.Teller.*;
import java.util.*;

/**
 * This is the class that launches your application.
 * DO NOT CHANGE ITS NAME.
 * DO NOT MOVE TO ANY OTHER (SUB)PACKAGE.
 * There's only one "main" method, it should be defined within this Main class, and its signature should not be changed.
 */
public class Main
{
	/**
	 * Program entry point.
	 * DO NOT CHANGE ITS NAME.
	 * DON'T CHANGE THE //!### TAGS EITHER.  If you delete them your program won't run our tests.
	 * No other function should be enclosed by the //!### tags.
	 */
	//!### COMENZAMOS
	public static void main( String[] args )
	{
		App app = new App();                        // We need the default constructor of your App implementation.  Make sure such
													// constructor exists.
		String r = app.initializeSystem( );          // We'll always call this function before testing your system.
		if( r.equals( "0" ) )
		{
			app.dropTables();
			// Helper helper= new Helper();
			// //for testing app.java
			r = app.createTables();
			app.setDate(2018, 2, 7);
			r = app.createCheckingSavingsAccount(AccountType.INTEREST_CHECKING, "1234", 1000.00, "4321", "Bob", "66 DP");
			// // System.out.println(r);
			// r = app.createCheckingSavingsAccount(AccountType.SAVINGS, "1233", 0.0, "4321", "Bob", "66 DP");
			// r = app.createPocketAccount("1", "1234", 500.0, "4321");
			// r = app.createPocketAccount("2", "1234", 100.0, "4321");
			// r = app.createCustomer("1234", "1111","Andrew","66 Sueno");
			// r = app.deposit("1234",1000.00);
			// r = app.topUp("1", 10.00);
			// app.topUp("1", 4.0);
			// r = app.topUp("2", 200.00);
			// r = app.payFriend("1","2",50);
			// r = app.listClosedAccounts();
			// //for testing customer.java
			// // r = helper.getDate();
			// // System.out.println(r);
			// // r = helper.getDate();
			// // System.out.println(r);
			// // r = Integer.toString(customer.getPin());
			// // System.out.println(r);
			// // r = customer.setPin(8888);
			// // r = Integer.toString(customer.getPin());
			// // System.out.println(r);
			// // r = customer.setName("Bob");
			// // r = customer.getName();
			// // System.out.println(r);
			// // r = customer.setName("Shu");
			// // r = customer.getName();
			// // System.out.println(r);
			// // List<Integer> res=customer.getAccountIDs(customer.getTaxID(),AccountType.STUDENT_CHECKING);
			// // for(int i =0;i<res.size();i++){
			// // 	System.out.println("AccountID "+Integer.toString(i)+": "+res.get(i));
			// // }
			// // System.out.println(customer.acctBelongsToCustomer(1234,AccountType.STUDENT_CHECKING));
			// // System.out.println(helper.hashPin(1234));
			// // System.out.println(helper.unhashPin("\"#$%"));
			// // for testing ATM
			// ATM atm = new ATM(4321,app);
			// System.out.println(atm.verifyPin(1717));
			// System.out.println(atm.withdraw("1234",300));
			// System.out.println(atm.purchase("1",10));
			// System.out.println(atm.transfer(1234,1233,100));
			// System.out.println(atm.collect(1234,1,200));
			// System.out.println(atm.wire(1234,1233,19));
			
			// //for testing Teller
			// Teller teller = new Teller (app);
			// System.out.println(teller.customerOwnsAccount("4321","1234"));
			// teller.changeInterestRate(1234,4321,1.4);
			// //teller.deleteTransactionHistory();
			// // String sql = "DELETE FROM Owns " +
            // //                     "WHERE tID = 1111";
			// // try{
			// // 	Statement stmt = helper.getConnection().createStatement();
			// // 	stmt.executeUpdate(sql);
				
			// // } catch (Exception e){
			// // 	System.out.println(e);
			// // }
			// //teller.deleteCustomers();
			// // sql = "UPDATE AccountPrimarilyOwns " +
            // //                         "SET isClosed = 1" + 
            // //                         " WHERE accountId = 1";
			// // try{
			// // 	Statement stmt = helper.getConnection().createStatement();
			// // 	stmt.executeUpdate(sql);
				
			// // app.deposit("1233",100.00);
			// // app.setDate(2018, 2, 14);
			// app.deposit("1233",10000.00);
			// app.setDate(2018, 2, 25);
			// app.deposit("1233",10000.00);
			// app.setDate(2018, 2, 28);
			// app.deposit("1233",3000.00);
			// app.setDate(2018, 2, 8);
			// app.deposit("1233",10000.00);
			// app.setDate(2018, 2, 10);
			// app.deposit("1233",100.00);
			// app.setDate(2018, 2, 14);
			// app.deposit("1233",10000.00);
			// app.setDate(2018, 2, 25);
			// app.deposit("1233",10000.00);
			// app.setDate(2018, 2, 28);
			// app.deposit("1233",3000.00);
			// //teller.addInterest();
			// //teller.writeCheck(1234,100.03);
			// //atm.withdraw("1234",1420.96);
			// System.out.println(atm.collect(1234,2,244.99));
			// atm.purchase("2",100);
			// //atm.transfer(1233,1234,1420.96);
			// List<String> res2 = teller.generateMonthly(4321);
			// for(int i=0;i<res2.size();i++){
			// 	System.out.println(res2.get(i));
			// }
			// res2 = teller.customerReport(4321);
			// for(int i=0;i<res2.size();i++){
			// 	System.out.println(res2.get(i));
			// }
			// res2 = teller.generateDTER();
			// for(int i=0;i<res2.size();i++){
			// 	System.out.println(res2.get(i));
			// }

			// start of GUI code
			String input ="";
			Teller teller = new Teller (app);
			//drop tables
			//load in their data/create tables

			while(true){
				System.out.println("\nWelcome to Andrew and Shu's Bank, enter 0 for ATM, 1 for teller, or -1 to exit.");
				input = System.console().readLine();
				switch(input){
					case "0"://ATM
						while(true){
							System.out.println("\nPlease enter your taxID");
							String taxID = System.console().readLine();
							ATM atm = new ATM(Integer.parseInt(taxID),app);
							while(true){
								System.out.println("\nPlease enter your PIN or enter 0 to exit");
								input = System.console().readLine();
								if (input.equals("0")){
									break;
								}
								else if(atm.verifyPin(Integer.parseInt(input))){
									System.out.println("Verified.");
									break;
								} else {
									System.out.println("Try again.");
								}
							}
							if(input.equals("0")){
								break;
							}
							while(true){
								String acc = "";
								String amt="";
								String acc2 = "";
								List<List<Integer>> accounts = atm.getCustomerAccounts();
								String temp ="\nAccounts: ";
								temp=temp+accounts.get(0);
								temp=temp +"\nPocket Accounts: ";
								temp=temp+accounts.get(1);
								System.out.println(temp);
								System.out.println("Select a transaction:\n(0)Deposit\n(1)Top-up\n(2)Withdrawal\n(3)Transfer\n(4)Collect\n(5)Wire\n(6)Pay-friend\n(7)Exit\n");
								input = System.console().readLine();
								if(input.equals("6") || input.equals("1")){
									System.out.println("Enter a pocket account.");
								} else if(!input.equals("7")){
									System.out.println("Enter an account.");
								}
								if(!input.equals("7")){
									System.console().readLine();
								}
								switch(input){
									case "0":
										System.out.println("Enter an amount.");
										 amt = System.console().readLine();
										atm.deposit(Integer.parseInt(acc),Double.parseDouble(amt));
										break;
									case "1":
										System.out.println("Enter an amount.");
										 amt = System.console().readLine();
										atm.topUp(Integer.parseInt(acc),Double.parseDouble(amt));
										break;
									case "2":
										System.out.println("Enter an amount.");
										 amt = System.console().readLine();
										atm.withdraw(acc,Double.parseDouble(amt));
										break;
									case "3":
										System.out.println("Enter another account.");
										 acc2 = System.console().readLine();
										System.out.println("Enter an amount.");
										 amt = System.console().readLine();
										atm.transfer(Integer.parseInt(acc),Integer.parseInt(acc2),Double.parseDouble(amt));
										break;
									case "4":
										System.out.println("Enter a pocket account.");
										 acc2 = System.console().readLine();
										System.out.println("Enter an amount.");
										 amt = System.console().readLine();
										atm.collect(Integer.parseInt(acc),Integer.parseInt(acc2),Double.parseDouble(amt));
										break;
									case "5":
										System.out.println("Enter another account.");
										 acc2 = System.console().readLine();
										System.out.println("Enter an amount.");
										 amt = System.console().readLine();
										atm.wire(Integer.parseInt(acc),Integer.parseInt(acc2),Double.parseDouble(amt));
										break;
									case "6":
										System.out.println("Enter another pocket account.");
										 acc2 = System.console().readLine();
										System.out.println("Enter an amount.");
										 amt = System.console().readLine();
										atm.payFriend(Integer.parseInt(acc),Integer.parseInt(acc2),Double.parseDouble(amt));
										break;
									case "7":
										break;
								}
								if(input.equals("7")){
									break;
								}
							}
							if(input.equals("7")){
									break;
							}
						}
						break;
					case "1"://TELLER
						while(true){
						System.out.println("Welcome to Teller Interface");
						System.out.println("Select a transaction:\n(0)Enter Check Transaction\n(1)Generate Monthly Statemet\n(2)List Closed Accounts\n(3)DTER\n(4)Customer Report\n(5)Add Interest\n(6)Create Account\n(7)Delete Closed Acounts\n(8)Delete Customers\n(9)Delete Transactions\n(10)Go Back to Main\n(-1)Exit");
						String inp = System.console().readLine();
						switch(Integer.parseInt(inp)){
							case 0: //enter check
								// while(true){
								String check = "";
								System.out.println("Writing a check......");
								System.out.println("Enter accountID for check");
								check = System.console().readLine();
								String accountId = check;
								System.out.println("Enter amount for check");
								check = System.console().readLine();
								String amount = check;
								System.out.println("Enter customer taxID");
								check = System.console().readLine();
								String taxID = check;
								teller.writeCheck(Integer.parseInt(accountId), Double.parseDouble(amount), Integer.parseInt(taxID));
								System.out.println("Check written");
								break;
							case 1: //monthly statement
								System.out.println("Printing monthly statement.....");
								String tax = "";
								System.out.println("Enter taxID for customer");
								tax = System.console().readLine();
								teller.generateMonthly(Integer.parseInt(tax));
								break;
							case 2: //closed accounts
							break;

							case 3: //DTER
							break;

							case 4: //customer report
							break;

							case 5: //add interest
							break;

							case 6: //create account
							break;

							case 7: //delete accounts
							break;

							case 8: //delete customers
							break;

							case 9: //delete transactions
							break;

							case 10: //back to main
							break;

							case -1: //exit
								break;
						}
						if(input.equals("7")){
							break;
						}
					}
					break;

					case "2":
					while(true){
						String in = "";
						System.out.println("Welcome to set date");
						System.out.println("Enter year");
						in = System.console().readLine();
						String year = in;
						System.out.println("Enter month");
						in = System.console().readLine();
						String month = in;
						System.out.println("Enter day");
						in = System.console().readLine();
						String day = in;
						app.setDate(Integer.parseInt(year), Integer.parseInt(month), Integer.parseInt(day));
						// System.out.println("Welcome to Andrew and Shu's Bank, enter 0 for ATM, 1 for teller, 2 to set date, or -1 to exit.");
						// input = System.console().readLine();
						break;
					}
					break;
					case "-1":
						System.out.println("Thanks for using our bank.");
						break;
				}
				if(input.equals("-1")){
					break;
				}
			}
			
		}
	}
	//!### FINALIZAMOS
}

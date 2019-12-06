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
			// app.dropTables();
			// Helper helper= new Helper();
			// //for testing app.java
			// r = app.createTables();
			// app.setDate(2018, 2, 7);
			// r = app.createCheckingSavingsAccount(AccountType.INTEREST_CHECKING, "1234", 1000.00, "4321", "Bob", "66 DP");
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
			// List<List<String>> coOwners = new ArrayList<List<String>>();
			// teller.createAccount(AccountType.INTEREST_CHECKING,coOwners,1000,"12345","4321","0");

			// start of GUI code
			String input ="";
			while(true){
				System.out.println("\nWelcome to Andrew and Shu's Bank, enter 0 for ATM, 1 for teller, 2 for setDate, or -1 to exit.");
				input = System.console().readLine();
				switch(input){
					case "0"://ATM
						while(true){
							System.out.println("\n--------------Welcome to the ATM Interface-------------");
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
								System.out.println("\n--------------Welcome to the ATM Interface-------------");
								System.out.println("Select a transaction:\n(0)Deposit\n(1)Top-up\n(2)Withdrawal\n(3)Transfer\n(4)Collect\n(5)Wire\n(6)Pay-friend\n(7)Set Pin\n(8)Exit\n");
								input = System.console().readLine();
								if(input.equals("7")){
									System.out.println("Enter new pin");
								}
								else if(input.equals("6") || input.equals("1")){
									System.out.println("Enter a pocket account.");
								} else if(!input.equals("7")){
									System.out.println("Enter an account.");
								}
								if(!input.equals("8")){
									acc = System.console().readLine();
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
										atm.setPin(Integer.parseInt(acc));
										break;
									case "8":
										break;
								}
								if(input.equals("8")){
									break;
								}
							}
							if(input.equals("8")){
									break;
							}
						}
						break;
					case "1"://TELLER
						break;
					case "2"://SETDATE
						while(true){
							String year="";
							String month="";
							String day="";
							System.out.println("Enter the year");
							year = System.console().readLine();
							System.out.println("Enter the month");
							month = System.console().readLine();
							System.out.println("Enter the day");
							day = System.console().readLine();
							if(app.setDate(Integer.parseInt(year),Integer.parseInt(month),Integer.parseInt(day)).charAt(0)==('0')){
								break;
							} else {
								System.out.println("Try again.");
							}
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

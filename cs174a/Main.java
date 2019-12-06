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
			
			//demo preload code

			app.dropTables();
			app.createTables();
			app.setDate(2011, 3, 1);
			Teller teller = new Teller(app);
			Helper helper = new Helper();

			List<String> cindy = new ArrayList();
			cindy.add("412231856");
			cindy.add("Cindy Laugher");
			cindy.add("7000 Hollister");
			ATM atm = new ATM(412231856, app);
			atm.setPin(3764);

			List<String> ivan = new ArrayList();
			ivan.add("322175130");
			ivan.add("Ivan Lendme");
			ivan.add("1235 Johnson Dr");
			atm = new ATM(322175130, app);
			atm.setPin(3764);

			List<List<String>> co = new ArrayList();
			co.add(cindy);
			co.add(ivan);
			teller.createAccount(AccountType.STUDENT_CHECKING, co, 1200.00, "17431", "344151573", "-1", "Joe Pepsi", "3210 State St");
			helper.setBranch("17431","San Francisco");
			atm = new ATM(344151573, app);
			atm.setPin(3692);

			List<String> eliz = new ArrayList();
			eliz.add("122219876");
			eliz.add("Elizabeth Sailor");
			eliz.add("4321 State St");
			atm = new ATM(122219876, app);
			atm.setPin(3856);

			List<String> nam = new ArrayList();
			nam.add("203491209");
			nam.add("Nam-Hoi Chung");
			nam.add("1997 Peoples St HK");
			atm = new ATM(203491209, app);
			atm.setPin(5340);

			co.removeAll(co);
			co.add(cindy);
			co.add(eliz);
			co.add(nam);
			teller.createAccount(AccountType.STUDENT_CHECKING, co, 21000.00, "54321", "212431965", "-1", "Hurryson Ford ", "678 State St");
			helper.setBranch("54321","Los Angeles");
			atm = new ATM(212431965, app);
			atm.setPin(3532);

			co.removeAll(co);
			teller.createAccount(AccountType.STUDENT_CHECKING, co, 1200.00, "12121", "207843218", "-1", "David Copperfill", "1357 State St");
			helper.setBranch("12121","Goleta");
			atm = new ATM(207843218, app);
			atm.setPin(8582);

			List<String> fatal = new ArrayList();
			fatal.add("401605312");
			fatal.add("Fatal Castro");
			fatal.add("3756 La Cumbre Plaza");
			atm = new ATM(401605312, app);
			atm.setPin(8193);

			List<String> billy = new ArrayList();
			billy.add("231403227");
			billy.add("Billy Clinton");
			billy.add("5777 Hollister");
			atm = new ATM(231403227, app);
			atm.setPin(1468);

			co.removeAll(co);
			co.add(fatal);
			co.add(billy);

			teller.createAccount(AccountType.INTEREST_CHECKING, co, 15000.00, "41725", "201674933", "-1", "George Brush", "5346 Foothill Av");
			helper.setBranch("41725","Los Angeles");
			atm = new ATM(201674933, app);
			atm.setPin(9824);


			List<String> olive = new ArrayList();
			olive.add("210389768");
			olive.add("Olive Stoner");
			olive.add("6689 El Colegio #151");
			atm = new ATM(210389768, app);
			atm.setPin(8452);

			List<String> magic = new ArrayList();
			magic.add("188212217");
			magic.add("Magic Jordon");
			magic.add("3852 Court Rd");
			atm = new ATM(188212217, app);
			atm.setPin(7351);

			co.removeAll(co);
			co.add(magic);

			teller.createAccount(AccountType.INTEREST_CHECKING, co, 8456.00, "76543", "212116070", "-1", "Li Kung", "2 Peoples Rd Beijing");
			helper.setBranch("76543","Santa Barbara");
			atm = new ATM(212116070, app);
			atm.setPin(9173);
			co.removeAll(co);
			
			co.removeAll(co);
			co.add(magic);
			co.add(olive);
			co.add(eliz);
			co.add(nam);
			teller.createAccount(AccountType.INTEREST_CHECKING, co, 2000000.00, "93156", "209378521", "-1", "Kelvin Costner", "Santa Cruz #3579");
			helper.setBranch("93156","Goleta");
			atm = new ATM(209378521, app);
			atm.setPin(4659);

			List<String> pit = new ArrayList();
			pit.add("400651982");
			pit.add("Pit Wilson");
			pit.add("911 State St");
			atm = new ATM(400651982 , app);
			atm.setPin(1821);

			List<String> hurryson = new ArrayList();
			hurryson.add("212431965");
			hurryson.add("Hurryson Ford");
			hurryson.add("678 State St");
			atm = new ATM(212431965, app);
			atm.setPin(3532);

			co.removeAll(co);
			co.add(pit);
			co.add(hurryson);
			co.add(ivan);

			teller.createAccount(AccountType.SAVINGS, co, 1289.00, "43942", "361721022", "-1", "Alfred Hitchcock", "6667 El Colegio #40");
			helper.setBranch("43942","Santa Barbara");
			atm = new ATM(361721022, app);
			atm.setPin(1234);

			List<String> li = new ArrayList();
			li.add("212116070");
			li.add("Li Kung");
			li.add("2 People's Rd Beijing");

			co.removeAll(co);
			co.add(li);
			co.add(olive);

			teller.createAccount(AccountType.SAVINGS, co, 34000.00, "29107", "209378521", "-1", "Kelvin Costner", "Santa Cruz #3579");
			helper.setBranch("29107","Los Angeles");
			// atm = ATM(29107);
			// atm.setPin(4659);
			// app.createPocketAccount("43947","29107",30,"400651982");

			List<String> george = new ArrayList();
			george.add("201674933");
			george.add("George Brush");
			george.add("5346 Foothill Av");

			co.removeAll(co);
			co.add(george);
			co.add(fatal);

			teller.createAccount(AccountType.SAVINGS, co, 2300.00, "19023", "412231856", "-1", "Cindy Laugher", "7000 Hollister");
			helper.setBranch("19023","San Francisco");
			// atm = ATM(19023);
			// atm.setPin(3764);


			List<String> david = new ArrayList();
			david.add("207843218");
			david.add("David Copperfield");
			david.add("1357 State St");
			atm = new ATM(207843218, app);
			atm.setPin(8582);

			List<String> joe = new ArrayList();
			joe.add("344151573");
			joe.add("Joe Pepsi");
			joe.add("3210 State St");

			co.removeAll(co);
			co.add(david);
			co.add(eliz);
			co.add(joe);
			co.add(nam);
			co.add(olive);

			teller.createAccount(AccountType.SAVINGS, co, 1000.00, "32156", "188212217", "-1", "Magic Jordon", "3852 Court Rd");
			helper.setBranch("32156","Goleta");
			// atm = ATM(32156);
			// atm.setPin(7351);

			co.removeAll(co);
			System.out.println("fatal");
			teller.createAccount(AccountType.POCKET, co, 100.00, "67521", "401605312", "19023", "Fatal Castro", "default");
			System.out.println("David");

			teller.createAccount(AccountType.POCKET, co, 50.00, "53027", "207843218", "12121", "David", "default");
			System.out.println("pit");

			teller.createAccount(AccountType.POCKET, co, 20.00, "60413", "400651982", "43942", "Pit Wilson", "default");
			System.out.println("li");

			teller.createAccount(AccountType.POCKET, co, 30.00, "43947", "212116070", "29107", "Li Kung", "default");
			// teller.createAccount(AccountType.POCKET, co, 100.00, "67521", "401605312", "19023", "Fatal Castro", "default", "default");

			

			// // start of GUI code
			// String input ="";
			// Teller teller = new Teller (app);
			// Helper helper = new Helper();
			// //drop tables
			// //load in their data/create tables

			// while(true){
			// 	System.out.println("\nWelcome to Andrew and Shu's Bank, enter \n(0)for ATM\n(1)for teller\n(2)for set date\n(3)for reset monthly\n(-1)to exit.");
			// 	input = System.console().readLine();
			// 	switch(input){
			// 		case "0"://ATM
			// 			while(true){
			// 				System.out.println("\n--------------Welcome to the ATM Interface-------------");
			// 				System.out.println("\nPlease enter your taxID");
			// 				String taxID = System.console().readLine();
			// 				ATM atm = new ATM(Integer.parseInt(taxID),app);
			// 				while(true){
			// 					System.out.println("\nPlease enter your PIN or enter 0 to exit");
			// 					input = System.console().readLine();
			// 					if (input.equals("0")){
			// 						break;
			// 					}
			// 					else if(atm.verifyPin(Integer.parseInt(input))){
			// 						System.out.println("Verified.");
			// 						break;
			// 					} else {
			// 						System.out.println("Try again.");
			// 					}
			// 				}
			// 				if(input.equals("0")){
			// 					break;
			// 				}
			// 				while(true){
			// 					String acc = "";
			// 					String amt="";
			// 					String acc2 = "";
			// 					List<List<Integer>> accounts = atm.getCustomerAccounts();
			// 					String temp ="\nAccounts: ";
			// 					temp=temp+accounts.get(0);
			// 					temp=temp +"\nPocket Accounts: ";
			// 					temp=temp+accounts.get(1);
			// 					System.out.println(temp);
			// 					System.out.println("\n--------------Welcome to the ATM Interface-------------");
			// 					System.out.println("Select a transaction:\n(0)Deposit\n(1)Top-up\n(2)Withdrawal\n(3)Transfer\n(4)Collect\n(5)Wire\n(6)Pay-friend\n(7)Set Pin\n(8)Exit\n");
			// 					input = System.console().readLine();
			// 					if(input.equals("7")){
			// 						System.out.println("Enter new pin");
			// 					}
			// 					else if(input.equals("6") || input.equals("1")){
			// 						System.out.println("Enter a pocket account.");
			// 					} else if(!input.equals("7")){
			// 						System.out.println("Enter an account.");
			// 					}
			// 					if(!input.equals("8")){
			// 						acc = System.console().readLine();
			// 					}
			// 					switch(input){
			// 						case "0":
			// 							System.out.println("Enter an amount.");
			// 							 amt = System.console().readLine();
			// 							atm.deposit(Integer.parseInt(acc),Double.parseDouble(amt));
			// 							break;
			// 						case "1":
			// 							System.out.println("Enter an amount.");
			// 							 amt = System.console().readLine();
			// 							atm.topUp(Integer.parseInt(acc),Double.parseDouble(amt));
			// 							break;
			// 						case "2":
			// 							System.out.println("Enter an amount.");
			// 							 amt = System.console().readLine();
			// 							atm.withdraw(acc,Double.parseDouble(amt));
			// 							break;
			// 						case "3":
			// 							System.out.println("Enter another account.");
			// 							 acc2 = System.console().readLine();
			// 							System.out.println("Enter an amount.");
			// 							 amt = System.console().readLine();
			// 							atm.transfer(Integer.parseInt(acc),Integer.parseInt(acc2),Double.parseDouble(amt));
			// 							break;
			// 						case "4":
			// 							System.out.println("Enter a pocket account.");
			// 							 acc2 = System.console().readLine();
			// 							System.out.println("Enter an amount.");
			// 							 amt = System.console().readLine();
			// 							atm.collect(Integer.parseInt(acc),Integer.parseInt(acc2),Double.parseDouble(amt));
			// 							break;
			// 						case "5":
			// 							System.out.println("Enter another account.");
			// 							 acc2 = System.console().readLine();
			// 							System.out.println("Enter an amount.");
			// 							 amt = System.console().readLine();
			// 							atm.wire(Integer.parseInt(acc),Integer.parseInt(acc2),Double.parseDouble(amt));
			// 							break;
			// 						case "6":
			// 							System.out.println("Enter another pocket account.");
			// 							 acc2 = System.console().readLine();
			// 							System.out.println("Enter an amount.");
			// 							 amt = System.console().readLine();
			// 							atm.payFriend(Integer.parseInt(acc),Integer.parseInt(acc2),Double.parseDouble(amt));
			// 							break;
			// 						case "7":
			// 							atm.setPin(Integer.parseInt(acc));
			// 							break;
			// 						case "8":
			// 							break;
			// 					}
			// 					if(input.equals("8")){
			// 						break;
			// 					}
			// 				}
			// 				if(input.equals("8")){
			// 						break;
			// 				}
			// 			}
			// 			break;
			// 		case "1"://TELLER
			// 			while(true){
			// 			System.out.println("---------------------Welcome to Teller Interface-----------------------");
			// 			System.out.println("Select a transaction:\n(0)Enter Check Transaction\n(1)Generate Monthly Statemet\n(2)List Closed Accounts\n(3)DTER\n(4)Customer Report\n(5)Add Interest\n(6)Create Account\n(7)Delete Closed Acounts\n(8)Delete Customers\n(9)Delete Transactions\n(10)Change interest rate\n(11)Go Back to Main\n");
			// 			String inp = System.console().readLine();
			// 			switch(Integer.parseInt(inp)){
			// 				case 0: //enter check
			// 					// while(true){
			// 					String check = "";
			// 					System.out.println("Writing a check......");
			// 					System.out.println("Enter accountID for check");
			// 					check = System.console().readLine();
			// 					String accountId = check;
			// 					System.out.println("Enter amount for check");
			// 					check = System.console().readLine();
			// 					String amount = check;
			// 					System.out.println("Enter customer taxID");
			// 					check = System.console().readLine();
			// 					String taxID = check;
			// 					teller.writeCheck(Integer.parseInt(accountId), Double.parseDouble(amount), Integer.parseInt(taxID));
			// 					System.out.println("Check written");
			// 					break;
			// 				case 1: //monthly statement
			// 					System.out.println("Printing monthly statement.....");
			// 					String tax = "";
			// 					System.out.println("Enter taxID for customer");
			// 					tax = System.console().readLine();
			// 					List<String> monthly = teller.generateMonthly(Integer.parseInt(tax));
			// 					for(int i = 0; i < monthly.size(); i++){
			// 						System.out.println(monthly.get(i));
			// 					}
			// 					break;
			// 				case 2: //closed accounts
			// 					System.out.println("Listing closed acounts..........");
			// 					System.out.println(teller.listClosedAccounts());
			// 					break;
			// 				case 3: //DTER
			// 					System.out.println("generateDTER");
			// 					List<String> dter = teller.generateDTER();
			// 					for(int i = 0; i < dter.size(); i++){
			// 						System.out.println(dter.get(i));
			// 					}
			// 					break;
			// 				case 4: //customer report
			// 					System.out.println("Printing customer report");
			// 					String id = "";
			// 					System.out.println("Enter taxId for customer report");
			// 					id = System.console().readLine();
			// 					List<String> report = teller.customerReport(Integer.parseInt(id));
			// 					for(int i = 0; i < report.size(); i++){
			// 						System.out.println(report.get(i));
			// 					}
			// 					break;
			// 				case 5: //add interest
			// 					System.out.println("Add interest to accounts. Be sure it's the end of the month!");
			// 					if((teller.addInterest().equals("1"))){
			// 						break;
			// 					}
			// 					System.out.println("Interest added on accounts");
			// 					break;
			// 				case 6: //create account
			// 					System.out.println("Create an acount...........");
			// 					String create = "";
			// 					String linked = "-1";
			// 					AccountType type = AccountType.STUDENT_CHECKING;
			// 					System.out.println("Choose account type. \n(0)STUDENT_CHECKING\n(1)INTEREST_CHECKING\n(2)SAVINGS\n(3)POCKET\n");
			// 					create = System.console().readLine();
			// 					while(true){
			// 					switch(create){
			// 						case "0":
			// 							break;
			// 						case "1":
			// 							type = AccountType.INTEREST_CHECKING;
			// 							break;
			// 						case "2":
			// 							type = AccountType.SAVINGS;
			// 							break;
			// 						case "3":
			// 							type = AccountType.POCKET;
			// 							System.out.println("Enter linkedAccount id");
			// 							linked = System.console().readLine();
			// 							break;
			// 						default:
			// 							System.out.println("Incorrect value. Please enter account type");
			// 							create = System.console().readLine();
			// 					}
			// 					break;
			// 				}
			// 					System.out.println("\nDo you want to add co-owners? \n(y) Yes\n(n) No" );
			// 					create = System.console().readLine();
			// 					List<List<String>> coOwners = new ArrayList();
			// 					switch(create){
			// 						case "y":
			// 							System.out.println("\nHow many co-owners do you want to add?");
			// 							String num = System.console().readLine();
			// 							for(int i = 0; i < Integer.parseInt(num); i++){
			// 								System.out.println("\nAdding co-owner number: " + i);
			// 								System.out.println("Enter co-owner taxID");
			// 								String coID = System.console().readLine();
			// 								System.out.println("Enter co-owner addr");
			// 								String addr = System.console().readLine();
			// 								System.out.println("Enter co-owner name");
			// 								String name = System.console().readLine();
			// 								List<String> co = new ArrayList();
			// 								co.add(coID);
			// 								co.add(addr);
			// 								co.add(name);
			// 								coOwners.add(co);
			// 							}
			// 							System.out.println("\nDone adding co-owners");
			// 							break;
			// 						case "n":
			// 							break;
			// 					}
			// 					System.out.println("\nEnter amount for account");
			// 					String amt = System.console().readLine();
			// 					System.out.println("Enter new accountID");
			// 					String acctId = System.console().readLine();
			// 					System.out.println("Enter primary owner taxID");
			// 					String primID = System.console().readLine();
			// 					System.out.println("Enter primary owner name");
			// 					String name = System.console().readLine();
			// 					System.out.println("Enter primary owner address");
			// 					String addr = System.console().readLine();
			// 					teller.createAccount(type, coOwners, Double.parseDouble(amt), acctId, primID, linked,name,addr);
			// 					System.out.println("Added account...................");
			// 					break;
			// 				case 7: //delete accounts
			// 					System.out.println("Delete closed accounts. Make sure you already generatedMonthly and DTER");
			// 					System.out.println("It's the end of the month and you've already done the above?\n(y)Yes\n(n)No");
			// 					String go = "";
			// 					go = System.console().readLine();
			// 					if(go.equals("n")){
			// 						break;
			// 					}
			// 					if("0".equals(teller.deleteClosedAccounts())){
			// 						System.out.println("Closed accounts deleted successfully\n");
			// 					}else{
			// 						System.out.println("Failed to close accounts");
			// 					}
			// 					break;
			// 				case 8: //delete customers
			// 					System.out.println("Delete customers. Make sure you already generatedMonthly and DTER");
			// 					System.out.println("It's the end of the month and you've already done the above?\n(y)Yes\n(n)No");
			// 					String cust = "";
			// 					cust = System.console().readLine();
			// 					if(cust.equals("n")){
			// 						break;
			// 					}
			// 					teller.deleteCustomers();	
			// 					System.out.println("Deleted customers with no accounts");							
			// 					break;
			// 				case 9: //delete transactions
			// 					System.out.println("Delete transactions. Make sure you already generatedMonthly and DTER");
			// 					System.out.println("It's the end of the month and you've already done the above?\n(y)Yes\n(n)No");
			// 					String trans = "";
			// 					trans = System.console().readLine();
			// 					if(trans.equals("n")){
			// 						break;
			// 					}
			// 					teller.deleteTransactionHistory();
			// 					System.out.println("Deleted transaction history");
			// 					break;
			// 				case 10: //change interest rate
			// 					while(true){
			// 						System.out.println("Change interest rate for an account");
			// 						System.out.println("Enter accountID");
			// 						String acc = System.console().readLine();
			// 						System.out.println("Enter customer taxID");
			// 						String tid = System.console().readLine();
			// 						System.out.println("Enter new interest rate");
			// 						String rate = System.console().readLine();
			// 						teller.changeInterestRate(Integer.parseInt(acc), Integer.parseInt(tid), Double.parseDouble(rate));
			// 						System.out.println("Interest rate changed");
			// 						break;
			// 					}
			// 					break;
			// 				case 11: //back to main
			// 				break;

			// 			}
			// 			if(inp.equals("11")){
			// 				break;
			// 			}
			// 		}
			// 		break;

			// 		case "2":
			// 		while(true){
			// 			String in = "";
			// 			System.out.println("Welcome to set date");
			// 			System.out.println("Enter year");
			// 			in = System.console().readLine();
			// 			String year = in;
			// 			System.out.println("Enter month");
			// 			in = System.console().readLine();
			// 			String month = in;
			// 			System.out.println("Enter day");
			// 			in = System.console().readLine();
			// 			String day = in;
			// 			app.setDate(Integer.parseInt(year), Integer.parseInt(month), Integer.parseInt(day));
			// 			break;
			// 		}
			// 		break;
			// 		case "3":
			// 			while(true){
			// 				System.out.println("If it's the end of the month, you should do monthly reset.");
			// 				System.out.println("Only do monthly reset if you've already done other end of the month functions");
			// 				System.out.println("(y)Yes\n(n)No");
			// 				String del = "";
			// 				del = System.console().readLine();
			// 				if(del.equals("y")){
			// 					helper.monthlyReset();
			// 					System.out.println("Reset monthly data.");
			// 				}
			// 				break;
			// 			}
			// 			break;
			// 		case "2"://SETDATE
			// 			while(true){
			// 				String year="";
			// 				String month="";
			// 				String day="";
			// 				System.out.println("Enter the year");
			// 				year = System.console().readLine();
			// 				System.out.println("Enter the month");
			// 				month = System.console().readLine();
			// 				System.out.println("Enter the day");
			// 				day = System.console().readLine();
			// 				if(app.setDate(Integer.parseInt(year),Integer.parseInt(month),Integer.parseInt(day)).charAt(0)==('0')){
			// 					break;
			// 				} else {
			// 					System.out.println("Try again.");
			// 				}
			// 			}
			// 			break;
			// 		case "-1":
			// 			System.out.println("Thanks for using our bank.");
			// 			break;
			// 	}
			// 	if(input.equals("-1")){
			// 		break;
			// 	}
			// }
			
		}
	}
	//!### FINALIZAMOS
}

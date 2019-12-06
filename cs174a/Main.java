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
			Helper helper= new Helper();
			ATM atm = new ATM(1234, app);
			//for testing app.java
			r = app.createTables();
			app.setDate(2018, 2, 7);
			r = app.createCheckingSavingsAccount(AccountType.INTEREST_CHECKING, "1234", 1000.00, "4321", "Bob", "66 DP");
			// System.out.println(r);
			// r = app.createCheckingSavingsAccount(AccountType.SAVINGS, "1233", 1000.00, "4321", "Bob", "66 DP");
			r = app.createPocketAccount("1", "1234", 100.0, "4321");
			r = app.createPocketAccount("2", "1234", 100.0, "4321");
			r = app.createCustomer("1234", "1111","Andrew","66 Sueno");
			// r = app.deposit("1234",1000.00);
			// atm.deposit(1234, 1000.00);
			// atm.topUp(1, 200.0);
			// atm.payFriend(1, 2, 50);
			// r = app.topUp("1", 200.00);
			// r = app.topUp("2", 100.00);
			// r = app.payFriend("1","2",50);
			// r = app.listClosedAccounts();
			//for testing customer.java
			// Helper helper= new Helper();
			// r = helper.getDate();
			// System.out.println(r);
			// r = helper.getDate();
			// System.out.println(r);
			// r = Integer.toString(customer.getPin());
			// System.out.println(r);
			// r = customer.setPin(8888);
			// r = Integer.toString(customer.getPin());
			// System.out.println(r);
			// r = customer.setName("Bob");
			// r = customer.getName();
			// System.out.println(r);
			// r = customer.setName("Shu");
			// r = customer.getName();
			// System.out.println(r);
			// List<Integer> res=customer.getAccountIDs(customer.getTaxID(),AccountType.STUDENT_CHECKING);
			// for(int i =0;i<res.size();i++){
			// 	System.out.println("AccountID "+Integer.toString(i)+": "+res.get(i));
			// }
			// System.out.println(customer.acctBelongsToCustomer(1234,4321,AccountType.STUDENT_CHECKING));
			// System.out.println(helper.hashPin(1234));
			// System.out.println(helper.unhashPin("\"#$%"));
			// for testing ATM
			// ATM atm = new ATM(4321,app);
			// System.out.println(atm.verifyPin(1717));
			// System.out.println(atm.withdraw("1234",300));
			// System.out.println(atm.purchase("1",10));
			// System.out.println(atm.transfer(1234,1233,100));
			// System.out.println(atm.collect(1234,1,20));
			// System.out.println(atm.wire(1234,1233,19));
			//for testing Teller
			Teller teller = new Teller (4321,app);
			// System.out.println(teller.customerOwnsAccount("4321","1234"));
			// teller.changeInterestRate(1234,1.4);
			// //teller.deleteTransactionHistory();
			// String sql = "DELETE FROM Owns " +
            //                     "WHERE tID = 1111";
			// try{
			// 	Statement stmt = helper.getConnection().createStatement();
			// 	stmt.executeUpdate(sql);
				
			// } catch (Exception e){
			// 	System.out.println(e);
			// }
			// teller.deleteCustomers();
			// sql = "UPDATE AccountPrimarilyOwns " +
            //                         "SET isClosed = 1" + 
            //                         " WHERE accountId = 1";
			// try{
			// 	Statement stmt = helper.getConnection().createStatement();
			// 	stmt.executeUpdate(sql);
				
			// } catch (Exception e){
			// 	System.out.println(e);
			// }
			// //teller.deleteClosedAccounts();
			ArrayList<Integer> coOwners = new ArrayList();
			app.createCustomer("1233", "1111","Andrew","66 Sueno");
			coOwners.add(1111);
			teller.createAccount(AccountType.INTEREST_CHECKING,coOwners,1000.0,"1234","-1");
			teller.createAccount(AccountType.POCKET,coOwners,1000.0,"1234","-1");
			List<String> res2 = teller.customerReport(1111);
			for(int i=0;i<res2.size();i++){
				System.out.println(res2.get(i));
			}
			// res2 = teller.generateMonthly(4321);
			res2 = teller.generateMonthly(4321);
			System.out.println(res2);
			// for(int i=0;i<res2.size();i++){
			// 	System.out.println(res2.get(i));
			// }
			// System.out.println(teller.listClosedAccounts());
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
			// res2 = teller.generateDTER();
			// for(int i=0;i<res2.size();i++){
			// 	System.out.println(res2.get(i));
			// }
			// teller.addInterest();
			// teller.writeCheck(1234,100);
		}
	}
	//!### FINALIZAMOS
}

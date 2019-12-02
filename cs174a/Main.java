package cs174a;                         // THE BASE PACKAGE FOR YOUR APP MUST BE THIS ONE.  But you may add subpackages.

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
			//for testing app.java
			app.dropTables();
			r = app.createTables();
			r = app.setDate(4000, 2, 18);
			r = app.createCheckingSavingsAccount(AccountType.STUDENT_CHECKING, "1234", 1000.00, "4321", "Bob", "66 DP");
			r = app.createCheckingSavingsAccount(AccountType.STUDENT_CHECKING, "1233", 1000.00, "4321", "Bob", "66 DP");
			r = app.createPocketAccount("1", "1234", 100.0, "4321");
			r = app.createPocketAccount("2", "1234", 100.0, "4321");
			r = app.createCustomer("1234", "1111","Andrew","66 Sueno");
			r = app.deposit("1234",1000.00);
			r = app.topUp("1", 200.00);
			r = app.topUp("2", 100.00);
			r = app.payFriend("1","2",50);
			r = app.listClosedAccounts();
			//for testing customer.java
			// Helper helper= new Helper();
			// Customer customer = new Customer(4321);
			// r = customer.setAddress("Bob house");
			// r = customer.getAddress();
			// System.out.println(r);
			// r = customer.setAddress("Shu house");
			// r = customer.getAddress();
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
			//for testing ATM
			ATM atm = new ATM(4321,app);
			System.out.println(atm.verifyPin(1717));
			System.out.println(atm.withdraw("1234",300));
			System.out.println(atm.purchase("1",10));
			System.out.println(atm.transfer(1234,1233,100));
			System.out.println(atm.collect(1234,1,20));
			System.out.println(atm.wire(1234,1233,19));
		}
	}
	//!### FINALIZAMOS
}

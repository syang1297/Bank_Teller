//compile from Bank_Teller directory: javac -classpath /usr/lib/oracle/19.3/client64/lib/ojdbc8.jar:. cs174a/Testable.java cs174a/Customer.java cs174a/App.java cs174a/Main.java cs174a/Teller.java cs174a/ATM.java cs174a/Helper.java
//run from Bank_Teller directory to run program: java -classpath /usr/lib/oracle/19.3/client64/lib/ojdbc8.jar:. cs174a.Main

//Commands TAs will use to run program
//compile: javac -d out/ -cp /path/to/ojdbc8.jar cs174a/*.java		# out/ is where .class files are saved to.
//run: java -cp /path/to/ojdbc8.jar:out:. cs174a.Main

package cs174a;                                             // THE BASE PACKAGE FOR YOUR APP MUST BE THIS ONE.  But you may add subpackages.

// You may have as many imports as you need.
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Date;
import java.util.Properties;
import oracle.jdbc.pool.OracleDataSource;
import oracle.jdbc.OracleConnection;

/**
 * The most important class for your application.
 * DO NOT CHANGE ITS SIGNATURE.
 */
public class App implements Testable
{
	private OracleConnection _connection;                   // Example connection object to your DB.

	/**
	 * Default constructor.
	 * DO NOT REMOVE.
	 */
	App()
	{
		// TODO: Any actions you need.
	}

	/**
	 * This is an example access operation to the DB.
	 */
	// void exampleAccessToDB()
	// {
	// 	// Statement and ResultSet are AutoCloseable and closed automatically.
	// 	try( Statement statement = _connection.createStatement() )
	// 	{
	// 		try( ResultSet resultSet = statement.executeQuery( "select owner, table_name from all_tables" ) )
	// 		{
	// 			while( resultSet.next() )
	// 				System.out.println( resultSet.getString( 1 ) + " " + resultSet.getString( 2 ) + " " );
	// 		}
	// 	}
	// 	catch( SQLException e )
	// 	{
	// 		System.err.println( e.getMessage() );
	// 	}
	// }

	////////////////////////////// Implement all of the methods given in the interface /////////////////////////////////
	// Check the Testable.java interface for the function signatures and descriptions.

	@Override
	public String initializeSystem()
	{
		// Some constants to connect to your DB.
		final String DB_URL = "jdbc:oracle:thin:@cs174a.cs.ucsb.edu:1521/orcl";
		final String DB_USER = "c##andrewdoan";
		final String DB_PASSWORD = "3772365";

		// Initialize your system.  Probably setting up the DB connection.
		Properties info = new Properties();
		info.put( OracleConnection.CONNECTION_PROPERTY_USER_NAME, DB_USER );
		info.put( OracleConnection.CONNECTION_PROPERTY_PASSWORD, DB_PASSWORD );
		info.put( OracleConnection.CONNECTION_PROPERTY_DEFAULT_ROW_PREFETCH, "20" );

		try
		{
			OracleDataSource ods = new OracleDataSource();
			ods.setURL( DB_URL );
			ods.setConnectionProperties( info );
			_connection = (OracleConnection) ods.getConnection();

			// Get the JDBC driver name and version.
			DatabaseMetaData dbmd = _connection.getMetaData();
			System.out.println( "Driver Name: " + dbmd.getDriverName() );
			System.out.println( "Driver Version: " + dbmd.getDriverVersion() );

			// Print some connection properties.
			System.out.println( "Default Row Prefetch Value is: " + _connection.getDefaultRowPrefetch() );
			System.out.println( "Database Username is: " + _connection.getUserName() );
			System.out.println();

			return "0";
		}
		catch( SQLException e )
		{
			System.err.println( e.getMessage() );
			return "1";
		}
	}

	/**
	 * Destroy all of the tables in your DB.
	 * @return a string "r", where r = 0 for success, 1 for error.
	 */
	@Override
	public String dropTables(){
		try {
			System.out.println("Connecting to database...............");
			Statement stmt = _connection.createStatement();
			try {
				System.out.println("Dropping table GlobalDate");
				String sql = "DROP TABLE GlobalDate";
				stmt.executeUpdate(sql);
			} catch (Exception e) {
				System.out.println("Failed to drop table GlobalDate");
				System.out.println(e);
				return "1";
			}
			try {
				System.out.println("Dropping table Owns");
				String sql = "DROP TABLE Owns";
				stmt.executeUpdate(sql);
			} catch (Exception e) {
				System.out.println("Failed to drop table Owns");
				System.out.println(e);
				return "1";
			}
			try {
				System.out.println("Dropping table TransactionBelongs");
				String sql = "DROP TABLE TransactionBelongs";
				stmt.executeUpdate(sql);
			} catch (Exception e) {
				System.out.println("Failed to drop table TransactionBelongs");
				System.out.println(e);
				return "1";
			}
			try {
				System.out.println("Dropping table PocketAccountLinkedWith");
				String sql = "DROP TABLE PocketAccountLinkedWith";
				stmt.executeUpdate(sql);
			} catch (Exception e) {
				System.out.println("Failed to drop table PocketAccountLinkedWith");
				System.out.println(e);
				return "1";
			}
			try {
				System.out.println("Dropping table AccountPrimarilyOwns");
				String sql = "DROP TABLE AccountPrimarilyOwns";
				stmt.executeUpdate(sql);
			} catch (Exception e) {
				System.out.println("Failed to drop table AccountPrimarilyOwns");
				System.out.println(e);
				return "1";
			}
			try {
				System.out.println("Dropping table Customer");
				String sql = "DROP TABLE Customer";
				stmt.executeUpdate(sql);
			} catch (Exception e) {
				System.out.println("Failed to drop table Customer");
				System.out.println(e);
				return "1";
			}
		} 
		catch (Exception e) {
			System.out.println("Failed to connect to database.......");
			System.out.println(e);
			return "1";
		}
		System.out.println("Tables successfully dropped");
		return "0";
	}

	/**
	 * Create all of your tables in your DB.
	 * @return a string "r", where r = 0 for success, 1 for error.
	 */
	@Override
	public String createTables(){
		try {
			System.out.println("Connecting to database.................");
			Statement stmt = _connection.createStatement();
			try {
				System.out.println("Creating table GlobalDate");
				String sql = "CREATE TABLE GlobalDate (" + 
								"num INTEGER,"+
								"globalDate char(10),"+ 
								"PRIMARY KEY (num))";
				stmt.executeUpdate(sql);
				
			} catch (Exception e) {
				System.out.println("Failed to create table GlobalDate.");
				System.out.println(e);
				return "1";
			}
	
			try {
				System.out.println("Creating table Customer.");
				String sql = "CREATE TABLE Customer(" + 
								"taxID INTEGER," +
								"addr CHAR (32)," + 
								"pin INTEGER," + 
								"name CHAR(32)," + 
								"PRIMARY KEY (taxID))";
				
				stmt.executeUpdate(sql);
			} catch (Exception e) {
				System.out.println("Failed to create table Customer.");
				System.out.println(e);
				return "1";
			}
	
			try {
				System.out.println("Creating table AccountPrimarilyOwns.");
				String sql = "CREATE TABLE AccountPrimarilyOwns(" +
								"accountID INTEGER,"  +
								"taxID INTEGER NOT NULL," +
								"bankBranch CHAR(32)," +
								"balance INTEGER," +
								"balanceEndDate CHAR(10)," +
								"balanceStartDate CHAR(10)," +
								"isClosed NUMBER(1)," +
								"interestRate REAL," +
								"accountType CHAR(32)," +
								"interestAdded NUMBER(1)," +
								"PRIMARY KEY(accountID, taxID)," +
								"FOREIGN KEY (taxID) REFERENCES " +
								"Customer ON DELETE CASCADE)";
				stmt.executeUpdate(sql);
			} catch (Exception e) {
				System.out.println("Failed to table AccountPrimarilyOwns.");
				System.out.println(e);
				return "1";
			}
	
			try {
				System.out.println("Creating table Owns.");
				String sql = "CREATE TABLE Owns("  +
								"aID INTEGER," +
								"tID INTEGER," +
								"PRIMARY KEY(aID, tID)," +
								"FOREIGN KEY(aID, tID) REFERENCES " +
								"AccountPrimarilyOwns(accountID, taxID))" ;
				stmt.executeUpdate(sql);			
			} catch (Exception e) {
				System.out.println("Failed to create table Owns.");
				System.out.println(e);
				return "1";
			}
	
			try {
				System.out.println("Creating table TransactionBelongs");
				String sql = "CREATE TABLE TransactionBelongs(" +
								"amount REAL," +
								"fee INTEGER," +
								"transType CHAR(32)," +
								"transDate CHAR(10)," +
								"checkNo INTEGER," +
								"transactionID INTEGER," +
								"aID INTEGER NOT NULL," +
								"tID INTEGER NOT NULL," +
								"PRIMARY KEY(transactionID, aID, tID)," +
								"FOREIGN KEY(aID, tID) REFERENCES " +
								"AccountPrimarilyOwns(accountID, taxID) ON DELETE CASCADE)";
				stmt.executeUpdate(sql);			
			} catch (Exception e) {
				System.out.println("Failed to create table TransactionBelongs.");
				System.out.println(e);
				return "1";
			}
	
			try {
				System.out.println("Creating table PocketAccountLinkedWith");
				String sql = "CREATE TABLE PocketAccountLinkedWith("  +
								"aID INTEGER," +
								"tID INTEGER," +
								"otherAccountID INTEGER NOT NULL," +
								"otherTaxID INTEGER NOT NULL," +
								"feePaid NUMBER(1)," +
								"PRIMARY KEY (aID, otherAccountID)," +
								"FOREIGN KEY (aID, tID) REFERENCES " +
								"AccountPrimarilyOwns(accountID, taxID) ON DELETE CASCADE," +
								"FOREIGN KEY (otherAccountID, tID) REFERENCES " +
								"AccountPrimarilyOwns(accountID, taxID) ON DELETE CASCADE)";			
				stmt.executeUpdate(sql);			
			} catch (Exception e) {
				System.out.println("Failed to create table PocketAccountLinkedWith.");
				System.out.println(e);
				return "1";
			}
		} catch (Exception e) {
			System.out.println("Failed to connect to database.......");
			System.out.println(e);
			return "1";
		}
		System.out.println("All database tables successfully created");
		return "0";
	}

	/**
	 * Set system's date.
	 * @param year Valid 4-digit year, e.g. 2019.
	 * @param month Valid month, where 1: January, ..., 12: December.
	 * @param day Valid day, from 1 to 31, depending on the month (and if it's a leap year).
	 * @return a string "r yyyy-mm-dd", where r = 0 for success, 1 for error; and yyyy-mm-dd is the new system's date, e.g. 2012-09-16.
	 */
	@Override
	public String setDate( int year, int month, int day){
		String stringYear = Integer.toString(year);
		String stringMonth = Integer.toString(month);
		String stringDay = Integer.toString(day);
		if(stringMonth.length()<2){
			stringMonth="0"+stringMonth;
		}
		if(stringDay.length()<2){
			stringDay="0"+stringDay;
		}
		String res = stringYear+"-"+stringMonth+"-"+stringDay;
		if(stringYear.length() != 4) {
			System.out.println("Wrong year digits");
			return "1 "+res;
		} else if (!(month > 12) || !(month < 1)) {
			if(year%4 != 0 && month == 2){
				System.out.println("Not leap year in feb");
				if(day > 28 || day < 1){
					return "1 "+res;
				}
			} else if (month == 2 ) { // leap year only
				System.out.println("Leap year");
				if(day > 29 || day < 1){
					System.out.println("leap year messed up");
					return "1 "+res;
				}
			} else {
				System.out.println("Reached cases");
				switch(month){
					case 1:
					case 3:
					case 5:
					case 7:
					case 8:
					case 10:
					case 12:
						if(day>31 || day<1){
							return "1 "+res;
						}
						break;
					case 4:
					case 6:
					case 9:
					case 11:
						if(day>30 || day<1){
							return "1 "+res;
						}
						break;
				}
					
			}
		} else if(month>12 || month<1){
			System.out.println("month bad");
			return "1 "+res;
		}
		try {
				System.out.println("Connecting to database...............");
				Statement stmt = _connection.createStatement();
				System.out.println("Writing to table GlobalDate");
				try{
					String sqlDate = "1,"+stringYear+stringMonth+stringDay;
					String sql = "INSERT INTO GlobalDate VALUES ("+sqlDate+")";
					stmt.executeUpdate(sql);
				} catch(Exception e) {
					System.out.println("Failed to write date to DB.");
					System.out.println(e);
				}
			} catch (Exception e) {
				System.out.println("Failed to connect to DB.");
				System.out.println(e);
			}
		return "0 "+res;

	}

<<<<<<< HEAD
	// Date getDate(){
    //     Date currDate = null;
    //     try {
    //         System.out.println("Connecting to database for date...");
    //         Statement stmt = _connection.createStatement();
    //         try {
    //             String sql = "SELECT globalDate " +
    //             			"FROM GlobalDate";
	// 			ResultSet rs = stmt.executeQuery(sql);
	// 			while(rs.next()){
	// 				currDate = rs.getDate("globalDate");
	// 			}
	// 			rs.close();
    //             return currDate;
    //         } catch (Exception e) {
    //             System.out.println("Failed to select date from GlobalDate....");
    //             System.out.println(e);
    //             return currDate;
    //         }
    //     } catch (Exception e) {
    //         System.out.println("Failed to connect to database....");
    //         System.out.println(e);
    //         return currDate;
    //     }
    // }
=======
>>>>>>> master

	/**
	 * Create a new checking or savings account.
	 * If customer is new, then their name and address should be provided.
	 * @param accountType New account's checking or savings type.
	 * @param id New account's ID.
	 * @param initialBalance Initial account balance.
	 * @param tin Account's owner Tax ID number - it may belong to an existing or new customer.
	 * @param name [Optional] If customer is new, this is the customer's name.
	 * @param address [Optional] If customer is new, this is the customer's address.
	 * @return a string "r aid type balance tin", where
	 *         r = 0 for success, 1 for error;
	 *         aid is the new account id;
	 *         type is the new account's type (see the enum codes above, e.g. INTEREST_CHECKING);
	 *         balance is the account's initial balance with 2 decimal places (e.g. 1000.34, as with %.2f); and
	 *         tin is the Tax ID of account's primary owner.
	 */
	@Override
	public String createCheckingSavingsAccount( AccountType accountType, String id, double initialBalance, String tin, String name, String address )
	{
<<<<<<< HEAD
		String interestRate = "";
		switch(accountType){
			case STUDENT_CHECKING:
				interestRate = "0";
				break;
			case INTEREST_CHECKING:
				interestRate = "3.00";
				break;
			case SAVINGS:
				interestRate = "4.80";
				break;
			case POCKET:
				return "1 " + id + " " + accountType + " " + initialBalance + " " + tin;
		}

		//check account id doesn't already exist in db
		try{
			Statement stmt = _connection.createStatement();
			try {
				String sql = "SELECT accountID " +
								"FROM AccountPrimarilyOwns";
				ResultSet rs = stmt.executeQuery(sql);
				while(rs.next()){
					int aid = rs.getInt("accountID");
					String dbID = Integer.toString(aid);
					if(id.equals(dbID)){
						return "1 " + id + " " + accountType + " " + initialBalance + " " + tin;
					}
				}
				rs.close();
			} catch (Exception e) {
				System.out.println(e);
				return "1 " + id + " " + accountType + " " + initialBalance + " " + tin;
			}
		} catch(Exception e){
			System.out.println(e);
            return "1 " + id + " " + accountType + " " + initialBalance + " " + tin;
		}

		//check for initial balance.... if it's null, set to 100
		if(initialBalance <= 0 ){
			initialBalance = 100.00;
		}

		//if taxID doesn't exist in customer table... create new customer
		//return w/ error if new customer needed but the parameters are null
		try {
			Statement stmt = _connection.createStatement();
			try {
				String sql = "SELECT taxID " + 
								"FROM Customer " +
								"WHERE taxID EQUALS " + tin;
				ResultSet rs = stmt.executeQuery(sql);
				if(rs == null){
					//TODO: HASHING FUNCTION FOR PIN
					if(address == null || name == null){
						System.out.println("Address and Name cannot be null because we are inserting a new customer");
						return "1 " + id + " " + accountType + " " + initialBalance + " " + tin;
					}
					sql = "INSERT INTO Customer " +
							"VALUES (" + tin + ", " + address + ", 1234, " + name + ")";
				}
				//update account table to reflect customer
				try {
					//TODO: BANKBRANCH, balanceEendDate, balanceStartDate
					sql = "INSERT INTO AccountPrimarilyOwns " + 
								"VALUES (" + id + ", " + tin + ", bankBranch1, " + initialBalance +
								", 0000, 0000, " + "0, " + interestRate + ", " + accountType +
								", 0)";
				} catch (Exception e) {
					System.out.println("Unable to write to account table");
					System.out.println(e);
					return "1 " + id + " " + accountType + " " + initialBalance + " " + tin;
				}
			} catch (Exception e) {
				System.out.println("Failed to select taxID from Customer table");
				System.out.println(e);
				return "1 " + id + " " + accountType + " " + initialBalance + " " + tin;
			}
		} catch (Exception e) {
			System.out.println("getStatement() failed");
			System.out.println(e);
=======
		if(accountType == AccountType.POCKET){
>>>>>>> master
			return "1 " + id + " " + accountType + " " + initialBalance + " " + tin;
		}
		return "0 " + id + " " + accountType + " " + initialBalance + " " + tin;
	}

	/**
	 * Create a new pocket account.
	 * @param id New account's ID.
	 * @param linkedId Linked savings or checking account ID.
	 * @param initialTopUp Initial balance to be deducted from linked account and deposited into new pocket account.
	 * @param tin Existing customer's Tax ID number.  He/She will become the new pocket account's owner.
	 * @return a string "r aid type balance tin", where
	 *         r = 0 for success, 1 for error;
	 *         aid is the new account id;
	 *         type is the new account's type (see the enum codes above);
	 *         balance is the account's initial balance with up to 2 decimal places (e.g. 1000.12, as with %.2f); and
	 *         tin is the Tax ID of account's primary owner.
	 */
	@Override
	public String createPocketAccount( String id, String linkedId, double initialTopUp, String tin ){
		return "r";
	}

	/**
	 * Create a new customer and link them to an existing checking or saving account.
	 * @param accountId Existing checking or saving account.
	 * @param tin New customer's Tax ID number.
	 * @param name New customer's name.
	 * @param address New customer's address.
	 * @return a string "r", where r = 0 for success, 1 for error.
	 */
	@Override
	public String createCustomer( String accountId, String tin, String name, String address ){
		return "r";
	}

	/**
	 * Deposit a given amount of dollars to an existing checking or savings account.
	 * @param accountId Account ID.
	 * @param amount Non-negative amount to deposit.
	 * @return a string "r old new" where
	 *         r = 0 for success, 1 for error;
	 *         old is the old account balance, with up to 2 decimal places (e.g. 1000.12, as with %.2f); and
	 *         new is the new account balance, with up to 2 decimal places.
	 */
	@Override
	public String deposit( String accountId, double amount ){
		return "r";
	}

	/**
	 * Show an account balance (regardless of type of account).
	 * @param accountId Account ID.
	 * @return a string "r balance", where
	 *         r = 0 for success, 1 for error; and
	 *         balance is the account balance, with up to 2 decimal places (e.g. with %.2f).
	 */
	@Override
	public String showBalance( String accountId ){
		return "r";
	}

	/**
	 * Move a specified amount of money from the linked checking/savings account to the pocket account.
	 * @param accountId Pocket account ID.
	 * @param amount Non-negative amount to top up.
	 * @return a string "r linkedNewBalance pocketNewBalance", where
	 *         r = 0 for success, 1 for error;
	 *         linkedNewBalance is the new balance of linked account, with up to 2 decimal places (e.g. with %.2f); and
	 *         pocketNewBalance is the new balance of the pocket account.
	 */
	@Override
	public String topUp( String accountId, double amount ){
		return "r";
	}

	/**
	 * Move a specified amount of money from one pocket account to another pocket account.
	 * @param from Source pocket account ID.
	 * @param to Destination pocket account ID.
	 * @param amount Non-negative amount to pay.
	 * @return a string "r fromNewBalance toNewBalance", where
	 *         r = 0 for success, 1 for error.
	 *         fromNewBalance is the new balance of the source pocket account, with up to 2 decimal places (e.g. with %.2f); and
	 *         toNewBalance is the new balance of destination pocket account, with up to 2 decimal places.
	 */
	@Override
	public String payFriend( String from, String to, double amount ){
		return "r";
	}

	/**
	 * Example of one of the testable functions.
	 */
	@Override
	public String listClosedAccounts()
	{
		return "0 it works!";
	}





}

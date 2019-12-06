//compile from Bank_Teller directory: javac -classpath /usr/lib/oracle/19.3/client64/lib/ojdbc8.jar:. cs174a/Testable.java cs174a/Customer.java cs174a/App.java cs174a/Main.java cs174a/Teller.java cs174a/ATM.java cs174a/Helper.java
//run from Bank_Teller directory to run program: java -classpath /usr/lib/oracle/19.3/client64/lib/ojdbc8.jar:. cs174a.Main

//Commands TAs will use to run program
//compile: javac -d out/ -cp /path/to/ojdbc8.jar cs174a/*.java		# out/ is where .class files are saved to.
//run: java -cp /path/to/ojdbc8.jar:out:. cs174a.Main

//compile:  javac -d out/ -cp /usr/lib/oracle/19.3/client64/lib/ojdbc8.jar cs174a/*.java
//run: java -cp /usr/lib/oracle/19.3/client64/lib/ojdbc8.jar:out:. cs174a.Main

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
import cs174a.Helper.*;
import cs174a.Teller.*;
import cs174a.Customer.*;
import cs174a.ATM.*;
/**
 * The most important class for your application.
 * DO NOT CHANGE ITS SIGNATURE.
 */
public class App implements Testable
{
	private OracleConnection _connection;                   // Example connection object to your DB.
	private Helper helper;
	/**
	 * Default constructor.
	 * DO NOT REMOVE.
	 */
	App(){
		this.helper = new Helper();
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
		final String DB_USER = "c##syang01";
		final String DB_PASSWORD = "4621538";

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

			Statement stmt = _connection.createStatement();
			try {

				String sql = "DROP TABLE GlobalDate";
				stmt.executeUpdate(sql);
			} catch (Exception e) {
				System.out.println("Failed to drop table GlobalDate");
				System.out.println(e);
				return "1";
			}
			try {

				String sql = "DROP TABLE Owns";
				stmt.executeUpdate(sql);
			} catch (Exception e) {
				System.out.println("Failed to drop table Owns");
				System.out.println(e);
				return "1";
			}
			try {
			
				String sql = "DROP TABLE TransactionBelongs";
				stmt.executeUpdate(sql);
			} catch (Exception e) {
	
				System.out.println(e);
				return "1";
			}
			try {

				String sql = "DROP TABLE PocketAccountLinkedWith";
				stmt.executeUpdate(sql);
			} catch (Exception e) {
				System.out.println("Failed to drop table PocketAccountLinkedWith");
				System.out.println(e);
				return "1";
			}
			try {

				String sql = "DROP TABLE AccountPrimarilyOwns";
				stmt.executeUpdate(sql);
			} catch (Exception e) {

				System.out.println(e);
				return "1";
			}
			try {

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
	
			Statement stmt = _connection.createStatement();
			try {

				String sql = "CREATE TABLE GlobalDate(" + 
								"num INTEGER,"+
								"globalDate VARCHAR(10),"+ 
								"PRIMARY KEY (num))";
				stmt.executeUpdate(sql);
				
			} catch (Exception e) {
				System.out.println("Failed to create table GlobalDate.");
				System.out.println(e);
				return "1";
			}
	
			try {
		
				String sql = "CREATE TABLE Customer(" + 
								"taxID INTEGER," +
								"addr VARCHAR (32)," + 
								"pin VARCHAR(4)," + 
								"name VARCHAR(32)," + 
								"PRIMARY KEY (taxID))";
				
				stmt.executeUpdate(sql);
			} catch (Exception e) {
		
				System.out.println(e);
				return "1";
			}
	
			try {

				String sql = "CREATE TABLE AccountPrimarilyOwns(" +
								"accountID INTEGER,"  +
								"taxID INTEGER NOT NULL," +
								"bankBranch VARCHAR(32)," +
								"balance REAL," +
								"madeOn VARCHAR(32)," +
								"isClosed NUMBER(1)," +
								"interestRate REAL," +
								"accountType VARCHAR(32)," +
								"interestAdded NUMBER(1)," +
								"PRIMARY KEY(accountID, taxID)," +
								"FOREIGN KEY (taxID) REFERENCES " +
								"Customer ON DELETE CASCADE)";
				stmt.executeUpdate(sql);
			} catch (Exception e) {
				System.out.println("Failed to create table AccountPrimarilyOwns.");
				System.out.println(e);
				return "1";
			}
	
			try {
		
				String sql = "CREATE TABLE Owns("  +
								"aID INTEGER," +
								"tID INTEGER," +
								"numKey INTEGER, " +
								"PRIMARY KEY(numKey))";
				stmt.executeUpdate(sql);			
			} catch (Exception e) {
				System.out.println("Failed to create table Owns.");
				System.out.println(e);
				return "1";
			}
	
			try {
		
				String sql = "CREATE TABLE TransactionBelongs(" +
								"amount REAL," +
								"transType VARCHAR(32)," +
								"transDate VARCHAR(10)," +
								"checkNo INTEGER," +
								"transactionID INTEGER," +
								"aID INTEGER NOT NULL," +
								"toAID INTEGER NOT NULL," +
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
			
				String sql = "CREATE TABLE PocketAccountLinkedWith("  +
								"aID INTEGER," +
								"tID INTEGER," +
								"otherAccountID INTEGER NOT NULL," +
								"otherTaxID INTEGER NOT NULL," +
								"feePaid NUMBER(1)," +
								"PRIMARY KEY (aID, otherAccountID)," +
								"FOREIGN KEY (aID, tID) REFERENCES " +
								"AccountPrimarilyOwns(accountID, taxID) ON DELETE CASCADE," +
								"FOREIGN KEY (otherAccountID, otherTaxID) REFERENCES " +
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
		if(stringMonth.length()==1){
			stringMonth="0"+stringMonth;
		}
		if(stringDay.length()==1){
			stringDay="0"+stringDay;
		}
		String res = stringYear+"-"+stringMonth+"-"+stringDay;
		if(stringYear.length() != 4) {
			System.out.print(Integer.toString(stringYear.length()));
			System.out.println("Wrong year digits");
			return "1 "+ helper.getDate();
		} else if (!(month > 12) || !(month < 1)) {
			if(year%4 != 0 && month == 2){

				if(day > 28 || day < 1){
					return "1 "+helper.getDate();
				}
			} else if (month == 2 ) { // leap year only
				System.out.println("Leap year");
				if(day > 29 || day < 1){
					System.out.println("leap year messed up");
					return "1 "+helper.getDate();
				}
			} else {

				switch(month){
					case 1:
					case 3:
					case 5:
					case 7:
					case 8:
					case 10:
					case 12:
						if(day>31 || day<1){
							return "1 "+helper.getDate();
						}
						break;
					case 4:
					case 6:
					case 9:
					case 11:
						if(day>30 || day<1){
							return "1 "+helper.getDate();
						}
						break;
				}
					
			}
		} else if(month>12 || month<1){
			System.out.println("month bad");
			return "1 "+helper.getDate();
		}
		try {
		
				Statement stmt = _connection.createStatement();
		
				String sqlDate = stringYear + stringMonth + stringDay;
				try {
					String empty = "SELECT * FROM GlobalDate";
					ResultSet rs = stmt.executeQuery(empty);
					if(rs.next() == false){
						//empty
						try{
							String sql = "INSERT INTO GlobalDate VALUES (1, "+sqlDate+")";
							stmt.executeUpdate(sql);
						} catch(Exception e) {
							System.out.println("Failed to write date to DB.");
							System.out.println(e);
						}
					}else{
						//not empty so should update value
						// System.out.println(sqlDate);
						try {
							String updatesql = "UPDATE GlobalDate SET globalDate = '" + sqlDate + "'";
							stmt.executeUpdate(updatesql);
						} catch (Exception e) {
							System.out.println("Failed to update date to DB");
							System.out.println(e);
						}
					}
				} catch (Exception e) {
					System.out.println("Failed to check if global date needs to be updated or inserted");
					System.out.println(e);
				}
			} catch (Exception e) {
				System.out.println("Failed to connect to DB.");
				System.out.println(e);
			}
		return "0 "+res;

	}


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
	//add to transaction
	@Override
	public String createCheckingSavingsAccount( AccountType accountType, String id, double initialBalance, String tin, String name, String address )
	{
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
				return "1 " + id + " " + accountType + " " + String.format("%.2f",initialBalance) + " " + tin;
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
						System.out.println("AccountID exists already");
						return "1 " + id + " " + accountType + " " + String.format("%.2f",initialBalance) + " " + tin;
					}
				}

				rs.close();
			} catch (Exception e) {
				System.out.println("Failed to select from AccountPrimarilyOwns");
				System.out.println(e);
				return "1 " + id + " " + accountType + " " + String.format("%.2f",initialBalance) + " " + tin;
			}
		} catch(Exception e){
			System.out.println("Failed to connect to DB");
			System.out.println(e);
            return "1 " + id + " " + accountType + " " + String.format("%.2f",initialBalance) + " " + tin;
		}

		//check for initial balance.... if it's null, set to 1000
		if(initialBalance <= 1000.00 ){
			initialBalance = 1000.00;
		}

		//if taxID doesn't exist in customer table... create new customer
		//return w/ error if new customer needed but the parameters are null
		try {
			Statement stmt = _connection.createStatement();
			try {

				String sql = "SELECT taxID " + 
								"FROM Customer " +
								"WHERE taxID = " + tin      ;
				ResultSet rs = stmt.executeQuery(sql);
				if (rs.next() == false) {
					//give newly made customer a default pin of 1717
					try {
				
						if(address == null || name == null){
							System.out.println("Address and Name cannot be null because we are inserting a new customer");
							return "1 " + id + " " + accountType + " " + String.format("%.2f",initialBalance) + " " + tin;
						}
						String hashedPin = helper.hashPin(1717);
						String sqlValues = tin + ",'" + address + "','"+ hashedPin +"','" + name+"'";
						sql = "INSERT INTO Customer " +
								"VALUES (" + sqlValues + ")";
						// System.out.println(sql);
						stmt.executeUpdate(sql);
					} catch (Exception e) {
						System.out.println("Unable to write to customer table");
						System.out.println(e);
						return "1 " + id + " " + accountType + " " + String.format("%.2f",initialBalance) + " " + tin;
					}
					
				}
				//update account table to reflect customer
				try {
					String bankBranch="A&S";
					sql = "INSERT INTO AccountPrimarilyOwns " + 
								"VALUES (" + id + ", " + tin + ",'" + bankBranch + "', " + initialBalance +
								", '" + helper.getDate() + "', 0, " + interestRate + ", '"  + accountType +
								"', 0)";
					stmt.executeUpdate(sql);
				} catch (Exception e) {
					System.out.println("Unable to write to account table");
					System.out.println(e);
					return "1 " + id + " " + accountType + " " + initialBalance + " " + tin;
				}
			} catch (Exception e) {
				System.out.println("Failed to select taxID from Customer table");
				System.out.println(e);
				return "1 " + id + " " + accountType + " " + String.format("%.2f",initialBalance) + " " + tin;
			}
		} catch (Exception e) {
			System.out.println("getStatement() failed");
			System.out.println(e);
			return "1 " + id + " " + accountType + " " + String.format("%.2f",initialBalance) + " " + tin;
		}
		//add to transaction table
		helper.addTransaction(initialBalance, TransactionType.DEPOSIT, 0, id, Integer.toString(-1));
		return "0 " + id + " " + accountType + " " + String.format("%.2f", initialBalance) + " " + tin;
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
		//check if account ID doesnt exist already, then check if linkedID exists, 
		//check that the linkedID is not closed
		//check that that the customers tax ID number is associated with the linkedID
		//Check that initialTopUp doesnt drain the linkedID account balance
		//Check that the linkedID is not a pocket
		//add to linkedWith table
		// String initialBalance = "";
		int linkedIsClosed = 0;
		double linkedAccountInitBalance = 0.0;
		String acctType = "";
		boolean linkedAccountExists = false;
		String dbID = "";
		String linkedTID="";
		int aid = 0;
		String bankBranch="";
		if(initialTopUp < 0){
			System.out.println("Initial top up cannot be negative");
			return "1";
		}
		try {
			Statement stmt = _connection.createStatement();
			try {
			
				String sql = "SELECT aID " +
								"FROM PocketAccountLinkedWith";
				ResultSet rs = stmt.executeQuery(sql);
				while(rs.next()){
					aid = rs.getInt("aID");
					dbID = Integer.toString(aid);
					if(id.equals(dbID)){
						System.out.println("Pocket account already exists");
						return "1 " + id + " POCKET " + "0.0" + " " + tin;
					}
				}
				rs.close();
				try {
		
					sql = "SELECT * " +
							"FROM AccountPrimarilyOwns";
					rs = stmt.executeQuery(sql);

					while(rs.next()){
						aid = rs.getInt("accountID");
						dbID = Integer.toString(aid);
						if(linkedId.equals(dbID)){
							linkedIsClosed = rs.getInt("isClosed");
							linkedAccountInitBalance = rs.getDouble("balance");
							acctType = rs.getString("accountType");
							linkedTID = rs.getString("taxID");
							bankBranch= rs.getString("bankBranch");
							linkedAccountExists = true;
							break;
						}
					}
					if(linkedAccountExists == false || linkedIsClosed == 1 || linkedAccountInitBalance - initialTopUp <= 0.01 || acctType.equals("POCKET")){
						System.out.println("Failed to create pocket either bc linkedAccount dne/closed, amount would close account, or account to be linked is pocket");
						return "1 " + id + " POCKET " + "0.0" + " " + tin;
					}
					try {
						sql = "UPDATE AccountPrimarilyOwns " +
							"SET balance = " + Double.toString(linkedAccountInitBalance - initialTopUp) + 
							" " + "WHERE accountId = " + dbID;
						stmt.executeUpdate(sql);
						rs.close();
					//TODO: check customerId is associated w/ this linked account by calling verifyTaxId in teller/atm
					} catch (Exception e) {
						System.out.println("Failed to update linkedWith account");
						System.out.println(e);
						return "1 " + id + " POCKET " + "0.0" + " " + tin;
					}
					// rs.close();
					try {
						sql = "INSERT INTO AccountPrimarilyOwns " + 
								"VALUES (" + id + ", " + tin + ", '" + bankBranch + "', " + initialTopUp +
								", '" + helper.getDate() + "'," + "0, " + 0.0 + ", '" +  "POCKET" +
								"', 0)";
						stmt.executeQuery(sql);
						try {
							sql = "INSERT INTO PocketAccountLinkedWith " +
									"VALUES (" + id + ", " + tin + ", " + linkedId+", "+linkedTID+", 0)"; 
							stmt.executeQuery(sql);
							helper.addTransaction(initialTopUp, TransactionType.TOPUP,0,linkedId,id);
						} catch (Exception e) {
							System.out.println("Failed to add new pocketAccount to Pocket table");
							System.out.println(e);
							return "1 " + id + " " + AccountType.POCKET + " " + "0.0" + " " + tin;						
						}
					} catch (Exception e) {
						System.out.println("Failed to add pocketAccount to AccountPrimarilyOwns");
						System.out.println(e);
						return "1 " + id + " " + AccountType.POCKET + " " + "0.0" + " " + tin;						
					}				
				} catch (Exception e) {
					System.out.println("Failed to check if linkedwith account exists");
					System.out.println(e);
					return "1 " + id + " " + AccountType.POCKET + " " + "0.0" + " " + tin;
				}
			} catch (Exception e) {
				System.out.println("Failed to check if PocketAccount already exists");
				System.out.println(e);
				return "1 " + id + " POCKET " + "0.0" + " " + tin;
			}
		} catch (Exception e) {
			System.out.println("getStatement() failed");
			System.out.println(e);
			return "1 " + id + " POCKET " + "0.0" + " " + tin;
		}
		System.out.println("Successfully created new Pocket account.");
		return "0 " + id + " POCKET " + String.format("%.2f",initialTopUp) + " " + tin;
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
		//create new customer and add them as a cowowner to existing account
		//Check if account exists, and that it isn't marked for close
		//check customer exists already, and if they are return
		//check if theyre associated with the account already, and if they are return
		//add to owns table
		boolean accountExists = false;
		int accountClosed = 0;
		int aid = 0;
		String dbID =  "";
		int cid = 0;
		String taxID = "";
		try{
			Statement stmt = _connection.createStatement();
			try {
			
				String sql = "SELECT * " +
								"FROM AccountPrimarilyOwns";
				ResultSet rs = stmt.executeQuery(sql);
				while(rs.next()){
					aid = rs.getInt("accountID");
					dbID = Integer.toString(aid);
					if(accountId.equals(dbID)){
						accountExists = true;
						// accountClosed = rs.getInt("isClosed");
						break;
					}
				}
				// rs.close();
				if(accountExists == false){
					rs.close();
					return "1";
				} else{
					accountClosed = rs.getInt("isClosed");
					rs.close();
					if(accountClosed == 1){
						return "1";
					}
					try {
						sql = "SELECT * " +
								"FROM Customer WHERE taxID = " + tin ;
						rs = stmt.executeQuery(sql);
						if(rs.next()!=false){
								System.out.println("Customer already exists");
								return "1";
						}
						rs.close();
						try {
							//Give newly made customer default pin of 1717
							String hashedPin = helper.hashPin(1717);
							sql = "INSERT INTO Customer " + 
									"VALUES (" + tin + ",'" + address + "','" + hashedPin + "','" + name+"')";
									// System.out.println(sql);
							stmt.executeQuery(sql);
							try {
					
								sql = "INSERT INTO Owns " + 
										"VALUES (" + accountId + ", " + tin + ", " + helper.newOwnsID() +")";
								
								stmt.executeQuery(sql);
							} catch (Exception e) {
								System.out.println("Failed to add customer to Owns table");
								System.out.println(e);
								return "1";
							}
						} catch (Exception e) {
							System.out.println("Failed to add new customer to table");
							System.out.println(e);
							return "1";
						}
					} catch (Exception e) {
						System.out.println("Failed to check if customer already exists");
						System.out.println(e);
						return "1";
					}
				}
			}catch(Exception e){
				System.out.println("Failed to check if accountID exists.");
				System.out.println(e);
				return "1";
			}
		}catch(Exception e){
			System.out.println("Failed to create statement.");
			System.out.println(e);
			return "1";
		}
		System.out.println("Added new relation and customer");
		return "0";
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
		//check accountID exists 
		//TODO: GUI function will check if account id belongs to customer
		//check amount is not negative
		//grab current account balance and add to new balance and add it to accountprimarily owns table
		int aid = 0;
		String dbID = "";
		boolean accountExists = false;
		double oldBalance = 0.00;
		double newBalance = 0.00;
		String acctType = "";
		String result = "1 ";
		int isClosed = 0;
		try{
			Statement stmt = _connection.createStatement();
			try {
		
				String sql = "SELECT * " +
								"FROM AccountPrimarilyOwns ";
				ResultSet rs = stmt.executeQuery(sql);
				while(rs.next()){
					aid = rs.getInt("accountID");
					dbID = Integer.toString(aid);
					if(accountId.equals(dbID)){
						accountExists = true;
						// oldBalance = rs.getDouble("balance");
						// String acctType = rs.getString("accountType");
						break;
					}
				}
				// rs.close();
				if(accountExists == false){
					System.out.println("Account doesn't exist");
					rs.close();
					return "1 0.0 0.0";
				}
				else{
			
					isClosed = rs.getInt("isClosed");
					if(isClosed == 1){
						rs.close();
						return "1 0.0 0.0";
					}
					oldBalance = rs.getDouble("balance");
					acctType = rs.getString("accountType");
					rs.close();
					if(amount <= 0.00){
						System.out.println("Cannot deposit negative amount.");
						result += Double.toString(oldBalance) + " " + Double.toString(newBalance);
						return result;					
					}
					if(acctType.equals("POCKET")){
						System.out.println("Cannot deposit into pocket.");
						return "1";
					}
					try {
				
						newBalance = oldBalance + amount;
						sql = "UPDATE AccountPrimarilyOwns " +
							"SET balance = " + Double.toString(newBalance) + 
							" " + "WHERE accountId = " + dbID;
						stmt.executeUpdate(sql);
						result = "0 " + String.format("%.2f",oldBalance) + " " + String.format("%.2f",newBalance);
					} catch (Exception e) {
						System.out.println("Failed to deposit and add new balance to table");
						System.out.println(e);
						result += String.format("%.2f",oldBalance) + " " +String.format("%.2f", oldBalance);
						return result;
					}
				}
			}catch (Exception e){
				System.out.println("Failed to check if account exists");
				System.out.println(e);
				return result + "0.0 0.0";
			}
		}catch (Exception e){
			System.out.println("Failed to create statement in showBalance");
			System.out.println(e);
			return result + "0.0 0.0";
		}
		System.out.println("Added deposit.");
		helper.addTransaction(amount,TransactionType.DEPOSIT,0,accountId, Integer.toString(-1));
		return result + "0.0 0.0";
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
		//check if account exists
		//TODO: GUI function will check if account id belongs to customer
		//return balance
		int aid = 0;
		String dbID = "";
		boolean accountExists = false;
		double balance = 0.00;
		try{
			Statement stmt = _connection.createStatement();
			try {
	
				String sql = "SELECT * " +
								"FROM AccountPrimarilyOwns";
				ResultSet rs = stmt.executeQuery(sql);
				while(rs.next()){
					aid = rs.getInt("accountID");
					dbID = Integer.toString(aid);
					if(accountId.equals(dbID)){
						accountExists = true;
						// balance = rs.getDouble("balance");
						break;
					}
				}
				// rs.close();
				if(accountExists == false){
					System.out.println("Account doesn't exist");
					return "1 0.0";
				}
				else{
					balance = rs.getDouble("balance");
					rs.close();
				}
			}catch (Exception e){
				System.out.println("Failed to check if account exists");
				System.out.println(e);
				return "1 0.0";
			}
		}catch (Exception e){
			System.out.println("Failed to create statement in showBalance");
			System.out.println(e);
			return "1 0.0";
		}
		
		return "0 " + String.format("%.2f",balance);
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
		//check if accountID exists and is pocket account
		//TODO: GUI function will check if account id belongs to customer
		//amount is not negative
		//if fee has not been applied apply $5 fee and mark fee as paid for the month
		//check if amount will not close the parent account
		int aid = 0;
		String dbID = "";
		boolean pocketExists = false;
		boolean linkedExists = false;
		int feePaid = 0;
		double pocketBalance = 0.00;
		double linkedBalance = 0.00;
		int linkedId = 0;
		String linkedID = "";

		if(amount <= 0){
			System.out.println("Cannot topup negative amount");
			return "1";
		}
		try{
			Statement stmt = _connection.createStatement();
			try {
			
				String sql = "SELECT * " +
								"FROM PocketAccountLinkedWith";
				ResultSet rs = stmt.executeQuery(sql);
				while(rs.next()){
					aid = rs.getInt("aID");
					dbID = Integer.toString(aid);
					if(accountId.equals(dbID)){
						pocketExists = true;
						break;
					}
				}
				if(pocketExists == false){
					System.out.println("Pocket account doesn't exist.");
					rs.close();
					return "1 0.0 0.0";
				}
			
				linkedId = rs.getInt("otherAccountID");
				feePaid = rs.getInt("feePaid");
				sql = "SELECT * " +
								"FROM AccountPrimarilyOwns";
				rs = stmt.executeQuery(sql);
				int isClosed = 0;
				while(rs.next()){
					aid = rs.getInt("accountID");
					dbID = Integer.toString(aid);
					if(accountId.equals(dbID)){
						pocketBalance = rs.getDouble("balance");
						isClosed = rs.getInt("isClosed");
						break;
					}
				}
				rs.close();
				
				linkedID = Integer.toString(linkedId);
				try {
			
					sql = "SELECT * " +
							"FROM AccountPrimarilyOwns " +
							"WHERE accountID = " +linkedID;
					rs = stmt.executeQuery(sql);
					while(rs.next()){
						linkedBalance = rs.getDouble("balance");
						linkedExists = true;
						break;
					}
					if(!linkedExists){
						System.out.println("Linked account does not exist.");
						rs.close();
						return "1 " + String.format("%.2f",linkedBalance) + " " + String.format("%.2f",pocketBalance);
					}
					if(isClosed==1){
						System.out.println("Pocket account is closed");
						return "1 " + String.format("%.2f",linkedBalance) + " " + String.format("%.2f",pocketBalance);
					}
					else{
			
						if(amount <= 0 || (linkedBalance - amount) < 0){
							return "1 " + String.format("%.2f",linkedBalance) + " " + String.format("%.2f",pocketBalance);
						}
						linkedBalance -= amount;
						if(linkedBalance<0){
							return "1 " + String.format("%.2f",linkedBalance) + " " + String.format("%.2f",pocketBalance);
						}
						if(linkedBalance<=0.01){
							sql = "UPDATE AccountPrimarilyOwns " +
                                    "SET isClosed = 1 " +
                                    "WHERE accountId = " + linkedID;
                            stmt.executeUpdate(sql);
                            sql = "UPDATE AccountPrimarilyOwns SET isClosed = 1 WHERE accountID = "+ dbID;
                            stmt.executeUpdate(sql);
						}
						try {
							sql = "UPDATE AccountPrimarilyOwns " +
							"SET balance = " + Double.toString(linkedBalance) + 
							" WHERE accountId = " + linkedID;
							stmt.executeUpdate(sql);
							try {
								if(feePaid == 0){
									helper.addTransaction(5,TransactionType.FEE,0,dbID,"-1");
									pocketBalance = pocketBalance + amount - 5;
									try {
										sql = "UPDATE PocketAccountLinkedWith " +
												"SET feePaid = " + Integer.toString(1) + 
												" WHERE aId = " + dbID;
										stmt.executeUpdate(sql);
									} catch (Exception e) {
										System.out.println("Failed to update feePaid for pocket account");
										System.out.println(e);
										return "1" + String.format("%.2f",linkedBalance) + " " + String.format("%.2f",pocketBalance);
									}
								}
								else{
									pocketBalance = pocketBalance + amount;
								}								
								sql = "UPDATE AccountPrimarilyOwns " +
										"SET balance = " + (pocketBalance) +
										" WHERE accountId = " + dbID;
								stmt.executeUpdate(sql);
								helper.addTransaction(amount, TransactionType.TOPUP, 0, linkedID, accountId);
								
								return "0 " + String.format("%.2f",(linkedBalance)) + " " + String.format("%.2f",(pocketBalance));
							} catch (Exception e) {
								System.out.println("Failed to update pocketAccount with topup");
								System.out.println(e);
								return "1" + String.format("%.2f",linkedBalance) + " " + String.format("%.2f",pocketBalance);
							}
						} catch (Exception e) {
							System.out.println("Failed to subtract topup from linkedAccount");
							System.out.println(e);
							return "1" + String.format("%.2f",linkedBalance) + " " + String.format("%.2f",pocketBalance);
						}
					}
				} catch (Exception e) {
					System.out.println("Failed to update linkedAccount balance");
					System.out.println(e);
					return "1" + String.format("%.2f",linkedBalance) + " " + String.format("%.2f",pocketBalance);
				}
			} catch (Exception e){
				System.out.println("Failed to check if pocket account exists");
				System.out.println(e);
				return "1 0.0 0.0" ;
			}
		} catch (Exception e){
			System.out.println("Failed to create statement for topUp()");
			System.out.println(e);
			return "1 0.0 0.0";
		}
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
		//check if from exists and is pocket
		//check if from belongs to customer
		//check if to exists and is pocket
		//check if amount is negative
		//check if amount closes from account
		//get new balances and write to table for both accounts
		int fromid = 0;
		String fromID = "";
		boolean pocketFromExists = false;
		boolean pocketToExists = false;
		int fromFeePaid = 0;
		int toFeePaid = 0;
		double fromBalance = 0.00;
		double toBalance = 0.00;
		int toid = 0;
		String toID = "";
		if(amount <= 0){
			System.out.println("Cannot pay friend negative amount");
			return "1";
		}

		try{
			Statement stmt = _connection.createStatement();
			try {
			
				String sql = "SELECT * " +
								"FROM PocketAccountLinkedWith";
				ResultSet rs = stmt.executeQuery(sql);
				while(rs.next()){
					int id = rs.getInt("aID");
					String ID = Integer.toString(id);
					if(ID.equals(from)){
						pocketFromExists = true;
						fromid = id;
						fromID = ID;
						fromFeePaid = rs.getInt("feePaid");
					}
					if(ID.equals(to)){
						pocketToExists = true;
						toid = id;
						toID = ID;
						toFeePaid = rs.getInt("feePaid");
					}
				}
				if(pocketFromExists == false || pocketToExists == false){
					System.out.println("Pocket accounts dont exist or are not pocketaccounts");
					rs.close();
					return "1";
				}
		
				sql = "SELECT * " +
								"FROM AccountPrimarilyOwns";
				rs = stmt.executeQuery(sql);
				int toClosed = 0;
				int fromClosed = 0;
				while(rs.next()){
					int id = rs.getInt("accountID");
					String ID = Integer.toString(id);
					if(ID.equals(fromID)){
						fromBalance = rs.getDouble("balance");
						fromClosed = rs.getInt("isClosed");
					}
					if(ID.equals(toID)){
						toBalance = rs.getDouble("balance");
						toClosed = rs.getInt("isClosed");
					}
				}
				if(toClosed == 1 || fromClosed == 1){
					System.out.println("An account is closed");
					return "1";
				}
				if(pocketFromExists == false || pocketToExists == false){
					System.out.println("Pocket accounts dont exist.");
					rs.close();
					return "1";
				}

				if(fromBalance - amount<0){
					System.out.println("Not enough money in account.");
					return "1";
				} else if (fromBalance - amount<=0.01) {
					try {
						sql = "UPDATE AccountPrimarilyOwns SET isClosed = 1 WHERE accountID = "+ fromID;
					} catch (Exception e ){
						System.out.println("Could not close from account");
						System.out.println(e);
						return "1";
					}
				}
				if(toFeePaid == 0){
					helper.addTransaction(5,TransactionType.FEE,0,toID,"-1");
			
					toBalance = amount + toBalance - 5;
					try {
						sql = "UPDATE PocketAccountLinkedWith " +
						"SET feePaid = " + Integer.toString(1) + 
						"WHERE aID = " + toID;
						stmt.executeUpdate(sql);						
					} catch (Exception e) {
						System.out.println("Failed to update toFeePaid");
						System.out.println(e);
						return "1";					
					}
				}
				else{
					toBalance = amount + toBalance;
				}

				try {
					sql = "UPDATE AccountPrimarilyOwns " +
							"SET balance = " + Double.toString(toBalance) +
							"WHERE accountID = " + toID;
					stmt.executeUpdate(sql);
				} catch (Exception e) {
					System.out.println("Failed to update toBalance");
					System.out.println(e);
					return "1";
				}
				if(fromFeePaid == 0){
					helper.addTransaction(5,TransactionType.FEE,0,fromID,"-1");
		
					fromBalance = fromBalance - amount - 5;
					try {
						sql = "UPDATE PocketAccountLinkedWith " +
						"SET feePaid = " + Integer.toString(1) + 
						"WHERE aID = " + fromID;
						stmt.executeUpdate(sql);						
					} catch (Exception e) {
						System.out.println("Failed to update fromFeePaid");
						System.out.println(e);
						return "1";					
					}
				}
				else{
					fromBalance = fromBalance - amount;
				}
			
				try {
					sql = "UPDATE AccountPrimarilyOwns " +
							"SET balance = " + Double.toString(fromBalance) +
							"WHERE accountID = " + fromID;
					stmt.executeUpdate(sql);
				} catch (Exception e) {
					System.out.println("Failed to update fromBalance");
					System.out.println(e);
					return "1";
				}
			} catch (Exception e){
				System.out.println("Failed to get pocket accounts from table");
				System.out.println(e);
				return "1";
			}
		} catch (Exception e){
			System.out.println("Failed to create statement in payFriend");
			System.out.println(e);
			return "1";
		}
		System.out.println("Paid friend.");
		helper.addTransaction(amount,TransactionType.PAYFRIEND,0, from, to);
		return "0 " + String.format("%.2f",(fromBalance)) + " " + String.format("%.2f",(toBalance));
	}


	@Override
	public String listClosedAccounts(){
		// String res = "\n-------------CLOSED ACCOUNTS-------------\n";
		String res = "";
        try{
            Statement stmt = helper.getConnection().createStatement();
            try{
                String sql = "SELECT * FROM AccountPrimarilyOwns WHERE isClosed = 1";
                ResultSet accounts = stmt.executeQuery(sql);
                while(accounts.next()){
                    res = "ACCOUNTID: " + accounts.getString("accountID") + " PRIMARY OWNER: " + accounts.getString("taxID") +"\n";
                }
                } catch(Exception e){
                System.out.println("Failed to get accounts.");
                System.out.println(e);
            }
        } catch(Exception e){
            System.out.println("Failed to create statement");
            System.out.println(e);
        }
		return res;
	}





}

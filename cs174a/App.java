package cs174a;                                             // THE BASE PACKAGE FOR YOUR APP MUST BE THIS ONE.  But you may add subpackages.

// You may have as many imports as you need.
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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
	void exampleAccessToDB()
	{
		// Statement and ResultSet are AutoCloseable and closed automatically.
		try( Statement statement = _connection.createStatement() )
		{
			try( ResultSet resultSet = statement.executeQuery( "select owner, table_name from all_tables" ) )
			{
				while( resultSet.next() )
					System.out.println( resultSet.getString( 1 ) + " " + resultSet.getString( 2 ) + " " );
			}
		}
		catch( SQLException e )
		{
			System.err.println( e.getMessage() );
		}
	}

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
	String dropTables(){
		System.out.println("Dropping tables in database...............");
		Statement stmt = _connection.createStatement();

		try {
			System.out.println("Dropping table GlobalDate");
			String sql = "DROP TABLE GlobalDate";
			stmt.executeUpdate(sql);
		} catch (Exception e) {
			System.out.println("Failed to drop table GlobalDate");
			return "1";
		}

		try {
			System.out.println("Dropping table Customer");
			String sql = "DROP TABLE Customer";
			stmt.executeUpdate(sql);
		} catch (Exception e) {
			System.out.println("Failed to drop table Customer");
			return "1";
		}

		try {
			System.out.println("Dropping table AccountPrimarilyOwns");
			String sql = "DROP TABLE AccountPrimarilyOwns";
			stmt.executeUpdate(sql);
		} catch (Exception e) {
			System.out.println("Failed to drop table AccountPrimarilyOwns");
			return "1";
		}

		try {
			System.out.println("Dropping table Owns");
			String sql = "DROP TABLE Owns";
			stmt.executeUpdate(sql);
		} catch (Exception e) {
			System.out.println("Failed to drop table Owns");
			return "1";
		}

		try {
			System.out.println("Dropping table TransactionBelongs");
			String sql = "DROP TABLE TransactionBelongs";
			stmt.executeUpdate(sql);
		} catch (Exception e) {
			System.out.println("Failed to drop table TransactionBelongs");
			return "1";
		}

		try {
			System.out.println("Dropping table PocketAccountLinkedWith");
			String sql = "DROP TABLE PocketAccountLinkedWith";
			stmt.executeUpdate(sql);
		} catch (Exception e) {
			System.out.println("Failed to drop table PocketAccountLinkedWith");
			return "1";
		}

		return "r";
	}

	/**
	 * Create all of your tables in your DB.
	 * @return a string "r", where r = 0 for success, 1 for error.
	 */
	String createTables(){
		System.out.println("Creating tables in database.................");
		Statement stmt = _connection.createStatement();
		try {
			System.out.println("Creating table GlobalDate");
			String sql = "CREATE TABLE GlobalDate(" + 
							"date DATE," + 
							"PRIMARY KEY (date))";
			stmt.executeUpdate(sql);
			
		} catch (Exception e) {
			System.out.println("Failed to create table GlobalDate.");
			return "1";
		}

		try {
			System.out.println("Creating table Customer.");
			String sql = "CREATE TABLE Customer(" + 
							"taxID INTEGER," +
							"address CHAR (*)," + 
							"pin INTEGER," + 
							"name CHAR(*)," + 
							"PRIMARY KEY (taxID))";
			
			stmt.executeUpdate(sql);
		} catch (Exception e) {
			System.out.println("Failed to create table Customer.");
			return "1";
		}

		try {
			System.out.println("Creating table AccountPrimarilyOwns.");
			String sql = "CREATE TABLE AccountPrimarilyOwns(" +
							"accountID INTEGER,"  +
							"taxID INTEGER NOT NULL," +
							"bankBranch CHAR(*)," +
							"balance INTEGER," +
							"balanceEndDate CHAR(*)," +
							"balanceStartDate CHAR(*)," +
							"isClosed BOOLEAN," +
							"interestRate REAL," +
							"type CHAR(*)," +
							"interestAdded BOOLEAN," +
							"PRIMARY KEY(accountID, taxID)," +
							"FOREIGN KEY (taxID) REFERENCES" +
							"Customer ON DELETE CASCADE))";
			stmt.executeUpdate(sql);
		} catch (Exception e) {
			System.out.println("Failed to table AccountPrimarilyOwns.");
			return "1";
		}

		try {
			System.out.println("Creating table Owns.");
			String sql = "CREATE TABLE Owns("  +
							"accountID INTEGER," +
							"taxID INTEGER," +
							"PRIMARY KEY(accountID, taxID)," +
							"FOREIGN KEY(accountID) REFERENCES AccountPrimarilyOwns," +
							"FOREIGN KEY(taxID) REFERENCES Customer)";
			stmt.executeUpdate(sql);			
		} catch (Exception e) {
			System.out.println("Failed to create table Owns.");
			return "1";
		}

		try {
			System.out.println("Creating table TransactionBelongs");
			String sql = "CREATE TABLE TransactionBelongs(" +
							"amount REAL," +
							"fee INTEGER," +
							"type CHAR(*)," +
							"date DATE," +
							"checkNo INTEGER," +
							"transactionID INTEGER," +
							"accountID INTEGER NOT NULL," +
							"FOREIGN KEY(accountID) REFERENCES" +
							"AccountPrimarilyOwns ON DELETE CASCADE," +
							"PRIMARY KEY(transactionID, accountID))";
			stmt.executeUpdate(sql);			
		} catch (Exception e) {
			System.out.println("Failed to create table TransactionBelongs.");
			return "1";
		}

		try {
			System.out.println("Creating table PocketAccountLinkedWith");
			String sql = "CREATE TABLE PocketAccountLinkedWith("  +
							"accountID INTEGER," +
							"otherAccountID INTEGER NOT NULL," +
							"feePaid BOOLEAN," +
							"PRIMARY KEY (accountID, otherAccountID)," +
							"FOREIGN KEY (accountID) REFERENCES " +
							"AccountPrimarilyOwns ON DELETE CASCADE," +
							"FOREIGN KEY accountID REFERENCES" +
							"OtherAccount ON DELETE CASCADE)";			
			stmt.executeUpdate(sql);			
		} catch (Exception e) {
			System.out.println("Failed to create table PocketAccountLinkedWith.");
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
	String setDate( int year, int month, int day){
		return "r yyyy-mm-dd";
	}


	/**
	 * Example of one of the testable functions.
	 */
	@Override
	public String listClosedAccounts()
	{
		return "0 it works!";
	}

	/**
	 * Another example.
	 */
	@Override
	public String createCheckingSavingsAccount( AccountType accountType, String id, double initialBalance, String tin, String name, String address )
	{
		return "0 " + id + " " + accountType + " " + initialBalance + " " + tin;
	}


}

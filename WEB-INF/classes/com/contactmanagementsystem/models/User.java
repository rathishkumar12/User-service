package com.contactmanagementsystem.models;
import com.contactmanagementsystem.validations.*;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;


import com.contactmanagementsystem.helpers.DatabaseConnectivity;
import com.contactmanagementsystem.helpers.MailSender;

public class User {
	
	private int id;
	private String name = "";
	private String email= "";
	private String phoneNumber= "";
	private String password = "";
	private String isActive= "";
	private String etag = "";
	private static DatabaseConnectivity databaseConnectivity ;
	public static User currentUser = null;
	private String imageURL = "";
	private int type;
	private int userId;
	public User() {}
	public User(int id, String name, String email, String phoneNumber, String password , String isActive) {
		super();
		this.id = id;
		this.name = name;
		this.email = email;
		this.phoneNumber = phoneNumber;
		this.password = password;
		this.isActive = isActive;
	}
	public User(int id, String name, String email, String phoneNumber, String password , String isActive,String imageURL) {
		super();
		this.id = id;
		this.name = name;
		this.email = email;
		this.phoneNumber = phoneNumber;
		this.password = password;
		this.isActive = isActive;
		this.imageURL = imageURL;
	}

	public User(int id, String name, String email, String phoneNumber, String password , String isActive,String imageURL,int type ,int userId,String etag) {
		super();
		this.id = id;
		this.name = name;
		this.email = email;
		this.phoneNumber = phoneNumber;
		this.password = password;
		this.isActive = isActive;
		this.imageURL = imageURL;
		this.type = type;
		this.userId = userId;
		this.etag = etag;
	}
	
	public User(int id, String name, String email, String phoneNumber) {
		super();
		this.id = id;
		this.name = name;
		this.email = email;
		this.phoneNumber = phoneNumber;
	}
	public User(int id, String name, String email, String phoneNumber,String imageURL) {
		super();
		this.id = id;
		this.name = name;
		this.email = email;
		this.phoneNumber = phoneNumber;
		this.imageURL = imageURL;
	}

	public User(String name, String email, String phoneNumber,String imageURL,int type,int userId,String etag) {
		super();
		this.id = id;
		this.name = name;
		this.email = email;
		this.phoneNumber = phoneNumber;
		this.imageURL = imageURL;
		this.type = type;
		this.userId = userId;
		this.etag = etag;
	}
	
	public User( String name, String email, String phoneNumber ,String password) {
		super();
		this.name = name;
		this.email = email;
		this.phoneNumber = phoneNumber;
		this.password = password;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public String getEtag() {
		return etag;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) throws BaseException {
		UserException.isNameValid(name);
		this.name = name;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) throws BaseException, SQLException {
		UserException.isEmailValid(email);
		UserException.isEmailUnique(email);
		this.email = email;
	}
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) throws BaseException {
		UserException.isMobileNumberValid(phoneNumber);
		if(this.phoneNumber != null && this.phoneNumber.equals(phoneNumber) )
			throw new UserException("Phone number already exist.");
		this.phoneNumber = phoneNumber;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) throws UserException {
		this.password = password;
	}
	public String getIsActive() {
		return isActive;
	}
	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}
	public String getImageURL() {
		return imageURL;
	}
	public void setImageURL(String imageURL) {
		this.imageURL = imageURL;
	}
	
	public static ArrayList<User> all() throws SQLException
	{
		connectToDb();
		ArrayList<User> users = new ArrayList<>();
		ResultSet resultSet = databaseConnectivity.executeQuery("Select * from users");
		while(resultSet.next())
		{
			users.add(new User( Integer.parseInt(resultSet.getString("id")) , resultSet.getString("name") ,resultSet.getString("email"),
					resultSet.getString("phone_number") , resultSet.getString("password"),resultSet.getString("is_active"),resultSet.getString("image_url")));
		}  	
		return users;
	}

	public static ArrayList<User> allGUsers(int userId) throws SQLException
	{
		connectToDb();
		ArrayList<User> users = new ArrayList<>();
		ResultSet resultSet = databaseConnectivity.executeQuery("Select * from users where type = 1 and user_id = " + userId);
		while(resultSet.next())
		{
			users.add(new User( Integer.parseInt(resultSet.getString("id")) , resultSet.getString("name") ,resultSet.getString("email"),
					resultSet.getString("phone_number") , resultSet.getString("password"),resultSet.getString("is_active"),resultSet.getString("image_url"),resultSet.getInt("type"),resultSet.getInt("user_id"),resultSet.getString("etag")));
		}  	
		return users;
	}

	public static ArrayList<User> join( String tableName , String on , String condition ) throws SQLException
	{
		connectToDb();
		ArrayList<User> users = new ArrayList<>();
		ResultSet resultSet = databaseConnectivity.executeQuery("select users.* from users JOIN " + tableName + " on " + on + " where " + condition );
		while(resultSet.next())
		{
			users.add(new User( Integer.parseInt(resultSet.getString("id")) , resultSet.getString("name") ,resultSet.getString("email"),
					resultSet.getString("phone_number") , resultSet.getString("password"),resultSet.getString("is_active"),resultSet.getString("image_url"),resultSet.getInt("type"),resultSet.getInt("user_id"),resultSet.getString("etag")));
			//System.out.println("etag fromm db : "+users.get(0).toString());
		}  	
		return users;
	}
	
	public boolean save()
	{
		try {
			connectToDb();
			
			databaseConnectivity.executeUpdateQuery("INSERT INTO users ( name , email , phone_number , password )"+ "VALUES ('"+ name + "','" + email +  "','" + phoneNumber + 
				"','" + password +"')");
		}
		catch ( SQLException e )
		{
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public boolean saveGUser()
	{
		try {
			connectToDb();
			PreparedStatement preparedStatement = databaseConnectivity.getPreparedStatement("INSERT INTO users ( name , email , phone_number , password , type , user_id , etag ) VALUES (?,?,?,?,?,?,?)");
			preparedStatement.setString(1, name);
			preparedStatement.setString(2, email);
			preparedStatement.setString(3, phoneNumber);
			preparedStatement.setString(4, " ");
			preparedStatement.setInt(5, type);
			preparedStatement.setInt(6, userId);
			preparedStatement.setString(7, etag);
			preparedStatement.executeUpdate();
		}
		catch ( SQLException e )
		{
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public static boolean update( String email  , String columnName , String value )
	{
		try {
			connectToDb();
			databaseConnectivity.executeUpdateQuery("UPDATE users SET " + columnName + " = '" + value +"' WHERE email = '" + email+"'");
		}
		catch( SQLException e )
		{
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public  boolean updateAll()
	{
		try {
			connectToDb();
			PreparedStatement preparedStatement = databaseConnectivity.getPreparedStatement("UPDATE users SET name = ? , email = ? , phone_number = ? , image_url = ? , etag = ? WHERE id = ? ");
			preparedStatement.setString(1, name);
			preparedStatement.setString(2, email);
			preparedStatement.setString(3, phoneNumber);
			preparedStatement.setString(4, imageURL);
			preparedStatement.setString(5, etag);
			preparedStatement.setInt(6, id);
			int rows = preparedStatement.executeUpdate();
			System.out.println(rows + "Inserted by prepared statements");
		}
		catch( SQLException e )
		{
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public  boolean updateAllG()
	{
		try {
			connectToDb();
			PreparedStatement preparedStatement = databaseConnectivity.getPreparedStatement("UPDATE users SET name = ? , email = ? , phone_number = ? , image_url = ? , etag = ? WHERE email = ? ");
			preparedStatement.setString(1, name);
			preparedStatement.setString(2, email);
			preparedStatement.setString(3, phoneNumber);
			preparedStatement.setString(4, imageURL);
			preparedStatement.setString(5, etag);
			preparedStatement.setString(6, email);
			int rows = preparedStatement.executeUpdate();
			System.out.println(rows + "Inserted by prepared statements");
		}
		catch( SQLException e )
		{
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public static boolean deleteGUsers(  int userID )
	{
		try {
			connectToDb();
			PreparedStatement preparedStatement = databaseConnectivity.getPreparedStatement("DELETE FROM users WHERE user_id = ? and type = 1");
			preparedStatement.setInt(1, userID);
			preparedStatement.executeUpdate();
		}
		catch( SQLException e )
		{
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public static ArrayList<User> search( int userID  ) throws SQLException
	{
		connectToDb();
		ArrayList<User> users = new ArrayList<>();
		PreparedStatement preparedStatement = databaseConnectivity.getPreparedStatement("SELECT * FROM users WHERE id = ?");
		preparedStatement.setInt(1, userID);
		ResultSet resultSet = preparedStatement.executeQuery();
		while(resultSet.next())
		{
			users.add(new User( Integer.parseInt(resultSet.getString("id")) , resultSet.getString("name") ,resultSet.getString("email"),
					resultSet.getString("phone_number") , resultSet.getString("password"),resultSet.getString("is_active"),resultSet.getString("image_url")));
		}  
		return users;
	}
	
	public static ArrayList<User> searchBy( String columnName , String value  ) throws SQLException
	{
		connectToDb();
		ArrayList<User> users = new ArrayList<>();
		PreparedStatement preparedStatement = databaseConnectivity.getPreparedStatement("SELECT * FROM users WHERE " + columnName + " = ? ");
		preparedStatement.setString(1, value);
		ResultSet resultSet = preparedStatement.executeQuery();
		while(resultSet.next())
		{
			users.add(new User( Integer.parseInt(resultSet.getString("id")) , resultSet.getString("name") ,resultSet.getString("email"),
					resultSet.getString("phone_number") , resultSet.getString("password"),resultSet.getString("is_active")));
		}  
		System.out.println("search by user prep");
		return users;
	}
	
	public static boolean sendVerification( String email )
	{
		MailSender.sendMail(email,"Account verification - Contact management system",
				"<h3>Contact Management System</h3></br><a href=http://localhost:8082/User/user/verify?action_name=verify&email=" 
		+ email + "> Click here to verify. </a> <br><b>Note:</b>Do not share this mail with anyone.");
		return true;
	}
	
	private static void connectToDb()
	{
		try {
			databaseConnectivity = new DatabaseConnectivity("jdbc:mysql://localhost:3306/contact_management_system", "root", "", "com.mysql.cj.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
}

package com.contactmanagementsystem.models;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import com.contactmanagementsystem.helpers.DatabaseConnectivity;

public class Token {
	
	private String token ;
	private int userId ;
	private String expiryAt ;
	private static DatabaseConnectivity databaseConnectivity ;
	
	public Token(String token, int userId, String expiryAt) {
		super();
		this.token = token;
		this.userId = userId;
		this.expiryAt = expiryAt;
	}
	
	public Token() {}
	
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public String getExpiryAt() {
		return expiryAt;
	}
	public void setExpiryAt(String expiryAt) {
		this.expiryAt = expiryAt;
	}
	
	public static boolean create(String token,int userId)
	{
		try {
			connectToDb();
			databaseConnectivity.executeUpdateQuery("INSERT INTO tokens ( token , user_id , expiry_at ) VALUES "
					+ "('"+ token + "'," + userId +  " , DATE_ADD(now(), INTERVAL 30 MINUTE ) )");	
		}
		catch ( SQLException e )
		{
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public static boolean delete( String token  )
	{
		try {
			connectToDb();
			databaseConnectivity.executeUpdateQuery("DELETE FROM TOKENS WHERE TOKEN = '"+ token +"'");
		}
		catch( SQLException e )
		{
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public static Token search( String token  ) throws SQLException
	{
		connectToDb();
		ResultSet resultSet = databaseConnectivity.executeQuery( "SELECT * FROM tokens WHERE TOKEN = '"+ token +"'");
		if(resultSet.next())
		{
			return new Token( resultSet.getString("token"),Integer.parseInt(resultSet.getString("user_id")),resultSet.getString("expiry_at"));
		}
		return null;
	}
	
	public static boolean validateToken(String token,int userId)
	{
		Token tokenObj = null;
		try {
			 tokenObj = search( token );
		}
		catch (SQLException e){
			return false;
		}
		DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		LocalDateTime now = LocalDateTime.now();
		if( tokenObj != null && tokenObj.getUserId() == userId && token.equals(tokenObj.getToken()) 
				&& Timestamp.valueOf(tokenObj.getExpiryAt()).compareTo( Timestamp.valueOf(dateTimeFormatter.format(now)) ) > 0 )
		{
			return true;
		}
		return false;
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

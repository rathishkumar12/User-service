package com.contactmanagementsystem.controllers;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.*;
import java.io.BufferedReader;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.contactmanagementsystem.authentications.UserPrincipal;
import com.contactmanagementsystem.authentications.DuoCheck;
import com.contactmanagementsystem.helpers.*;
import com.contactmanagementsystem.models.Token;
import com.contactmanagementsystem.models.User;
import com.contactmanagementsystem.validations.UserException;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;

public class UserApi extends HttpServlet {
	HashMap<String,String> SUCCUESS = new HashMap<>();
	HashMap<String,String> FAILUIRE = new HashMap<>();
	public UserApi() {
		super();
		SUCCUESS.put("success","true");
		SUCCUESS.put("message","Successful");
		FAILUIRE.put("success","false");
		FAILUIRE.put("message","Something went wrong,check your params.");
	}
	

	@Override
	protected void doGet(HttpServletRequest httpServletRequest, HttpServletResponse response)  throws IOException, ServletException
	{
		response.setContentType("application/json");
		response.setCharacterEncoding("utf-8");
		String pathInfo = httpServletRequest.getPathInfo();
		if( pathInfo == null || pathInfo.substring(1).equals("") )
		{
			if( httpServletRequest.getUserPrincipal() != null && httpServletRequest.getSession().getAttribute("duoObject") != null 
				&& ((DuoCheck)httpServletRequest.getSession().getAttribute("duoObject")).getDuoCode() != null
				&& ((DuoCheck)httpServletRequest.getSession().getAttribute("duoObject")).getState() != null )
			{
				SUCCUESS.put("message", "Authorized.");
				SUCCUESS.put("user",  String.valueOf(((UserPrincipal)httpServletRequest.getUserPrincipal()).getUser().getId()) );
				response.getWriter().print(new JSONObject(SUCCUESS));
			}
			else
			{
				FAILUIRE.put("message", "Not authorized");
				response.getWriter().print(new JSONObject(FAILUIRE));
			}
		}
		else
		{
			int userId = ((UserPrincipal)httpServletRequest.getUserPrincipal()).getUser().getId();
			JSONObject jsonObject = null;
			try {
				jsonObject = new JSONObject(User.search(userId).get(0));
				jsonObject.remove("password");
				response.getWriter().print(jsonObject.toString());
			} catch (Exception e) {
				FAILUIRE.put("message",e.getMessage());
				response.getWriter().print(new JSONObject(FAILUIRE).toString());
			}
		}
	}

	@Override
	protected void doPut(HttpServletRequest httpServletRequest, HttpServletResponse response) throws IOException 
	{
		JSONObject userJSON = ServletApiHelper.getJSONBody(httpServletRequest);
		response.setContentType("application/json");
		response.setCharacterEncoding("utf-8");
		int userId = Integer.parseInt( httpServletRequest.getPathInfo().substring(1) );
		try 
		{
			if (!httpServletRequest.getUserPrincipal().getName().equals(userJSON.getString("email")))
			{
				UserException.isEmailUnique(userJSON.getString("email"));
			}
			String fileName = User.search(userId).get(0).getImageURL();
			if(fileName == null || fileName.equals("null"))
				fileName = "";
			if( userJSON.getString("imageBase64").length() > 0 )
			{
				fileName = userJSON.getString("imageFileName");
				decodeToImageAndSave(userJSON.getString("imageBase64").substring(userJSON.getString("imageBase64").indexOf(',') + 1),fileName);
			}
			User user = new User(userId, userJSON.getString("name"),userJSON.getString("email"), userJSON.getString("phoneNumber"),fileName);
			if(user.updateAll())
			{
				SUCCUESS.put("message", "Profile updated successfully.");
				response.getWriter().print(new JSONObject(SUCCUESS).toString());
			}
			else {
				response.getWriter().print(new JSONObject(FAILUIRE).toString());
			}
		} catch (Exception e) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			response.getWriter().print("An User with the same email already exist !!!");
		}
	}
	
	@Override
	protected void doOptions(HttpServletRequest httpServletRequest, HttpServletResponse response) throws IOException
	{
		response.setContentType("application/json");
		response.setCharacterEncoding("utf-8");
		String email = httpServletRequest.getPathInfo().substring(1,httpServletRequest.getPathInfo().lastIndexOf('/'));
		int userId = Integer.parseInt(httpServletRequest.getPathInfo().substring( httpServletRequest.getPathInfo().lastIndexOf('/') + 1 ));
		try
		{
			if(!User.search(userId).get(0).getEmail().equals(email))
				response.getWriter().print(new JSONObject(FAILUIRE).toString());
			else
			{
				String UUID = String.valueOf(java.util.UUID.randomUUID());
				Token.create(UUID, userId);
				MailSender.sendMail(email, "Password reset link - Contact management system",
					"<h3>Contact Management System</h3></br><a href=http://localhost:8082/User/ForgotPassword.jsp?action_name=forgot_password_reset&email="
					+ email + "&session_id=" + UUID
					+ "> Click here to reset your password. </a> Do not share this mail with anyone.<br><b>Note:</b>The link will expire within 30 minutes.");
				SUCCUESS.put("message", "Password Reset mail Sent successfully...");
				response.getWriter().print(new JSONObject(SUCCUESS).toString());
			}
		}
		catch(SQLException e )
		{
			response.getWriter().print(new JSONObject(FAILUIRE).toString());
		}
	}
	
	

	@Override
	protected void doPost(HttpServletRequest httpServletRequest, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("application/json");
		response.setCharacterEncoding("utf-8");
		JSONObject credentials = getJSONBody(httpServletRequest);
		try {  	
			httpServletRequest.login(credentials.getString("username"),credentials.getString("password"));
			SUCCUESS.put("message", "successfully logged in...");
			response.getWriter().print(new JSONObject(SUCCUESS));
			System.out.println("logged in");
		}  catch (Exception e) {
			FAILUIRE.put("message", e.getMessage());
			response.getWriter().print(new JSONObject(FAILUIRE));
		}
	}
	

	@Override
	protected void doDelete(HttpServletRequest httpServletRequest, HttpServletResponse response) throws IOException
	{
		response.setContentType("application/json");
		response.setCharacterEncoding("utf-8");
		int userId = Integer.parseInt( httpServletRequest.getPathInfo().substring(1) );
		if( httpServletRequest.getUserPrincipal() != null && ((UserPrincipal)httpServletRequest.getUserPrincipal()).getUser().getId() == userId )
			httpServletRequest.getSession().invalidate();
		else
		{
			FAILUIRE.put("message", "No user were logged in...");
			response.getWriter().print(new JSONObject(FAILUIRE));
		}
		SUCCUESS.put("message", "Logout Successful...");
		response.getWriter().print(new JSONObject(SUCCUESS));
	}
	
	
	public static boolean decodeToImageAndSave(String imageString , String fileNameAndExtension) {
        try {
        byte[] data = Base64.getDecoder().decode(imageString);
        String uploadPath = "C:\\Program Files\\Apache Software Foundation\\Tomcat 9.0_Tomcat_9_without_eclipse\\webapps\\ContactManagementSystem1\\images\\users\\" + fileNameAndExtension;
        OutputStream stream = new FileOutputStream(uploadPath);
        stream.write(data);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }
	
	public static JSONObject getJSONBody( HttpServletRequest httpServletRequest ) throws IOException
	{
		StringBuilder buffer = new StringBuilder();
		BufferedReader reader = httpServletRequest.getReader();
		String line;
		while ((line = reader.readLine()) != null) {
			buffer.append(line);
			buffer.append(System.lineSeparator());
		}
		String data = buffer.toString();
		return new JSONObject(data);
	}
	
}

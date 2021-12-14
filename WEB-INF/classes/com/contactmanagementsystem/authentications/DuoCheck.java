package com.contactmanagementsystem.authentications;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.duosecurity.Client;
import com.duosecurity.exception.DuoException;

@WebServlet("/DuoCheck")
public class DuoCheck extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Client duoClient;
	private String state = null;
	private String duoCode = null;
	public DuoCheck() throws DuoException {
		super();
		duoClient = new Client("DITGSNUZQ32AZBQSC27L", "iCSkEboX1oMx5dw9iAY50X3iMYKaKOo7ha7bURce",
				"api-064d3ecb.duosecurity.com", "http://localhost:8082/User/Authorize");
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			response.sendRedirect(getAuthURL(request));
		} catch (DuoException e) {
			e.printStackTrace();
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

	public static boolean checkAuthorization(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		if (request.getSession().getAttribute("state") == null) {
			DuoCheck duoCheck;
			try {
				duoCheck = new DuoCheck();
				duoCheck.doGet(request, response);
				return true;
			} catch (DuoException e) {
				e.printStackTrace();
			}
		}
		return false;
	}

	public static String checkAuthorizationForEmber(HttpServletRequest request) throws ServletException, IOException {
			DuoCheck duoCheck;
			try {
				duoCheck = new DuoCheck();
				return duoCheck.getAuthURL(request);
			} catch (DuoException e) {
				e.printStackTrace();
			}
		return null;
	}

	private String getAuthURL(HttpServletRequest request) throws DuoException {
		if (request.getUserPrincipal() == null)
			return null;
		duoClient.healthCheck();
		String state = duoClient.generateState();
		String authUrl = duoClient.createAuthUrl(request.getUserPrincipal().getName(), state);
		System.out.println(authUrl);
		return authUrl;
	}

	public String getDuoCode() {
		return duoCode;
	}

	public void setDuoCode(String duoCode) {
		this.duoCode = duoCode;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}
	
	public Client getDuoClient() {
		return duoClient;
	}

	public void setDuoClient(Client duoClient) {
		this.duoClient = duoClient;
	}

}

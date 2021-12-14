package com.contactmanagementsystem.authentications;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import com.duosecurity.exception.DuoException;

@WebServlet("/Authorize")
public class Authorize extends HttpServlet {
	private static final long serialVersionUID = 1L;
	HashMap<String,String> SUCCUESS = new HashMap<>();
	HashMap<String,String> FAILUIRE = new HashMap<>();

	public Authorize() {
		super();
		SUCCUESS.put("success","true");
		SUCCUESS.put("message","Successful");
		FAILUIRE.put("success","false");
		FAILUIRE.put("message","Something went wrong,check your params.");
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			DuoCheck duoCheck = new DuoCheck();
			if (request.getParameter("state") != null && request.getParameter("duo_code") != null
					&& duoCheck.getDuoCode() == null && duoCheck.getState() == null) {
				duoCheck.setDuoCode(request.getParameter("duo_code"));
				duoCheck.setState(request.getParameter("state"));
				try {
					System.out.println(duoCheck.getDuoCode() + "  huuuu   " + request.getUserPrincipal().getName());
					duoCheck.getDuoClient().exchangeAuthorizationCodeFor2FAResult(duoCheck.getDuoCode(),
							request.getUserPrincipal().getName());
					request.getSession().setAttribute("duoObject", duoCheck);
					int userId = ((UserPrincipal)request.getUserPrincipal()).getUser().getId();
					if( request.getSession().getAttribute("redirect_url") != null)
						response.sendRedirect(((String)request.getSession().getAttribute("redirect_url")));
					else
						response.sendRedirect("/ContactManagementSystem1/ContactManagementSystemEmber/user/" + userId );
					return;
				} catch (Exception e) {
					System.err.println(e.getMessage());
				}
			}
		} catch (DuoException e) {
			e.printStackTrace();
		}
	}
}

package com.contactmanagementsystem.controllers;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.contactmanagementsystem.authentications.UserPrincipal;
import com.contactmanagementsystem.models.User;
import com.contactmanagementsystem.authentications.DuoCheck;

@WebServlet("/login")
public class LoginController extends HttpServlet {
	private static final long serialVersionUID = 1L;
    public LoginController() {
        super();
    }
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		if(response.getStatus() == 403)
		{
			String email = request.getUserPrincipal().getName();
			request.getSession().invalidate();
			User.sendVerification(email);
			request.getSession().setAttribute("alert_message", "Your account has not been verfied.Check the mail for verification.");
			request.getRequestDispatcher("login.jsp").forward(request, response);
			return;
		}

		if( request.getUserPrincipal() != null && request.getSession().getAttribute("duoObject") != null 
			&& ((DuoCheck)request.getSession().getAttribute("duoObject")).getDuoCode() != null
			&& ((DuoCheck)request.getSession().getAttribute("duoObject")).getState() != null )
		{
			if(request.getParameter("redirect_url") != null)
				response.sendRedirect(request.getParameter("redirect_url"));
			else
			{
				int userId = ((UserPrincipal)request.getUserPrincipal()).getUser().getId();
				response.sendRedirect("/ContactManagementSystem1/ContactManagementSystemEmber/user/" + userId );
			}
		}
		else
		{
			if(request.getParameter("redirect_url") != null)
				request.getSession().setAttribute("redirect_url",request.getParameter("redirect_url"));
			response.sendRedirect( DuoCheck.checkAuthorizationForEmber(request) );
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
	
}

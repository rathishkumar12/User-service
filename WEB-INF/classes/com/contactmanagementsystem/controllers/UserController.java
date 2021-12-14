package com.contactmanagementsystem.controllers;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.contactmanagementsystem.authentications.UserPrincipal;
import com.contactmanagementsystem.helpers.MD5;
import com.contactmanagementsystem.helpers.MailSender;
import com.contactmanagementsystem.models.Token;
import com.contactmanagementsystem.models.User;
import com.contactmanagementsystem.validations.ContactException;
import com.contactmanagementsystem.validations.UserException;

@WebServlet("/user/*")
public class UserController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public UserController() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
			if (request.getParameter("action_name") != null
					&& request.getParameter("action_name").equals("forgot_password_reset")) {
				response.sendRedirect("ForgotPassword.jsp");
			} else if (request.getParameter("action_name") != null
					&& request.getParameter("action_name").equals("logout")) {
				request.getSession().invalidate();
				System.out.println("logged out...");
				response.sendRedirect("/User/login");
			} else if (request.getParameter("action_name") != null
					&& request.getParameter("action_name").equals("verify")) {
				User.update(request.getParameter("email"), "is_active", "1");
				request.getSession().setAttribute("alert_message", "Account verification successful !!!");
				response.sendRedirect("/User/login");
			} else {
				try {
					User user = User.searchBy("email", request.getUserPrincipal().getName()).get(0);
					request.setAttribute("user", user);
				} catch (SQLException e) {
					e.printStackTrace();
				}
				request.getRequestDispatcher("ProfileView.jsp").forward(request, response);
			}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
	
			if (request.getParameter("action_name") != null && request.getParameter("action_name").equals("create")) {
				try {
					UserException.isEmailUnique(request.getParameter("email"));
				} catch (UserException e) {
					request.getSession().setAttribute("alert_message", "An User with the same email already exist !!!");
					response.sendRedirect("/User/login");
					return;
				} catch (SQLException e) {
					e.printStackTrace();
				}
				User user = new User(request.getParameter("name"), request.getParameter("email"),
						request.getParameter("phone_number"), MD5.getMd5(request.getParameter("password")));
				user.save();
				User.sendVerification(request.getParameter("email"));
				request.getSession().setAttribute("alert_message",
						"Account created successfully , please check your email for verfication !!!");
				response.sendRedirect("/User/login");
			} else if (request.getParameter("action_name") != null
					&& request.getParameter("action_name").equals("edit")) {
				try {
					if (!request.getUserPrincipal().getName().equals(request.getParameter("email")))
						UserException.isEmailUnique(request.getParameter("email"));
				} catch (UserException e) {
					request.getSession().setAttribute("alert_message", "An User with the same email already exist !!!");
					response.sendRedirect("user");
					return;
				} catch (SQLException e) {
					e.printStackTrace();
				}
				User user = new User(Integer.parseInt(request.getParameter("id")), request.getParameter("name"),
						request.getParameter("email"), request.getParameter("phone_number"));
				user.updateAll();
				request.getSession().setAttribute("alert_message", "Profile edited successfully !!!");
				response.sendRedirect("user");
			} else if (request.getParameter("action_name") != null
					&& request.getParameter("action_name").equals("forgot_password_link")) {
				System.out.println("Sending pass reset mail to :" + request.getParameter("email"));
				int userId = 0;
				try {
					userId = User.searchBy("email", request.getParameter("email")).get(0).getId();
				} catch (SQLException e) {
					e.printStackTrace();
				}
				String UUID = String.valueOf(java.util.UUID.randomUUID());
				Token.create(UUID, userId);
				MailSender.sendMail(request.getParameter("email"), "Password reset link - Contact management system",
						"<h3>Contact Management System</h3></br><a href=http://localhost:8082/User/ForgotPassword.jsp?action_name=forgot_password_reset&email="
								+ request.getParameter("email") + "&session_id=" + UUID
								+ "> Click here to reset your password. </a> Do not share this mail with anyone.<br><b>Note:</b>The link will expire within 30 minutes.");
				request.getSession().setAttribute("alert_message", "Password reset link sent successfully !!!");
				response.sendRedirect("/User/user");
			} else if (request.getParameter("action_name") != null
					&& request.getParameter("action_name").equals("forgot_password_reset")) {
				int userId = 0;
				try {
					userId = User.searchBy("email", request.getParameter("email")).get(0).getId();
				} catch (SQLException e) {
					e.printStackTrace();
				}
				if (Token.validateToken(request.getParameter("session_id"), userId)) {
					Token.delete(request.getParameter("session_id"));
					User.update(request.getParameter("email"), "password",
							MD5.getMd5(request.getParameter("password")));
					request.getSession().setAttribute("alert_message", "Password reset successful !!!");
					response.sendRedirect("/User/login");
				} else {
					response.sendRedirect("/User/PageNotAvailable.jsp");
				}
			}
		}
}

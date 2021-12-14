<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="core" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
   <head>
      <meta charset="ISO-8859-1">
      <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-1BmE4kWBq78iYhFldvKuhfTAU6auU8tT94WrHftjDbrCEXSU1oBoqyl2QvZ6jIW3" crossorigin="anonymous">
      <title>Contact Management System</title>
   </head>
   <body>
      <center>
         <div class="container-fluid" style="margin-top:20px;">
            <div class="card shadow p-3 mb-5 bg-white rounded" style="width:500px;">
               <h5 class="card-title">Change Password</h5>
               <div class="card-body"  >
                  <form method = post action = "user/create?action_name=forgot_password_reset" >
                     <input type ="hidden" name = "email" value=<%= request.getParameter("email") %> >
                     <input type ="hidden" name = "session_id" value=<%= request.getParameter("session_id") %> >
                     <input type ="password" name = "password" placeholder="Enter new password" class="form-control" pattern="^(?=.*[0-9])(?=.*[a-zA-Z]).{6,}$" required/></br>
                     <input type ="submit" value="change" class="btn btn-success"/>
                  </form>
               </div>
            </div>
         </div>
      </center>
   </body>
</html>
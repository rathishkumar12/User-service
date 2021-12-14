<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1" %>
<%@ page import = "com.contactmanagementsystem.models.*" %>
<%@ taglib prefix="core" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
   <head>
      <meta charset="ISO-8859-1">
      <script src="https://code.jquery.com/jquery-3.2.1.slim.min.js" integrity="sha384-KJ3o2DKtIkvYIK3UENzmM7KCkRr/rE9/Qpg6aAZGJwFDMVNA/GpGFF93hXpG5KkN" crossorigin="anonymous"></script>
      <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.12.9/umd/popper.min.js" integrity="sha384-ApNbgh9B+Y1QKtv3Rn7W3mgPxhU9K/ScQsAP7hUibX39j7fakFPskvXusvfa0b4Q" crossorigin="anonymous"></script>
      <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/js/bootstrap.min.js" integrity="sha384-JZR6Spejh4U02d8jOt6vLEHfe/JQGiRRSQQxSfFWpi1MquVdAyjUar5+76PVCmYl" crossorigin="anonymous"></script>
      <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-1BmE4kWBq78iYhFldvKuhfTAU6auU8tT94WrHftjDbrCEXSU1oBoqyl2QvZ6jIW3" crossorigin="anonymous">
      <link href="https://fonts.googleapis.com/icon?family=Material+Icons" rel="stylesheet">
      <% request.setAttribute("header_name", ((User)request.getAttribute("user")).getName() + "'s profile"); %>
      <%@include file = "Header.jsp"  %>
      <title>Contact Management System</title>
   </head>
   <body>
      <core:choose>
         <core:when test="${ not empty alert_message}">
            <div class="alert alert-info alert-dismissible fade show container" role="alert" style="margin-top: 20px;">
               <h6 style="text-align: left;">${alert_message}
                  <span class="material-icons" data-dismiss="alert" aria-label="Close" style="margin-left: 20px;float:right;">close</span>
               </h6>
            </div>
            <% request.getSession().removeAttribute("alert_message"); %>
         </core:when>
      </core:choose>
      <center>
         <div class="container-fluid" style="margin-top:20px;">
            <div class="card shadow p-3 mb-5 bg-white rounded" style="width:500px;" >
               <div class="card-body" style="text-align: left;font-size: 18px;">
                  <b>Name : </b>${user.getName()} </br></br>
                  <b>Email : </b>${user.getEmail()}</br></br>
                  <b>Phone Number : </b>${user.getPhoneNumber()}</br></br>
                  <button class="btn btn-outline-info" data-toggle="modal" data-target="#exampleModal">
                  Edit profile
                  </button>
                  <input type = "button" value = "Change password" class = "btn btn-info text-white" data-toggle="modal" data-target="#exampleModal1">
                  <div class="modal fade" id="exampleModal" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true">
                     <div class="modal-dialog" role="document">
                        <div class="modal-content">
                           <div class="modal-header">
                              <h5 class="modal-title" id="exampleModalLabel">Edit Profile</h5>
                           </div>
                           <div class="modal-body">
                              <form method = post action = "user?action_name=edit" >
                                 <input type="hidden" name="id" value="${user.getId()}">
                                 <label style="font-weight: bold;font-size: 16px;">&nbspName :</label>
                                 <input type="text" value="${user.getName()}" class = "form-control" name = "name" required><br>
                                 <label style="font-weight: bold;font-size: 16px;">&nbspEmail :</label>
                                 <input type="text" value="${user.getEmail()}" class = "form-control" name = "email"  pattern="[a-z0-9._%+-]+@[a-z0-9.-]+\.[a-z]{2,4}$" required><br>
                                 <label style="font-weight: bold;font-size: 16px;">&nbspPhone Number :</label>
                                 <input type="text" value="${user.getPhoneNumber()}" class = "form-control" name = "phone_number" pattern="[0-9]{10}" required ><br>
                                 <div class="modal-footer">
                                    <button type="button" class="btn btn-outline-danger" data-dismiss="modal">Close</button>
                                    <input type="submit" class="btn btn-success" value="Save changes" />
                                 </div>
                              </form>
                           </div>
                        </div>
                     </div>
                  </div>
                  <div class="modal fade" id="exampleModal1" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true">
                     <div class="modal-dialog" role="document">
                        <div class="modal-content">
                           <div class="modal-header">
                              <h5 class="modal-title" id="exampleModalLabel">Forgot password</h5>
                           </div>
                           <div class="modal-body" style="text-align: left;">
                              <form method = post action = "user/create?action_name=forgot_password_link" autocomplete="off">
                                 <label style="font-weight: bold;font-size: 16px;">&nbspEmail :</label>
                                 <input type="hidden" name="email" value="${user.getEmail()}">
                                 <input type="email"  class = "form-control" name = "email" autocomplete="off" placeholder="email" value="${user.getEmail()}" disabled><br>
                                 <div class="modal-footer">
                                    <button type="button" class="btn btn-outline-danger" data-dismiss="modal">Close</button>
                                    <input type="submit" class="btn btn-success" value="Send reset link" />
                                 </div>
                              </form>
                           </div>
                        </div>
                     </div>
                  </div>
               </div>
            </div>
         </div>
      </center>
   </body>
</html>
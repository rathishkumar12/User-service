<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1" %>
<%@ taglib prefix="core" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
   <head>
      <style>
         #logo{
         margin-top: 20px;
         display: inline-flex;
         vertical-align: top;
         }
      </style>
      <title>Contact management system login</title>
      <script src="https://code.jquery.com/jquery-3.2.1.slim.min.js" integrity="sha384-KJ3o2DKtIkvYIK3UENzmM7KCkRr/rE9/Qpg6aAZGJwFDMVNA/GpGFF93hXpG5KkN" crossorigin="anonymous"></script>
      <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.12.9/umd/popper.min.js" integrity="sha384-ApNbgh9B+Y1QKtv3Rn7W3mgPxhU9K/ScQsAP7hUibX39j7fakFPskvXusvfa0b4Q" crossorigin="anonymous"></script>
      <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/js/bootstrap.min.js" integrity="sha384-JZR6Spejh4U02d8jOt6vLEHfe/JQGiRRSQQxSfFWpi1MquVdAyjUar5+76PVCmYl" crossorigin="anonymous"></script>
      <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-1BmE4kWBq78iYhFldvKuhfTAU6auU8tT94WrHftjDbrCEXSU1oBoqyl2QvZ6jIW3" crossorigin="anonymous">
      <link href="https://fonts.googleapis.com/icon?family=Material+Icons" rel="stylesheet">
   </head>
   <body >
      <center>
         <i class="material-icons" style="font-size: 30px;" id="logo">contacts</i><br>
         <span class = "fw-bold"style="line-height: 30px;font-size: 25px;">Contact Management System</span>
         <div class="container-fluid" style="margin-top:20px;">
            <div class="card shadow p-3 mb-5 bg-white rounded" style="width:500px;">
               <h5 class="card-title"></h5>
               <div class="card-body"  >
                  <form method=post action="j_security_check" >
                     <p style="text-align:left;" >
                        <label class="form-label">Email :</label>
                        <br/>
                        <input type="text"  name= "j_username" type="email" class="form-control">
                     </p>
                     <p style="text-align:left;">
                        <label class="form-label">Password :</label>
                        <br />
                        <input type="password"  name= "j_password" class="form-control">
                        <a href="#" style="text-decoration: none;" data-toggle="modal" data-target="#exampleModal" >Dont have an account ?</a><br>
                        <a href="#" style="text-decoration: none;" data-toggle="modal" data-target="#exampleModal1">Forgot password ?</a>
                     </p>
                     <p>
                        <input type="submit" value="Login" class="btn btn-primary" >
                     </p>
                  </form>
                  <div class="modal fade" id="exampleModal" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true">
                     <div class="modal-dialog" role="document">
                        <div class="modal-content">
                           <div class="modal-header">
                              <h5 class="modal-title" id="exampleModalLabel">Create account</h5>
                           </div>
                           <div class="modal-body" style="text-align: left;">
                              <form method = post action = "user/create?action_name=create" autocomplete="off">
                                 <label style="font-weight: bold;font-size: 16px;" >&nbspName :</label>
                                 <input type="text"  class = "form-control" name = "name" required><br>
                                 <label style="font-weight: bold;font-size: 16px;" >&nbspEmail :</label>
                                 <input type="text"  class = "form-control" name = "email"  autocomplete="off" pattern="[a-z0-9._%+-]+@[a-z0-9.-]+\.[a-z]{2,4}$" required><br>
                                 <label style="font-weight: bold;font-size: 16px;">&nbspPhone Number :</label>
                                 <input type="text"  class = "form-control" name = "phone_number" pattern="[0-9]{10}" required><br>
                                 <label style="font-weight: bold;font-size: 16px;">&nbspPassword :</label>
                                 <input type="password"  class = "form-control" name = "password" autocomplete="off" pattern="^(?=.*[0-9])(?=.*[a-zA-Z]).{6,}$" required><br>
                                 <div class="modal-footer">
                                    <button type="button" class="btn btn-outline-danger" data-dismiss="modal">Close</button>
                                    <input type="submit" class="btn btn-success" value="Create" />
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
                                 <input type="email"  class = "form-control" name = "email" autocomplete="off" placeholder="email"  pattern="[a-z0-9._%+-]+@[a-z0-9.-]+\.[a-z]{2,4}$" required><br>
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
   </body>
</html>
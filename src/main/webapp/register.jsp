<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Registration</title>
  <link rel="stylesheet" href="/style.css">
</head>
<body>
<form name="registForm" action="register" method="post">
<p>Please fill out the form:</p>
<p><b>User Name:<input type="text" name="newName" /></b></p>
<p><b>Email ID:<input type="text" name="email" /></b></p>
<p><b>Password:<input type="text" name="newPass" /></b></p>
<input type="submit">
</form>
</body>
</html>
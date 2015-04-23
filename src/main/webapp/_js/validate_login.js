function validate()
{
	var username = $("#username").val();
	var password = $("#password").val();
	
	var arr = "username="+ username +"&password="+ password;
	$.ajax({
		url: 'rest/users/login',
		type: 'POST',
		data: arr,
                contentType: "application/x-www-form-urlencoded",
		async: true,
		success: function(user) {
                    window.location = "index.jsp";
		}
	});
}	
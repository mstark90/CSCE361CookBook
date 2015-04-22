function validate(form)
{
	var username = form.username.value;
	var password = form.password.value;
	
	var arr = {user_name: username, password: password};
	$.ajax({
		url: 'localhost:8080/CookBook/rest/users/login',
		type: 'POST',
		data: JSON.stringify(arr),
		contentType: 'application/json; charset=utf-8',
		dataType: 'json',
		async: false,
		success: function(msg) {
			alert(msg);
		}
	});
}	
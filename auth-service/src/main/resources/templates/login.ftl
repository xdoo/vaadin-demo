<html>
<head>
<link rel="stylesheet" href="css/wro.css"/>
<script>
concat = function() {
  var user = document.getElementById('user').value;
  var mandant = document.getElementById('mandant').value;
  var username = mandant == 'Mandant' ? user :  mandant + '_' + user;
  document.getElementById('username').value = username;
  console.log(username);
};
</script>
</head>
<body>
<#if RequestParameters['error']??>
	<div class="alert alert-danger">
		There was a problem logging in. Please try again.
	</div>
</#if>
	<div class="container">
		<div id="login-form">
			<h3>Login</h3>
			<fieldset>
				<form onsubmit="return concat()" role="form" action="login" method="post">
				  <input type="text" id="user" name="user" required value="Username" onBlur="if(this.value=='')this.value='Username'" onFocus="if(this.value=='Username')this.value='' "> <!-- JS because of IE support; better: placeholder="test" -->
				  <!--  -->
				  <input type="text" id="mandant" name="mandant" required value="Mandant" onBlur="if(this.value=='')this.value='Mandant'" onFocus="if(this.value=='Mandant')this.value='' "> <!-- JS because of IE support; better: placeholder="Email" -->
				  <input type="password" id="password" name="password" required value="Password" onBlur="if(this.value=='')this.value='Password'" onFocus="if(this.value=='Password')this.value='' "> <!-- JS because of IE support; better: placeholder="Password" -->
				  <input type="hidden" id="username" name="username" value=""/>
				  <input type="hidden" id="csrf_token" name="${_csrf.parameterName}" value="${_csrf.token}"/>
				  <input type="submit" value="Login">
				</form>
			</fieldset>
		</div>
	</div>
	<script src="js/wro.js" type="text/javascript"></script>
</body>
</html>

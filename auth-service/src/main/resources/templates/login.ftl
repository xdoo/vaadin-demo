<html>
<head>
<link rel="stylesheet" href="css/wro.css"/>
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
				<form role="form" action="login" method="post">
				  <input type="text" id="username" name="username" required value="Username" onBlur="if(this.value=='')this.value='Username'" onFocus="if(this.value=='Username')this.value='' "> <!-- JS because of IE support; better: placeholder="Email" -->
				  <input type="password" id="password" name="password" required value="Password" onBlur="if(this.value=='')this.value='Password'" onFocus="if(this.value=='Password')this.value='' "> <!-- JS because of IE support; better: placeholder="Password" -->
				  <input type="hidden" id="csrf_token" name="${_csrf.parameterName}" value="${_csrf.token}"/>
				  <input type="submit" value="Login">
				</form>
			</fieldset>
		</div>
	</div>
	<script src="js/wro.js" type="text/javascript"></script>
</body>
</html>

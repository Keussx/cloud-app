<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
<script src="app.js"></script>
<script src="https://apis.google.com/js/client.js?onload=init"></script>
</head>
<body style="padding: 0; margin: 0">
	<jsp:include page="header.html" />


	<div style="min-height: 70%">

		<div class="container" style="padding: 2px">
		
			<div id="ingredients-selection">
							
			</div>


			<div id="process" style="width: 50%; margin: 40px 25%; float: right; padding: 20px; border: 1px solid #888; text-align: center">
				<form method="post">
					<div class="form-group">
						<input type="button" id="getRecipes" value="loading" onclick="notready" style="background: #15a; color: #fff; padding: 6px 40px; font-size: 15px" />
					</div>
				</form>
			</div>

		</div>
	</div>

	</div>
	<jsp:include page="footer.jsp" />
</body>
</html>
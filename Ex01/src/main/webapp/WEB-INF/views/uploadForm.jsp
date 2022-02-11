<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<style>
	iframe {
		width:0px;
		heught:0px;
		border:0px;
	}
</style>
</head>
<body>

	<form id="form1" action="uploadForm" method="post"  enctype="multipart/form-data" target="zeroFrame">
		<input type="file"  id="input_file" name="input_file"  multiple="multiple">
		<input type="submit" value="전송" />
	</form>
	<hr>
	<iframe name="zeroFrame"></iframe>
	
	<script>
		function addFilePath(msg) {
			alert(msg);
			document.getElementById("form1").reset();
		}
	</script>
</body>
</html>
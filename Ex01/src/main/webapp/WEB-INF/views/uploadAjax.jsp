<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<script src="https://code.jquery.com/jquery-3.6.0.js"></script>
</head>
<body>
	<form id="form1" enctype="multipart/form-data" >
		<input type="file"  id="input_file" name="file"  multiple="multiple">
		<input type="button" id="save" value="전송" />
	</form>
	
	<script>
		$(document).ready(function(){
			$("#save").click(function(event){
				var formData = new FormData($('#form1')[0]);
				alert(formData);
				//var formData = new FormData();
				//alert(formData);
				$.ajax({
					url : '/multipartUpload.do',
					enctype: 'multipart/form-data', // 필수
					data : formData,
					processData: false,
					contentType: false,
					type : 'post',
					success : function(data) {
						alert(data);
					}
				});
			});
		});
	</script>
</body>
</html>
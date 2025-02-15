<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<script src="https://code.jquery.com/jquery-3.6.0.js"
	integrity="sha256-H+K7U5CnXl1h5ywQfKtSj8PCmoN9aaq30gDh27Xc0jk="
	crossorigin="anonymous"></script>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>답장 페이지</title>
</head>
<body>
	<form id="noteForm" name="noteForm">
		<div class="container">
			<div>
				<label>보내는 사람</label> <input type="text" id="from_id" name="from_id"
					value="${map.from_id}" readonly>
			</div>
			<div>
				<label>받는 사람</label> <input type="text" id="to_id" name="to_id"
					value="${map.to_id}" readonly>
			</div>
			<div>
				<input type="text" id="content" name="content"
					placeholder="내용을 입력하세요.">
			</div>
			<div>
				<button type="button" id="submitBtn">답장</button>
			</div>
		</div>
	</form>


	<script>
	document.getElementById("submitBtn").onclick = function() {
		let noteSubmit = $("#noteForm").serialize();
		$.ajax({
			url : "${pageContext.request.contextPath}/note/note.do"
			,method : "post"
			,data : noteSubmit
		}).done(function(data){
			if(data == "success"){
				alert("쪽지가 전송되었습니다.");
				window.close();	
			}else{
				alert("쪽지 전송에 실패하였습니다.");
				window.close();	
			}
		}).fail(function(e){
			console.log(e);
		});
	}
	</script>
</body>
</html>
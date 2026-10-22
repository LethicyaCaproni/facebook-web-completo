<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>Post - Facebook CRUD</title>
	<!-- Bootstrap CSS -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/bootstrap.min.css"/>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/bootstrap-icons.css">
</head>
<body>
	<div class="container">
		<div class="row mt-3">
			<div class="col-md-2"></div>
			<div class="col-md-8">
				<h2>Post</h2>
				<form action="${pageContext.request.contextPath}/posts/save" method="get">
					<input type="hidden" name="post_id" value="${post.id}">

					<div class="mb-3">
						<label for="post_content" class="form-label">Conteúdo</label>
						<textarea class="form-control" id="post_content" 
						          name="post_content" rows="4">${post.content}</textarea>
					</div>
					<div class="mb-3">
						<label for="user_id" class="form-label">Usuário</label>
						<select class="form-select" id="user_id" name="user_id">
							<c:forEach var="u" items="${users_list}">
								<option value="${u.id}" ${post.user.id == u.id ? 'selected' : ''}>${u.name}</option>
							</c:forEach>
						</select>
					</div>

					<button type="submit" class="btn btn-primary">Salvar</button>
					<a href="${pageContext.request.contextPath}/posts" class="btn btn-secondary">Cancelar</a>
				</form>
			</div>
			<div class="col-md-2"></div>
		</div>
	</div>
</body>
</html>

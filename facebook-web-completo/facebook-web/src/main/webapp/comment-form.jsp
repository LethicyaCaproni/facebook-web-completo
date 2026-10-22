<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>Comentário - Facebook CRUD</title>
	<!-- Bootstrap CSS -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/bootstrap.min.css"/>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/bootstrap-icons.css">
</head>
<body>
	<div class="container">
		<div class="row mt-3">
			<div class="col-md-2"></div>
			<div class="col-md-8">
				<h2>Comentário</h2>
				<form action="${pageContext.request.contextPath}/comments/save" method="get">
					<input type="hidden" name="comment_id" value="${comment.id}">

					<div class="mb-3">
						<label for="comment_content" class="form-label">Conteúdo</label>
						<textarea class="form-control" id="comment_content" 
						          name="comment_content" rows="4">${comment.content}</textarea>
					</div>
					<div class="mb-3">
						<label for="post_id" class="form-label">Post</label>
						<select class="form-select" id="post_id" name="post_id">
							<c:forEach var="p" items="${posts_list}">
								<option value="${p.id}" ${comment.post.id == p.id ? 'selected' : ''}>${p.id} - ${p.content}</option>
							</c:forEach>
						</select>
					</div>

					<div class="mb-3">
						<label for="user_id" class="form-label">Usuário</label>
						<select class="form-select" id="user_id" name="user_id">
							<c:forEach var="u" items="${users_list}">
								<option value="${u.id}" ${comment.user.id == u.id ? 'selected' : ''}>${u.name}</option>
							</c:forEach>
						</select>
					</div>

					<button type="submit" class="btn btn-primary">Salvar</button>
					<a href="${pageContext.request.contextPath}/comments" class="btn btn-secondary">Cancelar</a>
				</form>
			</div>
			<div class="col-md-2"></div>
		</div>
	</div>
</body>
</html>

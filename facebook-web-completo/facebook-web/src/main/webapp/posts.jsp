<jsp:directive.page contentType="text/html; charset=UTF-8" />
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>Posts - Facebook CRUD</title>
	<!-- Bootstrap CSS -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/bootstrap.min.css"/>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/bootstrap-icons.css">
</head>
<body>

	<div class="container">
		<div class="row mt-3">
			<div class="col-md-2"></div>
			<div class="col-md-8">
				<a class="btn btn-secondary" 
				   href="${pageContext.request.contextPath}/">
					Home
				</a>
			</div>
			<div class="col-md-2"></div>
		</div>

		<div class="row">
			<h2>Posts</h2>
			<table class="table table-striped">
				<thead>
					<tr>
						<th scope="col">Id</th>
						<th scope="col">Conteúdo</th>
						<th scope="col">Data</th>
						<th scope="col">Usuário</th>
						<th scope="col">Ações</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach var="post" items="${posts_list}">
						<tr>
							<th scope="row">${post.getId()}</th>
							<td>${post.getContent()}</td>
							<td>${post.getPostDate()}</td>
							<td>${post.getUser().getName()}</td>

<td>
    <a href="${pageContext.request.contextPath}/posts/update?postId=${post.getId()}"
       class="bi bi-pencil-square" title="Editar"></a>

    <a href="${pageContext.request.contextPath}/posts/delete?postId=${post.getId()}"
       class="bi bi-trash" title="Excluir"
       onclick="return confirm('Deseja excluir este post?');"></a>
</td>
</tr>
					</c:forEach>
				</tbody>
				</table>
			
<div class="mt-3 mb-4">
    <a href="${pageContext.request.contextPath}/posts/create"
       class="btn btn-primary">
        <i class="bi bi-plus-circle"></i> Adicionar post
    </a>
</div>

		</div>
	</div>

	<script src="${pageContext.request.contextPath}/js/bootstrap.bundle.min.js"></script>
	<script src="${pageContext.request.contextPath}/js/bootstrap.min.js"></script>
</body>
</html>

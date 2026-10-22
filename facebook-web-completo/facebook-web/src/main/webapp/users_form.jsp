<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>Usuário - Facebook CRUD</title>
	<!-- Bootstrap CSS -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/bootstrap.min.css"/>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/bootstrap-icons.css">
</head>
<body>
	<div class="container">
		<div class="row mt-3">
			<div class="col-md-2"></div>
			<div class="col-md-8">
				<h2>Usuário</h2>
				<form action="${pageContext.request.contextPath}/users/save" method="get">
					<input type="hidden" name="user_id" value="${user.id}">

					<div class="mb-3">
						<label for="user_name" class="form-label">Nome</label>
						<input type="text" class="form-control" id="user_name"
						       name="user_name" value="${user.name}">
					</div>
					<div class="mb-3">
						<label for="user_gender" class="form-label">Sexo</label>
						<select class="form-select" id="user_gender" name="user_gender">
							<option value="M" ${user.gender == 'M' ? 'selected' : ''}>Masculino</option>
							<option value="F" ${user.gender == 'F' ? 'selected' : ''}>Feminino</option>
						</select>
					</div>
					<div class="mb-3">
						<label for="user_email" class="form-label">Email</label>
						<input type="text" class="form-control" id="user_email"
						       name="user_email" value="${user.email}">
					</div>
					<div class="mb-3">
						<label for="user_pw" class="form-label">Senha</label>
						<input type="password" class="form-control" id="user_pw"
						       name="user_pw">
					</div>

					<button type="submit" class="btn btn-primary">Salvar</button>
					<a href="${pageContext.request.contextPath}/users" class="btn btn-secondary">Cancelar</a>
				</form>
			</div>
			<div class="col-md-2"></div>
		</div>
	</div>
</body>
</html>

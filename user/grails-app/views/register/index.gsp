<head>
	<meta name="layout" content="main" />
	<title><g:message code="spring.security.ui.register.title" /></title>
</head>
<body>
	<g:if test='${emailSent}'>
		<g:message code='spring.security.ui.register.sent'/>
	</g:if>
	<g:else>
	<ul>
	<g:each in="${command.properties}" var="prop">
	<li>${prop}</li>
	</g:each>
	</ul>
		<div id="loginWrapper" class="container">
			<div class="panel panel-primary">
				<div class="panel-heading">
					<h1 class="panel-title"><b><g:message code="spring.security.ui.register.description" /></b></h1>
				</div>
				<g:hasErrors bean="${command}">
					<ul class="errors alert alert-danger" role="alert">
						<g:eachError bean="${command}" var="error">
						<li <g:if test="${error in org.springframework.validation.FieldError}">data-field-id="${error.field}"</g:if>><g:message error="${error}"/></li>
						</g:eachError>
					</ul>
				</g:hasErrors>
				<g:if test="${flash.error}">
					<div class="alert alert-danger">
						${flash.error}
					</div>
				</g:if>
				<form action="register" method="POST" role="form" autocomplete="off">
					<div class="panel-body">
						<div class="form-group">
							<label for="username" class="control-label"><g:message code="de.httc.plugin.user.loginName" /></label>
							<input type="text" name="username" value="${command.username}" class="form-control" id="username" placeholder="<g:message code="de.httc.plugin.user.loginName"/>" autocapitalize="off" autofocus required>
						</div>
						<div class="form-group">
							<label for="firstName" class="control-label"><g:message code="de.httc.plugin.user.firstName" /></label>
							<input type="text" name="profile.firstName" value="${command.profile?.firstName}" class="form-control" id="firstName" placeholder="<g:message code="de.httc.plugin.user.firstName"/>" required>
						</div>
						<div class="form-group">
							<label for="lastName" class="control-label"><g:message code="de.httc.plugin.user.lastName" /></label>
							<input type="text" name="profile.lastName" value="${command.profile?.lastName}" class="form-control" id="lastName" placeholder="<g:message code="de.httc.plugin.user.lastName"/>" required>
						</div>
						<div class="form-group">
							<label for="email" class="control-label"><g:message code="de.httc.plugin.user.email" /></label>
							<input type="email" name="email" value="${command.email}" class="form-control" id="username" placeholder="<g:message code="de.httc.plugin.user.email"/>" required>
						</div>
						<div class="form-group">
							<label for="password" class="control-label"><g:message code="de.httc.plugin.user.password" /></label>
							<input name="password" value="${command.password}" type="password" class="form-control" id="password" placeholder="<g:message code="de.httc.plugin.user.password"/>" required>
						</div>
						<div class="form-group">
							<label for="password2" class="control-label"><g:message code="de.httc.plugin.user.password2" /></label>
							<input name="password2" value="${command.password2}" type="password" class="form-control" id="password" placeholder="<g:message code="de.httc.plugin.user.password2"/>" required>
						</div>
					</div>
					<div class="panel-footer text-center">
						<button type="submit" class="btn btn-default"><g:message code="spring.security.ui.register.submit" /></button>
					</div>
				</form>
			</div>
		</div>
	</g:else>
</body>

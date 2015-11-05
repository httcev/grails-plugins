<html>
<head>
	<title><g:message code="de.httc.plugin.user.resetPassword"/></title>
	<meta name="layout" content="main"/>
</head>
<body>
	<div id="loginWrapper" class="container">
		<div class="panel panel-primary">
			<div class="panel-heading">
				<h1 class="panel-title"><b><g:message code="de.httc.plugin.user.resetPassword" /></b></h1>
			</div>
			<g:form url="[controller:'register', action:'resetPassword']" method="POST" role="form" autocomplete="off">
				<g:hiddenField name='t' value='${token}'/>
				<div class="panel-body">
					<g:if test="${flash.message}">
						<div class="alert alert-success">
							${flash.message}
						</div>
					</g:if>
					<g:if test="${flash.error}">
						<div class="alert alert-danger">
							${flash.error}
						</div>
					</g:if>
					<g:hasErrors bean="${command}">
						<ul class="errors alert alert-danger" role="alert">
							<g:eachError bean="${command}" var="error">
							<li <g:if test="${error in org.springframework.validation.FieldError}">data-field-id="${error.field}"</g:if>><g:message error="${error}"/></li>
							</g:eachError>
						</ul>
					</g:hasErrors>
					<p><g:message code="de.httc.plugin.user.resetPassword.prompt"/></p>
					<div class="form-group">
						<label for="password" class="control-label"><g:message code="de.httc.plugin.user.updatePassword.new" />:</label>
						<input type="password" name="password" class="form-control" id="password" placeholder="<g:message code="de.httc.plugin.user.password"/>..." autofocus required>
					</div>
					<div class="form-group">
						<label for="password2" class="control-label"><g:message code="de.httc.plugin.user.updatePassword.new2" />:</label>
						<input type="password" name="password2" class="form-control" id="password2" placeholder="<g:message code="de.httc.plugin.user.password"/>..." required>
					</div>
				</div>
				<div class="panel-footer text-center">
					<button type="submit" class="btn btn-default"><g:message code="de.httc.plugin.user.updatePassword" /></button>
				</div>
			</g:form>
		</div>
	</div>
</body>
</html>

<g:applyLayout name="admin">
<html>
	<head>
		<g:set var="entityName" value="${message(code: 'de.httc.plugin.repository.asset')}" />
		<g:set var="entitiesName" value="${message(code: 'de.httc.plugin.repository.assets')}" />
		<title><g:message code="default.create.label" args="[entityName]" /></title>
	</head>
	<body>
		<ol class="breadcrumb">
			<li><g:link uri="/"><g:message code="app.home" default="Administration" /></g:link></li>
			<li><g:link action="index">${entitiesName}</g:link></li>
			<li class="active"><g:message code="default.create.label" args="[entityName]" /></li>
		</ol>
		<h1 class="page-header"><g:message code="default.create.label" args="[entityName]" /></h1>
		<g:if test="${flash.message}">
			<div class="message alert alert-success" role="status">${flash.message}</div>
		</g:if>
		<g:if test="${flash.error}">
			<div class="message alert alert-danger" role="status">${flash.error}</div>
		</g:if>
		<g:hasErrors bean="${cmd}">
			<ul class="errors alert alert-danger" role="alert">
			<g:eachError bean="${cmd}" var="error">
				<li <g:if test="${error in org.springframework.validation.FieldError}">data-field-id="${error.field}"</g:if>><g:message error="${error}"/></li>
			</g:eachError>
			</ul>
		</g:hasErrors>
		<g:form url="[controller:'asset', action:'create']" class="form-horizontal" autocomplete="off" enctype="multipart/form-data">
			<g:layoutBody />
		</g:form>
	</body>
</html>
</g:applyLayout>

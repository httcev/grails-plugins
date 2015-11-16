<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="admin">
		<g:set var="entityName" value="${message(code: 'competence.label', default: 'Competence')}" />
		<title><g:message code="default.create.label" args="[entityName]" /></title>
	</head>
	<body>
		<ol class="breadcrumb">
			<li><g:link uri="/admin"><g:message code="default.admin.label" default="Administration" /></g:link></li>
			<li><g:link class="list" action="list" namespace="admin"><g:message code="default.list.label" args="[entityName]" /></g:link></li>
			<li class="active"><g:message code="default.create.label" args="[entityName]" /></li>
		</ol>
		<h1 class="page-header"><g:message code="default.create.label" args="[entityName]" /></h1>
		<g:hasErrors bean="${competenceInstance}">
			<ul class="errors alert alert-danger" role="alert">
				<g:eachError bean="${competenceInstance}" var="error">
				<li <g:if test="${error in org.springframework.validation.FieldError}">data-field-id="${error.field}"</g:if>><g:message error="${error}"/></li>
				</g:eachError>
			</ul>
		</g:hasErrors>
		<form action="${createLink(action:'save', namespace:'admin')}" method="post" role="form" class="form-horizontal" enctype="multipart/form-data">
			<g:render template="form" />
			<div class="buttons pull-right">
				<button class="save btn btn-success"><i class="fa fa-cloud-upload"></i> <g:message code="default.button.create.label" default="Create" /></button>
			</div>
		<form>
	</body>
</html>

<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="admin">
		<g:set var="entityName" value="${message(code: 'competence.label', default: 'Competence')}" />
		<title><g:message code="default.edit.label" args="[entityName]" /></title>
	</head>
	<body>
		<ol class="breadcrumb">
			<li><g:link uri="/admin"><g:message code="default.admin.label" default="Administration" /></g:link></li>
			<li><g:link class="list" action="list" namespace="admin"><g:message code="default.list.label" args="[entityName]" /></g:link></li>
			<li class="active"><g:message code="default.edit.label" args="[entityName]" /></li>
		</ol>
		<h1 class="page-header">
			<g:message code="default.edit.label" args="[entityName]" />
			<g:link class="create btn btn-default btn-sm pull-right" action="create" namespace="admin" title="${message(code: 'default.new.label', args:[entityName])}">
				  <i class="fa fa-plus"></i>
			</g:link>
		</h1>
		<g:hasErrors bean="${competence}">
			<ul class="errors alert alert-danger" role="alert">
				<g:eachError bean="${competence}" var="error">
				<li <g:if test="${error in org.springframework.validation.FieldError}">data-field-id="${error.field}"</g:if>><g:message error="${error}"/></li>
				</g:eachError>
			</ul>
		</g:hasErrors>
		<form action="${createLink(action:'update', id:competence?.id, namespace:'admin')}" method="post" role="form" class="form-horizontal">
			<g:hiddenField name="version" value="${competence?.version}" />
			<input type="hidden" name="_method" value="PUT" id="_method">
			<g:render template="form"/>
			<div class="buttons pull-right">
				<button class="save btn btn-success"><i class="fa fa-cloud-upload"></i> <g:message code="default.button.update.label" default="Update" /></button>
			</div>
		</form>
	</body>
</html>

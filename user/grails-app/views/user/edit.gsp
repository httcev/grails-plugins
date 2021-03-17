<html>
	<head>
		<meta name="layout" content="admin">
		<g:set var="entityName" value="${message(code: 'de.httc.plugin.user.user')}" />
		<g:set var="entitiesName" value="${message(code: 'de.httc.plugin.user.users')}" />
		<title><g:message code="default.edit.label" args="[entityName]" /></title>
		<asset:stylesheet src="user.css" />
	</head>
	<body>
		<ol class="breadcrumb">
			<li><g:message code="default.admin.label" default="Administration" /></li>
			<li><g:link class="index" action="index">${entitiesName}</g:link></li>
			<li class="active"><g:message code="default.edit.label" args="[entityName]" /></li>
		</ol>
		<g:form url="[resource:user, action:'update']" method="PUT" class="form-horizontal" enctype="multipart/form-data" autocomplete="off">
			<h1 class="page-header clearfix">
				<g:message code="default.edit.label" args="[entityName]" />
				<div class="buttons pull-right">
					<g:link class="delete btn btn-danger" action="delete" id="${user.id}" title="${message(code: 'default.button.delete.label', args:[entityName])}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', args: [entityName])}');">
						<i class="fa fa-user-times"></i>
					</g:link>
					<button class="save btn btn-success"><i class="fa fa-save"></i> <g:message code="default.button.update.label" default="Update" /></button>
				</div>
			</h1>
			<g:hasErrors bean="${user}">
				<ul class="errors alert alert-danger" role="alert">
					<g:eachError bean="${user}" var="error">
					<li <g:if test="${error in org.springframework.validation.FieldError}">data-field-id="${error.field}"</g:if>><g:message error="${error}"/></li>
					</g:eachError>
				</ul>
			</g:hasErrors>
			<g:hasErrors bean="${user.profile}">
				<ul class="errors alert alert-danger" role="alert">
					<g:eachError bean="${user.profile}" var="error">
					<li <g:if test="${error in org.springframework.validation.FieldError}">data-field-id="${error.field}"</g:if>><g:message error="${error}"/></li>
					</g:eachError>
				</ul>
			</g:hasErrors>
			<g:hiddenField name="version" value="${user?.version}" />
			<g:render template="form"/>
			<g:render template="account"/>
			<g:render template="profile"/>
			<div class="buttons pull-right">
				<button class="save btn btn-success"><i class="fa fa-save"></i> <g:message code="default.button.update.label" default="Update" /></button>
			</div>
		</g:form>
	</body>
</html>

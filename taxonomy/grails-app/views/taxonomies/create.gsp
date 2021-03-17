<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="admin">
		<g:set var="entityName" value="${message(code: 'de.httc.plugin.taxonomy.taxonomy', default: 'Taxonomy')}" />
		<g:set var="entitiesName" value="${message(code: 'de.httc.plugin.taxonomy.taxonomies', default: 'Taxonomies')}" />
		<title><g:message code="default.create.label" args="[entityName]" /></title>
	</head>
	<body>
		<ol class="breadcrumb">
			<li><g:message code="default.admin.label" default="Administration" /></li>
			<li><g:link class="list" action="index" >${entitiesName}</g:link></li>
			<li class="active"><g:message code="default.create.label" args="[entityName]" /></li>
		</ol>
		<h1 class="page-header"><g:message code="default.create.label" args="[entityName]" /></h1>
		<g:hasErrors bean="${taxonomy}">
			<ul class="errors alert alert-danger" role="alert">
				<g:eachError bean="${taxonomy}" var="error">
				<li <g:if test="${error in org.springframework.validation.FieldError}">data-field-id="${error.field}"</g:if>><g:message error="${error}"/></li>
				</g:eachError>
			</ul>
		</g:hasErrors>
		<form action="${createLink(action:'save', plugin:'httcTaxonomy')}" method="post" role="form" class="form-horizontal" enctype="multipart/form-data">
			<g:render template="form" />
			<div class="form-group">
				<label for="file" class="col-sm-2 control-label"><g:message code="de.httc.plugin.taxonomy.import" default="Import" /></label>
				<div class="col-sm-10"><input type="file" id="file" name="file" class="form-control"/></div>
			</div>
			<div class="buttons pull-right">
				<button class="save btn btn-success"><i class="fa fa-cloud-upload"></i> <g:message code="default.button.create.label" default="Create" /></button>
			</div>
		</form>
	</body>
</html>

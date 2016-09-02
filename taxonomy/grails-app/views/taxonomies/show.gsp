
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="admin">
		<g:set var="entityName" value="${message(code: 'de.httc.plugin.taxonomy.taxonomy', default: 'Taxonomy')}" />
		<g:set var="entitiesName" value="${message(code: 'de.httc.plugin.taxonomy.taxonomies', default: 'Taxonomies')}" />
		<title><g:message code="default.show.label" args="[entityName]" /></title>
	</head>
	<body>
		<ol class="breadcrumb">
			<li><g:link uri="/admin"><g:message code="default.admin.label" default="Administration" /></g:link></li>
			<li><g:link class="list" action="index" namespace="admin">${entitiesName}</g:link></li>
			<li class="active"><g:message code="default.show.label" args="[entityName]" /></li>
		</ol>
		<form action="${createLink(action:'delete', id:taxonomy?.id, namespace:'admin')}" method="post" class="taxonomy-show">
			<h1 class="page-header clearfix">
				<g:message code="default.show.label" args="[entityName]" />
				<div class="buttons pull-right">
					<g:link class="edit btn btn-default" action="edit" id="${taxonomy?.id}" namespace="admin" plugin="httcTaxonomy"><i class="fa fa-edit fa-lg"></i> <g:message code="default.button.edit.label" default="Edit" /></g:link>
					<button class="delete btn btn-danger" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', args: [entityName])}');"><i class="fa fa-trash-o fa-lg"></i> <g:message code="default.button.delete.label" default="Delete" /></button>
				</div>
			</h1>
			<g:if test="${flash.message}">
				<div class="message alert alert-success" role="status">${flash.message}</div>
			</g:if>
			<div id="taxonomy">
				<g:if test="${flash.error}">
					<div class="message alert alert-danger" role="status">${flash.error}</div>
				</g:if>
				<table class="table table-condensed table-bordered">
					<tbody>
						<tr><td><g:message code="de.httc.plugin.taxonomy.label" default="Name" />:</td><td><b><g:fieldValue bean="${taxonomy}" field="label"/></b></td></tr>
						<tr><td><g:message code="de.httc.plugin.taxonomy.lastUpdated" default="Last updated" />:</td><td><g:fieldValue bean="${taxonomy}" field="lastUpdated"/></td></tr>
						<tr><td><g:message code="de.httc.plugin.taxonomy.type" default="Type" />:</td><td><g:fieldValue bean="${taxonomy}" field="type"/></td></tr>
					</tbody>
				</table>
				<g:render template="terms" model="${[terms:taxonomy?.terms]}" />
			</div>
		</form>
	</body>
</html>

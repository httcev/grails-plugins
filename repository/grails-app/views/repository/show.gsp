
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="admin">
		<g:set var="entityName" value="${message(code: 'taxonomy.label', default: 'Taxonomy')}" />
		<title><g:message code="default.show.label" args="[entityName]" /></title>
	</head>
	<body>
		<ol class="breadcrumb">
			<li><g:link uri="/admin"><g:message code="default.admin.label" default="Administration" /></g:link></li>
			<li><g:link class="list" action="list" namespace="admin"><g:message code="default.list.label" args="[entityName]" /></g:link></li>
			<li class="active"><g:message code="default.show.label" args="[entityName]" /></li>
		</ol>
		<form action="${createLink(action:'delete', id:taxonomyInstance?.id, namespace:'admin')}" method="post" class="taxonomy-show">
			<h1 class="page-header">
				<g:message code="default.show.label" args="[entityName]" />
				<div class="buttons pull-right">
					<g:link class="edit btn btn-default" action="edit" id="${taxonomyInstance?.id}" namespace="admin"><i class="fa fa-edit fa-lg"></i> <g:message code="default.button.edit.label" default="Edit" /></g:link>
					<button class="delete btn btn-danger" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');"><i class="fa fa-trash-o fa-lg"></i> <g:message code="default.button.delete.label" default="Delete" /></button>
				</div>
			</h1>
			<div id="taxonomy">
				<table class="table table-condensed table-bordered">
					<tbody>
						<tr><td><g:message code="taxonomy.label.label" default="Name" />:</td><td><b><g:fieldValue bean="${taxonomyInstance}" field="label"/></b></td></tr>
						<tr><td><g:message code="default.lastModified.label" default="Last modified" />:</td><td><g:fieldValue bean="${taxonomyInstance}" field="lastUpdated"/></td></tr>
						<tr><td><g:message code="taxonomy.knowledgeDomain.label" default="Knowledge Domain" />:</td><td><g:fieldValue bean="${taxonomyInstance}" field="knowledgeDomain"/></td></tr>
					</tbody>
				</table>
				<g:render template="terms" model="${[terms:taxonomyInstance?.terms]}" />
			</div>
		</form>
	</body>
</html>

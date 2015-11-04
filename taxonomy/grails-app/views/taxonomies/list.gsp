
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="admin">
		<g:set var="entityName" value="${message(code: 'taxonomy.label', default: 'Taxonomy')}" />
		<title><g:message code="default.list.label" args="[entityName]" /></title>
	</head>
	<body>
		<ol class="breadcrumb">
			<li><g:link uri="/admin"><g:message code="default.admin.label" default="Administration" /></g:link></li>
			<li class="active"><g:message code="default.list.label" args="[entityName]" /></li>
		</ol>
		<h1 class="page-header">
			<g:message code="default.list.label" args="[entityName]" />
			<g:link class="create btn btn-default btn-sm pull-right" action="create" namespace="admin" title="${message(code: 'default.new.label', args:[entityName])}">
				<span class="fa-stack">
				  <i class="fa fa-tag fa-stack-2x"></i>
				  <i class="fa fa-plus fa-stack-1x text-primary"></i>
				</span>
			</g:link>
		</h1>
		<table>
			<thead>
				<tr>
					<g:sortableColumn property="label" namespace="admin" title="${message(code: 'taxonomy.label.label', default: 'Name')}" />
				</tr>
			</thead>
			<tbody>
			<g:each in="${taxonomyInstanceList}" status="i" var="taxonomyInstance">
				<tr class="${(i % 2) == 0 ? 'even' : 'odd'}">
				
					<td><g:link action="show" id="${taxonomyInstance.id}" namespace="admin">${fieldValue(bean: taxonomyInstance, field: "label")}</g:link></td>
				
				</tr>
			</g:each>
			</tbody>
		</table>
		<g:if test="${params.max < competenceCount}">
			<g:paginate total="${competenceCount ?: 0}" namespace="admin" />
		</g:if>
	</body>
</html>


<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="admin">
		<g:set var="entityName" value="${message(code: 'de.httc.plugin.taxonomy.taxonomy', default: 'Taxonomy')}" />
		<g:set var="entitiesName" value="${message(code: 'de.httc.plugin.taxonomy.taxonomies', default: 'Taxonomies')}" />
		<title>${entitiesName}</title>
	</head>
	<body>
		<ol class="breadcrumb">
			<li><g:link uri="/admin"><g:message code="default.admin.label" default="Administration" /></g:link></li>
			<li class="active">${entitiesName}</li>
		</ol>
		<h1 class="page-header">
			${entitiesName}
			<g:link class="create btn btn-primary pull-right" action="create" namespace="admin" title="${message(code: 'default.new.label', args:[entityName])}">
				<i class="fa fa-plus"></i><span class="button-label"> <g:message code="default.button.create.label" /></span>
			</g:link>
		</h1>
		<g:if test="${flash.message}">
			<div class="message alert alert-success" role="status">${flash.message}</div>
		</g:if>
		<g:if test="${taxonomyList?.size() > 0}">
			<p class="margin text-muted small"><g:message code="de.httc.search.hits.displaying" args="${[entitiesName, params.offset + 1, Math.min(params.offset + params.max, taxonomyCount), taxonomyCount]}" />:</p>
			<div class="table-responsive">
				<table class="table table-striped">
					<thead>
						<tr>
							<g:sortableColumn property="label" namespace="admin" title="${message(code: 'de.httc.plugin.taxonomy.label', default: 'Name')}" />
							<th><g:message code="de.httc.plugin.taxonomy.term.count" /></th>
							<g:sortableColumn property="type" namespace="admin" title="${message(code: 'de.httc.plugin.taxonomy.type', default: 'Type')}" />
							<g:sortableColumn property="lastUpdated" namespace="admin" title="${message(code: 'de.httc.plugin.taxonomy.lastUpdated', default: 'Last updated')}" />
						</tr>
					</thead>
					<tbody>
					<g:each in="${taxonomyList}" status="i" var="taxonomy">
						<tr>
							<td><g:link action="show" id="${taxonomy.id}" namespace="admin">${fieldValue(bean: taxonomy, field: "label")}</g:link></td>
							<td>${taxonomy.terms?.size()}</td>
							<td>${fieldValue(bean: taxonomy, field: "type")}</td>
							<td><g:formatDate date="${taxonomy.lastUpdated}" type="date"/></td>
						</tr>
					</g:each>
					</tbody>
				</table>
			</div>
			<g:if test="${params.max < taxonomyCount}">
				<g:paginate total="${taxonomyCount ?: 0}" namespace="admin" />
			</g:if>
		</g:if>
		<g:else>
			<div class="alert alert-danger margin"><g:message code="app.filter.empty" args="${[entitiesName]}" default="No {0} found."/></div>
		</g:else>
	</body>
</html>

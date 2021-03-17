<%@ page import="de.httc.plugins.repository.Asset" %>
<!DOCTYPE html>
<html>
	<head>
		<g:set var="currentNamespace" value="${grailsApplication.getArtefactByLogicalPropertyName('Controller', controllerName)?.namespace}" />
		<g:if test="${currentNamespace == 'admin'}">
			<meta name="layout" content="admin">
		</g:if>
		<g:else>
			<meta name="layout" content="main">
		</g:else>
		<g:set var="entityName" value="${message(code: 'de.httc.plugin.repository.asset')}" />
		<g:set var="entitiesName" value="${message(code: 'de.httc.plugin.repository.assets')}" />
		<title>${entitiesName}</title>
	</head>
	<body>
		<h1 class="page-header clearfix">
			${entitiesName}
			<sec:ifAnyGranted roles="ROLE_ADMIN,ROLE_REPOSITORY_ADMIN">
				<g:link class="create btn btn-primary pull-right" action="create" controller="asset" namespace="admin" title="${message(code: 'default.new.label', args:[entityName])}"><i class="fa fa-plus"></i> <g:message code="default.button.create.label" /></g:link>
			</sec:ifAnyGranted>
		</h1>
		<g:if test="${flash.error}">
			<div class="message alert alert-danger" role="status">${flash.error}</div>
		</g:if>
		<g:if test="${flash.message}">
			<div class="message alert alert-success" role="status">${flash.message}</div>
		</g:if>
		<g:if test="${assetList?.size() > 0}">
			<p class="margin text-muted small"><g:message code="app.search.hits.displaying" default="Showing {0} {1}-{2} of {3}" args="${[entitiesName, params.offset + 1, Math.min(params.offset + params.max, assetCount), assetCount]}" />:</p>
			<div class="table-responsive">
				<table class="table">
					<thead>
						<tr>
							<g:sortableColumn property="name" title="${message(code: 'app.meta.name')}" />
							<th><g:message code="app.meta.description" /></th>
							<g:sortableColumn property="lastUpdated" title="${message(code: 'de.httc.plugin.repository.asset.lastUpdated')}" />
							<g:sortableColumn property="mimeType" title="${message(code: 'app.meta.mimeType')}" />
						</tr>
					</thead>
					<tbody>
					<g:each in="${assetList}" var="asset">
						<tr>
							<td><g:link controller="asset" action="show" id="${asset.id}">${fieldValue(bean: asset, field: "name")}</g:link></td>
							<td><httc:abbreviate>${asset.props?."${Asset.PROP_DESCRIPTION}"}</httc:abbreviate></td>
							<td><g:formatDate date="${asset.lastUpdated}" type="date"/></td>
							<td>${fieldValue(bean: asset, field: "mimeType")}</td>
						</tr>
					</g:each>
					</tbody>
				</table>
			</div>
			<g:if test="${params.max < assetCount}">
				<div class="pagination pull-right">
					<g:paginate total="${assetCount ?: 0}" />
				</div>
			</g:if>
		</g:if>
		<g:else>
			<div class="alert alert-danger margin"><g:message code="app.filter.empty" args="${[entitiesName]}" default="No {0} found."/></div>
		</g:else>
	</body>
</html>

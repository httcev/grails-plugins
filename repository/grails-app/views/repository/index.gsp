
<%@ page import="de.httc.plugins.repository.Asset" %>
<!DOCTYPE html>
<html>
	<head>
		<g:set var="currentNamespace" value="${grailsApplication.getArtefactByLogicalPropertyName('Controller', controllerName).namespace}" />
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
				<g:link class="create btn btn-primary pull-right" action="create" controller="repository" namespace="admin" title="${message(code: 'default.new.label', args:[entityName])}"><i class="fa fa-plus"></i> <g:message code="default.button.create.label" /></g:link>
			</sec:ifAnyGranted>
		</h1>
		<g:if test="${flash.message}">
			<div class="message alert alert-success" role="status">${flash.message}</div>
		</g:if>
<!--		
		<g:form controller="search" method="GET" class="form-horizontal">
			<input type="hidden" name="hideFilter" value="true">
			<input type="hidden" name="type" value="asset">
			<input type="hidden" name="subType" value="learning-resource">
			<div class="form-group">
				<div class="col-xs-8">
					<input type="search" name="q" class="form-control" placeholder="${message(code:'app.search.for', args:[entitiesName], default:'Search for {0}')}..." autofocus>
				</div>
				<div class="col-xs-4">
					<button type="submit" class="search btn btn-default"><i class="fa fa-search"></i> <g:message code="app.search" default="Search" /></button>
				</div>
			</div>
		</g:form>
-->
		<g:if test="${assetList?.size() > 0}">
			<p class="margin text-muted small"><g:message code="de.httc.search.hits.displaying" default="Showing {0} {1}-{2} of {3}" args="${[entitiesName, params.offset + 1, Math.min(params.offset + params.max, assetCount), assetCount]}" />:</p>
			<div class="table-responsive">
				<table class="table">
					<thead>
						<tr>
							<g:sortableColumn property="name" namespace="${currentNamespace}" title="${message(code: 'de.httc.plugin.repository.asset.name')}" />
							<th><g:message code="de.httc.plugin.repository.asset.description" /></th>
							<g:sortableColumn property="lastUpdated" namespace="${currentNamespace}" title="${message(code: 'de.httc.plugin.repository.asset.lastUpdated')}" />
							<g:sortableColumn property="mimeType" namespace="${currentNamespace}" title="${message(code: 'de.httc.plugin.repository.asset.mimeType')}" />
						</tr>
					</thead>
					<tbody>
					<g:each in="${assetList}" var="asset">
						<tr>
							<td><g:link controller="repository" namespace="${currentNamespace}" action="show" id="${asset.id}">${fieldValue(bean: asset, field: "name")}</g:link></td>
							<td>${asset.props?."${Asset.PROP_DESCRIPTION}"}</td>
							<td><g:formatDate date="${asset.lastUpdated}" type="date"/></td>
							<td>${fieldValue(bean: asset, field: "mimeType")}</td>
						</tr>
					</g:each>
					</tbody>
				</table>
			</div>
			<g:if test="${params.max < assetCount}">
				<div class="pagination pull-right">
					<g:paginate total="${assetCount ?: 0}" namespace="admin" />
				</div>
			</g:if>
		</g:if>
		<g:else>
			<div class="alert alert-danger margin"><g:message code="app.filter.empty" args="${[entitiesName]}" default="No {0} found."/></div>
		</g:else>
	</body>
</html>

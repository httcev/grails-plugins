
<%@ page import="de.httc.plugins.competence.Competence"%>
<!DOCTYPE html>
<html>
<head>
	<meta name="layout" content="admin">
	<g:set var="entityName" value="${message(code: 'competence.label', default: 'Competence')}" />
	<g:set var="entitiesName" value="${message(code: 'admin.competences.label', default: 'Competences')}" />
	<title>${entitiesName}</title>
</head>
<body>
	<ol class="breadcrumb">
		<li><g:link uri="/admin"><g:message code="default.admin.label" default="Administration" /></g:link></li>
		<li class="active">${entitiesName}</li>
	</ol>
	<h1 class="page-header">
		${entitiesName}
		<g:link class="create btn btn-primary pull-right" action="create" controller="competences" namespace="admin" title="${message(code: 'default.new.label', args:[entityName])}"><i class="fa fa-plus"></i> <g:message code="default.button.create.label" /></g:link>
	</h1>
	<g:if test="${competenceList?.size() > 0}">
		<p class="margin text-muted small"><g:message code="app.search.hits.displaying" default="Showing {0} {1}-{2} of {3}" args="${[entitiesName, params.offset + 1, Math.min(params.offset + params.max, competenceCount), competenceCount]}" />:</p>
		<table class="table">
			<thead>
				<tr>
					<g:sortableColumn property="userId" title="${message(code: 'competence.userId.label', default: 'User Id')}" namespace="admin"/>
					<g:sortableColumn property="level" title="${message(code: 'competence.level.label', default: 'Level')}" namespace="admin"/>
					<g:sortableColumn property="primaryTermLabel" title="${message(code: 'competence.primaryTermId.label', default: 'Primary Term')}" namespace="admin"/>
					<g:sortableColumn property="source" title="${message(code: 'competence.source.label', default: 'Source')}" namespace="admin"/>
				</tr>
			</thead>
			<tbody>
				<g:each in="${competenceList}" status="i" var="competence">
					<tr class="${(i % 2) == 0 ? 'even' : 'odd'}">
						<td>
							<g:link action="show" id="${competence.id}" namespace="admin">
								${fieldValue(bean: competence, field: "userId")}
							</g:link>
						</td>
						<td>${fieldValue(bean: competence, field: "level")}</td>
						<td>${fieldValue(bean: competence, field: "primaryTermLabel")} [${fieldValue(bean: competence, field: "primaryTermId")}]</td>
						<td>${fieldValue(bean: competence, field: "source")}</td>
					</tr>
				</g:each>
			</tbody>
		</table>
		<g:if test="${params.max < competenceCount}">
			<g:paginate total="${competenceCount ?: 0}" namespace="admin" />
		</g:if>
	</g:if>
	<g:else>
		<div class="alert alert-danger margin"><g:message code="app.filter.empty" args="${[entitiesName]}" default="No {0} found."/></div>
	</g:else>
</body>
</html>

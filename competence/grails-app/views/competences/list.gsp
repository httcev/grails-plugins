
<%@ page import="de.httc.plugins.competence.Competence"%>
<!DOCTYPE html>
<html>
<head>
	<meta name="layout" content="admin">
	<g:set var="entityName" value="${message(code: 'competence.label', default: 'Competence')}" />
	<title><g:message code="default.list.label" args="[entityName]" /></title>
</head>
<body>
	<ol class="breadcrumb">
		<li><g:link uri="/admin">
				<g:message code="default.admin.label" default="Administration" />
			</g:link></li>
		<li class="active"><g:message code="default.list.label" args="[entityName]" /></li>
	</ol>
	<h1 class="page-header">
		<g:message code="default.list.label" args="[entityName]" />
		<g:link class="create btn btn-default btn-sm pull-right" action="create" namespace="admin" title="${message(code: 'default.new.label', args:[entityName])}">
			<span class="fa-stack"> <i class="fa fa-user fa-stack-2x"></i> <i class="fa fa-plus fa-stack-1x text-primary"></i>
			</span>
		</g:link>
	</h1>
	<g:if test="${competenceList?.size() > 0}">
		<table class="table">
			<thead>
				<tr>
					<g:sortableColumn property="userId" title="${message(code: 'competence.userId.label', default: 'User Id')}" namespace="admin"/>
					<g:sortableColumn property="level" title="${message(code: 'competence.level.label', default: 'Level')}" />
					<g:sortableColumn property="primaryTermLabel" title="${message(code: 'competence.primaryTermId.label', default: 'Primary Term')}" />
					<g:sortableColumn property="source" title="${message(code: 'competence.source.label', default: 'Source')}" />
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
		<g:paginate total="${competenceCount ?: 0}" namespace="admin" />
	</g:if>
</body>
</html>

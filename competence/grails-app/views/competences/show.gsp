
<%@ page import="de.httc.plugins.competence.Competence"%>
<!DOCTYPE html>
<html>
<head>
<meta name="layout" content="admin">
<g:set var="entityName" value="${message(code: 'competence.label', default: 'Competence')}" />
<title><g:message code="default.show.label" args="[entityName]" /></title>
</head>
<body>
	<ol class="breadcrumb">
		<li><g:link uri="/admin"><g:message code="default.admin.label" default="Administration" /></g:link></li>
		<li><g:link class="list" action="list" namespace="admin"><g:message code="default.list.label" args="[entityName]" /></g:link></li>
		<li class="active"><g:message code="default.show.label" args="[entityName]" /></li>
	</ol>
	<h1 class="page-header">
		<g:message code="default.show.label" args="[entityName]" />
	</h1>
	<ul class="property-list list-group competence">
		<g:if test="${competence?.userId}">
			<li class="fieldcontain list-group-item"><label id="userId-label" class="property-label"><g:message code="competence.userId.label" default="User Id" />:</label> <span class="property-value" aria-labelledby="userId-label"><g:fieldValue bean="${competence}" field="userId" /></span></li>
		</g:if>
		<g:if test="${competence?.level}">
			<li class="fieldcontain list-group-item"><label id="level-label" class="property-label"><g:message code="competence.level.label" default="Level" />:</label> <span class="property-value" aria-labelledby="level-label"><g:fieldValue bean="${competence}" field="level" /></span></li>
		</g:if>
		<g:if test="${competence?.primaryTermLabel}">
			<li class="fieldcontain list-group-item"><label id="primaryTermId-label" class="property-label"><g:message code="competence.primaryTermId.label" default="Primary Term" />:</label> <span class="property-value" aria-labelledby="primaryTermId-label"><g:fieldValue bean="${competence}" field="primaryTermLabel" /> [ID <g:fieldValue bean="${competence}" field="primaryTermId" />]</span></li>
		</g:if>
		<g:if test="${competence?.source}">
			<li class="fieldcontain list-group-item"><label id="source-label" class="property-label"><g:message code="competence.source.label" default="Source" />:</label> <span class="property-value" aria-labelledby="source-label"><g:fieldValue bean="${competence}" field="source" /></span></li>
		</g:if>
	</ul>
	<form action="${createLink(action:'delete', id:competence?.id, namespace:'admin')}" method="post">
		<input type="hidden" name="_method" value="DELETE" id="_method">
		<g:link class="edit btn btn-default" action="edit" id="${competence?.id}" namespace="admin">
			<i class="fa fa-edit fa-lg"></i>
			<g:message code="default.button.edit.label" default="Edit" />
		</g:link>
		<button class="delete btn btn-danger" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', args: [entityName])}');">
			<i class="fa fa-trash-o fa-lg"></i>
			<g:message code="default.button.delete.label" default="Delete" />
		</button>
	</form>
</body>
</html>

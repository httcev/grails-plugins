<%@ page import="de.httc.plugins.repository.Asset" %>
<g:set var="repositoryService" bean="repositoryService"/>
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
		<title><g:message code="default.show.label" args="[entityName]" /></title>
	</head>
	<body>
		<ol class="breadcrumb">
			<li><g:link uri="/"><g:message code="app.home" /></g:link></li>
			<li><g:link class="list" action="index" namespace="admin">${entitiesName}</g:link></li>
			<li class="active"><g:message code="default.show.label" args="[entityName]" /></li>
		</ol>
		<g:if test="${flash.message}">
			<div class="message alert alert-success" role="status">${flash.message}</div>
		</g:if>
		<h1 class="page-header clearfix">
			${asset?.name}
			<sec:ifAnyGranted roles="ROLE_ADMIN,ROLE_REPOSITORY_ADMIN">
				<div class="buttons pull-right">
					<g:link class="delete btn btn-danger" namespace="admin" action="delete" id="${asset.id}" title="${message(code: 'default.button.delete.label', args:[entityName])}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');"><i class="fa fa-times"></i></g:link>
					<g:link class="edit btn btn-primary" namespace="admin" action="edit" id="${asset.id}" title="${message(code: 'default.button.edit.label', args:[entityName])}"><i class="fa fa-pencil"></i></g:link>
				</div>
			</sec:ifAnyGranted>
		</h1>
		<g:if test='${asset?.props?."${Asset.PROP_DESCRIPTION}"}'>
			<p>${asset.props."${Asset.PROP_DESCRIPTION}"}</p>
		</g:if>
<%--		
		<div class="row">
			<div class="col-sm-2"><label><g:message code="de.httc.plugin.repository.asset.typeLabel" />:</label></div>
			<div class="col-sm-10"><code>${asset?.typeLabel}</code></div>
		</div>
--%>
		<div class="row">
			<div class="col-sm-2"><label><g:message code="de.httc.plugin.repository.asset.mimeType" />:</label></div>
			<div class="col-sm-10"><code>${asset?.mimeType}</code></div>
		</div>
		<div class="row">
			<div class="col-sm-2"><label><g:message code="de.httc.plugin.repository.asset.lastUpdated" />:</label></div>
			<div class="col-sm-10"><g:formatDate date="${asset?.lastUpdated}" type="datetime" style="LONG" timeStyle="SHORT"/></div>
		</div>
		<g:each in="${asset?.props?.sort { it.key }}" var="prop">
			<g:if test="${prop.key != Asset.PROP_DESCRIPTION}">
				<div class="row">
					<div class="col-sm-2"><label><g:message code="de.httc.plugin.repository.asset.${prop.key}" default="${prop.key}"/>:</label></div>
					<div class="col-sm-10">${prop.value}</div>
				</div>
			</g:if>
		</g:each>
		<div class="row">
			<div class="col-sm-2"><label><g:message code="de.httc.plugin.repository.asset.link" />:</label></div>
			<g:set var="url" value="${repositoryService.createEncodedLink(asset)}" />
			<div class="col-sm-10"><a href="${url}" target="_blank"><i class="fa fa-external-link"></i> ${url}</a></div>
		</div>
	</body>
</html>

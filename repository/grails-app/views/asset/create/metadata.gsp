<%@ page import="de.httc.plugins.repository.Asset" %>
<html>
	<head>
		<meta name="layout" content="createAsset">
	</head>
	<body>
		<div class="form-group ${hasErrors(bean: cmd, field: 'name', 'error')} required">
			<label for="name" class="col-sm-2 control-label">
				<g:message code="de.httc.plugin.repository.asset.name" />
				<span class="required-indicator">*</span>:
			</label>
			<div class="col-sm-10"><g:textField name="name" class="form-control" required="" value="${cmd?.name}" autofocus=""/></div>
		</div>
		<div class="form-group ${hasErrors(bean: cmd, field: 'description', 'error')}">
			<label for="description" class="col-sm-2 control-label">
				<g:message code="de.httc.plugin.repository.asset.description" />
				<span class="required-indicator">*</span>:
			</label>
			<div class="col-sm-10"><g:textArea id="description" name="props['$Asset.PROP_DESCRIPTION']" class="form-control" rows="10" required="" value='${cmd?.props?."${Asset.PROP_DESCRIPTION}"}'/></div>
		</div>
<%--		
		<div class="form-group ${hasErrors(bean: cmd, field: 'type', 'error')}">
			<label for="type" class="col-sm-2 control-label">
				<g:message code="de.httc.plugin.repository.asset.type" />:
			</label>
			<div class="col-sm-10"><g:textField name="type" class="form-control" value="${cmd?.type}"/></div>
		</div>
--%>		
		<div class="form-group ${hasErrors(bean: cmd, field: 'mimeType', 'error')}">
			<div class="col-sm-2">
				<label class="pull-right"><g:message code="de.httc.plugin.repository.asset.mimeType" />:</label>
			</div>
			<div class="col-sm-10">${cmd?.mimeType}</div>
		</div>
		<g:if test="${cmd?.props?."${Asset.PROP_ANCHOR}"}">
			<div class="form-group">
				<div class="col-sm-2">
					<label class="pull-right"><g:message code="de.httc.plugin.repository.asset.anchor" />:</label>
				</div>
				<div class="col-sm-10">${cmd?.props?."${Asset.PROP_ANCHOR}"}</div>
			</div>
		</g:if>
		<div class="buttons pull-right">
			<button name="_eventId_submit" class="save btn btn-success"><i class="fa fa-save"></i> <g:message code="default.button.create.label" default="Create" /></button>
		</div>
	</body>
</html>

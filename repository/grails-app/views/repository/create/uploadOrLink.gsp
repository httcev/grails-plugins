<%@ page import="de.httc.plugins.repository.Asset" %>
<html>
	<head>
		<meta name="layout" content="createAsset">
	</head>
	<body>
		<div class="form-group ${hasErrors(bean: cmd, field: 'content', 'error')} required">
			<label for="content" class="col-sm-3 control-label">
				<g:message code="de.httc.plugin.repository.asset.upload"/>:
			</label>
			<div class="col-sm-9"><input type="file" id="content" name="content" class="form-control" /></div>
		</div>
		<div class="form-group">
			<label class="col-sm-3 control-label"><g:message code="de.httc.plugin.repository.or" /></label>
		</div>
		<div class="form-group ${hasErrors(bean: cmd, field: 'props', 'error')} ">
			<label for="externalUrl" class="col-sm-3 control-label">
				<g:message code="de.httc.plugin.repository.asset.link" />:
			</label>
			<div class="col-sm-9"><g:textField id="externalUrl" name="props['${Asset.PROP_EXTERNAL_URL}']" class="form-control" value='${cmd?.props?."${Asset.PROP_EXTERNAL_URL}"}'/></div>
		</div>
		<div class="buttons pull-right">
			<button name="_eventId_submit" class="next btn btn-primary"><g:message code="de.httc.plugin.repository.next" /> <i class="fa fa-chevron-right"></i></button>
		</div>
	</body>
</html>

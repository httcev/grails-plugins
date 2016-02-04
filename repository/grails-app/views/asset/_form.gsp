<%@ page import="de.httc.plugins.repository.Asset" %>
<div class="form-group ${hasErrors(bean: asset, field: 'name', 'error')} required">
	<label for="name" class="col-sm-2 control-label"><g:message code="de.httc.plugin.repository.asset.name" /><span class="required-indicator">*</span>:</label>
	<div class="col-sm-10"><g:textField name="name" value="${asset?.name}" class="form-control" required="" /></div>
</div>
<g:if test='${asset?.props?."${Asset.PROP_DESCRIPTION}"}'>
	<div class="form-group">
		<label for="description" class="col-sm-2 control-label"><g:message code="de.httc.plugin.repository.asset.description" /><span class="required-indicator">*</span>:</label>
		<div class="col-sm-10"><g:textArea rows="8" id="description" name='props.$Asset.PROP_DESCRIPTION' value='${asset?.props."${Asset.PROP_DESCRIPTION}"}' class="form-control" /></div>
	</div>
</g:if>
<%--
<div class="form-group ${hasErrors(bean: asset, field: 'type', 'error')}">
	<label for="type" class="col-sm-2 control-label"><g:message code="de.httc.plugin.repository.asset.type" />:</label>
	<div class="col-sm-10"><g:textField name="type" value="${asset?.type}" class="form-control" /></div>
</div>
--%>
<g:each in="${asset?.props?.sort { it.key }}" var="prop">
	<g:if test="${prop.key != Asset.PROP_DESCRIPTION}">
		<div class="form-group">
			<label class="col-sm-2 control-label"><g:message code="de.httc.plugin.repository.asset.${prop.key}" default="${prop.key}" />:</label>
			<div class="col-sm-10"><g:textField name="props.${prop.key}" value="${prop.value}" class="form-control" /></div>
		</div>
	</g:if>
</g:each>

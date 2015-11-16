<%@ page import="de.httc.plugins.taxonomy.Taxonomy"%>

<div class="form-group">
	<label for="label" class="col-sm-2 control-label"><g:message code="de.httc.plugin.taxonomy.label" default="Name" /></label>
	<div class="col-sm-10">
		<g:textField name="label" value="${taxonomyInstance?.label}" class="form-control" />
	</div>
</div>
<div class="form-group">
	<label for="label" class="col-sm-2 control-label"><g:message code="de.httc.plugin.taxonomy.type" default="Taxonomy type" /></label>
	<div class="col-sm-10">
		<g:select name="type" class="form-control" from="${Taxonomy.Type.values()}" value="${taxonomyInstance?.type}" />
	</div>
</div>

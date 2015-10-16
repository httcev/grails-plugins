<div class="form-group">
	<label for="label" class="col-sm-2 control-label"><g:message code="taxonomy.label.label" default="Name" /></label>
	<div class="col-sm-10">
		<g:textField name="label" value="${taxonomyInstance?.label}" class="form-control" />
	</div>
	<div class="col-sm-offset-2 col-sm-10">
		<div class="checkbox">
			<label><g:checkBox name="knowledgeDomain" value="${false}" /> <g:message code="taxonomy.knowledgeDomain.label" default="Knowledge Domain" /></label>
		</div>
	</div>
</div>

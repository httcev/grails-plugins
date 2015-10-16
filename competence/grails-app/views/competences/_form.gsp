<%@ page import="de.httc.plugins.competence.Competence"%>

<div class="form-group fieldcontain ${hasErrors(bean: competence, field: 'userId', 'error')} ">
	<label for="userId"><g:message code="competence.userId.label" default="User Id" /></label>
	<g:field type="number" name="userId" value="${competence.userId}" class="form-control" />
</div>
<div class="form-group fieldcontain ${hasErrors(bean: competence, field: 'level', 'error')} ">
	<label for="level"><g:message code="competence.level.label" default="Level" /></label>
	<g:field type="number" name="level" value="${competence.level}" class="form-control" />
</div>
<div class="form-group fieldcontain ${hasErrors(bean: competence, field: 'primaryTermId', 'error')} ">
	<label for="primaryTermId"><g:message code="competence.primaryTermId.label" default="Primary Term Id" /></label>
	<g:field type="number" name="primaryTermId" value="${competence.primaryTermId}" class="form-control" />
</div>
<div class="form-group fieldcontain ${hasErrors(bean: competence, field: 'source', 'error')} ">
	<label for="source"><g:message code="competence.source.label" default="Source" /></label>
	<g:select name="source" from="${de.molem.competence.Competence$Source?.values()}" keys="${de.molem.competence.Competence$Source.values()*.name()}" required="" value="${competence?.source?.name()}" class="form-control" />
</div>



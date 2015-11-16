<fieldset>
	<legend class="text-primary clearfix">
		<g:message code="de.httc.plugin.taxonomy.terms" default="Terms" />
		<g:if test="${mode=='edit'}">
			<g:set var="entityName" value="${message(code: 'de.httc.plugin.taxonomy.term', default: 'Term')}" />
			<div class="buttons pull-right">
				<button type="button" class="create btn btn-default" type="button" title="<g:message code="default.add.label" args="[entityName]" />" onclick="termCreate()"><span class="glyphicon glyphicon-plus"></span> <g:message code="default.button.add.label" default="Add" /></button>
				<button id="rename-button" type="button" class="rename btn btn-default disabled" type="button" title="<g:message code="default.rename.label" args="[entityName]" />" onclick="termRename()"><g:message code="default.button.rename.label" default="Rename" /></button>
				<button id="delete-button" type="button" class="remove btn btn-danger disabled" type="button" title="<g:message code="default.delete.label" args="[entityName]" />" onclick="termRemove()"><span class="glyphicon glyphicon-trash"></span> <g:message code="default.button.delete.label" default="Delete" /></button>
			</div>
		</g:if>
	</legend>
	<div id="taxonomy">
		<g:if test="${terms?.size() > 0}">
			<ul>
				<g:each in="${terms}" var="term">
				    <g:render template="term" model="${[term:term]}" />
				</g:each>
			</ul>
		</g:if>
		<g:else>
			<div class="alert alert-danger margin"><g:message code="app.filter.empty" args="${[message(code: 'de.httc.plugin.taxonomy.terms', default: 'Terms')]}" default="No {0} found."/></div>
		</g:else>
	</div>
</fieldset>

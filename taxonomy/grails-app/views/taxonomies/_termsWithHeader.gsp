<g:set var="hasChildren" value="${taxonomy?.children?.size() > 0}" />
<g:if test="${mode=='edit' || hasChildren}">
	<fieldset>
		<legend class="text-primary clearfix padding-bottom">
			<g:message code="de.httc.plugin.taxonomy.terms" default="Terms" /><g:if test="${hasChildren}"> <span class="badge middle">${taxonomy.termCount}</span></g:if>
			<g:if test="${mode=='edit'}">
				<g:set var="entityName" value="${message(code: 'de.httc.plugin.taxonomy.term', default: 'Term')}" />
				<div class="buttons pull-right">
					<button type="button" class="create btn btn-default" type="button" title="${message(code:"default.add.label", args:[entityName], default:'Add term')}" onclick="termCreate()"><span class="glyphicon glyphicon-plus"></span> <g:message code="default.add.label" args="[entityName]" default="Add term" /></button>
					<button id="rename-button" type="button" class="rename btn btn-default disabled" type="button" title="${message(code:'default.rename.label', args:[entityName], default:'Rename term')}" onclick="termRename()"><g:message code="default.rename.label" args="[entityName]" default="Rename term" /></button>
					<button id="delete-button" type="button" class="remove btn btn-danger disabled" type="button" title="${message(code:'default.delete.label', args:[entityName], default:'Delete term')}" onclick="termRemove()"><span class="glyphicon glyphicon-trash"></span> <g:message code="default.delete.label" args="[entityName]" default="Delete term" /></button>
				</div>
			</g:if>
		</legend>
		<div id="taxonomy" class="taxonomy-${mode=='edit' ? 'edit' : 'show'}">
			<g:render template="terms" model="${[node:taxonomy]}" />
		</div>
	</fieldset>
</g:if>
<g:else>
	<div class="alert alert-danger margin"><g:message code="app.filter.empty" args="${[message(code: 'de.httc.plugin.taxonomy.terms', default: 'Terms')]}" default="No {0} found."/></div>
</g:else>

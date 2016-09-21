<option value="${term.id}"${selectedValue == term.id ? ' selected' : ''} style="text-indent:${level*20}px"><g:message code="kola.taxonomy.taskType.terms.${term.label}" default="${term.label}" /></option>
<g:each in="${term.children}" var="child">
	<g:render template="/taxonomies/termOption" model="${[term:child, selectedValue:selectedValue, level:level+1]}" />
</g:each>

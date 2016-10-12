<option value="${term.id}"${selectedValues?.contains(term.id) ? ' selected' : ''} style="text-indent:${level*20}px">${term.label}</option>
<g:each in="${term.children}" var="child">
	<g:render template="/taxonomies/termOption" model="${[term:child, selectedValues:selectedValues, level:level+1]}" />
</g:each>

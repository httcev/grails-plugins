<g:if test="${node?.children?.size() > 0}">
	<ul>
		<g:each in="${node.children}" var="term">
			<g:set var="termClasses" value="${['0']}" />
			<g:render template="term" model="${[term:term, first:node.children.first() == term, last:node.children.last() == term]}" />
		</g:each>
	</ul>
</g:if>

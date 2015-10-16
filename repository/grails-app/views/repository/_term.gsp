<li id="${term?.id}">
	<a><g:fieldValue bean="${term}" field="label"/></a>
	<g:if test="${term?.children?.size() > 0}">
		<g:each in="${term?.children}" var="child">
			<ul>
			    <g:render template="term" model="${[term:child]}" />
		    </ul>
		</g:each>
	</g:if>
</li>

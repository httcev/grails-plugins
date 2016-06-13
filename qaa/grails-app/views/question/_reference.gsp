<g:if test="${question.reference}">
	<div class="question-reference">
		<g:message code="de.httc.plugin.qaa.question.refersTo" /> <g:link resource="${question.reference}" action="show"><g:message code="de.httc.plugin.qaa.question.refersToThis" /></g:link>.
	</div>
</g:if>

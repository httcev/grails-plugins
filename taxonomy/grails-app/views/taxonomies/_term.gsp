<li id="${term?.id}" class="taxonomy-term${first ? ' taxonomy-term-first' : ''}${last ? ' taxonomy-term-last' : ''}">
	<a><g:fieldValue bean="${term}" field="label"/></a>
	<g:render template="terms" model="${[node:term]}" />
</li>

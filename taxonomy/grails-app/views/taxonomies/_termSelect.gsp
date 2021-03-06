<asset:stylesheet src="chosen-adapted.css"/>
<asset:javascript src="chosen.jquery.js"/>
<g:set var="selectId" value="${id ?: UUID.randomUUID().toString()}" />
<select id="${selectId}" name="${name}" class="form-control-dummy ${classes?.join(' ')}"${multiple ? " multiple" : ""} data-placeholder="${placeholder}">
	<option value=""></option>
	<g:each in="${terms}" var="term">
		<g:render template="/taxonomies/termOption" model="${[term:term, selectedValues:selectedValues, level:0]}" />
	</g:each>
</select>
<asset:script type="text/javascript">
	$(function() {
		$("#${selectId}").chosen({
			no_results_text:'<g:message code="app.filter.empty" args="${[message(code: 'de.httc.plugin.taxonomy.terms', default: 'Terms')]}" default="No {0} found."/>'
			, disable_search_threshold: 10
			, width:"100%"
			, allow_single_deselect: true
			, inherit_select_classes: true
		});
	});
</asset:script>
<asset:deferredScripts/>

<g:if test="${possibleReferences?.size() > 0}">
	<g:render model="${[question:question, possibleReferences:possibleReferences, mode:'edit']}" template="/question/reference" />
</g:if>
<div class="form-group">
	<label for="question-title"><g:message code="de.httc.plugin.qaa.question.title" />:</label>
	<input type="text" name="title" class="form-control" id="question-title" placeholder="${message(code:'de.httc.plugin.qaa.question.title.placeholder')}" required autofocus value="${question.title}">
</div>
<div class="form-group">
	<label for="question-text"><g:message code="de.httc.plugin.qaa.question.text" />:</label>
	<g:textArea rows="5" id="question-text" name="text" class="form-control" value="${question.text}" data-provide="markdown" data-iconlibrary="fa" data-language="de" data-hidden-buttons="cmdImage cmdCode cmdQuote cmdPreview" placeholder="${message(code:'de.httc.plugin.qaa.question.text.placeholder')}" />
</div>
<g:render model="${[attachments:question.attachments, mode:'edit']}" template="/task/attachments" />
<div class="text-right form-padding-all"><button type="submit" class="btn btn-success"><i class="fa fa-save"></i> <g:message code="default.save.label" args="[message(code:'de.httc.plugin.qaa.question')]" /></button></div>

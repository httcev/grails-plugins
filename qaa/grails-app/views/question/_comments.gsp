<div class="comments-list clearfix">
	<g:if test="${commentable?.comments?.size() > 0}">
		<h4 class="header"><g:message code="de.httc.plugin.qaa.comments" /></h4>
		<g:each var="comment" in="${commentable?.comments}">
			<g:if test="${!comment.deleted}">
				<div id="comment-${comment.id}" class="comment clearfix">
					<div id="comment-content-${comment.id}">
						<a name="${comment.id}"></a>
						<httc:markdown>${comment?.text}</httc:markdown>
						<div class="pull-right">
							<g:if test="${authService.canEdit(comment)}">
								<button type="button" class="btn btn-default btn-small" onclick="$('#comment-content-${comment.id}').hide().next('.form').removeClass('hidden').find('textarea').focus()"><i class="fa fa-pencil"></i> <g:message code="default.button.edit.label" /></button>
							</g:if>
							<g:render model="${[profile:comment?.creator?.profile, hideImage:true]}" template="/profile/show" />,
							<g:formatDate date="${comment?.dateCreated}" type="date" />
						</div>
					</div>
					<g:if test="${authService.canEdit(comment)}">
						<g:form class="form hidden form-horizontal" action="updateComment" id="${comment.id}" method="PUT">
							<div class="form-group">
								<input type="hidden" name="reference" value="${commentable.id}">
								<div class="col-sm-10"><g:textArea rows="5" name="text" class="form-control" data-provide="markdown" data-iconlibrary="fa" data-language="de" data-hidden-buttons="cmdImage cmdCode cmdQuote cmdPreview" value="${comment.text}" placeholder="${message(code:'de.httc.plugin.qaa.comment.placeholder')}" /></div>
								<div class="col-sm-2 text-right form-padding"><button type="submit" class="btn btn-success"><i class="fa fa-save"></i> <g:message code="default.save.label" args="[message(code:'de.httc.plugin.qaa.comment')]" /></button></div>
							</div>
						</g:form>
					</g:if>
				</div>
			</g:if>
		</g:each>
	</g:if>
	<button type="button" class="comment-button btn btn-default btn-small pull-right" onclick="$(this).hide().next('.new-comment').removeClass('hidden').find('.comment-text').focus()"><i class="fa fa-plus"></i> <g:message code="default.add.label" args="[message(code:'de.httc.plugin.qaa.comment')]"/></button>
	<div class="new-comment hidden">
		<h1><g:message code="default.add.label" args="[message(code:'de.httc.plugin.qaa.comment')]"/>:</h1>
		<g:form class="form form-horizontal" action="saveComment">
			<input type="hidden" name="reference" value="${commentable.id}">
			<div class="form-group">
				<div class="col-sm-10"><g:textArea rows="3" name="text" class="form-control comment-text" data-provide="markdown" data-iconlibrary="fa" data-language="de" data-hidden-buttons="cmdImage cmdCode cmdQuote cmdPreview" value="" placeholder="${message(code:'de.httc.plugin.qaa.comment.placeholder')}" required="true"/></div>
				<div class="col-sm-2 text-right form-padding"><button type="submit" class="btn btn-success"><i class="fa fa-save"></i> <g:message code="default.save.label" args="[message(code:'de.httc.plugin.qaa.comment')]"/></button></div>
			</div>
		</g:form>
	</div>
</div>

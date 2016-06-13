<div class="list-group-item question-list-item">
	<div class="answer-count${question.acceptedAnswer ? ' text-success' : ''}">
		<i class="httc-answer httc-fw"></i>&#160;${question.answers.size()}
	</div>
	<g:link resource="${question}" action="show">
	    <httc:abbreviate>${question.title}</httc:abbreviate>
	</g:link>
	<div class="clearfix">
		<div class="pull-right">
			<g:render model="${[profile:question?.creator?.profile]}" template="/profile/show" />,
			<g:formatDate date="${question?.dateCreated}" type="date" />
		</div>
	</div>
</div>


<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'de.httc.plugin.qaa.question')}" />
		<g:set var="entitiesName" value="${message(code: 'de.httc.plugin.qaa.questions')}" />
		<title>${entitiesName}</title>
	</head>
	<body>
		<h1 class="page-header clearfix">
			${entitiesName}
			<g:link class="create btn btn-primary pull-right" action="create" title="${message(code: 'de.httc.plugin.qaa.question.create')}">
				<i class="httc-question-add"></i> <g:message code="de.httc.plugin.qaa.question.create" />
			</g:link>
		</h1>
		<g:if test="${flash.error}">
			<div class="message alert alert-danger" role="status">${flash.error}</div>
		</g:if>
		<g:if test="${flash.message}">
			<div class="message alert alert-success" role="status">${flash.message}</div>
		</g:if>
		<form method="get">
			<div class="row filter">
				<div class="col-xs-12">
					<b><g:message code="kola.filter" /></b>:
					<span class="checkbox">
						<label><input name="own" type="checkbox" onclick="$(this).closest('form').submit()"${params.own ? ' checked' : ''}> <g:message code="kola.filter.own" /></label>
					</span>
					<span class="checkbox">
						<label><input name="ownCompany" type="checkbox" onclick="$(this).closest('form').submit()"${params.ownCompany ? ' checked' : ''}> <g:message code="kola.filter.ownCompany" /></label>
					</span>
				</div>
			</div>
		</form>
		<g:if test="${questionList?.size() > 0}">
			<p class="margin text-muted small"><g:message code="kola.search.hits.displaying" args="${[entitiesName, params.offset + 1, Math.min(params.offset + params.max, questionCount), questionCount]}" />:</p>
			<g:set var="filterParams" value="${[own:params.own, ownCompany:params.ownCompany]}" />
            <div class="list-group">
                <g:each in="${questionList}" var="question">
                    <g:render bean="${question}" var="question" template="questionListItem" plugin="httcQaa" />
                </g:each>
            </div>
			<g:if test="${params.max < questionCount}">
				<div class="pagination pull-right">
					<g:paginate total="${questionCount ?: 0}" params="${filterParams}" />
				</div>
			</g:if>
		</g:if>
		<g:else>
			<div class="alert alert-danger margin"><g:message code="app.filter.empty" args="${[entitiesName]}" /></div>
		</g:else>
	</body>
</html>

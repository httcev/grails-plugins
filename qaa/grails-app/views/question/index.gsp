
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'de.httc.plugin.qaa.question')}" />
		<g:set var="entitiesName" value="${message(code: 'de.httc.plugin.qaa.questions')}" />
		<title>${entitiesName}</title>
		<asset:stylesheet src="chosen-adapted.css"/>
		<asset:javascript src="chosen.jquery.js"/>
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
		<form method="get" id="filter-form" class="well form-inline">
			<b><g:message code="app.filter" />:</b>
			<div class="row filter">
				<div class="col-md-4 margin-vertical">
					<select name="createdBy" class="form-control-dummy" value="${params.createdBy}" data-placeholder="${message(code:'app.meta.createdBy')}...">
						<option value="all"${params.createdBy=="all" ? " selected" : ""}></option>
						<option value="own"${params.createdBy=="own" ? " selected" : ""}><g:message code="app.meta.createdBy" />: <g:message code="app.meta.me" /></option>
						<option value="company"${params.createdBy=="company" ? " selected" : ""}><g:message code="app.meta.createdBy" />: <g:message code="app.meta.myCompany" /></option>
					</select>
				</div>
				<div class="col-md-4 margin-vertical">
					<select name="answered" class="form-control-dummy" value="${params.answered}" data-placeholder="${message(code:'de.httc.plugin.qaa.answered')}...">
						<option value="all"${params.answered=="all" ? " selected" : ""}></option>
						<option value="yes"${params.answered=="yes" ? " selected" : ""}><g:message code="de.httc.plugin.qaa.answered" />: <g:message code="app.yes" /></option>
						<option value="no"${params.answered=="no" ? " selected" : ""}><g:message code="de.httc.plugin.qaa.answered" />: <g:message code="app.no" /></option>
					</select>
				</div>
				<div class="col-md-4 margin-vertical">
					<select name="task" class="form-control-dummy" value="${params.task}" data-placeholder="${message(code:'de.httc.plugin.qaa.taskRelation')}...">
						<option value="all"${params.task=="all" ? " selected" : ""}></option>
						<option value="yes"${params.task=="yes" ? " selected" : ""}><g:message code="de.httc.plugin.qaa.taskRelation" />: <g:message code="app.yes" /></option>
						<option value="no"${params.task=="no" ? " selected" : ""}><g:message code="de.httc.plugin.qaa.taskRelation" />: <g:message code="app.no" /></option>
					</select>
				</div>
			</div>
		</form>
		<g:if test="${questionList?.size() > 0}">
			<p class="margin text-muted small"><g:message code="app.search.hits.displaying" args="${[entitiesName, params.offset + 1, Math.min(params.offset + params.max, questionCount), questionCount]}" />:</p>
			<g:set var="filterParams" value="${[answered:params.answered, createdBy:params.createdBy, task:params.task]}" />
			<g:set var="sortParams" value="${[resetOffset:true] << filterParams}" />
			<div class="table-responsive">
				<table class="table table-striped">
					<thead>
						<tr>
							<g:sortableColumn property="title" title="${message(code: 'de.httc.plugin.qaa.question')}" params="${sortParams}" />
							<g:sortableColumn property="acceptedAnswer" class="text-center" title="${message(code: 'de.httc.plugin.qaa.answered')}" params="${sortParams}" />
							<th class="text-center">${message(code: 'de.httc.plugin.qaa.answers')}</th>
							<g:sortableColumn property="cp.lastName" title="${message(code: 'app.meta.createdBy')}" params="${sortParams}" />
							<g:sortableColumn property="cp.company" title="${message(code: 'de.httc.plugin.user.company')}" params="${sortParams}" />
							<g:sortableColumn property="dateCreated" title="${message(code: 'app.meta.created')}" params="${sortParams}" />
						</tr>
					</thead>
					<tbody>
					<g:each in="${questionList}" status="i" var="question">
						<tr>
							<td><g:link action="show" id="${question.id}"><httc:abbreviate>${fieldValue(bean: question, field: "title")}</httc:abbreviate></g:link></td>
							<td class="text-center"><i class="fa fa-fw ${question.acceptedAnswer ? 'fa-check text-success' : 'fa-minus text-warning'}"></i></td>
							<td class="text-center">${question.answers?.size()}</td>
							<td>${fieldValue(bean: question.creator?.profile, field: "displayNameReverse")}</td>
							<td>${question.creator?.profile?.company?.label}</td>
							<td><g:formatDate date="${question.dateCreated}" type="date"/></td>
						</tr>
					</g:each>
					</tbody>
				</table>
			</div>
<%--
      <div class="list-group">
        <g:each in="${questionList}" var="question">
          <g:render bean="${question}" var="question" template="questionListItem" plugin="httcQaa" />
        </g:each>
      </div>
--%>
			<g:if test="${params.max < questionCount}">
				<div class="pagination pull-right">
					<g:paginate total="${questionCount ?: 0}" params="${filterParams}" />
				</div>
			</g:if>
		</g:if>
		<g:else>
			<div class="alert alert-danger margin"><g:message code="app.filter.empty" args="${[entitiesName]}" /></div>
		</g:else>

		<script>
			$(function() {
				$("#filter-form select").change(function() {
					$(this).closest('form').submit();
				});
				$("#filter-form .form-control-dummy").chosen({
					disable_search_threshold: 10
					, width:"100%"
					, allow_single_deselect: true
					, inherit_select_classes: true
				});
			});
		</script>
	</body>
</html>

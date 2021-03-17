<html>
	<head>
		<meta name="layout" content="admin">
		<g:set var="entityName" value="${message(code: 'de.httc.plugin.user.user')}" />
		<g:set var="entitiesName" value="${message(code: 'de.httc.plugin.user.users')}" />
		<title>${entitiesName}</title>
	</head>
	<body>
<%--
		<ol class="breadcrumb">
			<li><g:message code="default.admin.label" default="Administration" /></li>
			<li class="active">${entitiesName}</li>
		</ol>
--%>
		<h1 class="page-header clearfix">
			<g:form class="form-inline" action="index" method="GET" name="filter-form" params="[sort:params.sort, order:params.order]">
				<i class="fa fa-users text-muted"></i> ${entitiesName}
				<div class="input-group">
					<span class="input-group-addon" onclick="$('#filter').focus()" title="${message(code: 'app.search.filter')}"><i class="fa fa-filter"></i></span>
					<input class="form-control" type="text" value="${params.filter}" placeholder="Filter..." name="filter" id="filter" autocomplete="off">
					<span class="input-group-btn"><button class="btn btn-default" type="button" onclick="$('#filter').val('').trigger('change')"><i class="fa fa-times"></i></button></span>
				</div>
				<g:link class="create btn btn-primary pull-right" action="create" title="${message(code: 'default.new.label', args:[entityName])}">
					<i class="fa fa-user-plus"></i><span class="button-label"> <g:message code="default.button.create.label" /></span>
				</g:link>
			</g:form>
		</h1>
		<g:if test="${flash.message}">
			<div class="message alert alert-success" role="status">${flash.message}</div>
		</g:if>
		<g:if test="${userList?.size() > 0}">
			<p class="margin text-muted small"><g:message code="app.search.hits.displaying" args="${[entitiesName, params.offset + 1, Math.min(params.offset + params.max, userCount), userCount]}" />:</p>
			<g:set var="filterParams" value="${[filter:params.filter]}" />
			<g:set var="sortParams" value="${[resetOffset:true] << filterParams}" />
			<div class="table-responsive">
				<table class="table table-striped">
					<thead>
						<tr>
							<g:sortableColumn property="username" title="${message(code: 'de.httc.plugin.user.loginName')}" params="${sortParams}" />
							<g:sortableColumn property="p.lastName" title="${message(code: 'de.httc.plugin.user.lastName')}" params="${sortParams}" />
							<g:sortableColumn property="p.firstName" title="${message(code: 'de.httc.plugin.user.firstName')}" params="${sortParams}" />
							<g:sortableColumn property="pc.label" title="${message(code: 'de.httc.plugin.user.company')}" params="${sortParams}" />
							<th>${message(code: 'de.httc.plugin.taxonomy.label.organisations')}</th>
							<g:sortableColumn property="enabled" title="${message(code: 'de.httc.plugin.user.enabled')}" params="${sortParams}" />
							<g:sortableColumn property="accountLocked" title="${message(code: 'de.httc.plugin.user.accountNotLocked')}" params="${sortParams}" />
						</tr>
					</thead>
					<tbody>
					<g:each in="${userList}" var="user">
						<tr>
							<td><g:link action="edit" id="${user.id}">${fieldValue(bean: user, field: "username")}</g:link></td>
							<td>${fieldValue(bean: user.profile, field: "lastName")}</td>
							<td>${fieldValue(bean: user.profile, field: "firstName")}</td>
							<td>${user?.profile?.company?.label}</td>
							<td>${user?.profile?.organisations*.label.join(", ")}</td>
							<td><i class="fa fa-lg fa-${user.enabled ? 'check text-success' : 'minus text-warning'}"></i></td>
							<td><i class="fa fa-lg fa-${user.accountLocked ? 'minus text-warning' : 'check text-success'}"></i></td>
						</tr>
					</g:each>
					</tbody>
				</table>
			</div>
			<g:if test="${params.max < userCount}">
				<div class="pagination pull-right">
					<g:paginate total="${userCount ?: 0}" params="${filterParams}" />
				</div>
			</g:if>
		</g:if>
		<g:else>
			<div class="alert alert-danger margin"><g:message code="app.filter.empty" args="${[entitiesName]}" default="No {0} found."/></div>
		</g:else>

		<script type="text/javascript">
			function debounce(func, wait, immediate) {
				var timeout;
				return function() {
					var context = this, args = arguments;
					var later = function() {
						timeout = null;
						if (!immediate) func.apply(context, args);
					};
					var callNow = immediate && !timeout;
					clearTimeout(timeout);
					timeout = setTimeout(later, wait);
					if (callNow) func.apply(context, args);
				};
			};

			$(document).ready(function() {
				var $form = $("#filter-form");
				var submitFormFunction = debounce(function() {
					$form.submit();
				}, 250);
				/*
				$form.submit(function() {
					$.ajax({
						type: "GET",
						url: $form.attr("action"),
						data: $form.serialize(),
						success: function(data) {
							updateDataTable(data);
						}
					});
					return false;
				});
				*/
				$("input", $form).bind("change", submitFormFunction);
			});
		</script>
	</body>
</html>

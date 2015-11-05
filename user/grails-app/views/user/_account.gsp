<%@ page import="de.httc.plugins.user.Role" %>

<div class="form-group">
	<label class="col-sm-2 control-label"><g:message code="de.httc.plugin.user.account" />:</label>
	<div class="col-sm-10">
		<div class="checkbox">
			<label><g:checkBox name="enabled" value="${user?.enabled}"/> <g:message code="de.httc.plugin.user.enabled" /></label>
		</div>
	</div>
	<div class="col-sm-offset-2 col-sm-10">
		<div class="checkbox">
			<label><g:checkBox name="passwordExpired" value="${user?.passwordExpired}"/> <g:message code="de.httc.plugin.user.passwordExpired" /></label>
		</div>
	</div>
	<div class="col-sm-offset-2 col-sm-10">
		<div class="checkbox">
			<label><g:checkBox name="termsOfUseAccepted" value="${user?.termsOfUseAccepted}"/> <g:message code="de.httc.plugin.user.termsOfUseAccepted" /></label>
		</div>
	</div>
	<div class="col-sm-offset-2 col-sm-10">
		<div class="checkbox">
			<label><g:checkBox name="accountLocked" value="${user?.accountLocked}"/> <g:message code="de.httc.plugin.user.accountLocked" /></label>
		</div>
	</div>
<%--
	<div class="col-sm-offset-2 col-sm-10">
		<div class="checkbox">
			<label><g:checkBox name="accountExpired" value="${user?.accountExpired}"/> <g:message code="de.httc.plugin.user.accountExpired" /></label>
		</div>
	</div>
--%>
</div>
<div class="form-group">
	<label class="col-sm-2 control-label"><g:message code="de.httc.plugin.user.roles" />:</label>
	<div class="col-sm-10">
		<g:each in="${Role.list()}" var="role" >
			<div class="checkbox">
				<label><g:checkBox name="role" value="${role.id}" checked="${user?.authorities?.contains(role)}"/> <g:message code="role.${role.authority}.label" default="${role.authority}" /></label>
			</div>
		</g:each>
	</div>
</div>

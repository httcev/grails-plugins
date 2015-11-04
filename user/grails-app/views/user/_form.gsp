<!-- fake fields are a workaround for chrome autofill getting the wrong fields -->
<input style="display:none" type="text" name="fakeusernameremembered"/>
<input style="display:none" type="password" name="fakepasswordremembered"/>

<div class="form-group ${hasErrors(bean: user, field: 'username', 'error')} required">
	<label for="username" class="col-sm-2 control-label"><g:message code="de.httc.plugin.user.loginName" /><span class="required-indicator">*</span>:</label>
	<div class="col-sm-10"><g:textField name="username" value="${user?.username}" class="form-control" required="" autocomplete="off"/></div>
</div>
<div class="form-group ${hasErrors(bean: user, field: 'password', 'error')} required">
	<label for="password" class="col-sm-2 control-label"><g:message code="de.httc.plugin.user.password" /><span class="required-indicator">*</span>:</label>
	<div class="col-sm-10"><input type="password" name="password" value="${user?.password}" class="form-control" id="password" required autocomplete="off"></div>
</div>
<div class="form-group ${hasErrors(bean: user, field: 'email', 'error')} required">
	<label for="email" class="col-sm-2 control-label"><g:message code="de.httc.plugin.user.email" /><span class="required-indicator">*</span>:</label>
	<div class="col-sm-10"><input type="email" name="email" value="${user?.email}" class="form-control" id="email" required></div>
</div>


<%@ page import="de.httc.plugins.user.Profile" %>
<%@ page import="de.httc.plugins.taxonomy.Taxonomy" %>

<div class="form-group ${hasErrors(bean: user.profile, field: 'firstName', 'error')} required">
	<label for="firstName" class="col-sm-2 control-label"><g:message code="de.httc.plugin.user.firstName" /><span class="required-indicator">*</span>:</label>
	<div class="col-sm-10"><g:textField id="firstName" name="profile.firstName" required="" value="${user?.profile?.firstName}" class="form-control"/></div>
</div>
<div class="form-group ${hasErrors(bean: user.profile, field: 'lastName', 'error')} required">
	<label for="lastName" class="col-sm-2 control-label"><g:message code="de.httc.plugin.user.lastName" /><span class="required-indicator">*</span>:</label>
	<div class="col-sm-10"><g:textField id="lastName" name="profile.lastName" required="" value="${user?.profile?.lastName}" class="form-control"/></div>
</div>
<%--
<div class="form-group ${hasErrors(bean: user.profile, field: 'company', 'error')} required">
	<label for="company" class="col-sm-2 control-label"><g:message code="de.httc.plugin.user.company" />:</label>
	<div class="col-sm-10">
		<input id="company" type="text" list="companies" name="profile.company" value="${user?.profile?.company}" class="form-control">
		<datalist id="companies">
			<g:each in="${Profile.executeQuery("select distinct p.company from Profile p")}">
				<option value="${it}">
			</g:each>
		</datalist>
	</div>
</div>
--%>
<g:set var="companiesTaxonomy" value="${Taxonomy.findByLabel("companies")}" />
<g:if test="${companiesTaxonomy?.termCount > 0}">
	<div class="form-group ${hasErrors(bean: user.profile, field: 'company', 'error')} ">
		<label for="company" class="col-sm-2 control-label">
			<g:message code="de.httc.plugin.user.company" />:
		</label>
		<div class="col-sm-10">
			<g:render template="/taxonomies/termSelect" plugin="httcTaxonomy" model="${[name:"profile.company", terms:companiesTaxonomy.children, selectedValues:user?.profile?.company*.id, placeholder:" ", multiple:false]}" />
		</div>
	</div>
</g:if>
<g:set var="organisationsTaxonomy" value="${Taxonomy.findByLabel("organisations")}" />
<g:if test="${organisationsTaxonomy?.termCount > 0}">
	<div class="form-group ${hasErrors(bean: user.profile, field: 'organisations', 'error')} ">
		<label for="organisations" class="col-sm-2 control-label">
			<g:message code="de.httc.plugin.taxonomy.label.organisations" />:
		</label>
		<div class="col-sm-10">
			<g:render template="/taxonomies/termSelect" plugin="httcTaxonomy" model="${[name:"profile.organisations", terms:organisationsTaxonomy.children, selectedValues:user?.profile?.organisations*.id, placeholder:" ", multiple:true]}" />
		</div>
	</div>
</g:if>
<div class="form-group ${hasErrors(bean: user.profile, field: 'phone', 'error')} ">
	<label for="phone" class="col-sm-2 control-label"><g:message code="de.httc.plugin.user.phone" />:</label>
	<div class="col-sm-10"><input type="tel" name="profile.phone" value="${user?.profile?.phone}" class="form-control" id="phone"></div>
</div>
<div class="form-group ${hasErrors(bean: user.profile, field: 'mobile', 'error')} ">
	<label for="mobile" class="col-sm-2 control-label"><g:message code="de.httc.plugin.user.mobile" />:</label>
	<div class="col-sm-10"><input type="tel" name="profile.mobile" value="${user?.profile?.mobile}" class="form-control" id="mobile"></div>
</div>
<div class="form-group ${hasErrors(bean: user.profile, field: 'photo', 'error')} ">
	<label for="photo" class="col-sm-2 control-label"><g:message code="de.httc.plugin.user.photo" />:</label>
	<div class="col-sm-10">
		<input type="hidden" name="_deletePhoto" id="deletePhoto" value="false">
		<g:if test="${user.profile?.photo?.length > 0}">
			<div id="avatar-container">
				<img class="avatar pull-left" src="data:image/png;base64,${user.profile.photo.encodeBase64().toString()}">

				<button type="button" class="delete btn btn-danger" title="Bild löschen" onclick="$('#avatar-container').remove(); $('#deletePhoto').val('true');" style="margin-left:10px"><i class="fa fa-times"></i></button>
			</div>
		</g:if>
		<input type="file" name="_photo" class="form-control" id="photo">
	</div>
</div>

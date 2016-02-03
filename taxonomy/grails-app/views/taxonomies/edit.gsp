<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="admin">
		<g:set var="entityName" value="${message(code: 'de.httc.plugin.taxonomy.taxonomy', default: 'Taxonomy')}" />
		<g:set var="entitiesName" value="${message(code: 'de.httc.plugin.taxonomy.taxonomies', default: 'Taxonomies')}" />
		<title><g:message code="default.edit.label" args="[entityName]" /></title>
	</head>
	<body>
		<ol class="breadcrumb">
			<li><g:link uri="/admin"><g:message code="default.admin.label" default="Administration" /></g:link></li>
			<li><g:link class="list" action="list" namespace="admin">${entitiesName}</g:link></li>
			<li class="active"><g:message code="default.edit.label" args="[entityName]" /></li>
		</ol>
		<form action="${createLink(action:'update', id:taxonomy?.id, namespace:'admin')}" method="post" role="form" class="taxonomy-edit form-horizontal">
			<h1 class="page-header">
				<g:message code="default.edit.label" args="[entityName]" />
				<button class="save btn btn-success pull-right" onclick="saveTaxonomy()"><i class="fa fa-cloud-upload"></i> <g:message code="default.button.update.label" default="Update" /></button>
			</h1>
			<g:hasErrors bean="${taxonomy}">
				<ul class="errors alert alert-danger" role="alert">
					<g:eachError bean="${taxonomy}" var="error">
					<li <g:if test="${error in org.springframework.validation.FieldError}">data-field-id="${error.field}"</g:if>><g:message error="${error}"/></li>
					</g:eachError>
				</ul>
			</g:hasErrors>
			<g:hiddenField name="version" value="${taxonomy?.version}" />
			<input type="hidden" name="data" id="data">
			<g:render template="form" />
		</form>
		<g:render template="terms" model="${[terms:taxonomy?.terms, mode:'edit']}" />

		<asset:stylesheet src="jstree-3.0.1.min.css"/>
		<asset:javascript src="jstree-3.0.1.min.js"/>
		<asset:script type="text/javascript">
			var tree;
			
			function termCreate() {
				var sel = tree.get_selected();
				sel = sel.length > 0 ? sel[0] : null;
				sel = tree.create_node(sel);
				if(sel) {
					tree.edit(sel);
				}
			}
			function termRename() {
				var sel = tree.get_selected();
				if(!sel.length) { return false; }
				sel = sel[0];
				tree.edit(sel);
			}
			function termRemove() {
				var sel = tree.get_selected();
				if(!sel.length) { return false; }
				tree.delete_node(sel);
			}
			function convertJsonToTree(terms) {
				$(terms).each(function(index, term) {
					term.text = term.label;
					if (term.children) {
						convertJsonToTree(term.children);
					}
				});
			}
			function convertTreeToJson() {
				var result = { terms:[] };
				var treeNodes = tree.get_json("#");
				$.each(treeNodes, function(index, node) {
					result.terms.push(convertTreeNodeToJsonRecursive(node));
				});
				return result;
			}
			function convertTreeNodeToJsonRecursive(node) {
				var result = { label:node.text };
				// only set id if the node has not been newly created, in which case the id starts with "j"
				if ((typeof node.id === "string") && node.id.length > 0 && node.id[0] != "j") {
					result.id = node.id
				}
				if (node.children && node.children.length > 0) {
					result.children = [];
					$.each(node.children, function(index, child) {
						result.children.push(convertTreeNodeToJsonRecursive(child));
					});
				}
				return result;
			}
			function saveTaxonomy() {
				$("#data").val(JSON.stringify(convertTreeToJson()));
			}
			
			$(document).ready(function() {
				$("#taxonomy").jstree({
					"core" : { "animation":0, "check_callback":true },
				    "plugins" : ["dnd"]
			    });
			    tree = $("#taxonomy").jstree(true);
		    });
		    $('#taxonomy').on("changed.jstree", function (e, data) {
		    	$("#rename-button,#delete-button").toggleClass("disabled", data.selected.length == 0);
			});
		</asset:script>
		<asset:deferredScripts/>
	</body>
</html>

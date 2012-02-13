<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="n" uri="neo"%>
<%@ taglib prefix="t" uri="template"%>

<%@ tag pageEncoding="UTF-8"%>

<%@ attribute name="name" required="true" %>
<%@ attribute name="onchange" required="false" %>
<%@ attribute name="estiloclasse" required="false" %>

<n:property name="${name}">
	<input type="text" readOnly="true" value="${n:descriptionToString(value)}" id="${name}_label"	name="${name}_label" style="width: 150px" class="${estiloclasse}"/> 
	<n:input type="hidden" id="${name}_value"  onchange="${onchange}" forceValidation="true"/>


	<button type="button" id="botaoEscolher${name}" class="botao_normal" style="width: 80px" onclick="escolherUnidadeGerencial${name}()"><c:if test="${empty value}">Escolher</c:if><c:if test="${!empty value}">Limpar</c:if></button>
	<script type="text/javascript">
		
		$(document).ready(function(){
			if(form['planoGestao'].value != "<null>"){
				if(form['${name}'].value == "" || form['${name}'].value == "<null>"){
					$("#${name}_label").removeAttr("readOnly");
				} else {
					$("#${name}_label").attr("readOnly", "true");
					$("#${name}_label").unautocomplete();
				}
			} else {
				$("#${name}_label").attr("readOnly", "true");
			}
		});
		
		$(form['planoGestao']).change(function(){
			form['${name}'].value = '<null>';
 			form['${name}_label'].value = '';
 			document.getElementById('botaoEscolher${name}').innerHTML = 'Escolher';
	 		<c:if test="${!empty onchange}">
	 			form['${name}'].onchange();
	 		</c:if>	 
		
			if(form['planoGestao'].value != "<null>"){
				$("#${name}_label").removeAttr("readOnly");
				
				$("#${name}_label").autocomplete('${pageContext.request.contextPath}/util/UnidadeGerencialAutoComplete?planoGestao='+form['planoGestao'].value, {
					width: 300,
					multiple: true,
					matchContains: true,
					formatItem: formatItem,
					formatResult: formatResult,
					max: 100
				}).result(function(event, data, formatted) {
					data[0] = stripJavascriptTags(data[0]);
					formatted = stripJavascriptTags(formatted);				
					$("#${name}_label").val(formatted);
					$("#${name}_value").val('br.com.linkcom.sgm.beans.UnidadeGerencial[id=' + data[1] + ']');
					document.getElementById('botaoEscolher${name}').innerHTML = 'Limpar';
					
					<c:if test="${!empty onchange}">
			 			form['${name}'].onchange();
			 		</c:if>
			 		$("#${name}_label").unautocomplete();
			 		$("#${name}_label").attr("readOnly", "true");	 	
				});	
			} else {
				$("#${name}_label").attr("readOnly", "true");
				
				$("#${name}_label").unautocomplete();
			}
		});
		
		
		$("#${name}_label").autocomplete('${pageContext.request.contextPath}/util/UnidadeGerencialAutoComplete?planoGestao='+form['planoGestao'].value, {
			width: 300,
			multiple: true,
			matchContains: true,
			formatItem: formatItem,
			formatResult: formatResult,
			max: 100
		}).result(function(event, data, formatted) {
			data[0] = stripJavascriptTags(data[0]);
			formatted = stripJavascriptTags(formatted);		
			$("#${name}_label").val(formatted);
			$("#${name}_value").val('br.com.linkcom.sgm.beans.UnidadeGerencial[id=' + data[1] + ']');
			document.getElementById('botaoEscolher${name}').innerHTML = 'Limpar';
			
			<c:if test="${!empty onchange}">
	 			form['${name}'].onchange();
	 		</c:if>
	 		$("#${name}_label").unautocomplete();
	 		$("#${name}_label").attr("readOnly", "true");	 	
		});
		
		function formatItem(row) {
			return stripJavascriptTags(row[0]);
		}
		function formatResult(row) {
			return stripJavascriptTags(row[0]);
		}
		
	 	function escolherUnidadeGerencial${name}(){
	 		if(document.getElementById('botaoEscolher${name}').innerHTML != 'Limpar'){
	 			if (form['planoGestao'].value != '<null>') {
		 			window.open('${pageContext.request.contextPath}/util/UnidadeGerencialTreeView?propriedade=${name}&unidadeGerencial='+form['${name}'].value+'&planoGestao='+form['planoGestao'].value, 'ugtv', 'directories=no,height=450,width=500,top=50,left=200,location=no,menubar=no,status=no,toolbar=no,resizable=yes');
		 		}
		 		else {
		 			alert('Favor selecionar o ano da gestão.');
		 		}					 		
	 		} else {
	 			form['${name}'].value = '<null>';
	 			form['${name}_label'].value = '';
	 			document.getElementById('botaoEscolher${name}').innerHTML = 'Escolher';
		 		<c:if test="${!empty onchange}">
		 			form['${name}'].onchange();
		 		</c:if>	 
		 		$("#${name}_label").removeAttr("readOnly");
		 		
		 		$("#${name}_label").autocomplete('${pageContext.request.contextPath}/util/UnidadeGerencialAutoComplete?planoGestao='+form['planoGestao'].value, {
					width: 300,
					multiple: true,
					matchContains: true,
					formatItem: formatItem,
					formatResult: formatResult,
					max: 100
				}).result(function(event, data, formatted) {
					data[0] = stripJavascriptTags(data[0]);
					formatted = stripJavascriptTags(formatted);				
					$("#${name}_label").val(formatted);
					$("#${name}_value").val('br.com.linkcom.sgm.beans.UnidadeGerencial[id=' + data[1] + ']');
					document.getElementById('botaoEscolher${name}').innerHTML = 'Limpar';
					
					<c:if test="${!empty onchange}">
			 			form['${name}'].onchange();
			 		</c:if>
			 		$("#${name}_label").unautocomplete();
			 		$("#${name}_label").attr("readOnly", "true");	 	
				});			
	 		}
	 	}
 	</script>
</n:property>
<%@page import="br.com.linkcom.sgm.service.ObjetivoEstrategicoService"%>
<%@page import="br.com.linkcom.sgm.controller.filtro.DefinicaoEstrategiaFiltro"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="n" uri="neo"%>
<%@ taglib prefix="t" uri="template"%>
<%@ taglib prefix="code" uri="code"%>
<%@ taglib prefix="f" tagdir="/WEB-INF/tags/"%>

<t:tela titulo="Elaborar mapa estratégico">
	<input type="hidden" name="reload" value="" id="idReload">
	<t:janelaFiltro>
		<t:tabelaFiltro showSubmit="false">
			<t:property name="planoGestao"	style="width: 100px;"/>
			<n:output styleClass="desc11" value="Unidade Gerencial"/>
			<n:panel>
				<f:unidadeGerencialInput name="unidadeGerencial" onchange="recarregarTela()" estiloclasse="required"/>
			</n:panel>
		</t:tabelaFiltro>		
	</t:janelaFiltro>
   
	<c:if test="${filtro.planoGestao != null && filtro.unidadeGerencial != null && !SEMPERMISSAO}">
			 	<n:bean name="filtro" valueType="<%=DefinicaoEstrategiaFiltro.class %>">
			 		<t:tabelaEntrada style="border-collapse:separate;">
						<n:panelGrid columns="2" colspan="2">
							<t:property name="unidadeGerencial.id" rows="2" cols="45" label=""write="false" type="hidden" />
							<t:property name="unidadeGerencial.mapaEstrategico.id" rows="2" cols="45" label=""write="false" type="hidden" />
							<t:property name="unidadeGerencial.mapaEstrategico.missao" mode="output" labelStyleClass="txt_und11" panelStyleClass="txt_und11"/>
							<t:property name="unidadeGerencial.mapaEstrategico.visao" rows="2" cols="130" label="Visão" labelStyleClass="txt_und11"/>
						</n:panelGrid>
						<n:panel colspan="2">
							<t:detalhe id="detalhe_perspectiva" name="unidadeGerencial.mapaEstrategico.listaPerspectivaMapaEstrategico" labelnovalinha="Nova Perspectiva"  showBotaoRemover="false" showBotaoNovaLinha="${empty HIDEBOTAOSALVAR}">
								<c:set var="indexPerspectiva" value="${index}"></c:set>
								<n:column header="" style="background-color:transparent">
									<n:body>
										<n:group columns="1" legend="Perspectiva" showBorder="true" width="100%">
											<n:panelGrid columns="2" columnStyles="width:50%,vertical-align:top" style="width:100%;" width="100%">
												<t:property name="perspectiva" label="" />
												<t:property name="ordem" label="Ordem:" style="width:20px" labelStyle="margin-left:250px;"/>
											</n:panelGrid>
											<t:property name="id" label="" write="false" type="hidden" renderAs="single" />
											<n:group columns="1" legend="Objetivos Estratégicos" showBorder="true" style="width:100%">
												<t:detalhe name="listaObjetivoMapaEstrategico" dynaline="false" showBotaoNovaLinha="false" style="width: 100%">
													<n:column header="">
														<t:property name="id" label="" write="false" type="hidden" renderAs="single" class="idObjetivoEstrategico"/>
														<t:property name="objetivoEstrategico" label=" " class="selectObjetivoEstrategico" />
													</n:column>
												</t:detalhe>
												<n:input class="btnApp" type="button" value="Novo Objetivo Estratégico" onclick="addNovaLinhaObjetivoEstrategico(this, \"${indexPerspectiva}\")" />
											</n:group>
												<t:acao>
											<c:if test="${empty renderHeaderdetalhe_perspectiva}">
													<a id="button.excluir[table_id=detalhe_perspectiva, indice=${indexPerspectiva}]" onclick="if(confirm('Tem certeza que deseja excluir este item?')){excluirLinhaPorNome(this.id,true);reindexFormPorNome(this.id, forms[0], 'unidadeGerencial.mapaEstrategico.listaPerspectivaMapaEstrategico', true);}" class="excluirdetalhe excluirPerspectiva" type="button" href="javascript:void(0);">
														<img border="0" alt="Excluir este item" src="${ctx}/images/ico_excluir.gif">
													</a>
											</c:if>
												</t:acao>
										</n:group>
									</n:body>
								</n:column>
							</t:detalhe>
						</n:panel>
					</t:tabelaEntrada>
					<c:if test="${empty HIDEBOTAOSALVAR}">
						<div style="text-align: right">
							<br>
							<input type="submit"  class="botao_normal" onclick="javascript:if(confirm('Tem certeza que deseja excluir todo o mapa estratégico?')){excluirMapa();}" value="Excluir mapa estratégico">&nbsp;
							<n:submit action="salvar" class="botao_normal" validate="true">salvar</n:submit>
						</div>
					 </c:if>
				</n:bean>	
	</c:if>
</t:tela>
<style>.dataGridHeader{background: inherit;}</style>
<script type="text/javascript">

	function addNovaLinhaObjetivoEstrategico(botaoDOM, indexPerspectiva){
		var idSelect = Math.round( Math.random()*100000000 );
		var botao = $(botaoDOM)
		//var table = botao.siblings("table").find("table").children("tbody"); //document.getElementById(idTabela);
	   	var table = botao.parent().parent().prev().find("tbody");
	   	var rowCount = table.children().size()
	   	var html = "<tr class='dataGridBody"+((rowCount+1)%2+1)+"'><td >"+getHTMLSelectObjetivoEstrategico(indexPerspectiva, rowCount, idSelect)+"</td><td>" + 
	   				"<a href='javascript:void(0);' onclick='if(confirm(\"Tem certeza que deseja excluir este objetivo estratégico?\")){excluirLinhaObjetivoEstrategico(this);} ' class='excluirdetalhe' type='button' > " + 
									"<img border='0' alt='Excluir este item' src='${ctx}/images/ico_excluir.gif'></button>"+  
	   	"</td></tr>"
	   	table.append(html);
	   	$("#"+idSelect).val(botao.prev().val())
	}
	
	function excluirLinhaObjetivoEstrategico(btnexcluir){
		var tabela = $(btnexcluir).parent().parent().parent();
		$(btnexcluir).parent().parent().remove();
		
		var campo;
		var nomeCampo;
			
		// Reordena o índice dos IDs
		var ids = tabela.find(".idObjetivoEstrategico")
		ids.each(function(i){
			campo = $(this);
			nomeCampo = campo.attr("name");
			campo.attr("name", getNomeSelectReindexado(nomeCampo, i) );			
		});
		
		// Reordena o índice dos selects de objetivo estratégico
		var selects = tabela.find(".selectObjetivoEstrategico")
		selects.each(function(i){
			campo = $(this);
			nomeCampo = campo.attr("name");
			campo.attr("name", getNomeSelectReindexado(nomeCampo, i) );
		});
		

		
	}
	
	function getNomeSelectReindexado(nome, num){
		var i = nome.lastIndexOf("[");
		var j = nome.lastIndexOf("]");
		//alert(nome.substr(0,i) + (num) + nome.substr(j))
		return nome.substr(0,i+1) + (num) + nome.substr(j)
		
	}
	
	function getHTMLSelectObjetivoEstrategico(idPai, id, idSelect){
		return '<select id="'+idSelect+'"class="required selectObjetivoEstrategico" style="" onchange="" onkeyup="$n.limpaCombo(event,this);" name="unidadeGerencial.mapaEstrategico.listaPerspectivaMapaEstrategico['+idPai+'].listaObjetivoMapaEstrategico['+id+'].objetivoEstrategico">' + 
								'<option value="&lt;null&gt;"></option>' + 
							<c:forEach items="<%=ObjetivoEstrategicoService.getInstance().findAll()%>" var="objetivoEstrategicoItem">	
								'<option value="br.com.linkcom.sgm.beans.ObjetivoEstrategico[id=${objetivoEstrategicoItem.id}]">${objetivoEstrategicoItem.descricao}</option>' + 
							</c:forEach>
			'</select>';
	}

	function recarregarTela(){
		form.validate = 'false'; 
		form.suppressErrors.value = 'true';
		form.ACAO.value = 'entrada';
		form.suppressValidation.value = 'true';
		document.getElementById('idReload').value = 'true';
		submitForm();
	}
	
	function excluirMapa (){
		form.ACAO.value ='excluir';
		form.action = "${ctx}/sgm/process/DefinicaoEstrategia";
		form.validate = 'false';
		submitForm();
	}
	
	function submitForm() {
		var validar = form.validate;
		try {validarFormulario;} 
		catch (e) {validar = false;}
		
		try {clearMessages();} catch (e) {}
		if (validar == "true") {var valid = validarFormulario();
		if (valid) {form.submit();}} else {form.submit();}
	}
	
	<%--Função reescrita para ordenar corretamente o detalhe pai--%>
	function reindex(form, removedIndexedProperty){
		if(form==null){
			alert("reindex(): O form fornecido ? null   \n\n@author rogelgarcia");
			return;
		}
		if(removedIndexedProperty==null){
			alert("reindex(): O removedIndexedProperty fornecido ? null   \n\n@author rogelgarcia");
			return;
		} else {
			if(!removedIndexedProperty.match("\\w*\\[\\d*\\]")){
				alert("reindex(): O removedIndexedProperty fornecido ? inv?lido ("+removedIndexedProperty+")\nO formato deve ser propriedade[indice]");
				return;
			}
		}
		//alert('reindexing '+removedIndexedProperty);
		var property = removedIndexedProperty.substring(0,removedIndexedProperty.lastIndexOf("["));
		var excludedNumber = extrairNumeroDeIndexedProperty(removedIndexedProperty);
		//alert('property '+property);
		//alert('excludedNumber '+excludedNumber);
		for(i = 0; i < form.elements.length; i++){
			var element = form.elements[i];
			if(element.name == null) continue;
			//alert(element.name + " " + (element.name.match(property+"\\[\\d*\\].*")));
			
			var elementReducedProperty = element.name;
			var indexBrackets = null;
			var liorp = elementReducedProperty.indexOf("[");
			if(liorp > 0){
				elementReducedProperty = elementReducedProperty.substring(0,liorp);
				indexBrackets = element.name.substring(element.name.indexOf('['), element.name.indexOf(']')+1);
			}
			
			if(elementReducedProperty == property){
				var elementName = elementReducedProperty + indexBrackets;
				//alert('before '+element.name);
				var elementSubproperties = element.name.substring(elementName.length, element.name.length);
				//alert(indexBrackets);
				var open = elementName.indexOf("[");
				var close = elementName.indexOf("]");
				var number = extrairNumeroDeIndexedProperty(elementName);
				//alert(number);
				if(number>excludedNumber){
					number--;
					var reindexedName = elementName.substring(0,open)+"["+number+"]"+ elementSubproperties;
					//alert(element.name + " -> "+reindexedName);
					element.name = reindexedName;
					//alert('after'+element.name);
				}
			}
		}
	}
			
	<%--Como o Neo gerava um erro ao passar a variavel indexPerspectiva, o incremento dela é feito via JavaScript--%>
	function corrigeIndexPerspectiva(){
		$(".excluirPerspectiva").each(function(k){
			var obj = $(this)
			var id = obj.attr("id");
			var i = id.indexOf("indice=")+7;
			var j = id.lastIndexOf("]")-1
			var strId = id.substr(0,i) + (k+1) + "]" ;
			obj.attr("id", strId);
		});
		
		$(".selectObjetivoEstrategico").each(function() {
			var child = $(this).parent().next().children("a"); 
			child.attr("href", "javascript:void(0)");
			child.unbind('click');
			child.removeAttr("onclick");
			child.click(function() {
				if(confirm('Tem certeza que deseja excluir este objetivo estratégico?')) {
					excluirLinhaObjetivoEstrategico(this);
				}
			});
		});		
	}
			
	$(function(){
		corrigeIndexPerspectiva();
		$("#detalhe_perspectivaButton").click(function() {
			corrigeIndexPerspectiva();
		});
		corrigeIndexPerspectiva();
	});
			
</script>
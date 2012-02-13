<%@page import="br.com.linkcom.sgm.beans.UnidadeGerencial"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="n" uri="neo"%>
<%@ taglib prefix="t" uri="template"%>
<%@ taglib prefix="code" uri="code"%>
<%@ taglib prefix="f" tagdir="/WEB-INF/tags/"%>

<t:tela titulo="Definição de Competências">
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
   	<br>
	<c:if test="${filtro.planoGestao != null && filtro.unidadeGerencial != null && !SEMPERMISSAO}">
	 	<n:bean name="unidadeGerencial" valueType="<%=UnidadeGerencial.class %>">
		 	<t:janelaEntrada showSubmit="false">
		 		<t:tabelaEntrada title="Definição de competências" style="border-collapse:separate;">
		 			<n:panelGrid columns="2" colspan="2" >
						<t:property name="id" write="false" type="hidden" label=" "/>
						<t:property name="mapaCompetencia.id" write="false" type="hidden" label=" "/>
						<t:property name="mapaCompetencia.missao" mode="output" labelStyleClass="txt_und11" panelStyleClass="txt_und11"/>
					</n:panelGrid>
					<n:panel colspan="2"><BR><BR></n:panel>
					<n:group colspan="2" legend="Atividades" style="width:100%">
						<t:detalhe name="mapaCompetencia.atividades" labelnovalinha="Nova atividade" showBotaoNovaLinha="${empty HIDEBOTAOSALVAR}">
							<n:column header="Descrição">
								<n:body>
									<t:property name="descricao" style="width:100%"/>
									<t:property name="id" type="hidden"/>
								</n:body>								
							</n:column>
						</t:detalhe>
					</n:group>
					<n:group colspan="2" legend="Competências organizacionais" style="width:100%">
							<t:detalhe name="mapaCompetencia.competencias" id="detalhe_competencia" labelnovalinha="Nova competência" showBotaoNovaLinha="${empty HIDEBOTAOSALVAR}">
								<n:column header="Competência">
									<n:body>
										<n:panelGrid columns="2">
											<t:propertyConfig showLabel="false"> 
												<t:property name="competenciaOrganizacional" style="width:250px" onchange="ajaxCompetencia(this)"/>
												<t:property name="competenciaOrganizacional.descricao" mode="input" style="border:none; background-color: transparent; overflow: auto;" rows="3" cols="120" disabled="true"/>
												<t:property name="id" type="hidden"/>
											</t:propertyConfig>
										</n:panelGrid>
									</n:body>								
								</n:column>
							</t:detalhe>
					</n:group>
				</t:tabelaEntrada>
			</t:janelaEntrada>					
			<c:if test="${empty HIDEBOTAOSALVAR}">
				<div style="text-align: right">
					<br>
					<n:submit action="salvar" class="botao_normal" validate="true">salvar</n:submit>
				</div>
			</c:if>
		</n:bean>	
	</c:if>
</t:tela>

<script type="text/javascript">

	var elementoDescricaoCompetencia;
	
	function recarregarTela(){
		form.validate = 'false'; 
		form.suppressErrors.value = 'true';
		form.ACAO.value = 'entrada';
		form.suppressValidation.value = 'true';
		document.getElementById('idReload').value = 'true';
		submitForm();
	}
	
	function verifyElement(elementName) {
		 $("#detalhe_competencia textarea").each(function(){
		      if (this.name == elementName) {
		      	elementoDescricaoCompetencia = this;
		      }
		 });
	}	
	
	function ajaxCompetencia(el) {
		var campo_CompDesc = el.getAttribute("name") + ".descricao";
		verifyElement(campo_CompDesc);
		callbackFunction = showDescricao;		
		sendRequest('${ctx}/sgm/Ajaxcompetenciaorganizacional?id='+el.value);
	}

	function showDescricao(data){
		eval(data);
		elementoDescricaoCompetencia.value = desc;
	}	

</script>
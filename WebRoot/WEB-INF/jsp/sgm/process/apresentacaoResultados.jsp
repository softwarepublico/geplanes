<%@page import="br.com.linkcom.sgm.beans.Indicador"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="n" uri="neo"%>
<%@ taglib prefix="t" uri="template"%>
<%@ taglib prefix="f" tagdir="/WEB-INF/tags"%>

<t:tela titulo="Apresentação de Resultados">
	<input type="hidden" name="reload" value="" id="idReload">
	<t:janelaFiltro>
			<t:tabelaFiltro showSubmit="false" columnStyles="width:15%,width:85%">
				<t:property name="planoGestao" style="width: 150px"/>			
				<n:output styleClass="desc11" value="Unidade Gerencial"/>
				<n:panel>
					<t:property name="dataInicial" type="hidden" write="false" showLabel="false"/>				
					<t:property name="dataFinal" type="hidden" write="false" showLabel="false"/>
					<t:property name="tipoResultado" type="hidden" write="false" showLabel="false"/>
					<t:property name="indicadorSelecionado" type="hidden" write="false" showLabel="false"/>
					<f:unidadeGerencialInput name="unidadeGerencial"  onchange="reloadTela()" estiloclasse="required"/>
				</n:panel>
				
				<c:if test="${filtro.unidadeGerencial != null && empty acessoNaoAutorizado}">
					<n:panelGrid colspan="2" id="divIndicador" width="100%" cellpadding="1">
						<n:panelGrid width="100%" style="text-align:center">
							<n:output styleClass="desc14bold" value="Indicadores" style="text:align:center"/>
						</n:panelGrid>
						<n:panelGrid width="100%" columnStyles="width:15px, width:100%" style="text-align:center">
							<n:output styleClass="desc" value=""/>
							<t:propertyConfig mode="output">
								<n:dataGrid property="listaIndicadores" headerStyleClass="txt_tit" bodyStyleClasses="txt_l1, txt_l2" styleClass="fd_tabela1" varIndex="index" var="indicador">
									<c:choose>
										<c:when test="${filtro.indicadorSelecionado != null && filtro.indicadorSelecionado.id == indicador.id}">
											<t:property name="nome" bodyStyle="font-weight:bold;background-color:#FFFF99"/>
											<t:property name="melhor" bodyStyle="font-weight:bold;background-color:#FFFF99"/>
											<t:property name="frequencia" bodyStyle="font-weight:bold;background-color:#FFFF99"/>
											<n:column header="Ação" style="width:50px;text-align:center;background-color:#FFFF99">
												<input type="image" src="../../images/ico_grafico.png" border="0" style="border:none" onclick="form.ACAO.value ='exibirResultado';if (validaSelecao(${indicador.id})) {form.action = '${ctx}/sgm/process/ApresentacaoResultados?idIndicadorSelecionado=${indicador.id}'; form.validate = 'true'; submitForm()}" onmouseover='Tip("Gerar gráfico")'>
											</n:column>												
										</c:when>
										<c:otherwise>
											<t:property name="nome" />
											<t:property name="melhor"/>
											<t:property name="frequencia"/>
											<n:column header="Ação" style="width:50px;text-align:center">
												<input type="image" src="../../images/ico_grafico.png" border="0" style="border:none" onclick="form.ACAO.value ='exibirResultado';if (validaSelecao(${indicador.id})) {form.action = '${ctx}/sgm/process/ApresentacaoResultados?idIndicadorSelecionado=${indicador.id}'; form.validate = 'true'; submitForm()}" onmouseover='Tip("Gerar gráfico")'>
											</n:column>										
										</c:otherwise>
									</c:choose>
								</n:dataGrid>
							</t:propertyConfig>						
						</n:panelGrid>					
					</n:panelGrid>
					<n:panelGrid columns="2" columnStyles="width:50%, width:50%" width="100%" cellpadding="1">
						<n:panel>
							<fieldset style="height:80px">
								<legend>TIPO DE GRÁFICO</legend>
								<n:panelGrid columns="2" style="width:390px" columnStyles="vertical-align:middle">
									<c:forEach items="${mapaTipoGrafico}" var="item">
										<c:choose>
											<c:when test="${filtro.tipoGrafico.name == item.key}">
												<input type="radio" class="radioClass" value="${item.key}" name="tipoGrafico" checked="checked" style="border:none">&nbsp;${item.value}
											</c:when>
											<c:otherwise>
												<input type="radio" class="radioClass" value="${item.key}" name="tipoGrafico" style="border:none">&nbsp;${item.value}
											</c:otherwise>
										</c:choose>
										<br>			
									</c:forEach>			
								</n:panelGrid>
							</fieldset>
						</n:panel>					
						<n:panel>
							<fieldset style="height:80px">
								<legend>FORMA DE APRESENTAÇÃO</legend>
								<n:panelGrid columns="2" style="width:390px" columnStyles="vertical-align:middle">
									<c:forEach items="${mapaFormaApresentacao}" var="item">
										<c:choose>
											<c:when test="${filtro.formaApresentacao.name == item.key}">
												<input type="radio" class="radioClass" value="${item.key}" name="formaApresentacao" checked="checked" style="border:none">&nbsp;${item.value}
											</c:when>
											<c:otherwise>
												<input type="radio" class="radioClass" value="${item.key}" name="formaApresentacao" style="border:none">&nbsp;${item.value}
											</c:otherwise>
										</c:choose>									
										<br>			
									</c:forEach>			
								</n:panelGrid>
							</fieldset>
						</n:panel>
					</n:panelGrid>
				</c:if>

			</t:tabelaFiltro>

			<c:if test="${exibirResultado}">			
				<c:choose>
					<c:when test="${semDados}">
						<script language="javascript">alert('Nenhuma informação foi encontrada de acordo com o filtro selecionado.');</script>
					</c:when>
					<c:otherwise>
						<br>
						<div class="painel_resultado">
							<br>
							<div align="center">
								<img src="${ctx}/GRAFICO?graficoTipo=${filtro.tipoGrafico.name}&graficoApresentacao=${filtro.formaApresentacao.name}">							
							</div>					
							<br>
							<c:if test="${tipoPercentual}">			
								<n:dataGrid itens="${listaIndicadorSelecionado}" var="indicador" headerStyleClass="dataGridHeader" bodyStyleClasses="dataGridBody1, dataGridBody2" styleClass="dataGrid" style="margin-left: 20px; margin-right: 20px;">
									<n:bean name="indicador" propertyPrefix="listaIndicadorSelecionado" valueType="<%=Indicador.class%>">
										<n:column header="${tituloIndicador}">
											<t:property name="nome"/>
										</n:column>
					
										<c:forEach var="epoca" items="${listaEpoca}" varStatus="status">
												<n:column header="${epoca}" align="center">
													<c:if test="${!empty indicador.acompanhamentosIndicador[status.index].percentualReal}">
														<n:output value="${indicador.acompanhamentosIndicador[status.index].percentualReal}" pattern="##0.00"/>%									
													</c:if>
												</n:column>				
										</c:forEach>
									</n:bean>
								</n:dataGrid>
							</c:if>
							
							<c:if test="${tipoAcompanhamento}">					
								<n:panelGrid columns="${n:size(listaEpoca) + 1}" cellspacing="1" styleClass="fd_tabela1" style="margin-left: 20px; margin-right: 20px;" rowStyleClasses="txt_l1" columnStyleClasses="txt_l1">
									<n:panel class="dataGridHeader" style="text-align:center"></n:panel>
									<c:forEach var="epoca" items="${listaEpoca}">
										<n:panel class="dataGridHeader" style="text-align:center">${epoca}</n:panel>
									</c:forEach>
									<n:panel class="dataGridHeader">Indicador</n:panel>
									<c:forEach var="epoca" items="${listaEpoca}">
										<c:if test="${melhorDoIndicador.name == 'MELHOR_CIMA'}">
											<n:panel class="dataGridHeader" style="text-align:center">Meta : Real</n:panel>
										</c:if>
										<c:if test="${melhorDoIndicador.name == 'MELHOR_ENTRE_FAIXAS'}">
											<n:panel class="dataGridHeader" style="text-align:center">Lim.Inf. : Real : Lim.Sup.</n:panel>
										</c:if>
										<c:if test="${melhorDoIndicador.name == 'MELHOR_BAIXO'}">
											<n:panel class="dataGridHeader" style="text-align:center">Meta : Real</n:panel>
										</c:if>										
									</c:forEach>
									<c:forEach var="indicador" items="${listaIndicadorSelecionado}">
										<n:panel>${indicador.nome}</n:panel>
										<c:forEach var="epoca" items="${listaEpoca}" varStatus="status">
											<n:panel align="center">
												
												<c:if test="${melhorDoIndicador.name == 'MELHOR_CIMA'}">
													<c:if test="${!empty indicador.acompanhamentosIndicador[status.index].valorLimiteSuperior}">
														<n:output value="${indicador.acompanhamentosIndicador[status.index].valorLimiteSuperior}" pattern="##0.00"/>									
													</c:if>							
													:
													<c:if test="${!empty indicador.acompanhamentosIndicador[status.index].valorReal}">
														<n:output value="${indicador.acompanhamentosIndicador[status.index].valorReal}" pattern="##0.00"/>									
													</c:if>
												</c:if>
												<c:if test="${melhorDoIndicador.name == 'MELHOR_ENTRE_FAIXAS'}">
													<c:if test="${!empty indicador.acompanhamentosIndicador[status.index].valorLimiteInferior}">
														<n:output value="${indicador.acompanhamentosIndicador[status.index].valorLimiteInferior}" pattern="##0.00"/>																						
													</c:if>							
													:
													<c:if test="${!empty indicador.acompanhamentosIndicador[status.index].valorReal}">
														<n:output value="${indicador.acompanhamentosIndicador[status.index].valorReal}" pattern="##0.00"/>									
													</c:if>
													:																			
													<c:if test="${!empty indicador.acompanhamentosIndicador[status.index].valorLimiteSuperior}">
														<n:output value="${indicador.acompanhamentosIndicador[status.index].valorLimiteSuperior}" pattern="##0.00"/>									
													</c:if>												
												</c:if>
												<c:if test="${melhorDoIndicador.name == 'MELHOR_BAIXO'}">
													<c:if test="${!empty indicador.acompanhamentosIndicador[status.index].valorLimiteInferior}">
														<n:output value="${indicador.acompanhamentosIndicador[status.index].valorLimiteInferior}" pattern="##0.00"/>																						
													</c:if>							
													:
													<c:if test="${!empty indicador.acompanhamentosIndicador[status.index].valorReal}">
														<n:output value="${indicador.acompanhamentosIndicador[status.index].valorReal}" pattern="##0.00"/>									
													</c:if>
												</c:if>											
							
											</n:panel>
										</c:forEach>							
									</c:forEach>
								</n:panelGrid>
							</c:if>
							
							<c:if test="${tipoFarol}">			
								<n:dataGrid itens="${listaIndicadorSelecionado}" var="indicador" headerStyleClass="txt_tit" bodyStyleClasses="txt_l1, txt_l2" styleClass="fd_tabela1" style="margin-left: 20px; margin-right: 20px;" >
									<n:bean name="indicador" propertyPrefix="listaIndicadorSelecionado" valueType="<%=Indicador.class%>">
										<n:column header="Indicador">
											<t:property name="nome"/>
										</n:column>				
										<n:column header="Valor não cadastrado" align="center">
											<n:output value="${indicador.numFarois[0]}"/>
										</n:column>
										<n:column header="Meta não cumprida" align="center">
											<n:output value="${indicador.numFarois[1]}"/>
										</n:column>
										<n:column header="Meta cumprida parcialmente" align="center">
											<n:output value="${indicador.numFarois[2]}"/>
										</n:column>
										<n:column header="Meta cumprida em 100%" align="center">
											<n:output value="${indicador.numFarois[3]}"/>
										</n:column>
										<n:column header="Meta não aplicável" align="center">
											<n:output value="${indicador.numFarois[4]}"/>
										</n:column>									
									</n:bean>										
								</n:dataGrid>
							</c:if>
							<br>
						</div>
						<br>
						<div align="right">
							<n:submit url="/sgm/report/ApresentacaoResultados" action="gerar" class="botao_normal">Imprimir</n:submit>							
						</div>
					</c:otherwise>
				</c:choose>
			</c:if>
	</t:janelaFiltro>							
</t:tela>

<script language="javascript">

	$(document).ready(function() {

		$('input:radio[name="tipoGrafico"]').click(function() {
			filtraFormaApresentacao();
			exibeRelatorio();
		});
		
		$('input:radio[name="tipoGrafico"]').change(function() {
			filtraFormaApresentacao();
			exibeRelatorio();
		});
		
		$('input:radio[name="formaApresentacao"]').click(function() {
			exibeRelatorio();
		});
		
		$('input:radio[name="formaApresentacao"]').change(function() {
			exibeRelatorio();
		});
		
		<c:if test="${filtro.indicadorSelecionado != null}">
			filtraFormaTipoGrafico(${filtro.indicadorSelecionado.id});
		</c:if>
		filtraFormaApresentacao();
	});
	
	function reloadTela(){
		form.action = '${ctx}/sgm/process/ApresentacaoResultados';		
		form.validate = 'false'; 
		form.suppressErrors.value = 'true';
		form.ACAO.value = 'inicializar';
		form.suppressValidation.value = 'true';
		document.getElementById('idReload').value = 'true';
		submitForm();
	}
	
	function exibeRelatorio() {		
		<c:if test="${exibirResultado}">
			form.action = '${ctx}/sgm/process/ApresentacaoResultados';		
			form.validate = 'false'; 
			form.suppressErrors.value = 'true';
			form.ACAO.value = 'exibirResultado';
			form.suppressValidation.value = 'true';
			submitForm();
		</c:if>
	}
	
	function validaSelecao(idIndicadorSelecionado) {
		if ($('input:radio[name="tipoGrafico"]:checked').val() == null && $('input:radio[name="formaApresentacao"]:checked').val() == null) {
			alert('Favor selecionar o tipo de gráfico e a forma de apresentação!');
			return false;
		}
		if ($('input:radio[name="tipoGrafico"]:checked').val() == null) {
			alert('Favor selecionar o tipo de gráfico!');
			return false;
		}
		if ($('input:radio[name="formaApresentacao"]:checked').val() == null) {
			alert('Favor selecionar a forma de apresentação!');
			return false;
		}
		
		filtraFormaTipoGrafico(idIndicadorSelecionado);
				
		return true;
	}
	
	function filtraFormaTipoGrafico(idIndicadorSelecionado) {
		if (idIndicadorSelecionado != null) {
			if (idIndicadorSelecionado == 0) {
				$('input:radio[name="formaApresentacao"][value!="PIZZA"]').removeAttr('disabled');
				$('input:radio[name="tipoGrafico"][value!="PERCENTUAL"]').attr('disabled','disabled');
				$('input:radio[name="tipoGrafico"][value="PERCENTUAL"]').attr('checked','checked');
				
				if ($('input:radio[name="formaApresentacao"][value="PIZZA"]').attr('checked')) {
					$('input:radio[name="formaApresentacao"][value="LINHA"]').attr('checked','checked');
				}				
			}
			else {
				$('input:radio[name="tipoGrafico"][value!="PERCENTUAL"]').removeAttr('disabled');
			}
		}
	}
	
	function filtraFormaApresentacao() {
		if ($('input:radio[name="tipoGrafico"]:checked').val() != null) {
			if ($('input:radio[name="tipoGrafico"]:checked').val() == "ACOMPANHAMENTO") {
				if ($('input:radio[name="formaApresentacao"]:checked').val() == null || $('input:radio[name="formaApresentacao"]:checked').val() == "PIZZA") {							
					$('input:radio[name="formaApresentacao"][value="LINHA"]').attr('checked','checked');
				}			
				$('input:radio[name="formaApresentacao"][value!="PIZZA"]').removeAttr('disabled');
				$('input:radio[name="formaApresentacao"][value="PIZZA"]').attr('disabled','disabled');
			}
			if ($('input:radio[name="tipoGrafico"]:checked').val() == "PERCENTUAL") {
				if ($('input:radio[name="formaApresentacao"]:checked').val() == null || $('input:radio[name="formaApresentacao"]:checked').val() == "PIZZA") {							
					$('input:radio[name="formaApresentacao"][value="LINHA"]').attr('checked','checked');
				}			
				$('input:radio[name="formaApresentacao"][value!="PIZZA"]').removeAttr('disabled');
				$('input:radio[name="formaApresentacao"][value="PIZZA"]').attr('disabled','disabled');
			}
			if ($('input:radio[name="tipoGrafico"]:checked').val() == "FAROL") {
				$('input:radio[name="formaApresentacao"][value!="PIZZA"]').attr('disabled','disabled');
				$('input:radio[name="formaApresentacao"][value="PIZZA"]').removeAttr('disabled');
				$('input:radio[name="formaApresentacao"][value="PIZZA"]').attr('checked','checked');			
			}
		}
	}
	
		
</script>
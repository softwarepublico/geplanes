<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="n" uri="neo"%>
<%@ taglib prefix="t" uri="template"%>
<%@ taglib prefix="code" uri="code"%>
<%@ taglib prefix="f" tagdir="/WEB-INF/tags/"%>

<t:tela titulo="LANÇAMENTO DE ${VALORES_REAIS}">

	<t:janelaFiltro>
		<input type="hidden" name="reload" value="" id="idReload">
		<t:propertyConfig showLabel="true" renderAs="double">
			<n:group columns="2" showBorder="false" >
				<t:property name="planoGestao" mode="input" style="width: 100px;"/>
				<n:output styleClass="desc11" value="Unidade Gerencial"/>
				<n:panel>
					<f:unidadeGerencialInput name="unidadeGerencial" onchange="recarregarTela()" estiloclasse="required"/>
				</n:panel>
			</n:group>
		</t:propertyConfig>
	</t:janelaFiltro>
	
	<c:if test="${showTela && filtro.planoGestao != null && filtro.unidadeGerencial != null}">
	
		<c:if test="${!empty filtro.listaPerspectivaMapaEstrategico}">
			<div style="text-align: right;" class="txt_normal">
				<input type="hidden" value="${filtro.alternar}" name="alternar"/>
				<c:if test="${filtro.alternar}">
					Valores acumulados&nbsp;&nbsp;
				</c:if>
				<c:if test="${!filtro.alternar}">
					Valores apurados&nbsp;&nbsp;
				</c:if>
				<n:submit action="alternarValores" type="button" class="botao_normal">Alternar</n:submit>
			</div>			
		</c:if>
		
		<br>
		<n:bean name="filtro">
			<c:if test="${ empty filtro.listaPerspectivaMapaEstrategico}">
				<span class="txt_normal">
					Não existem indicadores cadastrados para esta unidade gerencial neste ${plano_de_gestao}.
				</span>
				<br><br>
			</c:if>
			<t:janelaResultados>
				<n:dataGrid property="listaPerspectivaMapaEstrategico" styleClass="fd_tabela1" bodyStyleClasses="txt_l1, txt_l1" varIndex="indexPerspectivaMapaEstrategico" var="perspectivaMapaEstrategico">
					<n:column style="width: 100px;">
						<n:panelGrid columns="1">
							<n:panel>
								<img src="../../images/ico_pc_perspectiva.gif"/>
								<t:property name="descricao" />
							</n:panel>
						</n:panelGrid>
					</n:column>
					<n:column>
						<n:dataGrid property="listaObjetivoMapaEstrategico" styleClass="fd_tabela1" bodyStyleClasses="txt_l1, txt_l1" varIndex="indexObjetivoMapaEstrategico" var="objetivoMapaEstrategico" style="width: 830px;">
							<n:column>
								<n:panel>
									<img src="../../images/ico_pc_objetivoMapaEstrategico.gif"/>
									<t:property name="descricao" />
								</n:panel>
								
								<n:dataGrid property="listaIndicador" styleClass="fd_tabela2" bodyStyleClasses="txt_l1" style="margin-left: 20px; width: 815px" var="indicador" varIndex="indexIndicador">
									<n:column>
										<n:panelGrid columns="2" style="width: 100%" columnStyles=" , text-align: right;">
											<n:panel>
												<t:propertyConfig mode="input">
													<t:property name="id"					type="hidden"/>
													<t:property name="melhor"				type="hidden"/>
													<t:property name="status"				type="hidden"/>
													<t:property name="nome"					type="hidden" />
													<t:property name="peso"					type="hidden"/>
													<c:if test='${!empty indicador.unidadeMedida.sigla}'>
														<t:property name="unidadeMedida.sigla"	type="hidden"/>
													</c:if>
												</t:propertyConfig>
												
												<img src="../../images/ico_pc_ind_${indicador.melhor.name}.gif"/>
												<span title="${indicador.descricao}"> <t:property name="nome"/> (<t:property name="unidadeMedida" /> X <t:property name="precisao" />)</span>
												<n:output value="Peso: " styleClass="desc" />
													<c:choose>
														<c:when test='${!indicador.cancelado}'>
															<t:property name="peso"/>
														</c:when>
														<c:otherwise>
															-
														</c:otherwise>
													</c:choose>
											</n:panel>
											<c:if test='${!indicador.cancelado}'>													
												<n:panel>
													<button type="button" class="botao_normal"  onclick="popUpIndicador(${indicador.id});"/>detalhamento do indicador</button>
												</n:panel>
											</c:if>
										</n:panelGrid>
										
										<c:choose>
											<c:when test='${indicador.podeCancelar}'>
												<c:if test='${!indicador.cancelado && !indicador.emCancelamento}'>
													<button type="button" class="botao_normal" style="margin: 5px; width: 180px;"  onclick="popUpSolicitacaoCancelamentoIndicador(${indicador.id});"/>Solicitar cancelamento</button>
												</c:if>
												<c:if test='${indicador.emCancelamento}'>
													<div class="botao_desabilitado" style="margin: 5px; width: 250px;">Cancelamento aguardando aprovação</div>
												</c:if>
												<c:if test='${indicador.cancelado}'>
													<div class="botao_desabilitado" style="margin: 5px; width: 100px;">Cancelado</div>
												</c:if>											
											</c:when>
											<c:otherwise>
												<div class="botao_desabilitado" style="margin: 5px; width: 480px;">Você não tem permissão para solicitar o cancelamento deste indicador.</div>
											</c:otherwise>
										</c:choose>	
											
										<BR>
										
										<c:choose>
											<c:when test='${indicador.podeRepactuar}'>
												<c:if test='${!indicador.cancelado}'>
													<c:if test='${!indicador.repactuacaoEmAprovacao}'>
														<button type="button" class="botao_normal" style="margin: 5px; width: 180px;"  onclick="popUpSolicitacaoRepactuacaoIndicador(${indicador.id});"/>Solicitar repactuação</button>
													</c:if>
													<c:if test='${indicador.repactuacaoEmAprovacao}'>
														<div class="botao_desabilitado" style="margin: 5px; width: 250px;">Repactuação aguardando aprovação</div>
													</c:if>
												</c:if>
											</c:when>
											<c:otherwise>
												<div class="botao_desabilitado" style="margin: 5px; width: 480px;">Você não tem permissão para solicitar a repactuação deste indicador.</div>
											</c:otherwise>
										</c:choose>										
										
										
										<c:set value="width: 60px;" var="fieldSize" scope="request" />
										
										<div style="margin-left: 20px; margin-bottom: 10px; width: 750px; overflow: auto;">													
											<c:if test="${!indicador.cancelado}">														
												<c:if test="${empty indicador.acompanhamentosIndicador}">
													<br>
														Não foi feito o cadastro de ${valores_de_base} para este indicador.
													<br>
													<br>
												</c:if>
												<c:if test="${!empty indicador.acompanhamentosIndicador}">
													<table class="fd_tabela3" cellspacing="1">
														<tr class="txt_tit">
															<td style="width: 88px;">&nbsp;</td>
															<c:forEach items="${indicador.acompanhamentosIndicador}" var="acompanhamentoIndicador" varStatus="indexAcompanhamento">
																<n:bean name="acompanhamentoIndicador" propertyPrefix="listaPerspectivaMapaEstrategico[${indexPerspectivaMapaEstrategico}].listaObjetivoMapaEstrategico[${indexObjetivoMapaEstrategico}].listaIndicador[${indexIndicador}].acompanhamentosIndicador[${indexAcompanhamento.index}]">
																	<td style="width: 50px;">
																		<t:property name="epoca" mode="output"/>
																		
																		<t:property name="id" 								type="hidden"/>
																		<n:property name="dtLembLancRes">
																			<n:input type="hidden" name="${name}" value="${value}" />
																		</n:property>
																		<t:property name="dataInicial" 						type="hidden"/>
																		<t:property name="dataFinal" 						type="hidden"/>
																		<t:property name="indice" 							type="hidden"/>
																		<t:property name="epoca" 							type="hidden"/>
																	</td>
																</n:bean>
															</c:forEach>
														</tr>
														
														<c:if test="${!filtro.alternar}">
															<c:if test="${indicador.melhor.name == 'MELHOR_CIMA' || indicador.melhor.name == 'MELHOR_ENTRE_FAIXAS'}">
																<tr class="txt_l2">
																	<c:if test="${indicador.melhor.name == 'MELHOR_CIMA'}">
																		<td>Meta</td>
																	</c:if>
																	<c:if test="${indicador.melhor.name == 'MELHOR_ENTRE_FAIXAS'}">
																		<td>Lim. Sup.</td>
																	</c:if>
																	<c:forEach items="${indicador.acompanhamentosIndicador}" var="acompanhamentoIndicador" varStatus="indexAcompanhamento">
																		<n:bean name="acompanhamentoIndicador" propertyPrefix="listaPerspectivaMapaEstrategico[${indexPerspectivaMapaEstrategico}].listaObjetivoMapaEstrategico[${indexObjetivoMapaEstrategico}].listaIndicador[${indexIndicador}].acompanhamentosIndicador[${indexAcompanhamento.index}]" >
																			<td class="txt_cC" style="padding: 1px;">
																				<c:choose>
																					<c:when test="${acompanhamentoIndicador.naoaplicavel}">
																						<span class="txt_normal10">Não aplicável</span>
																					</c:when>
																					<c:otherwise>																						
																						<c:choose>
																							<c:when test="${acompanhamentoIndicador.podeMostrar}">
																								<t:property name="valorLimiteSuperior" mode="input" type="hidden" write="false"/>
																								<t:property name="valorLimiteSuperiorAsString" mode="output"/>
																							</c:when>
																							<c:otherwise>
																								<span class="msg_erro9">Cadastro incompleto</span>
																							</c:otherwise>																					
																						</c:choose>																						
																					</c:otherwise>
																				</c:choose>
																			</td>
																		</n:bean>
																	</c:forEach>
																</tr>
															</c:if>
															<tr class="txt_l1">
																<td>Realizado</td>
																<c:forEach items="${indicador.acompanhamentosIndicador}" var="acompanhamentoIndicador" varStatus="indexAcompanhamento">
																	<n:bean name="acompanhamentoIndicador" propertyPrefix="listaPerspectivaMapaEstrategico[${indexPerspectivaMapaEstrategico}].listaObjetivoMapaEstrategico[${indexObjetivoMapaEstrategico}].listaIndicador[${indexIndicador}].acompanhamentosIndicador[${indexAcompanhamento.index}]" >
																		<td class="txt_cC" style="padding: 1px;">
																			<c:choose>
																				<c:when test="${acompanhamentoIndicador.naoaplicavel}">
																					<span>-</span>
																				</c:when>
																				<c:otherwise>
																					<c:choose>
																						<c:when test="${acompanhamentoIndicador.podeMostrar}">
																							<c:choose>
																								<c:when test="${acompanhamentoIndicador.podeAlterar}">
																									<t:property name="valorReal" mode="input" style="${fieldSize}" />																											
																								</c:when>
																								<c:otherwise>
																									<t:property name="valorReal" mode="input" type="hidden" write="false"/>
																									<t:property name="valorRealAsString" mode="output"/>
																								</c:otherwise>
																							</c:choose>
																						</c:when>
																						<c:otherwise>
																							<span>-</span>
																						</c:otherwise>																					
																					</c:choose>
																				</c:otherwise>																			
																			</c:choose>
																		</td>
																	</n:bean>
																</c:forEach>
															</tr>
															<c:if test="${ indicador.melhor.name == 'MELHOR_BAIXO' || indicador.melhor.name == 'MELHOR_ENTRE_FAIXAS' }">
																<tr class="txt_l2">
																	<c:if test="${indicador.melhor.name == 'MELHOR_BAIXO'}">
																		<td>Meta</td>
																	</c:if>
																	<c:if test="${indicador.melhor.name == 'MELHOR_ENTRE_FAIXAS'}">
																		<td>Lim. Inf.</td>
																	</c:if>														
																	<c:forEach items="${indicador.acompanhamentosIndicador}" var="acompanhamentoIndicador" varStatus="indexAcompanhamento">
																		<n:bean name="acompanhamentoIndicador" propertyPrefix="listaPerspectivaMapaEstrategico[${indexPerspectivaMapaEstrategico}].listaObjetivoMapaEstrategico[${indexObjetivoMapaEstrategico}].listaIndicador[${indexIndicador}].acompanhamentosIndicador[${indexAcompanhamento.index}]" >
																			<td class="txt_cC" style="padding: 1px;">
																				<c:choose>
																					<c:when test="${acompanhamentoIndicador.naoaplicavel}">
																						<span class="txt_normal10">Não aplicável</span>
																					</c:when>
																					<c:otherwise>
																						<c:choose>
																							<c:when test="${acompanhamentoIndicador.podeMostrar}">
																								<t:property name="valorLimiteInferior" mode="input" type="hidden" write="false"/>
																								<t:property name="valorLimiteInferiorAsString" mode="output"/>
																							</c:when>
																							<c:otherwise>
																								<span class="msg_erro9">Cadastro incompleto</span>
																							</c:otherwise>																					
																						</c:choose>																					
																					</c:otherwise>
																				</c:choose>																				
																			</td>
																		</n:bean>
																	</c:forEach>
																</tr>
															</c:if>
														</c:if>
														<c:if test="${filtro.alternar}">
															<c:if test="${ indicador.melhor.name == 'MELHOR_CIMA' || indicador.melhor.name == 'MELHOR_ENTRE_FAIXAS' }">
																<tr class="txt_l1">
																	<c:if test="${indicador.melhor.name == 'MELHOR_CIMA'}">
																		<td>Meta ACU</td>
																	</c:if>
																	<c:if test="${indicador.melhor.name == 'MELHOR_ENTRE_FAIXAS'}">
																		<td>Lim. Sup. ACU</td>
																	</c:if>																	
																	<c:forEach items="${indicador.acompanhamentosIndicador}" var="acompanhamentoIndicador" varStatus="indexAcompanhamento">
																		<n:bean name="acompanhamentoIndicador" propertyPrefix="listaPerspectivaMapaEstrategico[${indexPerspectivaMapaEstrategico}].listaObjetivoMapaEstrategico[${indexObjetivoMapaEstrategico}].listaIndicador[${indexIndicador}].acompanhamentosIndicador[${indexAcompanhamento.index}]" >
																			<td class="txt_cC" style="padding: 1px;">
																				<c:choose>
																					<c:when test="${acompanhamentoIndicador.naoaplicavel}">
																						<span class="txt_normal10">Não aplicável</span>
																					</c:when>
																					<c:otherwise>
																						<t:property name="valorLimiteSuperiorAcumulado" mode="input" type="hidden" write="false"/>
																						<t:property name="valorLimiteSuperiorAcumuladoAsString" mode="output"/>
																					</c:otherwise>
																				</c:choose>
																			</td>
																		</n:bean>
																	</c:forEach>															
																</tr>
															</c:if>
															<tr class="txt_l2">
																<td>Realizado ACU</td>
																<c:forEach items="${indicador.acompanhamentosIndicador}" var="acompanhamentoIndicador" varStatus="indexAcompanhamento">
																	<n:bean name="acompanhamentoIndicador" propertyPrefix="listaPerspectivaMapaEstrategico[${indexPerspectivaMapaEstrategico}].listaObjetivoMapaEstrategico[${indexObjetivoMapaEstrategico}].listaIndicador[${indexIndicador}].acompanhamentosIndicador[${indexAcompanhamento.index}]" >
																		<td class="txt_cC" style="padding: 1px;">
																			<c:choose>
																				<c:when test="${acompanhamentoIndicador.naoaplicavel}">
																					<span>-</span>
																				</c:when>
																				<c:otherwise>
																					<t:property name="valorRealAcumulado" mode="input" type="hidden" write="false"/>
																					<t:property name="valorRealAcumuladoAsString" mode="output"/>
																				</c:otherwise>
																			</c:choose>																		
																		</td>
																	</n:bean>
																</c:forEach>
															</tr>
															<c:if test="${ indicador.melhor.name == 'MELHOR_BAIXO' || indicador.melhor.name == 'MELHOR_ENTRE_FAIXAS' }">
																<tr class="txt_l1">
																	<c:if test="${indicador.melhor.name == 'MELHOR_BAIXO'}">
																		<td>Meta ACU</td>
																	</c:if>
																	<c:if test="${indicador.melhor.name == 'MELHOR_ENTRE_FAIXAS'}">
																		<td>Lim. Inf. ACU</td>
																	</c:if>
																	<c:forEach items="${indicador.acompanhamentosIndicador}" var="acompanhamentoIndicador" varStatus="indexAcompanhamento">
																		<n:bean name="acompanhamentoIndicador" propertyPrefix="listaPerspectivaMapaEstrategico[${indexPerspectivaMapaEstrategico}].listaObjetivoMapaEstrategico[${indexObjetivoMapaEstrategico}].listaIndicador[${indexIndicador}].acompanhamentosIndicador[${indexAcompanhamento.index}]" >
																			<td class="txt_cC" style="padding: 1px;">
																				<c:choose>
																					<c:when test="${acompanhamentoIndicador.naoaplicavel}">
																						<span class="txt_normal10">Não aplicável</span>
																					</c:when>
																					<c:otherwise>
																						<t:property name="valorLimiteInferiorAcumulado" mode="input" type="hidden" write="false"/>
																						<t:property name="valorLimiteInferiorAcumuladoAsString" mode="output"/>
																					</c:otherwise>
																				</c:choose>																			
																			</td>
																		</n:bean>
																	</c:forEach>															
																</tr>
															</c:if>
														</c:if>
														
														<c:if test="${usuarioPodeCriarAcaoPreventiva}">																
															<tr class="txt_l1">
																<td>Ação Preventiva</td>
																<c:forEach items="${indicador.acompanhamentosIndicador}" var="acompanhamentoIndicador" varStatus="indexAcompanhamento">
																	<n:bean name="acompanhamentoIndicador" propertyPrefix="listaPerspectivaMapaEstrategico[${indexPerspectivaMapaEstrategico}].listaObjetivoMapaEstrategico[${indexObjetivoMapaEstrategico}].listaIndicador[${indexIndicador}].acompanhamentosIndicador[${indexAcompanhamento.index}]" >																	
																		<td class="txt_cC" style="padding: 1px;">
																			<c:choose>
																				<c:when test="${acompanhamentoIndicador.naoaplicavel}">
																					<span>-</span>
																				</c:when>
																				<c:otherwise>
																					<c:choose>
																						<c:when test="${!empty acompanhamentoIndicador.acaoPreventiva}">
																							<n:link action="tratarAcaoPreventiva" parameters="id=${acompanhamentoIndicador.id}" type="button" class="botao_normal">tratar</n:link>
																						</c:when>
																						<c:otherwise>
																							<n:link action="tratarAcaoPreventiva" parameters="id=${acompanhamentoIndicador.id}&idUgOrigem=${filtro.unidadeGerencial.id}" type="button" class="botao_normal">criar</n:link>
																						</c:otherwise>
																					</c:choose>
																				</c:otherwise>
																			</c:choose>
																		</td>											
																	</n:bean>
																</c:forEach>
															</tr>
														</c:if>
														<c:if test="${usuarioPodeCriarAnomalia}">																
															<tr class="txt_l1">
																<td>Ação Corretiva</td>
																<c:forEach items="${indicador.acompanhamentosIndicador}" var="acompanhamentoIndicador" varStatus="indexAcompanhamento">
																	<n:bean name="acompanhamentoIndicador" propertyPrefix="listaPerspectivaMapaEstrategico[${indexPerspectivaMapaEstrategico}].listaObjetivoMapaEstrategico[${indexObjetivoMapaEstrategico}].listaIndicador[${indexIndicador}].acompanhamentosIndicador[${indexAcompanhamento.index}]" >																	
																		<td class="txt_cC" style="padding: 1px;">
																			<c:choose>
																				<c:when test="${acompanhamentoIndicador.naoaplicavel}">
																					<span>-</span>
																				</c:when>
																				<c:otherwise>
																					<c:choose>
																						<c:when test="${!empty acompanhamentoIndicador.anomalia}">
																							<n:link action="tratarAnomalia" parameters="id=${acompanhamentoIndicador.id}" type="button" class="botao_normal">tratar</n:link>
																						</c:when>
																						<c:otherwise>
																							<n:link action="tratarAnomalia" parameters="id=${acompanhamentoIndicador.id}&idUgOrigem=${filtro.unidadeGerencial.id}" type="button" class="botao_normal">criar</n:link>
																						</c:otherwise>
																					</c:choose>
																				</c:otherwise>
																			</c:choose>																		
																		</td>											
																	</n:bean>
																</c:forEach>
															</tr>
														</c:if>
													</table>
												</c:if>
											</c:if>
										</div>
									</n:column>							
								</n:dataGrid>
							</n:column>							
						</n:dataGrid>					
					</n:column>
				</n:dataGrid>
				<c:if test="${showBotaoSalvar}">
					<div style="text-align: right;">
						<br>
						<n:submit action="salvar" class="botao_normal" validate="true">salvar</n:submit>
					</div>
				</c:if>
			</t:janelaResultados>
		</n:bean>
	</c:if>
</t:tela>

<script type="text/javascript">
	function recarregarTela(){
		form.validate = 'false'; 
		form.suppressErrors.value = 'true';
		form.ACAO.value = 'entrada';
		form.suppressValidation.value = 'true';
		document.getElementById('idReload').value = 'true';
		submitForm();
	}

	function popUpIndicador(indicadorID) {
		var width = 700;
	    var height = 500;
	
	    var left = 99;
	    var top = 99;
	
	    window.open('${ctx}/sgm/process/LancamentoValorReal?ACAO=popUpIndicador&indicadorID='+indicadorID,'indicador', 'width='+width+', height='+height+', top='+top+', left='+left+', scrollbars=yes, status=no, toolbar=no, location=no, directories=no, menubar=no, resizable=no, fullscreen=no');
	}

	function popUpSolicitacaoCancelamentoIndicador(indicadorID) {
		var width = 500;
	    var height = 400;
	
	    var left = 99;
	    var top = 99;
	    window.open('${ctx}/sgm/process/LancamentoValorReal?ACAO=popUpSolicitacaoCancelamentoIndicador&indicadorID='+indicadorID,'solicitacao_cancelamento_indicador', 'width='+width+', height='+height+', top='+top+', left='+left+', scrollbars=yes, status=no, toolbar=no, location=no, directories=no, menubar=no, resizable=no, fullscreen=no');
	}	
	
	function popUpSolicitacaoRepactuacaoIndicador(indicadorID) {
		var width = 500;
	    var height = 400;
	
	    var left = 99;
	    var top = 99;
	    window.open('${ctx}/sgm/process/LancamentoValorReal?ACAO=popUpSolicitacaoRepactuacaoIndicador&indicadorID='+indicadorID,'solicitacao_repactuacao_indicador', 'width='+width+', height='+height+', top='+top+', left='+left+', scrollbars=yes, status=no, toolbar=no, location=no, directories=no, menubar=no, resizable=no, fullscreen=no');
	}	

	function popUpAnexo(indicadorID) {
		var width = 500;
	    var height = 400;
	
	    var left = 99;
	    var top = 99;
	
	    window.open('${ctx}/sgm/process/LancamentoValorReal?ACAO=popUpAnexo&indicadorID='+indicadorID,'anexo_do_indicador', 'width='+width+', height='+height+', top='+top+', left='+left+', scrollbars=yes, status=no, toolbar=no, location=no, directories=no, menubar=no, resizable=no, fullscreen=no');
	}

</script>
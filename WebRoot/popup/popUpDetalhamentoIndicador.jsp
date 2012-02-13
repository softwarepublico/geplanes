<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="n" uri="neo"%>
<%@ taglib prefix="t" uri="template" %>

<html>
	<head>
		<n:head/>
		<script language="JavaScript" src="${ctx}/javascript/ajquery.js"></script>
		<script language="JavaScript" src="${ctx}/javascript/jquery.maskedinput-1.1.1.pack.js"></script>
		<script language="JavaScript" src="${ctx}/javascript/dimensions.pack.js"></script>
		<script language="JavaScript" src="${ctx}/javascript/dimmer.js"></script>
		<script language="JavaScript" src="${ctx}/javascript/treetable.js"></script>
		<script language="JavaScript" src="${ctx}/javascript/GeplanesUtil.js"></script>
		<script language="JavaScript" src="${ctx}/javascript/thickbox-compressed.js"></script>
		<script language="JavaScript" src="${ctx}/javascript/jquery.bgiframe.min.js"></script>
		<script language="JavaScript" src="${ctx}/javascript/jquery.autocomplete.js"></script>
		<script language="JavaScript" src="${ctx}/javascript/jquery.maskedinput-1.1.1.js"></script>
		<script language="JavaScript" src="${ctx}/javascript/jquery.ajaxQueue.js"></script>
		<script language="JavaScript" src="${ctx}/javascript/akModal.js"></script>		
	</head>
	<body leftmargin="0" topmargin="0" style="padding:0px;background-image:none;overflow:auto;height:270px;width:99%;background-color:#F5F5F5;" bgColor="#FFFFFF" 
	style="background-color: #FFFFFF" onload="inicializaCampos(${indicador.idxPerspectivaMapaEstrategico}, ${indicador.idxObjetivoMapaEstrategico}, ${indicador.idxIndicador})">
			<n:messages />
				<t:entrada titulo="DETALHAMENTO DO INDICADOR" showListagemLink="false" showSaveLink="false" formEnctype="multipart/form-data">
					<t:property name="idxPerspectivaMapaEstrategico" write="false" type="hidden"/>
					<t:property name="idxObjetivoMapaEstrategico" write="false" type="hidden"/>
					<t:property name="idxIndicador" write="false" type="hidden"/>
					<t:property name="id" write="false" type="hidden"/>
					<t:property name="peso" write="false" type="hidden"/>
					<t:property name="status" write="false" type="hidden"/>
					<t:property name="unidadeGerencial" write="false" type="hidden"/>
					<t:property name="unidadeGerencial.planoGestao" write="false" type="hidden"/>
					<t:property name="unidadeGerencial.planoGestao.anoExercicio" write="false" type="hidden"/>
					<t:janelaEntrada submitAction="salvarPopUpDetalhamentoIndicador" showSubmit="false">
						<t:tabelaEntrada>
							<t:property name="objetivoMapaEstrategico" style="width: 500px;" write="true" type="hidden"/>
							<t:property name="nome"				style="width: 300px; overflow:hidden;" />
							<t:property name="unidadeMedida"	style="width: 130px;"/>
							<t:property name="descricao"				rows="4" style="width:550px;"/>
							<t:property name="responsavel"				style="width: 200px;" />
							<t:property name="relevancia"				style="width: 100px;"/>
							<t:property name="frequenciaAcompanhamento"	style="width: 100px;" />
							<t:property name="mecanismoControle"		rows="4" style="width:550px;"/>
							<t:property name="fonteDados"				rows="4" style="width:550px;"/>
							<t:property name="formulaCalculo"			rows="4" style="width:550px;"/>
						</t:tabelaEntrada>
						
						<n:panel title="Metas">
							<c:set var="fieldSize" value="width: 60px;" scope="request" />
							<t:tabelaEntrada>
								<t:property name="frequencia"		style="width: 130px;" onchange="alteraFrequenciaIndicador('${indicador.frequencia}')"/>
								<t:property name="melhor"			style="width: 130px;" onchange="configuraLimites(this)" id="idMelhor"/>
								<t:property name="precisao"			style="width: 130px;"/>
								<t:property name="tolerancia"		style="width: 130px;"/>
							</t:tabelaEntrada>
								<br>
								<c:choose>
									<c:when test="${indicador.frequencia == null}">
										<span class="msg_erro12">&nbsp;Favor selecionar a frequência de lançamento do indicador.</span>
									</c:when>
									<c:otherwise>
										<table class="fd_tabela3" cellspacing="1" style="font-size:12px;">												
											<!-- CABEÇALHO -->							
											<tr class="txt_tit">
												<td style="width: 80px;">&nbsp;</td>
												<c:forEach items="${indicador.acompanhamentosIndicador}" var="acompanhamentoIndicador" varStatus="indexAcompanhamento">
													<n:bean name="acompanhamentoIndicador" propertyPrefix="acompanhamentosIndicador[${indexAcompanhamento.index}]">
														<td style="width: 50px;">
															<t:property name="epoca" mode="output"/>
															<t:property name="id" 								type="hidden"/>
															<t:property name="valorReal"						type="hidden"/>																							
															<t:property name="dtLembLancRes" 					type="hidden" write="false"/>
															<t:property name="dataInicial" 						type="hidden"/>
															<t:property name="dataFinal" 						type="hidden"/>
															<t:property name="indice" 							type="hidden"/>
															<t:property name="epoca" 							type="hidden"/>
														</td>
													</n:bean>
												</c:forEach>
											</tr>
											
											<!-- LIMITE SUPERIOR -->
											<tr class="txt_l2" id="trLimiteSuperior" style="display:none">
												<td id="tdLimiteSuperior" style="text-align:center">Lim. Sup.</td>
												<c:forEach items="${indicador.acompanhamentosIndicador}" var="acompanhamentoIndicador" varStatus="indexAcompanhamento">
													<n:bean name="acompanhamentoIndicador" propertyPrefix="acompanhamentosIndicador[${indexAcompanhamento.index}]">
														<td class="txt_cC" style="padding: 1px;">
															<c:choose>
																<c:when test="${dataExpirada && !usuarioAdministrador}">
																	<t:property name="valorLimiteSuperior" mode="input" type="hidden" write="true"/>
																</c:when>
																<c:otherwise>
																	<c:choose>
																		<c:when test="${!podeRegistrarMeta}">
																			<t:property name="valorLimiteSuperior" mode="input" type="hidden" write="true"/>
																		</c:when>
																		<c:otherwise>
																			<t:property name="valorLimiteSuperior" mode="input" style="${fieldSize}" />
																		</c:otherwise>
																	</c:choose>																							
																</c:otherwise>
															</c:choose>
														</td>
													</n:bean>
												</c:forEach>
											</tr>
											
											<!-- LIMITE INFERIOR -->
											<tr class="txt_l2" id="trLimiteInferior" style="display:none">
												<td id="tdLimiteInferior" style="text-align:center">Lim. Inf.</td>
												<c:forEach items="${indicador.acompanhamentosIndicador}" var="acompanhamentoIndicador" varStatus="indexAcompanhamento">
													<n:bean name="acompanhamentoIndicador" propertyPrefix="acompanhamentosIndicador[${indexAcompanhamento.index}]">
														<td class="txt_cC" style="padding: 1px;">
															<c:choose>
																<c:when test="${dataExpirada && !usuarioAdministrador}">
																	<t:property name="valorLimiteInferior" mode="input" type="hidden" write="true"/>
																</c:when>
																<c:otherwise>
																	<c:choose>
																		<c:when test="${!podeRegistrarMeta}">
																			<t:property name="valorLimiteInferior" mode="input" type="hidden" write="true"/>
																		</c:when>
																		<c:otherwise>
																			<t:property name="valorLimiteInferior" mode="input" style="${fieldSize}" />
																		</c:otherwise>
																	</c:choose>																							
																</c:otherwise>
															</c:choose>
														</td>
													</n:bean>
												</c:forEach>
											</tr>
											
											<!-- NÃO SE APLICA -->
											<tr class="txt_l2" id="trNaoSeAplica">
												<td id="tdNaoSeAplica" style="text-align:center">Não aplicável</td>
												<c:forEach items="${indicador.acompanhamentosIndicador}" var="acompanhamentoIndicador" varStatus="indexAcompanhamento">
													<n:bean name="acompanhamentoIndicador" propertyPrefix="acompanhamentosIndicador[${indexAcompanhamento.index}]">
														<td class="txt_cC" style="padding: 1px;">
															<c:choose>
																<c:when test="${dataExpirada && !usuarioAdministrador}">
																	<t:property name="naoaplicavel" mode="input" type="hidden" write="true"/>
																</c:when>
																<c:otherwise>
																	<c:choose>
																		<c:when test="${!podeRegistrarMeta}">
																			<t:property name="naoaplicavel" mode="input" type="hidden" write="true"/>
																		</c:when>
																		<c:otherwise>
																			<t:property name="naoaplicavel" mode="input" onclick="controlaExibicaoLimites(this.name, this.checked)"/>
																		</c:otherwise>
																	</c:choose>																							
																</c:otherwise>
															</c:choose>
														</td>
													</n:bean>
												</c:forEach>
											</tr>
										</table>
									</c:otherwise>
								</c:choose>
						</n:panel>
						
						<t:detalhe name="anexosIndicador" labelnovalinha="Novo anexo">
							<n:column header="Nome">
								<t:property name="id" write="false" type="hidden"/>
								<t:property name="nome" style="width:98%" bodyStyle="white-space:nowrap"/>
							</n:column>
							<t:property name="descricao" rows="2" cols="30" style="width:98%"/>
							<t:property name="arquivo" showRemoverButton="false" style="overflow:hidden;"/>
						</t:detalhe>						
						
					</t:janelaEntrada>		
					<table class="form_filtro_pesquisar">
						<tr>
							<td style="text-align: right;">		
								<n:panel colspan="2" style="text-align: right;">
									<n:submit action="salvarPopUpDetalhamentoIndicador" validate="true" class="botao_normal">salvar</n:submit>
								</n:panel>
							</td>
						</tr>
					</table>
				</t:entrada>
	</body>
</html>

<script type="text/javascript">

	$(document).ready(function() {
		$('input[type=checkbox][name$="naoaplicavel"]').each(function(){
			controlaExibicaoLimites($(this).attr("name"),$(this).attr("checked"));
		});
	});			


	function inicializaCampos(idxPerspectivaMapaEstrategico, idxObjetivoMapaEstrategico, idxIndicador) {
		if(${erroMsg == null}){
			var strLista = 'listaPerspectivaMapaEstrategico[' + idxPerspectivaMapaEstrategico + '].listaObjetivoMapaEstrategico[' + idxObjetivoMapaEstrategico + '].listaIndicador[' + idxIndicador + ']';
			var docOpener = window.opener.document;
		
			form['peso'].value = docOpener.getElementsByName(strLista + '.peso')[0].value != '<null>' && docOpener.getElementsByName(strLista + '.peso')[0].value != '' ? docOpener.getElementsByName(strLista + '.peso')[0].value : '0';
			form['nome'].value = docOpener.getElementsByName(strLista + '.nome')[0].value;
			form['melhor'].value = docOpener.getElementsByName(strLista + '.melhor')[0].value;
			
			$("#idMelhor").change();
		}
	}
	
	function capWords(str){
		var newStr = '';
	   for (i=0 ; i < str.length ; i++){
	       if(i == 0){
	       	newStr += str.substring(i,i+1).toUpperCase();
	       } else {
	       	newStr += str.substring(i,i+1).toLowerCase();
	       }
	   }         
	   return newStr;
	}
	
	function alteraFrequenciaIndicador(frequenciaIndicadorAtual) {
		if (frequenciaIndicadorAtual == '' || confirm('ATENÇÃO! A alteração da frequência do indicador implicará na perda dos valores de meta e resultado lançados até então.')) {
			form.validate = 'false'; 
			form.suppressErrors.value = 'true';
			form.ACAO.value ='popUpDetalhamentoIndicador';
			form.action = '${ctx}/sgm/process/DistribuicaoPesosIndicadores?reloadTela=true';
			form.suppressValidation.value = 'true';	
			submitForm();
		}
		else {
			form['frequencia'].value = frequenciaIndicadorAtual.toUpperCase();
		}
	}
	
	function configuraLimites(melhorDoIndicadorAtual) {
		if (melhorDoIndicadorAtual.value != '<null>') {
			if (melhorDoIndicadorAtual.value == 'MELHOR_CIMA') {
				$("#trLimiteSuperior").show();
				$("#tdLimiteSuperior").html("Meta");
				$("#trLimiteInferior").hide();
			}
			else if (melhorDoIndicadorAtual.value == 'MELHOR_ENTRE_FAIXAS') {
				$("#trLimiteSuperior").show();
				$("#tdLimiteSuperior").html("Lim. Sup.");
				$("#trLimiteInferior").show();
				$("#tdLimiteInferior").html("Lim. Inf.");
			}
			else if (melhorDoIndicadorAtual.value == 'MELHOR_BAIXO') {
				$("#trLimiteSuperior").hide();
				$("#trLimiteInferior").show();
				$("#tdLimiteInferior").html("Meta");
			}
		}
		else {
			$("#trLimiteSuperior").hide();
			$("#trLimiteInferior").hide();
		}
	}
	
	function controlaExibicaoLimites(nomeCampoCheck, campoCheckMarcado) {
	
		var re = /acompanhamentosIndicador\[(\d+)\]\.naoaplicavel/;
		var casamento;
		var idx = -1;
		casamento = re.exec(nomeCampoCheck);
		
		if (casamento) {
			idx = casamento[1];
			
			var $campoLimiteInferior = $('input:text[name="acompanhamentosIndicador\[' + idx + '\].valorLimiteInferior"]');
			var $campoLimiteSuperior = $('input:text[name="acompanhamentosIndicador\[' + idx + '\].valorLimiteSuperior"]');
			if (campoCheckMarcado == true) {
				$campoLimiteInferior.attr('disabled','disabled');
				$campoLimiteInferior.val("");
				$campoLimiteSuperior.attr('disabled','disabled');
				$campoLimiteSuperior.val("");
			}
			else {
				$campoLimiteInferior.removeAttr('disabled');
				$campoLimiteSuperior.removeAttr('disabled');
			}
		}
	}
</script>
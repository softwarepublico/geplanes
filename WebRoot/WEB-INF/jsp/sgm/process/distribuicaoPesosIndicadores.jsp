<%@page import="br.com.linkcom.neo.controller.crud.CrudController"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="n" uri="neo"%>
<%@ taglib prefix="t" uri="template"%>
<%@ taglib prefix="f" tagdir="/WEB-INF/tags/"%>

<t:tela titulo="Painel de indicadores">
	<t:janelaFiltro>
		<input type="hidden" name="reload" value="" id="idReload">

		<t:propertyConfig showLabel="true" renderAs="double">
			<n:group columns="2" showBorder="false" columnStyles="width:40%,width:60%" style="width:100%">
				
				<n:group columns="2" showBorder="false">
					<t:property name="planoGestao" mode="input" style="width: 100px;"/>
				
					<n:output styleClass="desc11" value="Unidade Gerencial"/>
					<n:panel>
						<f:unidadeGerencialInput name="unidadeGerencial" onchange="recarregarTela()" estiloclasse="required"/>
					</n:panel>
				</n:group>
				
				<n:group columns="1" showBorder="false" style="width:100%">
					<n:panel style="text-align:right">
						<c:if test="${showTela and !empty filtro.planoGestao and !empty filtro.unidadeGerencial}">
							<n:link id="idFiltro" class="v9n" url="javascript:void(0);" onclick="abrirConfiguracaoFiltro();">Configurar filtro de objetivos estratégicos</n:link>
						</c:if>
					</n:panel>
				</n:group>				
				
			</n:group>
		</t:propertyConfig>

			<c:if test="${showTela and !empty filtro.planoGestao and !empty filtro.unidadeGerencial}">
				
				<TABLE class="fd_tabela1" width='100%' cellspacing='1'>
					<TR class="txt_tit" >
						<TD style="width: 40px">Perspectiva</TD>
						<TD colspan="2">&nbsp;</TD>
					</TR>

					<n:property name="listaPerspectivaMapaEstrategico" varValue="listaPerspectivaMapaEstrategico">

						<%-- Lista as perspectivas --%>

						<c:forEach items="${listaPerspectivaMapaEstrategico}" var="perspectiva" varStatus="status">
							<n:bean name="perspectiva" propertyPrefix="listaPerspectivaMapaEstrategico" propertyIndex="${status.index}">
								
								<TR class="txt_l1" >
									<TD style="width:120px">
										<t:property name="id" mode="input" write="false" type="hidden"/>
										<t:property name="descricao" mode="input" write="true" type="hidden"/>
									</TD>
									<TD colspan="2">
										<TABLE class="fd_tabela1" width='100%' cellspacing='1'>
											<TR class="txt_tit" >
												<TD style="width: 120px">Objetivo Estratégico</TD>
												<TD>Iniciativas e Indicadores</TD>
											</TR>										
											<c:forEach items="${perspectiva.listaObjetivoMapaEstrategico}" var="objetivoMapaEstrategico" varStatus="status2">
												<n:bean name="objetivoMapaEstrategico" propertyPrefix="listaPerspectivaMapaEstrategico[${status.index}].listaObjetivoMapaEstrategico" propertyIndex="${status2.index}">
													<TR class="txt_l1" >
														<TD style="width:120px">
															<t:property name="id" mode="input" write="false" type="hidden"/>
															<t:property name="descricao" mode="input" write="true" type="hidden"/>
														</TD>
														<TD style="padding: 5px;">
															<span class="desc12bold">Iniciativas</span><br>
															<t:propertyConfig showLabel="false">
																<c:if test="${showTela and showButton}">
																	<t:detalhe name="listaIniciativa" showColunaAcao="true" labelnovalinha="Adicionar iniciativa">
																		<t:property name="descricao" style="width:500px"/>
																		<t:acao ladoDireito="false">
																			<t:propertyConfig renderAs="single">																																								
																				<n:property name="id">
																					<n:input type="hidden"/>
																				</n:property>
																			</t:propertyConfig>																		
																			<img src="../../images/ico_planoacao.png" onclick="popUpPlanoAcao(this)" style="cursor:pointer" alt="Plano de ação" onmouseover="Tip('Plano de ação')"/>
																		</t:acao>
																	</t:detalhe>
																</c:if>
																<c:if test="${showTela and !showButton}">
																	<t:detalhe name="listaIniciativa" showColunaAcao="true" showBotaoNovaLinha="false" showBotaoRemover="false">
																		<t:property name="descricao" type="hidden" write="true"/>
																		<t:acao ladoDireito="false">
																			<t:propertyConfig renderAs="single">																																								
																				<n:property name="id">
																					<n:input type="hidden"/>
																				</n:property>
																			</t:propertyConfig>																		
																			<img src="../../images/ico_planoacao.png" onclick="popUpPlanoAcao(this)" style="cursor:pointer" alt="Plano de ação" onmouseover="Tip('Plano de ação')"/>
																		</t:acao>
																	</t:detalhe>
																</c:if>
															</t:propertyConfig>
															<br>
															<span class="desc12bold">Indicadores</span><br>
															<t:propertyConfig showLabel="false">
																<c:if test="${showTela and showButton}" >
																	<t:detalhe name="listaIndicador" showBotaoRemover="false" showBotaoNovaLinha="false">
																		<c:set var="detalheTableId" value="${Tdetalhe.tableId}"/>							
																		<c:set var="detalheTableOnNewLine" value="${Tdetalhe.onNewLine}"/>													
																		<c:choose>
																			<c:when test="${!indicador.cancelado}">
																				<t:property name="peso" style="width: 30px" headerStyle="width:30px" bodyStyle="width:30px"/>
																				<t:property name="nome" style="width: 450px"/>
																				<t:property name="melhor" style="width: 100px"/>
																				<t:acao>
																					<t:propertyConfig renderAs="single">																																								
																						<n:property name="id">
																							<n:input type="hidden"/>
																						</n:property>
																						<n:property name="status">
																							<n:input type="hidden"/>
																						</n:property>
																					</t:propertyConfig>
																					<table border="0" cellpadding="0" cellspacing="0">
																						<tr>
																							<td>
																								<img src="../../images/ico_editar.gif" onclick="popUpDetalhamentoIndicador(this)" style="cursor:pointer" alt="Detalhar indicador" onmouseover="Tip('Detalhar indicador')"/>
																							</td>
																							<td>
																								<img src="../../images/ico_excluir.gif" 
																								onclick="if (confirm('Deseja realmente excluir esse registro?')){excluirLinhaPorNome(this.id,true);reindexFormPorNome(this.id, forms[0], '${Tdetalhe.fullNestedName}', true);}" 
																								id="button.excluir[table_id=${Tdetalhe.tableId}, indice=${rowIndex}]" 
																								style="cursor:pointer;" 
																								alt="Excluir"/>
																							</td>
																						</tr>
																					</table>
																				</t:acao>
																			</c:when>
																			<c:otherwise>
																				<t:property name="peso" style="width: 30px" headerStyle="width: 30px" label="Peso" write="true" type="hidden" bodyStyleClass="cancelado"/>
																				<t:property name="nome" style="width: 450px" write="true" type="hidden" colspan="4" bodyStyleClass="cancelado"/>
																				<t:property name="melhor" style="width: 100px" write="true" type="hidden" bodyStyleClass="cancelado"/>
																				<t:acao>
																					<t:propertyConfig renderAs="single">
																						<n:property name="id">
																							<n:input type="hidden"/>
																						</n:property>
																						
																						<n:property name="status">
																							<n:input type="hidden"/>
																						</n:property>
																					</t:propertyConfig>
																					<img src="../../images/ico_excluir.gif" 
																							onclick="excluirLinhaPorNome(this.id,true);reindexFormPorNome(this.id, forms[0], '${Tdetalhe.fullNestedName}', true);" 
																							id="button.excluir[table_id=${Tdetalhe.tableId}, indice=${rowIndex}]" 
																							style="cursor:pointer;" 
																							alt="Excluir"/>
																				</t:acao>															
																			</c:otherwise>
																		</c:choose>
																	</t:detalhe>
																	<button type="button" class="btnApp" id="${detalheTableId}Button" onclick="newLine${detalheTableId}();${detalheTableOnNewLine}">
																		Adicionar Indicador
																	</button>
																	<n:hasAuthorization url="/util/crud/Indicador" action="<%=CrudController.LISTAGEM%>">
																		&nbsp;
																		<button type="button" onclick="popUpCopiaIndicador(${filtro.unidadeGerencial.id},${objetivoMapaEstrategico.id})" class="botao_normal botao_normal_nl">
																			Copiar Indicador
																		</button>
																	</n:hasAuthorization>
																</c:if>
																<c:if test="${showTela and !showButton}">
																	<t:detalhe name="listaIndicador" showBotaoNovaLinha="false" showColunaAcao="false">
																		<c:choose>
																			<c:when test="${!indicador.cancelado}">
																				<t:property name="peso" style="width: 30px" headerStyle="width: 30px" label="Peso" write="true" type="hidden" bodyStyleClass="readonlyBody"/>
																				<t:property name="nome" style="width: 450px" write="true" type="hidden" bodyStyleClass="readonly"/>
																				<t:property name="melhor" style="width: 100px" write="true" type="hidden" bodyStyleClass="readonly"/>
																			</c:when>
																			<c:otherwise>
																				<t:property name="peso" style="width: 30px" headerStyle="width: 30px" label="Peso" write="true" type="hidden" bodyStyleClass="cancelado"/>
																				<t:property name="nome" style="width: 450px" write="true" type="hidden" bodyStyleClass="cancelado"/>
																				<t:property name="melhor" style="width: 100px"write="true" type="hidden" bodyStyleClass="cancelado"/>
																			</c:otherwise>
																		</c:choose>
																	</t:detalhe>
																</c:if>										
															</t:propertyConfig>
														</TD>																										
													</TR>
												</n:bean>
											</c:forEach>											
										</TABLE>										
									</TD>
								</TR>
							</n:bean>
						</c:forEach>
					</n:property>	
				</table>
							
				<c:if test="${!esconderSalvar && showTela and showButton}"	>
					<div align="right">
						<br>
						<n:submit class="botao_normal" action="salvar">salvar</n:submit>					
					</div>
				</c:if>							
				
			</c:if>
			
		
	</t:janelaFiltro>
</t:tela>

<script type="text/javascript">
	function recarregarTela(){
		form.validate = 'false';
		document.getElementById('idReload').value = 'true';	
		submitForm();
	}
	
	function abrirConfiguracaoFiltro(){
		var width = 800;
	    var height = 500;
	    var url = '${ctx}/sgm/process/DistribuicaoPesosIndicadores?ACAO=popUpConfiguraFiltro&idUnidadeGerencial=${filtro.unidadeGerencial.id}';	
		openModalWindow(url,width,height,false);
	}	
	
	function popUpDetalhamentoIndicador(botao) {
		var width  = screen.width;
	    var height = screen.height;	
	    var left   = 0;
	    var top    = 0;
	    
		var re = /listaPerspectivaMapaEstrategico\[(\d+)\]\.listaObjetivoMapaEstrategico\[(\d+)\]\.listaIndicador\[(\d+)\]\.peso/;	
		var el = botao.parentNode.parentNode.parentNode.parentNode.parentNode.parentNode;
		var casamento;
		var node;
		var idxPerspectivaMapaEstrategico = -1;
		var idxObjetivoMapaEstrategico = -1;
		var idxIndicador = -1;
				
		for (i = 0; i < el.childNodes.length; i++){			
			node = el.childNodes[i];
			casamento = re.exec(node.innerHTML);
			if (casamento) {
				idxPerspectivaMapaEstrategico = casamento[1]
				idxObjetivoMapaEstrategico = casamento[2];
				idxIndicador = casamento[3];
				break;
			}			
		}	
		
		var idPerspectivaMapaEstrategico = form['listaPerspectivaMapaEstrategico['+ idxPerspectivaMapaEstrategico +'].id'].value != "<null>" ? form['listaPerspectivaMapaEstrategico['+ idxPerspectivaMapaEstrategico +'].id'].value : "";   
		var idObjetivoMapaEstrategico = form['listaPerspectivaMapaEstrategico['+ idxPerspectivaMapaEstrategico +'].listaObjetivoMapaEstrategico['+ idxObjetivoMapaEstrategico +'].id'].value != "<null>" ? form['listaPerspectivaMapaEstrategico['+ idxPerspectivaMapaEstrategico +'].listaObjetivoMapaEstrategico['+ idxObjetivoMapaEstrategico +'].id'].value : "";   
		var id = form['listaPerspectivaMapaEstrategico['+ idxPerspectivaMapaEstrategico +'].listaObjetivoMapaEstrategico[' + idxObjetivoMapaEstrategico + '].listaIndicador[' + idxIndicador + '].id'].value != '<null>' ? form['listaPerspectivaMapaEstrategico['+ idxPerspectivaMapaEstrategico +'].listaObjetivoMapaEstrategico[' + idxObjetivoMapaEstrategico + '].listaIndicador[' + idxIndicador + '].id'].value : "";

    	window.open('${ctx}/sgm/process/DistribuicaoPesosIndicadores?ACAO=popUpDetalhamentoIndicador&idUnidadeGerencial=${filtro.unidadeGerencial.id}&idxPerspectivaMapaEstrategico='+idxPerspectivaMapaEstrategico+'&idxObjetivoMapaEstrategico='+idxObjetivoMapaEstrategico+'&idxIndicador='+idxIndicador+'&idIndicador='+id+'&idObjetivoMapaEstrategico='+idObjetivoMapaEstrategico+'&idPerspectivaMapaEstrategico='+idPerspectivaMapaEstrategico+'&reloadTela=false','indicador', 'width='+width+', height='+height+', top='+top+', left='+left+', scrollbars=yes, status=no, toolbar=no, location=no, directories=no, menubar=no, resizable=no, fullscreen=no');
	}
	
	function setarIniciativa(idxPerspectivaMapaEstrategico, idxObjetivoMapaEstrategico, idxIniciativa, id){
		var strLista = 'listaPerspectivaMapaEstrategico[' + idxPerspectivaMapaEstrategico + '].listaObjetivoMapaEstrategico[' + idxObjetivoMapaEstrategico + '].listaIniciativa[' + idxIniciativa + ']';
		form[strLista + '.id'].value = id;
	}
	
	function setarIndicador(idxPerspectivaMapaEstrategico, idxObjetivoMapaEstrategico, idxIndicador, peso, nome, melhor, id){
		var strLista = 'listaPerspectivaMapaEstrategico[' + idxPerspectivaMapaEstrategico + '].listaObjetivoMapaEstrategico[' + idxObjetivoMapaEstrategico + '].listaIndicador[' + idxIndicador + ']';
		
		form[strLista + '.peso'].value = peso;
		form[strLista + '.nome'].value = nome;
		form[strLista + '.melhor'].value = melhor;
		form[strLista + '.id'].value = id;
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
	
	function popUpPlanoAcao(botao) {
		var width = 800;
	    var height = 400;
	    var left = 99;
	    var top = 99;
	
		var re = /listaPerspectivaMapaEstrategico\[(\d+)\]\.listaObjetivoMapaEstrategico\[(\d+)\]\.listaIniciativa\[(\d+)\]\.descricao/;	
		var el = botao.parentNode.parentNode;
		var casamento;
		var node;
		var idxPerspectivaMapaEstrategico = -1;
		var idxObjetivoMapaEstrategico = -1;
		var idxIniciativa = -1;
				
		for (i = 0; i < el.childNodes.length; i++){
			node = el.childNodes[i];
			casamento = re.exec(node.innerHTML);
			if (casamento) {
				idxPerspectivaMapaEstrategico = casamento[1]
				idxObjetivoMapaEstrategico = casamento[2];
				idxIniciativa = casamento[3];
				break;
			}			
		}	
		
		var descricaoIniciativa = form['listaPerspectivaMapaEstrategico['+ idxPerspectivaMapaEstrategico +'].listaObjetivoMapaEstrategico[' + idxObjetivoMapaEstrategico + '].listaIniciativa[' + idxIniciativa + '].descricao'].value;
		if (descricaoIniciativa == null  || descricaoIniciativa == '') {
			alert('Favor preencher a descrição da iniciativa.');
		}
		else {
			var idPerspectivaMapaEstrategico = form['listaPerspectivaMapaEstrategico['+ idxPerspectivaMapaEstrategico +'].id'].value != "<null>" ? form['listaPerspectivaMapaEstrategico['+ idxPerspectivaMapaEstrategico +'].id'].value : "";   
			var idObjetivoMapaEstrategico = form['listaPerspectivaMapaEstrategico['+ idxPerspectivaMapaEstrategico +'].listaObjetivoMapaEstrategico['+ idxObjetivoMapaEstrategico +'].id'].value != "<null>" ? form['listaPerspectivaMapaEstrategico['+ idxPerspectivaMapaEstrategico +'].listaObjetivoMapaEstrategico['+ idxObjetivoMapaEstrategico +'].id'].value : "";   
			var id = form['listaPerspectivaMapaEstrategico['+ idxPerspectivaMapaEstrategico +'].listaObjetivoMapaEstrategico[' + idxObjetivoMapaEstrategico + '].listaIniciativa[' + idxIniciativa + '].id'].value != '<null>' ? form['listaPerspectivaMapaEstrategico['+ idxPerspectivaMapaEstrategico +'].listaObjetivoMapaEstrategico[' + idxObjetivoMapaEstrategico + '].listaIniciativa[' + idxIniciativa + '].id'].value : "";
	
	    	window.open('${ctx}/sgm/process/DistribuicaoPesosIndicadores?ACAO=popUpPlanoAcao&idUnidadeGerencial=${filtro.unidadeGerencial.id}&idxPerspectivaMapaEstrategico='+idxPerspectivaMapaEstrategico+'&idxObjetivoMapaEstrategico='+idxObjetivoMapaEstrategico+'&idxIniciativa='+idxIniciativa+'&idIniciativa='+id+'&idObjetivoMapaEstrategico='+idObjetivoMapaEstrategico+'&idPerspectivaMapaEstrategico='+idPerspectivaMapaEstrategico+'&reloadTela=false','plano_acao', 'width='+width+', height='+height+', top='+top+', left='+left+', scrollbars=yes, status=no, toolbar=no, location=no, directories=no, menubar=no, resizable=no, fullscreen=no');    
	    }
	}	
	
	function popUpCopiaIndicador(unidadeGerencialId, objetivoMapaEstrategicoId) {
		var width  = screen.width;
	    var height = screen.height;	
	    var left   = 0;
	    var top    = 0;
	    
    	window.open('${ctx}/util/crud/Indicador?unidadeGerencialId='+unidadeGerencialId+'&objetivoMapaEstrategicoId='+objetivoMapaEstrategicoId+'','indicador', 'width='+width+', height='+height+', top='+top+', left='+left+', scrollbars=yes, status=no, toolbar=no, location=no, directories=no, menubar=no, resizable=no, fullscreen=no');
	}	
	
</script>

<style>
	span {
		white-space: normal;
	}
</style>
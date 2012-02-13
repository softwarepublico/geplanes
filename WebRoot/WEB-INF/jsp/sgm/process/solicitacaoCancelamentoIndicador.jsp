<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="n" uri="neo"%>
<%@ taglib prefix="t" uri="template"%>
<%@ taglib prefix="f" tagdir="/WEB-INF/tags/"%>

<t:tela titulo="Solicitação de cancelamento de indicador">
	<t:janelaFiltro>
		<input type="hidden" name="reload" value="" id="idReload">
		
		<t:tabelaFiltro>
			<n:comboReloadGroup useAjax="true">
				<t:property name="planoGestao"	style="width: 100px;" />
				<n:output styleClass="desc11" value="Unidade Gerencial"/>
				<n:panel>
					<f:unidadeGerencialInput name="unidadeGerencial" onchange="carregaPerspectivas()"/>
				</n:panel>
				<t:property name="perspectivaMapaEstrategico" itens="perspectivaMapaEstrategicoService.findByUnidadeGerencialThroughMapaEstrategico(unidadeGerencial,true)" style="width:70%;" onchange="carregaObjetivosEstrategicos()"/>
				<t:property name="objetivoMapaEstrategico" itens="objetivoMapaEstrategicoService.findByUnidadeGerencialPerspectivaThroughMapaEstrategico(unidadeGerencial,perspectivaMapaEstrategico)" style="width:70%;" />
				<t:property name="status"/>				
			</n:comboReloadGroup>
		</t:tabelaFiltro>

			<t:propertyConfig showLabel="true" renderAs="double">			
			
			<c:if test="${!empty filtro.planoGestao}">
			<n:panel>				
				<br>
				<t:propertyConfig showLabel="false" >
					<n:dataGrid property="listaSolicitacaoCancelamentoIndicador" var="sol" headerStyleClass="txt_tit" bodyStyleClasses="txt_l1, txt_l2" styleClass="fd_tabela1">
						<n:column header="U.G.">
							<t:property name="indicador.unidadeGerencial" type="hidden" write="true" label="U.G."/>
							<t:property name="id" type="hidden" showLabel="false"/>
							<t:property name="indicador.objetivoMapaEstrategico.id" type="hidden" write="false" />
						</n:column>
						<t:property name="indicador.objetivoMapaEstrategico" mode="output" />
						<t:property name="indicador" type="hidden" write="true" />
						<t:property name="dtSolicitacao" type="hidden" write="true" />
						<t:property name="solicitante" type="hidden" write="true" />
						<t:property name="justificativaSol" type="hidden" write="true" />	
						<t:property name="status" mode="input" type="hidden" write="true"/>											
						<c:choose>
							<c:when test="${isAdmin && sol.status.codigo == 0}">
								<t:property name="justificativaRes" rows="2" cols="40" mode="input"/>
							</c:when>
							<c:otherwise>
								<t:property name="justificativaRes" type="hidden" write="true" />								
							</c:otherwise>
						</c:choose>
						<n:column header="">
							<n:body>
								<table cellpadding="0" cellspacing="0" border="0">
									<tr>
										<td>
											<image onmouseover="Tip('COMENTÁRIOS')" src="../../images/ico_comentario.png" onclick="javascript:popUpComentario(${sol.id});"/>
										</td>
										<c:if test="${isAdmin}">
											<c:if test="${sol.status.codigo == 0}">
												<td>
													<n:submit action="salvar" onmouseover="Tip(\"APROVAR\")" img="../../images/aprovado.png" parameters="id=${sol.id}&aprovar=true"/>
												</td>
												<td>
													<n:submit action="salvar" onmouseover="Tip(\"REPROVAR\")" img="../../images/reprovado.png" parameters="id=${sol.id}&aprovar=false"/>
												</td>
											</c:if>
											<td>
												<n:link action="excluir" img="../../images/ico_excluir.gif" onmouseover="Tip(\"EXCLUIR\")" confirmationMessage="Deseja realmente excluir esse registro?" parameters="id=${sol.id}"/>
											</td>
										</c:if>
									</tr>
								</table>
							</n:body>
						</n:column>
					</n:dataGrid>
				</t:propertyConfig>
			</n:panel>				
			</c:if>			
		</t:propertyConfig>		
	</t:janelaFiltro>			
</t:tela>

<script type="text/javascript">
	function recarregarTela(){
		form.validate = 'false';
		document.getElementById('idReload').value = 'true';
		submitForm();
	}
	
	function carregaPerspectivas() {
		setTimeout('form[\'perspectivaMapaEstrategico\'].loadItens()',1);
	}	
	
	function carregaObjetivosEstrategicos() {
		setTimeout('form[\'objetivoMapaEstrategico\'].loadItens()',1);
	}

	function popUpComentario(solicitacaoID) {
		var width = 500;
	    var height = 400;
	
	    var left = 99;
	    var top = 99;
	    window.open('${ctx}/sgm/process/SolicitacaoCancelamentoIndicador?ACAO=popUpComentario&solicitacaoID='+solicitacaoID+'','comentario', 'width='+width+', height='+height+', top='+top+', left='+left+', scrollbars=yes, status=no, toolbar=no, location=no, directories=no, menubar=no, resizable=no, fullscreen=no');
	}
</script>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="n" uri="neo"%>
<%@ taglib prefix="t" uri="template"%>
<%@ taglib prefix="f" tagdir="/WEB-INF/tags/"%>


<t:listagem titulo="Planos de Ação das Iniciativas" showNewLink="false">
	<t:janelaFiltro>
		<input type="hidden" name="reload" value="" id="idReload">
		<t:tabelaFiltro>
			<n:comboReloadGroup useAjax="true">
				<t:property name="planoGestao" style="width: 100px;"/>
				<n:output styleClass="desc11" value="Unidade Gerencial" />
				<n:panel>
					<f:unidadeGerencialInput name="unidadeGerencial" onchange="carregaPerspectivas()"/>
					&nbsp;<t:property name="incluirSubordinadas" />
				</n:panel>
				<t:property name="perspectivaMapaEstrategico" itens="perspectivaMapaEstrategicoService.findByUnidadeGerencialThroughMapaEstrategico(unidadeGerencial,true)" style="width:70%;" onchange="carregaObjetivosEstrategicos()"/>
				<t:property name="objetivoMapaEstrategico" itens="objetivoMapaEstrategicoService.findByUnidadeGerencialPerspectivaThroughMapaEstrategico(unidadeGerencial,perspectivaMapaEstrategico)" style="width:70%;" onchange="carregaIniciativas()"/>
				<t:property name="iniciativa" itens="iniciativaService.findByUnidadeGerencialObjetivoEstrategico(unidadeGerencial, objetivoMapaEstrategico)" style="width:70%;" />
			</n:comboReloadGroup>
			<%--
			<t:property name="expirado" trueFalseNullLabels="Sim,Não,"/>
			<t:property name="listaStatus" size="3" includeBlank="false" type="SELECT_MANY" itens="mapaStatus"/>
			--%>
		</t:tabelaFiltro>
		
		<t:propertyConfig showLabel="true" renderAs="double">			
			<c:if test="${!empty filtro.planoGestao}">
				<n:panel>				
					<br>
					<t:propertyConfig showLabel="false" >
						<n:dataGrid property="listaPlanoAcao" var="planoAcao" headerStyleClass="txt_tit" bodyStyleClasses="txt_l1, txt_l2" styleClass="fd_tabela1">
							<n:column header="U.G.">
								<t:property name="unidadeGerencial" type="hidden" write="true" label="U.G."/>
								<t:property name="id" type="hidden" showLabel="false"/>
								<t:property name="dtAtualizacaoStatus" mode="input" type="hidden" write="false"/>
							</n:column>
							<t:property name="iniciativa.objetivoMapaEstrategico" mode="output" />
							<t:property name="iniciativa" type="hidden" write="true" />
							<t:property name="texto" type="hidden" write="true" />
							<t:property name="textoComo" type="hidden" write="true" />
							<t:property name="textoPorque" type="hidden" write="true" />	
							<t:property name="textoQuem" mode="input" type="hidden" write="true"/>											
							<t:property name="dtPlano" mode="input" type="hidden" write="true"/>							
							<n:column header="Status">
								<c:choose>
									<c:when test="${USUARIOADMIN}">
											<t:property name="status" mode="input" includeBlank="false"/>																					
									</c:when>
									<c:otherwise>
										<c:choose>
											<c:when test="${planoAcao.status.name == 'CONCLUIDO'}">											
												<t:property name="status" mode="input" type="hidden" write="true"/>
											</c:when>
											<c:otherwise>
												<c:choose>
													<c:when test="${planoAcao.podeEditarStatus}">
														<t:property name="status" mode="input" includeBlank="false"/>
													</c:when>
													<c:otherwise>
														<t:property name="status" mode="input" type="hidden" write="true"/>
													</c:otherwise>
												</c:choose>
											</c:otherwise>
										</c:choose>
									</c:otherwise>
								</c:choose>
								&nbsp;<t:property name="descricaoDtAtualizacaoStatus" mode="output"/>
							</n:column>
						</n:dataGrid>
					</t:propertyConfig>
				</n:panel>				
			</c:if>			
		</t:propertyConfig>		
	</t:janelaFiltro>
	<c:if test="${!empty filtro.listaPlanoAcao}">
		<table align="right">
			<tr>
				<td>
					<n:submit action="salvar" class="botao_normal" validate="true">Salvar</n:submit>
				</td>
			</tr>
		</table> 
	</c:if>
</t:listagem>


<script language="javascript">

	function carregaPerspectivas() {
		setTimeout('form[\'perspectivaMapaEstrategico\'].loadItens()',1);
	}
	
	function carregaObjetivosEstrategicos() {
		setTimeout('form[\'objetivoMapaEstrategico\'].loadItens()',1);
	}
		
	function carregaIniciativas() {
		setTimeout('form[\'iniciativa\'].loadItens()',1);
	}	
	
</script>

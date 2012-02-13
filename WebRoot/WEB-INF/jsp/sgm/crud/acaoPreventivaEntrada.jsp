<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="n" uri="neo"%>
<%@ taglib prefix="t" uri="template"%>
<%@ taglib prefix="f" tagdir="/WEB-INF/tags/"%>

<c:if test="${podeConsultarAcaoPreventiva}">
    <style>.form_filtro_value{text-align:left;vertical-align:top;}</style>
	<t:entrada titulo="TRATAMENTO DE AÇÕES PREVENTIVAS" showSaveLink="${podeEditarAcaoPreventiva || podeEncerrarAcaoPreventiva}">
	    <span style="display: none">
			<t:property name="id" write="false" type="hidden" />				
			<t:property name="acompanhamentoIndicador" mode="input" type="hidden" />
			<t:property name="status" mode="input" type="hidden"/>
	    </span>
		<t:janelaEntrada showSubmit="false">
		<c:choose>
			<c:when test="${podeEditarAcaoPreventiva}">
				<t:tabelaEntrada>
					<c:if test="${acaoPreventiva.id != null}">
						<t:property name="sequencial" type="hidden" write="true"/>
					</c:if>
					<c:if test="${!empty idUgOrigem or !empty acaoPreventiva.ugRegistro.id}">
						<t:property name="planoGestao" type="hidden" write="true"/>				
						<t:property name="ugRegistro" disabled="disabled" 	style="width: 150px" />
						<t:property name="ugRegistro" type="hidden" label=" "	style="width: 150px" />
					</c:if>
					<c:if test="${empty idUgOrigem and empty acaoPreventiva.ugRegistro.id}">
						<t:property name="planoGestao" style="width: 100px;"/>				
						<n:output styleClass="desc10bold" value="Setor de registro"/>
						<n:panel>
							<f:unidadeGerencialInput name="ugRegistro" estiloclasse="required"/>
						</n:panel>
					</c:if>
					<t:property name="dataAbertura"	style="width: 100px" type="hidden" write="true"/>
					<c:if test="${acaoPreventiva.id != null}">
						<c:choose>
							<c:when test="${podeEncerrarAcaoPreventiva}">
								<t:property name="dataEncerramento"	style="width: 72px" />
							</c:when>
							<c:otherwise>
								<t:property name="dataEncerramento"	style="width: 72px" type="hidden" write="true"/>
							</c:otherwise>
						</c:choose>					
					</c:if>
					<t:property name="tipo"/>
					<t:property name="origem"/>
					<t:property name="descricao" 				rows="8" cols="100"/>
					<t:property name="observacoes" 				rows="8" cols="100"/>
				</t:tabelaEntrada>
				<t:detalhe name="planosAcao" labelnovalinha="Novo plano de ação" showBotaoNovaLinha="true" showBotaoRemover="true">
					<n:column header="O que">
						<t:property name="id" write="false" type="hidden" renderAs="single"/>
						<t:property name="dtAtualizacaoStatus" write="false" type="hidden" renderAs="single"/>
						<t:property name="texto" 		rows="3" style="width:140px"/>
					</n:column>			   
					<t:property name="textoComo" 	rows="3" style="width:140px"/>
					<t:property name="textoPorque" 	rows="3" style="width:140px"/>
					<t:property name="textoQuem"	rows="3" style="width:140px"/>
					<t:property name="dtPlano" style="width:72px"/>
					<n:column header="Status">
						<t:property name="status" includeBlank="false"/>
						<br>
						<t:property name="descricaoDtAtualizacaoStatus" mode="output"/>
					</n:column>
				</t:detalhe>				
			</c:when>
			<c:otherwise>
				<t:tabelaEntrada>
						<c:choose>
							<c:when test="${acaoPreventiva.id != null}">
								<t:property name="sequencial" type="hidden" write="true"/>
							</c:when>
							<c:otherwise>						
							</c:otherwise>
						</c:choose>			
						<t:property name="planoGestao" 				type="hidden" write="true" />				
						<t:property name="ugRegistro" 				write="true" type="hidden" 	style="width: 150px" />
						<t:property name="dataAbertura"				type="hidden" write="true"	style="width: 100px"/>
						<c:if test="${acaoPreventiva.id != null}">
							<c:choose>
								<c:when test="${podeEncerrarAcaoPreventiva}">
									<t:property name="dataEncerramento"	style="width: 100px" />
								</c:when>
								<c:otherwise>
									<t:property name="dataEncerramento"	style="width: 100px" type="hidden" write="true"/>
								</c:otherwise>
							</c:choose>						
						</c:if>
						<t:property name="tipo" 					write="true" type="hidden"	style="width: 150px"/>
						<t:property name="origem" 					write="true" type="hidden"	style="width: 150px"/>
						<t:property name="descricao" 				rows="8" cols="100" label="Descrição da acaoPreventiva" type="hidden" write="true"/>
						<t:property name="observacoes" 				rows="8" cols="100" type="hidden" write="true"/>
				</t:tabelaEntrada>
				<t:detalhe name="planosAcao" showBotaoNovaLinha="false" showBotaoRemover="false">
					<n:column header="O que">
						<t:property name="id" write="false" type="hidden" renderAs="single"/>
						<t:property name="dtAtualizacaoStatus" write="false" type="hidden" renderAs="single"/>
						<t:property name="texto" rows="3" style="width:140px" readonly="true" class="readonlyField"/>
					</n:column>
					<t:property name="textoComo" 	rows="3" style="width:140px" readonly="true" class="readonlyField"/>
					<t:property name="textoPorque" 	rows="3" style="width:140px" readonly="true" class="readonlyField"/>
					<t:property name="textoQuem"	rows="3" style="width:140px" readonly="true" class="readonlyField"/>
					<t:property name="dtPlano" write="true" type="hidden" class="readonlyField"/>
					<t:property name="status" write="true" type="hidden" class="readonlyField"/>							
				</t:detalhe>				
			</c:otherwise>
		</c:choose>
		<c:if test="${acaoPreventiva.id != null}">
			<jsp:include page="/WEB-INF/jsp/sgm/crud/acaoPreventivaCausaTratamento.jsp" />
		</c:if>
		</t:janelaEntrada>
		<table class="form_filtro_pesquisar">
			<tr>
				<td>
					<c:if test="${!empty acaoPreventiva.id}">
						<n:submit class="botao_normal" action="gerar" url="/sgm/report/AcaoPreventiva" parameters="${acaoPreventiva}" validate="true">imprimir</n:submit>
						&nbsp;
					</c:if>
				</td>
			</tr>
		</table>
	</t:entrada>
</c:if>
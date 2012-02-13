<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="n" uri="neo"%>
<%@ taglib prefix="t" uri="template"%>
<%@ taglib prefix="f" tagdir="/WEB-INF/tags/"%>

<t:listagem titulo="LISTAGEM DE AÇÕES PREVENTIVAS">
	<t:janelaFiltro>
		<t:tabelaFiltro>
			<input type="hidden" name="reload" value="true" id="idReload">
			<t:property name="planoGestao" style="width: 100px;" onchange="$geplanesUtil.limpaUnidadeGerencialAnomalia()"/>		
			<n:output styleClass="desc11" value="Setor de registro"/>
			<n:panel>
				<f:unidadeGerencialInput name="ugRegistro"/>
				&nbsp;<t:property name="incluirSubordinadasReg"/>
			</n:panel>
			<t:property name="status"/>	
			<t:property name="sequencial" style="width: 150px;overflow:hidden;"/>			
			<t:property name="descricao" style="width: 250px;overflow:hidden;"/>
		</t:tabelaFiltro>
	</t:janelaFiltro>
	
	<t:janelaResultados>
	
		<t:tabelaResultados showEditarLink="false" showExcluirLink="false">
			<t:property name="sequencial" />
			<t:property name="ugRegistro" label="Setor de registro" />
			<t:property name="descricao" style="overflow:hidden;" />
			<t:property name="dataAbertura" style="overflow:hidden;" label="Abertura"/>
			<n:column header="Status">
				<n:body style="text-align:center;">
					<c:if test="${acaoPreventiva.status == 'Aberta'}">
						<img src="../../images/ico_aberta.gif" alt="Aberta" title="Aberta"/>
					</c:if>
					<c:if test="${acaoPreventiva.status == 'Encerrada'}">
						<img src="../../images/ico_encerrada.gif" alt="Encerrada" title="Encerrada"/>
					</c:if>
				</n:body>
			</n:column>
			<t:acao>
				<c:if test="${acaoPreventiva.podeImprimir}">
					<n:link url="/sgm/report/AcaoPreventiva" action="gerar"
						parameters="${n:idProperty(n:reevaluate(TtabelaResultados.name,pageContext))}=${n:id(n:reevaluate(TtabelaResultados.name,pageContext))}">
						<img src="../../images/pdf.gif" border="0"
							title="Relatório da ação preventiva" />
					</n:link>
				</c:if>
				<c:if test="${acaoPreventiva.podeEditar}">
					<n:link action="editar" img="../../images/ico_editar.gif"
						parameters="${n:idProperty(n:reevaluate(TtabelaResultados.name,pageContext))}=${n:id(n:reevaluate(TtabelaResultados.name,pageContext))}" />
				</c:if>
				<c:if test="${acaoPreventiva.podeExcluir}">
					<n:link action="excluir" img="../../images/ico_excluir.gif"
						confirmationMessage="Deseja realmente excluir esse registro?"
						parameters="${n:idProperty(n:reevaluate(TtabelaResultados.name,pageContext))}=${n:id(n:reevaluate(TtabelaResultados.name,pageContext))}" />
				</c:if>
			</t:acao>
		</t:tabelaResultados>
	</t:janelaResultados>
</t:listagem>
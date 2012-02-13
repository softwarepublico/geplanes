<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="n" uri="neo"%>
<%@ taglib prefix="t" uri="template"%>
<%@ taglib prefix="f" tagdir="/WEB-INF/tags/"%>

<t:listagem titulo="Listagem de auditorias internas" showNewLink="${podeCriarRegistroAuditoriaInterna}">
	<t:janelaFiltro>
		<t:tabelaFiltro>
			<input type="hidden" name="pesquisar" value="1"/>
			<t:property name="planoGestao" style="width: 100px;" onchange="$geplanesUtil.limpaUnidadeGerencialAuditoriaInterna()"/>		
			<n:output styleClass="desc11" value="Área auditada"/>
			<n:panel>
				<f:unidadeGerencialInput name="ugResponsavel"/>
				&nbsp;<t:property name="incluirSubordinadasResp"/>
			</n:panel>
			<t:property name="norma"/>
			<t:property name="status"/>
		</t:tabelaFiltro>
	</t:janelaFiltro>
	
	<t:janelaResultados>
	
		<t:tabelaResultados showEditarLink="false" showExcluirLink="false">
			<t:property name="ugRegistro" label="Setor de registro" />
			<t:property name="ugResponsavel" label="Área auditada" />
			<t:property name="norma"/>
			<t:property name="dataAuditoria" style="overflow:hidden;"/>
			<t:property name="dataEncerramento" style="overflow:hidden;"/>
			<n:column header="Status" align="center">
				<n:body style="text-align:center;">
					<c:if test="${auditoriaInterna.status == 'Aberta'}">
						<img src="../../images/ico_aberta.gif" alt="Aberta" title="Aberta"/>
					</c:if>
					<c:if test="${auditoriaInterna.status == 'Encerramento solicitado'}">
						<img src="../../images/ico_encerramentosolicitado.gif" alt="Encerramento solicitado" title="Encerramento solicitado"/>
					</c:if>
					<c:if test="${auditoriaInterna.status == 'Encerrada'}">
						<img src="../../images/ico_encerrada.gif" alt="Encerrada" title="Encerrada"/>
					</c:if>
				</n:body>
			</n:column>			
			<t:acao>
				<c:if test="${auditoriaInterna.podeImprimir}">
					<n:link url="/sgm/report/AuditoriaInterna" action="gerar"
						parameters="${n:idProperty(n:reevaluate(TtabelaResultados.name,pageContext))}=${n:id(n:reevaluate(TtabelaResultados.name,pageContext))}">
						<img src="../../images/pdf.gif" border="0"
							title="Relatório da auditoria interna" />
					</n:link>
				</c:if>
				<c:if test="${auditoriaInterna.podeEditar}">
					<n:link action="editar" img="../../images/ico_editar.gif"
						parameters="${n:idProperty(n:reevaluate(TtabelaResultados.name,pageContext))}=${n:id(n:reevaluate(TtabelaResultados.name,pageContext))}" />
				</c:if>
				<c:if test="${auditoriaInterna.podeExcluir}">
					<n:link action="excluir" img="../../images/ico_excluir.gif"
						confirmationMessage="Deseja realmente excluir esse registro?"
						parameters="${n:idProperty(n:reevaluate(TtabelaResultados.name,pageContext))}=${n:id(n:reevaluate(TtabelaResultados.name,pageContext))}" />
				</c:if>
			</t:acao>
		</t:tabelaResultados>
	</t:janelaResultados>
</t:listagem>
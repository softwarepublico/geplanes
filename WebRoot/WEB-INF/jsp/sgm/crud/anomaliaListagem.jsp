<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="n" uri="neo"%>
<%@ taglib prefix="t" uri="template"%>
<%@ taglib prefix="f" tagdir="/WEB-INF/tags/"%>

<t:listagem titulo="LISTAGEM DE ANOMALIAS">
	<t:janelaFiltro>
		<t:tabelaFiltro>
			<input type="hidden" name="pesquisar" value="1"/>
			<t:property name="planoGestao" style="width: 100px;"/>		
			<n:output styleClass="desc11" value="Setor de registro"/>
			<n:panel>
				<f:unidadeGerencialInput name="ugRegistro"/>
				&nbsp;<t:property name="incluirSubordinadasReg"/>
			</n:panel>
			<n:output styleClass="desc11" value="Setor responsável pelo tratamento"/>
			<n:panel>
				<f:unidadeGerencialInput name="ugResponsavel"/>
				&nbsp;<t:property name="incluirSubordinadasResp"/>
			</n:panel>
			<t:property name="origem" id="origem" onchange="changeOrigem(this)"/>
			<t:property name="tipoAuditoria" id="tipoAuditoria"/>
			<t:property name="status"/>	
			<t:property name="sequencial" style="width: 150px;overflow:hidden;"/>			
			<t:property name="descricao" style="width: 250px;overflow:hidden;"/>
		</t:tabelaFiltro>
	</t:janelaFiltro>
	
	<t:janelaResultados>
	
		<div style="text-align:right">
			<n:link class="v10n" action="javascript:popUpLegenda()">* Legenda - Status das Anomalias</n:link>
		</div>
		
		<t:tabelaResultados showEditarLink="false"
			showExcluirLink="false">
			<t:property name="sequencial" />
			<t:property name="ugRegistro" label="Setor de registro" />
			<t:property name="ugResponsavel" label="Setor responsável pelo tratamento" />
			<t:property name="descricao" style="overflow:hidden;" />
			<t:property name="dataAbertura" style="overflow:hidden;" label="Abertura"/>
			<n:column header="Status">
				<n:body style="text-align:center;">
					<c:if test="${anomalia.status.name == 'ABERTA'}">
						<img src="../../images/ico_aberta.gif" alt="Aberta" title="Aberta"/>
					</c:if>
					<c:if test="${anomalia.status.name == 'TRATAMENTO_PENDENTE'}">
						<img src="../../images/ico_tratamentopendente.gif" alt="Tratamento pendente" title="Tratamento pendente"/>
					</c:if>
					<c:if test="${anomalia.status.name == 'ENCERRAMENTO_SOLICITADO'}">
						<img src="../../images/ico_encerramentosolicitado.gif" alt="Encerramento solicitado" title="Encerramento solicitado"/>
					</c:if>
					<c:if test="${anomalia.status.name == 'ENCERRAMENTO_PENDENTE'}">
						<img src="../../images/ico_encerramentopendente.gif" alt="Encerramento pendente" title="Encerramento pendente"/>
					</c:if>
					<c:if test="${anomalia.status.name == 'TRATADA'}">
						<img src="../../images/ico_tratada.gif" alt="Tratada" title="Tratada"/>
					</c:if>
					<c:if test="${anomalia.status.name == 'BLOQUEADA'}">
						<img src="../../images/ico_bloqueada.gif" alt="Bloqueada" title="Bloqueada"/>
					</c:if>
					<c:if test="${anomalia.status.name == 'CUMPRIMENTO_PENDENTE'}">
						<img src="../../images/ico_cumprimentopendente.gif" alt="Cumprimento pendente" title="Cumprimento pendente"/>
					</c:if>
					<c:if test="${anomalia.status.name == 'ENCERRADA'}">
						<img src="../../images/ico_encerrada.gif" alt="Encerrada" title="Encerrada"/>
					</c:if>
					<c:if test="${anomalia.status.name == 'REANALISE'}">
						<img src="../../images/ico_reanalise.gif" alt="Reanálise" title="Reanálise"/>
					</c:if>
				</n:body>
			</n:column>
			<t:acao>
				<c:if test="${anomalia.podeImprimir}">
					<n:link url="/sgm/report/Anomalia" action="gerar"
						parameters="${n:idProperty(n:reevaluate(TtabelaResultados.name,pageContext))}=${n:id(n:reevaluate(TtabelaResultados.name,pageContext))}">
						<img src="../../images/pdf.gif" border="0"
							title="Relatório da anomalia" />
					</n:link>
				</c:if>
				<c:if test="${anomalia.podeEditar}">
					<n:link action="editar" img="../../images/ico_editar.gif"
						parameters="${n:idProperty(n:reevaluate(TtabelaResultados.name,pageContext))}=${n:id(n:reevaluate(TtabelaResultados.name,pageContext))}" />
				</c:if>
				<c:if test="${anomalia.podeExcluir}">
					<n:link action="excluir" img="../../images/ico_excluir.gif"
						confirmationMessage="Deseja realmente excluir esse registro?"
						parameters="${n:idProperty(n:reevaluate(TtabelaResultados.name,pageContext))}=${n:id(n:reevaluate(TtabelaResultados.name,pageContext))}" />
				</c:if>
			</t:acao>
		</t:tabelaResultados>
	</t:janelaResultados>
</t:listagem>

<script language="javascript">
	function popUpLegenda() {
		var width  = 900;
	    var height = 280;	
	    var left   = 0;
	    var top    = 0;
	    
    	window.open('${ctx}/legendaAnomalia.jsp','Legenda', 'width='+width+', height='+height+', top='+top+', left='+left+', scrollbars=yes, status=no, toolbar=no, location=no, directories=no, menubar=no, resizable=no, fullscreen=no');
	}	
	
	function changeOrigem(el){
		var linha = $("#tipoAuditoria").parent().parent();
		var display = el.value=="AUDITORIA_INTERNA" ? "table-row" : "none"; 
		linha.css("display", display);
		linha.prev().css("display", display);
	}
	
	$(function(){
		changeOrigem(document.getElementById('origem'));
	})
</script>
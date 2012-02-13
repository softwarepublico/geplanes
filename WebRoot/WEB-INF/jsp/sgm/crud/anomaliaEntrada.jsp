<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="n" uri="neo"%>
<%@ taglib prefix="t" uri="template"%>
<%@ taglib prefix="f" tagdir="/WEB-INF/tags/"%>

<c:if test="${podeConsultarAnomalia}">
    <style>
    	.form_filtro_value{text-align:left;vertical-align:top;}
    </style>
	<t:entrada titulo="TRATAMENTO DE ANOMALIAS" showSaveLink="false" formEnctype="multipart/form-data">
	   	<jsp:attribute name="linkArea">
		<c:if test="${(podeEditarAnomalia || podeTratarAnomalia || podeEncerrarAnomalia || podeCriarPlanoAcao || podeEditarPlanoAcao) && !podeDestravarAnomalia}">
			<button type="button" onclick="javascript:abrirEscolha()" title=""   id='btn_gravar' title='Gravar' checkpermission='true' class='botao_normal'>Salvar</button>
		</c:if>
		</jsp:attribute>
		<jsp:body>	
		    <span style="display: none">
				<t:property name="lembreteEnviado" write="false" type="hidden"/>
				<t:property name="id" write="false" type="hidden" />				
				<t:property name="dataSolicitacaoEncerramento" write="false" type="hidden" />				
				<t:property name="ocorrencia" write="false" type="hidden" />				
				<t:property name="local" type="hidden" />
				<t:property name="acompanhamentoIndicador" mode="input" type="hidden" />
				<t:property name="efeito.id" mode="input" type="hidden"/>
				<t:property name="status" mode="input" type="hidden"/>
				<t:property name="statusTratamento" mode="input" type="hidden"/>
				<n:submit action="salvar" class="botao_normal" validate="true" style="display:none;" id="salvar_button"/>
				<n:submit action="solicitarEncerramento" class="botao_normal" validate="true" style="display:none;" id="salvar_button_sol_enc"/>				
		    </span>
			<t:janelaEntrada showSubmit="false">
			<c:choose>
				<c:when test="${podeEditarAnomalia}">
					<t:tabelaEntrada>
						<c:if test="${anomalia.id != null}">
							<t:property name="sequencial" type="hidden" write="true"/>
						</c:if>
						<c:if test="${anomalia.ocorrencia != null}">
					 		<t:property name="ocorrencia.numero" type="hidden" write="true"/>
						</c:if>
						<c:if test="${anomalia.subordinacao != null}">
							<t:property name="subordinacao" type="hidden" write="false" label=""/>
							<t:property name="subordinacao.sequencial" type="hidden" write="true" label="Nº anomalia em reanálise"/>
						</c:if>							
						<c:if test="${!empty idUgOrigem or !empty anomalia.ugRegistro.id}">
							<t:property name="planoGestao" type="hidden" write="true"/>				
							<t:property name="ugRegistro" disabled="disabled" 	style="width: 150px" />
							<t:property name="ugRegistro" type="hidden" label=" "	style="width: 150px" />
						</c:if>
						<c:if test="${empty idUgOrigem and empty anomalia.ugRegistro.id}">
							<t:property name="planoGestao" style="width: 100px;"/>				
							<n:output styleClass="desc10bold" value="Setor de registro"/>
							<n:panel>
								<f:unidadeGerencialInput name="ugRegistro" onchange="alteraOrigemAnomalia();" estiloclasse="required"/>
							</n:panel>
						</c:if>
						<n:output styleClass="desc10bold" value="Setor responsável pelo tratamento"/>
						<n:panel>
							<f:unidadeGerencialInput name="ugResponsavel" onchange="alteraOrigemAnomalia();" estiloclasse="required"/>
						</n:panel>
						
						<t:property name="local" disabled="disabled"	style="width: 150px" />
						<t:property name="dataAbertura"	style="width: 100px" type="hidden" write="true"/>
						<c:if test="${anomalia.dataDestravamento != null}">
							<t:property name="dataDestravamento" type="hidden" write="true"/>
						</c:if>
						<t:property name="responsavel" style="width: 200px;overflow:hidden;" type="hidden" write="true"/>
						<t:property name="classificacao"/>
						<t:property name="tipo"/>
						<t:property name="origem" id="origem" onchange="changeOrigem(this);" />
						<t:property name="tipoAuditoria" id="tipoAuditoria" />
						<t:property name="descricao" 				rows="8" cols="100" label="Descrição da anomalia"/>
						<t:property name="contraMedidasImediatas"	rows="8" cols="100" />
						<t:property name="observacoes" 				rows="8" cols="100"/>
					</t:tabelaEntrada>
				</c:when>
				<c:otherwise>
					<t:tabelaEntrada>
							<c:choose>
								<c:when test="${anomalia.id != null}">
									<t:property name="sequencial" type="hidden" write="true"/>
								</c:when>
								<c:otherwise>						
								</c:otherwise>
							</c:choose>
							<c:if test="${anomalia.subordinacao != null}">
								<t:property name="subordinacao" type="hidden" write="false" label=""/>
								<t:property name="subordinacao.sequencial" type="hidden" write="true" label="Nº anomalia em reanálise"/>
							</c:if>											
							<t:property name="planoGestao" 				type="hidden" write="true" />				
							<t:property name="ugRegistro" 				write="true" type="hidden" 	style="width: 150px" />
							<t:property name="ugResponsavel" 			write="true" type="hidden"	style="width: 150px"/>
							<t:property name="local" 					disabled="disabled"			style="width: 150px" />
							<t:property name="dataAbertura"				type="hidden" write="true"	style="width: 100px"/>
							<c:if test="${anomalia.dataDestravamento != null}">
								<t:property name="dataDestravamento" type="hidden" write="true"/>
							</c:if>						
							<t:property name="responsavel" 				type="hidden" write="true"/>
							<t:property name="classificacao" 			write="true" type="hidden"	style="width: 150px"/>
							<t:property name="tipo" 					write="true" type="hidden"	style="width: 150px"/>
							<t:property name="origem" 					write="true" type="hidden"	style="width: 150px"/>
							<c:if test="${anomalia.origem == 'Auditoria interna'}">
								<t:property name="tipoAuditoria"		write="true" type="hidden"	style="width: 150px"/>	
							</c:if>
							<t:property name="descricao" 				rows="8" cols="100" label="Descrição da anomalia" type="hidden" write="true"/>
							<t:property name="contraMedidasImediatas"	rows="8" cols="100" type="hidden" write="true"/>
							<t:property name="observacoes" 				rows="8" cols="100" type="hidden" write="true"/>
					</t:tabelaEntrada>
				</c:otherwise>
			</c:choose>
			<t:detalhe name="anexosAnomalia" labelnovalinha="Novo anexo">
				<n:column header="Nome">
					<t:property name="id" write="false" type="hidden"/>
					<t:property name="nome" style="width:98%" bodyStyle="white-space:nowrap"/>
				</n:column>
				<t:property name="descricao" rows="2" cols="30" style="width:98%"/>
				<t:property name="arquivo" showRemoverButton="false" style="overflow:hidden;"/>
			</t:detalhe>			
				<c:if test="${param.ACAO == 'editar'}">
					<jsp:include page="/WEB-INF/jsp/sgm/crud/anomaliaCausaTratamento.jsp" />
				</c:if>
			</t:janelaEntrada>
			<table class="form_filtro_pesquisar">
				<tr>
					<td>
						<c:if test="${!empty anomalia.id}">
							<n:submit class="botao_normal" action="gerar" url="/sgm/report/Anomalia" parameters="${anomalia}" validate="true">imprimir</n:submit>
							&nbsp;
						</c:if>
						<c:if test="${podeDestravarAnomalia}">
							<n:submit class="botao_normal" action="destravar" validate="true">desbloquear</n:submit>
							&nbsp;
						</c:if>
						<c:if test="${exibirBotaoSolicitarEncerramento}">
							<n:submit class="botao_normal" action="solicitarEncerramento" validate="true">solicitar encerramento</n:submit>
							&nbsp;
						</c:if>
						<c:if test="${exibirBotaoNotificarCumprimentoPendente}">
							<n:submit class="botao_normal" action="notificarCumprimentoPendente" validate="true">notificar cumprimento pendente</n:submit>
							&nbsp;
						</c:if>
						<c:if test="${exibirBotaoSolicitarReanalise}">
							<n:submit class="botao_normal" action="solicitarReanalise" validate="true">solicitar reanálise</n:submit>
							&nbsp;
						</c:if>
					</td>
				</tr>
			</table>
		</jsp:body>
	</t:entrada>
</c:if>	
			
<script type="text/javascript">

	function alteraOrigemAnomalia(){
		var row1 = document.getElementsByName('ugResponsavel')[0];
		var row2 = document.getElementsByName('ugRegistro')[0];
		var row3 = document.getElementsByName('local')[1];
		if(row1.value != '<null>' && row2.value != '<null>'){
			if (row1.value == row2.value){
				row3.options[1].selected = true;
			} else {
				row3.options[2].selected = true;
			}
		}else{
			row3.options[0].selected = true;
		}
	}
	
	function changeOrigem(el){
	
		if(el.value=="AUDITORIA_INTERNA") $("#tipoAuditoria").parent().parent().css("display", "table-row");
		else $("#tipoAuditoria").parent().parent().css("display", "none");
	}
	
	$(function(){
		$("#tipoAuditoria").parent().parent().css("display", $("#origem").val()=="AUDITORIA_INTERNA" ? "table-row" : "none");
	})
	
	function abrirEscolha(){
		if(validateForm()){
			var analiseCausas  = $('#idAnaliseCausas').val();
			var statusAnomalia = '${anomalia.status.name}';
			var existePlanoDeAcao = false;
			var planosDeAcaoConcluidos = true;
			
			if (analiseCausas != null && analiseCausas != '' && (statusAnomalia == 'ABERTA' || statusAnomalia == 'TRATADA' || statusAnomalia == 'TRATAMENTO_PENDENTE' || statusAnomalia == 'CUMPRIMENTO_PENDENTE')) {
				$('select[name*="planosAcao"][name*="status"] option:selected').each(function(index) {			
	    			existePlanoDeAcao = true;
	    			if ($(this).val() == 'CONCLUIDO') {
	    				planosDeAcaoConcluidos = planosDeAcaoConcluidos && true;
	    			}
	    			else {
	    				planosDeAcaoConcluidos = planosDeAcaoConcluidos && false; 
	    			} 
	  			});
	  			
				$('input:hidden[name*="planosAcao"][name*="status"]').each(function(index) {
	    			existePlanoDeAcao = true;
	    			if ($(this).val() == 'CONCLUIDO') {
	    				planosDeAcaoConcluidos = planosDeAcaoConcluidos && true;
	    			}
	    			else {
	    				planosDeAcaoConcluidos = planosDeAcaoConcluidos && false; 
	    			}
	  			});
	  		}
			
			if (existePlanoDeAcao && planosDeAcaoConcluidos) {
				openModalWindow('${app}/escolhaSolicitarEncerramento.jsp',650,200, false);
			}
			else {
				submitSalvar();
			}
		}
	}
	
	function submitSalvar() {
		$("#salvar_button").click();
	}

	function submitSalvarSolicitandoEncerramento() {
		$("#salvar_button_sol_enc").click();
	}		
</script>
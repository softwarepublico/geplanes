<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="n" uri="neo"%>
<%@ taglib prefix="t" uri="template"%>
<%@ taglib prefix="f" tagdir="/WEB-INF/tags/"%>

<c:if test="${podeConsultarAuditoriaInterna}">
	<t:entrada titulo="Cadastro de Auditorias internas" showSaveLink="false">
		<jsp:attribute name="linkArea">
			<c:if test="${podeEditarAuditoriaInterna}">
				<button type="button" onclick="javascript:abrirEscolha()" title="" id='btn_gravar' title='Gravar' checkpermission='true' class='botao_normal'>Salvar</button>
			</c:if>
		</jsp:attribute>
		<jsp:body>
			<t:property name="id" write="false" type="hidden"/>
			<t:property name="status" write="false" type="hidden"/>
			<input type="hidden" name="reload" value="" id="idReload">
			<t:janelaEntrada>
				<t:tabelaEntrada>
					<c:choose>
						<c:when test="${auditoriaInterna.status.name == 'ABERTA'}">
							<t:property name="planoGestao" onchange="recarregarTela()" style="width: 100px;"/>				
							<t:property name="ugRegistro" itens="${listaUGAuditoriaInterna}"/>				
							<n:output styleClass="desc10bold" value="Área auditada"/>
							<n:panel>
								<f:unidadeGerencialInput name="ugResponsavel" estiloclasse="required"/>
							</n:panel>
							<t:property name="norma" onchange="recarregarTela()"/>
							<t:property name="dataAuditoria"/>
							<c:if test="${auditoriaInterna.dataEncerramento != null}">
								<t:property name="dataEncerramento"	type="hidden" write="true"/>
							</c:if>					
						</c:when>
						<c:otherwise>
							<t:property name="planoGestao" type="hidden" write="true"/>				
							<t:property name="ugRegistro"  type="hidden" write="true"/>				
							<t:property name="ugResponsavel"  type="hidden" write="true"/>
							<t:property name="norma"  type="hidden" write="true"/>
							<t:property name="dataAuditoria" type="hidden" write="true"/>
							<c:if test="${auditoriaInterna.dataEncerramento != null}">
								<t:property name="dataEncerramento"	type="hidden" write="true"/>
							</c:if>					
						</c:otherwise>
					</c:choose>
					
					<t:property name="observacoes" rows="8" cols="120"/>
				</t:tabelaEntrada>
				<t:detalhe name="listaEquipeAuditora" labelnovalinha="Novo auditor"  showBotaoNovaLinha="${podeEditarAuditoriaInterna}" showColunaAcao="${podeEditarAuditoriaInterna}" showBotaoRemover="${podeEditarAuditoriaInterna}">
					<n:column header="Auditor">
						<n:body>
							<t:property name="id" type="hidden"/>
							<t:property name="nome" style="width:600px"/>
						</n:body>
					</n:column>
					<t:property name="funcao" style="width:300px"/>
				</t:detalhe>
				<t:detalhe name="listaEquipeAuditada" labelnovalinha="Novo auditado"  showBotaoNovaLinha="${podeEditarAuditoriaInterna}" showColunaAcao="${podeEditarAuditoriaInterna}" showBotaoRemover="${podeEditarAuditoriaInterna}">
					<n:column header="Auditado">
						<n:body>
							<t:property name="id" type="hidden"/>
							<t:property name="nome" style="width:600px"/>
						</n:body>
					</n:column>
					<t:property name="funcao" style="width:300px"/>
				</t:detalhe>		
				<t:detalhe name="listaNaoConformidades" labelnovalinha="Novo item"  showBotaoNovaLinha="${podeEditarAuditoriaInterna}" showColunaAcao="${podeEditarAuditoriaInterna}" showBotaoRemover="${podeEditarAuditoriaInterna}">
					<n:column header="Requisito">
						<n:body>
							<t:property name="id" type="hidden"/>
							<t:property name="requisitoNorma" itens="${listaRequisitoNormaNaoConformidades}" style="width:170px"/>
						</n:body>
					</n:column>
					<t:property name="descricao" rows="2" cols="100"/>
					<t:property name="ugExterna" itens="${listaUGExterna}"/>
				</t:detalhe>
				<t:detalhe name="listaOutrasNaoConformidades" labelnovalinha="Novo item" showBotaoNovaLinha="${podeEditarAuditoriaInterna}" showColunaAcao="${podeEditarAuditoriaInterna}" showBotaoRemover="${podeEditarAuditoriaInterna}">
					<n:column header="Norma">
						<n:body>
							<t:property name="id" type="hidden"/>
							<t:property name="norma" itens="${listaOutrasNormas}" onchange="carregaRequisitosNormaOutrasNaoConformidades(this)" style="width:130px"/>
						</n:body>							
					</n:column>
					<n:column header="Requisito" style="width:170px">
						<n:body>
							<c:choose>
								<c:when test="${n:ognl(\"index instanceof java.lang.Number\")}">
									<t:property name="requisitoNorma" itens="${listaRequisitoNormaOutrasNaoConformidades[index]}" style="width:170px"/>
								</c:when>
								<c:otherwise>
									<t:property name="requisitoNorma" itens="0" style="width:170px"/>
								</c:otherwise>
							</c:choose>
						</n:body>
					</n:column>												
					<t:property name="descricao" rows="2" cols="60"/>
					<t:property name="ugExterna" itens="${listaUGExterna}"/>
				</t:detalhe>
			</t:janelaEntrada>
			<n:panel style="padding-left:890px;">
				<n:submit action="salvar" class="botao_normal" validate="true" style="display:none;" id="button_salvar">salvar</n:submit>
				<n:submit action="salvarESolicitarEncerramento" class="botao_normal" validate="true" style="display:none;" id="button_salvarESolicitarEncerramento">salvar e solicitar encerramento</n:submit>
				<n:submit action="salvarEEncerrar" class="botao_normal" validate="true" style="display:none;" id="button_salvarEEncerrar">salvar e encerrar</n:submit>
			</n:panel>		
		</jsp:body>
	</t:entrada>
</c:if>

<script language="javascript">

	function recarregarTela(){
		form.validate = 'false'; 
		form.suppressErrors.value = 'true';
		form.ACAO.value = 'entrada';
		form.suppressValidation.value = 'true';
		document.getElementById('idReload').value = 'true';
		submitForm();
	}
	
	function abrirEscolha() {
		if (validateForm()) {
			var statusAuditoria = "${auditoriaInterna.status.name}";
			openModalWindow('${app}/escolhaAcaoAuditoriaInterna.jsp?status=' + statusAuditoria,650,200, false);
		}
	}
	
	function salvarAuditoria(){
		$("#button_salvar").click();
	}
	
	function salvarAuditoriaESolicitarEncerramento(){
		$("#button_salvarESolicitarEncerramento").click();
	}
	
	function salvarAuditoriaEEncerrar(){
		$("#button_salvarEEncerrar").click();
	}
	
	<c:if test="${!podeEditarAuditoriaInterna}">
		$(function(){
			$("select, input[type!='hidden'], textarea").each(function(){
				$(this).attr("disabled", "disabled");
			});
		});
	</c:if>
	
	function carregaRequisitosNormaOutrasNaoConformidades(elemento) {
		var idxRequisito = -1;
		var re = /listaOutrasNaoConformidades\[(\d+)\]\.norma/;	
		var match = re.exec(elemento.name);
		if (match) {
			idxRequisito = match[1];
		}	
		setTimeout(function() {ajaxGerenciaRequisitos(idxRequisito,elemento)}, 1);
	}
	
	function ajaxGerenciaRequisitos(idxRequisito,elNorma) {
		var idNorma = getObjectId(elNorma.value);
		
		$.post("${ctx}/sgm/crud/AuditoriaInterna",
			{'ACAO':'ajaxComboRequisito', 'idNorma':idNorma},
			 function(data) {
			 	data = stripJavascriptTags(data);
				eval(data);
				var combo = createComboDataModelByList(listaRequisitoNorma);

				// Preenche as opções do combo correspondente de requisitos relacionados à norma selecionada
				$("select[name='listaOutrasNaoConformidades\[" + idxRequisito + "\]\.requisitoNorma']").html(combo);
			}
		);	
	}
	
	function getObjectId(obj) {
		var re = /.*\[id=(\d+)\]/;
		var id = "";
		var match = re.exec(obj);
		if (match) {
			id = match[1];
		}
		return id;	
	}
	
	function createComboDataModelByList(lista) {
		var text = " ";
		var i;
	
		if (!lista || lista.length == 0) {
			text = "";
		}
	
		var combo = "<option value='<null>'>"+text+"</option>";
		if (lista) {
			for (i=0;i<lista.length;i++) {
				combo+="<option value='"+lista[i][0]+"' >"+lista[i][1]+"</option>";
			}
		}	
		combo += "<span class='required'></span>";
		return combo;
	}
	
</script>
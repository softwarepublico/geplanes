<%@page import="br.com.linkcom.sgm.service.FatorAvaliacaoService"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="n" uri="neo"%>
<%@ taglib prefix="t" uri="template"%>

<t:entrada titulo="Modelo de auditoria de gestão" submitConfirmationScript="validaFormulario()">
	<t:property name="id" write="false" type="hidden"/>
	<t:janelaEntrada>
		<t:tabelaEntrada>
			<c:if test="${nao_editar == null || !nao_editar}">
				<t:property name="nome"/>
			</c:if>
			<c:if test="${nao_editar != null && nao_editar}">
				<t:property name="nome" type="hidden" write="true"/>
			</c:if>
		</t:tabelaEntrada>
		<t:detalhe name="listaItemModeloAuditoriaGestao" showColunaAcao="${nao_editar == null || !nao_editar}" showBotaoNovaLinha="${nao_editar == null || !nao_editar}" showBotaoRemover="${nao_editar == null || !nao_editar}" labelnovalinha="Novo item">
			<c:if test="${nao_editar == null || !nao_editar}">
				<n:column header="Nome">
					<n:body>
						<t:property name="id" type="hidden"/>
						<t:property name="nome"  style="width: 200px;"/>
					</n:body>
				</n:column>
				<t:property name="descricao"  rows="2" style="width:450px;"/>
				<t:property name="fatorAvaliacao" style="width: 200px;" itens="<%=FatorAvaliacaoService.getInstance().find(Boolean.FALSE)%>"/>
				<t:property name="ordem"  style="width: 40px;"/>
			</c:if>
			<c:if test="${nao_editar != null && nao_editar}">
				<n:column header="Nome">
					<n:body>
						<t:property name="id" type="hidden"/>
						<t:property name="nome" type="hidden" write="true"/>
					</n:body>
				</n:column>
				<t:property name="descricao" type="hidden" write="true"/>
				<t:property name="fatorAvaliacao" type="hidden" write="true"/>
				<t:property name="ordem"  type="hidden" write="true"/>
			</c:if>
		</t:detalhe>
	</t:janelaEntrada>
</t:entrada>

<script type="text/javascript">
	function validaFormulario(){
		var msg = '';
		var show = false;
		
		if($("input[name^=listaItemModeloAuditoriaGestao][name$=nome]").length == 0){
			msg += 'O modelo de auditoria deve possuir pelo menos um item.\n';
			show = true;
		}
		
		if(show){
			alert(msg);
		}
		
		return !show;
	}
</script>

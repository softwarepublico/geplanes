<%@page import="br.com.linkcom.sgm.beans.UnidadeGerencial"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="n" uri="neo"%>
<%@ taglib prefix="t" uri="template"%>
<%@ taglib prefix="code" uri="code"%>
<%@ taglib prefix="f" tagdir="/WEB-INF/tags/"%>

<t:tela titulo="Definição do Negócio">
	<input type="hidden" name="reload" value="" id="idReload">
	<t:janelaFiltro>
		<t:tabelaFiltro showSubmit="false">
			<t:property name="planoGestao"	style="width: 100px;"/>
			<n:output styleClass="desc11" value="Unidade Gerencial"/>
			<n:panel>
				<f:unidadeGerencialInput name="unidadeGerencial" onchange="recarregarTela()" estiloclasse="required"/>
			</n:panel>
		</t:tabelaFiltro>		
	</t:janelaFiltro>
   	<br>
	<c:if test="${filtro.planoGestao != null && filtro.unidadeGerencial != null && !SEMPERMISSAO}">
	 	<n:bean name="unidadeGerencial" valueType="<%=UnidadeGerencial.class %>">
	 	<t:janelaEntrada showSubmit="false">
	 		<t:tabelaEntrada title="Definição do Negócio" style="border-collapse:separate;">
				<t:property name="mapaNegocio.id" write="false" type="hidden" label=" "/>
				<t:property name="mapaNegocio.unidadeGerencial" write="false" type="hidden" label=" "/>
				<n:panel colspan="2">
		 			<n:panelGrid columns="4">
						 <t:property name="mapaNegocio.missao" 			rows="4" cols="50"/>
						 <t:property name="mapaNegocio.fornecedores" 	rows="4" cols="50"/>
						 <t:property name="mapaNegocio.insumos"			rows="4" cols="50"/>
						 <t:property name="mapaNegocio.negocio" 		rows="4" cols="50"/>
					  	 <t:property name="mapaNegocio.pessoal" 		rows="4" cols="50"/>
					 	 <t:property name="mapaNegocio.equipamentos" 	rows="4" cols="50"/>
					  	 <t:property name="mapaNegocio.produtos" 		rows="4" cols="50"/>
					  	 <t:property name="mapaNegocio.clientes" 		rows="4" cols="50"/>
					</n:panelGrid>
				</n:panel>
			</t:tabelaEntrada>
			<%--
			<t:tabelaEntrada title="Definição de Indicadores" style="border-collapse:separate;">
			 	<n:panelGrid columns="2">
				  <t:property name="mapaNegocio.produto" rows="2" cols="45"/>
				  <t:property name="mapaNegocio.cliente" rows="2" cols="45"/>
				</n:panelGrid>
				<n:panel></n:panel>
			    <n:group legend="Qualidade" columns="2"> 
				  <t:property name="mapaNegocio.descQualidade" 	rows="2" cols="45"/>
				  <t:property name="mapaNegocio.indQualidade" 	rows="2" cols="45"/>
				  <t:property name="mapaNegocio.exprQualidade" 	rows="2" cols="45"/>
				  <t:property name="mapaNegocio.freqQualidade" 	rows="2" cols="45"/>
				  <t:property name="mapaNegocio.metaQualidade" 	rows="2" cols="45"/>
				 </n:group> 
			    <n:group legend="Custo" columns="2"> 			 
				  <t:property name="mapaNegocio.descCusto" 		rows="2" cols="45"/>
				  <t:property name="mapaNegocio.indCusto" 		rows="2" cols="45"/>
				  <t:property name="mapaNegocio.exprCusto" 		rows="2" cols="45"/>
				  <t:property name="mapaNegocio.freqCusto" 		rows="2" cols="45"/>
				  <t:property name="mapaNegocio.metaCusto" 		rows="2" cols="45"/>
				</n:group>
				<n:group legend="Entrega/Atendimento" columns="2"> 
				  <t:property name="mapaNegocio.descEntrega" 	rows="2" cols="45"/>
				  <t:property name="mapaNegocio.indEntrega" 	rows="2" cols="45"/>
				  <t:property name="mapaNegocio.exprEntrega" 	rows="2" cols="45"/>
				  <t:property name="mapaNegocio.freqEntrega" 	rows="2" cols="45"/>
				  <t:property name="mapaNegocio.metaEntrega" 	rows="2" cols="45"/>
				</n:group>
			    <n:group legend="Segurança" columns="2"> 			  
				  <t:property name="mapaNegocio.descSeguranca" 	rows="2" cols="45"/>
				  <t:property name="mapaNegocio.indSeguranca" 	rows="2" cols="45"/>
				  <t:property name="mapaNegocio.exprSeguranca" 	rows="2" cols="45"/>
				  <t:property name="mapaNegocio.freqSeguranca" 	rows="2" cols="45"/>
				  <t:property name="mapaNegocio.metaSeguranca" 	rows="2" cols="45"/>
				 </n:group> 
			</t:tabelaEntrada>
			--%>
		</t:janelaEntrada>					
		<c:if test="${empty HIDEBOTAOSALVAR}">
			<div style="text-align: right">
				<br>
				<n:submit action="salvar" class="botao_normal" validate="true">salvar</n:submit>
			</div>
		 </c:if>
	
		</n:bean>	
	</c:if>
</t:tela>

<script type="text/javascript">

	function recarregarTela(){
		form.validate = 'false'; 
		form.suppressErrors.value = 'true';
		form.ACAO.value = 'entrada';
		form.suppressValidation.value = 'true';
		document.getElementById('idReload').value = 'true';
		submitForm();
	}
	
</script>
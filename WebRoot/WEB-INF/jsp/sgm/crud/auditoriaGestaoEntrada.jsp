
<%@page import="br.com.linkcom.sgm.beans.AuditoriaGestaoIndicadorItem"%>
<%@page import="br.com.linkcom.sgm.beans.AuditoriaGestaoIndicador"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="n" uri="neo"%>
<%@ taglib prefix="t" uri="template"%>
<%@ taglib prefix="f" tagdir="/WEB-INF/tags"%>

<t:entrada titulo="Auditoria de gestão">
	<t:property name="id" type="hidden"/>
	<input type="hidden" name="reload" value="" id="idReload">
	<t:janelaEntrada>
		<t:tabelaEntrada>
			<c:if test="${cadastro_detalhe == null || !cadastro_detalhe}">
				<t:property name="planoGestao" style="width: 100px;"/>
				<n:output styleClass="desc10bold" value="Unidade Gerencial"/>
				<n:panel>
					<f:unidadeGerencialInput name="unidadeGerencial" onchange="recarregarTela()" estiloclasse="required"/>
				</n:panel>
				<t:property name="modeloAuditoriaGestao" onchange="recarregarTela()"/>
			</c:if>
			<c:if test="${cadastro_detalhe != null && cadastro_detalhe}">
				<t:property name="planoGestao" type="hidden" write="true"/>
				<t:property name="unidadeGerencial" type="hidden" write="true"/>
				<t:property name="modeloAuditoriaGestao" type="hidden" write="true"/>
			</c:if>
			<t:property name="descricao"/>
			<t:property name="responsavel"/>
			<t:property name="dataAuditoria"/>
		</t:tabelaEntrada>
		
		<c:if test="${cadastro_detalhe != null && cadastro_detalhe}">
			<n:panel title="Indicadores">
				<n:panelGrid columns="2">
						<t:propertyConfig mode="input" showLabel="false" renderAs="double">
							<n:output styleClass="desc10bold" value="Indicador"/>
							<t:property name="indicador" itens="${listaIndicadores}" onchange="mostraIndicador()"/>
							<n:panel><BR></n:panel>
							<n:panel><BR></n:panel>
						</t:propertyConfig>
				</n:panelGrid>
				<n:panelGrid columns="1">
					<n:dataGrid cellspacing="0" itens="${auditoriaGestao.listaAuditoriaGestaoIndicador}" var="auditoriaGestaoIndicador" varIndex="index" headerStyle="display:none;" bodyStyleClasses="listagemBody1,listagemBody1">
						<n:bean name="auditoriaGestaoIndicador" propertyPrefix="listaAuditoriaGestaoIndicador" propertyIndex="${index}" valueType="<%=AuditoriaGestaoIndicador.class%>">
							<t:propertyConfig mode="input" showLabel="false" renderAs="double">
								<n:column>
									<n:body id="indicador${auditoriaGestaoIndicador.indicador.id}" style="display:none;">
										<t:property name="id" type="hidden"/>
										<t:property name="indicador" type="hidden"/>
										<n:dataGrid bodyStyles="height:30px;" itens="${auditoriaGestaoIndicador.listaAuditoriaGestaoIndicadorItem}" var="auditoriaGestaoIndicadorItem" varIndex="indexitem" headerStyle="display:none;"  bodyStyleClasses="listagemBody2,listagemBody2">
											<n:bean name="auditoriaGestaoIndicadorItem" propertyPrefix="listaAuditoriaGestaoIndicador[${index}].listaAuditoriaGestaoIndicadorItem" propertyIndex="${indexitem}" valueType="<%=AuditoriaGestaoIndicadorItem.class%>">
												<n:column>
													<n:body style="font-size: 12px;">
														<t:property name="itemModeloAuditoriaGestao" type="hidden" write="true"/>
														<t:property name="id" type="hidden"/>
													</n:body>
												</n:column>
												<c:if test="${auditoriaGestaoIndicadorItem.itemModeloAuditoriaGestao.fatorAvaliacao != null}">
													<t:property name="itemFatorAvaliacao" type="select_one_radio" includeBlank="false" bodyStyle="font-size: 12px;" itens="${auditoriaGestaoIndicadorItem.itemModeloAuditoriaGestao.fatorAvaliacao.listaItemFatorAvaliacao}"/>
												</c:if>
												<c:if test="${auditoriaGestaoIndicadorItem.itemModeloAuditoriaGestao.fatorAvaliacao == null}">
													<t:property name="texto" rows="2" style="width: 700px;"/>
												</c:if>										
											</n:bean>
										</n:dataGrid>
									</n:body>
								</n:column>
							</t:propertyConfig>
						</n:bean>
					</n:dataGrid>
				</n:panelGrid>
			</n:panel>
		</c:if>
	</t:janelaEntrada>
</t:entrada>

<script language="javascript">
	$(document).ready(function(){
		$("input[type=radio]").each(function(){
			$(this).css("border","none");
		});
	});

	function recarregarTela(){
		if(confirmaCadastroDetalhe()){
			form.validate = 'false'; 
			form.suppressErrors.value = 'true';
			form.ACAO.value = 'entrada';
			form.suppressValidation.value = 'true';
			document.getElementById('idReload').value = 'true';
			submitForm();
		}
	}
	
	function confirmaCadastroDetalhe(){
		if(form['planoGestao'].value != "<null>" && 
			form['unidadeGerencial'].value != "<null>" && 
			form['modeloAuditoriaGestao'].value != "<null>"){
			return confirm('Deseja realmente cadastrar auditoria com as seguintes informações?\n\n' +
							'Ano da gestão: ' +  getComboLabelSelected(form['planoGestao']) + '\n' +
							'Unidade gerencial: ' + form['unidadeGerencial_label'].value + '\n' +
							'Modelo de auditoria: ' + getComboLabelSelected(form['modeloAuditoriaGestao']));	
		} else {
			return true;
		}
			
	}
	
	function getComboLabelSelected(combo){
		return combo.options[combo.selectedIndex].text;
	}
	
	function getComboIdSelected(combo){
		var value = combo.options[combo.selectedIndex].value;
		if(value != "<null>")
			return value.substring(value.lastIndexOf("=")+1,value.lastIndexOf("]")); 
		else
			return value;
	}
	
	function mostraIndicador(){
		var indicador = getComboIdSelected(form['indicador']);
		$("td[id^=indicador]").each(function(){
			$(this).hide();
		});
		
		if(indicador != "<null>"){
			$("td[id=indicador" + indicador + "]").fadeIn();
		}
	}
	
</script>

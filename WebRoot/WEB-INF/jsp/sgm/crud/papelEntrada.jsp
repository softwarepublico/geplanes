<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="n" uri="neo"%>
<%@ taglib prefix="t" uri="template"%>

<t:entrada titulo="Cadastro de Níveis de Acesso">
	<t:janelaEntrada>
		<t:tabelaEntrada>
			<t:property name="id"/>
			<t:property name="nome"/>
			<t:property name="administrador" id="idAdministrador"/>
		</t:tabelaEntrada>
		<n:panel title="Autorização">
			<c:forEach items="${papel.groupAuthorizationMap}" var="item">
				<t:janelaResultados>
					<n:panelGrid columns="1" rowStyleClasses="filtro1" width="100%">
						<n:panel style="color: #333333">&nbsp;&nbsp;&nbsp;<b><i>${item.key}</i></b></n:panel>
					</n:panelGrid>
					<n:dataGrid itens="${item.value}" width="100%" cellspacing="1" headerStyleClass="listagemHeader" bodyStyleClasses="listagemBody1, listagemBody2" footerStyleClass="listagemFooter">
						<n:bean name="row" propertyPrefix="groupAuthorizationMap[${item.key}][${index}]" valueType="${authorizationProcessItemFilterClass}">
		
						<n:column header="Tela">
							<t:property name="description"/>				
							<t:property name="path" mode="input" type="hidden"/>						
						</n:column>
						
						<c:forEach items="${mapaGroupModule[item.key].authorizationItens}" var="authorizationItem">
							<n:column header="${authorizationItem.nome}" width="80px" style="text-align:center">
								<c:if test="${fn:length(authorizationItem.valores) == 2}">
									<%-- Possibilidade de ser true false --%>
									<n:property name="permissionMap[${authorizationItem.id}]">
										<n:input type="checkbox"/>		
									</n:property>
								</c:if>
								<c:if test="${fn:length(authorizationItem.valores) != 2}">
									(Não implementado ainda)
									<n:input itens="${authorizationItem.valores}"/>
								</c:if>						
							</n:column>
						</c:forEach>					
						</n:bean>
					</n:dataGrid>
				</t:janelaResultados>
			</c:forEach>		
		</n:panel>
	</t:janelaEntrada>
</t:entrada>
<script language="javascript">
	$(document).ready(function() {

		$("#idAdministrador").click(function() {
			gerenciaAbaAutorizacao();
		});

		gerenciaAbaAutorizacao();
	});
	
	function gerenciaAbaAutorizacao() {
		if ($("#idAdministrador").attr("checked")) {	
			$("#janelaEntrada_link_1").hide(); // Oculta a aba de Autorização
		}
		else {
			$("#janelaEntrada_link_1").show(); // Torna visível a aba de Autorização
		}		
	}
</script>
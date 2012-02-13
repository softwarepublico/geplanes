<%@page import="br.com.linkcom.sgm.beans.DistribuicaoPesosIndicadores"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="n" uri="neo"%>
<%@ taglib prefix="t" uri="template"%>

<html>
	<head>
		<n:head/>
	</head>
	<body>
	<script language="JavaScript" src="${app}/tooltip/wz_tooltip.js"></script>
	<TABLE class="tabelaBase base_tabelabase" style="width: 100%;">
		<n:bean name="filtro" valueType="<%= DistribuicaoPesosIndicadores.class %>">
			<n:form validate="true">
			<t:property name="unidadeGerencial" type="hidden" write="false"/>				
			<TR>
				<TD style="height: 82px;">
				<TABLE class="tabelaBase" style="width: 100%;">
						<TR>
							<TD rowspan="2" class="base_logo" 
								style="cursor: pointer;" >&nbsp;</TD>
							<TD class="base_user">
								<SPAN class="txt_normal">${USER.nome}</SPAN>
							</TD>
						</TR>
						<TR>
							<TD class="base_menu"valign="top" align="right">
							 <div class="menuSGI" onmouseover="esconderTabela()" onmouseout="esconderTabela();">
							</TD>
						</TR>
					</TABLE> 		
				</TD>
			</TR>
			<TR>
				<TD valign="top">
					<div style="overflow:auto;height:380px">
						<n:panelGrid columns="1" columnStyleClasses="propertyColumn,propertyColumn" columnStyles="padding-right:0px" style="width: 100%;">
							<n:panel class="desc14bold">Selecione abaixo os objetivos estratégicos que serão exibidos no Painel de indicadores.</n:panel>
							<c:set var="idxObjetivoEstrategico" value="0"/>
							<c:forEach var="perspectivaMapaEstrategico" items="${listaPerspectivaMapaEstrategico}">
								<n:panel class="tituloColunaPainelIndicadorFiltro"><br>${perspectivaMapaEstrategico.perspectiva.descricao}</n:panel>						
								<n:panelGrid columns="1" columnStyleClasses="propertyColumn,propertyColumn" columnStyles="padding-right:0px">
									<c:forEach var="objetivoMapaEstrategico" items="${perspectivaMapaEstrategico.listaObjetivoMapaEstrategico}" varStatus="status">
										<n:panel>
											<c:choose>
												<c:when test="${objetivoMapaEstrategico.somenteLeitura}">
													<n:input name="listaObjetivoMapaEstrategicoFiltro[${idxObjetivoEstrategico}].id" id="${objetivoMapaEstrategico.id}" type="checklist" value="${objetivoMapaEstrategico.id}" disabled="disabled" checked="checked"/>${objetivoMapaEstrategico.objetivoEstrategico.descricao}
													<input type="hidden" name="listaObjetivoMapaEstrategicoFiltro[${idxObjetivoEstrategico}].id" value="${objetivoMapaEstrategico.id}">
												</c:when>
												<c:otherwise>
													<n:input name="listaObjetivoMapaEstrategicoFiltro[${idxObjetivoEstrategico}].id" id="${objetivoMapaEstrategico.id}" type="checklist" value="${objetivoMapaEstrategico.id}"/>${objetivoMapaEstrategico.objetivoEstrategico.descricao}
												</c:otherwise>
											</c:choose>
										</n:panel>
										<c:set var="idxObjetivoEstrategico" value="${idxObjetivoEstrategico + 1}"/>
									</c:forEach>							
								</n:panelGrid>
							</c:forEach>
						</n:panelGrid>
					</div>
				</TD>
			</TR>
			<TR>
				<TD align="center">
					<n:link type="button" class="botao_normal" url="javascript:cancelar();">Cancelar</n:link>
					&nbsp;&nbsp;
					<n:submit action="salvaPopUpConfiguraFiltro" class="botao_normal">Salvar</n:submit>
				</TD>
			</TR>
			</n:form>
		</n:bean>
		</TABLE>
	</body>
</html>

<script type="text/javascript">

	$(document).ready(function() {
		// Desmarca tudo
		$('input[type=checkbox][name^="listaObjetivoMapaEstrategicoFiltro"]').each(function() {
			$(this).attr("checked",null);
		});
		
		// Marca somente os que já estão escolhidos
		$('input[type=checkbox][name^="listaObjetivoMapaEstrategicoFiltro"]').each(function() {
			<c:forEach items="${filtro.listaObjetivoMapaEstrategicoFiltro}" var="objMapEstrat">
				if ($(this).attr("id") == ${objMapEstrat.id}) {
					$(this).attr("checked","checked");
				}
				
			</c:forEach>
		});
	});
	
	function cancelar(){
		parent.$.akModalRemove();
	}
</script>
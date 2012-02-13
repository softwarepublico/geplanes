
<%@page import="br.com.linkcom.sgm.beans.ComentarioItem"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="n" uri="neo"%>
<%@ taglib prefix="t" uri="template"%>

<html>
	<head>
		<n:head/>
		<script language="JavaScript" src="${ctx}/javascript/ajquery.js"></script>
		<script language="JavaScript" src="${ctx}/javascript/jquery.maskedinput-1.1.1.pack.js"></script>
		<script language="JavaScript" src="${ctx}/javascript/dimensions.pack.js"></script>
		<script language="JavaScript" src="${ctx}/javascript/dimmer.js"></script>
		<script language="JavaScript" src="${ctx}/javascript/treetable.js"></script>
		<script language="JavaScript" src="${ctx}/javascript/GeplanesUtil.js"></script>
		<script language="JavaScript" src="${ctx}/javascript/thickbox-compressed.js"></script>
		<script language="JavaScript" src="${ctx}/javascript/jquery.bgiframe.min.js"></script>
		<script language="JavaScript" src="${ctx}/javascript/jquery.autocomplete.js"></script>
		<script language="JavaScript" src="${ctx}/javascript/jquery.maskedinput-1.1.1.js"></script>
		<script language="JavaScript" src="${ctx}/javascript/jquery.ajaxQueue.js"></script>
		<script language="JavaScript" src="${ctx}/javascript/akModal.js"></script>		
	</head>
	
	<body leftmargin="0" topmargin="0" style="padding:0px" bgColor="#FFFFFF" style="background-color: #FFFFFF;overflow:hidden;">
		<n:form validate="true">
			<n:validation>
				<table class="lookup_janela">
					<TR>
						<TD class="fd_tela_titulo" style="height: 20px;" >Comentários</TD>
					</TR>
					<TR>
						<TD valign="top">
							<n:dataGrid itens="${listaComentarioItem}" var="comentarioItem" id="tabelaResultados" headerStyleClass="txt_tit" bodyStyleClasses="txt_l1, txt_l2" footerStyleClass="listagemFooter" styleClass="fd_tabela1">
								<n:bean name="comentarioItem" valueType="<%= ComentarioItem.class %>">
									<t:property name="usuario.nome"/>
									<t:property name="data"/>
									<t:property name="texto"/>
								</n:bean>
							</n:dataGrid>					
						</TD>
					</TR>
					<c:if test="${podeIncluir}">
						<TR>
							<TD class="fd_tela_titulo" style="height: 20px;" >Incluir comentário</TD>
						</TR>
						<TR>
							<TD valign="top">
								<n:bean name="comentarioitem">
									<t:property name="solicitacaoCancelamentoIndicador" mode="input" type="hidden" showLabel="false"/>
									<t:property name="comentario" mode="input" type="hidden" showLabel="false"/>
									<t:property name="usuario" mode="input" type="hidden" showLabel="false"/>
									<t:property name="data" mode="input" type="hidden" showLabel="false"/>
									<t:propertyConfig showLabel="true" renderAs="double">	
										<t:janelaResultados>
											<n:group columns="2" showBorder="false">
												<t:property name="texto" type="TEXT_AREA" mode="input" style="width:350px;height:100px" onKeyPress="return contadorLetras(\"texto\",3000,event)" onkeyUp="return contadorLetras(\"texto\",3000,event)" />
											</n:group>
											<div align="right" style="padding-right:20px;">
												<n:submit url="/sgm/process/SolicitacaoCancelamentoIndicador" action="incluirComentario" class="botao_normal">Incluir</n:submit>
											</div>
										</t:janelaResultados>
									</t:propertyConfig>
								</n:bean>			
							</TD>
						</TR>							
					</c:if>
				</table>
		</n:validation>
	 </n:form>
	</body>
</html>
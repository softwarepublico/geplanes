<%@page import="br.com.linkcom.neo.core.standard.Neo"%>
<%@page import="br.com.linkcom.neo.util.Util"%><%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="n" uri="neo"%>
<c:set var="PODE_EXCLUIR" scope="page" value="<%=new Boolean(Neo.getApplicationContext().getAuthorizationManager().isAuthorized(Util.web.getFirstUrl(), \"excluir\", Neo.getRequestContext().getUser())) %>" />
<n:form validate="false">
	<n:validation>
		<input type="hidden" name="notFirstTime" value="true"/>
		<table class="outterTable" cellspacing="0" cellpadding="0" align="center">
			<tr class="outterTableHeader">
				<td>
					<span class="outterTableHeaderLeft">
						${listagemTag.titulo}
					</span>
					<span class="outterTableHeaderRight">
						${listagemTag.invokeLinkArea} 
						<c:if test="${!pos_acao_ladoDireito}">
							${acoes}
						</c:if>
						
						<span id="botoesPadrao">
							<c:if test="${empty listagemTag.dynamicAttributesMap['createurl']}">
								<c:if test="${listagemTag.showNewLink || !empty listagemTag.linkArea}">
									<n:link action="criar" id="btn_novo" type="button" class="botao_normal" checkPermission="true">criar</n:link>
								</c:if>
							</c:if>
							<c:if test="${!empty listagemTag.dynamicAttributesMap['createurl']}">
								<c:if test="${listagemTag.showNewLink || !empty listagemTag.linkArea}">
									<n:link class="outterTableHeaderLink" url="${listagemTag.dynamicAttributesMap['createurl']}" id="btn_novo" checkPermission="true">Novo</n:link>
								</c:if>
							</c:if>
							<%--
							<c:if test="${empty listagemTag.dynamicAttributesMap['showdeletelink'] || listagemTag.dynamicAttributesMap['showdeletelink'] || !empty listagemTag.linkArea}">
								<c:if test="${PODE_EXCLUIR}">
									<n:link url="#" class="outterTableHeaderLink" onclick="javascript:excluirItensSelecionados();" id="btn_excluir">Excluir</n:link>
								</c:if>
							</c:if>
							--%>
						</span>
						<c:if test="${pos_acao_ladoDireito}">
							${acoes}
						</c:if>
					</span>
				</td>
			</tr>
			<tr>
				<td>
					<n:doBody />
				</td>
			</tr>
		</table>
	</n:validation>
</n:form>
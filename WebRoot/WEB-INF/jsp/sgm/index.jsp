<%@page import="br.com.linkcom.sgm.util.GeplanesUtils"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="n" uri="neo"%>

<table cellpadding="0" cellspacing="1" border="0" width="100%" height="100%">
	<tr>
		<td style="height: 70px;" valign="top" colspan="3">
			<img src="${ctx}/images/img_tit_sistema.gif" />
		</td>
	</tr>
	<tr>		
		<td valign="top" style="width:50%;border: 3px solid #507D9E; padding: 10px;">
			<font style="color: #507D9E; font-weight: bold;">QUADRO DE PENDÊNCIAS</font>
			<div style="height: 330px; overflow: auto">
				<ul class="mensagens">
					<c:forEach var="controleCadastro" items="${listaControleCadastro}">
						<c:if test="${controleCadastro.pendente}">
						<li>
							<span class="mensagens_data12">
								${controleCadastro.unidadeGerencial.descricao}
								<c:forEach var="controleCadastroItem" items="${controleCadastro.listaControleCadastroItem}">
									<c:if test="${controleCadastroItem.pendente}">
										<br>
										<span class="mensagens_data11">
											<c:choose>
												<c:when test="${controleCadastroItem.exibirLink}">
													<n:link url="${controleCadastroItem.url}" class="mensagens_data11">- ${controleCadastroItem.descricao}</n:link>
												</c:when>
												<c:otherwise>
													- ${controleCadastroItem.descricao}
												</c:otherwise>
											</c:choose>
										</span>								
									</c:if>
								</c:forEach>
							</span>
							<br>							
						</li>
						</c:if>
					</c:forEach>
				</ul>
			</div>
		</td>
		<td style="width:1%">&nbsp;</td>		
		<td valign="top">
			<table cellpadding="0" cellspacing="1" border="0" width="100%" height="100%">
				<tr>
					<td valign="top" style="border: 3px solid #507D9E; padding: 10px; height:250px">
						<font style="color: #507D9E; font-weight: bold;">MENSAGENS</font>
						<div style="height: 250px; overflow: auto;">
							<ul class="mensagens">
								<c:forEach var="msg" items="${mensagens}">
									<li>
										<span class="mensagens_data12">
											${msg.dataLancamentoFormatada}
										</span><br>
										${msg.conteudo}
									</li>
								</c:forEach>
							</ul>
						</div>
					</td>			
				</tr>
				<tr>
					<td align="right">
						<c:set var="logoEmpresaId" value="<%= GeplanesUtils.getLogoEmpresaId() %>"></c:set>
						<c:choose>
							<c:when test="${logoEmpresaId != 0}">
								<img src="${ctx}/DOWNLOADFILEPUB/${logoEmpresaId}"/>
							</c:when>
							<c:otherwise>
								<img src="${ctx}/images/img_empresa.png">
							</c:otherwise>
						</c:choose>					
					</td>
				</tr>				
			</table>		
		</td>
	</tr>
</table>
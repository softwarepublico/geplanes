<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="n" uri="neo"%>
<%@ taglib prefix="t" uri="template"%>

<c:set var="status" value="<%= request.getParameter("status")%>"/>

<html>
	<head>
		<n:head/>
	</head>
	<body>
	<script language="JavaScript" src="${app}/tooltip/wz_tooltip.js"></script>
	<TABLE class="tabelaBase base_tabelabase" style="width: 100%;">
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
							 <div class="menuGeplanes" onmouseover="esconderTabela()" onmouseout="esconderTabela();">
							</TD>
						</TR>
					</TABLE> 		
				</TD>
			</TR>
			<TR>
				<TD align="center">
					<c:if test="${status == 'ABERTA'}">
						<n:link type="button" class="botao_normal" url="javascript:salvar();">Salvar registro</n:link>
						&nbsp;&nbsp;
						<n:link type="button" class="botao_normal" url="javascript:salvarESolicitarEncerramento();">Salvar registro e solicitar encerramento</n:link>
						&nbsp;&nbsp;
						<n:link type="button" class="botao_normal" url="javascript:cancelar();">Cancelar</n:link>
					</c:if>
					<c:if test="${status == 'ENCERRAMENTO_SOLICITADO'}">
						<n:link type="button" class="botao_normal" url="javascript:salvar();">Salvar registro</n:link>
						&nbsp;&nbsp;
						<n:link type="button" class="botao_normal" url="javascript:salvarEEncerrar();">Salvar registro e encerrar auditoria</n:link>
						&nbsp;&nbsp;
						<n:link type="button" class="botao_normal" url="javascript:cancelar();">Cancelar</n:link>
					</c:if>
					
					<script type="text/javascript">
						
						function salvar() {
							parent.salvarAuditoria();
							parent.$.akModalRemove();
						}
						
						function salvarESolicitarEncerramento(){
							parent.salvarAuditoriaESolicitarEncerramento();
							parent.$.akModalRemove();
						}
						
						function salvarEEncerrar(){
							parent.salvarAuditoriaEEncerrar();
							parent.$.akModalRemove();
						}
						
						function cancelar(){
							parent.$.akModalRemove();
						}
					</script>
				</TD>
			</TR>
			<TR>
				<TD align="center">
					<span class="desc12bold">
						<c:if test="${status == 'ABERTA'}">
							Atenção: ao clicar em 'Salvar registro e solicitar encerramento', este registro de auditoria não mais poderá ser editado e um email será enviado aos responsáveis pela área de qualidade do setor envolvido para que façam a validação da auditoria.
						</c:if>
					</span>
				</TD>
			</TR>			
		</TABLE>
	</body>
</html>
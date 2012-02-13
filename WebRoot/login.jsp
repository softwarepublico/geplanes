<%@page import="br.com.linkcom.sgm.util.GeplanesUtils"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="n" uri="neo"%>
<%@ taglib prefix="t" uri="template"%>

<% 
	String app = request.getContextPath();
%>

<html>
	<head>
		<link href="<%= app %>/css/estilo.css" rel="stylesheet" type="text/css"/>
	</head>
	<body onload="document.loginForm.username.focus();">
		<TABLE class="tabelaBase base_tabelabase" align="center">
			<TR>
				<TD class="cima_login" colspan="2">
					<IMG src="<%= app %>/images/img_sgm_grande.png">
					<br>
					<span class="txt_versao">Versão: ${VERSAO}</span>
				</TD>
			</TR>

			<TR>
				<TD class="meio_login" colspan="2">
					<form action="${ctx}/sgm/neo_security_manager" name="loginForm" method="post" style="margin: 0px;">
						<TABLE class="tabelaBase" cellpadding="5" >
							<TR>
								<TD style="width: 90%;">&nbsp;</TD>
								<TD nowrap="nowrap">
									<SPAN class="desc11bold">Login</SPAN><br>
									<input name="username" type="text" style="width: 95px;"/>
								</TD>
								<TD nowrap="nowrap">
									<SPAN class="desc11bold">Senha</SPAN><br>
									<input name="password" type="password" style="width: 95px;"/>
									<INPUT type="submit" value="entrar" class="botao_normal" style="width: 60px; margin-left: 5px;"/>
								</TD>
							</TR>
						</TABLE>
					</form>
				</TD>
			</TR>
			<TR>
				<TD class="msg_login" colspan="2">
					<c:if test="${!empty login_error}">
						<SPAN class="msg_erro">Login e/ou senha incorretos!</SPAN>
					</c:if>	
				</TD>
			</TR>
			<TR>
				<TD class="baixo_login_esq">
					<c:set var="logoEmpresaId" value="<%= GeplanesUtils.getLogoEmpresaId() %>"></c:set>
					<c:choose>
						<c:when test="${logoEmpresaId != 0}">
							<img src="<%= app %>/DOWNLOADFILEPUB/${logoEmpresaId}"/>
						</c:when>
						<c:otherwise>
							<img src="<%= app %>/images/img_empresa.png">
						</c:otherwise>
					</c:choose>
				</TD>
				<TD class="baixo_login_dir">
					<IMG src="<%= app %>/images/img_linkcom_pequeno.gif">
				</TD>
			</TR>
		</TABLE>
	</body>
</html>
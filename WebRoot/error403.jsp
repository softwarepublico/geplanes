<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="n" uri="neo"%>
<%@ taglib prefix="t" uri="template"%>

<% 
	String app = request.getContextPath();
%>

<html>
	<head>
		<n:head/>
	</head>
	<body>
		<TABLE class="tabelaBase base_tabelabase">
			<TR>
				<TD style="height: 82px;">
					<TABLE class="tabelaBase" style="width: 1000px;">
						<TR>
							<TD rowspan="2" class="base_logo">&nbsp;</TD>
							<TD class="base_user">
								<SPAN class="txt_normal">${USER.nome}</SPAN>
							</TD>
						</TR>
						<TR>
							<TD class="base_menu" align="right" valign="top">

							</TD>
						</TR>
					</TABLE>
				</TD>
			</TR>
			<TR>
				<TD class="base_conteudo">
				    Sem permissão para acessar essa funcionalidade do sistema
				</TD>
			</TR>
			<TR>
				<TD class="base_baixo" valign="middle"><a href="http://www.linkcom.com.br" target="blank"><IMG src="<%= app %>/images/ico_linkcom.gif"></a></TD>
			</TR>
		</TABLE>
	</body>
</html>
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
	<body>
	<%-- 
	<div id=loadmsg>
		<span class="message">Recarregando os dados da tela...</span>
	</div>	
	--%>
	<script language="JavaScript" src="${app}/tooltip/wz_tooltip.js"></script>
	<TABLE class="tabelaBase base_tabelabase" align="center">
			<TR>
				<TD style="height: 82px;">
				<TABLE class="tabelaBase" style="width: 1000px;">
						<TR>
							<TD rowspan="2" class="base_logo" 
								onclick="window.location='${ctx}/sgm/Index'"
								style="cursor: pointer;" >&nbsp;</TD>
							<TD class="base_user">
								<SPAN class="txt_normal">${USER.nome}</SPAN>
							</TD>
						</TR>
						<TR>
							<TD class="base_menu"valign="top" align="right">
							 <div class="menuGeplanes" onmouseover="esconderTabela()" onmouseout="esconderTabela();">
							   <n:menu menupath="/WEB-INF/menu.xml"/></div>
							</TD>
						</TR>
					</TABLE> 		
				</TD>
			</TR>
			<TR>
				<TD class="base_conteudo">
					<div align="center">
						<n:messages/>
					</div>
					<jsp:include page="${bodyPage}" />
				</TD>
			</TR>
			<TR>
				<TD class="base_baixo" valign="middle"><a href="http://www.linkcom.com.br" target="blank"><IMG src="${ctx}/images/ico_linkcom.gif"></a></TD>
			</TR>
		</TABLE>
	</body>
</html>
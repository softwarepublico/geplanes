<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="n" uri="neo"%>
<%@ taglib prefix="t" uri="template"%>

<html>
	<head>
		<n:head/>
	</head>
	<body>
	<script language="JavaScript" src="${ctx}/tooltip/wz_tooltip.js"></script>
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
					<n:link type="button" class="botao_normal" url="javascript:salvarSemAnomalia();">Salvar registro</n:link>
					&nbsp;&nbsp;
					<n:link type="button" class="botao_normal" url="javascript:salvarComAnomalia();">Salvar registro e tratar anomalia</n:link>
					&nbsp;&nbsp;
					<n:link type="button" class="botao_normal" url="javascript:cancelar();">Cancelar</n:link>
					
					<script type="text/javascript">
					
						function salvarSemAnomalia(){
							parent.submitSemAnomalia();
							parent.$.akModalRemove();
						}
						
						function salvarComAnomalia(){
							parent.submitComAnomalia();
							parent.$.akModalRemove();
						}
						
						function cancelar(){
							parent.$.akModalRemove();
						}
					</script>
				</TD>
			</TR>
		</TABLE>
	</body>
</html>
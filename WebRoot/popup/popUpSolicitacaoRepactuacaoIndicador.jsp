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
				<TD class="fd_tela_titulo" style="height: 20px;" >Solicitação de repactuação de indicador</TD>
			</TR>
			<TR>
				<TD valign="top">
					<n:bean name="solicitacaoRepactuacaoIndicador">

						<t:property name="indicador" mode="input" type="hidden" showLabel="false"/>
						<t:property name="solicitante" mode="input" type="hidden" showLabel="false" />
	
						<t:propertyConfig showLabel="true" renderAs="double">	
						
							<t:janelaResultados>
								<n:group columns="2" showBorder="false">
									<t:property name="indicador" mode="input" disabled="disabled" cols="200" style="width:300px"/>
									<t:property name="solicitante" mode="input" disabled="disabled" cols="200" />
									<t:property name="justificativaSol" type="TEXT_AREA" mode="input" style="width:350px;height:150px" onKeyPress="return contadorLetras(\"justificativaSol\",500,event)" onkeyUp="return contadorLetras(\"justificativaSol\",500,event)" />
								</n:group>
								<div align="right" style="padding-top:20px;padding-right:20px;">
									<n:submit url="/sgm/process/SolicitacaoRepactuacaoIndicador" action="repactuacaoPorLancamento" class="botao_normal">Salvar</n:submit>
								</div>
							</t:janelaResultados>
						</t:propertyConfig>
					</n:bean>			
				</TD>
			</TR>		
		  </table>
		</n:validation>
	 </n:form>
	</body>
</html>
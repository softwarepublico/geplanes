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
	<body leftmargin="0" topmargin="0" style="padding:0px;background-color: #F5F5F5;background-image:none;overflow:auto;height:270px;width:99%" bgColor="#FFFFFF" style="background-color: #FFFFFF">
		<n:form validate="true" name="indicadorForm">
			<n:validation functionName="validateForm">	
				<table class="lookup_janela">
					<TR>
						<TD class="fd_tela_titulo" style="height: 20px;" >Detalhamento do Indicador</TD>
					</TR>
					<TR>
						<TD valign="top" class="fd_tela_corpo" style="height: 100%">		
							<n:bean name="indicador">	
								<t:propertyConfig mode="output">
									<t:janelaEntrada showSubmit="false">
										<t:tabelaEntrada title="Indicador">
											<t:property name="objetivoMapaEstrategico" 			style="width: 500px;" write="true" type="hidden"/>
											<t:property name="nome"				style="width: 200px;" write="true" type="hidden" />
											<t:property name="melhor"			style="width: 100px;" write="true" type="hidden"/>
											<t:property name="unidadeMedida"	style="width: 100px;" write="true" type="hidden"/>
											<t:property name="precisao"			style="width: 100px;" write="true" type="hidden"/>
											<t:property name="tolerancia"		style="width: 100px;" write="true" type="hidden"/>		
											<t:property name="frequencia"		style="width: 100px;" type="hidden" write="true"/>
											<t:property name="descricao"		rows="4" cols="100"  write="true" type="hidden"/>
											<t:property name="responsavel"		style="width: 200px;"  write="true" type="hidden"/>
											<t:property name="relevancia"		style="width: 100px;" write="true" type="hidden"/>
											<t:property name="frequenciaAcompanhamento"		style="width: 100px;" write="true" type="hidden"/>
											<t:property name="mecanismoControle"		rows="4" cols="100"  write="true" type="hidden"/>
											<t:property name="fonteDados"		rows="4" cols="100"  write="true" type="hidden"/>
											<t:property name="formulaCalculo"		rows="4" cols="100"  write="true" type="hidden"/>
										</t:tabelaEntrada>
										
										<t:detalhe name="anexosIndicador" showBotaoNovaLinha="false" showBotaoRemover="false" showColunaAcao="false">
											<n:column header="Nome">
												<t:property name="id" write="false" type="hidden"/>
												<t:property name="nome" style="width:98%" bodyStyle="white-space:nowrap" write="true" type="hidden"/>
											</n:column>
											<t:property name="descricao" rows="2" cols="30" style="width:98%" write="true" type="hidden"/>
											<n:column header="Arquivo">
												<n:body>
													<a href="${ctx}/DOWNLOADFILE/${anexoIndicador.arquivo.id}">${anexoIndicador.arquivo.nome}</a>
												</n:body>
											</n:column>
										</t:detalhe>
										
									</t:janelaEntrada>
								</t:propertyConfig>
							</n:bean>	
						</TD>
					</TR>			
				</table>
			</n:validation>
		</n:form>
	</body>
</html>
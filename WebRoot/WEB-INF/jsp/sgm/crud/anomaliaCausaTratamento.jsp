<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="n" uri="neo"%>
<%@ taglib prefix="t" uri="template"%>
<%@ taglib prefix="f" tagdir="/WEB-INF/tags/"%>

<style>
	.divPrincipal{
		background-image: url(../../images/diagramaFeito.gif);
		background-repeat:no-repeat;
		background-position:center top;
		width:1300px;
		height:1500px;
		position:relative;
	}
	#principal textarea{
		width: 120px;
		overflow:auto;
	}
</style>

<c:choose>
		<c:when test="${podeTratarAnomalia}">
			<n:panel title="Análise - Diagrama de Causa e Efeito">
				<t:propertyConfig renderAs="single" mode="input">
				 	<div style="position:relative;">
					 	<div class="divPrincipal" id="principal">
						  <div style="position:absolute; left: 590px; top: 0px;">
					 		<t:property name="efeito.descricao" rows="4"/>
						  </div>
						
						  <div style="position:absolute; left: 223px; top: 150px;">
						    <t:property name="causasEfeito[0].id" type="hidden"/>
						    <t:property name="causasEfeito[0].descricao" rows="4"/>
						  </div>
						  
						  <div style="position:absolute; left: 6px; top: 262px;">
						  	<t:property name="causasEfeito[0].listaCausaEfeito[0].id" type="hidden" />
						  	<t:property name="causasEfeito[0].listaCausaEfeito[0].descricao" rows="4"/>
						  </div>
						  <div style="position:absolute; left: 152px; top: 262px;">
						    <t:property name="causasEfeito[0].listaCausaEfeito[1].id" type="hidden" />
						    <t:property name="causasEfeito[0].listaCausaEfeito[1].descricao" rows="4"/>
						  </div>
						  <div style="position:absolute; left: 298px; top: 262px;">
						    <t:property name="causasEfeito[0].listaCausaEfeito[2].id" type="hidden"/>
						    <t:property name="causasEfeito[0].listaCausaEfeito[2].descricao" rows="4"/>
						  </div>
						   <div style="position:absolute; left: 447px; top: 262px;">
						    <t:property name="causasEfeito[0].listaCausaEfeito[3].id" type="hidden"/>
						    <t:property name="causasEfeito[0].listaCausaEfeito[3].descricao" rows="4"/>
						  </div>
						  
						  <div style="position:absolute; left: 6px; top: 382px;">
						    <t:property name="causasEfeito[0].listaCausaEfeito[0].listaCausaEfeito[0].id" type="hidden"/>
						    <t:property name="causasEfeito[0].listaCausaEfeito[0].listaCausaEfeito[0].descricao" rows="4"/>
						  </div>
						  <div style="position:absolute; left: 152px; top: 382px;">
						     <t:property name="causasEfeito[0].listaCausaEfeito[1].listaCausaEfeito[0].id" type="hidden"/>
						     <t:property name="causasEfeito[0].listaCausaEfeito[1].listaCausaEfeito[0].descricao" rows="4"/>
						  </div>
						  <div style="position:absolute; left: 298px; top: 382px;">
						     <t:property name="causasEfeito[0].listaCausaEfeito[2].listaCausaEfeito[0].id" type="hidden"/>
						     <t:property name="causasEfeito[0].listaCausaEfeito[2].listaCausaEfeito[0].descricao" rows="4"/>
						  </div>
						  <div style="position:absolute; left: 447px; top: 382px;">
						     <t:property name="causasEfeito[0].listaCausaEfeito[3].listaCausaEfeito[0].id" type="hidden"/>
						     <t:property name="causasEfeito[0].listaCausaEfeito[3].listaCausaEfeito[0].descricao" rows="4"/>
						  </div>
						
						  <div style="position:absolute; left: 957px; top: 150px;">
						     <t:property name="causasEfeito[1].id" type="hidden"/>
						     <t:property name="causasEfeito[1].descricao" rows="4"/>
						  </div>
						  
						  <div style="position:absolute; left: 733px; top: 262px;">
						    <t:property name="causasEfeito[1].listaCausaEfeito[0].id" type="hidden"/>
						    <t:property name="causasEfeito[1].listaCausaEfeito[0].descricao" rows="4"/>
						  </div>
						  <div style="position:absolute; left: 882px; top: 262px;">
						   <t:property name="causasEfeito[1].listaCausaEfeito[1].id" type="hidden"/>
						   <t:property name="causasEfeito[1].listaCausaEfeito[1].descricao" rows="4"/>
						  </div>
						  <div style="position:absolute; left: 1028px; top: 262px;">
						    <t:property name="causasEfeito[1].listaCausaEfeito[2].id" type="hidden"/>
						    <t:property name="causasEfeito[1].listaCausaEfeito[2].descricao" rows="4"/>
						  </div>
						  <div style="position:absolute; left: 1174px; top: 262px;">
						    <t:property name="causasEfeito[1].listaCausaEfeito[3].id" type="hidden"/>
						    <t:property name="causasEfeito[1].listaCausaEfeito[3].descricao" rows="4"/>
						  </div>
						  
						  <div style="position:absolute; left: 733px; top: 382px;">
						    <t:property name="causasEfeito[1].listaCausaEfeito[0].listaCausaEfeito[0].id" type="hidden"/>
						    <t:property name="causasEfeito[1].listaCausaEfeito[0].listaCausaEfeito[0].descricao" rows="4"/>
						  </div>
						  <div style="position:absolute; left: 882px; top: 382px;">
						    <t:property name="causasEfeito[1].listaCausaEfeito[1].listaCausaEfeito[0].id" type="hidden"/>
						    <t:property name="causasEfeito[1].listaCausaEfeito[1].listaCausaEfeito[0].descricao" rows="4"/>
						  </div>
						  <div style="position:absolute; left: 1028px; top: 382px;">
						    <t:property name="causasEfeito[1].listaCausaEfeito[2].listaCausaEfeito[0].id" type="hidden"/>
						    <t:property name="causasEfeito[1].listaCausaEfeito[2].listaCausaEfeito[0].descricao" rows="4"/>
						  </div>
						  <div style="position:absolute; left: 1174px; top: 382px;">
						    <t:property name="causasEfeito[1].listaCausaEfeito[3].listaCausaEfeito[0].id" type="hidden"/>
						    <t:property name="causasEfeito[1].listaCausaEfeito[3].listaCausaEfeito[0].descricao" rows="4"/>
						  </div>
						  
						  <div style="position:absolute; left: 223px; top: 644px;">
						    <t:property name="causasEfeito[2].id" type="hidden"/>
						    <t:property name="causasEfeito[2].descricao" rows="4"/>
						  </div>
						  
						  <div style="position:absolute; left: 6px; top: 756px;">
						     <t:property name="causasEfeito[2].listaCausaEfeito[0].id" type="hidden"/>
						     <t:property name="causasEfeito[2].listaCausaEfeito[0].descricao" rows="4"/>
						  </div>
						   <div style="position:absolute; left: 152px; top: 756px;">
						     <t:property name="causasEfeito[2].listaCausaEfeito[1].id" type="hidden"/>
						     <t:property name="causasEfeito[2].listaCausaEfeito[1].descricao" rows="4"/>
						  </div>
						   <div style="position:absolute; left: 298px; top: 756px;">
						     <t:property name="causasEfeito[2].listaCausaEfeito[2].id" type="hidden"/>
						     <t:property name="causasEfeito[2].listaCausaEfeito[2].descricao" rows="4"/>
						  </div>
						  <div style="position:absolute; left: 447px; top: 756px;">
						     <t:property name="causasEfeito[2].listaCausaEfeito[3].id" type="hidden"/>
						     <t:property name="causasEfeito[2].listaCausaEfeito[3].descricao" rows="4"/>
						  </div>
						 
						  <div style="position:absolute; left: 6px; top: 876px;">
						    <t:property name="causasEfeito[2].listaCausaEfeito[0].listaCausaEfeito[0].id" type="hidden"/>
						    <t:property name="causasEfeito[2].listaCausaEfeito[0].listaCausaEfeito[0].descricao" rows="4"/>
						  </div>
						  <div style="position:absolute; left: 152px; top: 876px;">
						    <t:property name="causasEfeito[2].listaCausaEfeito[1].listaCausaEfeito[0].id" type="hidden"/>
						    <t:property name="causasEfeito[2].listaCausaEfeito[1].listaCausaEfeito[0].descricao" rows="4"/>
						  </div>
						  <div style="position:absolute; left: 298px; top: 876px;">
						     <t:property name="causasEfeito[2].listaCausaEfeito[2].listaCausaEfeito[0].id" type="hidden"/>
						     <t:property name="causasEfeito[2].listaCausaEfeito[2].listaCausaEfeito[0].descricao" rows="4"/>
						  </div>
						   <div style="position:absolute; left: 447px; top: 876px;">
						     <t:property name="causasEfeito[2].listaCausaEfeito[3].listaCausaEfeito[0].id" type="hidden"/>
						     <t:property name="causasEfeito[2].listaCausaEfeito[3].listaCausaEfeito[0].descricao" rows="4"/>
						  </div>
						  
						  <div style="position:absolute; left: 957px; top: 644px;">
						    <t:property name="causasEfeito[3].id" type="hidden"/>
						    <t:property name="causasEfeito[3].descricao" rows="4"/>
						  </div>
						  
						  <div style="position:absolute; left: 733px; top: 756px;">
						     <t:property name="causasEfeito[3].listaCausaEfeito[0].id" type="hidden"/>
						     <t:property name="causasEfeito[3].listaCausaEfeito[0].descricao" rows="4"/>
						  </div>
						  <div style="position:absolute; left: 882px; top: 756px;">
						    <t:property name="causasEfeito[3].listaCausaEfeito[1].id" type="hidden"/>
						    <t:property name="causasEfeito[3].listaCausaEfeito[1].descricao" rows="4"/>
						  </div>
						  <div style="position:absolute; left: 1028px; top: 756px;">
						    <t:property name="causasEfeito[3].listaCausaEfeito[2].id" type="hidden"/>
						    <t:property name="causasEfeito[3].listaCausaEfeito[2].descricao" rows="4"/>
						  </div>
						  <div style="position:absolute; left: 1174px; top: 756px;">
						    <t:property name="causasEfeito[3].listaCausaEfeito[3].id" type="hidden"/>
						    <t:property name="causasEfeito[3].listaCausaEfeito[3].descricao" rows="4"/>
						  </div>
						  
						  <div style="position:absolute; left: 733px; top: 876px;">
						    <t:property name="causasEfeito[3].listaCausaEfeito[0].listaCausaEfeito[0].id" type="hidden"/>
						    <t:property name="causasEfeito[3].listaCausaEfeito[0].listaCausaEfeito[0].descricao" rows="4"/>
						  </div>
						  <div style="position:absolute; left: 882px; top: 876px;">
						   <t:property name="causasEfeito[3].listaCausaEfeito[1].listaCausaEfeito[0].id" type="hidden"/>
						   <t:property name="causasEfeito[3].listaCausaEfeito[1].listaCausaEfeito[0].descricao" rows="4"/>
						  </div>
						  <div style="position:absolute; left: 1028px; top: 876px;">
						    <t:property name="causasEfeito[3].listaCausaEfeito[2].listaCausaEfeito[0].id" type="hidden"/>
						    <t:property name="causasEfeito[3].listaCausaEfeito[2].listaCausaEfeito[0].descricao" rows="4"/>
						  </div>
						  <div style="position:absolute; left: 1174px; top: 876px;">
						    <t:property name="causasEfeito[3].listaCausaEfeito[3].listaCausaEfeito[0].id" type="hidden"/>
						    <t:property name="causasEfeito[3].listaCausaEfeito[3].listaCausaEfeito[0].descricao" rows="4"/>
						  </div>
						  
						  <div style="position:absolute; left: 223px; top: 1139px;">
						     <t:property name="causasEfeito[4].id" type="hidden"/>
						     <t:property name="causasEfeito[4].descricao" rows="4"/>
						  </div>
						  
						  <div style="position:absolute; left: 6px; top: 1251px;">
						     <t:property name="causasEfeito[4].listaCausaEfeito[0].id" type="hidden"/>
						     <t:property name="causasEfeito[4].listaCausaEfeito[0].descricao" rows="4"/>
						  </div>
						  <div style="position:absolute; left: 152px; top: 1251px;">
						    <t:property name="causasEfeito[4].listaCausaEfeito[1].id" type="hidden"/>
						    <t:property name="causasEfeito[4].listaCausaEfeito[1].descricao" rows="4"/>
						  </div>
						  <div style="position:absolute; left: 298px; top: 1251px;">
						    <t:property name="causasEfeito[4].listaCausaEfeito[2].id" type="hidden"/>
						    <t:property name="causasEfeito[4].listaCausaEfeito[2].descricao" rows="4"/>
						  </div>
						  <div style="position:absolute; left: 447px; top: 1251px;">
						    <t:property name="causasEfeito[4].listaCausaEfeito[3].id" type="hidden"/>
						    <t:property name="causasEfeito[4].listaCausaEfeito[3].descricao" rows="4"/>
						  </div>
						  
						  <div style="position:absolute; left: 6px; top: 1371px;">
						    <t:property name="causasEfeito[4].listaCausaEfeito[0].listaCausaEfeito[0].id" type="hidden"/>
						    <t:property name="causasEfeito[4].listaCausaEfeito[0].listaCausaEfeito[0].descricao" rows="4"/>
						  </div>
						  <div style="position:absolute; left: 152px; top: 1371px;">
						    <t:property name="causasEfeito[4].listaCausaEfeito[1].listaCausaEfeito[0].id" type="hidden"/>
						    <t:property name="causasEfeito[4].listaCausaEfeito[1].listaCausaEfeito[0].descricao" rows="4"/>
						  </div>
						  <div style="position:absolute; left: 298px; top: 1371px;">
						    <t:property name="causasEfeito[4].listaCausaEfeito[2].listaCausaEfeito[0].id" type="hidden"/>
						    <t:property name="causasEfeito[4].listaCausaEfeito[2].listaCausaEfeito[0].descricao" rows="4"/>
						  </div>
						  <div style="position:absolute; left: 447px; top: 1371px;">
						    <t:property name="causasEfeito[4].listaCausaEfeito[3].listaCausaEfeito[0].id" type="hidden"/>
						    <t:property name="causasEfeito[4].listaCausaEfeito[3].listaCausaEfeito[0].descricao" rows="4"/>
						  </div>
						  
						  
						  <div style="position:absolute; left: 957px; top: 1139px;">
						    <t:property name="causasEfeito[5].id" type="hidden"/>
						    <t:property name="causasEfeito[5].descricao" rows="4"/>
						  </div>
						  
						  <div style="position:absolute; left: 733px; top: 1251px;">
						    <t:property name="causasEfeito[5].listaCausaEfeito[0].id" type="hidden"/>
						    <t:property name="causasEfeito[5].listaCausaEfeito[0].descricao" rows="4"/>
						  </div>
						  <div style="position:absolute; left: 882px; top: 1251px;">
						    <t:property name="causasEfeito[5].listaCausaEfeito[1].id" type="hidden"/>
						    <t:property name="causasEfeito[5].listaCausaEfeito[1].descricao" rows="4"/>
						  </div>
						  <div style="position:absolute; left: 1028px; top: 1251px;">
						    <t:property name="causasEfeito[5].listaCausaEfeito[2].id" type="hidden"/>
						    <t:property name="causasEfeito[5].listaCausaEfeito[2].descricao" rows="4"/>
						  </div>
						  <div style="position:absolute; left: 1174px; top: 1251px;">
						    <t:property name="causasEfeito[5].listaCausaEfeito[3].id" type="hidden"/>
						    <t:property name="causasEfeito[5].listaCausaEfeito[3].descricao" rows="4"/>
						  </div>
						  
						  <div style="position:absolute; left: 733px; top: 1371px;">
						    <t:property name="causasEfeito[5].listaCausaEfeito[0].listaCausaEfeito[0].id" type="hidden"/>
						    <t:property name="causasEfeito[5].listaCausaEfeito[0].listaCausaEfeito[0].descricao" rows="4"/>
						  </div>
						  <div style="position:absolute; left: 882px; top: 1371px;">
						    <t:property name="causasEfeito[5].listaCausaEfeito[1].listaCausaEfeito[0].id" type="hidden"/>
						    <t:property name="causasEfeito[5].listaCausaEfeito[1].listaCausaEfeito[0].descricao" rows="4"/>
						  </div>
						  <div style="position:absolute; left: 1028px; top: 1371px;">
						    <t:property name="causasEfeito[5].listaCausaEfeito[2].listaCausaEfeito[0].id" type="hidden"/>
						    <t:property name="causasEfeito[5].listaCausaEfeito[2].listaCausaEfeito[0].descricao" rows="4"/>
						  </div>
						  <div style="position:absolute; left: 1174px; top: 1371px;">
						    <t:property name="causasEfeito[5].listaCausaEfeito[3].listaCausaEfeito[0].id" type="hidden"/>
						    <t:property name="causasEfeito[5].listaCausaEfeito[3].listaCausaEfeito[0].descricao" rows="4"/>
						  </div>
						</div>
				 	</div>
			 		
			 	</t:propertyConfig>	
			 	
				<n:panel colspan="2" style="text-align: right;">
		 		  <n:submit class="botao_normal" action="gerar" url="/sgm/report/AnomaliaDiagrama" parameters="${anomalia}" validate="true">visualizar diagrama</n:submit>
		 		</n:panel>
			</n:panel>
			<%-- Início da TAB de tratamento de anomalias. --%>
			<n:panel title="Tratamento">
				<t:tabelaEntrada>
					<n:panelGrid columns="1" columnStyleClasses="form_filtro_value">
						<n:panel><span class="desc">Análise de Causas</span></n:panel>						
						<t:property name="analiseCausas" label="" rows="3" cols="180" id="idAnaliseCausas"/>
					</n:panelGrid>
				</t:tabelaEntrada>
				<br>
			   <n:panel><span class="desc">Plano de Ação</span></n:panel>		
			   <t:detalhe name="planosAcao" labelnovalinha="Novo plano de ação" showBotaoNovaLinha="true" showBotaoRemover="true">
					<n:column header="O que">
						<t:property name="id" write="false" type="hidden" renderAs="single"/>
						<t:property name="dtAtualizacaoStatus" write="false" type="hidden" renderAs="single"/>
						<t:property name="texto" 		rows="3" style="width:140px"/>
					</n:column>			   
					<t:property name="textoComo" 	rows="3" style="width:140px"/>
					<t:property name="textoPorque" 	rows="3" style="width:140px"/>
					<t:property name="textoQuem"	rows="3" style="width:140px"/>
					<t:property name="dtPlano" style="width:72px"/>
					<n:column header="Status">
						<t:property name="status" includeBlank="false"/>
						<br>
						<t:property name="descricaoDtAtualizacaoStatus" mode="output"/>
					</n:column>					
				</t:detalhe>
				<br>
				<br>
				<t:tabelaEntrada>
					<n:panelGrid columns="1" columnStyleClasses="form_filtro_value">
						<n:panel><span class="desc">Verificação</span>&nbsp;-&nbsp;<span class="desc">Evidências do tratamento da anomalia</span></n:panel>						
						<t:property name="verificacao" rows="3" cols="180" label=""/>
					</n:panelGrid>
				</t:tabelaEntrada>
				<br>
				<t:tabelaEntrada>
					<n:panelGrid columns="1" columnStyleClasses="form_filtro_value">
						<n:panel><span class="desc">Padronização</span>&nbsp;-&nbsp;<span class="desc">Documentos e/ou metodologias padronizadas no tratamento</span></n:panel>						
						<t:property name="padronizacao" rows="3" cols="180" label=""/>
					</n:panelGrid>
				</t:tabelaEntrada>			
			</n:panel>
		</c:when>
		<c:otherwise>
			<%-- Início da TAB de Análise diagrama causa e efeito. --%>
			<n:panel title="Análise diagrama causa e efeito">
				
				<t:propertyConfig renderAs="single" mode="input">
				 	<div style="position:relative;">
					 	<div class="divPrincipal">
						  <div style="position:absolute; left: 590px; top: 0px;">
					 		<t:property name="efeito.descricao" rows="4" disabled="disabled"/>
					 		<t:property name="efeito.descricao" rows="4" type="hidden"/>
						  </div>
						
						  <div style="position:absolute; left: 223px; top: 150px;">
						    <t:property name="causasEfeito[0].id" type="hidden"/>
						    <t:property name="causasEfeito[0].descricao" type="hidden"/>
						    <t:property name="causasEfeito[0].descricao" rows="4" disabled="disabled"/>
						  </div>
						  
						  <div style="position:absolute; left: 6px; top: 262px;">
						  	<t:property name="causasEfeito[0].listaCausaEfeito[0].id" type="hidden" />
						  	<t:property name="causasEfeito[0].listaCausaEfeito[0].descricao" type="hidden"/>
						  	<t:property name="causasEfeito[0].listaCausaEfeito[0].descricao" rows="4" disabled="disabled"/>
						  </div>
						  <div style="position:absolute; left: 152px; top: 262px;">
						    <t:property name="causasEfeito[0].listaCausaEfeito[1].id" type="hidden" />
						    <t:property name="causasEfeito[0].listaCausaEfeito[1].descricao" type="hidden"/>
						    <t:property name="causasEfeito[0].listaCausaEfeito[1].descricao" rows="4" disabled="disabled"/>
						  </div>
						  <div style="position:absolute; left: 298px; top: 262px;">
						    <t:property name="causasEfeito[0].listaCausaEfeito[2].id" type="hidden"/>
						    <t:property name="causasEfeito[0].listaCausaEfeito[2].descricao" type="hidden"/>
						    <t:property name="causasEfeito[0].listaCausaEfeito[2].descricao" rows="4" disabled="disabled"/>
						  </div>
						   <div style="position:absolute; left: 447px; top: 262px;">
						    <t:property name="causasEfeito[0].listaCausaEfeito[3].id" type="hidden"/>
						    <t:property name="causasEfeito[0].listaCausaEfeito[3].descricao" type="hidden"/>
						    <t:property name="causasEfeito[0].listaCausaEfeito[3].descricao" rows="4" disabled="disabled"/>
						  </div>
						  
						  <div style="position:absolute; left: 6px; top: 382px;">
						    <t:property name="causasEfeito[0].listaCausaEfeito[0].listaCausaEfeito[0].id" type="hidden"/>
						    <t:property name="causasEfeito[0].listaCausaEfeito[0].listaCausaEfeito[0].descricao" type="hidden"/>
						    <t:property name="causasEfeito[0].listaCausaEfeito[0].listaCausaEfeito[0].descricao" rows="4" disabled="disabled"/>
						  </div>
						  <div style="position:absolute; left: 152px; top: 382px;">
						     <t:property name="causasEfeito[0].listaCausaEfeito[1].listaCausaEfeito[0].id" type="hidden"/>
						     <t:property name="causasEfeito[0].listaCausaEfeito[1].listaCausaEfeito[0].descricao" type="hidden"/>
						     <t:property name="causasEfeito[0].listaCausaEfeito[1].listaCausaEfeito[0].descricao" rows="4" disabled="disabled"/>
						  </div>
						  <div style="position:absolute; left: 298px; top: 382px;">
						     <t:property name="causasEfeito[0].listaCausaEfeito[2].listaCausaEfeito[0].id" type="hidden"/>
						     <t:property name="causasEfeito[0].listaCausaEfeito[2].listaCausaEfeito[0].descricao" type="hidden"/>
						     <t:property name="causasEfeito[0].listaCausaEfeito[2].listaCausaEfeito[0].descricao" rows="4" disabled="disabled"/>
						  </div>
						  <div style="position:absolute; left: 447px; top: 382px;">
						     <t:property name="causasEfeito[0].listaCausaEfeito[3].listaCausaEfeito[0].id" type="hidden"/>
						     <t:property name="causasEfeito[0].listaCausaEfeito[3].listaCausaEfeito[0].descricao" type="hidden"/>
						     <t:property name="causasEfeito[0].listaCausaEfeito[3].listaCausaEfeito[0].descricao" rows="4" disabled="disabled"/>
						  </div>
						
						  <div style="position:absolute; left: 957px; top: 150px;">
						     <t:property name="causasEfeito[1].id" type="hidden"/>
						     <t:property name="causasEfeito[1].descricao" type="hidden"/>
						     <t:property name="causasEfeito[1].descricao" rows="4" disabled="disabled"/>
						  </div>
						  
						  <div style="position:absolute; left: 733px; top: 262px;">
						    <t:property name="causasEfeito[1].listaCausaEfeito[0].id" type="hidden"/>
						    <t:property name="causasEfeito[1].listaCausaEfeito[0].descricao" type="hidden"/>
						    <t:property name="causasEfeito[1].listaCausaEfeito[0].descricao" rows="4" disabled="disabled"/>
						  </div>
						  <div style="position:absolute; left: 882px; top: 262px;">
						   <t:property name="causasEfeito[1].listaCausaEfeito[1].id" type="hidden"/>
						   <t:property name="causasEfeito[1].listaCausaEfeito[1].descricao" type="hidden"/>
						   <t:property name="causasEfeito[1].listaCausaEfeito[1].descricao" rows="4" disabled="disabled"/>
						  </div>
						  <div style="position:absolute; left: 1028px; top: 262px;">
						    <t:property name="causasEfeito[1].listaCausaEfeito[2].id" type="hidden"/>
						    <t:property name="causasEfeito[1].listaCausaEfeito[2].descricao" type="hidden"/>
						    <t:property name="causasEfeito[1].listaCausaEfeito[2].descricao" rows="4" disabled="disabled"/>
						  </div>
						  <div style="position:absolute; left: 1174px; top: 262px;">
						    <t:property name="causasEfeito[1].listaCausaEfeito[3].id" type="hidden"/>
						    <t:property name="causasEfeito[1].listaCausaEfeito[3].descricao" type="hidden"/>
						    <t:property name="causasEfeito[1].listaCausaEfeito[3].descricao" rows="4" disabled="disabled"/>
						  </div>
						  
						  <div style="position:absolute; left: 733px; top: 382px;">
						    <t:property name="causasEfeito[1].listaCausaEfeito[0].listaCausaEfeito[0].id" type="hidden"/>
						    <t:property name="causasEfeito[1].listaCausaEfeito[0].listaCausaEfeito[0].descricao" type="hidden"/>
						    <t:property name="causasEfeito[1].listaCausaEfeito[0].listaCausaEfeito[0].descricao" rows="4" disabled="disabled"/>
						  </div>
						  <div style="position:absolute; left: 882px; top: 382px;">
						    <t:property name="causasEfeito[1].listaCausaEfeito[1].listaCausaEfeito[0].id" type="hidden"/>
						    <t:property name="causasEfeito[1].listaCausaEfeito[1].listaCausaEfeito[0].descricao" type="hidden"/>
						    <t:property name="causasEfeito[1].listaCausaEfeito[1].listaCausaEfeito[0].descricao" rows="4" disabled="disabled"/>
						  </div>
						  <div style="position:absolute; left: 1028px; top: 382px;">
						    <t:property name="causasEfeito[1].listaCausaEfeito[2].listaCausaEfeito[0].id" type="hidden"/>
						    <t:property name="causasEfeito[1].listaCausaEfeito[2].listaCausaEfeito[0].descricao" type="hidden"/>
						    <t:property name="causasEfeito[1].listaCausaEfeito[2].listaCausaEfeito[0].descricao" rows="4" disabled="disabled"/>
						  </div>
						  <div style="position:absolute; left: 1174px; top: 382px;">
						    <t:property name="causasEfeito[1].listaCausaEfeito[3].listaCausaEfeito[0].id" type="hidden"/>
						    <t:property name="causasEfeito[1].listaCausaEfeito[3].listaCausaEfeito[0].descricao" type="hidden"/>
						    <t:property name="causasEfeito[1].listaCausaEfeito[3].listaCausaEfeito[0].descricao" rows="4" disabled="disabled"/>
						  </div>
						  
						  <div style="position:absolute; left: 223px; top: 644px;">
						    <t:property name="causasEfeito[2].id" type="hidden"/>
						    <t:property name="causasEfeito[2].descricao" type="hidden"/>
						    <t:property name="causasEfeito[2].descricao" rows="4" disabled="disabled"/>
						  </div>
						  
						  <div style="position:absolute; left: 6px; top: 756px;">
						     <t:property name="causasEfeito[2].listaCausaEfeito[0].id" type="hidden"/>
						     <t:property name="causasEfeito[2].listaCausaEfeito[0].descricao" type="hidden"/>
						     <t:property name="causasEfeito[2].listaCausaEfeito[0].descricao" rows="4" disabled="disabled"/>
						  </div>
						   <div style="position:absolute; left: 152px; top: 756px;">
						     <t:property name="causasEfeito[2].listaCausaEfeito[1].id" type="hidden"/>
						     <t:property name="causasEfeito[2].listaCausaEfeito[1].descricao" type="hidden"/>
						     <t:property name="causasEfeito[2].listaCausaEfeito[1].descricao" rows="4" disabled="disabled"/>
						  </div>
						   <div style="position:absolute; left: 298px; top: 756px;">
						     <t:property name="causasEfeito[2].listaCausaEfeito[2].id" type="hidden"/>
						     <t:property name="causasEfeito[2].listaCausaEfeito[2].descricao" type="hidden"/>
						     <t:property name="causasEfeito[2].listaCausaEfeito[2].descricao" rows="4" disabled="disabled"/>
						  </div>
						  <div style="position:absolute; left: 447px; top: 756px;">
						     <t:property name="causasEfeito[2].listaCausaEfeito[3].id" type="hidden"/>
						     <t:property name="causasEfeito[2].listaCausaEfeito[3].descricao" type="hidden"/>
						     <t:property name="causasEfeito[2].listaCausaEfeito[3].descricao" rows="4" disabled="disabled"/>
						  </div>
						 
						  <div style="position:absolute; left: 6px; top: 876px;">
						    <t:property name="causasEfeito[2].listaCausaEfeito[0].listaCausaEfeito[0].id" type="hidden"/>
						    <t:property name="causasEfeito[2].listaCausaEfeito[0].listaCausaEfeito[0].descricao" type="hidden"/>
						    <t:property name="causasEfeito[2].listaCausaEfeito[0].listaCausaEfeito[0].descricao" rows="4" disabled="disabled"/>
						  </div>
						  <div style="position:absolute; left: 152px; top: 876px;">
						    <t:property name="causasEfeito[2].listaCausaEfeito[1].listaCausaEfeito[0].id" type="hidden"/>
						    <t:property name="causasEfeito[2].listaCausaEfeito[1].listaCausaEfeito[0].descricao" type="hidden"/>
						    <t:property name="causasEfeito[2].listaCausaEfeito[1].listaCausaEfeito[0].descricao" rows="4" disabled="disabled"/>
						  </div>
						  <div style="position:absolute; left: 298px; top: 876px;">
						     <t:property name="causasEfeito[2].listaCausaEfeito[2].listaCausaEfeito[0].id" type="hidden"/>
						     <t:property name="causasEfeito[2].listaCausaEfeito[2].listaCausaEfeito[0].descricao" type="hidden"/>
						     <t:property name="causasEfeito[2].listaCausaEfeito[2].listaCausaEfeito[0].descricao" rows="4" disabled="disabled"/>
						  </div>
						   <div style="position:absolute; left: 447px; top: 876px;">
						     <t:property name="causasEfeito[2].listaCausaEfeito[3].listaCausaEfeito[0].id" type="hidden"/>
						     <t:property name="causasEfeito[2].listaCausaEfeito[3].listaCausaEfeito[0].descricao" type="hidden"/>
						     <t:property name="causasEfeito[2].listaCausaEfeito[3].listaCausaEfeito[0].descricao" rows="4" disabled="disabled"/>
						  </div>
						  
						  <div style="position:absolute; left: 957px; top: 644px;">
						    <t:property name="causasEfeito[3].id" type="hidden"/>
						    <t:property name="causasEfeito[3].descricao" type="hidden"/>
						    <t:property name="causasEfeito[3].descricao" rows="4" disabled="disabled"/>
						  </div>
						  
						  <div style="position:absolute; left: 733px; top: 756px;">
						     <t:property name="causasEfeito[3].listaCausaEfeito[0].id" type="hidden"/>
						     <t:property name="causasEfeito[3].listaCausaEfeito[0].descricao" type="hidden"/>
						     <t:property name="causasEfeito[3].listaCausaEfeito[0].descricao" rows="4" disabled="disabled"/>
						  </div>
						  <div style="position:absolute; left: 882px; top: 756px;">
						    <t:property name="causasEfeito[3].listaCausaEfeito[1].id" type="hidden"/>
						    <t:property name="causasEfeito[3].listaCausaEfeito[1].descricao" type="hidden"/>
						    <t:property name="causasEfeito[3].listaCausaEfeito[1].descricao" rows="4" disabled="disabled"/>
						  </div>
						  <div style="position:absolute; left: 1028px; top: 756px;">
						    <t:property name="causasEfeito[3].listaCausaEfeito[2].id" type="hidden"/>
						    <t:property name="causasEfeito[3].listaCausaEfeito[2].descricao" type="hidden"/>
						    <t:property name="causasEfeito[3].listaCausaEfeito[2].descricao" rows="4" disabled="disabled"/>
						  </div>
						  <div style="position:absolute; left: 1174px; top: 756px;">
						    <t:property name="causasEfeito[3].listaCausaEfeito[3].id" type="hidden"/>
						    <t:property name="causasEfeito[3].listaCausaEfeito[3].descricao" type="hidden"/>
						    <t:property name="causasEfeito[3].listaCausaEfeito[3].descricao" rows="4" disabled="disabled"/>
						  </div>
						  
						  <div style="position:absolute; left: 733px; top: 876px;">
						    <t:property name="causasEfeito[3].listaCausaEfeito[0].listaCausaEfeito[0].id" type="hidden"/>
						    <t:property name="causasEfeito[3].listaCausaEfeito[0].listaCausaEfeito[0].descricao" type="hidden"/>
						    <t:property name="causasEfeito[3].listaCausaEfeito[0].listaCausaEfeito[0].descricao" rows="4" disabled="disabled"/>
						  </div>
						  <div style="position:absolute; left: 882px; top: 876px;">
						   <t:property name="causasEfeito[3].listaCausaEfeito[1].listaCausaEfeito[0].id" type="hidden"/>
						   <t:property name="causasEfeito[3].listaCausaEfeito[1].listaCausaEfeito[0].descricao" type="hidden"/>
						   <t:property name="causasEfeito[3].listaCausaEfeito[1].listaCausaEfeito[0].descricao" rows="4" disabled="disabled"/>
						  </div>
						  <div style="position:absolute; left: 1028px; top: 876px;">
						    <t:property name="causasEfeito[3].listaCausaEfeito[2].listaCausaEfeito[0].id" type="hidden"/>
						    <t:property name="causasEfeito[3].listaCausaEfeito[2].listaCausaEfeito[0].descricao" type="hidden"/>
						    <t:property name="causasEfeito[3].listaCausaEfeito[2].listaCausaEfeito[0].descricao" rows="4" disabled="disabled"/>
						  </div>
						  <div style="position:absolute; left: 1174px; top: 876px;">
						    <t:property name="causasEfeito[3].listaCausaEfeito[3].listaCausaEfeito[0].id" type="hidden"/>
						    <t:property name="causasEfeito[3].listaCausaEfeito[3].listaCausaEfeito[0].descricao" type="hidden"/>
						    <t:property name="causasEfeito[3].listaCausaEfeito[3].listaCausaEfeito[0].descricao" rows="4" disabled="disabled"/>
						  </div>
						  
						  <div style="position:absolute; left: 223px; top: 1139px;">
						     <t:property name="causasEfeito[4].id" type="hidden"/>
						     <t:property name="causasEfeito[4].descricao" type="hidden"/>
						     <t:property name="causasEfeito[4].descricao" rows="4" disabled="disabled"/>
						  </div>
						  
						  <div style="position:absolute; left: 6px; top: 1251px;">
						     <t:property name="causasEfeito[4].listaCausaEfeito[0].id" type="hidden"/>
						     <t:property name="causasEfeito[4].listaCausaEfeito[0].descricao" type="hidden"/>
						     <t:property name="causasEfeito[4].listaCausaEfeito[0].descricao" rows="4" disabled="disabled"/>
						  </div>
						  <div style="position:absolute; left: 152px; top: 1251px;">
						    <t:property name="causasEfeito[4].listaCausaEfeito[1].id" type="hidden"/>
						    <t:property name="causasEfeito[4].listaCausaEfeito[1].descricao" type="hidden"/>
						    <t:property name="causasEfeito[4].listaCausaEfeito[1].descricao" rows="4" disabled="disabled"/>
						  </div>
						  <div style="position:absolute; left: 298px; top: 1251px;">
						    <t:property name="causasEfeito[4].listaCausaEfeito[2].id" type="hidden"/>
						    <t:property name="causasEfeito[4].listaCausaEfeito[2].descricao" type="hidden"/>
						    <t:property name="causasEfeito[4].listaCausaEfeito[2].descricao" rows="4" disabled="disabled"/>
						  </div>
						  <div style="position:absolute; left: 447px; top: 1251px;">
						    <t:property name="causasEfeito[4].listaCausaEfeito[3].id" type="hidden"/>
						    <t:property name="causasEfeito[4].listaCausaEfeito[3].descricao" type="hidden"/>
						    <t:property name="causasEfeito[4].listaCausaEfeito[3].descricao" rows="4" disabled="disabled"/>
						  </div>
						  
						  <div style="position:absolute; left: 6px; top: 1371px;">
						    <t:property name="causasEfeito[4].listaCausaEfeito[0].listaCausaEfeito[0].id" type="hidden"/>
						    <t:property name="causasEfeito[4].listaCausaEfeito[0].listaCausaEfeito[0].descricao" type="hidden"/>
						    <t:property name="causasEfeito[4].listaCausaEfeito[0].listaCausaEfeito[0].descricao" rows="4" disabled="disabled"/>
						  </div>
						  <div style="position:absolute; left: 152px; top: 1371px;">
						    <t:property name="causasEfeito[4].listaCausaEfeito[1].listaCausaEfeito[0].id" type="hidden"/>
						    <t:property name="causasEfeito[4].listaCausaEfeito[1].listaCausaEfeito[0].descricao" type="hidden"/>
						    <t:property name="causasEfeito[4].listaCausaEfeito[1].listaCausaEfeito[0].descricao" rows="4" disabled="disabled"/>
						  </div>
						  <div style="position:absolute; left: 298px; top: 1371px;">
						    <t:property name="causasEfeito[4].listaCausaEfeito[2].listaCausaEfeito[0].id" type="hidden"/>
						    <t:property name="causasEfeito[4].listaCausaEfeito[2].listaCausaEfeito[0].descricao" type="hidden"/>
						    <t:property name="causasEfeito[4].listaCausaEfeito[2].listaCausaEfeito[0].descricao" rows="4" disabled="disabled"/>
						  </div>
						  <div style="position:absolute; left: 447px; top: 1371px;">
						    <t:property name="causasEfeito[4].listaCausaEfeito[3].listaCausaEfeito[0].id" type="hidden"/>
						    <t:property name="causasEfeito[4].listaCausaEfeito[3].listaCausaEfeito[0].descricao" type="hidden"/>
						    <t:property name="causasEfeito[4].listaCausaEfeito[3].listaCausaEfeito[0].descricao" rows="4" disabled="disabled"/>
						  </div>
						  
						  
						  <div style="position:absolute; left: 957px; top: 1139px;">
						    <t:property name="causasEfeito[5].id" type="hidden"/>
						    <t:property name="causasEfeito[5].descricao" type="hidden"/>
						    <t:property name="causasEfeito[5].descricao" rows="4" disabled="disabled"/>
						  </div>
						  
						  <div style="position:absolute; left: 733px; top: 1251px;">
						    <t:property name="causasEfeito[5].listaCausaEfeito[0].id" type="hidden"/>
						    <t:property name="causasEfeito[5].listaCausaEfeito[0].descricao" type="hidden"/>
						    <t:property name="causasEfeito[5].listaCausaEfeito[0].descricao" rows="4" disabled="disabled"/>
						  </div>
						  <div style="position:absolute; left: 882px; top: 1251px;">
						    <t:property name="causasEfeito[5].listaCausaEfeito[1].id" type="hidden"/>
						    <t:property name="causasEfeito[5].listaCausaEfeito[1].descricao" type="hidden"/>
						    <t:property name="causasEfeito[5].listaCausaEfeito[1].descricao" rows="4" disabled="disabled"/>
						  </div>
						  <div style="position:absolute; left: 1028px; top: 1251px;">
						    <t:property name="causasEfeito[5].listaCausaEfeito[2].id" type="hidden"/>
						    <t:property name="causasEfeito[5].listaCausaEfeito[2].descricao" type="hidden"/>
						    <t:property name="causasEfeito[5].listaCausaEfeito[2].descricao" rows="4" disabled="disabled"/>
						  </div>
						  <div style="position:absolute; left: 1174px; top: 1251px;">
						    <t:property name="causasEfeito[5].listaCausaEfeito[3].id" type="hidden"/>
						    <t:property name="causasEfeito[5].listaCausaEfeito[3].descricao" type="hidden"/>
						    <t:property name="causasEfeito[5].listaCausaEfeito[3].descricao" rows="4" disabled="disabled"/>
						  </div>
						  
						  <div style="position:absolute; left: 733px; top: 1371px;">
						    <t:property name="causasEfeito[5].listaCausaEfeito[0].listaCausaEfeito[0].id" type="hidden"/>
						    <t:property name="causasEfeito[5].listaCausaEfeito[0].listaCausaEfeito[0].descricao" type="hidden"/>
						    <t:property name="causasEfeito[5].listaCausaEfeito[0].listaCausaEfeito[0].descricao" rows="4" disabled="disabled"/>
						  </div>
						  <div style="position:absolute; left: 882px; top: 1371px;">
						    <t:property name="causasEfeito[5].listaCausaEfeito[1].listaCausaEfeito[0].id" type="hidden"/>
						    <t:property name="causasEfeito[5].listaCausaEfeito[1].listaCausaEfeito[0].descricao" type="hidden"/>
						    <t:property name="causasEfeito[5].listaCausaEfeito[1].listaCausaEfeito[0].descricao" rows="4" disabled="disabled"/>
						  </div>
						  <div style="position:absolute; left: 1028px; top: 1371px;">
						    <t:property name="causasEfeito[5].listaCausaEfeito[2].listaCausaEfeito[0].id" type="hidden"/>
						    <t:property name="causasEfeito[5].listaCausaEfeito[2].listaCausaEfeito[0].descricao" type="hidden"/>
						    <t:property name="causasEfeito[5].listaCausaEfeito[2].listaCausaEfeito[0].descricao" rows="4" disabled="disabled"/>
						  </div>
						  <div style="position:absolute; left: 1174px; top: 1371px;">
						    <t:property name="causasEfeito[5].listaCausaEfeito[3].listaCausaEfeito[0].id" type="hidden"/>
						    <t:property name="causasEfeito[5].listaCausaEfeito[3].listaCausaEfeito[0].descricao" type="hidden"/>
						    <t:property name="causasEfeito[5].listaCausaEfeito[3].listaCausaEfeito[0].descricao" rows="4" disabled="disabled"/>
						  </div>
						</div>
				 	</div>
			 	</t:propertyConfig>
			 			 		
				<n:panel colspan="2" style="text-align: right;">
		 		  <n:submit class="botao_normal" action="gerar" url="/sgm/report/AnomaliaDiagrama" parameters="${anomalia}" validate="true">visualizar diagrama</n:submit>
		 		</n:panel>
			</n:panel>

			<n:panel title="Tratamento">
			
				<t:tabelaEntrada>
					<n:panelGrid columns="1" columnStyleClasses="form_filtro_value">
						<n:panel><span class="desc">Análise de Causas</span></n:panel>						
						<t:property name="analiseCausas" rows="3" cols="180" readonly="true" class="readonlyField" label="" id="idAnaliseCausas"/>
					</n:panelGrid>
				</t:tabelaEntrada>			
				<br>
			   <n:panel><span class="desc">Plano de Ação</span></n:panel>
			   <t:detalhe name="planosAcao" labelnovalinha="Novo plano de ação" showBotaoNovaLinha="${podeCriarPlanoAcao}" showBotaoRemover="false">
			   		<c:choose>
			   			<c:when test="${planoAcao.podeEditarTexto && planoAcao.podeEditarStatus}">
							<n:column header="O que">
								<t:property name="id" write="false" type="hidden" renderAs="single"/>
								<t:property name="dtAtualizacaoStatus" write="false" type="hidden" renderAs="single"/>
								<t:property name="texto" 		rows="3" style="width:140px"/>
							</n:column>			   
							<t:property name="textoComo" 	rows="3" style="width:140px"/>
							<t:property name="textoPorque" 	rows="3" style="width:140px"/>
							<t:property name="textoQuem"	rows="3" style="width:140px"/>
							<t:property name="dtPlano" style="width:72px"/>
							<n:column header="Status">
								<t:property name="status" includeBlank="false"/>
								<br>
								<t:property name="descricaoDtAtualizacaoStatus" mode="output"/>
							</n:column>
							<t:acao>
								<table border="0" cellpadding="0" cellspacing="0">
									<tr>
										<td>
											<img src="../../images/ico_excluir.gif" 
											onclick="excluirLinhaPorNome(this.id,true);reindexFormPorNome(this.id, forms[0], '${Tdetalhe.fullNestedName}', true);" 
											id="button.excluir[table_id=${Tdetalhe.tableId}, indice=${rowIndex}]" 
											style="cursor:pointer;" 
											alt="Excluir"/>
										</td>
									</tr>
								</table>
							</t:acao>			   			
			   			</c:when>
			   			<c:otherwise>
			   				<c:choose>
			   					<c:when test="${planoAcao.podeEditarStatus}">
									<n:column header="O que">
										<t:property name="id" write="false" type="hidden" renderAs="single"/>
										<t:property name="dtAtualizacaoStatus" write="false" type="hidden" renderAs="single"/>
										<t:property name="texto" 		rows="3" style="width:140px" readonly="true" class="readonlyField"/>
									</n:column>
									<t:property name="textoComo" 	rows="3" style="width:140px" readonly="true" class="readonlyField"/>
									<t:property name="textoPorque" 	rows="3" style="width:140px" readonly="true" class="readonlyField"/>
									<t:property name="textoQuem"	rows="3" style="width:140px" readonly="true" class="readonlyField"/>
									<t:property name="dtPlano" write="true" type="hidden"/>
									<n:column header="Status">
										<t:property name="status" includeBlank="false"/>
										<br>
										<t:property name="descricaoDtAtualizacaoStatus" mode="output"/>
									</n:column>			   					
			   					</c:when>
			   					<c:otherwise>
									<n:column header="O que">
										<t:property name="id" write="false" type="hidden" renderAs="single"/>
										<t:property name="dtAtualizacaoStatus" write="false" type="hidden" renderAs="single"/>
										<t:property name="texto" 		rows="3" style="width:140px" readonly="true" class="readonlyField"/>
									</n:column>
									<t:property name="textoComo" 	rows="3" style="width:140px" readonly="true" class="readonlyField"/>
									<t:property name="textoPorque" 	rows="3" style="width:140px" readonly="true" class="readonlyField"/>
									<t:property name="textoQuem"	rows="3" style="width:140px" readonly="true" class="readonlyField"/>
									<t:property name="dtPlano" write="true" type="hidden" class="readonlyField"/>
									<n:column header="Status">
										<t:property name="status" write="true" type="hidden" class="readonlyField"/>							
										<br>
										<t:property name="descricaoDtAtualizacaoStatus" mode="output"/>
									</n:column>			   					
			   					</c:otherwise>
			   				</c:choose>
			   			</c:otherwise>
			   		</c:choose>
				</t:detalhe>
				<br>
				<t:tabelaEntrada>
					<n:panelGrid columns="1" columnStyleClasses="form_filtro_value">						
						<n:panel><span class="desc">Verificação</span>&nbsp;-&nbsp;<span class="desc">Evidências do tratamento da anomalia</span></n:panel>						
						<t:property name="verificacao" rows="3" cols="180" label="" readonly="true" class="readonlyField"/>						
					</n:panelGrid>
				</t:tabelaEntrada>
				<br>
				<t:tabelaEntrada>
					<n:panelGrid columns="1" columnStyleClasses="form_filtro_value">						
						<n:panel><span class="desc">Padronização</span>&nbsp;-&nbsp;<span class="desc">Documentos e/ou metodologias padronizadas no tratamento</span></n:panel>						
						<t:property name="padronizacao" rows="3" cols="180" label="" readonly="true" class="readonlyField"/>						
					</n:panelGrid>
				</t:tabelaEntrada>
			</n:panel>
		</c:otherwise>
	</c:choose>

	<n:panel title="Encerramento">
		<t:tabelaEntrada>					
			<n:panelGrid columns="1" columnStyleClasses="form_filtro_value">
				<c:choose>
					<c:when test="${podeEncerrarAnomalia && !podeDestravarAnomalia}">
						<n:panel><span class="desc">Data de encerramento</span></n:panel>
						<t:property name="dataEncerramento"	style="width: 100px" label=""/>
						<n:panel><span class="desc">Conclusão</span></n:panel>
						<t:property name="conclusao" label="" rows="3" cols="180"/>
					</c:when>
					<c:otherwise>
						<n:panel><span class="desc">Data de encerramento</span></n:panel>
						<t:property name="dataEncerramento"	type="hidden" write="true" label=""/>
						<n:panel><span class="desc">Conclusão</span></n:panel>
						<t:property name="conclusao" rows="3" cols="180" readonly="true" class="readonlyField" label=""/>
					</c:otherwise>
				</c:choose>
			</n:panelGrid>				
		</t:tabelaEntrada>										
	</n:panel>	
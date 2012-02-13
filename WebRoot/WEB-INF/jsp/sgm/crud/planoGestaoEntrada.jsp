<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="n" uri="neo"%>
<%@ taglib prefix="t" uri="template"%>

<style>
	.colunaTrimestre {
		border: none;
		text-align: center; 
		width:90px;
		font-size: 12px;
		font-weight: bold;
	}
	.colunaDatas {
		border: none;
		text-align: center; 
		width:250px;
		font-size: 12px;
		font-weight: bold;		
	}

</style>

<t:entrada titulo="Cadastro de ${PLANOS_DE_GESTAO}">
	<t:property name="id" type="hidden"/>
	<span style="display: none">
		<t:property name="copia" type="hidden" write="false"/>	
		<t:property name="lembreteCriacaoMetasIndicadores" type="hidden" />
	</span>
	<t:janelaEntrada>
		<t:tabelaEntrada>
			<t:property name="anoExercicio" style="width: 40px;" />
			<t:property name="descricao" rows="2" cols="50" onKeyPress="contadorLetras(\"descricao\",500,event)" onKeyUp="contadorLetras(\"descricao\",500,event)"/>
			<t:property name="limiteCriacaoMapaNegocio" style="width: 75px;"/>			
			<t:property name="limiteCriacaoMapaEstrategico" style="width: 75px;"/>			
			<t:property name="limiteCriacaoMapaCompetencia" style="width: 75px;"/>			
			<t:property name="limiteCriacaoMatrizFcs" style="width: 75px;"/>			
			<t:property name="limiteCriacaoMetasIndicadores" style="width: 75px;" />
			<n:panel colspan="2" style="text-align: center; width: 100%;">
				<n:panel style="text-align: center; width: 600px;">
					<fieldset>
						<legend>LANÇAMENTO DE RESULTADOS</legend>
						<BR>
						<n:panelGrid columns="3" rowStyles="height: 30px;" columnStyleClasses="colunaTrimestre,colunaDatas,colunaDatas">
						
							<!-- Linha 1 -->
							<n:panel>&nbsp</n:panel>
							<n:panel>DATA LIMITE PARA LANÇAMENTO</n:panel>
							<n:panel>DATA PARA TRAVAMENTO</n:panel>
							
							<!-- Linha 2 -->
							<n:panel class="colunaTrimestre">1º TRIMESTRE</n:panel>
							<t:property name="dtLimLancRes1T" style="width:75px;" renderAs="single" showLabel="false"/>
							<t:property name="dtTravLancRes1T" style="width:75px;" renderAs="single" showLabel="false"/>
							
							<!-- Linha 3 -->
							<n:panel class="colunaTrimestre">2º TRIMESTRE</n:panel>
							<t:property name="dtLimLancRes2T" style="width:75px;" renderAs="single" showLabel="false"/>
							<t:property name="dtTravLancRes2T" style="width:75px;" renderAs="single" showLabel="false"/>
							
							<!-- Linha 4 -->	
							<n:panel class="colunaTrimestre">3º TRIMESTRE</n:panel>				
							<t:property name="dtLimLancRes3T" style="width:75px;" renderAs="single" showLabel="false"/>
							<t:property name="dtTravLancRes3T" style="width:75px;" renderAs="single" showLabel="false"/>
							
							<!-- Linha 5 -->					
							<n:panel class="colunaTrimestre">4º TRIMESTRE</n:panel>	
							<t:property name="dtLimLancRes4T" style="width:75px;" renderAs="single" showLabel="false"/>
							<t:property name="dtTravLancRes4T" style="width:75px;" renderAs="single" showLabel="false"/>	
						</n:panelGrid>
						<BR>
					</fieldset>
				</n:panel>
			</n:panel>
		</t:tabelaEntrada>
	</t:janelaEntrada>
</t:entrada>
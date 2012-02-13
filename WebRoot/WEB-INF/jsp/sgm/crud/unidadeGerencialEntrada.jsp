<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="n" uri="neo"%>
<%@ taglib prefix="t" uri="template"%>
<%@ taglib prefix="f" tagdir="/WEB-INF/tags/"%>

<t:entrada titulo="Cadastro de unidades gerenciais">
	<t:property name="id" write="false" type="hidden"/>
	<t:property name="seqAnomalia" write="false" type="hidden"/>
	<t:property name="seqAcaoPreventiva" write="false" type="hidden"/>
	<t:property name="seqOcorrencia" write="false" type="hidden"/>
	<t:janelaEntrada>
		<t:tabelaEntrada>
			<t:property name="nome" style="overflow:hidden;width:400px"/>
			<t:property name="sigla" style="overflow:hidden;width:100px"/>
			<t:property name="nivelHierarquico" style="overflow:hidden;"/>
			<t:property name="planoGestao" style="width: 100px;" />
			<n:output value="Subordinação" styleClass="desc10bold"/>		
			<n:panel>
				<f:unidadeGerencialInput name="subordinacao"/>
			</n:panel>
			<t:property name="areaQualidade"/>
			<t:property name="areaAuditoriaInterna"/>			
			<t:property name="permitirMapaNegocio"/>				
			<t:property name="permitirMapaCompetencia"/>				
			<t:property name="permitirMapaEstrategico"/>				
			<t:property name="permitirMatrizFcs"/>				
		</t:tabelaEntrada>
		<t:detalhe name="usuariosUnidadeGerencial" labelnovalinha="Novo vínculo">
			<n:column header="Usuário">
				<n:body>
					<t:property name="id" type="hidden"/>
					<t:property name="usuario" itens="${listaNaoAdministradores}"/>
				</n:body>
			</n:column>
			<t:property name="funcao"/>
		</t:detalhe>		
	</t:janelaEntrada>
</t:entrada>
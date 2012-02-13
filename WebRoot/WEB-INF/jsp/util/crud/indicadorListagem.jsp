<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="n" uri="neo"%>
<%@ taglib prefix="t" uri="template"%>
<%@ taglib prefix="f" tagdir="/WEB-INF/tags/"%>

<t:listagem titulo="Listagem de Indicadores" showNewLink="false">
	<t:janelaFiltro>
		<t:tabelaFiltro>
			<input type="hidden" name="unidadeGerencialId" value="${unidadeGerencialId}">
			<input type="hidden" name="objetivoMapaEstrategicoId" value="${objetivoMapaEstrategicoId}">
			<n:comboReloadGroup useAjax="true">
				<t:property name="planoGestao"	style="width: 100px;"/>
	
				<n:output styleClass="desc11" value="Unidade Gerencial"/>
				<n:panel>
					<f:unidadeGerencialInput name="unidadeGerencial" onchange="carregaPerspectivas()" estiloclasse="required"/>
				</n:panel>
				<t:property name="perspectivaMapaEstrategico" itens="perspectivaMapaEstrategicoService.findByUnidadeGerencialThroughMapaEstrategico(unidadeGerencial,true)" style="width:70%;" onchange="carregaObjetivosEstrategicos()"/>
				<t:property name="objetivoMapaEstrategico" itens="objetivoMapaEstrategicoService.findByUnidadeGerencialPerspectivaThroughMapaEstrategico(unidadeGerencial,perspectivaMapaEstrategico)" style="width:70%;" onchange="carregaIniciativas()"/>
			</n:comboReloadGroup>
		</t:tabelaFiltro>		
	</t:janelaFiltro>
	<t:janelaResultados>
		<t:tabelaResultados showEditarLink="false" showExcluirLink="false">
			<t:property name="objetivoMapaEstrategico"/>
			<t:property name="nome"/>
			<t:property name="melhor"/>			
			<t:property name="unidadeMedida"/>
			<t:property name="precisao"/>
			<t:property name="tolerancia" bodyStyle="text-align:left"/>
			<t:property name="frequencia"/>
			<t:acao>	
				<n:link action="copiar" parameters="id=${indicador.id}&unidadeGerencialId=${unidadeGerencialId}&objetivoMapaEstrategicoId=${objetivoMapaEstrategicoId}" description="Copiar indicador"><img src="../../images/ico_copiar.gif"></n:link>
			</t:acao>
		</t:tabelaResultados>
	</t:janelaResultados>
	
</t:listagem>

<script type="text/javascript">

	function carregaPerspectivas() {
		setTimeout('form[\'perspectivaMapaEstrategico\'].loadItens()',1);
	}
	
	function carregaObjetivosEstrategicos(){
		setTimeout('form[\'objetivoMapaEstrategico\'].loadItens()',1);
	}
</script>

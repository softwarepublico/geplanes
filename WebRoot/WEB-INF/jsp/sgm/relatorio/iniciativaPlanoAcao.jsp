<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="n" uri="neo"%>
<%@ taglib prefix="t" uri="template"%>
<%@ taglib prefix="f" tagdir="/WEB-INF/tags/"%>


<t:listagem titulo="Planos de Ação das Iniciativas" showNewLink="false">
	<t:janelaFiltro>
		<input type="hidden" name="reload" value="" id="idReload">
		<t:tabelaFiltro showSubmit="false">
			<n:comboReloadGroup useAjax="true">
				<t:property name="planoGestao" style="width: 100px;"/>
				<n:output styleClass="desc11" value="Unidade Gerencial" />
				<n:panel>
					<f:unidadeGerencialInput name="unidadeGerencial" onchange="carregaPerspectivas()"/>
					&nbsp;<t:property name="incluirSubordinadas" />
				</n:panel>
				<t:property name="perspectivaMapaEstrategico" itens="perspectivaMapaEstrategicoService.findByUnidadeGerencialThroughMapaEstrategico(unidadeGerencial,true)" style="width:70%;" onchange="carregaObjetivosEstrategicos()"/>
				<t:property name="objetivoMapaEstrategico" itens="objetivoMapaEstrategicoService.findByUnidadeGerencialPerspectivaThroughMapaEstrategico(unidadeGerencial,perspectivaMapaEstrategico)" style="width:70%;" onchange="carregaIniciativas()"/>
				<t:property name="iniciativa" itens="iniciativaService.findByUnidadeGerencialObjetivoEstrategico(unidadeGerencial, objetivoMapaEstrategico)" style="width:70%;" />
			</n:comboReloadGroup>
			<t:property name="expirado" trueFalseNullLabels="Sim,Não,"/>
			<t:property name="listaStatus" size="3" includeBlank="false" type="SELECT_MANY" itens="mapaStatus"/>
		</t:tabelaFiltro>
	</t:janelaFiltro>
	<table align="right">
		<tr>
			<td>
				<n:submit action="gerar" class="botao_normal" validate="true">Gerar relatório</n:submit>
			</td>
		</tr>
	</table> 
</t:listagem>


<script language="javascript">

	function carregaPerspectivas() {
		setTimeout('form[\'perspectivaMapaEstrategico\'].loadItens()',1);
	}

	function carregaObjetivosEstrategicos() {
		setTimeout('form[\'objetivoMapaEstrategico\'].loadItens()',1);
	}
		
	function carregaIniciativas() {
		setTimeout('form[\'iniciativa\'].loadItens()',1);
	}	
	
</script>

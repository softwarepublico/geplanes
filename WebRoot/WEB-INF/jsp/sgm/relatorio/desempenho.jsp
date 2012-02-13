<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="n" uri="neo"%>
<%@ taglib prefix="t" uri="template"%>
<%@ taglib prefix="f" tagdir="/WEB-INF/tags/"%>


<t:listagem titulo="Relatório de Desempenho" showNewLink="false">
	<t:janelaFiltro>
		<t:tabelaFiltro showSubmit="false">
			<t:property name="planoGestao" style="width: 100px;"/>
			<n:panel class="form_filtro_label">
				<span class="desc11">Tipo</span>			
			</n:panel>			
			<n:panel>
				<input type="radio" style="border:none" name="tipoRelatorio" value="1" checked="checked" onclick="selecionaTipoRelatorio(1)"/>&nbsp;Desempenho Por Unidade Gerencial
				<input type="radio" style="border:none" name="tipoRelatorio" value="2" onclick="selecionaTipoRelatorio(2)"/>&nbsp;Desempenho Por Objetivo Estratégico
			</n:panel>
			<n:output styleClass="desc11" value="Unidade Gerencial"/>
			<n:panel>
				<f:unidadeGerencialInput name="unidadeGerencial"/>
			</n:panel>
			<t:property name="objetivoEstrategico" style="width: 600px;" id="idEstrategia"/>
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

	// Seta o default do tipo como Por Objetivo Estratégico
	selecionaTipoRelatorio(1);

	function selecionaTipoRelatorio(tipoRelatorio) {
		var elementoEstrategia = $('#idEstrategia');
		var elementoUnidadeGerencialLabel = $('#unidadeGerencial_label');
		var elementoUnidadeGerencialButton = $('#botaoEscolherunidadeGerencial');
		
		if (tipoRelatorio == 1) { // Por Unidade Gerencial
			elementoEstrategia.val("");
			elementoEstrategia.attr("disabled","true");
			
			$('select[name=planoGestao]').change();
			
			elementoUnidadeGerencialLabel.removeAttr("disabled");
			elementoUnidadeGerencialButton.show();		
		}
		else { //Por Objetivo Estratégico
			elementoEstrategia.removeAttr("disabled");
			
			// Limpa Unidade Gerencial
			$geplanesUtil.limpaUnidadeGerencial();
						
			elementoUnidadeGerencialLabel.attr("disabled","true");
			elementoUnidadeGerencialButton.hide();			
		}
	}	
</script>
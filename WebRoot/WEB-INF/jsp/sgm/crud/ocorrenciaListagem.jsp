<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="n" uri="neo"%>
<%@ taglib prefix="t" uri="template"%>
<%@ taglib prefix="f" tagdir="/WEB-INF/tags/"%>

<t:listagem titulo="Listagem de ocorrências">

	<t:janelaFiltro>
		<t:tabelaFiltro>
			<t:property name="planoGestao" style="width: 100px;"/>
			
			<n:output styleClass="desc11" value="Unidade Gerencial"/>
			<n:panel>
				<f:unidadeGerencialInput name="unidadeGerencial" />
			</n:panel>
				
		</t:tabelaFiltro>
	</t:janelaFiltro>

	<t:janelaResultados>
		<t:tabelaResultados>
			<t:property name="unidadeGerencial" />
			<t:property name="numero" />
			<t:property name="descricao" />
			<t:property name="relator" />
			<t:property name="dataLancamento" />
			<t:property name="reincidente" />
		</t:tabelaResultados>
		
		<c:if test="${mostrarImprimir}">
			<div align="right">
				<br>
				<n:submit url="/sgm/report/DiarioBordo" confirmationScript="validarForm()" action="gerar" class="botao_normal">Imprimir</n:submit>
			</div>
		</c:if>
	</t:janelaResultados>
</t:listagem>
<script type="text/javascript">
	function validarForm(){
		if(form['unidadeGerencial'].value == '<null>'){
			alert("O campo Unidade gerencial é obrigatório.");
			return false;
		}
		return true;
	}
</script>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="n" uri="neo"%>
<%@ taglib prefix="t" uri="template"%>
<%@ taglib prefix="f" tagdir="/WEB-INF/tags/"%>


<t:listagem titulo="Mapa de Competências" showNewLink="false">
	<t:janelaFiltro>
		<input type="hidden" name="reload" value="" id="idReload">
		<t:tabelaFiltro showSubmit="false">
			<t:property name="planoGestao" style="width: 100px;"/>
			<n:output styleClass="desc11" value="Unidade Gerencial" />
			<n:panel>
				<f:unidadeGerencialInput name="unidadeGerencial" estiloclasse="required"/>
		</n:panel>
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
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="n" uri="neo"%>
<%@ taglib prefix="t" uri="template"%>

<t:listagem titulo="Listagem de Unidades de medida">
	<t:janelaFiltro>
		<t:tabelaFiltro>
			<t:property name="nome" 	style="width: 200px;overflow:hidden;" />
			<t:property name="sigla"	style="width: 100px;overflow:hidden;" />
		</t:tabelaFiltro>
	</t:janelaFiltro>
	
	<t:janelaResultados>
		<t:tabelaResultados>
			<t:property name="nome"/>
			<t:property name="sigla"/>
		</t:tabelaResultados>
	</t:janelaResultados>
</t:listagem>
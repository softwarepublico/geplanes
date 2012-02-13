<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="n" uri="neo"%>
<%@ taglib prefix="t" uri="template"%>

<t:listagem titulo="Listagem de Perspectivas">
	<t:janelaFiltro>
		<t:tabelaFiltro>
			<t:property name="descricao" style="width: 200px;overflow:hidden;" />
		</t:tabelaFiltro>
	</t:janelaFiltro>
	
	<t:janelaResultados>
		<t:tabelaResultados>
			<t:property name="descricao"/>
		</t:tabelaResultados>
	</t:janelaResultados>
</t:listagem>
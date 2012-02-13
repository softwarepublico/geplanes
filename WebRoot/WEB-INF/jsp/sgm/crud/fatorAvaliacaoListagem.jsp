<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="n" uri="neo"%>
<%@ taglib prefix="t" uri="template"%>

<t:listagem titulo="Listagem de Sistemas de pontuação">
	<t:janelaFiltro>
		<t:tabelaFiltro>
			<t:property name="nome"/>
		</t:tabelaFiltro>
	</t:janelaFiltro>
	<t:janelaResultados>
		<t:tabelaResultados>
			<t:property name="nome"/>
			<t:property name="utilizarMatrizFCS"/>
		</t:tabelaResultados>
	</t:janelaResultados>
</t:listagem>

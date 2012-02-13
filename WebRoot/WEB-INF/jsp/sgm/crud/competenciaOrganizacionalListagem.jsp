<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="n" uri="neo"%>
<%@ taglib prefix="t" uri="template"%>
<%@ taglib prefix="f" tagdir="/WEB-INF/tags/"%>

<t:listagem titulo="Listagem de Competências organizacionais">

	<t:janelaFiltro>
	<t:janelaFiltro>
		<t:tabelaFiltro>
			<t:property name="nome" style="width: 200px;overflow:hidden;"/>
		</t:tabelaFiltro>
	</t:janelaFiltro>
	</t:janelaFiltro>

	<t:janelaResultados>
		<t:tabelaResultados>
			<t:property name="nome" />
			<t:property name="descricao" />
		</t:tabelaResultados>
	</t:janelaResultados>
	
</t:listagem>
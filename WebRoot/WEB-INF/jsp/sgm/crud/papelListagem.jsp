<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="n" uri="neo"%>
<%@ taglib prefix="t" uri="template"%>

<t:listagem titulo="Listagem de Níveis de Acesso">
	<%--
	<t:janelaFiltro>
		<t:tabelaFiltro>
		</t:tabelaFiltro>
	</t:janelaFiltro>
	--%>
	<t:janelaResultados>
		<t:tabelaResultados>
		<t:property name="nome"/>
		</t:tabelaResultados>
	</t:janelaResultados>
</t:listagem>

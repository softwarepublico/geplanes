<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="n" uri="neo"%>
<%@ taglib prefix="t" uri="template"%>

<t:listagem titulo="LISTAGEM DE MENSAGENS">
	<t:janelaFiltro>
		<t:tabelaFiltro>
			<t:property name="conteudo" style="width: 400px;"/>
			<t:property name="dataInicio" style="width: 100px;" />
			<t:property name="dataFim" style="width: 100px;" />
			<t:property name="visivel" trueFalseNullLabels="Sim,Não,"/>
		</t:tabelaFiltro>
	</t:janelaFiltro>

	<t:janelaResultados>
		<t:tabelaResultados >
			<t:property name="visivel" />
			<t:property name="conteudo" />
			<t:property name="dataLancamento" />
		</t:tabelaResultados>
	</t:janelaResultados>
</t:listagem>



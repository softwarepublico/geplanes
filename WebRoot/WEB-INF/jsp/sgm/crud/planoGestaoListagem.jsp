<%@page import="br.com.linkcom.neo.controller.crud.CrudController"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="n" uri="neo"%>
<%@ taglib prefix="t" uri="template"%>

<t:listagem titulo="Listagem de ${PLANOS_DE_GESTAO}">
	<t:janelaFiltro>
		<t:tabelaFiltro>
			<t:property name="anoExercicio" style="width: 100px;overflow:hidden;" />
			<t:property name="descricao" style="width: 300px;overflow:hidden;" />
		</t:tabelaFiltro>
	</t:janelaFiltro>

	<t:janelaResultados>
		<t:tabelaResultados>
			<t:property name="anoExercicio" />
			<t:property name="descricao" />
			<t:acao ladoDireito="false">
				<n:hasAuthorization url="/sgm/crud/PlanoGestao" action="<%= CrudController.EDITAR %>">
					<n:link action="editar" parameters="id=${planoGestao.id}&copiar=true" description="Copiar ano da gestão, unidades gerenciais, iniciativas e indicadores"><img src="../../images/ico_copiar.gif"></n:link>
				</n:hasAuthorization>
			</t:acao>
		</t:tabelaResultados>
	</t:janelaResultados>
</t:listagem>

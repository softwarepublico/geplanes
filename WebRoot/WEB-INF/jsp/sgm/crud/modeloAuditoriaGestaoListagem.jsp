<%@page import="br.com.linkcom.neo.controller.crud.CrudController"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="n" uri="neo"%>
<%@ taglib prefix="t" uri="template"%>

<t:listagem titulo="Modelo de auditoria de gestão">
	<t:janelaFiltro>
		<t:tabelaFiltro>
			<t:property name="nome"/>
		</t:tabelaFiltro>
	</t:janelaFiltro>
	<t:janelaResultados>
		<t:tabelaResultados>
			<t:property name="nome"/>
			<t:acao ladoDireito="false">
				<n:hasAuthorization url="/sgm/crud/ModeloAuditoriaGestao" action="<%= CrudController.EDITAR %>">
					<n:link action="criar" parameters="id=${modeloAuditoriaGestao.id}&copiar=true" description="Copiar o modelo de auditoria"><img src="../../images/ico_copiar.gif"></n:link>
				</n:hasAuthorization>
			</t:acao>
		</t:tabelaResultados>
	</t:janelaResultados>
</t:listagem>

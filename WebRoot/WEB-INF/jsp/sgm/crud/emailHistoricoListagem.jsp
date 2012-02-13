<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="n" uri="neo"%>
<%@ taglib prefix="t" uri="template"%>

<t:listagem showNewLink="false" titulo="Histórico de e-mails enviados">
	<t:janelaFiltro>
		<t:tabelaFiltro>
			<t:property name="remetente" style="width: 300px;overflow:hidden;" />
			<t:property name="assunto" style="width: 300px;overflow:hidden;" />
			<t:property name="dtInicio" style="width:72px;overflow:hidden;"/>
			<t:property name="dtFim" style="width:72px;overflow:hidden;"/>
		</t:tabelaFiltro>
	</t:janelaFiltro>

	<t:janelaResultados>
		<t:tabelaResultados showExcluirLink="false" showEditarLink="false">
			<t:property name="remetente" />
			<t:property name="destinatarioResumido"/>
			<t:property name="assunto" />
			<t:property name="strData" label="data"/>
			<t:acao>
				<n:link action="editar" img="../../images/ico_consultar.gif" parameters="${n:idProperty(n:reevaluate(TtabelaResultados.name,pageContext))}=${n:id(n:reevaluate(TtabelaResultados.name,pageContext))}" description="Consultar detalhes"/>
			</t:acao>			
		</t:tabelaResultados>
	</t:janelaResultados>
</t:listagem>

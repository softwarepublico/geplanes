<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="n" uri="neo"%>
<%@ taglib prefix="t" uri="template"%>
<%@ taglib prefix="f" tagdir="/WEB-INF/tags/"%>

<t:listagem titulo="Listagem de unidades gerenciais">
	<t:janelaFiltro>
		<t:tabelaFiltro>
			<t:property name="nome" style="overflow:hidden;" />
			<t:property name="sigla" style="overflow:hidden;" />
			<t:property name="nivelHierarquico"/>
			<t:property name="planoGestao" style="width: 100px;" onchange="$geplanesUtil.limpaSubordinacao()"/>
			<n:output value="Subordinação" styleClass="desc11"/>
			<n:panel>
				<f:unidadeGerencialInput name="subordinacao"/>
			</n:panel>
		</t:tabelaFiltro>
	</t:janelaFiltro>
	<t:janelaResultados>
		<t:tabelaResultados showExcluirLink="false">
			<t:property name="nome" style="overflow:hidden;"/>
			<t:property name="sigla" style="overflow:hidden;"/>
			<t:property name="planoGestao"/>
			<t:property name="subordinacao"/>
			<t:acao>
				<%--
				<c:if test="${unidadeGerencial.podeImprimirMapaNegocio}">
				  	<n:link url="/sgm/report/MapaNegocio" action="gerar" parameters="unidadeGerencial.id=${unidadeGerencial.id};planoGestao.id=${unidadeGerencial.planoGestao.id}">
						<img src="../../images/pdfMapaNegocio.gif" border="0" title="Mapa do negócio"/>
					</n:link>		
				</c:if>
				--%>
				<n:link action="excluir" img="../../images/ico_excluir.gif"
					confirmationMessage="ATENÇÃO! A exclusão da unidade gerencial irá apagar todos os dados a ela vinculados, inclusive as unidades gerenciais subordinadas. Deseja realmente excluir esse registro?"
					parameters="${n:idProperty(n:reevaluate(TtabelaResultados.name,pageContext))}=${n:id(n:reevaluate(TtabelaResultados.name,pageContext))}" />				
			</t:acao>
		</t:tabelaResultados>
	</t:janelaResultados>
</t:listagem>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="n" uri="neo"%>
<%@ taglib prefix="t" uri="template"%>
<%@ taglib prefix="f" tagdir="/WEB-INF/tags"%>

<t:listagem titulo="Auditoria de gestão">
	<t:janelaFiltro>
		<t:tabelaFiltro>
			<t:property name="planoGestao" style="width: 100px;"/>
			<n:output styleClass="desc11" value="Unidade Gerencial"/>
			<n:panel>
				<f:unidadeGerencialInput name="unidadeGerencial"/> 
			</n:panel>
			<t:property name="modeloAuditoriaGestao"/>
			<t:property name="descricao"/>
			<n:output styleClass="desc11" value="Data da auditoria"/>
			<n:panel>
				<t:propertyConfig showLabel="false" renderAs="single">
					<t:property name="dataAuditoria1"/> 
					até
					<t:property name="dataAuditoria2"/>
				</t:propertyConfig>
			</n:panel>
		</t:tabelaFiltro>
	</t:janelaFiltro>
	<t:janelaResultados>
		<t:tabelaResultados>
			<t:property name="descricao"/>
			<t:property name="unidadeGerencial.planoGestao"/>
			<t:property name="unidadeGerencial"/>
			<t:property name="responsavel"/>
			<t:property name="dataAuditoria"/>
			<c:if test="${podeGerarRelatorio}">
				<t:acao ladoDireito="false">
					<n:link url="/sgm/report/AuditoriaGestao" action="gerar" parameters="id=${auditoriaGestao.id}" description="Relatório de Auditoria"><img src="../../images/pdf.gif" border="0"></n:link>
				</t:acao>			
			</c:if>
		</t:tabelaResultados>
	</t:janelaResultados>
</t:listagem>
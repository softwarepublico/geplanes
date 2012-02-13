<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="n" uri="neo"%>
<%@ taglib prefix="t" uri="template"%>
<%@ taglib prefix="f" tagdir="/WEB-INF/tags"%>

<t:tela titulo="Auditoria de gestão">
	<t:janelaFiltro>
		<t:tabelaFiltro>
			<input type="hidden" name="reload" value="true" id="idReload">
			<t:property name="planoGestao" style="width: 100px;"/>
			<n:output styleClass="desc11" value="Unidade Gerencial"/>
			<n:panel>
				<f:unidadeGerencialInput name="unidadeGerencial"/>
			</n:panel>
			<t:property name="modeloAuditoriaGestao"/>
			<t:property name="descricao"/>
			<n:output styleClass="desc" value="Data da auditoria"/>
			<n:panel>
				<t:propertyConfig showLabel="false" renderAs="single">
					<t:property name="dataAuditoria1"/> 
					até
					<t:property name="dataAuditoria2"/>
				</t:propertyConfig>
			</n:panel>
		</t:tabelaFiltro>
	</t:janelaFiltro>
	<n:dataGrid itens="${listaAuditoriaGestao}" var="auditoriaGestao" id="tabelaResultados" headerStyleClass="txt_tit" bodyStyleClasses="txt_l1, txt_l2" footerStyleClass="listagemFooter" styleClass="fd_tabela1">
		<n:bean name="auditoriaGestao" valueType="<%= br.com.linkcom.sgm.beans.AuditoriaGestao.class %>">
			<t:property name="unidadeGerencial.planoGestao"/>
			<t:property name="unidadeGerencial"/>
			<t:property name="descricao"/>
			<t:property name="responsavel"/>
			<t:property name="dataAuditoria"/>
			<n:column header="">
				<n:link url="/sgm/report/AuditoriaGestao" action="gerar" parameters="id=${auditoriaGestao.id}" description="Relatório de Auditoria"><img src="../../images/pdf.gif" border="0"></n:link>
			</n:column>
		</n:bean>
	</n:dataGrid>
</t:tela>
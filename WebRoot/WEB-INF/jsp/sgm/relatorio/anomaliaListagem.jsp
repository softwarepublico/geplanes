<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="n" uri="neo"%>
<%@ taglib prefix="t" uri="template"%>
<%@ taglib prefix="f" tagdir="/WEB-INF/tags/"%>


<t:listagem titulo="Anomalias" showNewLink="false">
  <t:janelaFiltro>
		<t:tabelaFiltro showSubmit="false">
			<t:property name="planoGestao" style="width: 100px;"/>		
			<n:output styleClass="desc11" value="Setor de registro"/>
			<n:panel>
				<f:unidadeGerencialInput name="ugRegistro"/>
				&nbsp;<t:property name="incluirSubordinadasReg"/>
			</n:panel>
			<n:output styleClass="desc11" value="Setor responsável pelo tratamento"/>
			<n:panel>
				<f:unidadeGerencialInput name="ugResponsavel"/>
				&nbsp;<t:property name="incluirSubordinadasResp"/>
			</n:panel>
			<t:property name="status"/>	
			<t:property name="statusTratamento"/>	
		</t:tabelaFiltro>
	</t:janelaFiltro>
	<table align="right">
		<tr>
			<td>
				<n:submit action="gerar" class="botao_normal" validate="true">Gerar relatório</n:submit>
			</td>
		</tr>
	</table>            
</t:listagem>
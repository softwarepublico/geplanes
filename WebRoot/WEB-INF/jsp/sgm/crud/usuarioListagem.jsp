<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="n" uri="neo"%>
<%@ taglib prefix="t" uri="template"%>
<%@ taglib prefix="f" tagdir="/WEB-INF/tags/"%>

<t:listagem titulo="Listagem de usuários">
	<t:janelaFiltro>
		<t:tabelaFiltro>
			<t:property name="nome"			style="width: 300px;overflow:hidden;"/>
			<t:property name="email"		style="width: 300px;overflow:hidden;"/>
			<t:property name="login"		style="width: 150px;overflow:hidden;"/>
			<t:property name="papel" 		style="width: 150px;overflow:hidden;"/>
			<t:property name="bloqueado"	trueFalseNullLabels="Sim,Não,"/>
			<t:property name="planoGestao"	style="width: 100px;"/>
			<n:output styleClass="desc11" value="Unidade Gerencial"/>
			<n:panel>
				<f:unidadeGerencialInput name="unidadeGerencial"/>
				&nbsp;<t:property name="incluirSubordinadas"/>
			</n:panel>			
		</t:tabelaFiltro>
	</t:janelaFiltro>
	
	<t:janelaResultados>
		<t:tabelaResultados>
			<t:property name="ugsAtuais" order=""/>
			<t:property name="nome"/>
			<t:property name="email"/>
			<t:property name="login"/>
			<t:property name="bloqueado"/>
			<t:property name="listaPapel" label="Nível de Acesso" order="usuario.id"/>
		</t:tabelaResultados>
	</t:janelaResultados>
</t:listagem>
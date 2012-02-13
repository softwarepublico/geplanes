<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="n" uri="neo"%>
<%@ taglib prefix="t" uri="template"%>
<%@ taglib prefix="f" tagdir="/WEB-INF/tags/"%>

<t:listagem titulo="Listagem de usuários" showNewLink="false">
	<t:janelaFiltro>
		<t:tabelaFiltro showSubmit="false">
			<t:property name="nome"			style="width: 300px;overflow:hidden;"/>
			<t:property name="email"		style="width: 300px;overflow:hidden;"/>
			<t:property name="login"		style="width: 150px;overflow:hidden;"/>
			<t:property name="papel"		style="width: 150px;overflow:hidden;"/>
			<t:property name="bloqueado"	trueFalseNullLabels="Sim,Não,"/>
			<t:property name="planoGestao"	style="width: 100px;"/>
			<n:output styleClass="desc" value="Unidade Gerencial"/>
			<n:panel>
				<f:unidadeGerencialInput name="unidadeGerencial"/>
				&nbsp;<t:property name="incluirSubordinadas"/>
			</n:panel>			
		</t:tabelaFiltro>
		<table class="form_filtro_pesquisar">
			<tr>
				<td>
					<n:input name="resetCurrentPage" type="hidden" write="false" />		
					<button class="botao_normal" onclick="window.opener.incluirDestinatarios(new Array(${idsFormatados}))" type="button">todos em destinatário</button>&nbsp;&nbsp;&nbsp;	
					<button class="botao_normal" onclick="window.opener.incluirCcs(new Array(${idsFormatados}))" type="button">todos em cc</button>&nbsp;&nbsp;&nbsp;	
					<button class="botao_normal" onclick="window.opener.incluirCcos(new Array(${idsFormatados}))" type="button">todos em cco</button>&nbsp;&nbsp;&nbsp;	
					<input type="submit"  class="botao_normal" onclick="javascript:doFilter();resetPage();" value="pesquisar">&nbsp;&nbsp;&nbsp;
					<button class="botao_normal" onclick="window.close()" type="button">fechar</button>
			</td>
			</tr>
		</table>
	</t:janelaFiltro>
	
	<t:janelaResultados>
		<t:tabelaResultados showEditarLink="false" showExcluirLink="false">
			<t:property name="ugsAtuais" order=""/>
			<t:property name="nome"/>
			<t:property name="email"/>
			<t:property name="login"/>
			<t:property name="bloqueado"/>
			<t:property name="listaPapel" label="Perfil" order="usuario.id"/>
			<n:column header="">
				<n:body style="text-align: center;">
					<button class="botao_normal" style="width: 100px;" onclick="window.opener.incluirDestinatario('${usuario.nome}','${usuario.email}')" type="button">destinatário</button><BR>
					<button class="botao_normal" style="width: 100px;" onclick="window.opener.incluirCc('${usuario.nome}','${usuario.email}')" type="button">cc</button><BR>
					<button class="botao_normal" style="width: 100px;" onclick="window.opener.incluirCco('${usuario.nome}','${usuario.email}')" type="button">cco</button>
				</n:body>
			</n:column>
		</t:tabelaResultados>
	</t:janelaResultados>
</t:listagem>

<script language="javascript">
	function doFilter(){
		form.ACAO.value ='${TabelaFiltroTag.submitAction}';
		form.action = '';
		form.validate = '${TabelaFiltroTag.validateForm}';
		submitForm();
	}
	
	function resetPage(){
		form.resetCurrentPage.value = 'true';
	}
	
	function resetFilter(){
		form.clearFilter.value = 'true';
		form.validate = 'false';
		form.suppressValidation.value = 'true';
	}	
</script>
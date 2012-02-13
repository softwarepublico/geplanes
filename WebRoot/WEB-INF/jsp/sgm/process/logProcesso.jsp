<%@page import="br.com.linkcom.sgm.beans.LogProcesso"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="n" uri="neo"%>
<%@ taglib prefix="t" uri="template"%>
<%@ taglib prefix="code" uri="code"%>
<%@ taglib prefix="f" tagdir="/WEB-INF/tags/"%>

<t:tela titulo="Processos de Notificação / Atualização">
	<n:dataGrid itens="${logProcesso}" var="logProcessoItem" headerStyleClass="txt_tit" bodyStyleClasses="txt_l1, txt_l2" footerStyleClass="listagemFooter" styleClass="fd_tabela1">
		<n:bean name="logProcessoItem" valueType="<%= LogProcesso.class %>">
			<n:column header="Item" style="width: 400px;">
				<t:property name="item"/>
			</n:column>
			<n:column header="Data/hora da última atualização" style="text-align:center;">
				<t:property name="strDtHrAtualizacao"/>
			</n:column>
			<n:column header="Ação" style="width: 30px; text-align:center;">
				
						<img src="../../images/btn_trocar.gif" 
							 border="0" 
							 onmouseover="Tip('Atualiza ${logProcessoItem.item}')"
							 onclick="atualiza('${logProcessoItem.acao}');"
							 style="cursor: pointer;"
						/>
				
			</n:column>
		</n:bean>
	</n:dataGrid>
</t:tela>
<script type="text/javascript">
	function atualiza(acao){
		form.validate = 'false'; 
		form.suppressErrors.value = 'true';
		form.ACAO.value = acao;
		form.suppressValidation.value = 'true';
		submitForm();
	}
</script>
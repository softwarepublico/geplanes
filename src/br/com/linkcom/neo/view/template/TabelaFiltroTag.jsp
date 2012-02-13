<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="n" uri="neo"%>
<%@ taglib prefix="t" uri="template"%>
<c:set var="submitLabel" value="${n:default('Filtrar', TabelaFiltroTag.submitLabel)}" />
<c:set var="panelGridColumns" value="${n:default(1, TabelaFiltroTag.columns)}" />
<c:set var="panelGridStyleClass" value="${n:default('inputTable', TabelaFiltroTag.styleClass)}" />
<c:set var="panelGridColumnStylesClasses" value="${n:default('doublelineformat', TabelaFiltroTag.columnStyleClasses)}" />
<t:acao></t:acao><%-- Resetando a tag ação --%>
<n:panelGrid columns="${panelGridColumns}"
	 style="${tag.style}"
	 colspan="${tag.colspan}"
	 columnStyleClasses="${panelGridColumnStylesClasses}"
	 columnStyles="${tag.columnStyles}"
	 dynamicAttributesMap="${tag.dynamicAttributesMap}"
	 rowStyles="${tag.rowStyles}"
	 styleClass="${panelGridStyleClass}"
	 propertyRenderAsDouble="${tag.propertyRenderAsDouble}" width="${tag.width}" rowStyleClasses="${tag.rowStyleClasses}" cellpadding="1">
		
	<t:propertyConfig mode="input" showLabel="${tag.propertyShowLabel}" renderAs="double">
		<n:doBody />
	</t:propertyConfig>
</n:panelGrid>
<table width="100%">
<TR align="right">
	<td>
		<c:if test="${!pos_acao_ladoDireito}">
			${acoes}
		</c:if>
		<c:if test="${empty TabelaFiltroTag.showSubmit || TabelaFiltroTag.showSubmit eq true}">
			<n:input name="resetCurrentPage" type="hidden" write="false"/>
			<input type="hidden" name="clearFilter" value="false">
			<n:link url="#" onclick="javascript:doFilter();resetPage();" id="btn_filtro">Filtrar</n:link>		
			|&nbsp;&nbsp;
			<n:link url="#" onclick="javascript:${tag.dynamicAttributesMap['beforeclear']};javascript:resetFilter();$geplanesUtil.clearForm(\"form\");${tag.dynamicAttributesMap['afterclear']};submitForm();" id="btn_limpar">Limpar filtro</n:link>
		</c:if>
		<c:if test="${pos_acao_ladoDireito}">
			${acoes}
		</c:if>
	</td>
</TR>
</table>
<c:if test="${tag.showSubmit}">
	<script>
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
</c:if>
<c:if test="${!tag.showSubmit}">
	<script>
		$("#btn_filtro").hide();
		$("#limpar").hide();
	</script>
</c:if>
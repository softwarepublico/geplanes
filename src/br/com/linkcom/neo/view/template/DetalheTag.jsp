<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="n" uri="neo"%>
<%@ taglib prefix="t" uri="template"%>
<t:acao></t:acao><%-- Resetando a tag ação --%>
<n:panel title="${Tdetalhe.detailDysplayName}">
	<div class="detailBlock">
	<n:dataGrid itens="${Tdetalhe.itens}" cellspacing="1" dynaLine="${Tdetalhe.dynamicAttributesMap['dynaline']==null ? true : Tdetalhe.dynamicAttributesMap['dynaline']}" id="${Tdetalhe.tableId}"  var="${Tdetalhe.detailVar}">
		<n:bean name="${Tdetalhe.detailVar}" valueType="${Tdetalhe.detailClass}" propertyPrefix="${Tdetalhe.fullNestedName}" propertyIndex="${index}">
			<t:propertyConfig mode="input" renderAs="column" disabled="${consultar}">
				<n:doBody/>
			</t:propertyConfig>
			<c:if test="${!consultar}">
				<c:if test="${Tdetalhe.showColunaAcao}">
				<n:column header=" " style="width: 1%; white-space: nowrap; padding-right: 3px;">
					<c:if test="${!pos_acao_ladoDireito}">
						${acoes}
					</c:if>
					<c:if test="${Tdetalhe.showBotaoRemover}">
						<c:if test="${!empty Tdetalhe.beforeDeleteLine}">
							<c:set var="beforeDeleteLine">
								${Tdetalhe.beforeDeleteLine}(getIndexPorNome(this.id),'${Tdetalhe.tableId}');
							</c:set>
						</c:if>
						<c:if test="${!empty Tdetalhe.afterDeleteLine}">
							<c:set var="afterDeleteLine">
								${Tdetalhe.afterDeleteLine}(getIndexPorNome(this.id),'${Tdetalhe.tableId}');
							</c:set>
						</c:if>
						
						<c:if test="${!propertyConfigDisabled || dataGridDynaline}">					
							<a href="#" type="button" class="excluirdetalhe"
								onclick="${Tdetalhe.confirmDeleteStart}${beforeDeleteLine}excluirLinhaPorNome(this.id,true);reindexFormPorNome(this.id, forms[0], '${Tdetalhe.fullNestedName}', true);${afterDeleteLine}${Tdetalhe.confirmDeleteEnd}" id="button.excluir[table_id=${Tdetalhe.tableId}, indice=${rowIndex}]" >
								<img src="${ctx}/images/ico_excluir.gif" alt="Excluir este item" border="0">
							</a>
						</c:if>
						<c:if test="${propertyConfigDisabled && !dataGridDynaline}">
							<button type="button" class="excluirdetalhe" onclick="${Tdetalhe.confirmDeleteStart}${beforeDeleteLine}excluirLinhaPorNome(this.id,true);reindexFormPorNome(this.id, forms[0], '${Tdetalhe.fullNestedName}', true);${Tdetalhe.confirmDeleteEnd}" id="button.excluir[table_id=${Tdetalhe.tableId}, indice=${rowIndex}]" >
								<img src="${ctx}/images/ico_excluir.gif" border="0">
							</button>						
						</c:if>					
					</c:if>
					<c:if test="${pos_acao_ladoDireito}">
						${acoes}
					</c:if>
				</n:column>
				</c:if>
			</c:if>	
		</n:bean>
	</n:dataGrid>
	<c:if test="${!consultar}">
		<div class="lineNovalinha">
			<c:if test="${Tdetalhe.showBotaoNovaLinha}">
				<c:if test="${empty Tdetalhe.dynamicAttributesMap['labelnovalinha']}">
					<c:set value="Nova linha" scope="page" var="labelnovalinha"/>
				</c:if>
				<c:if test="${!empty Tdetalhe.dynamicAttributesMap['labelnovalinha']}">
					<c:set value="${Tdetalhe.dynamicAttributesMap['labelnovalinha']}" scope="page" var="labelnovalinha"/>
				</c:if>
				
				<c:if test="${!propertyConfigDisabled}">
					<button type="button" class="btnApp" id="${Tdetalhe.tableId}Button" onclick="newLine${Tdetalhe.tableId}();${Tdetalhe.onNewLine}">
						${labelnovalinha}
					</button>
				</c:if>
				<c:if test="${propertyConfigDisabled}">
					
					<button type="button" class="btnApp" id="${Tdetalhe.tableId}Button" disabled="disabled" onclick="newLine${Tdetalhe.tableId}();${Tdetalhe.onNewLine}">
						${labelnovalinha}
					</button>
				</c:if>
			</c:if>
		</div>
	</c:if>
	</div>
</n:panel>
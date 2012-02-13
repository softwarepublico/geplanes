<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="n" uri="neo"%>
<n:form validate="false" validateFunction="validarFormulario" enctype="${entradaTag.formEnctype}">
	<n:validation  functionName="validateForm">
		<script language="javascript">
			// caso seja alterada a função validation ela será chamada após a validacao do formulario
			var validation;
			function validarFormulario(){
				var valido = validateForm();
				if(validation){
					valido = validation(valido);
				}
				return valido;
			}
		</script>
		<c:if test="${consultar}">
			<input type="hidden" name="forcarConsulta" value="true"/>
			<style>input, select, textarea, .required {background-color:#ffffff; color:#000000;}</style>
		</c:if>
		<c:if test="${param.fromInsertOne == 'true'}">
			<input type="hidden" name="fromInsertOne" value="true"/>
		</c:if>
		<table class="outterTable" cellspacing="0" align="center">
			<tr class="outterTableHeader">
				<td>
					<span class="outterTableHeaderLeft">${entradaTag.titulo}</span>
					<span class="outterTableHeaderRight">
						<c:if test="${!pos_acao_ladoDireito}">
							${acoes}
						</c:if>					
						<c:if test="${consultar}">
							<c:if test="${(empty entradaTag.showListagemLink) || entradaTag.showListagemLink}">
								<n:link action="listagem" id="btn_voltar"  checkPermission="true" description="Retornar à listagem">Retornar à listagem</n:link>&nbsp;|&nbsp;
							</c:if>
							<c:if test="${(empty entradaTag.showNewLink) || entradaTag.showNewLink}">
								<n:link action="criar"  id="btn_novo" class="outterTableHeaderLink"  checkPermission="true" description="Novo">Novo</n:link>&nbsp;|&nbsp;
							</c:if>
							<c:if test="${(empty entradaTag.showEditLink) || entradaTag.showEditLink}">								
								<n:link action="editar" id="btn_editar" parameters="${n:idProperty(n:reevaluate(TEMPLATE_beanName,pageContext))}=${n:id(n:reevaluate(TEMPLATE_beanName,pageContext))}" class="outterTableHeaderLink" checkPermission="true">Editar</n:link>&nbsp;|&nbsp;
							</c:if>
							<c:if test="${(empty entradaTag.showDeleteLink) || entradaTag.showDeleteLink}">
								<n:link action="excluir" id="btn_excluir" parameters="${n:idProperty(n:reevaluate(TEMPLATE_beanName,pageContext))}=${n:id(n:reevaluate(TEMPLATE_beanName,pageContext))}" confirmationMessage="Você tem certeza que deseja excluir este registro?" class="outterTableHeaderLink"  checkPermission="true">Excluir</n:link>
							</c:if>
						</c:if>
						<c:if test="${!consultar}">
							<c:if test="${(empty entradaTag.showListagemLink) || entradaTag.showListagemLink}">
								<c:if test="${(empty entradaTag.dynamicAttributesMap['showconfirmationreturn']) || entradaTag.dynamicAttributesMap['showconfirmationreturn']}">
									<n:link action="listagem" id="btn_voltar" type="button" class="botao_normal" confirmationMessage="Deseja retornar à listagem sem salvar as alterações?" checkPermission="true" description="Retornar à listagem">Retornar à listagem</n:link>
								</c:if>
								<c:if test="${(!empty entradaTag.dynamicAttributesMap['showconfirmationreturn']) && !entradaTag.dynamicAttributesMap['showconfirmationreturn']}">
									<n:link action="listagem" id="btn_voltar" type="button" class="botao_normal" checkPermission="true" description="Retornar à listagem">Retornar à listagem</n:link>					
								</c:if>
							</c:if>

							<c:if test="${(empty entradaTag.showSaveLink) || entradaTag.showSaveLink}">
								<n:submit type="button" id="btn_gravar" title="Gravar" action="salvar" validate="true" confirmationScript="${entradaTag.dynamicAttributesMap['submitconfirmationscript']}" class="botao_normal" checkPermission="true">Salvar</n:submit>
							</c:if>
						</c:if>
						${entradaTag.invokeLinkArea}
						<c:if test="${pos_acao_ladoDireito}">
							${acoes}
						</c:if>
						<script>
							function alertExclude(){
								confirm("Você tem certeza que deseja excluir este registro?");
							}
							
							function alertCancel(){
								return confirm("Deseja retornar à consulta sem salvar as alterações?");
							}
						</script>				
					</span>				

				</td>
			</tr>
			<tr>
				<td class="tableBody">
					<n:bean name="${TEMPLATE_beanName}">
					<n:doBody />
					</n:bean>
				</td>
			</tr>
		</table>
	</n:validation>
</n:form>
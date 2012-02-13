<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="n" uri="neo"%>
<%@ taglib prefix="t" uri="template"%>

<t:tela titulo="Parâmetros do Sistema" formEnctype="multipart/form-data">
	<t:janelaFiltro>
		<n:tabPanel id="tudo">
			<n:panel title="Geral">
				<t:propertyConfig renderAs="double">
					<n:group columns="2" showBorder="false" style="width:100%">
						<c:if test="${admin}">
							<n:panelGrid columns="1">
							
								<n:panel>
									<fieldset>
										<legend>INDICADORES</legend>
										<n:panelGrid columns="2" style="width:500px" columnStyles="vertical-align:middle">
											<t:property name="diasLembreteCriacaoMetasIndicadores" 	style="width: 50px;overflow:hidden;" 	mode="input" showLabel="true" />
											<t:property name="diasLembreteLancamentoValoresReais" 	style="width: 50px;overflow:hidden;" 	mode="input" showLabel="true" />
			
										</n:panelGrid>
									</fieldset>
								</n:panel>						
		
								<n:panel>
									<fieldset>
										<legend>TRATAMENTO DE ANOMALIAS</legend>
										<n:panelGrid columns="2" style="width:400px" columnStyles="vertical-align:middle">
											<t:property name="notificarEnvolvidosAnomalia" style="width: 50px" mode="input" showLabel="true" trueFalseNullLabels="SIM, NÃO, " />								
											<t:property name="diasLembTratAnomalia" 	style="width: 50px;overflow:hidden;" 	mode="input" showLabel="true" />
											<t:property name="diasTravTratAnomalia" 	style="width: 50px;overflow:hidden;" 	mode="input" showLabel="true" />
											<t:property name="diasEncerramentoAnomalia" 	style="width: 50px;overflow:hidden;" 	mode="input" showLabel="true" />
										</n:panelGrid>
									</fieldset>
								</n:panel>						
								<n:panel>
									<fieldset>
										<legend>CONFIGURAÇÃO DE E-MAIL</legend>
										<n:panelGrid columns="2" style="width:600px" columnStyles="vertical-align:middle;text-align:left;">
											<t:property name="emailRemetente" 						style="width: 200px;overflow:hidden;" 	mode="input" showLabel="true" />					
											<t:property name="emailServidorSMTP" 					style="width: 200px;overflow:hidden;" 	mode="input" showLabel="true" />
											<t:property name="emailPortaSMTP" 						style="width: 40px;overflow:hidden;" 	mode="input" showLabel="true" />
											<t:property name="emailServidorUsaSSL"					style="width: 200px;overflow:hidden;" 	mode="input" showLabel="true" />
											<t:property name="emailNeedAuth" 						style="width: 200px;overflow:hidden;" 	mode="input" showLabel="true" id="idAutenticacao"/>					
											
											<n:panel id="idUsuarioLabel">
												<n:output styleClass="desc11" value="Usuário para autenticação"/>
											</n:panel>
											<t:property name="emailUsuarioDominio" 					style="width: 200px;overflow:hidden;" 	mode="input" showLabel="false" id="idUsuarioText"/>
											
											<n:panel id="idSenhaLabel">
												<n:output styleClass="desc11" value="Senha do usuário para autenticação"/>
											</n:panel>
											<t:property name="emailSenha" type="password"			style="width: 200px;overflow:hidden;" 	mode="input" showLabel="false" id="idSenhaText"/>	
											
											<n:output styleClass="desc10bold" value="TESTAR CONFIGURAÇÃO DO E-MAIL" />
											
											<n:panel>
												<t:property name="emailTeste" style="width: 200px" 	mode="input" showLabel="false" />
												<n:submit class="botao_normal" style="width: 70px" type="button" action="testeemail">Enviar</n:submit>
												<br>
												<n:output styleClass="txt_especial" value="${msgEmail}" />
											</n:panel>														
										</n:panelGrid>
									</fieldset>
								</n:panel>
							</n:panelGrid>
						</c:if>
					</n:group>
				</t:propertyConfig>
			</n:panel>
			<n:panel title="Imagens">
				<t:propertyConfig renderAs="double" mode="input">
					<n:group columns="2" showBorder="false" style="width:100%">
						<n:panelGrid columns="1" colspan="2">
							<n:panel>
								<span class="txt_und12">O tamanho máximo para cada uma das imagens deve ser de 160 x 100 pixels.</span>
							</n:panel>
						</n:panelGrid>					
						<n:panelGrid columns="1" colspan="2">			
							<t:property name="imgEmpresa" showRemoverButton="true" style="overflow:hidden;" />
							<c:if test="${!empty filtro.imgEmpresa.id}">
								<n:panel colspan="2">
									<img src="${ctx}/DOWNLOADFILE/${filtro.imgEmpresa.id}"/>
								</n:panel>
							</c:if>
						</n:panelGrid>
						<n:panelGrid columns="1" colspan="2">
							<n:panel>&nbsp;</n:panel>
						</n:panelGrid>
						<n:panelGrid columns="1" colspan="2">			
							<t:property name="imgEmpresaRelatorio" showRemoverButton="true" style="overflow:hidden;" />
							<c:if test="${!empty filtro.imgEmpresaRelatorio.id}">
								<n:panel colspan="2">
									<img src="${ctx}/DOWNLOADFILE/${filtro.imgEmpresaRelatorio.id}"/>
								</n:panel>
							</c:if>
						</n:panelGrid>						
					</n:group>
				</t:propertyConfig>
			</n:panel>
		</n:tabPanel>
		
		<c:if test="${admin}">
			<div align="right">
				<n:submit action="salvar" class="botao_normal">salvar</n:submit>
			</div>
		</c:if>
		
	</t:janelaFiltro>
</t:tela>

<script type="text/javascript">

	$(document).ready(function() {
		$('#idAutenticacao').click(function(){
			gerenciaCamposAutenticacao($(this).attr("checked"));
		});
		
		gerenciaCamposAutenticacao($('#idAutenticacao').attr("checked"));
	});
	
	function gerenciaCamposAutenticacao(exibirCamposAutenticacao) {
		if (exibirCamposAutenticacao) {
			$('#idUsuarioLabel').show();
			$('#idUsuarioText').show();
			$('#idSenhaLabel').show();
			$('#idSenhaText').show();
		}
		else {
			$('#idUsuarioLabel').hide();
			$('#idUsuarioText').hide();
			$('#idSenhaLabel').hide();
			$('#idSenhaText').hide();
		}
	}
</script>
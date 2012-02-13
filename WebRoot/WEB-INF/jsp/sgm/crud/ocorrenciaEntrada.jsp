<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="n" uri="neo"%>
<%@ taglib prefix="t" uri="template"%>
<%@ taglib prefix="f" tagdir="/WEB-INF/tags/"%>

<t:entrada titulo="Lançamento de ocorrências" showSaveLink="${!opcaoCriarAnomalia}">
	<jsp:attribute name="linkArea">
		<c:if test="${opcaoCriarAnomalia}">
			<button type="button" onclick="javascript:abrirEscolha()" title=""   id='btn_gravar' title='Gravar' checkpermission='true' class='botao_normal'>Salvar</button>
		</c:if>
	</jsp:attribute>
	<jsp:body>
		<t:property name="id" write="false" type="hidden" />
		<t:property name="relator" write="false" type="hidden" />
		<t:janelaEntrada showSubmit="false">
			<t:tabelaEntrada>
				<c:if test="${ocorrencia.id != null}">
					<t:property name="numero" write="true" type="hidden" />
				</c:if>
				<c:choose>
					<c:when test="${editarSomenteSituacao}">
						<t:property name="planoGestao" write="true" type="hidden" />
						<t:property name="unidadeGerencial"  write="true" type="hidden" />
						<t:property name="dataLancamento" write="true" type="hidden" />
						<t:property name="relator" disabled="true" write="true" type="hidden" />
						<t:property name="descricao" write="true" type="hidden" />
						<t:property name="contraMedidasImediatas" write="true" type="hidden" />
						<t:property name="reincidente" write="true" type="hidden" />					
					</c:when>
					<c:otherwise>
						<t:property name="planoGestao" style="width: 100px;" />
						<n:output styleClass="desc" value="Unidade Gerencial" />
						<n:panel>
							<f:unidadeGerencialInput name="unidadeGerencial" estiloclasse="required"/>
						</n:panel>
						<t:property name="dataLancamento" />
						<t:property name="relator" disabled="true" />
						<t:property name="descricao" cols="100" rows="6"/>
						<t:property name="contraMedidasImediatas" cols="100" rows="6"/>
						<t:property name="reincidente" style="border:none" trueFalseNullLabels=" Sim, Não" includeBlank="false" type="SELECT_ONE_RADIO" />					
					</c:otherwise>
				</c:choose>
				<t:property name="situacao" cols="100" rows="2" id="situacao" />
			</t:tabelaEntrada>
		</t:janelaEntrada>
		<n:panel style="padding-left:890px;">
			<c:if test="${opcaoCriarAnomalia}">
				<n:submit action="salvar" class="botao_normal" validate="true" style="display:none;" id="salvar_button_sem">salvar</n:submit>
				<n:submit action="salvarAndEncaminhar" class="botao_normal" validate="true" style="display:none;" id="salvar_button_com" parameters="gerarAnomalia=true">salvar</n:submit>
			</c:if>
		</n:panel>
	</jsp:body>
</t:entrada>
<script language="javascript">

function abrirEscolha(){
	if(validateForm()){
		openModalWindow('${app}/escolhaAnomalia.jsp',650,200, false);
	}
}

function submitComAnomalia(){
	$("#salvar_button_com").click();
}

function submitSemAnomalia(){
	$("#salvar_button_sem").click();
}
</script>
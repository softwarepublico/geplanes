<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="n" uri="neo"%>
<%@ taglib prefix="t" uri="template"%>
<%@ taglib prefix="f" tagdir="/WEB-INF/tags/"%>

<c:choose>
	<c:when test="${podeEncerrarAcaoPreventiva}">			
		<n:panel title="Conclusão">
			<t:tabelaEntrada>
				<n:panelGrid columns="1" columnStyleClasses="form_filtro_value">
					<n:panel><span class="desc">Avaliação da eficácia da ação executada</span></n:panel>
					<t:property name="avalEficaciaAcao" label=""/>
					<n:panel>&nbsp;</n:panel>
					<n:panel><span class="desc">Evidência da eficácia</span></n:panel>
					<t:property name="evidenciaEficaciaAcao" rows="3" cols="175" label=""/>
					<n:panel>&nbsp;</n:panel>
					<n:panel><span class="desc">Conclusão da área de qualidade</span></n:panel>
					<t:property name="conclusao" rows="3" cols="175" label=""/>
				</n:panelGrid>
			</t:tabelaEntrada>
		</n:panel>
	</c:when>
	<c:otherwise>
		<n:panel title="Conclusão">
			<t:tabelaEntrada>
				<n:panelGrid columns="1" columnStyleClasses="form_filtro_value">
					<n:panel><span class="desc">Avaliação da eficácia da ação executada</span></n:panel>
					<t:property name="avalEficaciaAcao" disabled="true" class="readonlyField" label=""/>
					<t:property name="avalEficaciaAcao" type="hidden" write="false" label=""/>
					<n:panel>&nbsp;</n:panel>
					<n:panel><span class="desc">Evidência da eficácia</span></n:panel>
					<t:property name="evidenciaEficaciaAcao" rows="3" cols="175" readonly="true" class="readonlyField" label=""/>
					<n:panel>&nbsp;</n:panel>
					<n:panel><span class="desc">Conclusão da área de qualidade</span></n:panel>
					<t:property name="conclusao" rows="3" cols="175" readonly="true" class="readonlyField" label=""/>
				</n:panelGrid>
			</t:tabelaEntrada>
		</n:panel>
	</c:otherwise>
</c:choose>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="n" uri="neo"%>
<%@ taglib prefix="t" uri="template"%>

<t:entrada titulo="CADASTRO DE MENSAGENS">
	<t:property name="id" type="hidden"/>	
	<t:janelaEntrada>
		<t:tabelaEntrada>
			<c:choose>
				<c:when test="${mensagem.id != null}">
					<t:property name="dataLancamento" type="hidden" write="true"/>
				</c:when>
				<c:otherwise>
					<t:property name="dataLancamento" type="hidden" write="false" label=""/>
				</c:otherwise>
			</c:choose>
			
			<t:property name="visivel" label="Visível na página inicial" />
			<t:property name="conteudo" rows="5" cols="100" onKeyPress="contadorLetras(\"conteudo\", 512, event)" onKeyUp="contadorLetras(\"conteudo\",512, event)"/>
		</t:tabelaEntrada>
	</t:janelaEntrada>
</t:entrada>
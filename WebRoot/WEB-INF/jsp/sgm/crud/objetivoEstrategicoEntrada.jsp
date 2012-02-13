<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="n" uri="neo"%>
<%@ taglib prefix="t" uri="template"%>

<t:entrada titulo="Cadastro de Objetivos estratégicos">
	<t:property name="id" write="false" type="hidden"/>	
	<t:janelaEntrada>
		<t:tabelaEntrada>
			<t:property name="descricao"  rows="4"	style="width: 100%;overflow:hidden;"/>
		</t:tabelaEntrada>
	</t:janelaEntrada>
</t:entrada>
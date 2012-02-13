<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="n" uri="neo"%>
<%@ taglib prefix="t" uri="template"%>

<t:entrada titulo="Cadastro de Unidades de medida">
	<t:property name="id" write="false" type="hidden"/>	
	<t:janelaEntrada>
		<t:tabelaEntrada>
			<t:property name="nome" 	style="width: 200px;overflow:hidden;"/>
			<t:property name="sigla"	style="width: 100px;overflow:hidden;"/>
		</t:tabelaEntrada>
	</t:janelaEntrada>
</t:entrada>
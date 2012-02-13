<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="n" uri="neo"%>
<%@ taglib prefix="t" uri="template"%>

<t:entrada titulo="Cadastro de Competências organizacionais">
	<t:property name="id" write="false" type="hidden"/>	
	<t:janelaEntrada>
		<t:tabelaEntrada>
			<t:property name="nome" style="width: 600px;overflow:hidden;" />
			<t:property name="descricao"  rows="3"	style="width: 600px;overflow:hidden;"/>
		</t:tabelaEntrada>
	</t:janelaEntrada>
</t:entrada>
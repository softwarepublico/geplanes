<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="n" uri="neo"%>
<%@ taglib prefix="t" uri="template"%>

<t:entrada titulo="Cadastro de Normas" submitconfirmationscript="validaFormulario()">
	<t:property name="id" write="false" type="hidden"/>
	<t:janelaEntrada>
		<t:tabelaEntrada>			
			<t:property name="nome" style="width:400px"/>
			<t:property name="descricao" rows="3" cols="100"/>
		</t:tabelaEntrada>
		<t:detalhe name="listaRequisitoNorma" labelnovalinha="Novo requisito">
			<n:column header="Índice">
				<n:body>
					<t:property name="id" type="hidden"/>
					<t:property name="indice" style="width:70px"/>
				</n:body>
			</n:column>
			<t:property name="descricao" rows="2" cols="160"/>
		</t:detalhe>
	</t:janelaEntrada>
</t:entrada>

<script type="text/javascript">
	function validaFormulario(){
		var re = /^\d+(?:\.(?:[a-z]|\d+))*$/;
		var casamento;
		var valido = true;
				
		$("input[name$=indice]").each(function(){
			var indice = $(this).val();
					
			casamento = re.exec(indice);
		
			if (!casamento) {
				alert("O índice " + indice + " não está em um formato válido. Exemplos de formatos aceitos: 1, 1.1, 1.a, etc.");
				valido = false;
			}
		});		
		
		return valido;
	}
</script>
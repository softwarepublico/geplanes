<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="n" uri="neo"%>
<%@ taglib prefix="t" uri="template"%>

<t:entrada titulo="Cadastro de Sistemas de pontuação" submitConfirmationScript="validaFormulario()">
	<t:property name="id" write="false" type="hidden"/>
	<t:janelaEntrada>
		<t:tabelaEntrada>
			<t:property name="nome"/>
			<t:property name="utilizarMatrizFCS"/>
		</t:tabelaEntrada>
		<t:detalhe name="listaItemFatorAvaliacao" labelnovalinha="Novo fator de avaliação">
			<n:column header="Valor">
				<n:body>
					<t:property name="id" type="hidden"/>
					<t:property name="valor" style="width:30px"/>
				</n:body>
			</n:column>
			<t:property name="descricao" style="width:600px"/>
		</t:detalhe>
	</t:janelaEntrada>
</t:entrada>

<script type="text/javascript">
	function validaFormulario(){
		var msg = '';
		var show = false;
		
		if($("input[name^=listaItemFatorAvaliacao][name$=valor]").length == 0){
			msg += 'O fator de avaliação deve possuir pelo menos um item.\n';
			show = true;
		}
		
		
		if(show){
			alert(msg);
		}
		
		return !show;
	}
</script>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="n" uri="neo"%>
<%@ taglib prefix="t" uri="template"%>

<script language="JavaScript" src="${app}/tiny_mce/tiny_mce.js"></script>

<t:entrada titulo="Histórico de e-mails enviados" showSaveLink="false">
	<t:property name="id" write="false" type="hidden"/>	
	<t:janelaEntrada showSubmit="false">
		<t:tabelaEntrada>
			<t:property name="remetente" style="width: 500px;overflow:hidden;" readonly="true" class="readonlyField"/>
			<t:property name="destinatario" rows="2" cols="150" readonly="true" class="readonlyField"/>
			<t:property name="copia" rows="2" cols="150" readonly="true" class="readonlyField"/>
			<t:property name="copiaoculta" rows="2" cols="150" readonly="true" class="readonlyField"/>
			<t:property name="assunto" style="width: 500px;overflow:hidden;" readonly="true" class="readonlyField"/>
			<t:property name="mensagem" readonly="true" class="readonlyField" style="width: 800px; height: 250px;"  id="mensagem"/>
			<t:property name="strData" style="width: 105px;overflow:hidden;" readonly="true" class="readonlyField" label="Data"/>
		</t:tabelaEntrada>
	</t:janelaEntrada>
</t:entrada>

<script type="text/javascript">

	/* Monta o campo "E-mail" com formatador HTML */
	tinyMCE.init({
		mode : "none",
		theme : "advanced",
		content_css : "../../css/HtmlEditorReadonly.css",
		readonly : true
	});

	$(document).ready(function(){
		tinyMCE.execCommand("mceAddControl", true, "mensagem");
	});
	
</script>
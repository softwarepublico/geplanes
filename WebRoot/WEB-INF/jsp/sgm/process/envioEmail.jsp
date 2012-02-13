<%@page import="br.com.linkcom.sgm.controller.filtro.EnvioEmailFiltro"%>
<%@page import="br.com.linkcom.neo.controller.crud.CrudController"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="n" uri="neo"%>
<%@ taglib prefix="t" uri="template"%>
<%@ taglib prefix="f" tagdir="/WEB-INF/tags/"%>

<script language="JavaScript" src="${app}/tiny_mce/tiny_mce.js"></script>

<t:tela titulo="Envio de e-mail" formEnctype="multipart/form-data">
	<n:bean name="filtro" valueType="<%=EnvioEmailFiltro.class%>">
		<n:panelGrid columns="2" style="width:100%;">
			<t:propertyConfig mode="input" showLabel="true" renderAs="double">
				<t:property name="remetente" style="width: 400px;"/>
				<t:property name="assunto" style="width: 400px;"/>
				<t:property name="mensagem" style="width: 700px; height: 250px;"  id="mensagem"/>
				<n:panel colspan="2">
				
					<BR><BR>
				</n:panel>
			</t:propertyConfig>
		</n:panelGrid>
		<n:panel>
		<BR><BR>
			<table class="fd_tela" width="98%" align="center" cellspacing="0">
				<tr>
					<td class='fd_tela_titulo'>
						<div>
							<b>Anexos</b>
						</div>
					</td>
				</tr>
				<tr>
					<td colspan="1">
						<n:panelGrid columns="2" style="width:100%">
							<n:panel colspan="2">
								<t:detalhe name="listaArquivo" id="listaArquivo" style="width:100%" labelnovalinha="Novo anexo">
									<t:property name="anexo" label="Arquivo" showLabel="false" size="100"/>
								</t:detalhe>
							</n:panel>
						</n:panelGrid>
					</td>
				</tr>
			</table>
			<BR><BR>
		</n:panel>
		
		<n:panelGrid columns="2" style="width: 100%;">
			<n:panel style="width: 70px;" valign="top"><span class="desc">NOVO E-MAIL</span></n:panel>
			<n:panel>
				<input name="novoEmail" type="text" style="width: 300px;"/>&nbsp;&nbsp;&nbsp;
				<button class="botao_normal" onclick="clickNovoEmailDestinatario()" type="button">incluir destinatário</button>&nbsp;&nbsp;&nbsp;
				<button class="botao_normal" onclick="clickNovoEmailCc()" type="button">incluir cc</button>&nbsp;&nbsp;&nbsp;
				<button class="botao_normal" onclick="clickNovoEmailCco()" type="button">incluir cco</button>
				<n:hasAuthorization url="/util/crud/Usuario" action="<%=CrudController.LISTAGEM%>">
					&nbsp;&nbsp;&nbsp;
					<button class="botao_normal" onclick="openPopUpUsuarios()" type="button">incluir usuário(s)</button>
				</n:hasAuthorization>
			</n:panel>
		</n:panelGrid>
		
		<n:panel>
		<BR>
			<table class="fd_tela" width="98%" align="center" cellspacing="0">
				<tr>
					<td class='fd_tela_titulo'>
						<div>
							<b>Destinatários</b>
						</div>
					</td>
				</tr>
				<tr>
					<td colspan="1">
						<n:panelGrid columns="2" style="width:100%">
							<n:panel colspan="2" style="text-align:right;">
								<button class="botao_normal" onclick="excluirTodosDestinatarios()" type="button">excluir todos destinatários</button>
							</n:panel>
							<n:panel colspan="2">
								<t:detalhe name="listaDestinatario" showBotaoNovaLinha="false" id="listaDestinatario" style="width:100%" showDeleteLineWarning="false">
									<t:property name="nome" type="hidden" write="true"/>
									<t:property name="email" type="hidden" write="true" label="E-mail"/>
								</t:detalhe>
							</n:panel>
						</n:panelGrid>
					</td>
				</tr>
			</table>
		</n:panel>
		
		<n:panel>
			<BR><BR>
			<table class="fd_tela" width="98%" align="center" cellspacing="0">
				<tr>
					<td class='fd_tela_titulo'>
						<div>
							<b>CC</b>
						</div>
					</td>
				</tr>
				<tr>
					<td colspan="1">
						<n:panelGrid columns="2" style="width:100%">
							<n:panel colspan="2" style="text-align:right;">
								<button class="botao_normal" onclick="excluirTodosCc()" type="button">excluir todos cc</button>
							</n:panel>
							<n:panel colspan="2">
								<t:detalhe name="listaCc" showBotaoNovaLinha="false" id="listaCc" style="width:100%" showDeleteLineWarning="false">
									<t:property name="nome" type="hidden" write="true"/>
									<t:property name="email" type="hidden" write="true" label="E-mail"/>
								</t:detalhe>
							</n:panel>
						</n:panelGrid>
					</td>
				</tr>
			</table>
		</n:panel>
		
		<n:panel>
			<BR><BR>
			<table class="fd_tela" width="98%" align="center" cellspacing="0">
				<tr>
					<td class='fd_tela_titulo'>
						<div>
							<b>CCo</b>
						</div>
					</td>
				</tr>
				<tr>
					<td colspan="1">
						<n:panelGrid columns="2" style="width:100%">
							<n:panel colspan="2" style="text-align:right;">
								<button class="botao_normal" onclick="excluirTodosCco()" type="button">excluir todos cco</button>
							</n:panel>
							<n:panel colspan="2">
								<t:detalhe name="listaCco" showBotaoNovaLinha="false" id="listaCco" style="width:100%" showDeleteLineWarning="false">
									<t:property name="nome" type="hidden" write="true"/>
									<t:property name="email" type="hidden" write="true" label="E-mail"/>
								</t:detalhe>
							</n:panel>
						</n:panelGrid>
					</td>
				</tr>
			</table>
		</n:panel>
		<div style="text-align: right">
			<br>		
			<n:submit class="botao_normal" action="enviarMailling" confirmationScript="validaFormulario()">Enviar e-mail</n:submit>
		</div>
	</n:bean>
</t:tela>


<script type="text/javascript">

	/* Monta o campo "E-mail" com formatador HTML */
	tinyMCE.init({
		mode : "none", 
		theme : "advanced",
		theme_advanced_buttons1 : "bold,italic,underline,separator,strikethrough,justifyleft,justifycenter,justifyright, justifyfull,bullist,numlist,undo,redo,link,unlink,code,separator,fontselect,fontsizeselect",
		theme_advanced_buttons2 : "",
		theme_advanced_buttons3 : "",
		theme_advanced_toolbar_location : "top",
		theme_advanced_toolbar_align : "left",
		theme_advanced_path_location : "none",
		force_br_newlines : true,
		extended_valid_elements : "a[name|href|target|title|onclick],img[class|src|border=0|alt|title|hspace|vspace|width|height|align|onmouseover|onmouseout|name],hr[class|width|size|noshade],font[face|size|color|style],span[class|align|style]"
	});

	$(document).ready(function(){
		tinyMCE.execCommand("mceAddControl", true, "mensagem");
	});

	function validaFormulario(){
		tinyMCE.triggerSave();
		
		if($("input[name$=nome][name^=listaDestinatario]").length == 0){
			alert("Favor adicionar pelo menos um destinatário.");
			return false;
		}
		
		return true;
	}
	
	function openPopUpUsuarios(){
		window.open('${ctx}/util/crud/Usuario', 'ustv', 'scrollbars=yes,height=650,width=900,top=50,left=200,location=no,menubar=no,status=no,toolbar=no,resizable=yes');
	}
	
	function clickNovoEmailDestinatario(){
		var novoEmail = form['novoEmail'].value;
		if(novoEmail == ""){
			alert("Favor digitar um e-mail.");
		} else {
			if(checkEmail(novoEmail)){
				incluirDestinatario('<b>Usuário não cadastrado</b>',novoEmail);
				form['novoEmail'].value = "";
			} else {
				alert("Favor digitar um e-mail válido.");
			}
		}	
	}
	
	function incluirDestinatarios(usuarios){
		for(var i = 0; i < usuarios.length; i++){
			var usu = usuarios[i].split('|');
			incluirDestinatario(usu[0], usu[1]);
		}
	}
	
	function incluirDestinatario(nome,email){
		document.getElementById('listaDestinatario').dataModel[0] = dataModelDestinatarios[0].replace("<null>",nome).replace("</span>",nome + "</span>");
		document.getElementById('listaDestinatario').dataModel[1] = dataModelDestinatarios[1].replace("<null>",email).replace("</span>",email + "</span>");
		
		newLinelistaDestinatario();
	}
	
	var dataModelDestinatarios = [
		"<input type=\"hidden\" name=\"listaDestinatario[{index}].nome\" value=\"<null>\" /><span id=\"listaDestinatario[{index}].nome_value\"></span>",
		"<input type=\"hidden\" name=\"listaDestinatario[{index}].email\" value=\"<null>\" /><span id=\"listaDestinatario[{index}].email_value\"></span>",
		"<img src=\"../../images/ico_excluir.gif\" onclick=\"excluirLinhaPorNome(this.id,true);reindexFormPorNome(this.id, forms[0], 'listaDestinatario', true);\" id=\"button.excluir[table_id=listaDestinatario, indice={indexplus}]\" style=\"cursor:pointer;\" alt=\"Excluir\"/>"
	];
	
	function excluirTodosDestinatarios(){
		$("[id^=button.excluir][id*=listaDestinatario,]").each(function(){
			excluirLinhaPorNome($(this).attr("id"),true);
			reindexFormPorNome($(this).attr("id"), document.forms[0], 'listaDestinatario', true);
		});
	}
	
	function clickNovoEmailCc(){
		var novoEmail = form['novoEmail'].value;
		if(novoEmail == ""){
			alert("Favor digitar um e-mail.");
		} else {
			if(checkEmail(novoEmail)){
				incluirCc('<b>Usuário não cadastrado</b>',novoEmail);
				form['novoEmail'].value = "";
			} else {
				alert("Favor digitar um e-mail válido.");
			}
		}	
	}
	
	function incluirCcs(usuarios){
		for(var i = 0; i < usuarios.length; i++){
			var usu = usuarios[i].split('|');
			incluirCc(usu[0], usu[1]);
		}
	}
	
	function incluirCc(nome,email){
		document.getElementById('listaCc').dataModel[0] = dataModelCc[0].replace("<null>",nome).replace("</span>",nome + "</span>");
		document.getElementById('listaCc').dataModel[1] = dataModelCc[1].replace("<null>",email).replace("</span>",email + "</span>");
		
		newLinelistaCc();
	}
	
	var dataModelCc = [
		"<input type=\"hidden\" name=\"listaCc[{index}].nome\" value=\"<null>\" /><span id=\"listaCc[{index}].nome_value\"></span>",
		"<input type=\"hidden\" name=\"listaCc[{index}].email\" value=\"<null>\" /><span id=\"listaCc[{index}].email_value\"></span>",
		"<img src=\"../../images/ico_excluir.gif\" onclick=\"excluirLinhaPorNome(this.id,true);reindexFormPorNome(this.id, forms[0], 'listaCc', true);\" id=\"button.excluir[table_id=listaCc, indice={indexplus}]\" style=\"cursor:pointer;\" alt=\"Excluir\"/>"
	];
	
	
	function excluirTodosCc(){
		$("[id^=button.excluir][id*=listaCc,]").each(function(){
			excluirLinhaPorNome($(this).attr("id"),true);
			reindexFormPorNome($(this).attr("id"), document.forms[0], 'listaCc', true);		
		});
	}
	
	function clickNovoEmailCco(){
		var novoEmail = form['novoEmail'].value;
		if(novoEmail == ""){
			alert("Favor digitar um e-mail.");
		} else {
			if(checkEmail(novoEmail)){
				incluirCco('<b>Usuário não cadastrado</b>',novoEmail);
				form['novoEmail'].value = "";
			} else {
				alert("Favor digitar um e-mail válido.");
			}
		}	
	}
	
	function incluirCcos(usuarios){
		for(var i = 0; i < usuarios.length; i++){
			var usu = usuarios[i].split('|');
			incluirCco(usu[0], usu[1]);
		}
	}
	
	function incluirCco(nome,email){
		document.getElementById('listaCco').dataModel[0] = dataModelCco[0].replace("<null>",nome).replace("</span>",nome + "</span>");
		document.getElementById('listaCco').dataModel[1] = dataModelCco[1].replace("<null>",email).replace("</span>",email + "</span>");
		
		newLinelistaCco();
	}
	
	var dataModelCco = [
		"<input type=\"hidden\" name=\"listaCco[{index}].nome\" value=\"<null>\" /><span id=\"listaCco[{index}].nome_value\"></span>",
		"<input type=\"hidden\" name=\"listaCco[{index}].email\" value=\"<null>\" /><span id=\"listaCco[{index}].email_value\"></span>",
		"<img src=\"../../images/ico_excluir.gif\" onclick=\"excluirLinhaPorNome(this.id,true);reindexFormPorNome(this.id, forms[0], 'listaCco', true);\" id=\"button.excluir[table_id=listaCco, indice={indexplus}]\" style=\"cursor:pointer;\" alt=\"Excluir\"/>"
	];
	
	
	function excluirTodosCco(){
		$("[id^=button.excluir][id*=listaCco,]").each(function(){
			excluirLinhaPorNome($(this).attr("id"),true);
			reindexFormPorNome($(this).attr("id"), document.forms[0], 'listaCco', true);
		});
	}
	
</script>
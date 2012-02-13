var neofecharcarregando = function(){};
var neoabrircarregando = function(){};				

/**
 * Função que conta e mostra ao usuário o total de caracteres dentro do TextÁrea.
 * @author Matheus Melo Gonçalves
 * @param Nome do TextÁrea,Máximo de caracteres,Event 
 * @see function contadorLetras(nome,max) 
*/
function contadorLetras(nome,max,teclaPres){
	 var TECLA_SETA_ESQUERDA = 37;  
	 var TECLA_SETA_DIREITA  = 39;
	 var TECLA_SETA_CIMA = 38;  
	 var TECLA_SETA_BAIXO  = 40;
	 var TECLA_NAVEGADOR = teclaPres.keyCode;
	 if((TECLA_NAVEGADOR != TECLA_SETA_ESQUERDA)&&
	    (TECLA_NAVEGADOR != TECLA_SETA_DIREITA)&&
	    (TECLA_NAVEGADOR != TECLA_SETA_CIMA)&&
	    (TECLA_NAVEGADOR != TECLA_SETA_BAIXO)){
		 
		 var qtd = document.getElementsByName(nome)[0].value.length;
		 var elContador = document.getElementById("contador");
		 
		 if (elContador != null) { 
		 	elContador.innerHTML="("+ ((max-1) - qtd+1) +" caracteres restantes).";
		 }
		 if(((max-1) - qtd) < 0){
		 	if (elContador != null) {
		    	elContador.innerHTML="Limite de "+(max)+" caracteres excedido.";
			}
			document.getElementsByName(nome)[0].value = document.getElementsByName(nome)[0].value.substring(0,max); 
		 }
		 else if(qtd == 0){
			if (elContador != null) {		 
		  		elContador.innerHTML="";
		  	}
		 }
		 else if((max -1) == qtd) {
			if (elContador != null) {
		   		elContador.innerHTML="(1 caractere restante).";
		   	}
		 }
	  }
}

/**
 * Função para remover as tags de início
 * e fim de bloco javascript. 
*/
function stripJavascriptTags(text) {
	return text.replace("<script>","").replace("</script>","");
}

/**
 * Função para esconder elementos que sobrepoem o menu.
 * @author Matheus Melo Gonçalves
 * Coloque um div com ID = esconderTabela no elemento que deseja esconder. 
*/
function esconderTabela(){
 try{
     if(document.getElementById("esconderTabela").style.display == "none"){
      document.getElementById("esconderTabela").style.display="block";
     }
     else{
      document.getElementById("esconderTabela").style.display="none";
     }
    }catch(err){}
}

function openModalWindow(url, w, h, scroll){
	$.showAkModal(url,'Box', w, h, scroll);
}

function mostraDivCarregando(){	 
   $('#loadmsg').fadeIn("slow");
}

/**
*	Construtor da biblioteca.
*/
function GeplanesUtil(){
}

//declara a biblioteca
var $geplanesUtil = new GeplanesUtil();

/**
 * Limpa os campos do formulário
 */
GeplanesUtil.prototype.clearForm = function(name){
	$("form[name="+name+"] input").each(function(){
		var el = $(this);
		var type = el.attr("type");
		if(type == "text"){
			el.val("");
		}
		else if (type == "date"){
			el.val("");
		} else if (type == "radio") {
			el.val('<null>');
		} else if (type == "checkbox") {
			el.removeAttr('checked');
		}
	})
			
	$("form[name="+name+"] select").each(function(){
		var el = $(this);
		el.val('<null>');
	});
	
	$geplanesUtil.limpaUnidadeGerencial();
	$geplanesUtil.limpaSubordinacao();
	$geplanesUtil.limpaUnidadeGerencialAnomalia();
	$geplanesUtil.limpaUnidadeGerencialAcaoPreventiva();
	$geplanesUtil.limpaUnidadeGerencialAuditoriaInterna();
}

GeplanesUtil.prototype.limpaUnidadeGerencial = function(){
	$('input[name=unidadeGerencial]').val('<null>');
	$('input[name=unidadeGerencial_label]').val('');
	$("#botaoEscolherunidadeGerencial").innerHTML = 'Escolher';
}

GeplanesUtil.prototype.limpaSubordinacao = function(){
	$('input[name=subordinacao]').val('<null>');
	$('input[name=subordinacao_label]').val('');
	$("#botaoEscolhersubordinacao").innerHTML = 'Escolher';
}

GeplanesUtil.prototype.limpaUnidadeGerencialAnomalia = function(){
	$('input[name=ugOrigem]').val('<null>');
	$('input[name=ugOrigem_label]').val('');
	$("#botaoEscolherugOrigem").innerHTML = 'Escolher';
	
	$('input[name=ugResponsavel]').val('<null>');
	$('input[name=ugResponsavel_label]').val('');
	$("#botaoEscolherugResponsavel").innerHTML = 'Escolher';
}

GeplanesUtil.prototype.limpaUnidadeGerencialAcaoPreventiva = function(){
	$('input[name=ugRegistro]').val('<null>');
	$('input[name=ugRegistro_label]').val('');
	$("#botaoEscolherugRegistro").innerHTML = 'Escolher';
}

GeplanesUtil.prototype.limpaUnidadeGerencialAuditoriaInterna = function(){
	$('input[name=ugRegistro]').val('');
	
	$('input[name=ugResponsavel]').val('<null>');
	$('input[name=ugResponsavel_label]').val('');
	$("#botaoEscolherugResponsavel").innerHTML = 'Escolher';
}
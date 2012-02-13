<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="n" uri="neo"%>
<%@ taglib prefix="t" uri="template"%>
<%@ taglib prefix="f" tagdir="/WEB-INF/tags/"%>


<script language="JavaScript" src="${app}/tiny_mce/tiny_mce.js"></script>

<t:tela titulo="Matriz de Iniciativas x FCS">
	<input type="hidden" name="reload" value="" id="idReload">
	<t:janelaFiltro>
		<input type="hidden" name="matrizFCS.id" value="" id="idMatriz">
		<t:propertyConfig showLabel="true" renderAs="double">
			<n:group columns="2" showBorder="false" >
				<n:comboReloadGroup useAjax="true">
					<t:property name="planoGestao" mode="input" onchange="matrizFcs.filtro.limparCampos(1);matrizFcs.filtro.liberarTela(event);"/>
					<n:output styleClass="desc11" value="Unidade Gerencial" />
					<n:panel>
						<f:unidadeGerencialInput name="unidadeGerencial" onchange="matrizFcs.filtro.recarregarTela()" estiloclasse="required"/>
					</n:panel>
					<t:property name="perspectivaMapaEstrategico" mode="input" style="width:500px;" itens="perspectivaMapaEstrategicoService.findByUnidadeGerencialThroughMapaEstrategico(unidadeGerencial,true)" onchange="carregaObjetivosEstrategicos(); matrizFcs.filtro.limparCampos(3); matrizFcs.filtro.liberarTela(event)"/>
					<t:property name="objetivoMapaEstrategico" mode="input" style="width:500px;"  itens="objetivoMapaEstrategicoService.findByUnidadeGerencialPerspectivaThroughMapaEstrategico(unidadeGerencial,perspectivaMapaEstrategico)" onchange="matrizFcs.filtro.limparCampos(4);matrizFcs.filtro.liberarTela(event)"/>
				</n:comboReloadGroup>
				<t:property itens="${fatorAvaliacaoItens}" name="fatorAvaliacao" id="fatorAvaliacao" mode="input" onchange="matrizFcs.table.loadItemFatorAvaliacao(this);matrizFcs.filtro.liberarTela(event)"/>
			</n:group>
		</t:propertyConfig>
	
		<c:if test="${!SEMPERMISSAO}">
			<div id="contMatriz" style="display: none;">
				<div class="fd_tela_titulo" style="margin-top:20px;">Matriz</div>
				<c:if test="${empty HIDEBOTAOSALVAR}">	
					<n:panelGrid columns="2">
						<n:output styleClass="desc11" value="Novo Fator crítico de sucesso"/>
						<n:panel>
							<input type="text" style="width:400px;" id="txt_novofcs" name="txt_novofcs" class="required"/>
							&nbsp;<button class="botao_normal" style="width: 80px;" onclick="matrizFcs.table.addColumn()" type="button">Criar</button>
						</n:panel>
					
						<n:output styleClass="desc11" value="Nova iniciativa"/>
						<n:panel>
							<input type="text" style="width:400px;" id="txt_novainiciativa" name="txt_novainiciativa" class="required"/>
							&nbsp;<button class="botao_normal" style="width: 80px;" onclick="matrizFcs.table.addLine()" type="button">Criar</button>
						</n:panel>	
					</n:panelGrid>
				</c:if>
				
				<div class="containerTabelaFCS" style="width:970px; overflow: auto;">
					<table id="tabelaFCS" class="fd_tabela1" width="100%" cellspacing="1">
						<tr class="txt_tit" id="cabecalho">
							<td>Prioritária?</td>
							<td align="center"><img src="${app}/images/img_coluna_iniciativas.png" /></td>
						</tr>
					</table>
				</div>
				<div align="right">
					<n:hasAuthorization url="/sgm/report/MatrizFCS">
						<input type="button"  class="botao_normal" onclick="javascript:imprimirMatrizFCS();" value="imprimir">&nbsp;
					</n:hasAuthorization>
					<c:if test="${empty HIDEBOTAOSALVAR}">
						<input type="submit"  class="botao_normal" onclick="javascript:if(confirm('Tem certeza que deseja excluir toda a matriz?')){matrizFcs.filtro.excluir();}" value="Excluir matriz">&nbsp;
						<input type="submit"  class="botao_normal" onclick="javascript:matrizFcs.filtro.salvar();" value="salvar">&nbsp;
					</c:if>
				</div>
			</div>
		</c:if>
	</t:janelaFiltro>
</t:tela>


<script type="text/javascript">
	var app = "${app}";
				
	function demo(){
		$("#txt_novofcs").val("lala2");
		matrizFcs.table.addColumn();
		$("#txt_novofcs").val("lala3");
		matrizFcs.table.addColumn();
		/*$("#txt_novofcs").val("lala4");
		matrizFcs.table.addColumn();
		$("#txt_novofcs").val("lala5");
		matrizFcs.table.addColumn();
		$("#txt_novofcs").val("lala6");
		matrizFcs.table.addColumn();*/
		
		
		$("#txt_novainiciativa").val("asdasdas 1");
		matrizFcs.table.addLine();
		$("#txt_novainiciativa").val("asdasdas 2");
		matrizFcs.table.addLine();
		/*$("#txt_novainiciativa").val("asdasdas 3");
		matrizFcs.table.addLine();
		$("#txt_novainiciativa").val("asdasdas 4");
		matrizFcs.table.addLine();
		$("#txt_novainiciativa").val("asdasdas 5");
		matrizFcs.table.addLine();*/
	}
		
	if (!window['matrizFcs']) 
		window['matrizFcs'] = {};
	
	(function ($,tabela) {
	
		if (this['filtro'] === undefined) {
			this.filtro = {
				planoGestao : null,
				unidadeGerencial : null,
				unidadeGerencial_Label : null,
				objetivoMapaEstrategico : null,
				fatorAvaliacao : null,
				restoreInProcess : false,
				limparCampos : function (from){
					if(from <=0) {
						$("select[name=planoGestao]").get(0).selectedIndex = 0;
					}
					if(from <=1){
						this.limpaUnidadeGerencial();
					} 
					if(from <=2) {
						$("select[name=perspectivaMapaEstrategico]").get(0).selectedIndex = 0;
					} 
					if(from <=3) {
						$("select[name=objetivoMapaEstrategico]").get(0).selectedIndex = 0;
					} 
					if(from <=4) {
						$("select[name=fatorAvaliacao]").get(0).selectedIndex = 0;
					}
					var unVal = form['unidadeGerencial'].value;
					if(from <= 2 && unVal == "<null>"){
						this.hideFields();
					} else {
						this.showFields();
					}
				},
				registerValues : function (){
					this.planoGestao = $("select[name=planoGestao]").val();
					this.objetivoMapaEstrategico = $("select[name=objetivoMapaEstrategico]").val();
					this.fatorAvaliacao = $("select[name=fatorAvaliacao]").val();
					this.unidadeGerencial = form['unidadeGerencial'].value;
					this.unidadeGerencial_Label = form['unidadeGerencial_label'].value;
				},
				hideFields : function (){
					$("select[name=perspectivaMapaEstrategico]").parent().parent().hide();
					$("select[name=objetivoMapaEstrategico]").parent().parent().hide();
					$("select[name=fatorAvaliacao]").parent().parent().hide();
				},
				showFields : function (){
					$("select[name=perspectivaMapaEstrategico]").parent().parent().show();
					$("select[name=objetivoMapaEstrategico]").parent().parent().show();
					$("select[name=fatorAvaliacao]").parent().parent().show();
				},
				limpaUnidadeGerencial : function() {
					form['unidadeGerencial'].value = '<null>';
					form['unidadeGerencial_label'].value = '';
					document.getElementById('botaoEscolherunidadeGerencial').innerHTML = 'Escolher';		
				},
				salvar : function (){
					var colunas = matrizFcs.table.colunas - 1;
					var linhas = matrizFcs.table.linhas;
					var totalElementosObrigatorios = colunas * linhas;
					
					var totalElementosSelecionados = tabela.find(":radio:checked").size();
					if(totalElementosSelecionados < totalElementosObrigatorios){
						alert("É obrigatória a avaliação de impacto para todas as iniciativas x FCS.");
						return;	
					}
					
					$("#fatorAvaliacao").removeAttr("disabled");
					form.ACAO.value ='salvar';
					form.action = app+'/sgm/process/MatrizFCS';
					form.validate = 'false';
					submitForm();
				},
				
				excluir : function (){
					form.ACAO.value ='excluir';
					form.action = app+'/sgm/process/MatrizFCS';
					form.validate = 'false';
					submitForm();
				},				
				
				recarregarTela : function(){
					form.validate = 'false'; 
					form.suppressErrors.value = 'true';
					form.ACAO.value = 'executar';
					form.suppressValidation.value = 'true';
					document.getElementById('idReload').value = 'true';
					submitForm();
				},
				 
				liberarTela: function (event) {
					var ignore = false;
					if(!event)
						ignore = true;
						
					$("#messageBlock").hide();
					var planoGestao = $("select[name=planoGestao]").val();
					var unidadeGerencial = $("input[name=unidadeGerencial]").val();
					var objetivoMapaEstrategico = $("select[name=objetivoMapaEstrategico]").val();
					var fatorAvaliacao = $("select[name=fatorAvaliacao]").val();
					
					var isNull = true;
					
					if(planoGestao && planoGestao != "<null>" && unidadeGerencial && unidadeGerencial != "<null>" && objetivoMapaEstrategico && objetivoMapaEstrategico != "<null>"){
						isNull = false;
					}
					
					if(matrizFcs.table.matrizLoaded){
						var somenteLeitura = ${!empty HIDEBOTAOSALVAR && HIDEBOTAOSALVAR};
						if(somenteLeitura || (!ignore && confirm("Se for feita a alteração deste valor, os dados não salvos serão perdidos. Deseja continuar mesmo assim?"))){
							
							$("#tabelaFCS tr:gt(0)").remove();
							$("#tabelaFCS td:gt(1)").remove();
							matrizFcs.table.colunas = 1;
							matrizFcs.table.linhas = 0;
							matrizFcs.table.validadeFator();
							$("#idMatriz").val("");
							matrizFcs.table.matrizLoaded = false;
							//return;
						} else {
							/*Restore*/
							this.restoreInProcess = true;
							$("select[name=planoGestao]").val(this.planoGestao);
							$("select[name=objetivoMapaEstrategico]").val(this.objetivoMapaEstrategico);
							$("select[name=fatorAvaliacao]").val(this.fatorAvaliacao);
							
							form['unidadeGerencial'].value = this.unidadeGerencial;
							form['unidadeGerencial_label'].value = this.unidadeGerencial_Label;

							document.getElementById('botaoEscolherunidadeGerencial').innerHTML = 'Limpar';
					 		$("#unidadeGerencial_label").unautocomplete();
					 		$("#unidadeGerencial_label").attr("readOnly", "true");	
							
							matrizFcs.filtro.showFields();
							this.restoreInProcess = false;
							return false;
						}
					}
					
					this.registerValues();
					
					if(!isNull){
						$.getJSON(app+"/sgm/process/MatrizFCS?ACAO=checkMatriz",{planoGestao:planoGestao,unidadeGerencial:unidadeGerencial,objetivoMapaEstrategico:objetivoMapaEstrategico,fatorAvaliacao:fatorAvaliacao},function(data){
							if(data.status == "error"){
								if(fatorAvaliacao && fatorAvaliacao != "<null>" ){
									$("#contMatriz").show();
									$("#idMatriz").val("");			
									matrizFcs.table.matrizLoaded = true;
								} else {
									matrizFcs.table.matrizLoaded = false;
									$('#contMatriz').hide()
								}
							} else {
								var fator = $("select[name=fatorAvaliacao]");
								fator.val(data.fatorAvaliacao);
								$("#idMatriz").val(data.id);
								
								matrizFcs.table.loadItemFatorAvaliacao(fator.get(0),true);
								//matrizFcs.table.carregarTabela();
								//$("#contMatriz").show();
								//vai carregar a tabela
								matrizFcs.table.matrizLoaded = true;
							}
							
							//objTabela.listaItemFatorAvaliacao = data.lista;		
						});
					} else{
						if($("select[name='fatorAvaliacao']").val() != "<null>")
							$("#contMatriz").show();
						else
							 $("#contMatriz").hide();
					}
					return true;	
				}
			}
		}
		
		if (this['table'] === undefined) {
			this.table = {
				cabecalho : null,
				colunas: 1,
				linhas: 0,
				matrizLoaded: false,
				tablePrefix: "matrizFCS.listaMatrizFcsIniciativa",
				tableHeaderPrefix: "matrizFCS.listaMatrizFcsFator",
				checkboxPrefix: "item",
				checkboxSuffix: "sufixo",
				d: document,
				listaItemFatorAvaliacao: null,
				loadItemFatorAvaliacao: function(elemento,toShow){
					var valor = $(elemento).val();
					matrizFcs.filtro.fatorAvaliacao = valor;
					var objTabela = this;
					$.getJSON(app+"/sgm/process/MatrizFCS?ACAO=findItemFatorAvaliacao&fatorAvaliacao="+valor,{},function(data){
						objTabela.listaItemFatorAvaliacao = data.lista;
						
						if( toShow ){
							matrizFcs.table.carregarTabela();
							$("#contMatriz").show();
						}		
					});
				},
				carregarTabela: function (){
					var id = $("#idMatriz").val();
					var objName = $("#idMatriz").attr("name");
					var pai = this;
					$.getJSON(app+"/sgm/process/MatrizFCS?ACAO=getInfosMatriz&"+objName+"="+id,{},function(data){
						var listaColuna = data.listaMatrizFcsFator;
						for(var i = 0; i < listaColuna.length ; i++){
							pai.addColumn(listaColuna[i]);							
						}		
						
						var listaLinhas = data.listaMatrizFCSIniciativa;
						for(var i = 0; i < listaLinhas.length ; i++){
							pai.addLine(listaLinhas[i]);							
						}
					});
				},
				addColumn : function (value){
					var text = null;
					var id = null;
					
					if(value) {
						text = value.descFator;
						id = value.id;
					}
					else						
						text = $("#txt_novofcs").val();
						
					if(text === undefined || text == null || text == ""){
						alert("O campo nova iniciativa é obrigatório");
						$("#txt_novofcs").focus();
						return;
					}
			
					$("#txt_novofcs").focus();
					$("#txt_novofcs").val("");
									
					//criou o cabeçalho
					var td = this.createTd(" "+text,{coluna:this.colunas});					
					
					var input_id = this.createHidden(this.tableHeaderPrefix + "[" + (this.colunas - 1) + "].id"); 
					//this.d.createElement('input');
					//input_id.setAttribute("type","hidden");
					//input_id.setAttribute("name",this.tableHeaderPrefix + "[" + (this.colunas - 1) + "].id");
					input_id.setAttribute("class","idvalorheader");
					if (value) {
						input_id.setAttribute("value",value.id);
					}					
					
					//var input_id = $("<input type='hidden'>").attr("name",this.tableHeaderPrefix + "[" + (this.colunas - 1) + "].id").attr("class","idvalorheader");
					// PEDRO if(id != null) input_id.setAttribute("value",id);
					
					//var input_desc = $("<input type='hidden'>").attr("name",this.tableHeaderPrefix + "[" + (this.colunas - 1) + "].descFator").attr("class","valor").val(text);
					var input_desc = this.createHidden(this.tableHeaderPrefix + "[" + (this.colunas - 1) + "].descFator"); 
					//this.d.createElement('input');
					//input_desc.setAttribute("type","hidden");
					//input_desc.setAttribute("name",this.tableHeaderPrefix + "[" + (this.colunas - 1) + "].descFator");
					input_desc.setAttribute("class","valor");
					input_desc.setAttribute("value",text);
										
					td.prepend(input_id);
					td.prepend(input_desc);
					
					<c:if test="${empty HIDEBOTAOSALVAR}">
						var imagem = this.buildImgAcao("Coluna");
						td.prepend(imagem);
					</c:if>
					
					this.getCabecalho().append(td);
					//adicionar uma td para cada tr criada
					var col = this.colunas;
					var pai = this;
					var radio = null;
					var input_id = null; 
					tabela.find("tr:gt(0)").each(function(){
						
						var tr = $(this);
						var linha = parseInt(tr.attr("linha"));
						
						input_id = pai.createHidden(pai.tablePrefix + "[" + (linha - 1) + "].listaMatrizFcsIniciativaFator["+(col-1)+"].id");
						//pai.d.createElement('input');
						//input_id.setAttribute("type","hidden");
						//input_id.setAttribute("name",pai.tablePrefix + "[" + (linha - 1) + "].listaMatrizFcsIniciativaFator["+(col-1)+"].id");
						input_id.setAttribute("class","idvalor");
							
						//input_id = $("<input type='hidden'>").attr("name",pai.tablePrefix + "[" + (linha - 1) + "].listaMatrizFcsIniciativaFator["+(col-1)+"].id").attr("class","idvalor");
						radio = pai.buildRadio(pai.tablePrefix + "[" + (linha - 1) + "].listaMatrizFcsIniciativaFator["+(col-1)+"].itemFatorAvaliacao");

						td = pai.createTd("",{coluna:col});
						
						td.append(input_id);
						td.append(radio);
						tr.append(td);						
					});
					this.colunas ++;
				},
				
				validadeFator : function (){
					if(this.linhas == 0){
						$("#fatorAvaliacao").removeAttr("disabled");	
					} else {
						$("#fatorAvaliacao").attr("disabled","disabled");
					}
				},
				
				createHidden : function (name){
					var input_id = null;
					try{			
						input_id = this.d.createElement('<input type="hidden" name="'+name+'">');
					} catch (err){
						input_id = this.d.createElement('input');
						input_id.setAttribute("type","hidden");
						input_id.name = name;
					}
					return input_id;
				},
				
				createCheck : function (name){
					var input_id = null;
					try{			
						input_id = this.d.createElement('<input type="checkbox" name="'+name+'" style="border:none">');
						<c:if test="${HIDEBOTAOSALVAR}">
							input_id.setAttribute("disabled","true");
						</c:if>						
					} catch (err){
						input_id = this.d.createElement('input');
						input_id.setAttribute("type","checkbox");
						input_id.setAttribute("style","border:none");
						input_id.name = name;
						<c:if test="${HIDEBOTAOSALVAR}">
							input_id.setAttribute("disabled","true");
						</c:if>
					}
					return input_id;
				},
				
				addLine : function (value){
					var text = null;
					var id = null;
					var lista = null;
					
					if(value) {
						text = value.descIniciativa;
						id = value.id;
						lista = value.listaMatrizFcsIniciativaFator;
					}
					else						
						text = $("#txt_novainiciativa").val();
				
					if(text === undefined || text == null || text == ""){
						alert("O campo novo Fator crítico de sucesso é obrigatório");
						$("#txt_novainiciativa").focus();
						return;
					}			
					$("#txt_novainiciativa").val("");
					$("#txt_novainiciativa").focus();
					
					//criou o cabeçalho
					this.linhas++;
					var classe = ((this.linhas % 2) ? 'txt_l1' : 'txt_l2');					
					var tr = this.createTr({linha:this.linhas,'class':classe});
					
					var input_id = this.createHidden(this.tablePrefix + "[" + (this.linhas - 1) + "].id");
					input_id.setAttribute("class","idvalor");
					if (value) {
						input_id.setAttribute("value",value.id);
					}					
					
					//var input_id = $("<input type='hidden'>").attr("name",this.tablePrefix + "[" + (this.linhas - 1) + "].id").attr("class","idvalor");
					// PEDRO if(value)
					// PEDRO	input_id.setAttribute("value",value.id);
					
					var input_desc = this.createHidden(this.tablePrefix + "[" + (this.linhas - 1) + "].descIniciativa"); 
					//this.d.createElement('input');
					//input_desc.setAttribute("type","hidden");
					//input_desc.setAttribute("name",this.tablePrefix + "[" + (this.linhas - 1) + "].descIniciativa");
					input_desc.setAttribute("class","valor");
					input_desc.setAttribute("value",text);	
					//var input_desc = $("<input type='hidden'>").attr("name",this.tablePrefix + "[" + (this.linhas - 1) + "].descIniciativa").attr("class","valor").val(text);
					//var check = $("<input type='checkbox'>").attr("name",this.tablePrefix + "[" + (this.linhas - 1) + "].prioritaria").attr("class","prioritariavalor");
					var check = this.createCheck(this.tablePrefix + "[" + (this.linhas - 1) + "].prioritaria");
					//this.d.createElement('input');
					//check.setAttribute("type","checkbox");
					//check.setAttribute("name",this.tablePrefix + "[" + (this.linhas - 1) + "].prioritaria");
					check.setAttribute("class","prioritariavalor");	
					
					td = this.createTd("",{coluna:0});
					td.append(check);
					
					if(value && value.prioritaria) {
						check.setAttribute("checked","checked");
					}
					
					tr.append(td);
									
					var td = this.createTd(" "+text,{coluna:0});
					td.prepend(input_id);
					td.prepend(input_desc);
					
					<c:if test="${empty HIDEBOTAOSALVAR}">
						var imagem = this.buildImgAcao("Linha");
						td.prepend(imagem);
					</c:if>
					
					tr.append(td);
					

					var radio = null;
					var pai = this;
					var input_id = null;
					var itemValor = null;
					var nameInput = null;
					for (var i = 1; i < this.colunas ; i++){
						if(lista)
							itemValor=lista[i-1];
							
							
						radio = pai.buildRadio(pai.tablePrefix + "[" + (pai.linhas - 1) + "].listaMatrizFcsIniciativaFator["+(i-1)+"].itemFatorAvaliacao",itemValor);
						
						input_id = this.createHidden(this.tablePrefix + "[" + (pai.linhas - 1) + "].listaMatrizFcsIniciativaFator["+(i-1)+"].id");
						input_id.setAttribute("class","idvalor");
						if (lista) {
							input_id.setAttribute("value",itemValor.id);
						}						
						
						//input_id = $("<input type='hidden'>").attr("name",this.tablePrefix + "[" + (pai.linhas - 1) + "].listaMatrizFcsIniciativaFator["+(i-1)+"].id").attr("class","idvalor");
						// PEDRO if(lista)
						// PEDRO	input_id.setAttribute("value",itemValor.id);
							
						td = this.createTd("",{coluna:i});
						td.append(input_id);
						td.append(radio);
						tr.append(td);
					}
					tabela.append(tr);
					
					this.validadeFator();
				},
				
				buildImgAcao : function (tipo) {
					var imagem1 = this.d.createElement("img");
					imagem1.setAttribute("src","../../images/ico_excluir.gif");
					
					var imagem2 = this.d.createElement("img");
					imagem2.setAttribute("src","../../images/ico_editar.gif");
					//var imagem1 = $("<img/>").attr("src","../../images/ico_excluir.gif").attr("onclick",);
					//var imagem2 = $("<img/>").attr("src","../../images/ico_editar.gif").attr("onclick","matrizFcs.table.editar"+tipo+"(this)");
					
					var span = $("<span></span>");
					span.append(imagem1);
					span.append("&nbsp;");
					span.append(imagem2);
					span.append(" - ");
					
					imagem1.onclick = function (){matrizFcs.table["excluir" + tipo](this);};
					imagem2.onclick = function (){matrizFcs.table["editar" + tipo](this);};
					
					return span;	
				},
				
				createRadio: function (name){
					var radio = null;
					try{ //mais uma vez por causa do ie!
						radio = this.d.createElement("<input type='radio' name='"+name+"' style='border:none'/>");
						<c:if test="${HIDEBOTAOSALVAR}">
							radio.setAttribute("disabled","true");
						</c:if>						
					} catch(err) {
						radio = document.createElement('input');
						radio.setAttribute('type','radio');
						radio.setAttribute('style','border:none');
						radio.setAttribute('name',name);
						<c:if test="${HIDEBOTAOSALVAR}">
							radio.setAttribute("disabled","true");
						</c:if>						
					}
					return radio;
				},
				buildRadio : function (name,valor){
					if(this.listaItemFatorAvaliacao != null){
						var container = this.d.createElement("div"); 
						//$("<div></div>");
						container.setAttribute("class","radios");
						var br_el = this.d.createElement("br");
						var radio = null;
						var item = null;
						for (var i = 0; i < this.listaItemFatorAvaliacao.length ; i++){
							item = this.listaItemFatorAvaliacao[i];
							
							radio = this.createRadio(name);
														
							radio.setAttribute("value",item.id);
							
							container.appendChild(radio);
							
							if(valor){
								if(valor.itemFatorAvaliacao == item.id){
									radio.setAttribute("checked","checked");
								}
							}
							
							container.appendChild(this.d.createTextNode(" "+item.descricao));
							container.innerHTML += "<br/>";
						}
						return container;
					}	
					return "";
				},
				
				excluirColuna : function (elemento){
					if(confirm("Confirma a exclusão da coluna?")){
						var colunaObj = $(elemento).parent().parent();
						var coluna = colunaObj.attr("coluna");
						tabela.find("td[coluna="+coluna+"]").remove();
						this.colunas--;
						
						this.rebuildColumns();
					}
				},
				
				excluirLinha : function (elemento){
					if(confirm("Confirma a exclusão da linha?")){
						$(elemento).parent().parent().parent().remove();
						this.linhas -- ;
						this.rebuildLines();
						this.validadeFator();
					}
				},
				
				editarLinha : function (elemento){
					var tr = $(elemento).parent().parent();
					var valor = tr.find(".content").html();
					
					var name = prompt("Alteração da linha", valor);
					tr.find(".content").html(name);
					var pai = this;
					tr.find("input").each(function(){
						var clazz = this.getAttribute("class");
						if(clazz == "valor"){
							pai.changeHiddenValue(this,name);
						}
					});					
				},
				
				editarColuna : function (elemento){
					var tr = $(elemento).parent().parent();
					var valor = tr.find(".content").html();
					
					var name = prompt("Alteração de coluna", valor);
					tr.find(".content").html(name);
					var pai = this;
					tr.find("input").each(function(){
						var clazz = this.getAttribute("class");
						if(clazz == "valor"){
							pai.changeHiddenValue(this,name);
						}
					});					
				},
				
				rebuildColumns : function (){
					var linha = 0; 
					var pai = this;
					
					tabela.find("tr:eq(0)").each(function(){
						var coluna = 1;
						$(this).find("td:gt(1)").each(function(){
							var td = $(this);
							this.setAttribute("coluna",coluna);
							coluna++;
						});
					});
															
					tabela.find("tr:gt(0)").each(function(){
						var coluna = 1;
						$(this).find("td:gt(1)").each(function(){
							var td = $(this);
							this.setAttribute("coluna",coluna);
							//td.attr("coluna",coluna);
							
							
							if($.browser.msie){ //é gambiarra mesmo! Die ie!
								//http://alt-tag.com/blog/archives/2006/02/ie-dom-bugs/
								
								var input_radio_old = td.find(":first").get(0);
								var valueKeep = input_radio_old.getAttribute("value");
								
								var input_id = pai.createHidden(pai.tablePrefix + "[" + (linha) + "].listaMatrizFcsIniciativaFator["+(coluna-1)+"].id");
								input_id.setAttribute("class","idvalor");
								input_id.setAttribute("value",valueKeep);
								
								input_radio_old.parentNode.replaceChild(input_id, input_radio_old);
								
								td.find("div input").each(function(){
									input_radio_old = $(this).get(0);
									var checkKeep = input_radio_old.getAttribute("checked");
									var valueKeep = input_radio_old.getAttribute("value");
									
									input_id = pai.createRadio(pai.tablePrefix + "[" + (linha) + "].listaMatrizFcsIniciativaFator["+(coluna -1)+"].itemFatorAvaliacao");
									
									input_radio_old.parentNode.replaceChild(input_id, input_radio_old);
									
									if(checkKeep)
										input_id.setAttribute("checked","checked");
										
									input_id.setAttribute("value",valueKeep);
									
								});
							} else {
								td.find(".idvalor").attr("name",pai.tablePrefix + "[" + (linha) + "].listaMatrizFcsIniciativaFator["+(coluna-1)+"].id");
								td.find(".radios input").each(function(){
									$(this).attr("name",pai.tablePrefix + "[" + (linha) + "].listaMatrizFcsIniciativaFator["+(coluna-1)+"].itemFatorAvaliacao");
								});
							}
							coluna++;	
						});
						
						linha ++;
					});
					
					
					var cabecalho = this.getCabecalho();
					cabecalho.find("td input").each(function(){
						var el = $(this);
						var clazz = this.getAttribute("class");
						var coluna = parseInt(el.parent().attr("coluna")); 
						
						if(clazz == "valor")	
							pai.changeHiddenName(this,pai.tableHeaderPrefix + "[" + (coluna - 1) + "].descFator");
						else if (clazz == "idvalorheader")			
							pai.changeHiddenName(this,pai.tableHeaderPrefix + "[" + (coluna - 1) + "].id");
						//$(this).attr("name",pai.tableHeaderPrefix + "[" + (coluna - 1) + "].descFator");
					});
					/*
					cabecalho.find(".idvalorheader").each(function(){
						var el = $(this);
						var coluna = parseInt(el.parent().attr("coluna"));
						if($.browser.msie){
							pai.changeHiddenName(this,pai.tableHeaderPrefix + "[" + (coluna - 1) + "].id");			
						} else {
							$(this).attr("name",pai.tableHeaderPrefix + "[" + (coluna - 1) + "].id");
						} 
						
					});*/
				},
				
				changeHiddenName : function (input_radio_old,newName){
					var valueKeep = input_radio_old.getAttribute("value");
					var input_id = this.createHidden(newName);
					input_id.setAttribute("class",input_radio_old.getAttribute("class"));
					input_id.setAttribute("value",valueKeep);
					
					input_radio_old.parentNode.replaceChild(input_id, input_radio_old);
					
					return input_id;
				},
				
				changeHiddenValue : function (input_radio_old,newValue){
					var valueKeep = input_radio_old.getAttribute("name");
					var input_id = this.createHidden(valueKeep);
					input_id.setAttribute("class",input_radio_old.getAttribute("class"));
					input_id.setAttribute("value",newValue);
					
					input_radio_old.parentNode.replaceChild(input_id, input_radio_old);
					
					return input_id;
				},
				
				changeCheckboxName : function (input_radio_old,newName){
					var input_id = this.createCheck(newName);
					var chk_val = input_radio_old.getAttribute("checked");
					var valueKeep = input_radio_old.getAttribute("value");
					input_id.setAttribute("class",input_radio_old.getAttribute("class"));
					input_id.setAttribute("value",valueKeep);
					
					input_radio_old.parentNode.replaceChild(input_id, input_radio_old);
					
					if(chk_val)
						input_id.setAttribute("checked","checked");
					
					return input_id;
				},
				
				
				rebuildLines : function (){
					var linha = 1; 
					var pai = this;
					
					tabela.find("tr:gt(0)").each(function(){
						var tr = $(this);
						tr.attr("linha",linha);
						tr.attr("class",(linha % 2 ? 'txt_l1' : 'txt_l2'));
						if($.browser.msie){ //coisa de sub-browser
							var input_valor = tr.find("input:not(:gt(2))").each(function(){
								var clazz = this.getAttribute("class");
								var name = "";
								if(clazz == "valor")
									name = pai.tablePrefix + "[" + (linha - 1) + "].descIniciativa";	
								else if(clazz == "prioritariavalor"){
									name = pai.changeCheckboxName(this,pai.tablePrefix + "[" + (linha - 1) + "].prioritaria");
									name = "";
								}else if(clazz == "idvalor")
									name = pai.tablePrefix + "[" + (linha - 1) + "].id";
								
								if(name != ""){
									matrizFcs.table.changeHiddenName(this,name);		
								}

							});
							/*var input_valor = tr.find("input:first").get(0);
							pai.changeHiddenName(input_valor,pai.tablePrefix + "[" + (linha - 1) + "].descIniciativa");
							
							input_valor = tr.find("input:eq(1)").get(0);
							pai.changeHiddenName(input_valor,pai.tablePrefix + "[" + (linha - 1) + "].prioritaria");
							
							input_valor = tr.find("input:eq(2)").get(0);
							pai.changeHiddenName(input_valor,pai.tablePrefix + "[" + (linha - 1) + "].id");*/
						} else {
							tr.find(".valor").attr("name",pai.tablePrefix + "[" + (linha - 1) + "].descIniciativa");
							tr.find(".prioritariavalor").attr("name",pai.tablePrefix + "[" + (linha - 1) + "].prioritaria");
							tr.find(".idvalor").attr("name",pai.tablePrefix + "[" + (linha - 1) + "].id");
						}

						
						var col = 0;						
						tr.find("td:gt(1)").each(function(){
							var td = $(this);
							if($.browser.msie){ //é gambiarra mesmo! Die ie!
								//http://alt-tag.com/blog/archives/2006/02/ie-dom-bugs/
								
								var input_radio_old = td.find(":first").get(0);
								var valueKeep = input_radio_old.getAttribute("value");
								
								var input_id = pai.createHidden(pai.tablePrefix + "[" + (linha - 1) + "].listaMatrizFcsIniciativaFator["+(col)+"].id");
								input_id.setAttribute("class","idvalor");
								input_id.setAttribute("value",valueKeep);
								
								input_radio_old.parentNode.replaceChild(input_id, input_radio_old);
								
								td.find("div input").each(function(){
									input_radio_old = $(this).get(0);
									var checkKeep = input_radio_old.getAttribute("checked");
									var valueKeep = input_radio_old.getAttribute("value");
									
									input_id = pai.createRadio(pai.tablePrefix + "[" + (linha - 1) + "].listaMatrizFcsIniciativaFator["+(col)+"].itemFatorAvaliacao");
									
									input_radio_old.parentNode.replaceChild(input_id, input_radio_old);
									
									if(checkKeep)
										input_id.setAttribute("checked","checked");
										
									input_id.setAttribute("value",valueKeep);
									
								});
							} else {
								td.find(":first").attr("name",pai.tablePrefix + "[" + (linha - 1) + "].listaMatrizFcsIniciativaFator["+(col)+"].id");
								td.find(".radios input").each(function(){
									$(this).attr("name",pai.tablePrefix + "[" + (linha - 1) + "].listaMatrizFcsIniciativaFator["+(col)+"].itemFatorAvaliacao");	
								});
							}
							col++;
						});
						
						linha++;							
					});
				},
				
				getCabecalho : function (){
					if(this.cabecalho == null)
						this.cabecalho = tabela.find("#cabecalho"); 
					
					return this.cabecalho;
				},
				
				createTr : function (parametros){
					var tr = $('<tr></tr>')
					tr = this.buildParametros(tr,parametros);
					return tr;
				},
				
				createTd : function (content,parametros){
					var td = $('<td></td>')
					if(content != null && content != ""){
						var el = $("<span></span>");
						el.html(content);
						el.attr("class","content");
												
						td.append(el);
					}
					td = this.buildParametros(td,parametros);
					//td.html("teste");
					return td;
				},
				
				buildParametros : function(elemento,parametros){
					if(parametros) {
						for (parametro in parametros){
							elemento.attr(parametro,parametros[parametro]);
						}
					}
					return elemento;
				}
				
			}
		}	
	}).call(matrizFcs,jQuery,$("#tabelaFCS"));
	
	function carregaObjetivosEstrategicos() {
		setTimeout('form[\'objetivoMapaEstrategico\'].loadItens()',1);
	}	
	
	function imprimirMatrizFCS(){
		form['ACAO'].value = "gerar";
		form.action = "${ctx}/sgm/report/MatrizFCS";
		form.submit(); 
	}
	
	$(document).ready(function(){
		<c:if test="${empty vindoDaAcaoSalvar}">
			matrizFcs.filtro.hideFields();
			<c:if test="${!SEMPERMISSAO}">
				matrizFcs.filtro.limparCampos(2);
			</c:if>			
		</c:if>
		<c:if test="${vindoDaAcaoSalvar}">	
			matrizFcs.filtro.limparCampos(2);
		</c:if>
	});
	
	function escolherUnidadeGerencialunidadeGerencial(){
 		if(document.getElementById('botaoEscolherunidadeGerencial').innerHTML != 'Limpar'){
 			if (form['planoGestao'].value != '<null>') {
	 			window.open('${pageContext.request.contextPath}/util/UnidadeGerencialTreeView?propriedade=unidadeGerencial&unidadeGerencial='+form['unidadeGerencial'].value+'&planoGestao='+form['planoGestao'].value, 'ugtv', 'directories=no,height=450,width=400,top=50,left=200,location=no,menubar=no,status=no,toolbar=no,resizable=yes');
	 		}
	 		else {
	 			alert('Favor selecionar o ano da gestão.');
	 		}					 		
 		} else {
 			var somenteLeitura = ${!empty HIDEBOTAOSALVAR && HIDEBOTAOSALVAR};
 			if (somenteLeitura || !matrizFcs.table.matrizLoaded || confirm("Se for feita a alteração deste valor, os dados não salvos serão perdidos. Deseja continuar mesmo assim?") ){
 				document.location.href = document.location.href//hack para erro do limpar 
	 			form['unidadeGerencial'].value = '<null>';
	 			form['unidadeGerencial_label'].value = '';
	 			document.getElementById('botaoEscolherunidadeGerencial').innerHTML = 'Escolher';
 				//$("select[name='objetivoMapaEstrategico']").html('');
		 		//$("select[name='fatorAvaliacao']").html('');
	 			matrizFcs.filtro.limparCampos(2); $("#contMatriz").hide(); 
		 		
		 		$("#unidadeGerencial_label").removeAttr("readOnly");
		 		
		 		$("#unidadeGerencial_label").autocomplete('${pageContext.request.contextPath}/util/UnidadeGerencialAutoComplete?planoGestao='+form['planoGestao'].value, {
					width: 300,
					multiple: true,
					matchContains: true,
					formatItem: formatItem,
					formatResult: formatResult,
					max: 100
				}).result(function(event, data, formatted) {
					data[0] = stripJavascriptTags(data[0]);
					formatted = stripJavascriptTags(formatted);
					$("#unidadeGerencial_label").val(formatted);
					$("#unidadeGerencial_value").val('br.com.linkcom.sgm.beans.UnidadeGerencial[id=' + data[1] + ']');
					document.getElementById('botaoEscolherunidadeGerencial').innerHTML = 'Limpar';
			 			form['unidadeGerencial'].onchange();
			 		$("#unidadeGerencial_label").unautocomplete();
			 		$("#unidadeGerencial_label").attr("readOnly", "true");
			 		//limpar campos
				});
			}			
 		}
 	}
</script>
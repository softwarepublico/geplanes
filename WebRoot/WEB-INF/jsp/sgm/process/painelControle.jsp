<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="n" uri="neo"%>
<%@ taglib prefix="geplanes" uri="geplanes"%>
<%@ taglib prefix="t" uri="template"%>
<%@ taglib prefix="code" uri="code"%>
<%@ taglib prefix="ajax" uri="ajax"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="f" tagdir="/WEB-INF/tags"%>

<code:class>
 <code:main>
   <t:tela titulo="Painel de controle" mainDivStyle="">
     <input type="hidden" name="reload" value="" id="idReload">
	 <t:janelaFiltro>
	   <t:tabelaFiltro showSubmit="false" setColumns="2" columnStyles="_,_,text-align:left,text-align:left; width:700px">
		<t:property name="planoGestao" style="width: 100px;"/>
		 <n:output styleClass="desc11" style="text-align: right;" value="Unidade Gerencial"/>
		  <n:panel><f:unidadeGerencialInput name="unidadeGerencial" onchange="reloadTela()" estiloclasse="required"/></n:panel>
			<n:panel>
			 <div style="text-align:left;">
			  <input type="radio" style="border:none" name="apuradosAcumulados" value="apurados" id="radioApurado" checked="checked" onclick="showInfoIdMarcado(); alternarFarois();"/>Valores apurados
			 </div>
			 <div style="text-align:left;">
			  <input type="radio" style="border:none" name="apuradosAcumulados" value="acumulados" id="radioAcumulado" onclick="showInfoIdMarcado();alternarFarois();"/>Valores acumulados
             </div>
            </n:panel>
            <n:panel>
			 <n:submit class="botao_normal" type="button" action="reset">Reset</n:submit>
			 &nbsp;&nbsp;&nbsp;&nbsp;
			 <n:submit class="botao_normal" type="button" action="javascript:imprimirRelatorio()">Imprimir</n:submit>
			 &nbsp;&nbsp;&nbsp;&nbsp;
			 <n:submit class="botao_normal" type="button" action="javascript:popUpLegenda()">Exibir legenda</n:submit>
		    </n:panel>
		 </t:tabelaFiltro>
	  </t:janelaFiltro>
			  <div id="esconderTabela" style="display:visible;position:relative;left:-23px;top:-172px;">
				<table class="tabela_titulo_indicadores" cellpadding="0" cellspacing="0" id="tabela_titulo_indicadores">
					<tr>
					  <td>
						<table cellspacing="1" id="tituloFrequencia" width="456">
						  <tr></tr>
						    <tr>
							 <td class="item4titulo" colspan="6"> </td>
							 <td class="item4titulo" colspan="6"> </td>
							 <td class="item4titulo" colspan="6"> </td>
							 <td class="item4titulo" colspan="6"> </td>
							</tr>
							<tr>
							 <td class="item12titulo" colspan="2">J</td>
							 <td class="item12titulo" colspan="2">F</td>
							 <td class="item12titulo" colspan="2">M</td>
							 <td class="item12titulo" colspan="2">A</td>
							 <td class="item12titulo" colspan="2">M</td>
							 <td class="item12titulo" colspan="2">J</td>
							 <td class="item12titulo" colspan="2">J</td>
							 <td class="item12titulo" colspan="2">A</td>																				
							 <td class="item12titulo" colspan="2">S</td>
							 <td class="item12titulo" colspan="2">O</td>
							 <td class="item12titulo" colspan="2">N</td>
							 <td class="item12titulo" colspan="2">D</td>	
							</tr>					
							<tr>
							 <c:forEach begin="0" end="23" step="1" varStatus="status">
							  <td class="item24titulo"  align="center" id="t24_${status.index}"> </td>									
							   </c:forEach>						
							  </tr>														
						</table>
					   </td>
					  </tr>				
				 </table>
				</div>
				<div class="painel_arvore" style="width: 954px;overflow-x:auto;" id="painel_arvore">
				 <table id="treeTableScript" cellpadding="0" cellspacing="0" class="lookup_arvore_galhos arvore_painel" style="cursor:default;" width="100%">
				  <tr>
				   <td></td><td style="width: 457px;"></td>
				  </tr>
				 </table>
				</div>
				<div class="painel_detalheindicador">
				 <table class="fd_tabela1" id="detalheIndicador" cellspacing="0" cellpadding="1" width="100%">
				  <tr class="txt_tit txt_peq ">
				   <td style="width: 70px">&nbsp;</td><td>&nbsp;</td>
				  </tr>
				  <tr class="txt_l1 txt_peq">
				   <td>% Realizado</td><td>&nbsp;</td>
				  </tr>
				  <tr class="txt_l2 txt_peq" id="linhaTolerancia" style="display:none">
				   <td>% Tolerância</td><td>&nbsp;</td>
				  </tr>
				  <tr class="txt_l1 txt_peq" id="linhaLimiteSuperior" style="display:none">
				   <td>Lim. Sup.</td><td>&nbsp;</td>
				  </tr>
				  <tr class="txt_l2 txt_peq" id="linhaValorReal" style="display:none">
				   <td>Realizado</td><td>&nbsp;</td>
				  </tr>
				  <tr class="txt_l1 txt_peq" id="linhaLimiteInferior" style="display:none">
				   <td>Lim. Inf.</td><td>&nbsp;</td>
				  </tr>
				   </table>
					<div style="font-size: 11px; padding: 3px;">
					 <span id="usuariosFaltantes"></span>
					</div>
				</div>
			</t:tela>
			<div id="divanomalias" style="background-color:#FFFFEE;
                     font-size: 12px; 
                     border:1px solid gray;
                     display:none;
                     width:980px;
                     heigth:200px"></div>	
			<div id="divanexos" style="background-color: #FFFFEE; 
					 font-size: 12px; 
					 border:1px solid gray;
					 display:none;
            		 width:980px;
            		 heigth:200px"></div>
			
			<%--<div id="windowinfo" style="position: absolute; left: 790px;top: 50px;background-color: white; width: 200px;height: 400px; border: 1px solid gray; overflow:auto; font-size: 11px">
			</div>--%>
			<script type="text/javascript">
			
				function popUpLegenda() {
					var width  = 600;
				    var height = 200;	
				    var left   = 0;
				    var top    = 0;
				    
			    	window.open('${ctx}/legendaPainelControle.jsp','Legenda', 'width='+width+', height='+height+', top='+top+', left='+left+', scrollbars=yes, status=no, toolbar=no, location=no, directories=no, menubar=no, resizable=no, fullscreen=no');
				}			
			
				function imprimirRelatorio(){
					if(document.getElementById("unidadeGerencial_label").value != ""){
						form.ACAO.value = 'relatorio';
						submitForm();
					}else{
						alert("O campo unidade gerencial é obrigatório.");
					}	
				}
				var allNodes = new Array();
				
				document.body.onclick = function(){
					esconderAnomalias();
					esconderAnexos();
				};
				
				window.onresize = function(){
					//setWindowinfo();
					autosetSize();
				}
				
				//setWindowinfo();
				autosetSize();				
				function setWindowinfo(){
					var div = document.getElementById('windowinfo');
					var text = '';
					
					text += 'document.body.clientHeight: '+document.body.clientHeight;
					div.innerHTML = text;
				}
				
				function autosetSize(){
					var h = document.body.clientHeight;
					h = h - 328;
					document.getElementById('painel_arvore').style.height = h;
				}
				
				function getNode(id, returnNullIfNotFound){
					for(var i = 0; i < allNodes.length; i++){
						var node = allNodes[i];
						if(node.id == id){
							return node;
						}
					}
					if(!returnNullIfNotFound){
						alert('node nao encontrado '+id);					
					}

				}
				
				var treeTable = installTreeTable('treeTableScript', '${ctx}/images/');
				treeTable.onaddnode = function (node){
					node.onopennode = function(node){
						if(!node.filhosLidos){
							carregarItens(node.id);						
						} else {
							carregarFilhos(node.id);
						}
					};
				};
				treeTable.configRow = function (row){
					row.onmouseover = function (){
						if(isSelected(row.node.id)){
							return;
						}
						row.style.backgroundColor = '#f3f3f3';
					}; 
					row.onmouseout  = function (){
						if(isSelected(row.node.id)){
							return;
						}
						row.style.backgroundColor = '';
					};
				};

				<code:call method="imprimeItens"/>

				
				if(navigator.appName.indexOf('Microsoft Internet Explorer') != -1){
					document.getElementById('tabela_titulo_indicadores').style.left = '502px';
				}
			
				function carregarFilhos(id){
					var stringid = '';
					var pai = getNode(id);
					var children = pai.children;
					for(var i = 0; i < children.length; i++){
						if(!children[i].filhosLidos){
							stringid += children[i].id;
							if(i + 1 < children.length){
								stringid+='&id=';
							}
						}
					}
					if(stringid != ''){
						carregarItens(stringid);					
					}
				}
				
				function getItemSpan(id){
					return document.getElementById('itemspan_'+id);
				}
				
				function isSelected(id){
					return currentSelectedId == id;
				}
				
				function acender(nodeid, divisao, indice){
					var tds = getTitulosTd(divisao, indice);
					for(var i = 0; i < tds.length; i++){
						var td = tds[i];
						td.style.backgroundColor = '#CDCDCD';
					}
					if(isSelected(nodeid)){
						var table = document.getElementById('detalheIndicador');
						var rows = table.rows;
						for(var i = 1; i < rows.length; i++){
							var row = rows[i];
							if(row.cells.length > indice + 1){
								row.cells[indice + 1].style.backgroundColor='#DEDEDE';							
							}

						}
						/*
						var usuariosFaltantes = getNode(nodeid).acompanhamentos[indice].usuariosFaltantes;
						var text = '';
						for(var i = 0; i < usuariosFaltantes.length; i++){
							text += usuariosFaltantes[i];
							if(i + 1 < usuariosFaltantes.length){
								text += ', ';
							}
						}
						document.getElementById('usuariosFaltantes').innerHTML = 'Usuários que não lançaram valores: '+text;
						*/
					}
				}
				
				function apagar(nodeid, divisao, indice){
					var tds = getTitulosTd(divisao, indice);
					for(var i = 0; i < tds.length; i++){
						var td = tds[i];
						td.style.backgroundColor = '';
					}
					
					var table = document.getElementById('detalheIndicador');
					var rows = table.rows;
					for(var i = 1; i < rows.length; i++){
						var row = rows[i];
						if(row.cells.length > indice + 1){
							row.cells[indice + 1].style.backgroundColor='';						
						}

					}
					document.getElementById('usuariosFaltantes').innerHTML = '';
				}
				
				function getTitulosTd(divisao, indice){
					var linha = 0;
					var tds = new Array();

					if(divisao == 2){
						linha = 0;
					} else if (divisao == 4){				
						linha = 1;
						tds = tds.concat(getFilhosTd(linha, indice));
						tds = tds.concat(getPaisTd(linha, indice));					
					} else if (divisao == 12){
						linha = 2;
						tds = tds.concat(getFilhosTd(linha, indice));
						tds = tds.concat(getPaisTd(linha, indice));	
					} else if (divisao == 24){
						linha = 3;
						tds = tds.concat(getPaisTd(linha, indice));	
					}					
					var td1 = document.getElementById('tituloFrequencia').rows[linha].cells[indice];
					
					tds.push(td1);
					return tds;
				}
			
				function getPaisTd(linha, indice){
					var tds = new Array();
					var linhapai = linha - 1;
					if(linhapai == -1 || linhapai == 0){
						return new Array();
					}
					var fator = 0;
					if(linha == 3){
						fator = 2;
					} else if(linha == 2){
						fator = 3;
					} else if(linha == 1){
						fator = 2;
					}
									
					var tdi = Math.floor(indice / fator);

					var td = document.getElementById('tituloFrequencia').rows[linhapai].cells[tdi];
					
					tds.push(td);
					
					tds = tds.concat(getPaisTd(linhapai, tdi));
					
					return tds;
				}
				
				var currentSelectedId = null;
				function showInfo(event, id){
					if(currentSelectedId){
						//currentSelectedSpan.style.border = '';
						getNode(currentSelectedId).row.style.backgroundColor = '';
					}
					if(currentSelectedId != id){
						getNode(id).row.style.backgroundColor = '#FFFFAA';
						//span.style.border = '1px dotted black';
						currentSelectedId = id;
						showInfoInTable(id);			
					} else {
						var table = document.getElementById('detalheIndicador');
						limparTabelaDetalheIndicador(table);
						criarUltimaColuna(table);
						currentSelectedId = null;
					}
		
					event.cancel = true;
					event.cancelBubble = true;
					return false;
				}
				
				function showInfoIdMarcado(){
					if(currentSelectedId != null){
						showInfoInTable(currentSelectedId);
					}

				}
				
				function alternarFarois(){
					var propriedadeAcompanhamentos;
					if(document.getElementById('radioApurado').checked){
						propriedadeAcompanhamentos = 'tabelaAcompanhamentos';
					} else {
						propriedadeAcompanhamentos = 'tabelaAcompanhamentosAcumulados';					
					}
					var rows = treeTable.rows;
					for(var i = 0; i < rows.length; i++){
						var row = rows[i];
						var node = row.node;
						if(node){
							var cell = node.row.cells[1];
							cell.innerHTML = node[propriedadeAcompanhamentos];												
						}
					}
				}
				
				function showInfoInTable(id){
					var table = document.getElementById('detalheIndicador');
	
					var node = getNode(id);
					//modificar entre acompanhamentos e acompanhamentosAcumulados
					
					var propriedadeAcompanhamentos;
					if(document.getElementById('radioApurado').checked){
						propriedadeAcompanhamentos = 'acompanhamentos';
					} else {
						propriedadeAcompanhamentos = 'acompanhamentosAcumulados';					
					}
					var acompanhamentos = node[propriedadeAcompanhamentos];
					
					limparTabelaDetalheIndicador(table);
					
					for(var i = 0; i < acompanhamentos.length; i++){
						var acompanhamento = acompanhamentos[i];
						
						if (i == 0) {
						
							if (acompanhamento.percentualTolerancia == '0') {
								$("#linhaTolerancia").hide();
							}
							else {
								$("#linhaTolerancia").show();
							}
						
							if (acompanhamento.melhor == 'MELHOR_CIMA') {
			 					$("#linhaLimiteSuperior").show();
			 					$("#linhaValorReal").show();
			 					$("#linhaLimiteInferior").hide();			
							}
							else if (acompanhamento.melhor == 'MELHOR_BAIXO') {
			 					$("#linhaLimiteSuperior").hide();
			 					$("#linhaValorReal").show();
			 					$("#linhaLimiteInferior").show();			
							}
							else if (acompanhamento.melhor == 'MELHOR_ENTRE_FAIXAS') {
			 					$("#linhaLimiteSuperior").show();
			 					$("#linhaValorReal").show();
			 					$("#linhaLimiteInferior").show();			
							}
							else {
			 					$("#linhaLimiteSuperior").hide();
			 					$("#linhaValorReal").hide();
			 					$("#linhaLimiteInferior").hide();							
							}						
						}
						
						var row0 = table.rows[0];
						var row1 = table.rows[1];
						var row2 = table.rows[2];
						var row3 = table.rows[3];
						var row4 = table.rows[4];
						var row5 = table.rows[5];																								
						
						var ctitulo = row0.insertCell(i+1);
						var c0 = row1.insertCell(i + 1);
						var c1 = row2.insertCell(i + 1);
						var c2 = row3.insertCell(i + 1);
						var c3 = row4.insertCell(i + 1);
						var c4 = row5.insertCell(i + 1);
						
						if(i == 0 && (acompanhamento.labelSuperior != '' || acompanhamento.labelInferior != '')){
							row3.cells[0].innerHTML = acompanhamento.labelSuperior;
							row5.cells[0].innerHTML = acompanhamento.labelInferior;
						}
						
						ctitulo.innerHTML = node.titulosDetalhe[i];
						
						if(acompanhamento.percentualReal != '')
							c0.innerHTML = acompanhamento.percentualReal;
						else 
							c0.innerHTML = '&nbsp;';
							
						if(acompanhamento.percentualTolerancia != '')							
							c1.innerHTML = acompanhamento.percentualTolerancia;
						else 
							c1.innerHTML = '&nbsp;';
						
						if(acompanhamento.valorLimiteSuperior != '')
							c2.innerHTML = acompanhamento.valorLimiteSuperior;
						else 
							c2.innerHTML = '&nbsp;';
							
						if(acompanhamento.valorReal != '')
							c3.innerHTML = acompanhamento.valorReal;
						else 
							c3.innerHTML = '&nbsp;';
							
						if(acompanhamento.limite)
							c4.innerHTML = acompanhamento.limite;
						else
							c4.innerHTML = '&nbsp;';
					}
				}
				
				function limparTabelaDetalheIndicador(table){
					var rows = table.rows;
					for(var i = 0; i < rows.length; i++){
						var row = rows[i];
						var cells = row.cells;
						while(row.cells.length > 1){
							row.deleteCell(row.cells.length - 1);
						}
					}
				}
				
				function criarUltimaColuna(table){
					var rows = table.rows;
					for(var i = 0; i < rows.length; i++){
						var row = rows[i];
						var cell = row.insertCell(1);
						cell.innerHTML = '&nbsp;';
					}
				}
				
				function getFilhosTd(linha, indice){
					var linhaFilhos = linha + 1;
					var fator = 0;		

					if(linha == 1){
						fator = 3;
					} else if(linha == 2){
						fator = 2;
					}
					if(linha == 3){
						return new Array();
					}
					
					var inicio = indice * fator;
					var fim = inicio + fator;
					
					var tds = new Array();
					for(var i = inicio; i < fim; i++){
						var td1 = document.getElementById('tituloFrequencia').rows[linhaFilhos].cells[i];	
						tds.push(td1);
						
						tds = tds.concat(getFilhosTd(linhaFilhos, i));
					}				
					return tds;
				}
				
				var acompanhamentoSelecionadoAnomalia = null;
				var nodeSelecionadoAnexo = null;
				
				function showAnexos(id, img, event){
					var node = getNode(id);
					var divanexos = document.getElementById('divanexos');
					if(nodeSelecionadoAnexo == node){
						esconderAnexos();
						event.cancelBubble = true;
						return;
					}
					nodeSelecionadoAnexo = node;
					var anexos = node.anexos;
					var text = '';
					for(var i = 0; i < anexos.length; i++){
						text += anexos[i].nome + ' <a href="${ctx}/DOWNLOADFILE/'+anexos[i].arquivo.id+'" title="'+anexos[i].descricao+'">download</a><BR>';
					}
					var posImg = findPos(img);
					
					divanexos.style.left = posImg[0] + 15;
					divanexos.style.top = posImg[1];
					divanexos.style.display = '';
					divanexos.innerHTML = text;
					event.cancelBubble = true;
				}
				
				function esconderAnexos(){
					var divanexos = document.getElementById('divanexos');
					divanexos.style.display = 'none';
					nodeSelecionadoAnexo = null;			
				}
				
				function showAnomalias(id, indice, img, event){
					var acompanhamento = getNode(id).acompanhamentos[indice];
					if(acompanhamento == acompanhamentoSelecionadoAnomalia){
						esconderAnomalias();
						event.cancelBubble = true;
						return;
					}
					acompanhamentoSelecionadoAnomalia = acompanhamento;
					var anomalias = acompanhamento.anomalias;
					var text = '';
					for(var i = 0; i < anomalias.length; i++){
						text +=  anomalias[i].descricao + ' <a href="${ctx}/sgm/crud/Anomalia?ACAO=editar&id='+anomalias[i].id+'">tratar</a><BR> ';
					}
					
					var posImg = findPos(img);
					
					var divanomalias = document.getElementById('divanomalias');
					divanomalias.style.left = posImg[0] - 200;
					divanomalias.style.top = posImg[1];
					divanomalias.style.display = '';
					divanomalias.innerHTML = text;
					event.cancelBubble = true;
				}
				
				function esconderAnomalias(){
					var divanomalias = document.getElementById('divanomalias');
					divanomalias.style.display = 'none';
					acompanhamentoSelecionadoAnomalia = null;					
				}
				
				function findPos(obj) {
					var curleft = curtop = 0;
					if (obj.offsetParent) {
						curleft = obj.offsetLeft;
						curtop = obj.offsetTop;
						while (obj = obj.offsetParent) {
							curleft += obj.offsetLeft;
							curtop += obj.offsetTop;
						}
					}
					return [curleft,curtop];
				}
				
				function reloadTela(){
					form.validate = 'false'; 
					form.suppressErrors.value = 'true';
					form.suppressValidation.value = 'true';
					form.ACAO.value ='reset';
					document.getElementById('idReload').value = 'true';
					submitForm();
				}				
			</script>

			<ajax:call functionName="carregarItens(id)" action="ajaxArvore" parameters="javascript:'id='+id+'&planoGestao='+form['planoGestao'].value"/>
	</code:main>

	<code:method name="imprimeItens">
		<c:forEach items="${itens}" var="item">
			<code:call method="imprimeScriptItem"/>
		</c:forEach>

	</code:method>
		
	<code:method name="imprimeScriptItem">
		<%-- Esse código supõe que exista uma unidade gerencial no escopo --%>

		var node${item.id} = new Node('${item.id}');
		node${item.id}.filhosLidos = false;
		node${item.id}.hasChild = ${item.temFilhos};
		node${item.id}.acompanhamentos = new Array();
		node${item.id}.acompanhamentosAcumulados = new Array();	
		var column1node${item.id} = node${item.id}.newColumn();
		var column2node${item.id} = node${item.id}.newColumn();
		
		column1node${item.id}.style.paddingTop = '1px';
		
		column1node${item.id}.icon = 'ico_pc_${item.icone}.gif';
		
		var ico_clip = '';
		<c:if test="${!empty item.anexos}">
			ico_clip = '&nbsp;&nbsp; <img style="cursor:hand" src="${ctx}/images/ico_clip.gif" onclick=\'showAnexos("${item.id}", this, event)\'>';

			node${item.id}.anexos = new Array();
			<c:forEach items="${item.anexos}" var="anexo">
				anexo = new Object();
				anexo.descricao = '${geplanes:escape(anexo.descricao)}';
				anexo.nome = '${geplanes:escape(anexo.nome)}';				
				anexo.arquivo = new Object();
				anexo.arquivo.id = ${anexo.arquivo.id};
				
				node${item.id}.anexos.push(anexo);
			</c:forEach>
		</c:if>
		
		peso = '';
		<c:if test="${item.mostraPeso}">
			peso = '<b>${item.peso}</b>';
		</c:if>

		column1node${item.id}.innerHTML = '<span style=\'white-space: nowrap;\' onclick=\'return showInfo(event, "${item.id}");\' id="itemspan_${item.id}">${geplanes:escape(item.descricao)} '+peso+ico_clip+'</span>';
		
		<c:if test="${item.mostraFarol}">
			<code:call method="montarTabelaAcompanhamentos"/>
			column2node${item.id}.innerHTML = tabelaAcompanhamentos;		
			node${item.id}.tabelaAcompanhamentos = tabelaAcompanhamentos;
			
			<code:call method="montarTabelaAcompanhamentosAcumulados"/>		
			node${item.id}.tabelaAcompanhamentosAcumulados = tabelaAcompanhamentosAcumulados;
		</c:if>
		<c:if test="${! item.mostraFarol}">
			node${item.id}.tabelaAcompanhamentos = '&nbsp;';
			node${item.id}.tabelaAcompanhamentosAcumulados = '&nbsp;';
		</c:if>		
				
		node${item.id}.titulosDetalhe = new Array();
		<c:forEach items="${item.titulosDetalhe}" var="titulo">
			node${item.id}.titulosDetalhe.push('${titulo}');
		</c:forEach>
		<c:forEach items="${item.acompanhamentos}" var="acompanhamento">
			acompanhamento = new Object();
			acompanhamento.percentualReal = '<fmt:formatNumber pattern="#0.##" value="${acompanhamento.percentualReal}"/>';
			acompanhamento.percentualTolerancia = '<fmt:formatNumber pattern="#0.##" value="${acompanhamento.percentualTolerancia}"/>';
		    acompanhamento.valorLimiteSuperior = '${acompanhamento.valorLimiteSuperiorAsString}';
			acompanhamento.valorReal = '${acompanhamento.valorRealAsString}';
			acompanhamento.limite = '${acompanhamento.valorLimiteInferiorAsString}';			
			acompanhamento.labelSuperior = '';
			acompanhamento.labelInferior = '';			
			acompanhamento.melhor = '${acompanhamento.melhor.name}';
			
			if (acompanhamento.melhor == 'MELHOR_CIMA') {
				acompanhamento.labelSuperior = 'Meta';
				acompanhamento.labelInferior = '';			
			}
			else if (acompanhamento.melhor == 'MELHOR_BAIXO') {
				acompanhamento.labelSuperior = '';
				acompanhamento.labelInferior = 'Meta';			
			}
			else if (acompanhamento.melhor == 'MELHOR_ENTRE_FAIXAS') {
				acompanhamento.labelSuperior = 'Lim. Sup.';
				acompanhamento.labelInferior = 'Lim. Inf.';			
			}
																			
			node${item.id}.acompanhamentos.push(acompanhamento);

			acompanhamento.usuariosFaltantes = new Array();
			<c:forEach items="${acompanhamento.usuariosFaltantes}" var="usuario">
				acompanhamento.usuariosFaltantes.push('${geplanes:escape(usuario.nome)}');
			</c:forEach>
			
			acompanhamento.anomalias = new Array();
			<c:forEach items="${acompanhamento.anomaliasUsuarios}" var="anomalia">
				anomalia = new Object();
				anomalia.id = ${anomalia.id};
				anomalia.descricao = '${geplanes:escape(anomalia.descricao)}';
				
				acompanhamento.anomalias.push(anomalia);
			</c:forEach>
		</c:forEach>
		
		<c:forEach items="${item.acompanhamentosAcumulados}" var="acompanhamento">
			acompanhamento = new Object();
			acompanhamento.percentualReal = '<fmt:formatNumber pattern="#0.##" value="${acompanhamento.percentualReal}"/>';
			acompanhamento.percentualTolerancia = '<fmt:formatNumber pattern="#0.##" value="${acompanhamento.percentualTolerancia}"/>';
			acompanhamento.valorLimiteSuperior = '${acompanhamento.valorLimiteSuperiorAsString}';
			acompanhamento.valorReal = '${acompanhamento.valorRealAsString}';
			acompanhamento.limite = '${acompanhamento.valorLimiteInferiorAsString}';		
			acompanhamento.labelSuperior = '';
			acompanhamento.labelInferior = '';			
			acompanhamento.melhor = '${acompanhamento.melhor.name}';
			
			if (acompanhamento.melhor == 'MELHOR_CIMA') {
				acompanhamento.labelSuperior = 'Meta';
				acompanhamento.labelInferior = '';			
			}
			else if (acompanhamento.melhor == 'MELHOR_BAIXO') {
				acompanhamento.labelSuperior = '';
				acompanhamento.labelInferior = 'Meta';			
			}
			else if (acompanhamento.melhor == 'MELHOR_ENTRE_FAIXAS') {
				acompanhamento.labelSuperior = 'Lim. Sup.';
				acompanhamento.labelInferior = 'Lim. Inf.';			
			}
			
			node${item.id}.acompanhamentosAcumulados.push(acompanhamento);

		</c:forEach>		
		
		<c:if test="${empty item.parent}">
			treeTable.addNode(node${item.id});
		</c:if>
		<c:if test="${!empty item.parent}">
			getNode('${item.parent.id}').filhosLidos = true;
			if(!getNode('${item.id}', true)){
				getNode('${item.parent.id}').addChild(node${item.id});			
			}

		</c:if>	
		

		
		allNodes.push(node${item.id});
		<c:forEach items="${item.filhos}" var="item">
			<code:call method="imprimeScriptItem"/>
		</c:forEach>
	</code:method>

	<code:method name="montarTabelaAcompanhamentos">
		tabelaAcompanhamentos = '<table style="height: 26px;	border-collapse: collapse;" class="tabela_farol_indicador" cellspacing="0" cellpadding="0" onclick="showInfo(event, \'${item.id}\')"><tr>';
		<c:forEach items="${item.acompanhamentos}" var="acompanhamento" varStatus="status">
			onmouseover = 'acender("${item.id}", ${n:size(item.acompanhamentos)}, ${status.index}); this.style.backgroundColor = "#EEEE66"';
			onmouseout = 'apagar("${item.id}", ${n:size(item.acompanhamentos)}, ${status.index}); this.style.backgroundColor = ""';
			iconanomalia = '';
			<c:if test="${n:size(acompanhamento.anomaliasUsuarios) > 0}">
				iconanomalia = '<img title="visualizar anomalias" style="cursor:hand" src="${ctx}/images/ico_anomalia.gif" onClick=\'showAnomalias("${item.id}", ${status.index}, this, event);\'>';
			</c:if>
			tabelaAcompanhamentos += '<td onmouseover=\''+onmouseover+'\' onmouseout=\''+onmouseout+'\' style=\'white-space: nowrap;border:1px solid #DEDEDE; width: ${item.tamanhoAcompanhamento}px; font-size:8px; text-align:center\'><img src="${ctx}/images/bola-${acompanhamento.corFarol}.png">'+iconanomalia+'</td>';
		</c:forEach>
		tabelaAcompanhamentos += '</tr></table>';
	</code:method>
	
	<code:method name="montarTabelaAcompanhamentosAcumulados">
		tabelaAcompanhamentosAcumulados = '<table style="height: 26px;	border-collapse: collapse;" class="tabela_farol_indicador" cellspacing="0" cellpadding="0" onclick="showInfo(event, \'${item.id}\')"><tr>';
		<c:forEach items="${item.acompanhamentosAcumulados}" var="acompanhamento" varStatus="status">
			onmouseover = 'acender("${item.id}", ${n:size(item.acompanhamentos)}, ${status.index}); this.style.backgroundColor = "#BDBDBD"';
			onmouseout = 'apagar("${item.id}", ${n:size(item.acompanhamentos)}, ${status.index}); this.style.backgroundColor = ""';
			iconanomalia = '';
			<c:if test="${n:size(acompanhamento.anomaliasUsuarios) > 0}">
				iconanomalia = '<img title="visualizar anomalias" style="cursor:hand" src="${ctx}/images/ico_anomalia.gif" onclick=\'showAnomalias("${item.id}", ${status.index}, this, event);\'>';
			</c:if>
			tabelaAcompanhamentosAcumulados += '<td onmouseover=\''+onmouseover+'\' onmouseout=\''+onmouseout+'\' style=\'white-space: nowrap;border:1px solid #DEDEDE; width: ${item.tamanhoAcompanhamento}px; font-size:8px; text-align:center\'><img src="${ctx}/images/bola-${acompanhamento.corFarol}.png">'+iconanomalia+'</td>';
		</c:forEach>
		tabelaAcompanhamentosAcumulados += '</tr></table>';
	</code:method>

</code:class>

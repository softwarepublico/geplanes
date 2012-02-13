<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="n" uri="neo"%>
<%@ taglib prefix="t" uri="template"%>

<table class="lookup_janela">
	<TR>
		<TD class="fd_tela_titulo" height="1%">Unidade Gerencial - ${descPlanoGestao}</TD>
	</TR>
	<TR>
		<TD class="fd_tela_corpo">
			<DIV class="lookup_arvore">
				<table id="treeTableScript" class="lookup_arvore_galhos" >
				</table>
			</DIV>
			<script language="javascript">
				var currentSelected = null;
	
				function selecionar(){
					if(currentSelected != null){
						window.opener.form[document.propriedade+'_label'].value = currentSelected.label;
						$(window.opener.form[document.propriedade+'_label']).attr("readOnly", "true")
						window.opener.form[document.propriedade].value = '${classUnidadeGerencial}[id='+currentSelected.id+']';
						window.opener.document.getElementById('botaoEscolher'+document.propriedade).innerHTML = 'Limpar';					
						window.opener.form[document.propriedade].onchange();
						window.close();
					}
				}
				function changeSelectState(node, event){
					if(currentSelected != null){
						document.getElementById('td'+currentSelected.id).style.border = '';				
						document.getElementById('td'+currentSelected.id).style.backgroundColor = '';					
					}
					if(currentSelected == node){
						currentSelected = null;									
					} else {
						select(node, event)
					}
					event.cancelBubble = true;
					event.cancel = true;
				}
				
				function select(node, event){
					document.getElementById('td'+node.id).style.border = '1px dotted black';
					document.getElementById('td'+node.id).style.backgroundColor = '#EFEFEF';
					currentSelected = node;
					event.cancel = true;				
				}
				
				function findNode(id){
					for(var i = 0; i < document.allnodes.length; i++){
						var node = document.allnodes[i];
						if(node.id == id){
							//alert(id);
							return node;
						}
					}
					alert('Nó não encontrado '+id);
				}
			
				var loadingNodes = new Array();
				var inLoad = 0;
				function loadNodesAjax(oldlist){
					inLoad++;
					var list = new Array();
					for(var j = 0; j < oldlist.length; j++){
						var add = true;
						for(var i = 0; i < loadingNodes.length; i++){					
							if(oldlist[j] == loadingNodes[i]){
								add = false;
								break;
							}
						}
						if(add){
							list.push(oldlist[j]);
						}
					}
					loadingNodes = loadingNodes.concat(list);
					
					var texto = '';
					for(var i =0; i < list.length; i++){
						texto += list[i].info+'<BR>';
					}
					//document.getElementById('info').innerHTML = texto;
					var params = '';
					for(var i =0; i < list.length; i++){
						params += 'unidadesGerenciais=${classUnidadeGerencial}[id='+list[i].id+']&';
					}
					if (params != '') {
						params +='ACAO=load';
						sendRequest('${pageContext.request.contextPath}/util/UnidadeGerencialTreeView',params,'POST', loadNodesAjaxCallback);
					}
				}
				
				function loadNodesAjaxCallback(data){
					inLoad--;
					if(inLoad == 0){
						loadingNodes = new Array();
					}
					eval(data);
				}
				
				function loadNodes(node, nivel){
					var nodeList = new Array();
					if(node.hasChild){
						if(node.children.length == 0){
							nodeList.push(node);
						} else {
							loadChildNodes(nodeList, node.children, 3);
						}
					}
					
					return nodeList;
				}
				
				function loadChildNodes(list, nodes, nivel){
					//document.getElementById('info2').innerHTML = document.getElementById('info2').innerHTML + 'loadChild carregar: '+nivel+' niveis   filhos: '+nodes.length+'<BR>';
					if(nivel > 1){
						for(var i = 0; i < nodes.length; i++){
							var node = nodes[i];
							var hc = node.hasChild;
							if(hc){
								if(node.children.length == 0){								
									list.push(node);
								} else {
									loadChildNodes(list, node.children, nivel - 1);
							
								}				
							}
						}
					}
				}
			
				document.allnodes = new Array();
				var treeTable = installTreeTable('treeTableScript', '${ctx}/images/');
				treeTable.onaddnode = function (node){
					document.allnodes.push(node);
					node.onopennode = function(node){
						//alert('abrindo '+node.id);
						//document.getElementById('info2').innerHTML = 'abrindo node '+node.id;
						var list = loadNodes(node, 3);
						loadNodesAjax(list);
					};
				};
	
				${codigo}
				
				<%--						
				var node1 = new Node('1');
				var c1 = node1.newColumn();
				var c2 = node1.newColumn();
				var c3 = node1.newColumn();
				c1.innerHTML = '1';
				c1.icon = 'folder.gif';
				c3.innerHTML = '640,78';
				c3.align = 'right';
				node1.hasChild = true;
	
				var node1_1 = new Node('1.1');
				c1 = node1_1.newColumn();
				c2 = node1_1.newColumn();
				c3 = node1_1.newColumn();
	
				c1.innerHTML = '1.1';
				c1.icon = 'folder.gif';
				c3.innerHTML = '571,42';
				c3.align = 'right';
				node1_1.hasChild = true;
				
				
				var node1_2 = new Node('1.2');
				c1 = node1_2.newColumn();
				c2 = node1_2.newColumn();
				c3 = node1_2.newColumn();
	
				c1.innerHTML = '1.2';
				//c1.icon = 'it1.gif';
				c2.innerHTML = '10/10/2006';
				c3.innerHTML = '69,36';
				c3.align = 'right';
				node1_2.hasChild = false;
	
				var node1_1_1 = new Node('1.1.1');
				c1 = node1_1_1.newColumn();
				c2 = node1_1_1.newColumn();
				c3 = node1_1_1.newColumn();
				c1.innerHTML = '1.1.1';
				c1.icon = 'folder.gif';
				c3.innerHTML = '473,71';
				c3.align = 'right';
				node1_1_1.hasChild = true;						
	
	
				var node1_1_1_1 = new Node('1.1.1.1');
				c1 = node1_1_1_1.newColumn();
				c2 = node1_1_1_1.newColumn();
				c3 = node1_1_1_1.newColumn();
				c1.innerHTML = '1.1.1.1';
				//c1.icon = 'empty.gif';
				c2.innerHTML = '03/06/2005';
				c3.innerHTML = '239,35';
				c3.align = 'right';
				node1_1_1_1.hasChild = false;	
				
				var node1_1_1_2 = new Node('1.1.1.2');
				c1 = node1_1_1_2.newColumn();
				c2 = node1_1_1_2.newColumn();
				c3 = node1_1_1_2.newColumn();
				c1.innerHTML = '1.1.1.2';
				//c1.icon = 'empty.gif';
				c2.innerHTML = '03/09/2004';			
				c3.innerHTML = '234,36';
				c3.align = 'right';
				node1_1_1_2.hasChild = false;			
				
				
				
				var node1_1_2 = new Node('1.1.2');
				c1 = node1_1_2.newColumn();
				c2 = node1_1_2.newColumn();
				c3 = node1_1_2.newColumn();
				c1.innerHTML = '1.1.2';
				c1.icon = 'folder.gif';
				c3.innerHTML = '97,71';
				c3.align = 'right';
				node1_1_2.hasChild = true;		
				
				var node1_1_2_1 = new Node('1.1.2.1');
				c1 = node1_1_2_1.newColumn();
				c2 = node1_1_2_1.newColumn();
				c3 = node1_1_2_1.newColumn();
				c1.innerHTML = '1.1.2.1';
				//c1.icon = 'empty.gif';
				c2.innerHTML = '03/01/2004';
				c3.innerHTML = '25,36';
				c3.align = 'right';
	
				node1_1_2_1.hasChild = false;	
				
				var node1_1_2_2 = new Node('1.1.2.2');
				c1 = node1_1_2_2.newColumn();
				c2 = node1_1_2_2.newColumn();
				c3 = node1_1_2_2.newColumn();
				c1.innerHTML = '1.1.2.2';
				//c1.icon = 'empty.gif';
				c2.innerHTML = '09/01/2005';			
				c3.innerHTML = '69,35';
				c3.align = 'right';
				node1_1_2_2.hasChild = false;	
														
				
				treeTable.addNode(node1);
				node1.addChild(node1_1);
				node1.addChild(node1_2);
				node1_1.addChild(node1_1_1);
				node1_1.addChild(node1_1_2);			
				node1_1_1.addChild(node1_1_1_1);
				node1_1_1.addChild(node1_1_1_2);
				node1_1_2.addChild(node1_1_2_1);
				node1_1_2.addChild(node1_1_2_2);			
	
				--%>
			</script>
			<div id="info">
				
			</div>
			<div id="info2">
				
			</div>
		</TD>
	</TR>
	<TR>
		<TD align="right" style="padding: 10px;" height="1%">
			<button onclick="selecionar()" class="botao_normal" >selecionar</button>
		</TD>
	</TR>
</table>
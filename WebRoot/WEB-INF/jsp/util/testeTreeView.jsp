<table id="x" width="400" bgcolor="white">




</table>

<script language="javascript">
	var treeTable = installTreeTable('x', '${ctx}/treetable/');
	
	var node1 = new Node(1);
	var c11 = node1.newColumn();
	var c21 = node1.newColumn();	
	
	var node2 = new Node(2);
	var c12 = node2.newColumn();
	var c22 = node2.newColumn();
	
	var node3 = new Node(3);
	var c13 = node3.newColumn();
	var c23 = node3.newColumn();
	
	var node4= new Node(4);
	var c14 = node4.newColumn();
	var c24 = node4.newColumn();
	
	c14.icon ='folder.gif';
	
	node1.hasChild = true;
	node2.hasChild = true;
	
	c11.innerHTML = 'pai';
	c12.innerHTML = 'filho';
	c13.innerHTML = 'neto 1';
	c14.innerHTML = 'neto 2';

	c21.innerHTML = '1';
	c22.innerHTML = '2';
	c23.innerHTML = '3';
	c24.innerHTML = '4';
	
	treeTable.addNode(node1);
	node1.addChild(node2);
	node2.addChild(node3);
	node2.addChild(node4);
</script>
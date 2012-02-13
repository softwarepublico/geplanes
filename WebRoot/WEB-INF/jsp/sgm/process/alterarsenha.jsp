<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="n" uri="neo"%>
<%@ taglib prefix="t" uri="template"%>

<script language="JavaScript">
	function limpa(){
		document.getElementById("novasenha").value="";
		document.getElementById("repetirnovasenha").value="";
	}
</script>

<t:tela titulo="Alterar Senha">
	<n:panelGrid columns="2" columnStyleClasses="labelColumn,propertyColumn">
		<n:bean name="filtro">
			<t:propertyConfig mode="input" renderAs="double" showLabel="true">
				<t:property name="listaUsuarios" itens="${lista}" id="listaUsuario" onchange="limpa()" autoSugestUniqueItem="true"/>
				<t:property name="novaSenha" type="password" id="novasenha"/>
				<t:property name="repetirNovaSenha" type="password" id="repetirnovasenha"/>
				<n:panel><br></n:panel><n:panel><br></n:panel>
				<t:property name="senha" type="password" id="senhaantiga" label="Digite a sua senha"/>
			</t:propertyConfig>
		</n:bean>
	</n:panelGrid><br>
	<table align="right">
		<tr>
			<td>
				<n:submit action="salvar" class="botao_normal" >Salvar</n:submit>
			</td>
		</tr>
	</table>
</t:tela>
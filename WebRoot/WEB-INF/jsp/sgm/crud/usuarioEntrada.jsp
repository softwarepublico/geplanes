<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="n" uri="neo"%>
<%@ taglib prefix="t" uri="template"%>

<t:entrada titulo="Cadastro de usuários" formEnctype="multipart/form-data">
	<t:property name="id" write="false" type="hidden"/>	
	<t:janelaEntrada>
		<t:tabelaEntrada>
			<t:property name="nome"				style="width: 300px;overflow:hidden;" />
			<t:property name="email"			style="width: 300px;overflow:hidden;" />
			<t:property name="cargo"			style="width: 300px;overflow:hidden;" />
			<t:property name="ramal" 			style="width: 120px;overflow:hidden;" />
			<t:property name="login" 			style="width: 120px;overflow:hidden;" />			
			<c:if test="${novo}">
				<t:property name="senha"			style="width: 120px;overflow:hidden;" type="password" class="required"/>
				<t:property name="verificaSenha"	style="width: 120px;overflow:hidden;" type="password" class="required"/>
			</c:if>
			<t:property name="papeis"			style="width: 250px" class="required"/>
			<t:property name="bloqueado"/>
			<t:property name="infComplementar"	style="width: 300px; height: 100px;" />
			<t:property name="foto" showRemoverButton="true" style="overflow:hidden;" />
			<c:if test="${!empty usuario.foto.id}">
				<n:panel colspan="2">
					<img src="${ctx}/DOWNLOADFILE/${usuario.foto.id}"/>
				</n:panel>
			</c:if>
		</t:tabelaEntrada>
		<t:detalhe name="usuariosUnidadeGerencial" showBotaoNovaLinha="false" showBotaoRemover="false" showColunaAcao="false">
			<t:propertyConfig mode="output">
				<t:property name="unidadeGerencial.planoGestao"/>
				<t:property name="unidadeGerencial"/>
				<t:property name="funcao"/>
			</t:propertyConfig>
		</t:detalhe>		
	</t:janelaEntrada>
</t:entrada>
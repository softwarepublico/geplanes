/* 
		Copyright 2007,2008,2009,2010 da Linkcom Informática Ltda
		
		Este arquivo é parte do programa GEPLANES.
 	   
 	    O GEPLANES é software livre; você pode redistribuí-lo e/ou 
		modificá-lo sob os termos da Licença Pública Geral GNU, conforme
 	    publicada pela Free Software Foundation; tanto a versão 2 da 
		Licença como (a seu critério) qualquer versão mais nova.
 	
 	    Este programa é distribuído na expectativa de ser útil, mas SEM 
		QUALQUER GARANTIA; sem mesmo a garantia implícita de 
		COMERCIALIZAÇÃO ou de ADEQUAÇÃO A QUALQUER PROPÓSITO EM PARTICULAR. 
		Consulte a Licença Pública Geral GNU para obter mais detalhes.
 	 
 	    Você deve ter recebido uma cópia da Licença Pública Geral GNU  	    
		junto com este programa; se não, escreva para a Free Software 
		Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 
		02111-1307, USA.
		
*/
package br.com.linkcom.sgm.senha.process;

import java.util.ArrayList;
import java.util.List;

import org.jasypt.util.password.StrongPasswordEncryptor;
import org.springframework.web.servlet.ModelAndView;

import br.com.linkcom.neo.authorization.process.ProcessAuthorizationModule;
import br.com.linkcom.neo.bean.annotation.Bean;
import br.com.linkcom.neo.controller.Controller;
import br.com.linkcom.neo.controller.DefaultAction;
import br.com.linkcom.neo.controller.Input;
import br.com.linkcom.neo.controller.MessageType;
import br.com.linkcom.neo.controller.MultiActionController;
import br.com.linkcom.neo.core.standard.Neo;
import br.com.linkcom.neo.core.web.WebRequestContext;
import br.com.linkcom.sgm.beans.Usuario;
import br.com.linkcom.sgm.exception.GeplanesException;
import br.com.linkcom.sgm.filtro.AlterarSenhaFiltro;
import br.com.linkcom.sgm.service.UsuarioService;

@Bean
@Controller(path="/sgm/process/AlterarSenha", authorizationModule=ProcessAuthorizationModule.class)
public class AlterarSenhaProcess extends MultiActionController{
	UsuarioService usuarioService;
	
	public void setUsuarioService(UsuarioService usuarioService) {
		this.usuarioService = usuarioService;
	}
	
	@DefaultAction
	public ModelAndView filtrar(WebRequestContext request, AlterarSenhaFiltro filtro) {
		List<Usuario> listaUsuario = new ArrayList<Usuario>(); 
		if (usuarioService.isUsuarioLogadoAdmin()) {
			listaUsuario = usuarioService.findAllNaoBloqueados();
		}
		else { 
			listaUsuario.add((Usuario)Neo.getRequestContext().getUser());
		}
		request.setAttribute("lista", listaUsuario);
		return new ModelAndView("process/alterarsenha", "filtro", filtro);
	}
	
	@Input("filtrar")
	public ModelAndView salvar(WebRequestContext request, AlterarSenhaFiltro filtro) {
		Usuario usuario = filtro.getListaUsuarios();
		String usuarioSenha = filtro.getSenha();
		Usuario usuarioLogado = (Usuario) Neo.getRequestContext().getUser();
		usuarioLogado.setSenha(usuarioService.obtemSenha(usuarioLogado).getSenha());
		
		StrongPasswordEncryptor encryptor = new StrongPasswordEncryptor();
		// Verifica se a senha do usuario logado é valida 
		if (!encryptor.checkPassword(usuarioSenha, usuarioLogado.getSenha())) {
			throw new GeplanesException("Sua senha não confere. Favor, informe a senha correta.");
		}		
		
		String novaSenha = filtro.getNovaSenha();
		
		//Verifica se a nova senha tem pelo menos 4 posições 
		if ((filtro.getNovaSenha()).length() < 4) {
			throw new GeplanesException("A nova senha deve conter, no mínimo, 4 caracteres.");
	    }
		
		//Verifica se a nova senha e a confirmação da nova senha são iguais 
		if (!(filtro.getNovaSenha().equals(filtro.getRepetirNovaSenha()))) {
			throw new GeplanesException("A nova senha e sua confirmação devem ser exatamente iguais.");
		}
		
		// Tudo OK para a atualização da senha do usuário.
		usuario.setSenha(novaSenha);
		usuarioService.updatePassword(usuario, true);
		request.addMessage("Senha alterada com sucesso", MessageType.INFO);
		return filtrar(request,filtro);
	}
}

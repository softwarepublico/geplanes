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
package br.com.linkcom.sgm.controller.crud;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;

import br.com.linkcom.neo.authorization.crud.CrudAuthorizationModule;
import br.com.linkcom.neo.controller.Controller;
import br.com.linkcom.neo.controller.MessageType;
import br.com.linkcom.neo.controller.crud.CrudController;
import br.com.linkcom.neo.controller.crud.CrudException;
import br.com.linkcom.neo.core.standard.Neo;
import br.com.linkcom.neo.core.web.WebRequestContext;
import br.com.linkcom.neo.persistence.ListagemResult;
import br.com.linkcom.neo.util.CollectionsUtil;
import br.com.linkcom.sgm.beans.PlanoGestao;
import br.com.linkcom.sgm.beans.UnidadeGerencial;
import br.com.linkcom.sgm.beans.Usuario;
import br.com.linkcom.sgm.filtro.UsuarioFiltro;
import br.com.linkcom.sgm.service.PlanoGestaoService;
import br.com.linkcom.sgm.service.UnidadeGerencialService;
import br.com.linkcom.sgm.service.UsuarioPapelService;
import br.com.linkcom.sgm.service.UsuarioService;
import br.com.linkcom.sgm.util.FiltroUtils;



@Controller(path={"/sgm/crud/Usuario","/util/crud/Usuario"}, authorizationModule=CrudAuthorizationModule.class)
public class UsuarioCrud extends CrudController<UsuarioFiltro, Usuario, Usuario> {
	
	private UsuarioService usuarioService;
	private UsuarioPapelService usuarioPapelService;
	private UnidadeGerencialService unidadeGerencialService;
	private PlanoGestaoService planoGestaoService;
 	
	public void setUsuarioPapelService(UsuarioPapelService usuarioPapelService) {this.usuarioPapelService = usuarioPapelService;}
	public void setUsuarioService(UsuarioService usuarioService) {this.usuarioService = usuarioService;}
	public void setUnidadeGerencialService(UnidadeGerencialService unidadeGerencialService) { this.unidadeGerencialService = unidadeGerencialService;}
	public void setPlanoGestaoService(PlanoGestaoService planoGestaoService) { this.planoGestaoService = planoGestaoService;}
	
	@Override
	protected void entrada(WebRequestContext request, Usuario form) throws Exception {
		if (form.getId()==null)
			request.setAttribute("novo", true);
		//apenas se o usuario digitar que será verificado novamente
		form.setSenha(null);
		form.setVerificaSenha(null);
		
		//popular os perfis do usuario
		if(form.getId() != null){
			form.setPapeis(usuarioPapelService.getPapeis(form));
		}
	}
	
	@Override
	public ModelAndView doListagem(WebRequestContext request, UsuarioFiltro filtro) throws CrudException {
		/*** Seta valores default para o filtro ***/		
		FiltroUtils.preencheFiltroPlanoGestaoUnidadeUsuario(filtro);
		
		return super.doListagem(request, filtro);
	}
	
	@Override
	protected ListagemResult<Usuario> getLista(WebRequestContext request, UsuarioFiltro filtro) {		
		ListagemResult<Usuario> result = super.getLista(request, filtro);
		List<Usuario> listaUsuario = result.list();
		
		if (request.getRequestQuery().contains("util")) {
			StringBuilder ids = new StringBuilder();
			for (Usuario usuario : listaUsuario) {
				ids.append("'"+ usuario.getNome() + "|" + usuario.getEmail() +"',");
			}
			if(ids.length() > 0){
				ids.delete(ids.length() - 1, ids.length());
			}
			request.setAttribute("idsFormatados", ids.toString());
		}
		
		PlanoGestao planoGestaoAtual = planoGestaoService.obtemPlanoGestaoAtual();
		
		for (Usuario usuario : listaUsuario) {
			List<UnidadeGerencial> lista = unidadeGerencialService.loadByUsuarioPlanoGestao(usuario, planoGestaoAtual);
			if(lista != null && lista.size() > 0){
				usuario.setUgsAtuais(CollectionsUtil.listAndConcatenate(lista, "sigla", ", "));
			}
		}
		return result;
	}
	
	@Override
	protected void validateBean(Usuario bean, BindException errors) {
		
		if(bean.getId() == null){
			//esta validação não é feita no bean pq a regra de negocio permite na alteração
			// do usuario não alterar a senha
			if(bean.getSenha() == null || bean.getSenha().equals("")){
				errors.reject("É necessário informar a senha.","É necessário informar a senha.");
			}
		}
		
		if(bean.getId() == null){
			if(!bean.getSenha().equals("")){
				if(bean.getVerificaSenha() == null || bean.getVerificaSenha().equals("")){
					errors.reject("É necessário a confirmação da senha.","É necessário a confirmação da senha.");
				}
				if(bean.getVerificaSenha() != null || !bean.getVerificaSenha().equals("")){
					if(!bean.getSenha().equals(bean.getVerificaSenha())){
						errors.reject("As senhas digitadas não conferem.","As senhas digitadas não conferem.");
					}else{
						if (bean.getId()==null) {						
							//criptogafrar a senha
							bean.setSenha(DigestUtils.md5Hex(bean.getSenha()));
						}
					}
				}
			}
		}
		//verificar se já existe o login informado
		if(!bean.getLogin().equals("") && bean.getId() == null){
			Boolean existe = usuarioService.verificaExisteLogin(bean.getLogin());
			if(existe){
				errors.reject("Este login já existe. Favor informar outro.","Este login já existe. Favor informar outro.");
			}
		}
		//se o usuario alterou o login verificar se este já existe
		if(!bean.getLogin().equals("") && bean.getId() != null){
			Usuario usuario = usuarioService.obtemLogin(bean);
			if(!usuario.getLogin().equals("") && !bean.getLogin().equals(usuario.getLogin())){
				Boolean existe = usuarioService.verificaExisteLogin(bean.getLogin());
				if(existe){
					errors.reject("Este login já existe. Favor informar outro.","Este login já existe. Favor informar outro.");
				}
			}
		}
		
		if(bean.getPapeis() == null ||bean.getPapeis().size() == 0 ){
			errors.reject("É necessário pelo menos um perfil.","É necessário pelo menos um perfil.");
		}
		
		if(bean.getFoto() != null && bean.getFoto().getSize() > 0) {
			if(bean.getFoto().getContent().length > 500000){
				errors.reject("O tamanho máximo permitido para o arquivo é 500kb.","O tamanho máximo permitido para o arquivo é 500kb.");
			}
			//400x500
			if (!"image/jpeg".equals(bean.getFoto().getContenttype()) && !"image/jpg".equals(bean.getFoto().getContenttype())) {
				errors.reject("O formato da imagem é inválido. É permitido apenas jpeg.","O formato da imagem é inválido. É permitido apenas jpeg.");
			}
			
			try {
				BufferedImage image = ImageIO.read(new ByteArrayInputStream(bean.getFoto().getContent()));
				if (image != null && image.getWidth() > 400) {
					errors.reject("O tamanho máximo permitido para o arquivo é de 400px de largura","O tamanho máximo permitido para o arquivo é de 400px de largura");
				}
				if (image != null && image.getHeight() > 500) {
					errors.reject("A tamanho máximo permitido para o arquivo é de 500px de altura","O tamanho máximo permitido para o arquivo é de 500px de altura");
				}
			} catch (IOException e) {
				errors.reject("Não foi possível carregar a imagem","Não foi possível carregar a imagem");
			}
		}
		super.validateBean(bean, errors);
		
	}
	
	@Override
	protected void excluir(WebRequestContext request, Usuario bean) throws Exception {
		// Verifica se quem foi excluído é quem esta logado, se for invalida a sessão
		Usuario usuario = (Usuario) Neo.getUser();
		boolean invalidar = bean.getId().equals(usuario.getId()) ? true : false;
		
		super.excluir(request, bean);
		
		if (invalidar) {
			request.getSession().invalidate();
		}
	}
	
	@Override
	protected void salvar(WebRequestContext request, Usuario bean) throws Exception {
		super.salvar(request, bean);
		request.addMessage("Usuário salvo com sucesso", MessageType.INFO);
	}
}

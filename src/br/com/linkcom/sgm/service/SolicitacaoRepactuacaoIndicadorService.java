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
package br.com.linkcom.sgm.service;

import java.util.ArrayList;
import java.util.List;

import br.com.linkcom.sgm.beans.Indicador;
import br.com.linkcom.sgm.beans.SolicitacaoRepactuacaoIndicador;
import br.com.linkcom.sgm.beans.UnidadeGerencial;
import br.com.linkcom.sgm.controller.filtro.SolicitacaoRepactuacaoIndicadorFiltro;
import br.com.linkcom.sgm.dao.SolicitacaoRepactuacaoIndicadorDAO;
import br.com.linkcom.sgm.util.neo.service.GenericService;

public class SolicitacaoRepactuacaoIndicadorService extends GenericService<SolicitacaoRepactuacaoIndicador> {

	private SolicitacaoRepactuacaoIndicadorDAO solicitacaoRepactuacaoIndicadorDAO;
	private UsuarioService usuarioService;
	
	public void setUsuarioService(UsuarioService usuarioService) {
		this.usuarioService = usuarioService;
	}
	public void setSolicitacaoRepactuacaoIndicadorDAO(SolicitacaoRepactuacaoIndicadorDAO solicitacaoRepactuacaoIndicadorDAO) {this.solicitacaoRepactuacaoIndicadorDAO = solicitacaoRepactuacaoIndicadorDAO;}
	
	/**
	 * Faz referência ao DAO.
	 * 
	 * @see br.com.linkcom.sgm.dao.SolicitacaoRepactuacaoIndicadorDAO#existeSolicitacaoRepactuacaoAberta
	 *
	 * @param indicador
	 * @return
	 * @author Rodrigo Freitas
	 */
	public Boolean existeSolicitacaoRepactuacaoAberta(Indicador indicador) {
		return solicitacaoRepactuacaoIndicadorDAO.existeSolicitacaoRepactuacaoAberta(indicador);
	}
	
	public void saveOrUpdateStatus(SolicitacaoRepactuacaoIndicador solicitacaoRepactuacao) {
		solicitacaoRepactuacaoIndicadorDAO.saveOrUpdateStatus(solicitacaoRepactuacao);		
	}
	
	public List<SolicitacaoRepactuacaoIndicador> findSolicitacoes(SolicitacaoRepactuacaoIndicadorFiltro filtro) {
		List<UnidadeGerencial> listaUGDisponivel = new ArrayList<UnidadeGerencial>();
		List<UnidadeGerencial> listaUGUsuario = usuarioService.getUsuarioLogadoUGs(filtro.getPlanoGestao());
		Boolean usuarioLogadoIsAdmin = usuarioService.isUsuarioLogadoAdmin();
		
		if (!usuarioLogadoIsAdmin) {
			if (listaUGUsuario != null) {
				listaUGDisponivel.addAll(listaUGUsuario);
			}
		}

		filtro.setListaUnidadeGerencialDisponivel(listaUGDisponivel);		
		return solicitacaoRepactuacaoIndicadorDAO.findSolicitacoes(filtro); 
	}
	
	/**
	 * Retorna a solicitação de repactuação com os comentários associados
	 * 
	 * @author Rodrigo Alvarenga
	 * @param solicitacaoRepactuacaoIndicador
	 * @return solicitacaoRepactuacaoIndicador
	 */
	public SolicitacaoRepactuacaoIndicador loadWithComentarios(SolicitacaoRepactuacaoIndicador solicitacaoRepactuacaoIndicador) {
		return solicitacaoRepactuacaoIndicadorDAO.loadWithComentarios(solicitacaoRepactuacaoIndicador);
	}
	
	/**
	 * Retorna a solicitação de repactuação com o indicador associada
	 * 
	 * @author Rodrigo Alvarenga
	 * @param solicitacaoRepactuacao
	 * @return solicitacaoRepactuacao
	 */
	public SolicitacaoRepactuacaoIndicador loadWithIndicador(SolicitacaoRepactuacaoIndicador solicitacaoRepactuacao) {
		return solicitacaoRepactuacaoIndicadorDAO.loadWithIndicador(solicitacaoRepactuacao); 
	}	
}

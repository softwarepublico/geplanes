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
import br.com.linkcom.sgm.beans.SolicitacaoCancelamentoIndicador;
import br.com.linkcom.sgm.beans.UnidadeGerencial;
import br.com.linkcom.sgm.beans.Usuario;
import br.com.linkcom.sgm.controller.filtro.SolicitacaoCancelamentoIndicadorFiltro;
import br.com.linkcom.sgm.dao.SolicitacaoCancelamentoIndicadorDAO;
import br.com.linkcom.sgm.util.neo.service.GenericService;

public class SolicitacaoCancelamentoIndicadorService extends GenericService<SolicitacaoCancelamentoIndicador> {

	private SolicitacaoCancelamentoIndicadorDAO solicitacaoCancelamentoIndicadorDAO;	
	private UsuarioService usuarioService;
	
	public void setSolicitacaoCancelamentoIndicadorDAO(SolicitacaoCancelamentoIndicadorDAO solicitacaoCancelamentoIndicadorDAO) {this.solicitacaoCancelamentoIndicadorDAO = solicitacaoCancelamentoIndicadorDAO;}	
	public void setUsuarioService(UsuarioService usuarioService) {this.usuarioService = usuarioService;}	
	
	
	/**
	 * Retorna uma lista de solicitações de cancelamento de indicador de acordo com o filtro
	 * Somente o planoGestao do filtro é obrigatório.
	 * 
	 * @author Rodrigo Alvarenga
	 * @param filtro
	 * @return List<SolicitacaoCancelamentoIndicador>
	 */
	public List<SolicitacaoCancelamentoIndicador> findSolicitacoes(SolicitacaoCancelamentoIndicadorFiltro filtro) {
		List<UnidadeGerencial> listaUGDisponivel = new ArrayList<UnidadeGerencial>();
		List<UnidadeGerencial> listaUGUsuario = usuarioService.getUsuarioLogadoUGs(filtro.getPlanoGestao());
		Boolean usuarioLogadoIsAdmin = usuarioService.isUsuarioLogadoAdmin();
		
		if (!usuarioLogadoIsAdmin) {
			if (listaUGUsuario != null) {
				listaUGDisponivel.addAll(listaUGUsuario);
			}
		}

		filtro.setListaUnidadeGerencialDisponivel(listaUGDisponivel);		
		return solicitacaoCancelamentoIndicadorDAO.findSolicitacoes(filtro); 
	}
	
	/**
	 * Retorna lista de solicitações de cancelamento do indicador
	 * @author Rodrigo Duarte
	 * @param indicador
	 * @return
	 */
	public List<SolicitacaoCancelamentoIndicador> findByIndicador(Indicador indicador) {
		return solicitacaoCancelamentoIndicadorDAO.findByIndicador(indicador); 
	}
	
	/**
	 * Faz referência ao DAO.
	 * 
	 * @see br.com.linkcom.sgm.dao.SolicitacaoCancelamentoIndicadorDAO#existeSolicitacaoCancelamentoAberta
	 *
	 * @param indicador
	 * @return
	 * @author Rodrigo Freitas
	 */
	public Boolean existeSolicitacaoCancelamentoAberta(Indicador indicador) {
		return solicitacaoCancelamentoIndicadorDAO.existeSolicitacaoCancelamentoAberta(indicador);
	}	

	/**
	 * update status
	 * @author Rodrigo Alvarenga 
	 * @param solicitacaoCancelamento
	 */
	public void saveOrUpdateStatus(SolicitacaoCancelamentoIndicador solicitacaoCancelamento) {
		solicitacaoCancelamentoIndicadorDAO.saveOrUpdateStatus(solicitacaoCancelamento);		
	}


	/**
	 * @author Rodrigo Alvarenga 
	 * @param solicitante
	 * @return
	 */
	public List<SolicitacaoCancelamentoIndicador> findByUsuario(Usuario solicitante) {
		return solicitacaoCancelamentoIndicadorDAO.findByUsuario(solicitante);
	}
	
	/**
	 * Retorna a solicitação de cancelamento com o indicador associada
	 * 
	 * @author Rodrigo Alvarenga
	 * @param solicitacaoCancelamento
	 * @return solicitacaoCancelamento
	 */
	public SolicitacaoCancelamentoIndicador loadWithIndicador(SolicitacaoCancelamentoIndicador solicitacaoCancelamento) {
		return solicitacaoCancelamentoIndicadorDAO.loadWithIndicador(solicitacaoCancelamento); 
	}
	
	public boolean isAprovado(SolicitacaoCancelamentoIndicador solicitacaoCancelamento) {
		return solicitacaoCancelamentoIndicadorDAO.isAprovado(solicitacaoCancelamento);
	}	
	
	/**
	 * Retorna a solicitação de cancelamento com os comentários associados
	 * 
	 * @author Rodrigo Alvarenga
	 * @param solicitacaoCancelamentoIndicador
	 * @return solicitacaoCancelamentoIndicador
	 */
	public SolicitacaoCancelamentoIndicador loadWithComentarios(SolicitacaoCancelamentoIndicador solicitacaoCancelamentoIndicador) {
		return solicitacaoCancelamentoIndicadorDAO.loadWithComentarios(solicitacaoCancelamentoIndicador);
	}	
	
}

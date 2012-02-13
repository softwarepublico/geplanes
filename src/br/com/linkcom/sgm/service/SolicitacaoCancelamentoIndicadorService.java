/* 
		Copyright 2007,2008,2009,2010 da Linkcom Inform�tica Ltda
		
		Este arquivo � parte do programa GEPLANES.
 	   
 	    O GEPLANES � software livre; voc� pode redistribu�-lo e/ou 
		modific�-lo sob os termos da Licen�a P�blica Geral GNU, conforme
 	    publicada pela Free Software Foundation; tanto a vers�o 2 da 
		Licen�a como (a seu crit�rio) qualquer vers�o mais nova.
 	
 	    Este programa � distribu�do na expectativa de ser �til, mas SEM 
		QUALQUER GARANTIA; sem mesmo a garantia impl�cita de 
		COMERCIALIZA��O ou de ADEQUA��O A QUALQUER PROP�SITO EM PARTICULAR. 
		Consulte a Licen�a P�blica Geral GNU para obter mais detalhes.
 	 
 	    Voc� deve ter recebido uma c�pia da Licen�a P�blica Geral GNU  	    
		junto com este programa; se n�o, escreva para a Free Software 
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
	 * Retorna uma lista de solicita��es de cancelamento de indicador de acordo com o filtro
	 * Somente o planoGestao do filtro � obrigat�rio.
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
	 * Retorna lista de solicita��es de cancelamento do indicador
	 * @author Rodrigo Duarte
	 * @param indicador
	 * @return
	 */
	public List<SolicitacaoCancelamentoIndicador> findByIndicador(Indicador indicador) {
		return solicitacaoCancelamentoIndicadorDAO.findByIndicador(indicador); 
	}
	
	/**
	 * Faz refer�ncia ao DAO.
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
	 * Retorna a solicita��o de cancelamento com o indicador associada
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
	 * Retorna a solicita��o de cancelamento com os coment�rios associados
	 * 
	 * @author Rodrigo Alvarenga
	 * @param solicitacaoCancelamentoIndicador
	 * @return solicitacaoCancelamentoIndicador
	 */
	public SolicitacaoCancelamentoIndicador loadWithComentarios(SolicitacaoCancelamentoIndicador solicitacaoCancelamentoIndicador) {
		return solicitacaoCancelamentoIndicadorDAO.loadWithComentarios(solicitacaoCancelamentoIndicador);
	}	
	
}

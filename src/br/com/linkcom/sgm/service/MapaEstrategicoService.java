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

import java.util.List;

import br.com.linkcom.sgm.util.neo.service.GenericService;
import br.com.linkcom.sgm.beans.MapaEstrategico;
import br.com.linkcom.sgm.beans.ObjetivoMapaEstrategico;
import br.com.linkcom.sgm.beans.PerspectivaMapaEstrategico;
import br.com.linkcom.sgm.beans.UnidadeGerencial;
import br.com.linkcom.sgm.dao.MapaEstrategicoDAO;

public class MapaEstrategicoService extends GenericService<MapaEstrategico> {

	private MapaEstrategicoDAO mapaEstrategicoDAO;
	private PerspectivaMapaEstrategicoService perspectivaMapaEstrategicoService;
	private ObjetivoMapaEstrategicoService objetivoMapaEstrategicoService;
	
	public void setMapaEstrategicoDAO(MapaEstrategicoDAO mapaEstrategicoDAO) {this.mapaEstrategicoDAO = mapaEstrategicoDAO;}
	public void setPerspectivaMapaEstrategicoService(PerspectivaMapaEstrategicoService perspectivaMapaEstrategicoService) {this.perspectivaMapaEstrategicoService = perspectivaMapaEstrategicoService;}
	public void setObjetivoMapaEstrategicoService(ObjetivoMapaEstrategicoService objetivoMapaEstrategicoService) {this.objetivoMapaEstrategicoService = objetivoMapaEstrategicoService;}

	/**
	 * Carrega o mapa estratégico de uma determinada unidade gerencial.
	 *
	 * @param unidadeGerencial
	 * @return
	 */
	public MapaEstrategico loadByUnidadeGerencial(UnidadeGerencial unidadeGerencial) {
		return mapaEstrategicoDAO.loadByUnidadeGerencial(unidadeGerencial);
	}	
	
	/**
	 * Salva o mapa estratégico.
	 * 
	 * @param mapaEstrategico
	 * @param unidadeGerencial
	 * @param useTransaction
	 */
	public void salvaDefinicaoObjetivoEstrategico(MapaEstrategico mapaEstrategico, UnidadeGerencial unidadeGerencial, boolean useTransaction) {
		if (mapaEstrategico != null) {
			
			//Setando a unidade salva no mapaEstratégico.
			mapaEstrategico.setUnidadeGerencial(unidadeGerencial);
			
			//Setando o mapa completo na Unidade Gerencial.
			unidadeGerencial.setMapaEstrategico(mapaEstrategico);
			
			//Salvando o mapa estratégico.
			if (useTransaction) {
				this.saveOrUpdate(mapaEstrategico);
			}
			else {
				this.saveOrUpdateWithoutTransaction(mapaEstrategico);
			}
			
			// Recupera os objetivos estratégicos do banco de dados.
			List<PerspectivaMapaEstrategico> listaPerspectivaMapaEstrategicoDB = perspectivaMapaEstrategicoService.findByMapaEstrategico(mapaEstrategico);
			
			// Verifica se alguma perspectiva foi excluída.
			if (listaPerspectivaMapaEstrategicoDB != null && !listaPerspectivaMapaEstrategicoDB.isEmpty()) {
				for (PerspectivaMapaEstrategico perspectivaMapaEstrategicoDB : listaPerspectivaMapaEstrategicoDB) {
					boolean perspectivaMapaEstrategicoRemovida = true;
					
					if (unidadeGerencial.getMapaEstrategico().getListaPerspectivaMapaEstrategico() != null) {
						for (PerspectivaMapaEstrategico perspectivaMapaEstrategicoApp : unidadeGerencial.getMapaEstrategico().getListaPerspectivaMapaEstrategico()) {
							if (perspectivaMapaEstrategicoDB.getId().equals(perspectivaMapaEstrategicoApp.getId())) {
								perspectivaMapaEstrategicoRemovida = false;
								break;
							}
						}
					}
					
					// Se o usuário removeu a perspectiva, exclui a dita cuja do banco de dados.
					if (perspectivaMapaEstrategicoRemovida) {
						perspectivaMapaEstrategicoDB.setMapaEstrategico(null);
						perspectivaMapaEstrategicoService.delete(perspectivaMapaEstrategicoDB);
					}
				}
			}			
			
			// Insere/atualiza as perspectivas criadas/alteradas pelo usuário.
			if (unidadeGerencial.getMapaEstrategico().getListaPerspectivaMapaEstrategico() != null) {
				for (PerspectivaMapaEstrategico perspectivaMapaEstrategico : unidadeGerencial.getMapaEstrategico().getListaPerspectivaMapaEstrategico()) {

					//Setando o mapa salvo.
					perspectivaMapaEstrategico.setMapaEstrategico(mapaEstrategico);
					
					//Salvando a perspectiva.
					if (useTransaction) {
						perspectivaMapaEstrategicoService.saveOrUpdate(perspectivaMapaEstrategico);
					}
					else {
						perspectivaMapaEstrategicoService.saveOrUpdateWithoutTransaction(perspectivaMapaEstrategico);
					}

					// Recupera os objetivos estratégicos do banco de dados.
					List<ObjetivoMapaEstrategico> listaObjetivoMapaEstrategicoDB = objetivoMapaEstrategicoService.findByPerspectivaMapaEstrategico(perspectivaMapaEstrategico);
					
					// Verifica se algum objetivo estratégico foi excluído.
					if (listaObjetivoMapaEstrategicoDB != null && !listaObjetivoMapaEstrategicoDB.isEmpty()) {
						for (ObjetivoMapaEstrategico objetivoMapaEstrategicoDB : listaObjetivoMapaEstrategicoDB) {
							boolean objEstratRemovido = true;
							
							if (perspectivaMapaEstrategico.getListaObjetivoMapaEstrategico() != null) {
								for (ObjetivoMapaEstrategico objetivoMapaEstrategicoApp : perspectivaMapaEstrategico.getListaObjetivoMapaEstrategico()){
									if (objetivoMapaEstrategicoDB.getId().equals(objetivoMapaEstrategicoApp.getId()) && objetivoMapaEstrategicoApp.getObjetivoEstrategico() != null) {
										objEstratRemovido = false;
										break;
									}
								}
							}
							
							// Se o usuário removeu o objetivo estratégico, exclui o dito cujo do banco de dados.
							// Porém, antes de excluir verifica se não existe Matriz FCS vinculada a esse objetivo.
							// Se tiver, não pode excluir.
							if (objEstratRemovido) {
								objetivoMapaEstrategicoService.delete(objetivoMapaEstrategicoDB);
							}
						}
					}
					
					// Insere/atualiza os objetivos estratégicos criadas/alteradas pelo usuário.
					if (perspectivaMapaEstrategico.getListaObjetivoMapaEstrategico()!=null) {
						for (ObjetivoMapaEstrategico objetivoMapaEstrategico : perspectivaMapaEstrategico.getListaObjetivoMapaEstrategico()){
							if (objetivoMapaEstrategico.getObjetivoEstrategico() != null) {
								//Setando a perspectiva no objetivo estratégico.
								objetivoMapaEstrategico.setPerspectivaMapaEstrategico(perspectivaMapaEstrategico);
								
								//Salvando o objetivo estratégico.
								if (useTransaction) {
									objetivoMapaEstrategicoService.saveOrUpdate(objetivoMapaEstrategico);
								}
								else {
									objetivoMapaEstrategicoService.saveOrUpdateWithoutTransaction(objetivoMapaEstrategico);
								}
							}
						}
					}
				}
			}
		}
	}
	
	/**
	 * Remove o mapa estratégico, com suas perspectivas e objetivos estratégicos.
	 * @param mapaEstrategico
	 */
	public void excluiMapaEstrategico(MapaEstrategico mapaEstrategico) {
		if (mapaEstrategico != null) {
			if (mapaEstrategico.getListaPerspectivaMapaEstrategico() != null && !mapaEstrategico.getListaPerspectivaMapaEstrategico().isEmpty()) {
				for (PerspectivaMapaEstrategico perspectivaMapaEstrategico : mapaEstrategico.getListaPerspectivaMapaEstrategico()) {
					if (perspectivaMapaEstrategico.getListaObjetivoMapaEstrategico() != null && !perspectivaMapaEstrategico.getListaObjetivoMapaEstrategico().isEmpty()) {
						for (ObjetivoMapaEstrategico objetivoMapaEstrategico : perspectivaMapaEstrategico.getListaObjetivoMapaEstrategico()) {
							if (objetivoMapaEstrategico.getId() != null) {
								objetivoMapaEstrategicoService.delete(objetivoMapaEstrategico);
							}
						}
					}
					if (perspectivaMapaEstrategico.getId() != null) {
						perspectivaMapaEstrategico.setListaObjetivoMapaEstrategico(null);
						perspectivaMapaEstrategicoService.delete(perspectivaMapaEstrategico);
					}
				}
			}
			if (mapaEstrategico.getId() != null) {
				mapaEstrategico.setListaPerspectivaMapaEstrategico(null);
				this.delete(mapaEstrategico);
			}
		}
	}
}

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
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanComparator;

import br.com.linkcom.sgm.beans.Indicador;
import br.com.linkcom.sgm.beans.MapaEstrategico;
import br.com.linkcom.sgm.beans.MatrizFCS;
import br.com.linkcom.sgm.beans.ObjetivoMapaEstrategico;
import br.com.linkcom.sgm.beans.PerspectivaMapaEstrategico;
import br.com.linkcom.sgm.beans.UnidadeGerencial;
import br.com.linkcom.sgm.dao.PerspectivaMapaEstrategicoDAO;
import br.com.linkcom.sgm.exception.GeplanesException;
import br.com.linkcom.sgm.util.neo.service.GenericService;

public class PerspectivaMapaEstrategicoService extends GenericService<PerspectivaMapaEstrategico> {

	private PerspectivaMapaEstrategicoDAO perspectivaMapaEstrategicoDAO;
	private UnidadeGerencialService unidadeGerencialService;
	private MatrizFCSService matrizFCSService;
	private ObjetivoMapaEstrategicoService objetivoMapaEstrategicoService;
	private IndicadorService indicadorService;
	
	public void setPerspectivaMapaEstrategicoDAO(PerspectivaMapaEstrategicoDAO perspectivaMapaEstrategicoDAO) {this.perspectivaMapaEstrategicoDAO = perspectivaMapaEstrategicoDAO;}
	public void setUnidadeGerencialService(UnidadeGerencialService unidadeGerencialService) {this.unidadeGerencialService = unidadeGerencialService;}
	public void setMatrizFCSService(MatrizFCSService matrizFCSService) {this.matrizFCSService = matrizFCSService;}
	public void setObjetivoMapaEstrategicoService(ObjetivoMapaEstrategicoService objetivoMapaEstrategicoService) {this.objetivoMapaEstrategicoService = objetivoMapaEstrategicoService;}
	public void setIndicadorService(IndicadorService indicadorService) {this.indicadorService = indicadorService;}
	
	/**
	 * Método retorna a lista de todas as perspectivas (com seus blocos e estratégias) relacionadas a um determinado MapaEstratégico.
	 * @author Matheus Melo Gonçalves
	 * @param mapaEstrategico
	 * @return List<PerspectivaMapaEstrategico>
	 * @throws GeplanesException (Se o mapa estratégico estiver nulo)
	 */
	public List<PerspectivaMapaEstrategico> findByMapaEstrategico(MapaEstrategico mapaEstrategico){
		if(mapaEstrategico == null || mapaEstrategico.getId() == null) {
			throw new GeplanesException("O mapa estratégico não pode ser nulo na pesquisa de perspectivas.");
		}
		return perspectivaMapaEstrategicoDAO.findByMapaEstrategico(mapaEstrategico);
	}
	
	/**
	 * Retorna as perspectivas cadastradas para uma determinada UG, 
	 * baseado no Mapa Estratégico.
	 * Se o parâmetro <code>pesquisarUGSuperior<code> for verdadeiro, 
	 * caso uma UG não tenha o Mapa Estratégico cadastrado, procura nas UGs superiores, recursivamente.
	 * 
	 * @param unidadeGerencial
	 * @param pesquisarUGSuperior
	 * @return
	 */
	public List<PerspectivaMapaEstrategico> findByUnidadeGerencialThroughMapaEstrategico(UnidadeGerencial unidadeGerencial, boolean pesquisarUGSuperior) {
		return findByUnidadeGerencialThroughMapaEstrategico(unidadeGerencial, pesquisarUGSuperior, null);
	}	
	
	/**
	 * Retorna as perspectivas cadastradas para uma determinada UG, 
	 * baseado no Mapa Estratégico.
	 * Se o parâmetro <code>pesquisarUGSuperior<code> for verdadeiro, 
	 * caso uma UG não tenha o Mapa Estratégico cadastrado, procura nas UGs superiores, recursivamente.
	 * As perspectivas serão retornadas na ordem definida pelo <code>orderBy</code>. 
	 * Caso esse parâmetro seja nulo, a ordem será definida pela descrição da perspectiva.
	 * 
	 * @param unidadeGerencial
	 * @param pesquisarUGSuperior
	 * @param orderBy
	 * @return
	 */
	public List<PerspectivaMapaEstrategico> findByUnidadeGerencialThroughMapaEstrategico(UnidadeGerencial unidadeGerencial, boolean pesquisarUGSuperior, String orderBy) {
		List<PerspectivaMapaEstrategico> listaPerspectivaMapaEstrategico = perspectivaMapaEstrategicoDAO.findByUnidadeGerencialThroughMapaEstrategico(unidadeGerencial, orderBy);
		if (listaPerspectivaMapaEstrategico == null || listaPerspectivaMapaEstrategico.isEmpty() && pesquisarUGSuperior) {
			// Percorre as UGs superiores até encontrar alguma que possua Mapa Estratégico cadastrado.
			List<UnidadeGerencial> ugPais = unidadeGerencialService.findAllParents(unidadeGerencial);
			if (ugPais != null && !ugPais.isEmpty()) {
				for (UnidadeGerencial ugPai : ugPais) {
					listaPerspectivaMapaEstrategico = perspectivaMapaEstrategicoDAO.findByUnidadeGerencialThroughMapaEstrategico(ugPai, orderBy);
					if (listaPerspectivaMapaEstrategico != null && !listaPerspectivaMapaEstrategico.isEmpty()) {
						break;
					}
				}
			}
		}
		return listaPerspectivaMapaEstrategico;		
	}
	
	/**
	 * Retorna as perspectivas cadastradas para uma determinada UG, 
	 * baseado na Matriz de Inciativas x FCS.
	 * Se o parâmetro <code>pesquisarUGSuperior<code> for verdadeiro, 
	 * caso uma UG não tenha a Matriz FCS cadastrada, procura nas UGs superiores, recursivamente.
	 * 
	 * @param unidadeGerencial
	 * @param pesquisarUGSuperior
	 * @return
	 */
	public List<PerspectivaMapaEstrategico> findByUnidadeGerencialThroughMatrizFCS(UnidadeGerencial unidadeGerencial, boolean pesquisarUGSuperior) {
		List<PerspectivaMapaEstrategico> listaPerspectivaMapaEstrategico = perspectivaMapaEstrategicoDAO.findByUnidadeGerencialThroughMatrizFCS(unidadeGerencial);
		if (listaPerspectivaMapaEstrategico == null || listaPerspectivaMapaEstrategico.isEmpty() && pesquisarUGSuperior) {
			// Percorre as UGs superiores até encontrar alguma que possua MatrizFCS cadastrada.
			List<UnidadeGerencial> ugPais = unidadeGerencialService.findAllParents(unidadeGerencial);
			if (ugPais != null && !ugPais.isEmpty()) {
				for (UnidadeGerencial ugPai : ugPais) {
					listaPerspectivaMapaEstrategico = perspectivaMapaEstrategicoDAO.findByUnidadeGerencialThroughMatrizFCS(ugPai);
					if (listaPerspectivaMapaEstrategico != null && !listaPerspectivaMapaEstrategico.isEmpty()) {
						break;
					}
				}
			}
		}
		return listaPerspectivaMapaEstrategico;		
	}
	
	@Override
	public void delete(PerspectivaMapaEstrategico bean) {
		
		List<ObjetivoMapaEstrategico> listaObjetivoMapaEstrategico;
		if (bean.getListaObjetivoMapaEstrategico() != null) {
			listaObjetivoMapaEstrategico = bean.getListaObjetivoMapaEstrategico();
		}
		else {
			listaObjetivoMapaEstrategico = objetivoMapaEstrategicoService.findByPerspectivaMapaEstrategico(bean);
		}
		
		if (listaObjetivoMapaEstrategico != null && !listaObjetivoMapaEstrategico.isEmpty()) {
			for (ObjetivoMapaEstrategico objetivoMapaEstrategico : listaObjetivoMapaEstrategico) {
				List<MatrizFCS> listaMatrizFCS = matrizFCSService.findByUnidadeGerencialObjetivoEstrategico(null, objetivoMapaEstrategico);
				if (listaMatrizFCS != null && !listaMatrizFCS.isEmpty()) {
					throw new GeplanesException("Não foi possível excluir o objetivo estratégico '" + objetivoMapaEstrategico.getDescricao() + "', pois existe(m) registro(s) de Matriz de Iniciativas x Fatores Críticos de Sucesso vinculados.");
				}				
			}
		}
		super.delete(bean);
	}
	
	/**
	 * Carrega a perspectiva do mapa estratégico com a unidade gerencial à qual está vinculada.
	 * 
	 * @param perspectivaMapaEstrategico
	 * @return
	 */
	public PerspectivaMapaEstrategico loadWithUnidadeGerencial(PerspectivaMapaEstrategico perspectivaMapaEstrategico) {
		return perspectivaMapaEstrategicoDAO.loadWithUnidadeGerencial(perspectivaMapaEstrategico);
	}
	
	/**
	 * Monta a árvore do mapa estratégico com somente as perspectivas e objetivos estratégicos que possuem indicadores vinculados.
	 * 
	 * @param unidadeGerencial
	 * @param listarAcompanhamento
	 * @param listarAnexo
	 * @param listarAnomalia
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<PerspectivaMapaEstrategico> montaArvorePlanejamentoEstrategico(UnidadeGerencial unidadeGerencial, boolean listarAcompanhamento, boolean listarAnexo, boolean listarAnomalia) {
		if (unidadeGerencial == null) {
			return null;
		}
		
		Map<PerspectivaMapaEstrategico, Map<ObjetivoMapaEstrategico,List<Indicador>>> mapaPerspectiva = new HashMap<PerspectivaMapaEstrategico, Map<ObjetivoMapaEstrategico,List<Indicador>>>();
		Map<ObjetivoMapaEstrategico,List<Indicador>> mapaObjetivo;
		List<Indicador> listaIndicadorMapa = null;
		PerspectivaMapaEstrategico perspectiva;
		ObjetivoMapaEstrategico objetivo;
		
		// Busca todos os indicadores (exceto os cancelados) vinculados à unidade gerencial
		List<Indicador> listaIndicador = indicadorService.findByUnidadeGerencialObjetivoEstrategico(unidadeGerencial, null, false, true, listarAnexo, listarAnomalia);
		
		// Percorre os indicadores e monta um mapa com a hierarquia: Perspectiva -> Objetivo Estratégico -> Indicador
		if (listaIndicador != null && !listaIndicador.isEmpty()) {
			for (Indicador indicador : listaIndicador) {
				perspectiva = indicador.getObjetivoMapaEstrategico().getPerspectivaMapaEstrategico();
				objetivo = indicador.getObjetivoMapaEstrategico();
				
				mapaObjetivo = mapaPerspectiva.get(perspectiva);
				if (mapaObjetivo == null) {
					mapaObjetivo = new HashMap<ObjetivoMapaEstrategico, List<Indicador>>();
					listaIndicadorMapa = new ArrayList<Indicador>();
				}
				else {
					listaIndicadorMapa = mapaObjetivo.get(objetivo);
					if (listaIndicadorMapa == null) {
						listaIndicadorMapa = new ArrayList<Indicador>();
					}
				}

				listaIndicadorMapa.add(indicador);
				mapaObjetivo.put(objetivo, listaIndicadorMapa);
				mapaPerspectiva.put(perspectiva, mapaObjetivo);
			}
		}
		
		// Percorre os mapa e preenche as listas mantendo a hierarquia
		List<PerspectivaMapaEstrategico> listaPerspectivaMapaEstrategico = null;
		List<ObjetivoMapaEstrategico> listaObjetivoMapaEstrategico = null;
		
		if (mapaPerspectiva != null) {
			listaPerspectivaMapaEstrategico = new ArrayList<PerspectivaMapaEstrategico>();
			Iterator<PerspectivaMapaEstrategico> itPerspectiva = mapaPerspectiva.keySet().iterator();
			while (itPerspectiva.hasNext()) {
				perspectiva = itPerspectiva.next();
				mapaObjetivo = mapaPerspectiva.get(perspectiva);
				
				if (mapaObjetivo != null) {
					listaObjetivoMapaEstrategico = new ArrayList<ObjetivoMapaEstrategico>();
					Iterator<ObjetivoMapaEstrategico> itObjetivo = mapaObjetivo.keySet().iterator();
					while (itObjetivo.hasNext()) {
						objetivo = itObjetivo.next();
						listaIndicadorMapa = mapaObjetivo.get(objetivo);
						
						objetivo.setListaIndicador(listaIndicadorMapa);
						listaObjetivoMapaEstrategico.add(objetivo);
					}
				}
				
				// Ordena a lista de objetivos
				Collections.sort(listaObjetivoMapaEstrategico, new BeanComparator("id"));
				
				perspectiva.setListaObjetivoMapaEstrategico(listaObjetivoMapaEstrategico);
				
				
				listaPerspectivaMapaEstrategico.add(perspectiva);
			}
			
			// Ordena a lista de perspectivas
			Collections.sort(listaPerspectivaMapaEstrategico, new BeanComparator("ordem"));			
		}
		
		return listaPerspectivaMapaEstrategico;
	}	
}


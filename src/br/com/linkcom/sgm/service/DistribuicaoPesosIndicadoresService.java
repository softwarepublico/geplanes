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

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.validation.BindException;

import br.com.linkcom.neo.bean.annotation.Bean;
import br.com.linkcom.neo.controller.MessageType;
import br.com.linkcom.neo.core.web.WebRequestContext;
import br.com.linkcom.neo.util.CollectionsUtil;
import br.com.linkcom.sgm.beans.DistribuicaoPesosIndicadores;
import br.com.linkcom.sgm.beans.Indicador;
import br.com.linkcom.sgm.beans.Iniciativa;
import br.com.linkcom.sgm.beans.ObjetivoMapaEstrategico;
import br.com.linkcom.sgm.beans.PainelIndicadorFiltro;
import br.com.linkcom.sgm.beans.PerspectivaMapaEstrategico;
import br.com.linkcom.sgm.beans.PlanoGestao;
import br.com.linkcom.sgm.beans.UnidadeGerencial;
import br.com.linkcom.sgm.beans.enumeration.StatusIndicadorEnum;
import br.com.linkcom.sgm.exception.GeplanesException;



/**
 * @author Rodrigo Duarte
 *
 */

@Bean
public class DistribuicaoPesosIndicadoresService {
	private IndicadorService indicadorService;
	private PlanoGestaoService planoGestaoService;
	private UsuarioService usuarioService;
	private PerspectivaMapaEstrategicoService perspectivaMapaEstrategicoService;
	private ObjetivoMapaEstrategicoService objetivoMapaEstrategicoService;
	private IniciativaService iniciativaService;
	private PainelIndicadorFiltroService painelIndicadorFiltroService;
	
	public void setIndicadorService(IndicadorService indicadorService) {this.indicadorService = indicadorService;}
	public void setPlanoGestaoService(PlanoGestaoService planoGestaoService) {this.planoGestaoService = planoGestaoService;}
	public void setUsuarioService(UsuarioService usuarioService) {this.usuarioService = usuarioService;}
	public void setPerspectivaMapaEstrategicoService(PerspectivaMapaEstrategicoService perspectivaMapaEstrategicoService) {this.perspectivaMapaEstrategicoService = perspectivaMapaEstrategicoService;}
	public void setObjetivoMapaEstrategicoService(ObjetivoMapaEstrategicoService objetivoMapaEstrategicoService) {this.objetivoMapaEstrategicoService = objetivoMapaEstrategicoService;}
	public void setIniciativaService(IniciativaService iniciativaService) {this.iniciativaService = iniciativaService;}
	public void setPainelIndicadorFiltroService(PainelIndicadorFiltroService painelIndicadorFiltroService) {this.painelIndicadorFiltroService = painelIndicadorFiltroService;}
	
	public void configuraParametrosTela(WebRequestContext request, DistribuicaoPesosIndicadores bean) {
		Boolean isUsuarioAdmin         = usuarioService.isUsuarioLogadoAdmin();
		Boolean isUsuarioResponsavelUG = usuarioService.isUsuarioLogadoResponsavelUG(bean.getUnidadeGerencial());
		Boolean isUsuarioApoioUG       = usuarioService.isUsuarioLogadoApoioUG(bean.getUnidadeGerencial());
		Boolean isUsuarioParticipanteUGAncestral = usuarioService.isUsuarioLogadoParticipanteUGAncestral(bean.getUnidadeGerencial());
		Boolean isUsuarioParticipanteUGSubordinada = usuarioService.isUsuarioLogadoParticipanteUGSubordinada(bean.getUnidadeGerencial());
		
		Boolean usuarioTemPermissaoLeitura = usuarioTemPermissaoLeitura(isUsuarioAdmin, isUsuarioResponsavelUG, isUsuarioApoioUG, isUsuarioParticipanteUGAncestral, isUsuarioParticipanteUGSubordinada);
		Boolean usuarioTemPermissaoEscrita = usuarioTemPermissaoEscrita(isUsuarioAdmin, isUsuarioResponsavelUG);
		Boolean usuarioPodeAlterar         = usuarioPodeAlterar(isUsuarioAdmin, isUsuarioResponsavelUG, bean.getPlanoGestao());
		Boolean dtLimCrIndNaoExp           = planoGestaoService.dataLimiteCriacaoMetasIndicadoresNaoExpirada(bean.getPlanoGestao());
		
		if(!usuarioTemPermissaoLeitura) {
			request.addMessage("Você não tem permissão para acessar os dados dessa Unidade Gerencial.", MessageType.ERROR);
		}
		else {
			if (!usuarioTemPermissaoEscrita) {
				request.addMessage("Você não tem permissão para criar/alterar os indicadores dessa Unidade Gerencial.", MessageType.ERROR);				
			}
			else {
				if (!dtLimCrIndNaoExp) {
					if (isUsuarioAdmin) {
						request.addMessage("A data limite para criação de indicadores/iniciativas/planos de ação está ultrapassada.", MessageType.WARN);
					}
					else {
						request.addMessage("A data limite para criação de indicadores/iniciativas/planos de ação está ultrapassada.", MessageType.ERROR);
					}
				}
			}
		}
		
		request.setAttribute("showTela", usuarioTemPermissaoLeitura);
		request.setAttribute("showButton", usuarioTemPermissaoEscrita && usuarioPodeAlterar);		
		
		if (bean.getListaPerspectivaMapaEstrategico().isEmpty()) {
			request.setAttribute("esconderSalvar", new Boolean(true));
		}
	}
	
	/**
	 * Verifica se o usuário tem permissão de leitura aos registros de distribuição de pesos e indicadores. Somente terão essa permissão
	 * os administradores e os usuários vinculados à UG da meta, sejam eles responsáveis ou de apoio, e também os usuários vinculados às UGs
	 * ancestrais ou subordinadas da UG em questão
	 * 
	 * @author Rodrigo Alvarenga 
	 * @param isUsuarioAdmin
	 * @param isUsuarioResponsavelUG
	 * @param isUsuarioApoioUG
	 * @param isUsuarioVinculadoUGAncestral
	 * @param isUsuarioVinculadoUGSubordinada
	 * @return verdadeiro ou falso
	 */	
	private Boolean usuarioTemPermissaoLeitura(Boolean isUsuarioAdmin, Boolean isUsuarioResponsavelUG, Boolean isUsuarioApoioUG, Boolean isUsuarioVinculadoUGAncestral, Boolean isUsuarioVinculadoUGSubordinada) {
		if (isUsuarioAdmin) {
			return true;
		}		
		if (isUsuarioResponsavelUG) {
			return true;
		}		
		if (isUsuarioApoioUG) {
			return true;
		}
		if (isUsuarioVinculadoUGAncestral) {
			return true;
		}
		if (isUsuarioVinculadoUGSubordinada) {
			return true;
		}
		return false;
	}	
	
	/**
	 * Verifica se o usuário tem permissão de gravação dos registros de distribuição de pesos e indicadores. Somente terão essa permissão
	 * os administradores e os usuários responsáveis pela UG da meta
	 * 
	 * @author Rodrigo Alvarenga 
	 * @param isUsuarioAdmin
	 * @param isUsuarioResponsavelUG
	 * @return verdadeiro ou falso
	 */		
	public Boolean usuarioTemPermissaoEscrita(Boolean isUsuarioAdmin, Boolean isUsuarioResponsavelUG) {		
		if (isUsuarioAdmin) {
			return true;
		}		
		if (isUsuarioResponsavelUG) {
			return true;
		}		
		return false;		
	}	
	
	/**
	 * Verifica se o usuário pode criar/alterar um registro de distribuição de pesos e indicadores. Somente terão essa permissão
	 * os administradores e os usuários responsáveis pela UG da meta, desde que a data limite para criação de metas e indicadores 
	 * não esteja expirada
	 * 
	 * @author Rodrigo Alvarenga 
	 * @param isUsuarioAdmin
	 * @param isUsuarioResponsavelUG
	 * @param planoGestao
	 * @return verdadeiro ou falso
	 */		
	private Boolean usuarioPodeAlterar(Boolean isUsuarioAdmin, Boolean isUsuarioResponsavelUG, PlanoGestao planoGestao) {		
		if (isUsuarioAdmin) {
			return true;
		}		
		if (isUsuarioResponsavelUG && planoGestaoService.dataLimiteCriacaoMetasIndicadoresNaoExpirada(planoGestao)) {
			return true;
		}		
		return false;		
	}
	
	/**
	 * Obtém a lista de perspectivas, estratégias, iniciativas 
	 * e indicadores de uma determinada Unidade Gerencial.
	 * 
	 * @param unidadeGerencial
	 * @return
	 */
	public List<PerspectivaMapaEstrategico> carregaElementosPainelIndicador(UnidadeGerencial unidadeGerencial) {
		
		// Busca as perspectivas possíveis para a UG
		List<PerspectivaMapaEstrategico> listaPerspectivaMapaEstrategico = perspectivaMapaEstrategicoService.findByUnidadeGerencialThroughMapaEstrategico(unidadeGerencial,true,"perspectivaMapaEstrategico.ordem");
		List<ObjetivoMapaEstrategico> listaObjetivoMapaEstrategico;		
		
		// Busca os objetivos estratégicos que estão disponíveis para visualização
		List<PainelIndicadorFiltro> listaPainelIndicadorFiltro = painelIndicadorFiltroService.findByUnidadeGerencial(unidadeGerencial);
		
		// Se não existir nenhuma informação no filtro exibe todos os objetivos estratégicos possíveis para a UG.
		if (listaPainelIndicadorFiltro != null && !listaPainelIndicadorFiltro.isEmpty()) {
			boolean inseriuPerspectiva;
			boolean filtroVazio = true;
			List<PerspectivaMapaEstrategico> listaPerspectivaMapaEstrategicoFiltrada = new ArrayList<PerspectivaMapaEstrategico>();
			PerspectivaMapaEstrategico perspectivaMapaEstrategicoFiltrada = null;
			
			if (listaPerspectivaMapaEstrategico != null && !listaPerspectivaMapaEstrategico.isEmpty()) {
				for (PerspectivaMapaEstrategico perspectivaMapaEstrategico : listaPerspectivaMapaEstrategico) {
					inseriuPerspectiva = false;
					listaObjetivoMapaEstrategico = objetivoMapaEstrategicoService.findByUnidadeGerencialPerspectivaThroughMapaEstrategico(unidadeGerencial, perspectivaMapaEstrategico, true, true, false, false, false, "objetivoMapaEstrategico.id");
					
					if (listaObjetivoMapaEstrategico != null && !listaObjetivoMapaEstrategico.isEmpty()) {
						for (ObjetivoMapaEstrategico objetivoMapaEstrategico : listaObjetivoMapaEstrategico) {
							for (PainelIndicadorFiltro painelIndicadorFiltro : listaPainelIndicadorFiltro) {								
								if (objetivoMapaEstrategico.equals(painelIndicadorFiltro.getObjetivoMapaEstrategico())) {
									filtroVazio = false;
									painelIndicadorFiltro.setExcluir(false);
									
									if (!inseriuPerspectiva) {
										perspectivaMapaEstrategicoFiltrada = new PerspectivaMapaEstrategico();
										try {
											BeanUtils.copyProperties(perspectivaMapaEstrategicoFiltrada, perspectivaMapaEstrategico);
										} catch (IllegalAccessException e) {
											throw new GeplanesException(e.getMessage());
										} catch (InvocationTargetException e) {
											throw new GeplanesException(e.getMessage());
										}
										listaPerspectivaMapaEstrategicoFiltrada.add(perspectivaMapaEstrategicoFiltrada);
										
										perspectivaMapaEstrategicoFiltrada.setListaObjetivoMapaEstrategico(new ArrayList<ObjetivoMapaEstrategico>());
										perspectivaMapaEstrategicoFiltrada.getListaObjetivoMapaEstrategico().add(objetivoMapaEstrategico);
										
										inseriuPerspectiva = true;
									}
									else {
										perspectivaMapaEstrategicoFiltrada.getListaObjetivoMapaEstrategico().add(objetivoMapaEstrategico);
									}
									break;
								}
							}
						}
						perspectivaMapaEstrategico.setListaObjetivoMapaEstrategico(listaObjetivoMapaEstrategico);
					}
				}
				
				// Remove do filtro os objetivos estratégicos que não mais se aplicam à UG
				for (PainelIndicadorFiltro painelIndicadorFiltro : listaPainelIndicadorFiltro) {
					if (painelIndicadorFiltro.isExcluir()) {
						painelIndicadorFiltroService.delete(painelIndicadorFiltro);
					}
				}
			}
			// Somente se existir algum objetivo estratégico no filtro é que ele será aplicado.
			if (!filtroVazio) {
				listaPerspectivaMapaEstrategico = listaPerspectivaMapaEstrategicoFiltrada;
			}
		}
		else {
			if (listaPerspectivaMapaEstrategico != null && !listaPerspectivaMapaEstrategico.isEmpty()) {
				for (PerspectivaMapaEstrategico perspectivaMapaEstrategico : listaPerspectivaMapaEstrategico) {
					listaObjetivoMapaEstrategico = objetivoMapaEstrategicoService.findByUnidadeGerencialPerspectivaThroughMapaEstrategico(unidadeGerencial, perspectivaMapaEstrategico, true, true, false, false, false, "objetivoMapaEstrategico.id");
					perspectivaMapaEstrategico.setListaObjetivoMapaEstrategico(listaObjetivoMapaEstrategico);
				}
			}
		}
		
		return listaPerspectivaMapaEstrategico;
	}	
	
	/**
	 * Obtém a lista de perspectivas, estratégias, iniciativas 
	 * e indicadores de uma determinada Unidade Gerencial.
	 * 
	 * @param unidadeGerencial
	 * @param listarAcompanhamento
	 * @param listarAnexo
	 * @param listarAnomalia
	 * @return
	 */
	public List<PerspectivaMapaEstrategico> obtemListaPerspectivaObjetivoEstrategicoIniciativaIndicador(UnidadeGerencial unidadeGerencial, boolean listarAcompanhamento, boolean listarAnexo, boolean listarAnomalia) {
		
		List<PerspectivaMapaEstrategico> listaPerspectivaMapaEstrategico = perspectivaMapaEstrategicoService.findByUnidadeGerencialThroughMapaEstrategico(unidadeGerencial,true);
		List<ObjetivoMapaEstrategico> listaObjetivoMapaEstrategico;
		
		if (listaPerspectivaMapaEstrategico != null && !listaPerspectivaMapaEstrategico.isEmpty()) {
			for (PerspectivaMapaEstrategico perspectivaMapaEstrategico : listaPerspectivaMapaEstrategico) {
				listaObjetivoMapaEstrategico = objetivoMapaEstrategicoService.findByUnidadeGerencialPerspectivaThroughMapaEstrategico(unidadeGerencial, perspectivaMapaEstrategico, true, true, listarAcompanhamento, listarAnexo, listarAnomalia);
				perspectivaMapaEstrategico.setListaObjetivoMapaEstrategico(listaObjetivoMapaEstrategico);
			}
		}
		
		return listaPerspectivaMapaEstrategico;
	}
	
	/**
	 * Para um mesmo objetivo estratégico, a soma 
	 * dos pesos dos indicadores deve ser igual a 100.
	 * 
	 * @param bean
	 * @param errors
	 * @return
	 */
	public Boolean confereSomaPesoIndicadores(DistribuicaoPesosIndicadores bean, BindException errors) {
	
		int somaPesos;
		
		if (bean.getListaPerspectivaMapaEstrategico() != null && !bean.getListaPerspectivaMapaEstrategico().isEmpty()) {
			for (PerspectivaMapaEstrategico perspectivaMapaEstrategico : bean.getListaPerspectivaMapaEstrategico()) {
				if (perspectivaMapaEstrategico.getListaObjetivoMapaEstrategico() != null && !perspectivaMapaEstrategico.getListaObjetivoMapaEstrategico().isEmpty()) {
					for (ObjetivoMapaEstrategico objetivoMapaEstrategico : perspectivaMapaEstrategico.getListaObjetivoMapaEstrategico()) {
						somaPesos=0;

						/** soma pesos indicadores **/
						List<Indicador> listaIndicadores = objetivoMapaEstrategico.getListaIndicador();
						if(listaIndicadores != null) {
							for (Indicador indicador : listaIndicadores) {
								 if (indicador.getStatus() == null || (!indicador.getStatus().equals(StatusIndicadorEnum.CANCELADO))) {
									if (indicador.getPeso() != null) {
										somaPesos += indicador.getPeso();
									}
									else {
										errors.reject("", "O peso do indicador "+indicador.getNome()+" não pode estar em branco");
										return false;
									}
								 }
							}
						}
						
						if (somaPesos !=100 && somaPesos != 0) {
							errors.reject("", "No objetivo estratégico '" + objetivoMapaEstrategico.getDescricao() + "' a soma dos pesos dos indicadores deve ser igual a 100.");
							return false;
						}						
					}
				}
			}			
		}
		return true;
	}
	
	public void salvar(DistribuicaoPesosIndicadores bean) {		
	
		List<PerspectivaMapaEstrategico> listaPerspectivaMapaEstrategico = bean.getListaPerspectivaMapaEstrategico();
		List<Iniciativa> listaIniciativa;
		List<Indicador> listaIndicador;

		if (listaPerspectivaMapaEstrategico != null && !listaPerspectivaMapaEstrategico.isEmpty()) {
			for (PerspectivaMapaEstrategico perspectivaEstrategico : listaPerspectivaMapaEstrategico) {
				if (perspectivaEstrategico.getListaObjetivoMapaEstrategico() != null && !perspectivaEstrategico.getListaObjetivoMapaEstrategico().isEmpty()) {
					for (ObjetivoMapaEstrategico objetivoMapaEstrategico : perspectivaEstrategico.getListaObjetivoMapaEstrategico()) {
						
						// Salva as iniciativas
						listaIniciativa = objetivoMapaEstrategico.getListaIniciativa();
						if (listaIniciativa != null) {
							iniciativaService.atualizaIniciativas(listaIniciativa, bean.getUnidadeGerencial(), objetivoMapaEstrategico);
							iniciativaService.deleteWhereNotIn(bean.getUnidadeGerencial(), objetivoMapaEstrategico, CollectionsUtil.listAndConcatenate(listaIniciativa, "id", ","));
						}
						
						// Salva os indicadores
						listaIndicador = objetivoMapaEstrategico.getListaIndicador();
						if (listaIndicador != null) {
							indicadorService.atualizaIndicadores(listaIndicador, bean.getUnidadeGerencial(), objetivoMapaEstrategico);
							indicadorService.deleteWhereNotIn(bean.getUnidadeGerencial(), objetivoMapaEstrategico, CollectionsUtil.listAndConcatenate(listaIndicador, "id", ","));
						}
					}
				}
			}
		}
	}
}

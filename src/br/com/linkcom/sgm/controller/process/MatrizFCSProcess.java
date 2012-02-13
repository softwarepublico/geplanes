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
package br.com.linkcom.sgm.controller.process;

import java.util.ArrayList;
import java.util.List;

import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;

import br.com.linkcom.neo.authorization.process.ProcessAuthorizationModule;
import br.com.linkcom.neo.bean.annotation.Bean;
import br.com.linkcom.neo.controller.Command;
import br.com.linkcom.neo.controller.Controller;
import br.com.linkcom.neo.controller.DefaultAction;
import br.com.linkcom.neo.controller.Input;
import br.com.linkcom.neo.controller.MessageType;
import br.com.linkcom.neo.controller.MultiActionController;
import br.com.linkcom.neo.core.web.WebRequestContext;
import br.com.linkcom.sgm.beans.ItemFatorAvaliacao;
import br.com.linkcom.sgm.beans.MatrizFCS;
import br.com.linkcom.sgm.beans.MatrizFCSFator;
import br.com.linkcom.sgm.beans.MatrizFCSIniciativa;
import br.com.linkcom.sgm.beans.MatrizFCSIniciativaFator;
import br.com.linkcom.sgm.beans.UnidadeGerencial;
import br.com.linkcom.sgm.controller.filtro.MatrizFCSFiltro;
import br.com.linkcom.sgm.exception.GeplanesException;
import br.com.linkcom.sgm.service.FatorAvaliacaoService;
import br.com.linkcom.sgm.service.ItemFatorAvaliacaoService;
import br.com.linkcom.sgm.service.MatrizFCSFatorService;
import br.com.linkcom.sgm.service.MatrizFCSIniciativaService;
import br.com.linkcom.sgm.service.MatrizFCSService;
import br.com.linkcom.sgm.service.PlanoGestaoService;
import br.com.linkcom.sgm.service.UnidadeGerencialService;
import br.com.linkcom.sgm.service.UsuarioService;
import br.com.linkcom.sgm.util.FiltroUtils;


@Bean
@Controller(path="/sgm/process/MatrizFCS", authorizationModule=ProcessAuthorizationModule.class)
public class MatrizFCSProcess extends MultiActionController {

	private ItemFatorAvaliacaoService itemFatorAvaliacaoService;
	private MatrizFCSService matrizFCSService;
	private MatrizFCSFatorService matrizFCSFatorService;
	private MatrizFCSIniciativaService matrizFCSIniciativaService;
	private FatorAvaliacaoService fatorAvaliacaoService;
	private UnidadeGerencialService unidadeGerencialService;
	private PlanoGestaoService planoGestaoService;
	private UsuarioService usuarioService;
	//private IndicadorService indicadorService;
	//private ObjetivoMapaEstrategicoService objetivoMapaEstrategicoService;
	//private IniciativaService iniciativaService;
	
	public void setItemFatorAvaliacaoService(ItemFatorAvaliacaoService itemFatorAvaliacaoService) {
		this.itemFatorAvaliacaoService = itemFatorAvaliacaoService;
	}
	
	public void setMatrizFCSService(MatrizFCSService matrizFCSService) {
		this.matrizFCSService = matrizFCSService;
	}
	
	public void setMatrizFCSIniciativaService(MatrizFCSIniciativaService matrizFCSIniciativaService) {
		this.matrizFCSIniciativaService = matrizFCSIniciativaService;
	}
	
	public void setMatrizFCSFatorService(MatrizFCSFatorService matrizFCSFatorService) {
		this.matrizFCSFatorService = matrizFCSFatorService;
	}
	
	public void setFatorAvaliacaoService(FatorAvaliacaoService fatorAvaliacaoService) {
		this.fatorAvaliacaoService = fatorAvaliacaoService;
	}
	
	public void setUnidadeGerencialService(UnidadeGerencialService unidadeGerencialService) {
		this.unidadeGerencialService = unidadeGerencialService;
	}
	
	public void setPlanoGestaoService(PlanoGestaoService planoGestaoService) {
		this.planoGestaoService = planoGestaoService;
	}
	
	public void setUsuarioService(UsuarioService usuarioService) {
		this.usuarioService = usuarioService;
	}
	
//	public void setIndicadorService(IndicadorService indicadorService) {
//		this.indicadorService = indicadorService;
//	}
//	
//	public void setObjetivoMapaEstrategicoService(ObjetivoMapaEstrategicoService objetivoMapaEstrategicoService) {
//		this.objetivoMapaEstrategicoService = objetivoMapaEstrategicoService;
//	}
//	
//	public void setIniciativaService(IniciativaService iniciativaService) {
//		this.iniciativaService = iniciativaService;
//	}
	
	/**
	 * Responsável pela ação padrão, faz com que os componentes ano da gestão e Unidade gerencial 
	 * sejam preenchidas automaticamente
	 * @see FiltroUtils#preencheFiltroPlanoGestaoUnidadeUsuario(Object)
	 * 
	 * @param request
	 * @param filtro
	 * @return
	 * @author Pedro Gonçalves
	 */
	@DefaultAction	
    public ModelAndView executar(WebRequestContext request, MatrizFCSFiltro filtro) {
		
		if (!"true".equals(request.getParameter("reload"))) {
			FiltroUtils.preencheFiltroPlanoGestaoUnidadeUsuario(filtro);		
		}
		
		if (filtro.getPlanoGestao() != null && filtro.getUnidadeGerencial() != null) {
			UnidadeGerencial unidadeGerencial = unidadeGerencialService.loadWithPlanoGestao(filtro.getUnidadeGerencial());

/* 
 * Comentado, pois os objetivos estratégicos no Painel de Indicadores serão vinculados ao Mapa Estratégico e não à Matriz.
 *			
			// Caso ainda não exista a matriz de iniciativas x fcs cadastrada para a UG,
			// verifica se já foi cadastrado algum indicador ou plano de ação para as iniciativas da UG.
			// Caso afirmativo, o cadastro foi feito baseado na matriz de uma UG superior.
			// Nesse caso, deve-se impedir o cadastro da matriz para a UG em questão.
			List<MatrizFCS> listaMatrizFCS = matrizFCSService.findByUnidadeGerencialObjetivoEstrategico(unidadeGerencial, null);
			if (listaMatrizFCS == null || listaMatrizFCS.isEmpty()) {
				List<Indicador> listaIndicador = indicadorService.findByUnidadeGerencial(unidadeGerencial);
				if (listaIndicador != null && !listaIndicador.isEmpty()) {
					request.setAttribute("SEMPERMISSAO", true);
					request.addMessage("Não é permitido o cadastro da matriz de iniciativas x fcs para essa unidade gerencial, pois já existe(m) indicador(es) cadastrados para os objetivos estratégicos da matriz de uma unidade gerencial superior.", MessageType.ERROR);
					return new ModelAndView("process/matrizFCS","filtro",filtro);
				}
			}
*/			
			
			boolean podeCadastrarMatriz = unidadeGerencial.getPermitirMatrizFcs() != null ? unidadeGerencial.getPermitirMatrizFcs() : false;
			boolean dtLimCrMatrizExpirada = !(planoGestaoService.dataLimiteCriacaoMatrizFcsNaoExpirada(unidadeGerencial.getPlanoGestao()));
			boolean usuarioLogadoParticipanteUG = usuarioService.isUsuarioLogadoParticipanteUG(unidadeGerencial);
			boolean usuarioLogadoParticipanteUGAncestral = usuarioService.isUsuarioLogadoParticipanteUGAncestral(unidadeGerencial);
			boolean usuarioLogadoIsAdmin = usuarioService.isUsuarioLogadoAdmin();
			
			if (podeCadastrarMatriz) {
				if (usuarioLogadoParticipanteUG || usuarioLogadoParticipanteUGAncestral || usuarioLogadoIsAdmin) {
					if (dtLimCrMatrizExpirada) {
						if (usuarioLogadoIsAdmin) {
							request.addMessage("A data limite para criação da matriz de iniciativas x fcs está ultrapassada.", MessageType.WARN);
						}
						else {
							request.addMessage("A data limite para criação da matriz de iniciativas x fcs está ultrapassada.", MessageType.ERROR);
							request.setAttribute("HIDEBOTAOSALVAR", Boolean.TRUE);							
						}
					}
					else {
						request.setAttribute("SEMPERMISSAO", false);
					}
				}
				else {
					request.setAttribute("SEMPERMISSAO", true);
					request.addMessage("Você não tem permissão para acessar os dados dessa unidade gerencial.", MessageType.ERROR);
				}
			}
			else {
				request.setAttribute("SEMPERMISSAO", true);
				request.addMessage("Não é permitido o cadastro da matriz de iniciativas x fcs para essa unidade gerencial.", MessageType.ERROR);
			}
		}		
		
		
		request.setAttribute("fatorAvaliacaoItens", fatorAvaliacaoService.find(Boolean.TRUE));
		return new ModelAndView("process/matrizFCS","filtro",filtro);
	}
	
	/**
	 * Salva a matriz MFS no banco de dados
	 * @see MatrizFCSService#salvaMatriz(MatrizFCS)
	 * 
	 * Conserta os parâmetros que o NEO não consegue bindar:
	 * @see #fixParams(WebRequestContext, MatrizFCSFiltro)
	 * 
	 * @param request
	 * @param filtro
	 * @return
	 * @throws Exception
	 * @author Pedro Gonçalves
	 */
	@Command(validate=true)
	@Input("error")	
	public ModelAndView salvar(WebRequestContext request, MatrizFCSFiltro filtro) throws Exception{
		
		//reseta os objetos
		filtro.getMatrizFCS().setPerspectivaMapaEstrategico(filtro.getPerspectivaMapaEstrategico());
		filtro.getMatrizFCS().setObjetivoMapaEstrategico(filtro.getObjetivoMapaEstrategico());
		filtro.getMatrizFCS().setFatorAvaliacao(filtro.getFatorAvaliacao());
		filtro.getMatrizFCS().setUnidadeGerencial(filtro.getUnidadeGerencial());
		
		try {
			matrizFCSService.salvaMatriz(filtro, true);
			request.addMessage("Matriz salva com sucesso!");
		}
		catch (GeplanesException e) {
			request.addError(e.getMessage());
		}
		
		MatrizFCSFiltro novaMatriz = new MatrizFCSFiltro();
		novaMatriz.setUnidadeGerencial(filtro.getUnidadeGerencial());
		request.setAttribute("vindoDaAcaoSalvar", true);
		return this.executar(request, novaMatriz);		
	}
	
	@Command(validate=true)
	@Input("error")
	public ModelAndView excluir(WebRequestContext request, MatrizFCSFiltro filtro) throws Exception {
		MatrizFCS matrizFCS = filtro.getMatrizFCS();
		
		if (matrizFCS != null) {
			try {
				matrizFCSService.delete(matrizFCS);
				request.addMessage("Matriz excluída com sucesso!");
			}
			catch (GeplanesException e) {
				request.addError(e.getMessage());
			}
		}
		
		MatrizFCSFiltro novaMatriz = new MatrizFCSFiltro();
		novaMatriz.setUnidadeGerencial(filtro.getUnidadeGerencial());
		request.setAttribute("vindoDaAcaoSalvar", true);
		return this.executar(request, novaMatriz);
	}
	
	@Override
	protected void validate(Object obj, BindException errors, String acao) {
		MatrizFCSFiltro matrizFCSFiltro = (MatrizFCSFiltro) obj;
		MatrizFCS matrizFCS = matrizFCSFiltro.getMatrizFCS();
		
		if (matrizFCS != null) {
			if (acao.equals("excluir")) {
				
/* 
 * Comentado, pois os objetivos estratégicos no Painel de Indicadores serão vinculados ao Mapa Estratégico e não à Matriz.
 *  				
				// Antes de excluir, verifica se existe algum indicador vinculado ao objetivo estratégico da matriz. Se tiver, não pode excluir.
				List<Indicador> listaIndicador = indicadorService.findByUnidadeGerencialObjetivoEstrategico(matrizFCSFiltro.getUnidadeGerencial(), matrizFCSFiltro.getObjetivoMapaEstrategico(), false, false, false, false);
				if (listaIndicador != null && !listaIndicador.isEmpty()) {
					ObjetivoMapaEstrategico objetivoMapaEstrategico = objetivoMapaEstrategicoService.load(matrizFCSFiltro.getObjetivoMapaEstrategico());
					errors.reject("","Não foi possível excluir a matriz para o objetivo estratégico '" + objetivoMapaEstrategico.getDescricao() + "', pois existe(m) indicador(es) vinculado(s).");
				}
				
				// Antes de excluir, verifica se existe alguma iniciativa vinculado ao objetivo estratégico da matriz. Se tiver, não pode excluir.
				List<Iniciativa> listaIniciativa = iniciativaService.findByUnidadeGerencialObjetivoEstrategico(matrizFCSFiltro.getUnidadeGerencial(), matrizFCSFiltro.getObjetivoMapaEstrategico());
				if (listaIniciativa != null && !listaIniciativa.isEmpty()) {
					ObjetivoMapaEstrategico objetivoMapaEstrategico = objetivoMapaEstrategicoService.load(matrizFCSFiltro.getObjetivoMapaEstrategico());
					errors.reject("","Não foi possível excluir a matriz para o objetivo estratégico '" + objetivoMapaEstrategico.getDescricao() + "', pois existe(m) iniciativa(s) vinculada(s).");
				}
*/				
			}
			if (acao.equals("salvar")) {
				List<MatrizFCSIniciativa> listaMatrizFcsIniciativa = matrizFCS.getListaMatrizFcsIniciativa();
				List<MatrizFCSFator> listaMatrizFcsFator = matrizFCS.getListaMatrizFcsFator();
				boolean encontrou = false;
				
				List<MatrizFCSFator> listaFatorForDelete = new ArrayList<MatrizFCSFator>();
				List<MatrizFCSIniciativa> listaIniciativaOld = new ArrayList<MatrizFCSIniciativa>();
				List<MatrizFCSIniciativa> listaIniciativaForDelete = new ArrayList<MatrizFCSIniciativa>();
				
				if (matrizFCS.getId() != null) {
					// Busca as iniciativas do banco de dados, para verificar se alguma delas foi excluída.
					listaIniciativaOld = matrizFCSIniciativaService.findByMatrizFCS(matrizFCS);
								
					encontrou = false;
					for (MatrizFCSIniciativa matrizFCSIniciativaOld : listaIniciativaOld) {
						encontrou = false;
						for (MatrizFCSIniciativa matrizFCSIniciativaNova : listaMatrizFcsIniciativa) {
							if (matrizFCSIniciativaNova.getId() == null) { //passa para frente, este resultado não interessa
								continue;
							}
							else if (matrizFCSIniciativaOld.getId().equals(matrizFCSIniciativaNova.getId())) {
								encontrou = true;
								break;
							}
						}
						if (!encontrou) {
							listaIniciativaForDelete.add(matrizFCSIniciativaOld);
						}
					}			
					
					// Busca os fatores críticos de sucesso do banco de dados, para verificar se algum deles foi excluído.
					List<MatrizFCSFator> findByMatrizFcsOld = matrizFCSFatorService.findByMatrizFCS(matrizFCS);
					int idx = 0;
					for (MatrizFCSFator matrizFCSFatorOld : findByMatrizFcsOld) {
						encontrou = false;
						idx = 0;
						for (MatrizFCSFator matrizFCSFatorNova : listaMatrizFcsFator) {
							if (matrizFCSFatorNova.getId() == null) {//passa para frente, este resultado não interessa
								continue;
							}
							else if (matrizFCSFatorOld.getId().equals(matrizFCSFatorNova.getId())) {
								encontrou = true;
								break;
							}
						}
						if (!encontrou) {
							listaFatorForDelete.add(matrizFCSFatorOld);
							for (MatrizFCSFator matrizFCSFatorNova : listaMatrizFcsFator) {
								if(matrizFCSFatorOld.getId().equals(matrizFCSFatorNova.getId())) {
									listaMatrizFcsFator.remove(idx);
								}
								idx++;
							}
						}
					}
				}
				matrizFCSFiltro.setListaMatrizFCSFatorForDelete(listaFatorForDelete);
				matrizFCSFiltro.setListaMatrizFCSIniciativaForDelete(listaIniciativaForDelete);
			}
		}
		super.validate(obj, errors, acao);
	}
	
	public ModelAndView error(WebRequestContext request, MatrizFCSFiltro filtro) {
		return new ModelAndView("process/matrizFCS","filtro",filtro);
    }	
	
	/**
	 * Verifica se a matriz existe. Caso positivo, ele formata o objeto matrizFcs em formato JSON.
	 * Ele envia o sistema de pontuação e o id. O primeiro para preencher o combo, e o segundo para o hidden.
	 * 
	 * @see MatrizFCSService#loadInfoMatriz(br.com.linkcom.sgm.controller.report.filtro.MatrizFCSReportFiltro)
	 * 
	 * @param request
	 * @param filtro
	 * @return
	 * @throws Exception
	 * @author Pedro Gonçalves
	 */
	public ModelAndView checkMatriz(WebRequestContext request, MatrizFCSFiltro filtro) throws Exception{
		
		//reseta os objetos
		filtro.setMatrizFCS(new MatrizFCS());
		filtro.getMatrizFCS().setPerspectivaMapaEstrategico(filtro.getPerspectivaMapaEstrategico());
		filtro.getMatrizFCS().setObjetivoMapaEstrategico(filtro.getObjetivoMapaEstrategico());
		filtro.getMatrizFCS().setFatorAvaliacao(filtro.getFatorAvaliacao());
		filtro.getMatrizFCS().setUnidadeGerencial(filtro.getUnidadeGerencial());
		
		
		try {
			MatrizFCS loadInfoMatriz = matrizFCSService.loadInfoMatriz(filtro.getMatrizFCS());
			if(loadInfoMatriz != null) {
				request.getServletResponse().setCharacterEncoding("utf-8");
				request.getServletResponse().getWriter().write("{status:'ok',id:"+loadInfoMatriz.getId()+",fatorAvaliacao:'br.com.linkcom.sgm.beans.FatorAvaliacao[id="+loadInfoMatriz.getFatorAvaliacao().getId()+"]'}");
				return null;
			} else {
				request.getServletResponse().setCharacterEncoding("utf-8");
				request.getServletResponse().getWriter().write("{status:'error',type:'nexiste'}");
				return null;
			}
		} catch (Exception e) {e.printStackTrace();}
		
		request.getServletResponse().setCharacterEncoding("utf-8");
		request.getServletResponse().getWriter().write("{status:'error',type:'erro'}");

		return null;
	}
	
	/**
	 * Carrega a lista ItemFatorAvaliação para ser utilizado na tabela nos radios. Os dados são enviados
	 * em formato json.
	 * 
	 * @see ItemFatorAvaliacaoService#findByFatorAvaliacao(br.com.linkcom.sgm.beans.FatorAvaliacao)
	 * 
	 * @param request
	 * @param filtro
	 * @return
	 * @throws Exception
	 * @author Pedro Gonçalves
	 */
    public ModelAndView findItemFatorAvaliacao(WebRequestContext request, MatrizFCSFiltro filtro) throws Exception{
		List<ItemFatorAvaliacao> lista = itemFatorAvaliacaoService.findByFatorAvaliacao(filtro.getFatorAvaliacao());
		if(lista != null){
			StringBuilder sb = new StringBuilder();
			for (ItemFatorAvaliacao itemFatorAvaliacao : lista) {
				sb.append("{id:'br.com.linkcom.sgm.beans.ItemFatorAvaliacao[id="+itemFatorAvaliacao.getId()+"]',descricao:'"+itemFatorAvaliacao.getDescricao()+"'},");
			}
			String json = sb.toString();
			json = json.substring(0,json.length()-1);
			
			request.getServletResponse().setCharacterEncoding("utf-8");
			request.getServletResponse().getWriter().write("{lista:["+json+"]}");
			
			return null;
		}
		request.getServletResponse().getWriter().write("{lista:[]}");
		return null;
	}
    
    /**
     * Carrega os dados da matriz necessários para exibição da tabela.
     * Este método é chamado após o método de checagem for concluído.
     * @see #checkMatriz(WebRequestContext, MatrizFCSFiltro)
     * 
     * Carrega a lista de iniciativas
     * @see MatrizFCSIniciativaService#findByMatrizFCS(MatrizFCS)
     * 
     * Carrega a lista de fatores
     * @see MatrizFCSFatorService#findByMatrizFCS(MatrizFCS)
     * 
     * Como resultado é enviado um texto no formato JSON para carregar a tabela
     * 
     * @param request
     * @param filtro
     * @return
     * @throws Exception
     * @author Pedro Gonçalves
     */
    public ModelAndView getInfosMatriz(WebRequestContext request, MatrizFCSFiltro filtro) throws Exception{
		//MatrizFCS carregarMatriz = matrizFCSService.carregarMatriz(filtro.getMatrizFCS());
    	List<MatrizFCSIniciativa> findByMatrizFCS = matrizFCSIniciativaService.findByMatrizFCS(filtro.getMatrizFCS());
		if(findByMatrizFCS != null){
			
			StringBuilder sb = new StringBuilder();
			StringBuilder sbIniciativaFator = new StringBuilder();
			
			List<MatrizFCSFator> findByMatrizFcs = matrizFCSFatorService.findByMatrizFCS(filtro.getMatrizFCS());
			
			for (MatrizFCSFator matrizFCSFator : findByMatrizFcs) {
				sb.append("{id:"+matrizFCSFator.getId()+",descFator:'"+matrizFCSFator.getDescFator()+"'},");
			}
			String jsonListaMatrizFcsFator = sb.toString();
			if (jsonListaMatrizFcsFator.length() > 0) {
				jsonListaMatrizFcsFator = jsonListaMatrizFcsFator.substring(0,jsonListaMatrizFcsFator.length()-1);
				
				sb = new StringBuilder();
				for (MatrizFCSIniciativa matrizFCSIniciativa : findByMatrizFCS) {
					sb.append("{id:"+matrizFCSIniciativa.getId()+",descIniciativa:'"+matrizFCSIniciativa.getDescIniciativa()+"',prioritaria:"+matrizFCSIniciativa.getPrioritaria()+",");
					List<MatrizFCSIniciativaFator> listaMatrizFcsIniciativaFator = matrizFCSIniciativa.getListaMatrizFcsIniciativaFator();
					
					sbIniciativaFator = new StringBuilder();
					for (MatrizFCSIniciativaFator matrizFCSIniciativaFator : listaMatrizFcsIniciativaFator) {
						sbIniciativaFator.append("{id:"+matrizFCSIniciativaFator.getId()+",itemFatorAvaliacao:'br.com.linkcom.sgm.beans.ItemFatorAvaliacao[id="+matrizFCSIniciativaFator.getItemFatorAvaliacao().getId()+"]'},");
					}
					String jsonListaIniciativaFator = sbIniciativaFator.toString();
					jsonListaIniciativaFator = jsonListaIniciativaFator.substring(0,jsonListaIniciativaFator.length()-1);
					
					sb.append("listaMatrizFcsIniciativaFator:["+jsonListaIniciativaFator+"]},");
				}
				
				String jsonListaMatrizFCSIniciativa = sb.toString();
				if (jsonListaMatrizFCSIniciativa.length() > 0) {
					jsonListaMatrizFCSIniciativa = jsonListaMatrizFCSIniciativa.substring(0,jsonListaMatrizFCSIniciativa.length()-1);
					
					request.getServletResponse().setCharacterEncoding("utf-8");
					request.getServletResponse().getWriter().write("{listaMatrizFcsFator:["+jsonListaMatrizFcsFator+"],listaMatrizFCSIniciativa:["+jsonListaMatrizFCSIniciativa+"]}");
				}
			}
			
			return null;
		}
		request.getServletResponse().getWriter().write("{lista:[]}");
		return null;
	}
}

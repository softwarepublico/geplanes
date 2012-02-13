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
package br.com.linkcom.sgm.util.calculos;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.collections.FunctorException;

import br.com.linkcom.neo.core.standard.Neo;
import br.com.linkcom.sgm.beans.AcompanhamentoIndicador;
import br.com.linkcom.sgm.beans.Hierarquizavel;
import br.com.linkcom.sgm.beans.Indicador;
import br.com.linkcom.sgm.beans.ObjetivoMapaEstrategico;
import br.com.linkcom.sgm.beans.PerspectivaMapaEstrategico;
import br.com.linkcom.sgm.beans.PlanoGestao;
import br.com.linkcom.sgm.beans.UnidadeGerencial;
import br.com.linkcom.sgm.beans.DTO.ItemPainelControleDTO;
import br.com.linkcom.sgm.beans.enumeration.FrequenciaIndicadorEnum;
import br.com.linkcom.sgm.exception.GeplanesException;
import br.com.linkcom.sgm.service.UnidadeGerencialService;
import br.com.linkcom.sgm.util.GeplanesUtils;

public class CalculosPainelControle {
	
	private int uid = 0;
	private UnidadeGerencialService unidadeGerencialService;
	
	public CalculosPainelControle() {
		unidadeGerencialService = Neo.getObject(UnidadeGerencialService.class);
	}
	
	public String gerarUID(){
		return "a" + uid++;
	}
	
	/** Carrega todas as UGs com objetivos estratégicos, iniciativas e indicadores.
	 *  Monta a hierarquia de UGs.
	 *  Iterage por todas UGs raiz e efetua as totalizações.
	 */
	public List<ItemPainelControleDTO> obtemHierarquiaCompleta(PlanoGestao planoGestao, UnidadeGerencial unidadeGerencialRaiz, boolean carregarSomenteUGRaiz) {
		
		//Obtem UGs sem hierarquia
		List<UnidadeGerencial> ugs = unidadeGerencialService.loadUGObjetivoEstrategicoIndicadorAcompanhamento(planoGestao, unidadeGerencialRaiz, carregarSomenteUGRaiz);

		//Organiza estrutura
		unidadeGerencialService.montaHierarquia(ugs, unidadeGerencialRaiz);
		
		String msgValidacaoSomaPesoIndicador = "";
		
		msgValidacaoSomaPesoIndicador = unidadeGerencialService.validaSomaPesoIndicadores(ugs, msgValidacaoSomaPesoIndicador);
		
		if (!msgValidacaoSomaPesoIndicador.equals("")) {
			throw new GeplanesException(msgValidacaoSomaPesoIndicador);
		}
		else {
			//Zera UID
			uid = 0;
			
			//Gera hierarquia de DTO a partir da hierarquia de UGs
			List<ItemPainelControleDTO> saida = new ArrayList<ItemPainelControleDTO>();
			for (UnidadeGerencial ugRaiz : ugs) {
				ItemPainelControleDTO dto = efetuaTotalizacoes(ugRaiz, planoGestao.getAnoExercicio());
				//Define Unique ID para DTOs
				defineUIDsDosDTOs(dto);
				saida.add(dto);
			}
			
			for (ItemPainelControleDTO dto : saida) {
				PainelControleUtil.somarAcompanhamentos(dto, FrequenciaIndicadorEnum.TRIMESTRAL);
			}
			
			//esse método altera o resultado na tela de PAinelDeControle.. utilizar para debug
			//PainelControleUtil.imprimeDTOs(saida, "");
			return saida;
		}
	}
	
	/** Passa recursivamente pela hierarquia. As totalizações devem acontecer primeiramente nas folhas,
	 * pois os pais devem contar com os resultados dos filhos.
	 */
	public ItemPainelControleDTO efetuaTotalizacoes(UnidadeGerencial ug, Integer ano) {
		List<UnidadeGerencial> ugsFilhos = ug.getFilhos();
		if (ugsFilhos != null && ugsFilhos.size() > 0) {
			
			//SE A UG POSSUI FILHOS...
			List<ItemPainelControleDTO> dtosFilhos = new ArrayList<ItemPainelControleDTO>();
			for (UnidadeGerencial ugFilho : ugsFilhos) {
				//... EFETUA A TOTALIZACAO DE CADA
				dtosFilhos.add( efetuaTotalizacoes(ugFilho, ano) );
			}
			
			//MONTA DTO apos montar filhos
			ItemPainelControleDTO dto = montaItemPainelControleDTO(ug, ano);
			
			//Adiciona filhos no pai
			if (dtosFilhos != null) {
				dto.setTemFilhos(true);
				for (ItemPainelControleDTO itemPainelControleDTOFilho : dtosFilhos) {
					dto.getFilhos().add(itemPainelControleDTOFilho);
				}
			}
			
			//System.out.println("Debuging (4)");
			//List listaDebug = new ArrayList();
			//listaDebug.add(dto);
			//PainelControleUtil.imprimeDTOs(listaDebug, "");
			
			return dto;
		}
		
		
		
		//SE A UG É FOLHA, MONTA DTO sem base em filhos
		ItemPainelControleDTO dto = montaItemPainelControleDTO(ug, ano);
		
		//System.out.println("Debuging (3)");
		//List listaDebug = new ArrayList();
		//listaDebug.add(dto);
		//PainelControleUtil.imprimeDTOs(listaDebug, "");
		
		return dto;
	}
	
	@SuppressWarnings("unchecked")
	/** 
	 * Passa recursivamente pela interface 'Hierarquizavel' que as Perspectivas, Objetivos Estratégicos e Indicadores implementam
	 * e gera uma estrutura de 'ItemPainelControleDTO'.
	 */
	public ItemPainelControleDTO montaItemPainelControleDTO(Hierarquizavel<? extends Hierarquizavel, ? extends Hierarquizavel> h, Integer ano) {
		
		// LISTA COLECAO FILHA
		List<ItemPainelControleDTO> dtosFilhos = new ArrayList<ItemPainelControleDTO>();
		if (h.getChildren() != null && h.getChildren().size() > 0) {
			
			for (Hierarquizavel hf : h.getChildren() ) {
				hf.setParent(h);
				ItemPainelControleDTO dtoFilho = montaItemPainelControleDTO(hf, ano);
				dtosFilhos.add(dtoFilho);
			}
			
		}
		
		//GERA DTO
		ItemPainelControleDTO dtoObjetivoEstrategico = geraDTO(h, dtosFilhos, ano);
				
		return dtoObjetivoEstrategico;
	}
	
	/**
	 * Gera o DTO!
	 */
	@SuppressWarnings("unchecked")
	private ItemPainelControleDTO geraDTO(Hierarquizavel h, List<ItemPainelControleDTO> dtosFilhos, Integer ano){
		
		ItemPainelControleDTO retorno = new ItemPainelControleDTO(h, dtosFilhos);
		
		if (h instanceof Indicador) {
			retorno.setAcompanhamentos( CalculosAuxiliares.somaAcompanhamentosPorEpoca(ano, (Indicador) h) );
		}
		else{
			//o somatorio dos acompanhamentos é feito depois.. em outro lugar (DEIXE ESSE CÓDIGO COMENTADO)
			//retorno.setAcompanhamentos( CalculosAuxiliares.calculaMediaAcompanhamentos(dtosFilhos) );
		}
		
		return retorno;
	}
	
	/**
	 * Define Unique ID para DTOs e 'pai'.
	 */
	public void defineUIDsDosDTOs(ItemPainelControleDTO dto){
		
		if (dto.getId() != null) {
			throw new FunctorException("Problema ao gerar árvore para painel de controle. Item " + dto + " já possui ID definido." );
		}
		
		String gerada = gerarUID();
		dto.setId(gerada);
		
		//debug Console
		/*
		int i = 0;
		ItemPainelControleDTO atual = dto;
		while (atual.getParent() != null) {
			atual = atual.getParent();
			i++;
		}
		String saidaConsole = dto.toString();
		for (int j = 0 ; j <= i ; j++ ) {
			saidaConsole = "   " + saidaConsole;
		}		
		System.out.println(saidaConsole);
		*/
		
		if (dto.getFilhos() != null && dto.getFilhos().size() > 0) {
			//Passa por todas as metas para detectar a MetaIndicador origem
			for (ItemPainelControleDTO dtoFilho : dto.getFilhos() ) {
				//define pai
				dtoFilho.setParent(dto);
				defineUIDsDosDTOs(dtoFilho);
			}
		}
	}

	@SuppressWarnings("unchecked")
	public static void debug(Collection<Hierarquizavel> hs, String padding) {
		if(hs != null){
			for (Hierarquizavel h : hs) {
				System.out.println(padding+h.getDescricaoCompleta());
				if(h instanceof UnidadeGerencial){
					debug((Collection)((UnidadeGerencial)h).getListaPerspectivaMapaEstrategico(), "   "+padding);	
				} else if (h instanceof PerspectivaMapaEstrategico){
					debug((Collection)((PerspectivaMapaEstrategico)h).getListaObjetivoMapaEstrategico(), "   "+padding);
				} else if (h instanceof ObjetivoMapaEstrategico){
					debug((Collection)((ObjetivoMapaEstrategico)h).getListaIndicador(), "   "+padding);
				} else {
					debug((Collection)h.getChildren(), "   "+padding);
				}
				
			}	
		}
		
	}
	
	/**
	 * Obtém o percentual real acumulado de um indicador até o último
	 * trimestre onde tenha havido lançamento de resultados.
	 * 
	 * @param listaAcompanhamentoIndicador
	 * @return
	 */
	public static Double getPercentualRealAcumulado(List<AcompanhamentoIndicador> listaAcompanhamentoIndicador) {
		Double percentualReal = null;
		AcompanhamentoIndicador acompanhamentoIndicador;
		
		if (listaAcompanhamentoIndicador != null) {
			for (int i = listaAcompanhamentoIndicador.size() - 1; i >=0; i--) {
				acompanhamentoIndicador = listaAcompanhamentoIndicador.get(i);
				
				percentualReal = acompanhamentoIndicador.getPercentualReal();
				
				if (percentualReal != null) {
					break;
				}
			}
		}
		
		return percentualReal;
	}	
	
}

class PainelControleUtil {
	
	//código do somatório das anomalias
	
	/**
	 * Soma os acompanhamentos desse dto e dos filhos
	 */
	public static void somarAcompanhamentos(ItemPainelControleDTO dto, FrequenciaIndicadorEnum frequencia){
		calcularValoresAcumulados(dto);
		agruparPorFrequencia(dto, frequencia);
		subirAcompanhamentos(dto, frequencia);
		//ordenarFilhos(dto);
		precisaoAcompanhamentos(dto);
		atualizarFarois(dto, true);
		atualizarFarois(dto, false);
	}
	

	private static void atualizarFarois(ItemPainelControleDTO dto, boolean acompanhamentosAcumulados) {
		List<AcompanhamentoIndicador> acompanhamentos;
		if(acompanhamentosAcumulados){
			acompanhamentos = dto.getAcompanhamentosAcumulados();
		} else {
			acompanhamentos = dto.getAcompanhamentos();	
		}
		
		if(acompanhamentos != null && acompanhamentos.size() == 24){
			for (AcompanhamentoIndicador acompanhamentoIndicador : acompanhamentos) {
				acompanhamentoIndicador.setFarolPequeno(true);
			}
		}
		List<ItemPainelControleDTO> filhos = dto.getFilhos();
		if(filhos != null){
			for (ItemPainelControleDTO controleDTO : filhos) {
				atualizarFarois(controleDTO, acompanhamentosAcumulados);
			}			
		}
	
	}


//	@SuppressWarnings("unchecked")
//	private static void ordenarFilhos(ItemPainelControleDTO dto) {
//		if(dto.getFilhos() != null){
//			Collections.sort(dto.getFilhos());
//			for (ItemPainelControleDTO filho : dto.getFilhos()) {
//				ordenarFilhos(filho);
//			}
//		}
//	}


	private static void calcularValoresAcumulados(ItemPainelControleDTO dto) {
		List<AcompanhamentoIndicador> acompanhamentos = dto.getAcompanhamentos();
		List<AcompanhamentoIndicador> acumulados = new ArrayList<AcompanhamentoIndicador>();
		for (AcompanhamentoIndicador acompanhamento : acompanhamentos) {
			AcompanhamentoIndicador acompanhamentoAcumulado = new AcompanhamentoIndicador();
			acompanhamentoAcumulado.setIndicador(acompanhamento.getIndicador());
			acompanhamentoAcumulado.setValorLimiteSuperior(acompanhamento.getValorLimiteSuperior());
			acompanhamentoAcumulado.setValorReal(acompanhamento.getValorReal());
			acompanhamentoAcumulado.setValorLimiteInferior(acompanhamento.getValorLimiteInferior());
			acompanhamentoAcumulado.setPercentualReal(null);
			acompanhamentoAcumulado.setPercentualTolerancia(acompanhamento.getPercentualTolerancia());
			acompanhamentoAcumulado.setAnomaliasUsuarios(acompanhamento.getAnomaliasUsuarios());
			acompanhamentoAcumulado.setMelhor(acompanhamento.getMelhor());
			acumulados.add(acompanhamentoAcumulado);
		}
		dto.setAcompanhamentosAcumulados(acumulados);
		calcularAcumulados(acumulados);
		if(dto.getFilhos() != null){
			List<ItemPainelControleDTO> filhos = dto.getFilhos();
			for (ItemPainelControleDTO filho : filhos) {
				calcularValoresAcumulados(filho);
			}
		}
	}

	public static void calcularAcumulados(List<AcompanhamentoIndicador> acumulados) {
		Double valorLimiteSuperiorAcumulado = null;
		Double valorRealAcumulado = null;
		Double valorLimiteInferiorAcumulado = null;
		
		if (acumulados != null) {
			for (AcompanhamentoIndicador acompanhamentoIndicador : acumulados) {
				if (acompanhamentoIndicador.getValorReal() != null) {
					
					if (valorLimiteSuperiorAcumulado == null) {
						valorLimiteSuperiorAcumulado = 0d;
					}
					
					if (valorLimiteInferiorAcumulado == null) {
						valorLimiteInferiorAcumulado = 0d;
					}
					
					if (valorRealAcumulado == null) {
						valorRealAcumulado = 0d;
					}
					
					valorLimiteSuperiorAcumulado +=  acompanhamentoIndicador.getValorLimiteSuperior() != null ? acompanhamentoIndicador.getValorLimiteSuperior() : 0;
					valorRealAcumulado += acompanhamentoIndicador.getValorReal();
					valorLimiteInferiorAcumulado += acompanhamentoIndicador.getValorLimiteInferior() != null ? acompanhamentoIndicador.getValorLimiteInferior() : 0;
				}
				
				acompanhamentoIndicador.setValorLimiteSuperior(valorLimiteSuperiorAcumulado);
				acompanhamentoIndicador.setValorLimiteInferior(valorLimiteInferiorAcumulado);
				acompanhamentoIndicador.setValorReal(valorRealAcumulado);
			}
		}
	}

	/**
	 * Sobe os acompanhamentos para os níveis superiores de acordo com a frequencia
	 * Os DTOs folha já devem possuir acompanhamentos agrupados por essa frequencia
	 * @param dto
	 * @param frequencia
	 */
	private static void subirAcompanhamentos(ItemPainelControleDTO dto, FrequenciaIndicadorEnum frequencia) {
		boolean temFilhos = dto.getFilhos() != null && dto.getFilhos().size() > 0;
		if(!temFilhos){
			// se nao tem filhos esse dto deve já estar agrupado pela frequencia, 
			// caso contrário não será possível somar o pai
		} else {
			//se tem filhos podemos calcular
			List<ItemPainelControleDTO> filhos = dto.getFilhos();
			for (ItemPainelControleDTO filho : filhos) {
				if(filho.getAcompanhamentosPorFator(frequencia.getFatorDivisao()) == null){
					// se o filho não foi ainda calculado... calcular
					
					//está sendo suposto que tanto os acompahamentos acumulados quanto os apurados estão subindo juntos
					subirAcompanhamentos(filho, frequencia);
				}
			}
			//depois que subiu todos os filhos podemos calcular os acompanhamentos desse dto
			
			//criar uma cópia da lista porque iremos modificá-la
			filhos = new ArrayList<ItemPainelControleDTO>(filhos);
			
			//nós não somamos as unidades gerenciais filhas na unidade gerencial pai, 
			//entao iremos remove-las
			if(dto.getHierarquizavel() instanceof UnidadeGerencial){
				for (Iterator<ItemPainelControleDTO> iter = filhos.iterator(); iter.hasNext();) {
					ItemPainelControleDTO filho = iter.next();
					if(filho.getHierarquizavel() instanceof UnidadeGerencial){
						iter.remove();
					}
				}
			}
			
			somarAcompanhamentos(dto, filhos, frequencia.getFatorDivisao());
		}
	}
	/**
	 * Formata os acompanhamentos do indicador de acordo com a precisão do indicador
	 * @param dto
	 */
	private static void precisaoAcompanhamentos(ItemPainelControleDTO dto) {
		List<ItemPainelControleDTO> filhos = dto.getFilhos();
		if (filhos != null && filhos.size() > 0) {
			for (ItemPainelControleDTO filho : filhos) {
				precisaoAcompanhamentos(filho);
			}
		}
		
		Integer precisao = dto.getPrecisao();
		if(precisao != null){
			for (AcompanhamentoIndicador a : dto.getAcompanhamentos()) {
				definePrecisaoAcompanhamento(precisao, a, "valorLimiteSuperior");
				definePrecisaoAcompanhamento(precisao, a, "valorReal");
				definePrecisaoAcompanhamento(precisao, a, "valorLimiteInferior");
				definePrecisaoAcompanhamento(precisao, a, "valorLimiteSuperiorAcumulado");
				definePrecisaoAcompanhamento(precisao, a, "valorLimiteInferiorAcumulado");
				definePrecisaoAcompanhamento(precisao, a, "valorRealAcumulado");
			}
			for (AcompanhamentoIndicador a : dto.getAcompanhamentosAcumulados()) {
				definePrecisaoAcompanhamento(precisao, a, "valorLimiteSuperior");
				definePrecisaoAcompanhamento(precisao, a, "valorReal");
				definePrecisaoAcompanhamento(precisao, a, "valorLimiteInferior");
				definePrecisaoAcompanhamento(precisao, a, "valorLimiteSuperiorAcumulado");
				definePrecisaoAcompanhamento(precisao, a, "valorLimiteInferiorAcumulado");
				definePrecisaoAcompanhamento(precisao, a, "valorRealAcumulado");
			}
		}
		
	}

	private static void definePrecisaoAcompanhamento(Integer precisao, AcompanhamentoIndicador bean, String propriedade) {
		Double valorInicial = (Double) GeplanesUtils.getProperty(bean, propriedade);
		if (valorInicial != null) {
			//arredonda e gera string
			Double valorArredondado = GeplanesUtils.round(valorInicial,	precisao);
			//define pattern
			String pat = "#,##0";
			if (precisao.intValue() > 0) {
				pat = "#,##0.";
				for (int i = 0 ; i < precisao.intValue() ; i++) {
					pat += "0";
				}
			}
			String valorArredondadoAsString = new DecimalFormat(pat).format(valorArredondado);
			
			//seta
			GeplanesUtils.setProperty(bean, propriedade, valorArredondado);
			
			//Caso se queira exibir os valores da maneira que foram cadastrados, independente da precisão,
			//inventer o comentário das linhas abaixo. Isso é só para efeito de exibição dos valores no Painel de Controle.
			//Para efeito de cálculo de somatórios, sempre valerá a precisão definida no indicador.
			GeplanesUtils.setProperty(bean, propriedade + "AsString", valorArredondadoAsString);
			//GeplanesUtils.setProperty(bean, propriedade + "AsString", new DecimalFormat("#,##0.###").format(valorInicial));
		}
	}
			

	/**
	 * Soma os acompanhamentos dos dtos filhos no dto pai
	 * Os dtos filhos devem possuir acompanhamentos pelo fator informado
	 * @param dto
	 * @param filhos
	 * @param fatorDivisao
	 */
	private static void somarAcompanhamentos(ItemPainelControleDTO dto, List<ItemPainelControleDTO> filhos, int fatorDivisao) {
		somarAcompanhamentos(dto, filhos, fatorDivisao, true);
		somarAcompanhamentos(dto, filhos, fatorDivisao, false);
	}

	private static Double calculaPesoObjetivoEstrategico(ItemPainelControleDTO dto, int fatorDivisao, int indiceEpoca, boolean somarApurados) {
		List<AcompanhamentoIndicador> acompanhamentosPorFator;		
		AcompanhamentoIndicador acompanhamentoIndicador;
		Double pesoTotal = 0.0;
		Double pesoFilho = 0.0;
		
		List<ItemPainelControleDTO> filhos = dto.getFilhos();		
		
		if (filhos != null) {
			for (ItemPainelControleDTO filho : filhos) { //Indicadores
				if(somarApurados){
					acompanhamentosPorFator = filho.getAcompanhamentosPorFator(fatorDivisao);	
				} 
				else {
					acompanhamentosPorFator = filho.getAcompanhamentosAcumuladosPorFator(fatorDivisao);
				}

				acompanhamentoIndicador = acompanhamentosPorFator.get(indiceEpoca);
				
				if (acompanhamentoIndicador != null) {
					
					//Se os percentuais reais nao foram lançados, o peso do indicador será desconsiderado.
					if (acompanhamentoIndicador.getPercentualReal() == null) {
						continue;				
					}
					
					pesoFilho = filho.getPeso();
					if (pesoFilho == null) {
						return null;
					}
					pesoTotal += pesoFilho;
				}				
			}
		}
		return pesoTotal;
	}

	private static void somarAcompanhamentos(ItemPainelControleDTO dto, List<ItemPainelControleDTO> filhos, int fatorDivisao, boolean somarApurados) {
		List<AcompanhamentoIndicador> acompanhamentosPorFatorDoDto = new ArrayList<AcompanhamentoIndicador>();
		for (int i = 0; i < fatorDivisao; i++) {
			double somaPesos = 0;
			double somaPercentualTolerancia = 0;
			Double somaPercentualReal = null;
			boolean naoAplicavelAoPeriodo = true;
			for (ItemPainelControleDTO filho : filhos) {
				
				Double peso;
				
				//Verifica se o dto é do tipo Perspectiva
				//Se for, o peso deve ser setado para um valor default, pois atualmente não está sendo utilizado peso em Perspectiva
				if (filho.getHierarquizavel() instanceof PerspectivaMapaEstrategico) {
					peso = 1d;
				}
				//Verifica se o dto é do tipo Objetivo Estratégico
				//Se for, o peso deve ser calculado de acordo com a época, pois podem existir indicadores que não tiveram seus valores lançados
				//ou até mesmo cujos valores não se aplicam a um determinado período.
				//Sendo assim, o peso do objetivo estratégico deve levar isso em consideração
				else if (filho.getHierarquizavel() instanceof ObjetivoMapaEstrategico) {					
					peso = calculaPesoObjetivoEstrategico(filho,fatorDivisao,i,somarApurados);
				}
				else {
					peso = filho.getPeso();	
				}
				
				if (peso == null) {
					String item = null;
					ItemPainelControleDTO atual = filho;
					while(atual != null){
						item = "[" + atual.getTipo() + ":" + atual.getDescricao() + "]" + (item == null ? "" : " > " + item);
						atual = atual.getParent();
					}
					throw new GeplanesException("Não foi possível somar os acompanhamentos. " + item + " não possui peso.");
				}
				//somaPesos += peso;
				List<AcompanhamentoIndicador> acompanhamentosPorFator;
				if (somarApurados) {
					acompanhamentosPorFator = filho.getAcompanhamentosPorFator(fatorDivisao);	
				} 
				else {
					acompanhamentosPorFator = filho.getAcompanhamentosAcumuladosPorFator(fatorDivisao);
				}
				
				if(acompanhamentosPorFator == null){
					//se nao tem acompanhamento agrupado por fator, nao é possível somar os itens
					//passar esse filho
					naoAplicavelAoPeriodo = false;
					continue;
				}
				
				AcompanhamentoIndicador acompanhamentoIndicador = acompanhamentosPorFator.get(i);
				naoAplicavelAoPeriodo = naoAplicavelAoPeriodo && acompanhamentoIndicador.getNaoaplicavel() != null ? acompanhamentoIndicador.getNaoaplicavel() : false;

				Double percentualReal = acompanhamentoIndicador.getPercentualReal();
				
				//se os percentuais reais nao foram lançados, os valores são desconsiderados.
				if (percentualReal == null) {
					continue;
				}
					
				somaPesos += peso;
				
				if (somaPercentualReal == null) {
					somaPercentualReal = percentualReal;
					if (percentualReal != null) {
						somaPercentualReal *= peso;
					}
				} 
				else {
					if (percentualReal != null) {
						somaPercentualReal += percentualReal * peso;
					}
				}
				
				if (acompanhamentoIndicador.getPercentualTolerancia() != null) {
					somaPercentualTolerancia += acompanhamentoIndicador.getPercentualTolerancia() * peso;
				}
			}
			
			AcompanhamentoIndicador acompanhamentoIndicador = new AcompanhamentoIndicador();
			if (somaPercentualReal != null) {
				acompanhamentoIndicador.setPercentualReal(somaPercentualReal / somaPesos);
			}
			acompanhamentoIndicador.setPercentualTolerancia(somaPercentualTolerancia / somaPesos);
			acompanhamentoIndicador.setNaoaplicavel(naoAplicavelAoPeriodo);
			
			acompanhamentosPorFatorDoDto.add(acompanhamentoIndicador);
		}
		if (somarApurados) {
			dto.getAcompanhamentosPorFatorDeDivisao().put(fatorDivisao, acompanhamentosPorFatorDoDto);
			dto.setAcompanhamentos(acompanhamentosPorFatorDoDto);	
		} 
		else {
			dto.getAcompanhamentosAcumuladosPorFatorDeDivisao().put(fatorDivisao, acompanhamentosPorFatorDoDto);
			dto.setAcompanhamentosAcumulados(acompanhamentosPorFatorDoDto);
		}
	}
	
	/**
	 * Agrupa os acompanhamentos em determinada frequencia no dto, SE ESSE DTO POSSUIR ACOMPANHAMENTOS (ou seja, se for folha)
	 * Agrupa também os filhos
	 */
	public static void agruparPorFrequencia(ItemPainelControleDTO dto, FrequenciaIndicadorEnum frequencia){
		//ATENÇÃO... OS DOIS BLOCOS TÊM QUE SER IGUAIS
		//o primeiro trata dos acompanhamentos Apurados
		//o segundo trata dos acompanhamentos Acumulados

		
		{
			//agrupar acompanhamentos
			List<AcompanhamentoIndicador> acompanhamentosPorFator = dto.getAcompanhamentosPorFator(frequencia.getFatorDivisao());
			if(acompanhamentosPorFator == null){//ainda nao foi calculado
				List<AcompanhamentoIndicador> acompanhamentos = dto.getAcompanhamentos();
				if(acompanhamentos != null && acompanhamentos.size() > 0){
					dto.getAcompanhamentosPorFatorDeDivisao().put(frequencia.getFatorDivisao(), CalculosAuxiliares.agruparPorFator(acompanhamentos, frequencia.getFatorDivisao(), dto.getPercentualTolerancia()));
				}
			}
		}
		
		{
			//agrupar acompanhamentos acumulados
			List<AcompanhamentoIndicador> acompanhamentosAcumuladosPorFator = dto.getAcompanhamentosAcumuladosPorFator(frequencia.getFatorDivisao());
			if(acompanhamentosAcumuladosPorFator == null){//ainda nao foi calculado
				List<AcompanhamentoIndicador> acompanhamentos = dto.getAcompanhamentosAcumulados();
				if(acompanhamentos != null && acompanhamentos.size() > 0){
					dto.getAcompanhamentosAcumuladosPorFatorDeDivisao().put(frequencia.getFatorDivisao(), CalculosAuxiliares.agruparPorFator(acompanhamentos, frequencia.getFatorDivisao(), dto.getPercentualTolerancia()));
				}
			}
		}
		
		if(dto.getFilhos() != null && dto.getFilhos().size() > 0){
			List<ItemPainelControleDTO> filhos = dto.getFilhos();
			for (ItemPainelControleDTO filho : filhos) {
				agruparPorFrequencia(filho, frequencia);
			}
		}
		
		
		
	}
	
	public static void imprimeDTOs(List<ItemPainelControleDTO> itens, String padding){
		//System.out.println("\n\n");
		for (ItemPainelControleDTO controleDTO : itens) {
			System.out.println(imprimeItem(padding, controleDTO));
			System.out.println(imprimeItemTrimestral(padding, controleDTO));
			imprimeDTOs(controleDTO.getFilhos(), padding+"  ");
		}
	}

	private static Object imprimeItemTrimestral(String padding, ItemPainelControleDTO controleDTO) {
		String texto = "";
		for (int i = 0; i < 90; i++) {
			texto += " ";
		}
		texto = montaFarois(texto, controleDTO.getAcompanhamentosPorFator(FrequenciaIndicadorEnum.TRIMESTRAL.getFatorDivisao()));
		return texto;
	}

	private static String imprimeItem(String padding, ItemPainelControleDTO controleDTO) {
		String texto = padding + controleDTO;
		while(texto.length() < 90){
			texto += " ";
		}
		List<AcompanhamentoIndicador> acompanhamentos = controleDTO.getAcompanhamentos();
		texto = montaFarois(texto, acompanhamentos);
		return texto;
	}

	private static String montaFarois(String texto, List<AcompanhamentoIndicador> acompanhamentos) {
		if(acompanhamentos == null){
			return "";
		}
		DecimalFormat decimalFormat = new DecimalFormat("#00.##");
		for (AcompanhamentoIndicador acompanhamento : acompanhamentos) {
			
			Double percentualReal = acompanhamento.getPercentualReal() != null ? acompanhamento.getPercentualReal() : 0.0;
			Double percentualTolerancia = acompanhamento.getPercentualTolerancia() != null ? acompanhamento.getPercentualTolerancia() : 0.0;
			texto += "  "+fill(decimalFormat.format(percentualReal) + " / "+decimalFormat.format(percentualTolerancia), 12);
		}
		return texto;
	}

	private static String fill(String corFarol, int i) {
		if(corFarol.length() >= i){
			return corFarol;
		}
		while(corFarol.length() < i){
			corFarol += " ";
		}
		return corFarol;
	}
}
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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import br.com.linkcom.neo.types.ListSet;
import br.com.linkcom.sgm.beans.AcompanhamentoIndicador;
import br.com.linkcom.sgm.beans.Indicador;
import br.com.linkcom.sgm.beans.ObjetivoMapaEstrategico;
import br.com.linkcom.sgm.beans.PerspectivaMapaEstrategico;
import br.com.linkcom.sgm.beans.UnidadeGerencial;
import br.com.linkcom.sgm.beans.DTO.ItemPainelControleDTO;
import br.com.linkcom.sgm.beans.enumeration.FrequenciaIndicadorEnum;
import br.com.linkcom.sgm.exception.GeplanesException;
import br.com.linkcom.sgm.service.UnidadeGerencialService;

public class CalculosAuxiliares {

	public static Set<AcompanhamentoIndicador> gerarAcompanhamento(FrequenciaIndicadorEnum frequencia, Integer ano) {
		
		boolean bissexto = anoBissexto(ano);
		
		//ALTERACAO DO RÓGEL PARA SUPORTAR ENUMS
		if(frequencia == FrequenciaIndicadorEnum.QUINZENAL){
			//gerar um acompanhamneto para cada quinzena do ano
			return geraQuinzenal(ano);
		}else if(frequencia == FrequenciaIndicadorEnum.MENSAL){
			//gerar um acompanhamento para cada mês do ano
			return geraMensal(ano, bissexto);
		}else{
			//gerar um acompanhamneto para cada trimestre do ano
			return geraTrimestre(ano);
		}
	}
	
	public static Set<AcompanhamentoIndicador> geraQuinzenal(Integer ano) {
		
		Set<AcompanhamentoIndicador> listaAcompanhamento = new ListSet<AcompanhamentoIndicador>(AcompanhamentoIndicador.class);
		
		int d = 0;
		for (int i = 0; i < 12; i++) {
			
			//PRIMEIRA QUINZENA
			AcompanhamentoIndicador acompanhamentoIndicador = new AcompanhamentoIndicador();
			
			Calendar dataInicio = Calendar.getInstance();
			dataInicio.set(Calendar.YEAR, ano);
			dataInicio.set(Calendar.MONTH, i);
			dataInicio.set(Calendar.DAY_OF_MONTH, 1);
			
			int midDay = new Double( Math.floor( dataInicio.getMaximum(Calendar.DAY_OF_MONTH) / 2 ) ).intValue();
			Calendar dataMeio = Calendar.getInstance();
			dataMeio.set(Calendar.YEAR, ano);
			dataMeio.set(Calendar.MONTH, i);
			dataMeio.set(Calendar.DAY_OF_MONTH, midDay);
			
			acompanhamentoIndicador.setDataInicial(dataInicio);
			acompanhamentoIndicador.setDataFinal(dataMeio);
			acompanhamentoIndicador.setIndice( d++ );
			listaAcompanhamento.add(acompanhamentoIndicador);
			
			//SEGUNDA QUINZENA
			acompanhamentoIndicador = new AcompanhamentoIndicador();
			Calendar dataMeio2 = Calendar.getInstance();
			dataMeio2.set(Calendar.YEAR, ano);
			dataMeio2.set(Calendar.MONTH, i);
			dataMeio2.set(Calendar.DAY_OF_MONTH, midDay + 1);
			
			Calendar dataFim = Calendar.getInstance(); 
			dataFim.set(Calendar.YEAR, ano);
			dataFim.set(Calendar.MONTH, i);
			dataFim.set(Calendar.DAY_OF_MONTH, dataInicio.getMaximum(Calendar.DAY_OF_MONTH) );
			
			acompanhamentoIndicador.setDataInicial(dataMeio2);
			acompanhamentoIndicador.setDataFinal(dataFim);
			acompanhamentoIndicador.setIndice( d++ );
			listaAcompanhamento.add(acompanhamentoIndicador);
			
		}
		return listaAcompanhamento;
	}
	
	public static Set<AcompanhamentoIndicador> geraMensal(Integer ano, boolean bissexto) {
		
		Set<AcompanhamentoIndicador> listaAcompanhamento = new ListSet<AcompanhamentoIndicador>(AcompanhamentoIndicador.class);
		
		for (int i = 0; i < 12; i++) {
			
			AcompanhamentoIndicador acompanhamentoIndicador = new AcompanhamentoIndicador();
			
			//inicicar o ano
			Calendar dataInicio = Calendar.getInstance(); 
			dataInicio.set(Calendar.YEAR, ano);
			dataInicio.set(Calendar.MONTH, i);
			dataInicio.set(Calendar.DAY_OF_MONTH, 1);
			
			Calendar dataFim = Calendar.getInstance(); 
			if(i == 1){
				dataFim.set(Calendar.DATE, bissexto ? 29 : 28);
			}else if ((i == 3) || (i == 5) || (i == 8) || (i == 10)) {  
				dataFim.set(Calendar.DATE,30);
			}else{
				dataFim.set(Calendar.DATE,31);
			}
			
			dataFim.set(Calendar.MONTH, i);
			dataFim.set(Calendar.YEAR, ano);
			
			acompanhamentoIndicador.setDataInicial(dataInicio);
			acompanhamentoIndicador.setDataFinal(dataFim);
			acompanhamentoIndicador.setIndice(i);
			listaAcompanhamento.add(acompanhamentoIndicador);
		}
		return listaAcompanhamento;
	}
	
	public static Set<AcompanhamentoIndicador> geraTrimestre(Integer ano) {
		Set<AcompanhamentoIndicador> listaAcompanhamento = new ListSet<AcompanhamentoIndicador>(AcompanhamentoIndicador.class);
		
		for (int i = 0; i <= 3; i++) {
			Calendar dataInicio = Calendar.getInstance(); 
			dataInicio.set(Calendar.DATE, 1);
			dataInicio.set(Calendar.MONTH, i == 0 ? 0 : i == 1 ? 3 : i == 2 ? 6 : i == 3 ? 9 : 0);
			dataInicio.set(Calendar.YEAR, ano);
			
			Calendar dataFim = Calendar.getInstance(); 
			dataFim.set(Calendar.DATE, (i == 1 || i == 2) ? 30 : 31);
			dataFim.set(Calendar.MONTH, i == 0 ? 2 : i == 1 ? 5 : i == 2 ? 8 : i == 3 ? 11 : 0);
			dataFim.set(Calendar.YEAR, ano);

			AcompanhamentoIndicador acompanhamentoIndicador = new AcompanhamentoIndicador();
			acompanhamentoIndicador.setDataInicial(dataInicio);
			acompanhamentoIndicador.setDataFinal(dataFim);
			acompanhamentoIndicador.setIndice(i);
			listaAcompanhamento.add(acompanhamentoIndicador);
		}
	
		return listaAcompanhamento;
	}
	
	@SuppressWarnings("unchecked")
	public static List<AcompanhamentoIndicador> somaAcompanhamentosPorEpoca(Integer ano, Indicador indicador){
		FrequenciaIndicadorEnum frequencia = indicador.getFrequencia();
		Set<AcompanhamentoIndicador> acompanhamentosExistentes = indicador.getAcompanhamentosIndicador();
		
		// Lista total
		List<AcompanhamentoIndicador> acompanhamentosNovos = (List<AcompanhamentoIndicador>) gerarAcompanhamento(frequencia, ano);
		for (AcompanhamentoIndicador acompanhamentoNovo : acompanhamentosNovos){
			acompanhamentoNovo.setPercentualTolerancia(indicador.getTolerancia());
			acompanhamentoNovo.setMelhor(indicador.getMelhor());
			
			// Busca o acompanhamento semelhante, através do índice
			for (AcompanhamentoIndicador acompanhamentoExistente : acompanhamentosExistentes) {
				
				if (acompanhamentoNovo.getIndice().equals(acompanhamentoExistente.getIndice())) {
					acompanhamentoNovo.setValorLimiteSuperior(acompanhamentoExistente.getValorLimiteSuperior());
					acompanhamentoNovo.setValorReal(acompanhamentoExistente.getValorReal());
					acompanhamentoNovo.setValorLimiteInferior(acompanhamentoExistente.getValorLimiteInferior());
					acompanhamentoNovo.setNaoaplicavel(acompanhamentoExistente.getNaoaplicavel());
					
					if (acompanhamentoExistente.getAnomalia() != null) {
						acompanhamentoNovo.getAnomaliasUsuarios().add(acompanhamentoExistente.getAnomalia());	
					}
					break;
				}
			}			
			
		}
		
		// Passa por todas as epocas...
		
		for (AcompanhamentoIndicador acTotal : acompanhamentosNovos) {
			acTotal.setIndicador(indicador);
			

		}
		
		return acompanhamentosNovos;
	}

	@SuppressWarnings("unchecked")
	public static  List<AcompanhamentoIndicador> calculaMediaAcompanhamentos(List<ItemPainelControleDTO> dtos){
		//int ord = new Double( Math.floor( Math.random() * 3.0 ) ).intValue();
		//FrequenciaIndicadorEnum freq = FrequenciaIndicadorEnum.values()[ord];
		FrequenciaIndicadorEnum freq = FrequenciaIndicadorEnum.QUINZENAL;
		
		List<AcompanhamentoIndicador> total = (List<AcompanhamentoIndicador>) gerarAcompanhamento(freq, 2008);
		for (AcompanhamentoIndicador acompanhamentoIndicador : total){
			//double real = Math.floor( Math.random() * 120.0 );
			acompanhamentoIndicador.setPercentualTolerancia(75.0);
			acompanhamentoIndicador.setPercentualReal(100.0);
		}
		return new ArrayList<AcompanhamentoIndicador>();
	}
	
	public static boolean anoBissexto(Integer ano)
	{
	    if (((ano % 4 == 0) && (ano % 100 > 0)) || (ano % 400 == 0) )
	        return true;
	    else
	        return false;
	}
	
	
	/**
	 * 
	 * @param acompanhamentos
	 */
	public static void calculaAcumulados(List<AcompanhamentoIndicador> acompanhamentos) {
		
		/*** A lista deve vir ordenada ***/
		
		Double limiteSuperiorAcumulado=new Double(0);
		Double realAcumulado=new Double(0);
		Double limiteInferiorAcumulado=new Double(0);
		
		for (AcompanhamentoIndicador acompanhamentoIndicador : acompanhamentos) {
			if( acompanhamentoIndicador.getValorLimiteSuperior() !=null)
				limiteSuperiorAcumulado+=acompanhamentoIndicador.getValorLimiteSuperior();
			
			if( acompanhamentoIndicador.getValorReal() !=null)
				realAcumulado+=acompanhamentoIndicador.getValorReal();
			
			if( acompanhamentoIndicador.getValorLimiteInferior() !=null)
				limiteInferiorAcumulado+=acompanhamentoIndicador.getValorLimiteInferior();			
			
			acompanhamentoIndicador.setValorLimiteSuperiorAcumulado(limiteSuperiorAcumulado);
			acompanhamentoIndicador.setValorRealAcumulado(realAcumulado);
			acompanhamentoIndicador.setValorLimiteInferiorAcumulado(limiteInferiorAcumulado);
		}
	}
	
	public static void nomeiaEpocasPorObjetivoEstrategico(List<PerspectivaMapaEstrategico> listaPerspectivaMapaEstrategico) {
		if (listaPerspectivaMapaEstrategico != null && !listaPerspectivaMapaEstrategico.isEmpty()) {
			for (PerspectivaMapaEstrategico perspectivaMapaEstrategico : listaPerspectivaMapaEstrategico) {
				if (perspectivaMapaEstrategico.getListaObjetivoMapaEstrategico() != null && !perspectivaMapaEstrategico.getListaObjetivoMapaEstrategico().isEmpty()) {
					for (ObjetivoMapaEstrategico objetivoMapaEstrategico : perspectivaMapaEstrategico.getListaObjetivoMapaEstrategico()) {
						if (objetivoMapaEstrategico.getListaIndicador() != null && !objetivoMapaEstrategico.getListaIndicador().isEmpty()) {
							for (Indicador indicador : objetivoMapaEstrategico.getListaIndicador()) {		
								nomeiaEpocas(indicador.getAcompanhamentosIndicador());
							}
						}
					}
				}
			}
		}
	}
	
	public static void nomeiaEpocas(List<AcompanhamentoIndicador> acompanhamentos){
		String[][] epocas = new String[25][];
		String[] trimestral = {"1° T", "2° T", "3° T", "4° T"};
		epocas[4] = trimestral;
		String[] mensal = {"Jan", "Fev", "Mar", "Abr", "Mai", "Jun", "Jul", "Ago", "Set", "Out", "Nov", "Dez"};
		epocas[12] = mensal;
		String[] quin = {"Jan1", "Jan2", "Fev1", "Fev2", "Mar1", "Mar2", "Abr1", "Abr2", "Mai1", "Mai2", "Jun1", "Jun2", "Jul1", "Jul2", "Ago1", "Ago2", "Set1", "Set2", "Out1", "Out2", "Nov1", "Nov2", "Dez1", "Dez2"};
		epocas[24] = quin;
		
		int i = 0;
		for (AcompanhamentoIndicador acompanhamentoIndicador : acompanhamentos) {
			if (acompanhamentos.size() == 4 || acompanhamentos.size() == 12 || acompanhamentos.size() == 24) {
				acompanhamentoIndicador.setEpoca( epocas[acompanhamentos.size()][i++] );
				continue;
			}
			acompanhamentoIndicador.setEpoca( "" + i++ );
		}
	}
	
	public static void nomeiaEpocas(Set<AcompanhamentoIndicador> acompanhamentos){
		nomeiaEpocas(new ArrayList<AcompanhamentoIndicador>(acompanhamentos));
	}
	
	/***
	 * Baseado na frequência do indicador, atribui nome às épocas dos seus acompanhamentos
	 * É imprescindível que os acompanhamentos estejam na quantidade exigida pela frequência e ordenados.
	 * 
	 * @param frequencia
	 * @param acompanhamentos
	 * 
	 * @author Rodrigo Alvarenga
	 */
	public static void nomeiaEpocas(FrequenciaIndicadorEnum frequencia, Set<AcompanhamentoIndicador> acompanhamentos){
		String[] epocaTrimestral = {"1° T", "2° T", "3° T", "4° T"};
		String[] epocaMensal = {"Jan", "Fev", "Mar", "Abr", "Mai", "Jun", "Jul", "Ago", "Set", "Out", "Nov", "Dez"};
		String[] epocaQuinzenal = {"Jan1", "Jan2", "Fev1", "Fev2", "Mar1", "Mar2", "Abr1", "Abr2", "Mai1", "Mai2", "Jun1", "Jun2", "Jul1", "Jul2", "Ago1", "Ago2", "Set1", "Set2", "Out1", "Out2", "Nov1", "Nov2", "Dez1", "Dez2"};
		
		int i = 0;
		for (AcompanhamentoIndicador acompanhamentoIndicador : acompanhamentos) {
			if (frequencia == FrequenciaIndicadorEnum.QUINZENAL) { //Quinzenal
				acompanhamentoIndicador.setEpoca( epocaQuinzenal[i++] );
			}
			else if (frequencia == FrequenciaIndicadorEnum.MENSAL) { //Mensal
				acompanhamentoIndicador.setEpoca( epocaMensal[i++] );
			}
			else { //Trimestral
				acompanhamentoIndicador.setEpoca( epocaTrimestral[i++] );
			}
						
		}
		
	}
	
	public static Calendar calculaDataFinalAcompanhamento(FrequenciaIndicadorEnum frequencia, Calendar dataInicial) {		
		int mes = dataInicial.get(Calendar.MONTH);
		int ano = dataInicial.get(Calendar.YEAR);
		boolean bissexto = anoBissexto(ano);
		
		Calendar dataFinal = Calendar.getInstance();
		dataFinal.set(Calendar.HOUR_OF_DAY, 0);
		dataFinal.set(Calendar.MINUTE, 0);
		dataFinal.set(Calendar.SECOND, 0);
		dataFinal.set(Calendar.MILLISECOND, 0);
		
		if(frequencia == FrequenciaIndicadorEnum.QUINZENAL) { //Frequência Quinzenal
			int midDay = new Double( Math.floor( dataInicial.getMaximum(Calendar.DAY_OF_MONTH) / 2 ) ).intValue();			
			dataFinal.set(Calendar.DAY_OF_MONTH, midDay);
			dataFinal.set(Calendar.MONTH, mes);
			dataFinal.set(Calendar.YEAR, ano);			
		}
		else if(frequencia == FrequenciaIndicadorEnum.MENSAL) {//Frequência Mensal
			if (mes == 1) {
				dataFinal.set(Calendar.DATE, bissexto ? 29 : 28);
			}
			else if ((mes == 3) || (mes == 5) || (mes == 8) || (mes == 10)) {  
				dataFinal.set(Calendar.DATE,30);
			}
			else{
				dataFinal.set(Calendar.DATE,31);
			}
			dataFinal.set(Calendar.MONTH, mes);
			dataFinal.set(Calendar.YEAR, ano);			
		}
		else { //Frequência Trimestral
			if (mes == 0 || mes == 9) {
				dataFinal.set(Calendar.DATE,31);
			}
			else if (mes == 3 || mes == 6) {  
				dataFinal.set(Calendar.DATE,30);
			}
			else {
				throw new GeplanesException("Mês inválido para a frequência Trimestral.");
			}
			dataFinal.set(Calendar.MONTH, mes + 2);
			dataFinal.set(Calendar.YEAR, ano);
		}		
		
		return dataFinal;
	}
	
	/**
	 * Agrupa os acompanhamentos por fator de divisão
	 * ex.: Se tivermos 24 acompanhamentos e o fator de divisao for 4. Será feita uma média dos acompanhamentos
	 * em grupos de 6 (4 (grupos) de 6 (acompanhamentos)) e serão retornados 4 acompanhamentos.
	 * Se o fator de divisao for maior que o número de acompanhamentos uma exceção será lançada
	 * Se o número de acompanhamentos não for múltiplo do fator de divisão uma exceção será lançada
	 * 
	 * @param acompanhamentos
	 * @param fatorDivisao
	 * @param percentualTolerancia 
	 */
	public static List<AcompanhamentoIndicador> agruparPorFator(List<AcompanhamentoIndicador> acompanhamentos, int fatorDivisao, Double percentualTolerancia) {
		List<AcompanhamentoIndicador> calculados = new ArrayList<AcompanhamentoIndicador>();
		
		if(acompanhamentos.size() < fatorDivisao){
			throw new GeplanesException("Não foi possível agrupar os acompanhamentos do indicador. O número de acompanhamentos ("+acompanhamentos.size()+") é menor que o fator de divisão ("+fatorDivisao+")");
		}
		if(acompanhamentos.size() % fatorDivisao != 0){
			throw new GeplanesException("Não foi possível agrupar os acompanhamentos do indicador. O número de acompanhamentos ("+acompanhamentos.size()+") não é múltiplo do fator de divisão ("+fatorDivisao+")");
		}
		if(percentualTolerancia == null){
			//throw new GeplanesException("Não foi possível agrupar os acompanhamentos. O percentual de tolerância não pode ser nulo.");
		}		
		
		//se os acompanhamentos já estiverem agrupados não é necessário mexer
		if (acompanhamentos.size() == fatorDivisao) {
			for (AcompanhamentoIndicador acompanhamento : acompanhamentos) {
				AcompanhamentoIndicador acompanhamentoIndicador = new AcompanhamentoIndicador();
				acompanhamentoIndicador.setIndicador(acompanhamento.getIndicador());
				acompanhamentoIndicador.setPercentualTolerancia(acompanhamento.getPercentualTolerancia());
				acompanhamentoIndicador.setPercentualReal(acompanhamento.getPercentualReal());
				acompanhamentoIndicador.setNaoaplicavel(acompanhamento.getNaoaplicavel());
				
				calculados.add(acompanhamentoIndicador);
			}
			return calculados;
		}
		
		int grupo = acompanhamentos.size() / fatorDivisao;
		int fatorEfetivoDivisao; //Criado para armazenar o fator de divisão que considera os casos em que o acompanhamento não se aplica ao período em questão.
		
		for (int i = 0; i < acompanhamentos.size(); i+= grupo) {
			
			fatorEfetivoDivisao = 0;
			Double somaPercentualReal = null;
			boolean naoAplicavelAoPeriodo = true;
			
			for (int j = 0; j < grupo; j++) {
				Double percentualReal = acompanhamentos.get(i + j).getPercentualReal();
				
				if (!Boolean.TRUE.equals(acompanhamentos.get(i + j).getNaoaplicavel())) {
					naoAplicavelAoPeriodo = false;
				}
				
				if (percentualReal != null) {
					if (somaPercentualReal == null) {
						somaPercentualReal = percentualReal;
					} 
					else {
						somaPercentualReal += percentualReal;
					}
					fatorEfetivoDivisao++;
				}
			}
			if (somaPercentualReal != null) {
				somaPercentualReal = somaPercentualReal / fatorEfetivoDivisao;	
			}
			
			AcompanhamentoIndicador acompanhamentoIndicador = new AcompanhamentoIndicador();
			acompanhamentoIndicador.setPercentualReal(somaPercentualReal);
			acompanhamentoIndicador.setPercentualTolerancia(percentualTolerancia);
			acompanhamentoIndicador.setNaoaplicavel(naoAplicavelAoPeriodo);
			
			calculados.add(acompanhamentoIndicador);
		}
		
		return calculados;
	}
	
	/**
	 * Método responsável por preencher o mapa com os percentuais reais (apurados ou acumulados) 
	 * de cada trimestre de uma determinada Unidade Gerencial.
	 * Todas as informações dos acompanhamentos serão calculadas para esta Unidade Gerencial. 
	 * 
	 * @param itemPainelControleDTO
	 * @param formaCalculo
	 * @return
	 */
	public static Map<Integer, Double> preencheMapaPercentualAMI(UnidadeGerencial unidadeGerencial, Integer formaCalculo) {
		List<AcompanhamentoIndicador> listaAcompanhamentoIndicador;
		List<ItemPainelControleDTO> listaDTOs;
		ItemPainelControleDTO itemPainelControleDTO;
		AcompanhamentoIndicador acompanhamentoIndicador;
		Map<Integer, Double> mapaPercentualEpoca = null;
				
		UnidadeGerencialService unidadeGerencialService = UnidadeGerencialService.getInstance();
		unidadeGerencial = unidadeGerencialService.loadWithPlanoGestao(unidadeGerencial);
		
		// Calcula os percentuais dos acompanhamentos para a UG e suas filhas.
		listaDTOs = new CalculosPainelControle().obtemHierarquiaCompleta(unidadeGerencial.getPlanoGestao(),unidadeGerencial, false);
		
		if (listaDTOs != null && !listaDTOs.isEmpty()) {
			// Obtém somente os dados referentes à UG em questão.			
			itemPainelControleDTO = listaDTOs.get(0);
			
			// Valores Apurados
			if (formaCalculo.intValue() == 1) {
				listaAcompanhamentoIndicador = itemPainelControleDTO.getAcompanhamentos();
			}
			// Valores Acumulados
			else {
				listaAcompanhamentoIndicador = itemPainelControleDTO.getAcompanhamentosAcumulados();
			}
			
			// Verifica se existem acompanhamentos para o indicador.							
			if(listaAcompanhamentoIndicador.size() == 4) {
				mapaPercentualEpoca = new HashMap<Integer, Double>();							
				// Busca o acompanhamento do indicador para cada trimestre.
				for (int i = 0; i < 4; i++) {
					acompanhamentoIndicador = listaAcompanhamentoIndicador.get(i);
					mapaPercentualEpoca.put(i+1, acompanhamentoIndicador.getPercentualReal());
				}				
			}			
		}
		return mapaPercentualEpoca;
	}	
	
	/**
	 * Retorna o trimestre em que o acompanhamento do indicador está vinculado,
	 * dados seu índice e a frequência do indicador.
	 * 
	 * @param frequenciaIndicadorEnum
	 * @param indiceAcompanhamento
	 * @return
	 */
	public static Integer getAcompanhamentoTrimestre(FrequenciaIndicadorEnum frequenciaIndicadorEnum, Integer indiceAcompanhamento) {
		if (frequenciaIndicadorEnum.equals(FrequenciaIndicadorEnum.QUINZENAL)) {
			return indiceAcompanhamento / 6;
		}
		else if (frequenciaIndicadorEnum.equals(FrequenciaIndicadorEnum.MENSAL)) {
			return indiceAcompanhamento / 3;
		}
		else if (frequenciaIndicadorEnum.equals(FrequenciaIndicadorEnum.TRIMESTRAL)) {
			return indiceAcompanhamento;
		}
		else {
			return -1;
		}
	}	
	
}
 

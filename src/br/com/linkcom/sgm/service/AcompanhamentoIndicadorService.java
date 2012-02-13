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

import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.List;

import br.com.linkcom.sgm.beans.AcaoPreventiva;
import br.com.linkcom.sgm.beans.AcompanhamentoIndicador;
import br.com.linkcom.sgm.beans.Anomalia;
import br.com.linkcom.sgm.beans.Indicador;
import br.com.linkcom.sgm.dao.AcompanhamentoIndicadorDAO;
import br.com.linkcom.sgm.util.CalendarComparator;
import br.com.linkcom.sgm.util.neo.service.GenericService;

public class AcompanhamentoIndicadorService extends	GenericService<AcompanhamentoIndicador> {
	
	private AcompanhamentoIndicadorDAO acompanhamentoIndicadorDAO;
	private IndicadorService indicadorService;
	
	public void setAcompanhamentoIndicadorDAO(AcompanhamentoIndicadorDAO acompanhamentoIndicadorDAO) {this.acompanhamentoIndicadorDAO = acompanhamentoIndicadorDAO;}
	public void setIndicadorService(IndicadorService indicadorService) {this.indicadorService = indicadorService;}
	
	public List<AcompanhamentoIndicador> obtemAcompanhamentos(Indicador indicador) {
		return acompanhamentoIndicadorDAO.obtemAcompanhamentos(indicador);
	}
	
	/**
	 * Seta para null a propriedade anomalia de todos os acompanhamentos indicadores
	 * @author Rodrigo Duarte
	 * @param anomalia
	 */
	public void setNullAnomalia( Anomalia anomalia){
		acompanhamentoIndicadorDAO.setNullAnomalia(anomalia);
	}
	
	/**
	 * Seta para null a propriedade acaoPreventiva de todos os acompanhamentos indicadores
	 * @author Rodrigo Duarte
	 * @param acaoPreventiva
	 */
	public void setNullAcaoPreventiva( AcaoPreventiva acaoPreventiva){
		acompanhamentoIndicadorDAO.setNullAcaoPreventiva(acaoPreventiva);
	}
	
		
	@SuppressWarnings("unchecked")
	public List<Calendar> getListaDataAcompanhamentoIndicador(List<Indicador> listaIndicador, Calendar dataLimiteInicial, Calendar dataLimiteFinal) {
		List<Calendar> listaDataInicial = new ArrayList<Calendar>();
		List<AcompanhamentoIndicador> listaAcompanhamentoIndicador;
		
		for (Indicador indicador : listaIndicador) {
			listaAcompanhamentoIndicador = acompanhamentoIndicadorDAO.getListaDataAcompanhamentoIndicador(indicador);
			for (AcompanhamentoIndicador acompanhamentoIndicador : listaAcompanhamentoIndicador) {
				if (acompanhamentoIndicador.getDataInicial().compareTo(dataLimiteInicial) >=0 && acompanhamentoIndicador.getDataInicial().compareTo(dataLimiteFinal) <= 0) {
					if (!listaDataInicial.contains(acompanhamentoIndicador.getDataInicial())) {
						listaDataInicial.add(acompanhamentoIndicador.getDataInicial());
					}
				}
			}
		}
		Collections.sort(listaDataInicial);
		return listaDataInicial;
	}
	
	public void updateValorBase(Indicador indicador) {
		
		// Obtém o fator de divisão para saber quantos acompanhamentos devem ser salvos
		indicadorService.obtemFrequencia(indicador);
		int fatorDivisao = indicador.getFrequencia().getFatorDivisao();
		
		// Caso tenha acompanhamento para salvar
		if (indicador.getAcompanhamentosIndicador().size() > 0) {
			for (AcompanhamentoIndicador acompanhamentoIndicador : indicador.getAcompanhamentosIndicador()) {
				acompanhamentoIndicador.setIndicador(indicador);
				acompanhamentoIndicador.setValorBaseOK(false);
				
				if (Boolean.TRUE.equals(acompanhamentoIndicador.getNaoaplicavel())) {
					acompanhamentoIndicador.setValorLimiteInferior(null);
					acompanhamentoIndicador.setValorLimiteSuperior(null);
					acompanhamentoIndicador.setValorReal(null);
					acompanhamentoIndicador.setValorBaseOK(true);
				}
				
				switch (indicador.getMelhor()) {
				
					case MELHOR_CIMA:
						if (acompanhamentoIndicador.getValorLimiteSuperior() != null) {
							acompanhamentoIndicador.setValorBaseOK(true);
						}
						acompanhamentoIndicador.setValorLimiteInferior(null);
						break;
						
					case MELHOR_ENTRE_FAIXAS:
						if (acompanhamentoIndicador.getValorLimiteSuperior() != null && acompanhamentoIndicador.getValorLimiteInferior() != null) {
							acompanhamentoIndicador.setValorBaseOK(true);
						}
						break;
							
					case MELHOR_BAIXO:
						if (acompanhamentoIndicador.getValorLimiteInferior() != null) {
							acompanhamentoIndicador.setValorBaseOK(true);
						}
						acompanhamentoIndicador.setValorLimiteSuperior(null);						
						break;
	
					default:						
						break;
				}
				
				if (fatorDivisao-- > 0) {
					acompanhamentoIndicadorDAO.saveOrUpdate(acompanhamentoIndicador);
				}
				else if (acompanhamentoIndicador.getId() != null) {
					acompanhamentoIndicadorDAO.delete(acompanhamentoIndicador);
				}
			}
		}
	}

	/**
	 * Altera apenas o valor real do acompanhamento indicador
	 * @author Rodrigo Duarte
	 * @param acompanhamentoIndicador
	 * @param valorReal
	 */
	public void updateValorReal(AcompanhamentoIndicador acompanhamentoIndicador, Double valorReal) {
		acompanhamentoIndicadorDAO.updateValorReal(acompanhamentoIndicador, valorReal);	
	}

	/**
	 * Liga uma anomalia a um acompanhamentoIndicador
	 * @author Rodrigo Duarte
	 * @param acompanhamentoIndicador
	 * @param anomalia
	 */
	public void updateAnomalia(AcompanhamentoIndicador acompanhamentoIndicador, Anomalia anomalia) {
		acompanhamentoIndicadorDAO.updateAnomalia(acompanhamentoIndicador, anomalia);		
	}
	
	/**
	 * Liga uma ação preventiva a um acompanhamentoIndicador
	 * @author Rodrigo Alvarenga
	 * @param acompanhamentoIndicador
	 * @param acaoPreventiva
	 */
	public void updateAcaoPreventiva(AcompanhamentoIndicador acompanhamentoIndicador, AcaoPreventiva acaoPreventiva) {
		acompanhamentoIndicadorDAO.updateAcaoPreventiva(acompanhamentoIndicador, acaoPreventiva);		
	}

	/**
	 * Define se um acompanhamento de indicador pode ser alterado ou não
	 * Regra: só pode ser alterado caso o usuário logado seja administrador ou participante da unidade gerencial (responsável ou apoio)
	 * e a data para travamento do lançamento do respectivo trimestre não tenha sido atingida.
	 * 
	 * @author Rodrigo Alvarenga
	 * 
	 * @param acompanhamentoIndicador
	 * @param isUsuarioAdmin
	 * @param isUsuarioResponsavelUG
	 * @param isUsuarioApoioUG
	 * @param isDtTravLancResExp
	 * @return verdadeiro ou falso
	 */
	public Boolean podeAlterar(AcompanhamentoIndicador acompanhamentoIndicador, Boolean isUsuarioAdmin, Boolean isUsuarioResponsavelUG, Boolean isUsuarioApoioUG, Boolean isDtTravLancResExp) {
		if (isUsuarioAdmin) {
			return true;
		}
		if (isUsuarioResponsavelUG || isUsuarioApoioUG) {
			if (!isDtTravLancResExp) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Define se o acompanhamento pode ser mostrado ou não
	 * @param acompanhamentoIndicador
	 * @return
	 */
	public Boolean podeMostrar(AcompanhamentoIndicador acompanhamentoIndicador) {
		
		if (acompanhamentoIndicador.getId() != null && acompanhamentoIndicador.getValorBaseOK() == null) {
			acompanhamentoIndicador=this.load(acompanhamentoIndicador);
		}
		
		return acompanhamentoIndicador.getValorBaseOK();
	}
	
	/**
	 * Verifica se o limite superior do acompanhamento é maior ou igual ao limite inferior.
	 * 
	 * @param acompanhamentoIndicador
	 * @return
	 */
	public Boolean verificaMelhorIndicador(AcompanhamentoIndicador acompanhamentoIndicador) {
		Double valorLimiteInferior = acompanhamentoIndicador.getValorLimiteInferior();
		Double valorLimiteSuperior = acompanhamentoIndicador.getValorLimiteSuperior();
		
		return ( valorLimiteSuperior >= valorLimiteInferior );
	}
	
	/**
	 * Verifica se o prazo para reenvio do lembrete para lançamento de resultados já foi atingido.
	 * 
	 * @param dtUltimoEnvioLembrete
	 * @param diasReenvio
	 * @return
	 */
	public Boolean isPrazoReenvioLembLancResAtingido(Date dtUltimoEnvioLembrete, Integer diasReenvio) {
		if (dtUltimoEnvioLembrete == null) {
			return true;
		}
		Calendar calUltimoEnvioLembrete = new GregorianCalendar();
		calUltimoEnvioLembrete.setTime(dtUltimoEnvioLembrete);
		calUltimoEnvioLembrete.add(Calendar.DAY_OF_MONTH, diasReenvio);
		return CalendarComparator.getDataAtualSemHora().compareTo(calUltimoEnvioLembrete) >= 0;
	}

}

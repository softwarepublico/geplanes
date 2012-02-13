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
package br.com.linkcom.sgm.quartzjobs;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import br.com.linkcom.neo.core.standard.Neo;
import br.com.linkcom.sgm.beans.AcompanhamentoIndicador;
import br.com.linkcom.sgm.beans.Indicador;
import br.com.linkcom.sgm.beans.LogProcesso;
import br.com.linkcom.sgm.beans.PlanoGestao;
import br.com.linkcom.sgm.service.AcompanhamentoIndicadorService;
import br.com.linkcom.sgm.service.IndicadorService;
import br.com.linkcom.sgm.service.LogProcessoService;
import br.com.linkcom.sgm.service.PlanoGestaoService;
import br.com.linkcom.sgm.util.calculos.CalculosAuxiliares;

public class AtualizaLancamentoResultadosJOB implements Job {

	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		atualizaLancamentoResultados();
	}	
	
	public void atualizaLancamentoResultados() {

		// Services
		PlanoGestaoService planoGestaoService = Neo.getObject(PlanoGestaoService.class);
		IndicadorService indicadorService = Neo.getObject(IndicadorService.class);
		AcompanhamentoIndicadorService acompanhamentoIndicadorService = Neo.getObject(AcompanhamentoIndicadorService.class);
		LogProcessoService logProcessoService = Neo.getObject(LogProcessoService.class);
		
		// Listas
		List<AcompanhamentoIndicador> listaAcompanhamentoIndicador;
		
		// Geral
		Integer trimestreAcompanhamento;
		
		// Busca o ano de gestão atual
		PlanoGestao planoGestaoAtual = planoGestaoService.obtemPlanoGestaoAtual();		
		
		// Busca todos os indicadores cadastrados para o ano em questão
		List<Indicador> listaIndicador = indicadorService.findBy(planoGestaoAtual, null, null, "indicador.id");
		
		if (listaIndicador != null && !listaIndicador.isEmpty()) {
			
			System.out.println("------------------------------------------------------------");
			System.out.println(" Iniciando processo de atualização lançamento de resultados ");
			System.out.println("------------------------------------------------------------");			
			
			for (Indicador indicador : listaIndicador) {
				
				listaAcompanhamentoIndicador = new ArrayList<AcompanhamentoIndicador>(indicador.getAcompanhamentosIndicador());
				for (AcompanhamentoIndicador acompanhamentoIndicador : listaAcompanhamentoIndicador) {
					acompanhamentoIndicador.setIndicador(indicador);
					
					trimestreAcompanhamento = CalculosAuxiliares.getAcompanhamentoTrimestre(indicador.getFrequencia(), acompanhamentoIndicador.getIndice());
					
					// Somente para os acompanhamentos aplicáveis ao período
					if (!Boolean.TRUE.equals(acompanhamentoIndicador.getNaoaplicavel())) {
						//Se o resultado não tiver sido lançado e a data limite para lançamento esteja expirada, atualiza com o valor 0.
						if (acompanhamentoIndicador.getValorReal() == null && planoGestaoService.isDtTravLancResultadosExpirada(planoGestaoAtual, trimestreAcompanhamento)) {
								
								System.out.println("Inserindo o valor 0 para o resultado do acompanhamento: " + acompanhamentoIndicador.getId());
								
								// Atualiza o valor no banco de dados;
								acompanhamentoIndicadorService.updateValorReal(acompanhamentoIndicador, 0.0);
						}
					}
				}
			}
			
			System.out.println("--------------------------------------------------------------");
			System.out.println(" Finalizando processo de atualização lançamento de resultados ");
			System.out.println("--------------------------------------------------------------");			
		}
		
		// Atualiza no banco de dados a data de execução do processo
		LogProcesso logProcesso = logProcessoService.getLogProcessoAtual();
		logProcesso.setDtAtualizaLancamentoResultados(new Timestamp(System.currentTimeMillis()));
		logProcessoService.saveOrUpdate(logProcesso);
	}	
}
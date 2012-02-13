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
import java.util.List;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import br.com.linkcom.neo.core.standard.Neo;
import br.com.linkcom.sgm.beans.Anomalia;
import br.com.linkcom.sgm.beans.LogProcesso;
import br.com.linkcom.sgm.beans.ParametrosSistema;
import br.com.linkcom.sgm.beans.enumeration.StatusAnomaliaEnum;
import br.com.linkcom.sgm.service.AnomaliaService;
import br.com.linkcom.sgm.service.LogProcessoService;
import br.com.linkcom.sgm.service.ParametrosSistemaService;

public class AtualizaStatusAnomaliaJOB implements Job {

	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		atualizaStatusAnomalia();
	}	
	
	public void atualizaStatusAnomalia() {

		// Services
		AnomaliaService anomaliaService = Neo.getObject(AnomaliaService.class);
		LogProcessoService logProcessoService = Neo.getObject(LogProcessoService.class);
		
		ParametrosSistema parametrosSistema = ParametrosSistemaService.getParametrosSistema();
		
		// Busca todas as anomalias cadastradas no sistema
		List<Anomalia> listaAnomalia = anomaliaService.findForAtualizacaoStatus();
		
		StatusAnomaliaEnum statusAtual;
		StatusAnomaliaEnum statusIdeal;
		
		if (listaAnomalia != null) {
			
			System.out.println("-----------------------------------------------------------");
			System.out.println(" Iniciando processo de atualização de status das anomalias ");
			System.out.println("-----------------------------------------------------------");			
			
			for (Anomalia anomalia : listaAnomalia) {
				statusAtual = anomalia.getStatus();
				statusIdeal = anomaliaService.getStatusAnomalia(anomalia, parametrosSistema.getDiasTravTratAnomalia(), parametrosSistema.getDiasEncerramentoAnomalia());
				
				if (!statusAtual.equals(statusIdeal)) {
					System.out.println("Anomalia: " + anomalia.getId() + " => Status Atual: " + statusAtual.toString() + " Status Ideal: " + statusIdeal.toString());
					
					// Atualiza o status da anomalia
					anomaliaService.atualizaStatusAnomalia(anomalia, statusIdeal);
				}
			}
			
			System.out.println("-------------------------------------------------------------");
			System.out.println(" Finalizando processo de atualização de status das anomalias ");
			System.out.println("-------------------------------------------------------------");			
		}
		
		// Atualiza no banco de dados a data de execução do processo
		LogProcesso logProcesso = logProcessoService.getLogProcessoAtual();
		logProcesso.setDtAtualizaStatusAnomalia(new Timestamp(System.currentTimeMillis()));
		logProcessoService.saveOrUpdate(logProcesso);
	}	
}
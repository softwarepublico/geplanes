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
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import br.com.linkcom.neo.core.standard.Neo;
import br.com.linkcom.sgm.beans.LogProcesso;
import br.com.linkcom.sgm.beans.ParametrosSistema;
import br.com.linkcom.sgm.beans.PlanoGestao;
import br.com.linkcom.sgm.beans.Usuario;
import br.com.linkcom.sgm.service.LogProcessoService;
import br.com.linkcom.sgm.service.ParametrosSistemaService;
import br.com.linkcom.sgm.service.PlanoGestaoService;
import br.com.linkcom.sgm.service.UsuarioService;
import br.com.linkcom.sgm.util.CalendarComparator;
import br.com.linkcom.sgm.util.Nomes;


public class EnviaEmailLembreteCriacaoIndicadoresJOB extends EnviaEmailLembreteJOB {
	
	public EnviaEmailLembreteCriacaoIndicadoresJOB() {
		super();
		setAssunto("[Geplanes] - Data Limite para Criação de Indicadores");
		setTexto(
				"<table width='100%'>" +
				"	<tr>" +
				"		<td>" +
				"			<img src='cid:img0'>" +
				"		</td> " +
				"	</tr>" +
				"</table>" +
				"<table width='100%' cellspacing='10' style='border-bottom: 5px solid #CCCCCC; border-top: 5px solid #CCCCCC; background-color: #EEEEEE' >" +
				"	<tr>" +						
				"		<td>" +
				"			<span style='font-size: 12px; font-weight: normal; color: #444444'>" +				
				"			Prezado Usuário --U--,<br>" +				
				"			A data limite para criação dos Indicadores do "+ Nomes.plano_de_gestao +" <b>--P--</b> é: <b>--D--</b>" +
				"			</span>" +
				"		</td>" +
				"	</tr>" +
				"</table>" +
				"<table width='100%' >" +
				"	<tr align='right'>" +
				"		<td><img src='cid:img1'></td>" +
				"	</tr>" +
				"</table>"
				);				
	}	
	
	public synchronized void enviaEmail() {		
		PlanoGestaoService planoGestaoService = Neo.getObject(PlanoGestaoService.class);
		UsuarioService usuarioService = Neo.getObject(UsuarioService.class);
		LogProcessoService logProcessoService = Neo.getObject(LogProcessoService.class);
		boolean enviado;
		List<Usuario> listaUsuarios = new ArrayList<Usuario>();
		ParametrosSistema parametrosSistema = ParametrosSistemaService.getParametrosSistema();	
		int diasLembreteCriacaoMetasIndicadores = parametrosSistema.getDiasLembreteCriacaoMetasIndicadores() != null ? parametrosSistema.getDiasLembreteCriacaoMetasIndicadores().intValue() : 0;
		Calendar dataAtual = CalendarComparator.getDataAtualSemHora();

		List<PlanoGestao> listaPlanoGestao = planoGestaoService.findLembreteCriacaoMetasIndicadoresNaoEnviado();
		for (PlanoGestao planoGestao : listaPlanoGestao) {
			Calendar dataInicioLimite = getDataInicioLimite(planoGestao.getLimiteCriacaoMetasIndicadores(),diasLembreteCriacaoMetasIndicadores);			
			Calendar dataFimLimite    = new GregorianCalendar();
			dataFimLimite.setTime(planoGestao.getLimiteCriacaoMetasIndicadores());			
			if (dataAtual.compareTo(dataInicioLimite) >= 0 && dataAtual.compareTo(dataFimLimite) <= 0) {
				listaUsuarios = usuarioService.findResponsaveisByPlanoGestao(planoGestao);
				enviado = envia(parametrosSistema.getEmailRemetente(), listaUsuarios, planoGestao.getAnoExercicio().toString(),planoGestao.getLimiteCriacaoMetasIndicadores());
				
				if (enviado) {
					planoGestao.setLembreteCriacaoMetasIndicadores(Boolean.TRUE);
					planoGestaoService.saveOrUpdate(planoGestao);
				}				
			}
		}
		
		// Atualiza no banco de dados a data de execução do processo
		LogProcesso logProcesso = logProcessoService.getLogProcessoAtual();
		logProcesso.setDtLembCriacaoIndicador(new Timestamp(System.currentTimeMillis()));
		logProcessoService.saveOrUpdate(logProcesso);		
	}
}

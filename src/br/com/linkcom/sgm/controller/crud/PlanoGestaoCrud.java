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
package br.com.linkcom.sgm.controller.crud;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import org.springframework.validation.BindException;

import br.com.linkcom.neo.authorization.crud.CrudAuthorizationModule;
import br.com.linkcom.neo.controller.Controller;
import br.com.linkcom.neo.controller.MessageType;
import br.com.linkcom.neo.controller.crud.CrudController;
import br.com.linkcom.neo.core.web.WebRequestContext;
import br.com.linkcom.sgm.beans.PlanoGestao;
import br.com.linkcom.sgm.filtro.PlanoGestaoFiltro;
import br.com.linkcom.sgm.service.PlanoGestaoService;
import br.com.linkcom.sgm.util.Nomes;

@Controller(path="/sgm/crud/PlanoGestao", authorizationModule=CrudAuthorizationModule.class)
public class PlanoGestaoCrud extends CrudController<PlanoGestaoFiltro, PlanoGestao, PlanoGestao> {
	
	private PlanoGestaoService planoGestaoService;
		
	public void setPlanoGestaoService(PlanoGestaoService planoGestaoService) {this.planoGestaoService = planoGestaoService;}
	
	@Override
	protected void validateBean(PlanoGestao bean, BindException errors) {
		
		Integer anoExercicio = bean.getAnoExercicio();
		
		Calendar anoLimiteMetasIndicadas = new GregorianCalendar();
		anoLimiteMetasIndicadas.setTime( bean.getLimiteCriacaoMetasIndicadores() );
		
		List<PlanoGestao> listaPlanoGestao = planoGestaoService.findAll();
		for (PlanoGestao planoGestao : listaPlanoGestao) {
			if(planoGestao.getAnoExercicio().equals(anoExercicio)) {
				if (bean.getCopia() != null && bean.getCopia()) {
					errors.rejectValue("anoExercicio", "","Cadastro não realizado. Já existe "+ Nomes.plano_de_gestao +" com este ano de exercício." );	
				}
				else {
					if (!planoGestao.getId().equals(bean.getId())) {
						errors.rejectValue("anoExercicio", "","Cadastro não realizado. Já existe "+ Nomes.plano_de_gestao +" com este ano de exercício." );
					}
				}
			}								
		}		
		
		if (bean.getDtLimLancRes1T().getTime() > bean.getDtTravLancRes1T().getTime()) {
			errors.rejectValue("dtLimLancRes1T", "","é posterior à respectiva data para travamento do lançamento" );
		}
		
		if (bean.getDtLimLancRes2T().getTime() > bean.getDtTravLancRes2T().getTime()) {
			errors.rejectValue("dtLimLancRes2T", "","é posterior à respectiva data para travamento do lançamento" );
		}
		
		if (bean.getDtLimLancRes3T().getTime() > bean.getDtTravLancRes3T().getTime()) {
			errors.rejectValue("dtLimLancRes3T", "","é posterior à respectiva data para travamento do lançamento" );
		}
		
		if (bean.getDtLimLancRes4T().getTime() > bean.getDtTravLancRes4T().getTime()) {
			errors.rejectValue("dtLimLancRes4T", "","é posterior à respectiva data para travamento do lançamento" );
		}
	}
	
	@Override
	protected void entrada(WebRequestContext request, PlanoGestao form)	throws Exception {
		if("true".equals(request.getParameter("copiar"))) {
			form.setCopia(true);
		}
		
		super.entrada(request, form);
	}	
	
	@Override
	protected void salvar(WebRequestContext request, PlanoGestao bean) throws Exception {
		if (bean.getCopia() != null && bean.getCopia()) {
			copiaPlanoGestao(request, bean);
			request.addMessage(Nomes.Plano_de_Gestao + ", unidades gerenciais, iniciativas e indicadores copiados com sucesso", MessageType.INFO);
		}
		else {
			super.salvar(request, bean);
			request.addMessage(Nomes.Plano_de_Gestao + " salvo com sucesso", MessageType.INFO);
		}		
	}

	private void copiaPlanoGestao(WebRequestContext request, PlanoGestao planoGestaoNovo) throws Exception {				
		PlanoGestao planoGestaoAntigo;

		planoGestaoAntigo = new PlanoGestao();
		planoGestaoAntigo.setId(planoGestaoNovo.getId());
		
		planoGestaoNovo.setId(null);
		
		planoGestaoService.copiaPlanoGestao(planoGestaoAntigo, planoGestaoNovo);
	}
	
}

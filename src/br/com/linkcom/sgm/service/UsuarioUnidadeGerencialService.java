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
import java.util.List;

import br.com.linkcom.sgm.util.neo.service.GenericService;
import br.com.linkcom.sgm.beans.PlanoGestao;
import br.com.linkcom.sgm.beans.UnidadeGerencial;
import br.com.linkcom.sgm.beans.UsuarioUnidadeGerencial;
import br.com.linkcom.sgm.beans.enumeration.FuncaoUGEnum;
import br.com.linkcom.sgm.dao.UsuarioUnidadeGerencialDAO;

public class UsuarioUnidadeGerencialService extends GenericService<UsuarioUnidadeGerencial> {
	
	private UsuarioUnidadeGerencialDAO usuarioUnidadeGerencialDAO;
	private UnidadeGerencialService unidadeGerencialService;
	
	public void setUnidadeGerencialService(UnidadeGerencialService unidadeGerencialService) {
		this.unidadeGerencialService = unidadeGerencialService;
	}
	
	public void setUsuarioUnidadeGerencialDAO(UsuarioUnidadeGerencialDAO usuarioUnidadeGerencialDAO) {
		this.usuarioUnidadeGerencialDAO = usuarioUnidadeGerencialDAO;
	}
	
	public List<UsuarioUnidadeGerencial> findByUnidadeGerencial(UnidadeGerencial unidadeGerencial) {		 
		return usuarioUnidadeGerencialDAO.findByUnidadeGerencial(unidadeGerencial, null);
	}
	
	public List<UsuarioUnidadeGerencial> findResponsaveisByUnidadeGerencial(UnidadeGerencial unidadeGerencial) {		 
		return usuarioUnidadeGerencialDAO.findByUnidadeGerencial(unidadeGerencial, FuncaoUGEnum.RESPONSAVEL);
	}	
	
	public void vincular(UsuarioUnidadeGerencial usuarioUnidadeGerencial) {	
		saveOrUpdate(usuarioUnidadeGerencial);
	}
	
	public void desvincular(UsuarioUnidadeGerencial usuarioUnidadeGerencial) {		
		delete(usuarioUnidadeGerencial);		
	}	
		
	public List<UsuarioUnidadeGerencial> findUsuariosQualidadeEnvolvidoAnomalia(PlanoGestao planoGestao) {
		
		List<UsuarioUnidadeGerencial> listaUsuarioUnidadeGerencial = null;
		
		List<UnidadeGerencial> listaUnidadeGerencialQualidade = unidadeGerencialService.findUGQualidade(planoGestao);
		
		if (listaUnidadeGerencialQualidade != null) {
			listaUsuarioUnidadeGerencial = new ArrayList<UsuarioUnidadeGerencial>();
			for (UnidadeGerencial unidadeGerencialQualidade : listaUnidadeGerencialQualidade) {
				listaUsuarioUnidadeGerencial.addAll(findResponsaveisByUnidadeGerencial(unidadeGerencialQualidade));
			}
		}
		
		return listaUsuarioUnidadeGerencial;		
	}	
			
}

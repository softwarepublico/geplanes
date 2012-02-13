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
package br.com.linkcom.sgm.dao;

import java.util.ArrayList;
import java.util.List;

import br.com.linkcom.sgm.util.neo.persistence.GenericDAO;
import br.com.linkcom.sgm.beans.AcaoPreventiva;
import br.com.linkcom.sgm.beans.AcompanhamentoIndicador;
import br.com.linkcom.sgm.beans.Anomalia;
import br.com.linkcom.sgm.beans.Indicador;


public class AcompanhamentoIndicadorDAO extends	GenericDAO<AcompanhamentoIndicador> {
	
	public List<AcompanhamentoIndicador> obtemAcompanhamentos(Indicador indicador) {
		List<AcompanhamentoIndicador> retorno = new ArrayList<AcompanhamentoIndicador>();
		if (indicador != null && indicador.getId() != null) {
			retorno = 
				query()
					.join("acompanhamentoIndicador.indicador indicador")
					.where("indicador = ?", indicador)
					.orderBy("acompanhamentoIndicador.dataInicial")
					.list();
		}
		
		return retorno;
	}
	
	/**
	 * @author Rodrigo Duarte
	 * @param anomalia
	 */
	public void setNullAnomalia( Anomalia anomalia){
		getJdbcTemplate().update("update acompanhamentoIndicador set anomalia_id=null where anomalia_id=?", new Object[]{anomalia.getId()});
	}
	
	/**
	 * @author Rodrigo Duarte
	 * @param acaoPreventiva
	 */
	public void setNullAcaoPreventiva( AcaoPreventiva acaoPreventiva){
		getJdbcTemplate().update("update acompanhamentoIndicador set acaoPreventiva_id=null where acaoPreventiva_id=?", new Object[]{acaoPreventiva.getId()});
	}
	
	public List<AcompanhamentoIndicador> getListaDataAcompanhamentoIndicador(Indicador indicador) {
		if (indicador == null) {
			return new ArrayList<AcompanhamentoIndicador>();
		}
		else {
			return
				query()
				.select("acompanhamentoIndicador.dataInicial")
				.leftOuterJoin("acompanhamentoIndicador.indicador indicador")
				.where("indicador = ?", indicador)
				.list();
		}
	}
	
	/**
	 * @author Rodrigo Duarte
	 * @param acompanhamentoIndicador
	 * @param valorReal
	 * @param usuario 
	 */
	public void updateValorReal(AcompanhamentoIndicador acompanhamentoIndicador, Double valorReal) {
		getJdbcTemplate().update("update acompanhamentoIndicador set valorReal="+valorReal+" where id=?", new Object[]{acompanhamentoIndicador.getId()});		
	}

	/**
	 * @author Rodrigo Duarte
	 * @param acompanhamentoIndicador
	 * @param anomalia
	 */
	public void updateAnomalia(AcompanhamentoIndicador acompanhamentoIndicador, Anomalia anomalia) {
		getJdbcTemplate().update("update acompanhamentoIndicador set anomalia_id="+anomalia.getId()+" where id=?", new Object[]{acompanhamentoIndicador.getId()});
		
	}
	
	public void updateAcaoPreventiva(AcompanhamentoIndicador acompanhamentoIndicador, AcaoPreventiva acaoPreventiva) {
		getJdbcTemplate().update("update acompanhamentoIndicador set acaoPreventiva_id="+acaoPreventiva.getId()+" where id=?", new Object[]{acompanhamentoIndicador.getId()});
		
	}	

}

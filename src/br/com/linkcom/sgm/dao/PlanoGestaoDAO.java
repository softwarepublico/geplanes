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

import java.util.Calendar;
import java.util.List;

import br.com.linkcom.neo.controller.crud.FiltroListagem;
import br.com.linkcom.neo.persistence.DefaultOrderBy;
import br.com.linkcom.sgm.util.neo.persistence.GenericDAO;
import br.com.linkcom.neo.persistence.QueryBuilder;
import br.com.linkcom.sgm.beans.Indicador;
import br.com.linkcom.sgm.beans.PlanoGestao;
import br.com.linkcom.sgm.filtro.PlanoGestaoFiltro;


@DefaultOrderBy("anoExercicio DESC")
public class PlanoGestaoDAO extends GenericDAO<PlanoGestao> {

	@Override
	public void updateListagemQuery(QueryBuilder<PlanoGestao> query, FiltroListagem _filtro) {
		PlanoGestaoFiltro filtro = (PlanoGestaoFiltro) _filtro;
		query
			.where("anoExercicio=?", filtro.getAnoExercicio())
			.whereLikeIgnoreAll("descricao",filtro.getDescricao());
	}
	
	public PlanoGestao obtemAnoExercicio(PlanoGestao planoGestao) {
		return query()
			.select("planoGestao.id,planoGestao.anoExercicio")
			.from(PlanoGestao.class)
			.where("planoGestao = ?", planoGestao)
			.unique();
	}

	public List<PlanoGestao> findLembreteCriacaoMetasIndicadoresNaoEnviado() {
		return
			query()
				.openParentheses()
					.where("lembreteCriacaoMetasIndicadores IS NULL")
					.or()
					.where("lembreteCriacaoMetasIndicadores = FALSE")
				.closeParentheses()
				.list();
	}
	
	public PlanoGestao findByIndicador(Indicador indicador) {
		if (indicador == null) {
			return new PlanoGestao();
		}
		else {
			return
				query()
					.leftOuterJoin("planoGestao.unidadesGerenciais unidadeGerencial")
					.leftOuterJoin("unidadeGerencial.listaIndicador indicador")
					.where("indicador = ?", indicador)
					.unique();
		}
	}
	
	public PlanoGestao obtemPlanoGestaoAnoAtual(){
		Calendar c = Calendar.getInstance();
		return query()
			.where("anoExercicio = ?", c.get(Calendar.YEAR))
			.unique();
	}
	
	public PlanoGestao obtemPlanoGestaoComMaiorAnoExercicio(){
		List<PlanoGestao> retorno = 
			query()
				.orderBy("anoExercicio DESC")
				.setPageNumberAndSize(0, 1)
				.list();
		
		if (retorno != null && retorno.size() > 0 ) {
			return retorno.get(0);
		}
		
		return null;
	}
}
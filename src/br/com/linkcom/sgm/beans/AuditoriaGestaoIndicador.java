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
package br.com.linkcom.sgm.beans;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;

import br.com.linkcom.neo.types.ListSet;
import br.com.linkcom.neo.validation.annotation.Required;

@Entity
@SequenceGenerator(name = "sq_auditoriagestaoindicador", sequenceName = "sq_auditoriagestaoindicador")
public class AuditoriaGestaoIndicador {
	
	private Integer id;
	private AuditoriaGestao auditoriaGestao;
	private Indicador indicador;
	
	private List<AuditoriaGestaoIndicadorItem> listaAuditoriaGestaoIndicadorItem = new ListSet<AuditoriaGestaoIndicadorItem>(AuditoriaGestaoIndicadorItem.class);
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO, generator="sq_auditoriagestaoindicador")
	public Integer getId() {
		return id;
	}
	
	@Required
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="auditoriaGestao_id")
	public AuditoriaGestao getAuditoriaGestao() {
		return auditoriaGestao;
	}

	@Required
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="indicador_id")
	public Indicador getIndicador() {
		return indicador;
	}
	
	@OneToMany(mappedBy="auditoriaGestaoIndicador")
	public List<AuditoriaGestaoIndicadorItem> getListaAuditoriaGestaoIndicadorItem() {
		return listaAuditoriaGestaoIndicadorItem;
	}
	
	public void setListaAuditoriaGestaoIndicadorItem(
			List<AuditoriaGestaoIndicadorItem> listaAuditoriaGestaoIndicadorItem) {
		this.listaAuditoriaGestaoIndicadorItem = listaAuditoriaGestaoIndicadorItem;
	}

	public void setAuditoriaGestao(AuditoriaGestao auditoriaGestao) {
		this.auditoriaGestao = auditoriaGestao;
	}

	public void setIndicador(Indicador indicador) {
		this.indicador = indicador;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	
}

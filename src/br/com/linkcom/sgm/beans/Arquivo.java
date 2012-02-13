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

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Transient;

import br.com.linkcom.neo.bean.annotation.DescriptionProperty;
import br.com.linkcom.neo.types.File;
import br.com.linkcom.sgm.exception.GeplanesException;


@Entity
@SequenceGenerator(name = "sq_arquivo", sequenceName = "sq_arquivo")
public class Arquivo implements File {
	
	private Integer id;
	
	private String nome;
	private String contentType;
	private byte[] content;
	private byte[] conteudo;
	private Long size;
	
	
	//=========================Get e Set==================================//
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO, generator="sq_arquivo")
	public Integer getId() {
		return id;
	}
	@Transient
	public byte[] getContent() {
		return content;
	}

	@DescriptionProperty
	public String getNome() {
		return nome;
	}
	public void setContent(byte[] content) {
		this.content = content;
		this.conteudo = content;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	@Transient
	public Long getCdfile() {
		return id != null? new Long(id) : null;
	}
	@Transient
	public String getName() {
		return nome;
	}
	
	public String getContenttype() {
		return contentType;
	}
	public Long getSize() {
		return size;
	}
	@Transient
	public Timestamp getTsmodification() {
		return null;
	}
	public void setName(String name) {
		this.nome = name;
	}
	public void setContenttype(String contenttype) {
		this.contentType = contenttype;
	}
	public void setSize(Long size) {
		this.size = size;
	}
	public void setTsmodification(Timestamp tsmodification) {
	}
	public void setCdfile(Long cdfile) {
		if(cdfile != null){
			this.id = cdfile.intValue();
		} else {
			this.id = null;
		}
		
	}
	public byte[] getConteudo() {
		return conteudo;
	}
	public void setConteudo(byte[] conteudo) {
		this.conteudo = conteudo;
		try {
			this.content = conteudo;
		} catch (Exception e) {
			throw new GeplanesException("Erro ao ler arquivo "+id, e);
		}
	}
}

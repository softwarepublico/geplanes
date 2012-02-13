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
import br.com.linkcom.neo.types.ListSet;
import br.com.linkcom.sgm.beans.Anomalia;
import br.com.linkcom.sgm.beans.CausaEfeito;
import br.com.linkcom.sgm.dao.CausaEfeitoDAO;
import br.com.linkcom.sgm.exception.GeplanesException;


public class CausaEfeitoService extends GenericService<CausaEfeito>{

	private CausaEfeitoDAO causaEfeitoDAO;
	public void setCausaEfeitoDAO(CausaEfeitoDAO causaEfeitoDAO) {this.causaEfeitoDAO = causaEfeitoDAO;}


	/**
	 * Método responsável por salvar a causa e o efeito de uma determinada anomalia.
	 * @author Matheus Melo Gonçalves
	 * @param Anomalia
	 */
	public void salvarCausaEfeito(Anomalia anomalia) {
		//Busca o efeito
		CausaEfeito efeito = anomalia.getEfeito();
		
		if (efeito != null) {
			
			// No caso de um registro novo, verifica se já existe um registro no banco de dados,
			// pois só pode existir um efeito por anomalia.
			if (efeito.getId() == null) {
				CausaEfeito efeitoDB = this.findByAnomalia(anomalia);
				
				if (efeitoDB != null) {
					throw new GeplanesException("Atenção: já foi salvo um registro de causa efeito para essa anomalia. Retorne à listagem e consulte novamente a anomalia.");
				}
			}			
			
			//Salva o efeito
			efeito.setAnomalia(anomalia);
			if(efeito.getDescricao() == null){
				efeito.setDescricao("");
			}
			this.saveOrUpdate(efeito);
			
			//Salva as causas
			for (CausaEfeito causa : anomalia.getCausasEfeito()) {
				causa.setEfeito(efeito);
				if(causa.getDescricao() == null){
					causa.setDescricao("");
				}
				salvaCausa(causa);
			}
		}
	}
	
	public void salvaCausa(CausaEfeito causa) {
		this.saveOrUpdate(causa);
		if (causa.getListaCausaEfeito() != null) {
			for (CausaEfeito causaOrigem : causa.getListaCausaEfeito()){
				if(causaOrigem.getDescricao() == null){
					causaOrigem.setDescricao("");
				}
				causaOrigem.setEfeito(causa);
				salvaCausa(causaOrigem);
			}
		}		
	}
	
	/**
	 * Retorna o efeito de uma determinada anomalia.
	 * @author Matheus Melo Gonçalves
	 * @param anomalia
	 * @return CausaEfeito
	 */
	public CausaEfeito findByAnomalia(Anomalia anomalia){
		return causaEfeitoDAO.findByAnomalia(anomalia);
	}
	
	/**
	 * Retorna uma lista com as causas relacionadas a uma causa pai.
	 * @author Matheus Melo Gonçalves
	 * @param CausaEfeito
	 * @return List<CausaEfeito>
	 */
	public List<CausaEfeito> findByCausaFilha(CausaEfeito causaEfeito){
		return causaEfeitoDAO.findByCausaFilha(causaEfeito);
	}
	
	/**
	 * Excluir Causas e efeitos de uma determinada anomalia
	 * @author Matheus Melo Gonçalves
	 * @param Anomalia
	 */
	public void excluirCausaEfeito(Anomalia anomalia){
		CausaEfeito causaEfeito = this.findByAnomalia(anomalia);
		List<CausaEfeito> listaCompleta = this.findByCausaFilha(causaEfeito);
		
		for (CausaEfeito nivel1 : listaCompleta) {
			for (CausaEfeito nivel2 : this.findByCausaFilha(nivel1)){
				for (CausaEfeito nivel3 : this.findByCausaFilha(nivel2)) {
					for (CausaEfeito nivel4 : this.findByCausaFilha(nivel3)) {
						this.delete(nivel4);
					}
					this.delete(nivel3);
				}
				this.delete(nivel2);
			}
			this.delete(nivel1);
		}
		this.delete(causaEfeito);
	}	
	
	/**
	 * Retorna uma lista completa com a causa e efeito de uma determinada anomalia.
	 * @author Matheus Melo Gonçalves
	 * @param anomalia
	 * @return List<CausaEfeito>
	 */
	public List<CausaEfeito> preencheListaCausaEfeito(Anomalia anomalia){
		
		//Objeto
		CausaEfeito causaEfeito = anomalia.getEfeito();
		//Lista
		List<CausaEfeito> listaCausaEfeitoCompleta = new ArrayList<CausaEfeito>();
		
		if (causaEfeito != null) {
			listaCausaEfeitoCompleta = findByCausaFilha(causaEfeito);
			for (CausaEfeito nivel1 : listaCausaEfeitoCompleta) {
				nivel1.setListaCausaEfeito(new ListSet<CausaEfeito>(CausaEfeito.class,this.findByCausaFilha(nivel1)));
				for (CausaEfeito nivel2 : nivel1.getListaCausaEfeito()) {
					nivel2.setListaCausaEfeito(new ListSet<CausaEfeito>(CausaEfeito.class,this.findByCausaFilha(nivel2)));
					for (CausaEfeito nivel3 : nivel2.getListaCausaEfeito()) {
						nivel3.setListaCausaEfeito(new ListSet<CausaEfeito>(CausaEfeito.class,this.findByCausaFilha(nivel3)));
						for (CausaEfeito nivel4 : nivel3.getListaCausaEfeito()) {
							nivel4.setListaCausaEfeito(new ListSet<CausaEfeito>(CausaEfeito.class,this.findByCausaFilha(nivel4)));
						}
					}
				}
			}
		}
		return listaCausaEfeitoCompleta;
		
	}
}

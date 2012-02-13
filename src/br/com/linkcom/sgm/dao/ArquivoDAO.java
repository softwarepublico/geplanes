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

import java.beans.PropertyDescriptor;

import br.com.linkcom.neo.exception.NeoException;
import br.com.linkcom.neo.persistence.QueryBuilder;
import br.com.linkcom.neo.types.File;
import br.com.linkcom.neo.util.Util;
import br.com.linkcom.sgm.beans.Arquivo;
import br.com.linkcom.sgm.util.neo.persistence.GenericDAO;


public class ArquivoDAO extends GenericDAO<Arquivo> {

	public void saveFile(Object bean, String filePropertyName, boolean useTransaction) {
		Arquivo arquivoVelho = null;
		Arquivo arquivoAtual = null;
		PropertyDescriptor pd;
		try {
			pd = new PropertyDescriptor(filePropertyName, bean.getClass());
			arquivoAtual = (Arquivo) pd.getReadMethod().invoke(bean);
		} catch (Exception e) {
			throw new NeoException("Não foi possivel adquirir arquivo. Propriedade "+filePropertyName+" da classe "+bean.getClass(), e);
		}
		if(Util.hibernate.getId(getHibernateTemplate(), bean) != null){
			Object beanVelho = new QueryBuilder<Object>(getHibernateTemplate())
						.from(bean.getClass())
						.leftOuterJoinFetch(Util.strings.uncaptalize(bean.getClass().getSimpleName())+"."+filePropertyName+" "+filePropertyName)
						.entity(bean)
						.unique();
			try {
				arquivoVelho = (Arquivo) pd.getReadMethod().invoke(beanVelho);
			} catch (Exception e) {
				throw new NeoException("Não foi possivel adquirir arquivo. Propriedade "+filePropertyName+" da classe "+bean.getClass(), e);
			}
		}
		
		File save = save(arquivoAtual, arquivoVelho, useTransaction);
		try {
			pd.getWriteMethod().invoke(bean, save);
		} catch (Exception e) {
			throw new NeoException("Não foi possível setar o arquivo. Propriedade "+filePropertyName+" da classe "+bean.getClass(), e);
		}
	}
	
	public File save(Arquivo arquivoNovo, Arquivo arquivoVelho, boolean useTransaction) {
		try {
			if (arquivoVelho == null) {
				// criar
				if (arquivoNovo != null && arquivoNovo.getSize() > 0) {
					if (useTransaction) {
						saveOrUpdate(arquivoNovo);
					}
					else {
						saveOrUpdateWithoutTransaction(arquivoNovo);
					}
				} else {
					return null;
				}
			} else {
				// atualizar
				if(arquivoNovo == null){
					//apagar o arquivo
					getHibernateTemplate().delete(arquivoVelho);
				} else if(arquivoNovo.getSize() > 0){ 
					getHibernateTemplate().evict(arquivoVelho);
					arquivoNovo.setCdfile(arquivoVelho.getCdfile());
					//sobrescrever o arquivo
					getHibernateTemplate().saveOrUpdate(arquivoNovo);
				} else {
					//se o tamanho for zero não mexer no arquivo
					arquivoNovo.setCdfile(arquivoVelho.getCdfile());
				}
			}
			getHibernateTemplate().flush();
			return arquivoNovo;
		} catch (Exception e) {
			throw new NeoException("Não foi posível salvar o arquivo. ", e);
		}
	}	

}

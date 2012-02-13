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
package br.com.linkcom.sgm.util;

import java.lang.reflect.Method;
import java.util.List;

import br.com.linkcom.neo.core.standard.Neo;
import br.com.linkcom.neo.util.Util;
import br.com.linkcom.sgm.beans.PlanoGestao;
import br.com.linkcom.sgm.beans.UnidadeGerencial;
import br.com.linkcom.sgm.beans.Usuario;
import br.com.linkcom.sgm.exception.GeplanesException;
import br.com.linkcom.sgm.service.PlanoGestaoService;
import br.com.linkcom.sgm.service.UnidadeGerencialService;


public class FiltroUtils {

	/**
	 * Preenche o bean com o plano de gestão atual
	 * @author Rodrigo Alvarenga
	 * @param bean
	 * @return
	 */
	public static void preencheFiltroPlanoGestao(Object bean) {
		
		try {
			PlanoGestaoService planoGestaoService = PlanoGestaoService.getInstance();
			PlanoGestao planoGestaoAtual = planoGestaoService.obtemPlanoGestaoAtual();
			Method getterMethod;
			Method setterMethod;
			Object value;
			
			if (planoGestaoAtual != null) {				
				getterMethod = Util.beans.getGetterMethod(bean.getClass(), "planoGestao");
				setterMethod = Util.beans.getSetterMethod(bean.getClass(), "planoGestao");
				
				if (getterMethod != null) {
					value = getterMethod.invoke(bean);
					if (value == null && setterMethod != null) {
						setterMethod.invoke(bean, planoGestaoAtual);						 
					}
				}
			}
		}
		catch (Exception e) {
			throw new GeplanesException("Não foi possível setar o ano da gestão.");
		}
	}
	
	public static void preencheFiltroPlanoGestaoUnidadeUsuario(Object bean) {
		preencheFiltroPlanoGestaoUnidadeUsuario(bean, "unidadeGerencial");
	}	
	
	/**
	 * Preenche o bean com o plano de gestão atual bem como a unidade do usuário no exercício do mesmo
	 * @author Rodrigo Alvarenga
	 * @param bean
	 * @return
	 */
	public static void preencheFiltroPlanoGestaoUnidadeUsuario(Object bean, String labelUG) {
		
		try {
			PlanoGestaoService planoGestaoService = PlanoGestaoService.getInstance();
			PlanoGestao planoGestaoAtual = planoGestaoService.obtemPlanoGestaoAtual();
			UnidadeGerencialService unidadeGerencialService = UnidadeGerencialService.getInstance();
			UnidadeGerencial unidadeGerencial = null;
			List<UnidadeGerencial> lista = null;
			Method getterMethod;
			Method setterMethod;
			Object value;
			Usuario usuario;
			
			if (planoGestaoAtual != null) {				
				getterMethod = Util.beans.getGetterMethod(bean.getClass(), "planoGestao");
				setterMethod = Util.beans.getSetterMethod(bean.getClass(), "planoGestao");
				
				if (getterMethod != null) {
					value = getterMethod.invoke(bean);
					usuario = (Usuario) Neo.getRequestContext().getUser();					
					if (value == null && setterMethod != null) {
						setterMethod.invoke(bean, planoGestaoAtual);						 
						lista = unidadeGerencialService.loadByUsuarioPlanoGestao(usuario, planoGestaoAtual);
						if(lista != null && lista.size() == 1){
							unidadeGerencial = lista.get(0);
						}
					}
					else if (value != null) {
						lista = unidadeGerencialService.loadByUsuarioPlanoGestao(usuario, (PlanoGestao) value);
						if(lista != null && lista.size() == 1){
							unidadeGerencial = lista.get(0);
						}
					}
					
					if (unidadeGerencial != null) {			
						getterMethod = Util.beans.getGetterMethod(bean.getClass(), labelUG);
						setterMethod = Util.beans.getSetterMethod(bean.getClass(), labelUG);
							
						if (getterMethod != null) {
							value = getterMethod.invoke(bean);
							if (value == null && setterMethod != null) {
								setterMethod.invoke(bean, unidadeGerencial);
							}
						}
					}
				}
			}
		}
		catch (Exception e) {
			throw new GeplanesException("Não foi possível setar a Unidade Gerencial do usuário.");
		}
	}
}

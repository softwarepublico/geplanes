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

import java.lang.reflect.InvocationTargetException;
import java.text.CharacterIterator;
import java.text.StringCharacterIterator;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.PropertyUtils;

import br.com.linkcom.neo.bean.PropertyDescriptor;
import br.com.linkcom.neo.core.standard.Neo;
import br.com.linkcom.neo.exception.NeoException;
import br.com.linkcom.neo.types.ListSet;
import br.com.linkcom.neo.util.Util;
import br.com.linkcom.sgm.beans.Arquivo;
import br.com.linkcom.sgm.beans.ParametrosSistema;
import br.com.linkcom.sgm.service.ParametrosSistemaService;

public class GeplanesUtils {
	
	public static Double round(Double valor, int decimal){
		double d = Math.pow(10.0, decimal);
		return Math.round( valor * d ) / d;
	}
	
	public static Object getProperty(Object bean, String property){
		Object retorno = null;
		try {
			retorno = PropertyUtils.getProperty(bean, property.trim());
		} catch (IllegalAccessException e) {
			throw new NeoException("Não foi possível inicializar o atributo '"+ property +"', pois houve um acesso ilegal.");
		} catch (InvocationTargetException e) {
			throw new NeoException("Não foi possível inicializar o atributo '"+ property +"', pois houve um erro de invocação.");
		} catch (NoSuchMethodException e) {
			throw new NeoException("Não foi possível inicializar o atributo '"+ property +"', pois seu método getter não existe.");
		}
		return retorno;
	}
	
	public static void setProperty(Object bean, String property, Object value){
		try {
			PropertyUtils.setProperty(bean, property.trim(), value);
		} catch (IllegalAccessException e) {
			throw new NeoException("Não foi possível definir o atributo '"+ property +"', pois houve um acesso ilegal.");
		} catch (InvocationTargetException e) {
			throw new NeoException("Não foi possível definir o atributo '"+ property +"', pois houve um erro de invocação.");
		} catch (NoSuchMethodException e) {
			throw new NeoException("Não foi possível definir o atributo '"+ property +"', pois seu método setter não existe.");
		}
	}
	
	public static String escape(String s){
		if(s==null) return null;
		return s
				.replaceAll("\\\\", "\\\\\\\\")
				.replaceAll("'", "\\\\'")
				.replaceAll("\n", "\\\\n")
				.replaceAll("\r", "\\\\r")
				;
	}
	
	public static String escape2(String s){
		if(s==null) return null;
		return s.replaceAll("\\\\", "\\\\\\\\")
				.replaceAll("\"", "\\\\\"")
				.replaceAll("'", "\\\\\\\\\\'");
	}
	
	/**
	 * Concatena todos os elementos de uma determinada collection e insere o token entre cada elemento
	 * @param collection Coleção a ser iteragida
	 * @param token String que deve ser usada entre cada elemento
	 * @return
	 */
	public static String concatenate(Collection<?> collection, String token){
		StringBuilder builder = new StringBuilder();
		for (Iterator<?> iter = collection.iterator(); iter.hasNext();) {
			Object o = iter.next();
			builder.append(o);
			if(iter.hasNext()){
				builder.append(token);
			}
		}
		return builder.toString();
	}
	
	public static boolean hasAuthorization(String url, String action, HttpServletRequest request) {
		try {		
			String partialURL = getPartialURL(url, request);
			if(partialURL.contains("?")){
				partialURL = partialURL.substring(0, partialURL.indexOf('?'));
			}
			return Neo.getApplicationContext().getAuthorizationManager().isAuthorized(partialURL, action, Neo.getUser());
		} catch (Exception e) {
			throw new NeoException("Problema ao verificar autorização", e);
		}
	}
	
	public static String getApplicationPath(HttpServletRequest request) {
		if (request != null) {
			String url = request.getRequestURL().toString();
			String[] urlDividida = url.split("/");
			return urlDividida[0] + "/" + urlDividida[1] + "/" + urlDividida[2] + "/" + urlDividida[3];
		}
		return null;
	}
	
	public static String getPartialURL(String url, HttpServletRequest request){
		if (url != null && url.startsWith(request.getContextPath())) {
			return url.substring(request.getContextPath().length());
		}
		String fullUrl = url == null ? Util.web.getFirstUrl() : (url.startsWith("/") ?  url : url);
		return fullUrl;
	}	
	
	@SuppressWarnings("unchecked")
	public static Set listToSet(List list, Class clazz) {
		if (list == null) {
			return new ListSet(clazz);
		}
		Set listSet = new ListSet(clazz);
		listSet.addAll(list);
		return listSet;
	}
	
	public static String escapeHTML(String aText) {
		final StringBuilder result = new StringBuilder();
		final StringCharacterIterator iterator = new StringCharacterIterator(
				aText);
		char character = iterator.current();
		while (character != CharacterIterator.DONE) {
			if (character == '<') {
				result.append("&lt;");
			} else if (character == '>') {
				result.append("&gt;");
			} else if (character == '&') {
				result.append("&amp;");
			} else if (character == '\"') {
				result.append("&quot;");
			} else if (character == '\t') {
				addCharEntity(9, result);
			} else if (character == '!') {
				addCharEntity(33, result);
			} else if (character == '#') {
				addCharEntity(35, result);
			} else if (character == '$') {
				addCharEntity(36, result);
			} else if (character == '%') {
				addCharEntity(37, result);
			} else if (character == '\'') {
				addCharEntity(39, result);
			} else if (character == '(') {
				addCharEntity(40, result);
			} else if (character == ')') {
				addCharEntity(41, result);
			} else if (character == '*') {
				addCharEntity(42, result);
			} else if (character == '+') {
				addCharEntity(43, result);
			} else if (character == ',') {
				addCharEntity(44, result);
			} else if (character == '-') {
				addCharEntity(45, result);
			} else if (character == '.') {
				addCharEntity(46, result);
			} else if (character == '/') {
				addCharEntity(47, result);
			} else if (character == ':') {
				addCharEntity(58, result);
			} else if (character == ';') {
				addCharEntity(59, result);
			} else if (character == '=') {
				addCharEntity(61, result);
			} else if (character == '?') {
				addCharEntity(63, result);
			} else if (character == '@') {
				addCharEntity(64, result);
			} else if (character == '[') {
				addCharEntity(91, result);
			} else if (character == '\\') {
				addCharEntity(92, result);
			} else if (character == ']') {
				addCharEntity(93, result);
			} else if (character == '^') {
				addCharEntity(94, result);
			} else if (character == '_') {
				addCharEntity(95, result);
			} else if (character == '`') {
				addCharEntity(96, result);
			} else if (character == '{') {
				addCharEntity(123, result);
			} else if (character == '|') {
				addCharEntity(124, result);
			} else if (character == '}') {
				addCharEntity(125, result);
			} else if (character == '~') {
				addCharEntity(126, result);
			} else {
				// the char is not a special one
				// add it to the result as is
				result.append(character);
			}
			character = iterator.next();
		}
		return result.toString();
	}

	private static void addCharEntity(Integer aIdx, StringBuilder aBuilder) {
		String padding = "";
		if (aIdx <= 9) {
			padding = "00";
		} else if (aIdx <= 99) {
			padding = "0";
		} else {
			// no prefix
		}
		String number = padding + aIdx.toString();
		aBuilder.append("&#" + number + ";");
	}
	
	public static Integer getLogoEmpresaId() {
		int logoEmpresaId = 0;
		
		ParametrosSistema parametrosSistema = ParametrosSistemaService.getParametrosSistema();
		
		Arquivo imgEmpresa = parametrosSistema.getImgEmpresa();
		if (imgEmpresa != null) {
			logoEmpresaId = imgEmpresa.getId();
		}
		return logoEmpresaId;
	}
	
	/**
	 * Método responsável por converter listas em variáveis javascript.
	 * 
	 * @param lista
	 * @param var
	 * @param label
	 * @return
	 */
	public static String convertToJavaScript(List<?> lista, String var, String label) {
		StringBuilder javascript = new StringBuilder();
		javascript.append("<script>");
		if(Util.strings.isNotEmpty(var)){
			javascript.append("var "+var+" = ");
		}
		javascript.append("[");
		for (Iterator<?> iter = lista.iterator(); iter.hasNext();) {
			Object element = iter.next();
			String description;
			if (Util.strings.isEmpty(label)) {
				description = Util.strings.toStringDescription(element);
			} else {
				PropertyDescriptor propertyDescriptor = Neo.getApplicationContext().getBeanDescriptor(element).getPropertyDescriptor(label);
				description = Util.strings.toStringDescription(propertyDescriptor.getValue());
			}
			description = escapeSingleQuotes(description);
			String id = Util.strings.toStringIdStyled(element);
			javascript.append("['"+id+"', '"+description+"']");
			if(iter.hasNext()){
				javascript.append(",");
			}
		}
		javascript.append("];");
		javascript.append("</script>");
		return javascript.toString();
	}
	
	public static String escapeSingleQuotes(String message) {
		return message
				.replace((CharSequence)"'", "\\'")
				.replace((CharSequence)"\n", " ")
				.replace((CharSequence)"\r", " ");
	}	
}
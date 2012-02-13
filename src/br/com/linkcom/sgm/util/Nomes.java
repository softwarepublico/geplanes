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

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class Nomes {
	
	public static final String VERSAO = "3.0.3";
	
	public static final String PLANOS_DE_GESTAO = "ANOS DA GESTÃO";
	public static final String PLANO_DE_GESTAO  = "ANO DA GESTÃO";
	public static final String plano_de_gestao  = "ano da gestão";
	public static final String Plano_de_gestao  = "Ano da gestão";
	public static final String Plano_de_Gestao  = "Ano da Gestão";
	
	public static final String VALORES_DE_BASE  = "METAS";
	public static final String valor_de_base    = "meta";
	public static final String valores_de_base  = "metas";
	public static final String Valores_de_Base  = "Metas";
	
	public static final String VALORES_REAIS  = "RESULTADOS";
	public static final String valores_reais  = "resultados";
	public static final String Valores_Reais  = "Resultados";
	
	public static final String ESTRATEGIAS = "OBJETIVOS ESTRATÉGICOS";
	public static final String ESTRATEGIA  = "OBJETIVO ESTRATÉGICO";
	public static final String Estrategias  = "Objetivos Estratégicos";
	public static final String Estrategia  = "Objetivo Estratégico";
	public static final String estrategia  = "objetivo estratégico";
	
	public static List<String[]> values(){
		List<String[]> r = new ArrayList<String[]>();
		Field[] fields = Nomes.class.getFields();
		for (Field f : fields) {
			String nome = f.getName();
			String valor = null;
			try {
				valor = (String) f.get(null);
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
			r.add(new String[] {nome, valor});
		}
		return r;
		
	}
}
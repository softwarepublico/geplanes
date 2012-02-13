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

import java.util.Calendar;
import java.util.Comparator;

public class CalendarComparator implements Comparator<Calendar> {
	
	public int compare(Calendar xcal, Calendar ycal) {
		if(xcal == null) return 1;
		if(ycal == null) return -1;
		
		if ( xcal.before(ycal) ) return -1;
		if ( xcal.after(ycal) ) return 1;
		return 0;
	}
	
	public static boolean isAnoIgual(Calendar x, Calendar y) {
		return x.get(Calendar.YEAR) == x.get(Calendar.YEAR);
	}
	
	public static boolean isMesIgual(Calendar x, Calendar y) {
		return x.get(Calendar.MONTH) == x.get(Calendar.MONTH);
	}
	
	public static boolean isDiaMesIgual(Calendar x, Calendar y) {
		return x.get(Calendar.DAY_OF_MONTH) == x.get(Calendar.DAY_OF_MONTH);
	}
	
	public static boolean isDiaAnoIgual(Calendar x, Calendar y) {
		return x.get(Calendar.DAY_OF_YEAR) == x.get(Calendar.DAY_OF_YEAR);
	}
	
	public static Calendar getDataAtualSemHora() {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.MILLISECOND,0);
		cal.set(Calendar.SECOND,0);
		cal.set(Calendar.MINUTE,0);
		cal.set(Calendar.HOUR_OF_DAY,0);
		return cal;
	}

}
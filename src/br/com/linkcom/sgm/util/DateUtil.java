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

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import br.com.linkcom.neo.types.Hora;
import br.com.linkcom.sgm.exception.GeplanesException;

public class DateUtil {
	
	public static final Pattern PADRAO_DATA_YYYYMMDD = Pattern.compile("\\d\\d\\d\\d[/-]\\d\\d[/-]\\d\\d"),
								PADRAO_DATA_DDMMYYYY = Pattern.compile("\\d\\d[/-]\\d\\d[/-]\\d\\d\\d\\d");	
	
	/**
	 * Formata a Data no padrão dd/MM/yyyy
	 * 
	 * @author Rodrigo Alvarenga
	 * @param data
	 * @return String (Data formatada)
	 */		
	public static String formataTimestamp(Timestamp data) {
		return formataTimestamp(data,"dd/MM/yyyy");
	}
	
	public static String formataDiaMes(java.util.Date data) {
		Date data2= new Date(data.getTime());
		return formataData(data2,"dd/MM");
	}
	
	/**
	 * Formata a Data usando o padrão especificado em pattern
	 * 
	 * @author Rodrigo Alvarenga
	 * @param data
	 * @param pattern
	 * @return String (Data formatada)
	 */		
	public static String formataTimestamp(Timestamp data, String pattern) {
		if (data == null) {
			return "";
		}
		if (pattern == null || pattern.equals("")) {
			return null;
		}
		SimpleDateFormat formatador = new SimpleDateFormat(pattern);
		return formatador.format(data);
	}	
	
	/**
	 * Formata a Data no padrão dd/MM/yyyy
	 * 
	 * @author Rodrigo Alvarenga
	 * @param data
	 * @return String (Data formatada)
	 */	
	public static String formataData(Date data) {
		return formataData(data,"dd/MM/yyyy");
	}	
	
	/**
	 * Formata a Data usando o padrão especificado em pattern
	 * 
	 * @author Rodrigo Alvarenga
	 * @param data
	 * @param pattern
	 * @return String (Data formatada)
	 */	
	public static String formataData(Date data, String pattern) {
		if (data == null) {
			return "";
		}
		if (pattern == null || pattern.equals("")) {
			return null;
		}
		SimpleDateFormat formatador = new SimpleDateFormat(pattern);
		return formatador.format(data);
	}	
	
	/**
	 * Formata a Data, a Hora e o Minuto
	 * 
	 * @author Rodrigo Alvarenga
	 * @param dataHora
	 * @return String (Data,Hora e Minuto formatados)
	 */	
	public static String formataDataHoraMinuto(Timestamp dataHora) {
		if (dataHora == null) {
			return "";
		}
		SimpleDateFormat formatador = new SimpleDateFormat("dd/MM/yyyy HH:mm");
		return formatador.format(dataHora);
	}	

	/**
	 * Obtém apenas a hora de um campo que contém a data e a hora
	 * 
	 * @author Bruno Eustáquio
	 * @param dataHora
	 * @return Hora ou null (caso o parâmetro dataHora seja nulo
	 */
	public static Hora extraiHora(Timestamp dataHora) {
		return dataHora != null ? new Hora(dataHora.getTime()): null;
	}

	/**
	 * Obtém apenas a data de um campo que contém a data e a hora
	 * 
	 * @author Bruno Eustáquio
	 * @param dataHora
	 * @return Data ou null (caso o parâmetro dataHora seja nulo
	 */
	public static Date extraiData(Timestamp dataHora) {
		if (dataHora != null) {
			Calendar cal = timestampToCalendar(dataHora);
			cal = somenteDiaMesAno(cal);
			return new Date(cal.getTimeInMillis());
		}
		return null;
	}
	
	/**
	 * Concatena a data e a hora em apenas um campo
	 * OBS: Os valores do parâmetro data referentes à hora minuto e segundo serão desconsiderados.
	 * 
	 * @author Bruno Eustáquio
	 * @param data
	 * @param hora 
	 * @return Data concatenada ou null caso a data seja nula
	 */
	public static Timestamp dateAndTimeToTimestamp(Date data, Time hora){
		if (data != null) {
			Calendar calendarData = dateToCalendar(data);
			
			if (hora != null) {
				Calendar calendarHora = dateToCalendar(hora);	    
				calendarData.set(Calendar.HOUR_OF_DAY, calendarHora.get(Calendar.HOUR_OF_DAY));
				calendarData.set(Calendar.MINUTE, calendarHora.get(Calendar.MINUTE));
				calendarData.set(Calendar.SECOND, calendarHora.get(Calendar.SECOND));
				calendarData.set(Calendar.MILLISECOND, calendarHora.get(Calendar.MILLISECOND));
			}
			else {
				calendarData.set(Calendar.HOUR_OF_DAY, 0);
				calendarData.set(Calendar.MINUTE, 0);
				calendarData.set(Calendar.SECOND, 0);
				calendarData.set(Calendar.MILLISECOND, 0);
			}

			return new Timestamp(calendarData.getTimeInMillis());
		}
		return null;
	}
	  
	/**
	 * Verifica se existe interseção entre dois intervalos data e hora inicio ou data e hora fim
	 * 
	 * @author Rodrigo Alvarenga
	 * @param inicioAtual
	 * @param fimAtual
	 * @param inicioProximo
	 * @param fimProximo
	 * @param intervaloFechado
	 * @return Boolean
	 */	  
	public static Boolean existeIntersecao(Long inicioAtual, Long fimAtual, Long inicioProximo, Long fimProximo, Boolean intervaloFechado) {
		if (inicioAtual == null || fimAtual == null || inicioProximo == null || fimProximo == null) {
			return false;	
		}
		if (Boolean.TRUE.equals(intervaloFechado)) {
			return (
					(inicioProximo.compareTo(inicioAtual) >= 0  && inicioProximo.compareTo(fimAtual) <= 0) ||
					(fimProximo.compareTo(inicioAtual) >= 0 && fimProximo.compareTo(fimAtual) <= 0) ||
					(inicioProximo.compareTo(inicioAtual) <= 0 && fimProximo.compareTo(fimAtual) >= 0)
					);
		}
		else {
			return (
					(inicioProximo.equals(inicioAtual) || fimProximo.equals(fimAtual)) ||
					(inicioProximo.compareTo(inicioAtual) > 0  && inicioProximo.compareTo(fimAtual) < 0) ||
					(fimProximo.compareTo(inicioAtual) > 0 && fimProximo.compareTo(fimAtual) < 0) ||
					(inicioProximo.compareTo(inicioAtual) < 0 && fimProximo.compareTo(fimAtual) > 0)
					);						
		}
	}
	
	/**
	 * Retorna a o resultado de (data1 - data2), em dias inteiros.
	 * 
	 * @author Bruno Eustáquio
	 * @param data1
	 * @param data2
	 * @return diferença em dias dias
	 */
	public static int diferencaHoras(java.util.Date data1, java.util.Date data2) {
		
		Calendar calendar1 = dateToCalendar(data1);
		calendar1.set(Calendar.HOUR_OF_DAY, 0);
		calendar1.set(Calendar.MINUTE, 0);
		calendar1.set(Calendar.SECOND, 0);
		calendar1.set(Calendar.MILLISECOND, 0);

		Calendar calendar2 = dateToCalendar(data2);
		calendar2.set(Calendar.HOUR_OF_DAY, 0);
		calendar2.set(Calendar.MINUTE, 0);
		calendar2.set(Calendar.SECOND, 0);
		calendar2.set(Calendar.MILLISECOND, 0);
		
		return (int) ((calendar1.getTimeInMillis() - calendar2.getTimeInMillis()) / 1000 / 60 / 60);
	}
	
	public static int diferencaDias(java.util.Date data1, java.util.Date data2) {
		return diferencaHoras(data1, data2) / 24;
	}
	
	/**
	 * Converte um java.sql.Date para Calendar
	 * 
	 * @author Bruno Eustáquio
	 * @param java.sql.Date
	 * @return Calendar
	 */
	public static Calendar dateToCalendar(java.util.Date date) {
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(date);
		return calendar;
	}
	
	/**
	 * Converte um tipo Hora para Calendar
	 * 
	 * @author Bruno Eustáquio
	 * @param Hora
	 * @return Calendar
	 */
	public static Calendar hourToCalendar(Hora hora) {
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(hora);
		return calendar;
	}
	
	/**
	 * Converte um tipo Timestamp para Calendar
	 * 
	 * @author Bruno Eustáquio
	 * @param Timestamp
	 * @return Calendar
	 */
	public static Calendar timestampToCalendar(Timestamp timestamp) {
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(timestamp);
		return calendar;
	}
	
	public static int extractHora(java.util.Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar.get(Calendar.HOUR_OF_DAY);
	}

	public static int extractMinuto(java.util.Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar.get(Calendar.MINUTE);
	}
	
	/**
	 * Valida a hora de forma que só sejam permitidos os minutos 00, 15, 30, 45.
	 * 
	 * @author Bruno Eustáquio
	 * @param calendar
	 * @return true ou false se os minutos estiverem de acorodo.
	 */
	public static boolean validateMinute(Calendar calendar) {
		if(calendar.get(Calendar.MINUTE) != 0 &&  calendar.get(Calendar.MINUTE) != 15 &&
		   calendar.get(Calendar.MINUTE) != 30 && calendar.get(Calendar.MINUTE) != 45){
			return false;
		}
		return true;
	}
	
	/**
	 * Zera todos os campos que não forem referentes a dia, mês e ano de uma data 
	 * 
	 * @author Rodrigo Alvarenga
	 * @param cal
	 * @return cal
	 */	
	public static Calendar somenteDiaMesAno(Calendar cal) {
		if (cal == null) {
			return null;
		}
		cal.set(Calendar.HOUR_OF_DAY,0);
		cal.set(Calendar.MINUTE,0);
		cal.set(Calendar.SECOND,0);
		cal.set(Calendar.MILLISECOND,0);
		return cal;
	}
	
	/**
	 * Zera todos os campos que não forem referentes a dia, mês e ano de uma data 
	 * 
	 * @author Rodrigo Alvarenga
	 * @param data
	 * @return data
	 */	
	public static Date somenteDiaMesAno(Date data) {
		Calendar cal = dateToCalendar(data);
		cal = somenteDiaMesAno(cal);
		return new Date(cal.getTimeInMillis());
	}
	
	/**
	 * Acrescenta uma quantidade de meses em uma determinada data
	 * @author Rodrigo Alvarenga
	 * @param data
	 * @param numeroMeses
	 * @return data
	 */	
	public static Date incrementaMes(Date data, int numeroMeses) {
		Calendar calendar = dateToCalendar(data);
		calendar.add(Calendar.MONTH, numeroMeses);
		return new Date(calendar.getTime().getTime());
	}
	
	/**
	 * Acrescenta uma quantidade de dias em uma determinada data
	 * @author Rodrigo Alvarenga
	 * @param data
	 * @param numeroDias
	 * @return data
	 */	
	public static Date incrementaDia(Date data, int numeroDias) {
		Calendar calendar = dateToCalendar(data);
		calendar.add(Calendar.DAY_OF_MONTH, numeroDias);
		return new Date(calendar.getTime().getTime());
	}
	
	/**
	 * Acrescenta uma quantidade de minutos em uma determinada data
	 * @author Rodrigo Alvarenga
	 * @param data
	 * @param numeroMinutos
	 * @return data
	 */
	public static Date incrementaMinuto(Date data, int numeroMinutos) {
		Calendar calendar = dateToCalendar(data);
		calendar.add(Calendar.MINUTE, numeroMinutos);
		return new Date(calendar.getTime().getTime());
	}
	
	/**
	 * Acrescenta uma quantidade de dias em um determinado timestamp
	 * @author Rodrigo Alvarenga
	 * @param data
	 * @param numeroDias
	 * @return data
	 */	
	public static Timestamp incrementaDia(Timestamp data, int numeroDias) {
		Calendar calendar = timestampToCalendar(data);
		calendar.add(Calendar.DAY_OF_MONTH, numeroDias);
		return new Timestamp(calendar.getTimeInMillis());
	}	
	
	/**
	 * Converte strings de datas no formato yyyy-mm-dd ou dd-mm-yyyy em objetos do tipo java.sql.Date.
	 * O "-" pode ser substituido por "/". 
	 * @author Rodrigo Alvarenga 
	 * @param data Data nos formatos descritos acima
	 * @return java.sql.Date ou null se o formato da data for inválido
	 */
	public static java.sql.Date stringToSqlDate(String data) {
		if (data == null) {
			return null;
		}

		data = data.trim();
		
		if (PADRAO_DATA_YYYYMMDD.matcher(data).matches()) {
			data = data.replace('/', '-');
		}
		else if (PADRAO_DATA_DDMMYYYY.matcher(data).matches()) {
			String dt[] = data.split("[/-]");
			data = dt[2]+"-"+dt[1]+"-"+dt[0];
		}
		else
			return null;

		return java.sql.Date.valueOf(data);
	}
	
	/**
	 * Retorna um objeto do tipo Time contendo o último segundo do dia
	 * 
	 * @author Rodrigo Alvarenga
	 * @return Time(23:59:59)
	 * @throws GeplanesException - caso não se consiga fazer o parse
	 */
	public static Time getUltimoSegundoDia() {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
		try {
			return new Time(simpleDateFormat.parse("23:59:59").getTime());
		} catch (ParseException e) {
			throw new GeplanesException(e.getMessage());
		}
	}
	
	/**
	 * Retorna uma lista de anos a partir do ano e tamanho passados como parâmetro
	 * 
	 * @author Bruno Eustáquio
	 * @return Lista de strings
	 * @param anoInicio - Ano em que a lista se inicia. Este ano é incluso na lista
	 * @param tamanho - Tamanho da lista (Ex: anoInicio = 2000; tamanho = 2; lista: 2000, 2001)
	 * 
	 */
	public static List<String> montaListaAnos(Integer anoInicio, Integer tamanho) {
		if (anoInicio == null || tamanho == null || tamanho < 1) {
			throw new GeplanesException("Um dos parâmetros do método montaListaAnos está sendo passado como null.");
		}
		List<String> anos = new ArrayList<String>();
		for (Integer ano = anoInicio; ano < anoInicio+tamanho; ano++) {
			anos.add(ano.toString());
		}
		return anos;
	}
	
	/**
	 * Verifica se um ano é bissexto
	 * @author Rodrigo Alvarenga
	 * 
	 * @param ano
	 * @return verdadeiro ou falso
	 * @throws GeplanesException - se o ano for nulo
	 */	
	public static Boolean anoBissexto(Integer ano) {
		if (ano == null) {
			throw new GeplanesException("Existem parâmetros nulos na chamada do método 'DateUtil.anoBissexto'");
		}
	    if (((ano % 4 == 0) && (ano % 100 > 0)) || (ano % 400 == 0)) {
	        return true;
	    }
	    else {
	        return false;
	    }
	}
	
	/**
	 * Monta um mapa contendo o índice do mês e a quantidade de dias do mesmo
	 * @author Rodrigo Alvarenga
	 * 
	 * @param ano
	 * @return mapa com a quantidade de dias por mês
	 * @throws GeplanesException - se o ano for nulo
	 */	
	public static Map<Integer,Integer> montaMapaQuantidadeDiasMes(Integer ano) {
		Map<Integer, Integer> mapa = new LinkedHashMap<Integer, Integer>();
		
		if (ano == null) {
			throw new GeplanesException("Existem parâmetros nulos na chamada do método 'DateUtil.montaListaQuantidadeDiasMes'");
		}
		for (int i = 1; i <= 12; i++) {
			if (i == 2) { //Fevereiro
				mapa.put(i, anoBissexto(ano) ? 29 : 28);
			}
			else if ((i == 4) || (i == 6) || (i == 9) || (i == 11)) { //Abril,Junho,Setembro,Novembro  
				mapa.put(i, 30);
			}
			else { //Janeiro,Março,Maio,Julho,Agosto,Outubro,Dezembro
				mapa.put(i, 31);
			}			
		}		
		return mapa;
	}
	
	/**
	 * Retorna a descrição do mês, dado o número do mesmo
	 * @author Rodrigo Alvarenga
	 * 
	 * @return descrição do mês
	 */	
	public static String getDescricaoMes(int mes){
		
		String descricaoMes = null;
		switch (mes) {
			case 1:
				descricaoMes = "Janeiro"; 
				break;
			case 2:
				descricaoMes = "Fevereiro"; 
				break;
			case 3:
				descricaoMes = "Março"; 
				break;
			case 4:
				descricaoMes = "Abril"; 
				break;
			case 5:
				descricaoMes = "Maio"; 
				break;
			case 6:
				descricaoMes = "Junho"; 
				break;
			case 7:
				descricaoMes = "Julho"; 
				break;
			case 8:
				descricaoMes = "Agosto"; 
				break;
			case 9:
				descricaoMes = "Setembro"; 
				break;
			case 10:
				descricaoMes = "Outubro"; 
				break;
			case 11:
				descricaoMes = "Novembro"; 
				break;
			case 12:
				descricaoMes = "Dezembro"; 
				break;
				
			default:
				descricaoMes = "Mês inexistente";
				break;
		}		
		return descricaoMes;
	}
	
	/**
	 * Retorna a descrição abreviada do mês, dado o número do mesmo
	 * @author Rodrigo Alvarenga
	 * 
	 * @return descrição abreviada do mês
	 */	
	public static String getDescricaoAbreviadaMes(int mes){
		
		String descricaoMes = null;
		switch (mes) {
			case 1:
				descricaoMes = "Jan"; 
				break;
			case 2:
				descricaoMes = "Fev"; 
				break;
			case 3:
				descricaoMes = "Mar"; 
				break;
			case 4:
				descricaoMes = "Abr"; 
				break;
			case 5:
				descricaoMes = "Mai"; 
				break;
			case 6:
				descricaoMes = "Jun"; 
				break;
			case 7:
				descricaoMes = "Jul"; 
				break;
			case 8:
				descricaoMes = "Ago"; 
				break;
			case 9:
				descricaoMes = "Set"; 
				break;
			case 10:
				descricaoMes = "Out"; 
				break;
			case 11:
				descricaoMes = "Nov"; 
				break;
			case 12:
				descricaoMes = "Dez"; 
				break;
				
			default:
				descricaoMes = "Mês inexistente";
				break;
		}		
		return descricaoMes;
	}	
	
	/**
	 * Retorna o número do mês, dado a string com o nome completo do mesmo
	 * @author Bruno Eustáquio
	 * 
	 * @return número do mês
	 */	
	public static Integer getNumeroMes(String mes){
		
		Integer numMes = null;
		mes = mes.toLowerCase();
		
		if (mes.equals("janeiro")) 			numMes = 1;
		else if (mes.equals("fevereiro")) 	numMes = 2;
		else if (mes.equals("março")) 		numMes = 3;
		else if (mes.equals("abril")) 		numMes = 4;
		else if (mes.equals("maio")) 		numMes = 5;
		else if (mes.equals("junho")) 		numMes = 6;
		else if (mes.equals("julho")) 		numMes = 7;
		else if (mes.equals("agosto")) 		numMes = 8;
		else if (mes.equals("setembro")) 	numMes = 9;
		else if (mes.equals("outubro")) 	numMes = 10;
		else if (mes.equals("novembro")) 	numMes = 11;
		else if (mes.equals("dezembro")) 	numMes = 12;
		else throw new GeplanesException("Parâmetro mês inválido na chamada do método 'DateUtil.getNumeroMes'");

		return numMes;
	}
	/**
	 * Retorna uma lista com as descrições dos meses do ano.
	 * @author Rodrigo Alvarenga
	 * @return List<String>
	 */
	public static List<String> getListaMes(){
		List<String> listaMes = new ArrayList<String>();
		listaMes.add("Janeiro");
		listaMes.add("Fevereiro");
		listaMes.add("Março");
		listaMes.add("Abril");
		listaMes.add("Maio");
		listaMes.add("Junho");
		listaMes.add("Julho");
		listaMes.add("Agosto");
		listaMes.add("Setembro");
		listaMes.add("Outubro");
		listaMes.add("Novembro");
		listaMes.add("Dezembro");
		
		return listaMes;
	}
	
	/**
	 * Retorna uma lista com as descrições abreviadas dos meses do ano.
	 * @author Matheus Melo Gonçalves
	 * @return List<String>
	 */
	public static List<String> getListaMesAbreviado(){
		List<String> listaMes = new ArrayList<String>();
		listaMes.add("Jan");
		listaMes.add("Fev");
		listaMes.add("Mar");
		listaMes.add("Abr");
		listaMes.add("Mai");
		listaMes.add("Jun");
		listaMes.add("Jul");
		listaMes.add("Ago");
		listaMes.add("Set");
		listaMes.add("Out");
		listaMes.add("Nov");
		listaMes.add("Dez");
		
		return listaMes;
	}	
	
	/**
	 * Limpa os campos de segundo, milisegundo, hora e minuto
	 * @param data
	 * @return
	 * @author Cíntia Nogueira
	 */
	public static Date limpaMinSegHora(Date data){
		GregorianCalendar calendar = new GregorianCalendar();
		calendar.setTime(data);		
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		
		return  new Date(calendar.getTime().getTime());
	}
	
	/**
	 * Incrementa segundo
	 * @param data
	 * @param numero
	 * @return
	 * @author Cíntia Nogueira
	 */
	public static Date incrementaSegundo(Date data, int numero) {
		Calendar calendar = dateToCalendar(data);
		calendar.add(Calendar.SECOND, numero);
		return new Date(calendar.getTime().getTime());
	}
	
	
	/**
	 * Retorna o tempo no último segundo do dia
	 * @param data
	 * @return
	 * @author Cíntia Nogueira
	 */
	public static Date getUltimoSegDia(Date data){
		java.util.Date date2 = incrementaDia(data, 1);
		data = new Date(date2.getTime());
		data= limpaMinSegHora(data);
		data= incrementaSegundo(data, -1);
		return data;
		
	}
	
	
	/**
	 * Método que adiciona dias a uma determinada Data
	 * 
	 * @param dtReferencia
	 * @param dias
	 * @return
	 * @author Tomás Rabelo
	 */
	public static Date addDiasData(Date dtReferencia, int dias) {
		Calendar dtLimiteAux = Calendar.getInstance();
		dtLimiteAux.setTimeInMillis(dtReferencia.getTime());
		dtLimiteAux.add(Calendar.DAY_OF_MONTH, dias);
		
		return new Date(dtLimiteAux.getTimeInMillis());
	}
	
	public static Date addMesData(Date dtReferencia, int mes) {
		Calendar dtLimiteAux = Calendar.getInstance();
		dtLimiteAux.setTimeInMillis(dtReferencia.getTime());
		dtLimiteAux.add(Calendar.MONTH, mes);
		
		return new Date(dtLimiteAux.getTimeInMillis());
	}
	
	/**
	 * Retorna uma nova data no começo do dia
	 * 
	 * @param data
	 * @return
	 * @author Tomás Rabelo
	 */
	public static Date dateToBeginOfDay(Date data) {
		if(data != null){
			Calendar dtAux = Calendar.getInstance();
			dtAux.setTime(data);
			dtAux.set(Calendar.HOUR_OF_DAY, 0);
			dtAux.set(Calendar.MINUTE, 0);
			dtAux.set(Calendar.SECOND, 0);
			dtAux.set(Calendar.MILLISECOND, 0);
			
			return new Date(dtAux.getTimeInMillis());
		}else{
			return null;
		}
		
	}
	
}
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

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ResourceBundle;

import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.jndi.JndiTemplate;

import br.com.linkcom.neo.core.web.NeoWeb;
import br.com.linkcom.neo.exception.NotInNeoContextException;

/**
 * Classe para gerenciar a conexão com o banco de acordo com a url.
 * 
 * @since 18/05/2010
 * @author Rodrigo Alvarenga
 *
 */
public class JndiGeplanesFactory implements FactoryBean {

	JndiTemplate jndiTemplate = new JndiTemplate();
	private String jndiURL;
	
	private DataSource getCurrentDataSource() {
		try {
			return (DataSource) jndiTemplate.lookup(getJndiName(), getObjectType());
		} 
		catch (NamingException e) {
			throw new RuntimeException("Erro ao conseguir conexão com o banco de dados!",e);
		}
	}
	
	private String getJndiName() {
		try {
			HttpServletRequest servletRequest = NeoWeb.getRequestContext().getServletRequest();
			String url = servletRequest.getRequestURL().toString();
			
			String[] urlDividida = url.split("/");
			
			String servidor = urlDividida[2];
			String contexto = urlDividida[3];
			
			urlDividida = servidor.split(".");
			if(urlDividida.length==0){
				urlDividida = servidor.split(":");
				if(urlDividida.length>0){				
					servidor = urlDividida[0];
				}
			}
			return getJndiURL() + "/" + servidor + "_" + contexto + "_PostgreSQLDS";
		} 
		catch (NullPointerException e) {
			// Se ainda nao tiver request, é porque veio dos processos de agendamento
			// Nesse caso, usa o banco padrão.
			System.out.println("Conectando ao banco padrão para execução de processos de agendamento. (jndi: 'geplanes_bsc_PostgreSQLDS')");
			return getJndiURL() + "/geplanes_bsc_PostgreSQLDS";
		}
		catch (NotInNeoContextException e) {
			// Se ainda nao estiver no contexto, retornar uma conexao default
			// o hibernate precisa de conexoes para ser configurado			
			System.out.println("Conectando ao banco padrão para configuração do hibernate. (jndi: 'geplanes_PostgreSQLDS')");
			return getJndiURL() + "/geplanes_bsc_PostgreSQLDS";
		}
	}

	public Object getObject() throws Exception {
		DataSource source = new DataSource(){

			public Connection getConnection() throws SQLException {
				return getCurrentDataSource().getConnection();
			}

			public Connection getConnection(String username, String password) throws SQLException {
				return getCurrentDataSource().getConnection(username, password);
			}

			public PrintWriter getLogWriter() throws SQLException {
				return getCurrentDataSource().getLogWriter();
			}

			public int getLoginTimeout() throws SQLException {
				return getCurrentDataSource().getLoginTimeout();
			}

			public void setLogWriter(PrintWriter out) throws SQLException {
				getCurrentDataSource().setLogWriter(out);
				
			}

			public void setLoginTimeout(int seconds) throws SQLException {
				getCurrentDataSource().setLoginTimeout(seconds);
				
			}

			public boolean isWrapperFor(Class<?> iface) throws SQLException {
				return getCurrentDataSource().isWrapperFor(iface);
			}

			public <T> T unwrap(Class<T> iface) throws SQLException {
				return getCurrentDataSource().unwrap(iface);
			}
			
		};
		return source;
	}

	@SuppressWarnings("unchecked")
	public Class getObjectType() {
		return DataSource.class;
	}

	public boolean isSingleton() {
		return true;
	}

	public String getJndiURL() {
		if (jndiURL == null) {
			ResourceBundle resourceBundle = ResourceBundle.getBundle("application");
			setJndiURL(resourceBundle.getString("jndi.url_prefix"));
		}
		return jndiURL;
	}

	public void setJndiURL(String jndiURL) {
		this.jndiURL = jndiURL;
	}
}

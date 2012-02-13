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

package br.com.linkcom.neo.view.ajax;

import br.com.linkcom.neo.controller.MultiActionController;
import br.com.linkcom.neo.core.web.NeoWeb;
import br.com.linkcom.neo.util.Util;
import br.com.linkcom.neo.view.BaseTag;

public class CallTag extends BaseTag {

	protected String url;
	protected String action;
	protected String parameters;
	protected String callback;
	protected String functionName;
	
	public String getAction() {
		return action;
	}
	public String getCallback() {
		return callback;
	}
	public String getParameters() {
		if(parameters == null){
			parameters = "";
		}
		return parameters;
	}
	public String getUrl() {
		return url;
	}
	public void setAction(String action) {
		this.action = action;
	}
	public void setCallback(String callback) {
		this.callback = callback;
	}
	public void setParameters(String parameters) {
		this.parameters = parameters;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	
	@Override
	protected void doComponent() throws Exception {
		NeoWeb.getRequestContext();
		getOut().println("<script language=\"javascript\">");
		url = getRequest().getContextPath() + (url == null? Util.web.getFirstUrl() : url);
		if(getParameters().startsWith("javascript:")){
			parameters = getParameters().substring("javascript:".length());
		} else {
			parameters = "'"+Util.strings.escape(getParameters())+"'";
		}
		if(Util.strings.isEmpty(callback)){
			//callback = "function (data){try{eval(data);}catch(e){alert('Erro ao executar callback!\\n'+e.name+': '+e.message); document.write('<b>Código enviado pelo servidor</b><br><hr>'+data.replace(/\\n/g, '<BR>'));}}";
			callback = "function (data){eval(data);}";
		}
		if(!functionName.contains("(")){
			functionName = functionName+"()";
		}
		getOut().println("    function "+functionName+"{");
		getOut().println("        sendRequest('"+url+"', '"+MultiActionController.ACTION_PARAMETER+"="+action+"&' + "+parameters+", 'POST', "+callback+", ajaxcallerrorcallback);");
		getOut().println("    }");
		getOut().println("</script>");
	}
	public String getFunctionName() {
		return functionName;
	}
	public void setFunctionName(String functionName) {
		this.functionName = functionName;
	}
}

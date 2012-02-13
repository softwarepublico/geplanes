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

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.com.linkcom.neo.controller.resource.Resource;
import br.com.linkcom.neo.core.standard.Neo;
import br.com.linkcom.sgm.beans.Arquivo;
import br.com.linkcom.sgm.dao.ArquivoDAO;


public class DownloadFilePubServlet extends br.com.linkcom.neo.view.DownloadFileServlet {

	private static final long serialVersionUID = 1L;

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Long cdfile;
		try {
			cdfile = extractCdfile(request);
		}
		catch (Exception e) {
        	response.sendError(HttpServletResponse.SC_NOT_FOUND);
        	return;
		}
		
		// Obtém o conteúdo
		Resource resource = getResource(request, cdfile);
        if(resource == null){
        	response.sendError(HttpServletResponse.SC_NOT_FOUND);
        	return;
        }

        response.setContentType(resource.getContentType());
		response.setHeader("Content-Disposition","attachment; filename=\""+ resource.getFileName() + "\";");
		//response.setHeader("Last-Modified", );
		if (resource.getSize()>=0) {
			response.setContentLength(resource.getSize());
		}

		response.getOutputStream().write(resource.getContents());
		response.flushBuffer();
	}
	
	@Override
	protected Resource getResource(HttpServletRequest request, Long cdfile) {
		Arquivo arquivo = new Arquivo();
		arquivo.setCdfile(cdfile);
		arquivo = Neo.getObject(ArquivoDAO.class).load(arquivo);
		Resource resource = new Resource(arquivo.getContenttype(), arquivo.getName(), arquivo.getContent());
		if (resource.getContents() != null) {
			resource.setSize(resource.getContents().length);
		}
		return resource;
	}
}

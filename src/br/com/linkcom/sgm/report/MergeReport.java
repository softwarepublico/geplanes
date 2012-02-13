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
package br.com.linkcom.sgm.report;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import br.com.linkcom.neo.controller.resource.Resource;
import br.com.linkcom.neo.core.standard.Neo;
import br.com.linkcom.neo.report.IReport;
import br.com.linkcom.neo.report.Report;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.pdf.PdfCopy;
import com.lowagie.text.pdf.PdfImportedPage;
import com.lowagie.text.pdf.PdfReader;

/**
 * Classe responsável por agrupar relatórios
 * 
 * @author Pedro Gonçalves
 * @since 28/09/2007
 */
public class MergeReport {
	
	private String resourcename;
	private List<Report> reportlist = new ArrayList<Report>();
	
	
	/**
	 * @author Pedro Gonçalves 
	 * @param resourcename - Nome do resource que será retornado
	 */
	public MergeReport(String resourcename) {
		setResourcename(resourcename);
	}
	
	public void setReportlist(List<Report> reportlist) {
		this.reportlist = reportlist;
	}
	
	public void addReport(Report report) {
		reportlist.add(report);
	}
	
	public void setResourcename(String resourcename) {
		this.resourcename = resourcename;
	}
	
	public String getResourcename() {
		return resourcename;
	}
	
	private PdfReader createPdfReader(IReport report) {
		try {
			return new PdfReader(getReportBytes(report));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private void convertToDocument(PdfReader pdfReader,PdfCopy writer) throws DocumentException {
		pdfReader.consolidateNamedDestinations();
		PdfImportedPage page;
		int n = pdfReader.getNumberOfPages();
		for (int i = 0; i < n; ) {
		    ++i;
		    page = writer.getImportedPage(pdfReader, i);
		    try {
				writer.addPage(page);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
	}
	
	private byte[] getReportBytes(IReport report) {
		return Neo.getApplicationContext().getReportGenerator().toPdf(report);
	}
	
	public Resource generateResource() throws Exception {
		
		List<PdfReader> listaPdfReader = new ArrayList<PdfReader>();		
		
		for (Report report : reportlist) {
			PdfReader pdfReader = createPdfReader(report);
			listaPdfReader.add(pdfReader);
		}
		
		Document document = new Document(listaPdfReader.get(0).getPageSizeWithRotation(1));
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		PdfCopy writer = new PdfCopy(document, output);
		document.open();
		
		for (PdfReader pdfReader : listaPdfReader) {
			convertToDocument(pdfReader,writer);
		}
		
		document.close();
		
		byte[] bytes = output.toByteArray();
		
		String name = getResourcename();
		return getPdfResource(name, bytes);
	}
	
	private Resource getPdfResource(String name, byte[] bytes) {
		Resource resource = new Resource();
        resource.setContentType("application/pdf");
        resource.setFileName(name);
        resource.setContents(bytes);
		return resource;
	}
}

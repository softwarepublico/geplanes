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
package br.com.linkcom.sgm.controller.report;

import java.awt.Color;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import br.com.linkcom.neo.core.standard.Neo;
import br.com.linkcom.neo.core.standard.NeoStandard;
import br.com.linkcom.neo.core.web.NeoWeb;
import br.com.linkcom.neo.core.web.WebApplicationContext;
import br.com.linkcom.sgm.beans.AcompanhamentoIndicador;
import br.com.linkcom.sgm.beans.ParametrosSistema;
import br.com.linkcom.sgm.beans.PlanoGestao;
import br.com.linkcom.sgm.beans.UnidadeGerencial;
import br.com.linkcom.sgm.beans.DTO.ItemPainelControleDTO;
import br.com.linkcom.sgm.beans.enumeration.MelhorDoIndicadorEnum;
import br.com.linkcom.sgm.controller.process.PainelControle;
import br.com.linkcom.sgm.service.ParametrosSistemaService;
import br.com.linkcom.sgm.service.PlanoGestaoService;
import br.com.linkcom.sgm.util.Nomes;

import com.lowagie.text.BadElementException;
import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfPageEventHelper;
import com.lowagie.text.pdf.PdfTemplate;
import com.lowagie.text.pdf.PdfWriter;

public class PainelControleReportGenerator {
	
	PlanoGestaoService planoGestaoService = Neo.getObject(PlanoGestaoService.class);
	PainelControle painelControle = Neo.getObject(PainelControle.class);
	
	private PlanoGestao planoGestao;
	private UnidadeGerencial unidadeGerencial;
	private boolean valoresApurados;

	public PainelControleReportGenerator(PlanoGestao planoGestao, UnidadeGerencial unidadeGerencial, boolean valoresApurados){
		this.planoGestao = planoGestaoService.load(planoGestao);
		this.unidadeGerencial = unidadeGerencial;
		this.valoresApurados = valoresApurados;
	}
	
	private Image getImage(String nome) throws BadElementException, MalformedURLException, IOException{
		try {
			InputStream is = ((WebApplicationContext)NeoWeb.getApplicationContext()).getServletContext().getResourceAsStream(nome);
			List<Byte> list = new ArrayList<Byte>();
			int i;
			while((i = is.read()) != -1){
				list.add((byte)i);
			}
			byte[] imagem = new byte[list.size()];
			for (int j = 0; j < imagem.length; j++) {
				imagem[j] = list.get(j);
			}
			return Image.getInstance(imagem);
		} catch (Exception e) {
			return Image.getInstance("WebRoot/"+nome);	
		}
		
	}
	
	private Image getIcon(ItemPainelControleDTO item) throws BadElementException, MalformedURLException, IOException {
		return getImage("/images/ico_pc_"+item.getIcone()+".gif");
	}
	
	
	private Image getLogoEmpresa() throws BadElementException, MalformedURLException, IOException {
		try {
			ParametrosSistema parametrosSistema = ParametrosSistemaService.getParametrosSistema();
			if (parametrosSistema.getImgEmpresaRelatorio() != null) {
				ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(parametrosSistema.getImgEmpresaRelatorio().getContent());				
				List<Byte> list = new ArrayList<Byte>();
				int i;
				while((i = byteArrayInputStream.read()) != -1){
					list.add((byte)i);
				}
				byte[] imagem = new byte[list.size()];
				for (int j = 0; j < imagem.length; j++) {
					imagem[j] = list.get(j);
				}
				return Image.getInstance(imagem);
			}
			else {
				return getImage("/images/img_empresa_relatorio.png");
			}
		}
		catch (IOException e) {
			e.printStackTrace();
			return null;
		}		
	}
	
	private Image getLogoSgm() throws BadElementException, MalformedURLException, IOException {
		return getImage("/images/img_sgm_relatorio.png");
	}

	private Image getFarol(AcompanhamentoIndicador acompanhamentoIndicador) throws BadElementException, MalformedURLException, IOException {
		return getImage("/images/bola-"+acompanhamentoIndicador.getCorFarol()+".png");
	}
	

	
	public byte[] criarRelatorio() throws DocumentException, MalformedURLException, IOException{
		ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();
		List<ItemPainelControleDTO> unidades = painelControle.carregarUnidades(planoGestao, unidadeGerencial, false);
		
		Document document = new Document();
		PdfWriter writer = PdfWriter.getInstance(document, arrayOutputStream);
		document.setPageSize(PageSize.A4.rotate());
		document.setMargins(10,10,74,10);
		document.open();
		
		{
			Image logoSgm = getLogoSgm();			
			Image logoEmpresa = getLogoEmpresa();
			
			final PdfPTable tabelaTitulo = new PdfPTable(3);
			tabelaTitulo.getDefaultCell().setBorder(0);
			
			tabelaTitulo.setTotalWidth(PageSize.A4.height() - 32);
			tabelaTitulo.setWidths(new int[]{2,22,2});
			tabelaTitulo.addCell(logoSgm);
								
			
			Font font = new Font(Font.HELVETICA, 16);
			font.setColor(new Color(135,135,135));
			font.setStyle(Font.BOLD);
			Paragraph paragraph = new Paragraph("PAINEL DE CONTROLE", font);
			paragraph.setAlignment(Element.ALIGN_CENTER);
			paragraph.setSpacingAfter(0);
			paragraph.setSpacingBefore(0);
			PdfPCell pdfPCell = new PdfPCell();
			pdfPCell.setPaddingTop(0);
			pdfPCell.setPaddingBottom(15);
			pdfPCell.setBorder(0);
			pdfPCell.addElement(paragraph);

			tabelaTitulo.addCell(pdfPCell);
			
			PdfPCell cell = new PdfPCell();
			cell.setPaddingTop(8);
			cell.setBorder(0);
			cell.addElement(logoEmpresa);
			tabelaTitulo.addCell(cell);
			
			
			cell = new PdfPCell();
			cell.setBorder(0);
			cell.setColspan(3);
			cell.setFixedHeight(2);
			cell.setBackgroundColor(new Color(215,215,215));
			tabelaTitulo.addCell(cell);
			
			
			cell = new PdfPCell();
			cell.setBorder(0);
			cell.setColspan(3);
			cell.setFixedHeight(1);
			cell.setBackgroundColor(new Color(135,135,135));
			tabelaTitulo.addCell(cell);
			
			final PdfTemplate total = writer.getDirectContent().createTemplate(100, 100);
			total.setBoundingBox(new Rectangle(-20, -20, 100, 100));
			
			final BaseFont helv = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.WINANSI, BaseFont.NOT_EMBEDDED);
			
			writer.setPageEvent(new PdfPageEventHelper() {

				public void onOpenDocument(PdfWriter writer, Document document) {
					
					
				}
				
				@Override
				public void onStartPage(PdfWriter writer, Document document) {

				}

				public void onEndPage(PdfWriter writer, Document document) {	
					try {
						tabelaTitulo.writeSelectedRows(0, -1, 0, -1, 16, PageSize.A4.width() - 16, writer.getDirectContent());
					} catch (Exception e) {
						throw new RuntimeException(e);
					}
					
					PdfContentByte cb = writer.getDirectContent();
					cb.saveState();
					String text = "Página " + writer.getPageNumber() + " de ";
					float textBase = document.bottom() -2;
					float textSize = helv.getWidthPoint(text, 12);
					cb.beginText();
					cb.setFontAndSize(helv, 12);
					float adjust = helv.getWidthPoint("0", 12);
					cb.setTextMatrix(document.right() - textSize - adjust, textBase);
					cb.showText(text);
					cb.endText();
					cb.addTemplate(total, document.right() - adjust, textBase);
					cb.restoreState();
				}
				
				public void onCloseDocument(PdfWriter writer, Document document) {
					total.beginText();
					total.setFontAndSize(helv, 12);
					total.setTextMatrix(0, 0);
					total.showText(String.valueOf(writer.getPageNumber() - 1));
					total.endText();
				}
			});
			
		}     
		
		{
			//tabela com os filtros
			Font fontTitulo = new Font(Font.HELVETICA, 12, Font.BOLD);
			fontTitulo.setColor(new Color(68,68,68));
			Font fontValor = new Font(Font.HELVETICA, 12);
			fontValor.setColor(new Color(160,160,160));
			
			PdfPTable table = new PdfPTable(2);
			//table.getDefaultCell().setLeading(0, 0.2f);
			table.setWidths(new int[]{1,5});
			PdfPCell cellGestaoTitulo = new PdfPCell();
			PdfPCell cellGestaoValor = new PdfPCell();
			PdfPCell cellAcumuladosTitulo = new PdfPCell();
			PdfPCell cellAcumuladosValor = new PdfPCell();
						
			Paragraph paragraph1 = new Paragraph( Nomes.Plano_de_Gestao + ":", fontTitulo);
			paragraph1.setAlignment(Element.ALIGN_RIGHT);
			cellGestaoTitulo.addElement(paragraph1);
			cellGestaoValor.addElement(new Paragraph(String.valueOf(planoGestao.getAnoExercicio()), fontValor));
			
			Paragraph paragraph2 = new Paragraph("Tipo de valor:", fontTitulo);
			paragraph2.setAlignment(Element.ALIGN_RIGHT);
			cellAcumuladosTitulo.addElement(paragraph2);
			cellAcumuladosValor.addElement(new Paragraph(valoresApurados? "Apurados":"Acumulados", fontValor));
			
			cellGestaoTitulo.setBorder(0);
			cellGestaoValor.setBorder(0);
			cellAcumuladosTitulo.setBorder(0);
			cellAcumuladosValor.setBorder(0);
			
			cellGestaoTitulo.setHorizontalAlignment(Element.ALIGN_RIGHT);
			cellAcumuladosTitulo.setHorizontalAlignment(Element.ALIGN_RIGHT);
			
			table.addCell(cellGestaoTitulo);
			table.addCell(cellGestaoValor);
			table.addCell(cellAcumuladosTitulo);
			table.addCell(cellAcumuladosValor);
			
			table.setSpacingAfter(34);
			
			document.add(table);
		}
		rederItens(document, unidades);
		
		document.close();
		
		return arrayOutputStream.toByteArray();
	}


	
	private void rederItens(Document document, List<ItemPainelControleDTO> unidades) throws DocumentException, MalformedURLException, IOException {
		PdfPTable table = new PdfPTable(2);
		table.setWidthPercentage(100);
		table.setWidths(new float[]{1f,2f});
		for (ItemPainelControleDTO item : unidades) {
			renderItem(table, item, "");
		}
		document.add(table);
	}

	private void renderItem(PdfPTable table, ItemPainelControleDTO item, String padding) throws MalformedURLException, IOException, DocumentException {
		renderTitleCell(table, item, padding);
		renderAcompanhamentos(table, item);
		for (ItemPainelControleDTO filho : item.getFilhos()) {
			renderItem(table, filho, padding+ "      ");
		}
	}

	private void renderTitleCell(PdfPTable table, ItemPainelControleDTO item, String padding) throws BadElementException, MalformedURLException, IOException {
		Paragraph paragraph = new Paragraph();
		Chunk chunk = new Chunk(getIcon(item),0,0);
		paragraph.add(new Phrase(padding));
		paragraph.add(chunk);
		paragraph.add(new Phrase(" "+item.getDescricao()));
		PdfPCell cell = new PdfPCell(paragraph);
		cell.setBorderColor(Color.LIGHT_GRAY);
		table.addCell(cell);
	}


	
	private void renderAcompanhamentos(PdfPTable table, ItemPainelControleDTO item) throws MalformedURLException, IOException, DocumentException {
		int colunas = item.getAcompanhamentos().size();
		if(colunas == 0 || !item.isMostraFarol()){
			PdfPCell cell = new PdfPCell(new Paragraph(""));
			cell.setBorderColor(Color.LIGHT_GRAY);
			table.addCell(cell);
			return;
		}
		List<PdfPTable> tabelas = new ArrayList<PdfPTable>();
		List<List<AcompanhamentoIndicador>> list = new ArrayList<List<AcompanhamentoIndicador>>();
		if(colunas == 24){//dividir em 2 linhas a tabela
			colunas = 12;
			if(valoresApurados){
				list.add(item.getAcompanhamentos().subList(0, 12));
				list.add(item.getAcompanhamentos().subList(12, 24));	
			} else {
				list.add(item.getAcompanhamentosAcumulados().subList(0, 12));
				list.add(item.getAcompanhamentosAcumulados().subList(12, 24));
			}
			
		} else {
			if(valoresApurados){
				list.add(item.getAcompanhamentos());	
			} else {
				list.add(item.getAcompanhamentosAcumulados());
			}
		}
		
		for (List<AcompanhamentoIndicador> listaAcompanhamentos : list) {
			PdfPTable tableAcompanhamentos = new PdfPTable(colunas + 1);
			if(colunas == 12){
				tableAcompanhamentos.setWidths(new int[]{1,1,1,1,1,1,1,1,1,1,1,1,1});
			} else {
				tableAcompanhamentos.setWidths(new int[]{1,3,3,3,3});
			}
			tableAcompanhamentos.setWidthPercentage(100);
			renderAcompanhamentos(tableAcompanhamentos, listaAcompanhamentos);
			tabelas.add(tableAcompanhamentos);
			
		}
		PdfPCell cell = new PdfPCell();
		cell.setBorderColor(Color.LIGHT_GRAY);
		cell.setPadding(0);
		for (PdfPTable tabela : tabelas) {
			cell.addElement(tabela);
		}
		table.addCell(cell);	
	}

	private void renderAcompanhamentos(PdfPTable tableAcompanhamentos, List<AcompanhamentoIndicador> acompanhamentos) throws BadElementException, MalformedURLException, IOException {
		//adiciona os farois
		criarCelulaTitulo(tableAcompanhamentos, " ");
		for (AcompanhamentoIndicador acompanhamentoIndicador : acompanhamentos) {
			Image farol = getFarol(acompanhamentoIndicador);
			int espaco = acompanhamentos.size() == 12? 11 : 51;
			Chunk chunk = new Chunk(farol, espaco, 0);
			PdfPCell cell = new PdfPCell();
			cell.setPadding(0);
			cell.setBorderColor(Color.LIGHT_GRAY);
			cell.setBackgroundColor(new Color(245,245,245));
			cell.addElement(chunk);
			tableAcompanhamentos.addCell(cell);
		}
		
		MelhorDoIndicadorEnum melhor = null;
		Double percentualTolerancia = null;
		for (int i = 0; i < acompanhamentos.size() && i < 1; i++) {
			AcompanhamentoIndicador acompInd = acompanhamentos.get(i);
			melhor = acompInd.getMelhor();
			percentualTolerancia = acompInd.getPercentualTolerancia();
		}		
		
		//%Real
		criarCelulaTitulo(tableAcompanhamentos, "%R");	
		for (AcompanhamentoIndicador acompanhamentoIndicador : acompanhamentos) {
			PdfPCell cell = new PdfPCell();
			cell.setBorderColor(Color.LIGHT_GRAY);
			Paragraph paragraph = new Paragraph(format(acompanhamentoIndicador.getPercentualReal()));
			paragraph.setAlignment(Element.ALIGN_RIGHT);
			cell.addElement(paragraph);
			tableAcompanhamentos.addCell(cell);
		}
		
		if (percentualTolerancia != null && percentualTolerancia.doubleValue() != 0.0) {
			//%Tolerancia
			criarCelulaTitulo(tableAcompanhamentos, "%T");
			for (AcompanhamentoIndicador acompanhamentoIndicador : acompanhamentos) {
				PdfPCell cell = new PdfPCell();
				cell.setBorderColor(Color.LIGHT_GRAY);
				Paragraph paragraph = new Paragraph(format(acompanhamentoIndicador.getPercentualTolerancia()));
				paragraph.setAlignment(Element.ALIGN_RIGHT);
				cell.addElement(paragraph);
				tableAcompanhamentos.addCell(cell);
			}
		}
		
		//Verifica o label dos acompanhamentos de acordo com o melhor do indicador
		String labelSuperior = "";		
		String labelInferior = "";
		
		if (melhor != null) {
			if (melhor.equals(MelhorDoIndicadorEnum.MELHOR_CIMA) || melhor.equals(MelhorDoIndicadorEnum.MELHOR_ENTRE_FAIXAS)) {
				if (melhor.equals(MelhorDoIndicadorEnum.MELHOR_CIMA)) {
					labelSuperior = "Meta";					
				}
				else {
					labelSuperior = "L. Sup.";					
				}
				
				//limite superior
				criarCelulaTitulo(tableAcompanhamentos, labelSuperior);
				for (AcompanhamentoIndicador acompanhamentoIndicador : acompanhamentos) {
					PdfPCell cell = new PdfPCell();
					cell.setBorderColor(Color.LIGHT_GRAY);
					Paragraph paragraph = new Paragraph(acompanhamentoIndicador.getValorLimiteSuperiorAsString());
					paragraph.setAlignment(Element.ALIGN_RIGHT);
					cell.addElement(paragraph);
					tableAcompanhamentos.addCell(cell);
				}				
			}
			
			//valor real
			criarCelulaTitulo(tableAcompanhamentos, "Real");
			for (AcompanhamentoIndicador acompanhamentoIndicador : acompanhamentos) {
				PdfPCell cell = new PdfPCell();
				cell.setBorderColor(Color.LIGHT_GRAY);
				Paragraph paragraph = new Paragraph(acompanhamentoIndicador.getValorRealAsString());
				paragraph.setAlignment(Element.ALIGN_RIGHT);
				cell.addElement(paragraph);
				tableAcompanhamentos.addCell(cell);
			}			
			
			if (melhor.equals(MelhorDoIndicadorEnum.MELHOR_BAIXO) || melhor.equals(MelhorDoIndicadorEnum.MELHOR_ENTRE_FAIXAS)) {
				if (melhor.equals(MelhorDoIndicadorEnum.MELHOR_BAIXO)) {
					labelInferior = "Meta";
				}
				else {
					labelInferior = "L. Inf.";
				}
				
				//limite inferior
				criarCelulaTitulo(tableAcompanhamentos, labelInferior);
				for (AcompanhamentoIndicador acompanhamentoIndicador : acompanhamentos) {
					PdfPCell cell = new PdfPCell();
					cell.setBorderColor(Color.LIGHT_GRAY);
					Paragraph paragraph = new Paragraph(acompanhamentoIndicador.getValorLimiteInferiorAsString());
					paragraph.setAlignment(Element.ALIGN_RIGHT);
					cell.addElement(paragraph);
					tableAcompanhamentos.addCell(cell);
				}				
			}
		}
	}

	DecimalFormat decimalFormat = new DecimalFormat("#,##0.##");
	private String format(Double percentualReal) {
		if(percentualReal == null || percentualReal.isNaN()){
			return "";
		} else {
			return decimalFormat.format(percentualReal);
		}
	}

	private void criarCelulaTitulo(PdfPTable tableAcompanhamentos, String titulo) {
		PdfPCell cellT = new PdfPCell();
		cellT.setPadding(1);
		cellT.setBorderColor(Color.LIGHT_GRAY);
		cellT.setBackgroundColor(new Color(245,245,245));
		cellT.addElement(new Paragraph(titulo));
		tableAcompanhamentos.addCell(cellT);
	}


	/* *****************************************
	 *           inicializacao j2se
	 * *****************************************/
	public static void main(String[] args) throws DocumentException, MalformedURLException, IOException {
		NeoStandard.createNeoContext();
		PlanoGestao planoGestao = new PlanoGestao();
		planoGestao.setId(6);
		UnidadeGerencial unidadeGerencial = new UnidadeGerencial();
		unidadeGerencial.setId(1);
		System.out.println("renderizando...");
		byte[] criarRelatorio = new PainelControleReportGenerator(planoGestao, unidadeGerencial, true).criarRelatorio();
		FileOutputStream fileOutputStream = new FileOutputStream("painel.pdf");
		fileOutputStream.write(criarRelatorio);
		fileOutputStream.flush();
		fileOutputStream.close();
		System.out.println("relatório gerado");
	}

}

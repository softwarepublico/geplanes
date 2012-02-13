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
package br.com.linkcom.sgm.report.bean;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRResultSetDataSource;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanArrayDataSource;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.data.JRMapArrayDataSource;
import net.sf.jasperreports.engine.data.JRMapCollectionDataSource;
import net.sf.jasperreports.view.JRViewer;
import br.com.linkcom.neo.core.standard.Neo;
import br.com.linkcom.neo.core.standard.NeoStandard;
import br.com.linkcom.neo.core.standard.RequestContext;
import br.com.linkcom.neo.exception.ReportException;
import br.com.linkcom.neo.report.JRIteratorDataSource;
import br.com.linkcom.neo.report.Report;
import br.com.linkcom.sgm.beans.AcompanhamentoIndicador;
import br.com.linkcom.sgm.beans.Usuario;
import br.com.linkcom.sgm.beans.DTO.ApresentacaoResultadosDTO;
import br.com.linkcom.sgm.beans.DTO.ApresentacaoResultadosReportDTO;
import br.com.linkcom.sgm.beans.enumeration.GraficoTipoEnum;
import br.com.linkcom.sgm.dao.UsuarioDAO;
import br.com.linkcom.sgm.service.IndicadorService;

import com.lowagie.text.DocumentException;

public class ApresentacaoResultadosReportBean {
	
	@SuppressWarnings("unused")
	private ApresentacaoResultadosDTO filtro;
	private List<ApresentacaoResultadosReportDTO> lista;
	
	SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
	DecimalFormat decimalFormat       = new DecimalFormat("###,##0.00");
	String[] farol					  = new String[]{"Valor não cadastrado","Meta não cumprida","Meta cumprida parcialmente","Meta cumprida em 100%","Meta não aplicável"};

	public ApresentacaoResultadosReportBean(ApresentacaoResultadosDTO filtro){
		this.filtro = filtro;
		execute();
	}
	
	private void execute() {
		lista = new ArrayList<ApresentacaoResultadosReportDTO>();
		lista.add(criarDTO(1, "", "", "", 0d, 0d, 0d, 0d, "", 0));
		
		if (filtro.getTipoGrafico().equals(GraficoTipoEnum.FAROL)) {
			if (filtro.getIndicadorSelecionado() != null) {
				for (int i = 0; i <= 4; i++) {
					lista.add(criarDTO( filtro.getIndicadorSelecionado().getId(),
							filtro.getIndicadorSelecionado().getNome(),
							"",
							"",
							0d,
							0d,
							0d,
							0d,
							farol[i],
							filtro.getIndicadorSelecionado().getNumFarois()[i]
					));					
				}
			}
		}
		else {
			if (filtro.getIndicadorSelecionado() != null) {
				for (AcompanhamentoIndicador acompanhamentoIndicador : filtro.getIndicadorSelecionado().getAcompanhamentosIndicador()) {
					lista.add(criarDTO( filtro.getIndicadorSelecionado().getId(),
							filtro.getIndicadorSelecionado().getNome(),
							simpleDateFormat.format(acompanhamentoIndicador.getDataInicial().getTime()),
							acompanhamentoIndicador.getEpoca(),
							acompanhamentoIndicador.getValorLimiteInferior(),
							acompanhamentoIndicador.getValorReal(),
							acompanhamentoIndicador.getValorLimiteSuperior(),
							acompanhamentoIndicador.getPercentualReal(),
							"",
							0
					));
				}
			}
		}
	}

	private ApresentacaoResultadosReportDTO criarDTO(int i, String indicador, String data, String epoca, Double valorLimiteInferior, Double valorReal, Double valorLimiteSuperior, Double percentual, String farol, int numFarois) {
		ApresentacaoResultadosReportDTO apresentacaoResultadosDTO = new ApresentacaoResultadosReportDTO();
		apresentacaoResultadosDTO.setIndicadorid(i);
		apresentacaoResultadosDTO.setData(data);
		apresentacaoResultadosDTO.setEpoca(epoca);		
		apresentacaoResultadosDTO.setIndicador(indicador);
		apresentacaoResultadosDTO.setValorLimiteInferior(valorLimiteInferior != null ? decimalFormat.format(valorLimiteInferior) : "");
		apresentacaoResultadosDTO.setValorReal(valorReal != null ? decimalFormat.format(valorReal) : "");
		apresentacaoResultadosDTO.setValorLimiteSuperior(valorLimiteSuperior != null ? decimalFormat.format(valorLimiteSuperior) : "");
		apresentacaoResultadosDTO.setPercentual(percentual != null ? decimalFormat.format(percentual)+"%" : "");
		apresentacaoResultadosDTO.setFarol(farol);
		apresentacaoResultadosDTO.setNumFarois(String.valueOf(numFarois));
		return apresentacaoResultadosDTO;
	}

	public String getDataInicial(){
		return simpleDateFormat.format(filtro.getDataInicial().getTime());
	}
	
	public String getDataFinal(){
		return simpleDateFormat.format(filtro.getDataFinal().getTime());
	}	
	
	public String getGraficoTipo(){
		return filtro.getTipoGrafico().toString();
	}
	
	private static Object getTitulo() {
		return "titulo";
	}

	public String getGraficoApresentacao() {
		return filtro.getFormaApresentacao().toString();
	}
	
	public String getGraficoTipoResultado() {
		return filtro.getTipoResultado().toString();
	}

	public String getPlanoGestao() {
		return filtro.getPlanoGestao().getAnoExercicio().toString();
	}

	public String getUnidadeGerencial() {
		return filtro.getUnidadeGerencial().getNome();
	}

	public InputStream getGrafico() {
		return filtro.getGrafico();
	}

	public List<ApresentacaoResultadosReportDTO> getDataSource() {
		return lista;
	}
	
	
	//inicializacao j2se
	private static Report getReport() {
		ApresentacaoResultadosDTO filtro = new ApresentacaoResultadosDTO();
		Report report = (Report) Neo.getObject(IndicadorService.class).createReportApresentacaoResultados(filtro);
		return report;
	}

	public static void main(String[] args) throws JRException, UnsupportedLookAndFeelException, ClassNotFoundException, InstantiationException, IllegalAccessException, IOException, DocumentException {
		UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		NeoStandard.createNeoContext();
		Usuario colaborador = new Usuario();
		colaborador.setId(1);
		Usuario load = Neo.getObject(UsuarioDAO.class).load(colaborador);
		NeoStandard.setUser(load);
		
		JasperPrint jasperPrint = getJasperPrint();
		final JFrame frame = new JFrame();
		frame.setSize(800, 540);
		//frame.setAlwaysOnTop(true);
		JButton button = new JButton("Reload");
		final JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());
		frame.add(panel);
		panel.add(button, BorderLayout.NORTH);
		final JRViewer[] viewers = new JRViewer[1];
		final RequestContext requestContext = Neo.getRequestContext();
		button.addActionListener(new ActionListener(){

			public void actionPerformed(ActionEvent e) {
				try {
					
					Neo.setRequestContext(requestContext);
					JasperPrint jasperPrint = getJasperPrint();
					JRViewer viewer = new JRViewer(jasperPrint);
					panel.remove(viewers[0]);
					viewers[0] = viewer;
					panel.add(viewer, BorderLayout.CENTER);
					frame.validate();
				} catch (FileNotFoundException e1) {
					JOptionPane.showMessageDialog(null, e1.getMessage());
					e1.printStackTrace();
				} catch (JRException e1) {
					JOptionPane.showMessageDialog(null, e1.getMessage());
					e1.printStackTrace();
				} catch(Exception e1){
					JOptionPane.showMessageDialog(null, e1.getMessage());
					e1.printStackTrace();
				}
				
			}});
		
		JRViewer viewer = new JRViewer(jasperPrint);
		panel.add(viewer, BorderLayout.CENTER);
		viewers[0] = viewer;
		frame.setVisible(true);
	}

	private static JasperPrint getJasperPrint() throws JRException, IOException, DocumentException {
		JasperPrint jasperPrint = getJasperPrint(getReport());		
		return jasperPrint;
	}

	private static JasperPrint getJasperPrint(Report relatorio) throws JRException, FileNotFoundException {
		montarParametros(relatorio);
		JRDataSource dataSource = montarDataSource(relatorio);
		JasperPrint jasperPrint = getJasperPrint(relatorio, dataSource);
		return jasperPrint;
	}

	private static JasperPrint getJasperPrint(Report relatorio, JRDataSource dataSource) throws JRException, FileNotFoundException {
		JasperPrint jasperPrint = JasperFillManager.fillReport(
				new FileInputStream(new File("WebRoot/WEB-INF/relatorio/"+relatorio.getName()+".jasper")), 
				relatorio.getParameters(), 
				dataSource);
		return jasperPrint;
	}

	private static void montarParametros(Report relatorio) {
		//RequestContext request = Neo.getRequestContext();
		
		String nome = "usuario";


		relatorio.addParameter("TITULO", getTitulo());
		relatorio.addParameter("DATA",new Date(System.currentTimeMillis()));
		relatorio.addParameter("HORA", System.currentTimeMillis());
		relatorio.addParameter("USUARIO", nome);
		relatorio.addParameter("HEADER", "GRÁFICO");	
	}

	private static JRDataSource montarDataSource(Report relatorio) {
		Object ds = relatorio.getDataSource();
		JRDataSource dataSource = null;
		if(ds instanceof JRDataSource){
			dataSource = (JRDataSource)ds;
		} else if(ds instanceof ResultSet){
			dataSource = new JRResultSetDataSource((ResultSet)ds);
		} else if(ds instanceof Object[]){
			Object[] array = (Object[]) ds;
			if (array.length > 0 && array[0] instanceof Map) {
				dataSource = new JRMapArrayDataSource(array);
			} else {
				dataSource = new JRBeanArrayDataSource(array);
			}
		} else if(ds instanceof Collection<?>){
			Iterator<?> iterator = ((Collection<?>)ds).iterator();
			Object primeiroElemento = null;
			if (iterator.hasNext()) {
				primeiroElemento = iterator.next();
			}
			if (primeiroElemento instanceof Map) {
				dataSource = new JRMapCollectionDataSource((Collection<?>)ds);
			} else {
				dataSource = new JRBeanCollectionDataSource((Collection<?>)ds);
			}
		} else if(ds instanceof Iterator<?>){
			Iterator<?> iterator = (Iterator<?>)ds;
			dataSource = new JRIteratorDataSource(iterator);
		} else if(ds == null){
			dataSource = new JREmptyDataSource();
		} else {
			throw new ReportException("O tipo de datasource é inválido! "+dataSource.getClass());
		}
		return dataSource;
	}




}

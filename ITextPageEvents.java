package dgen.files.pdf.builders.itext;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.ElementList;
import com.itextpdf.tool.xml.XMLWorker;
import com.itextpdf.tool.xml.XMLWorkerHelper;
import com.itextpdf.tool.xml.html.Tags;
import com.itextpdf.tool.xml.parser.XMLParser;
import com.itextpdf.tool.xml.pipeline.css.CSSResolver;
import com.itextpdf.tool.xml.pipeline.css.CssResolverPipeline;
import com.itextpdf.tool.xml.pipeline.end.ElementHandlerPipeline;
import com.itextpdf.tool.xml.pipeline.html.HtmlPipeline;
import com.itextpdf.tool.xml.pipeline.html.HtmlPipelineContext;

public class ITextPageEvents extends PdfPageEventHelper {
	private String pageHeader;
	private String pageFooter;
	
	public void setPageHeader(String contents) {
		this.pageHeader = contents;
	}
	
	public void setPageFooter(String contents) {
		this.pageFooter = contents;
	}
	
	public String getPageHeader() {
		return this.pageHeader;
	}
	
	public String getPageFooter() {
		return this.pageFooter;
	}
	
	public void onEndPage(PdfWriter writer, Document document) {
		Rectangle containerBox = null;
		
		try {
			float pageWidth = document.getPageSize().getWidth();
			float pageHeight = document.getPageSize().getHeight();
			
			// Add page header
			containerBox = new Rectangle(20, 20, pageWidth-20, pageHeight-20);
			addPageContents(writer, getPageHeader(), containerBox);
			
			// Add page footer
			containerBox = new Rectangle(20, 20, pageWidth-20, 100);
			addPageContents(writer, getPageFooter(), containerBox);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void addPageContents(PdfWriter writer, String contents, Rectangle containerBox) {
		try {
			// Add page header
			HtmlPipelineContext htmlContext = new HtmlPipelineContext(null);
			htmlContext.setTagFactory(Tags.getHtmlTagProcessorFactory());
			CSSResolver cssResolver = XMLWorkerHelper.getInstance().getDefaultCssResolver(true);
			
			ElementList elements = new ElementList();
			ElementHandlerPipeline elemHPipeline = new ElementHandlerPipeline(elements, null);
			HtmlPipeline htmlPipeline = new HtmlPipeline(htmlContext, elemHPipeline);
			CssResolverPipeline cssRPipeline = new CssResolverPipeline(cssResolver, htmlPipeline);			
			
			XMLWorker xmlWorker = new XMLWorker(cssRPipeline, true);
			XMLParser parser = new XMLParser(xmlWorker);
			parser.parse(new ByteArrayInputStream(contents.getBytes()));
			
			ColumnText ct = new ColumnText(writer.getDirectContent());
			ct.setSimpleColumn(containerBox);
			ct.addElement(elements.get(0));
			ct.go();
		} catch (DocumentException | IOException e) {
			e.printStackTrace();
		}
		
	}
}

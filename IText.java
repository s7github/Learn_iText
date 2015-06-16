package dgen.files.pdf.builders;

import java.io.ByteArrayInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.ElementList;
import com.itextpdf.tool.xml.XMLWorker;
import com.itextpdf.tool.xml.XMLWorkerHelper;
import com.itextpdf.tool.xml.exceptions.CssResolverException;
import com.itextpdf.tool.xml.html.Tags;
import com.itextpdf.tool.xml.parser.XMLParser;
import com.itextpdf.tool.xml.pipeline.css.CSSResolver;
import com.itextpdf.tool.xml.pipeline.css.CssResolverPipeline;
import com.itextpdf.tool.xml.pipeline.end.ElementHandlerPipeline;
import com.itextpdf.tool.xml.pipeline.end.PdfWriterPipeline;
import com.itextpdf.tool.xml.pipeline.html.HtmlPipeline;
import com.itextpdf.tool.xml.pipeline.html.HtmlPipelineContext;

import dgen.CustomException;
import dgen.HTMLPage;
import dgen.files.FilePDF;
import dgen.files.pdf.builders.itext.ITextPageEvents;
import entities.Margins;

public class IText implements dgen.files.builders.IFileBuilder {
	/**
	 * @param outputFileName
	 * @param htmlData
	 * @throws CustomException 
	 */
	public void generateFromHTML(String outputFileName, FilePDF filePdf) 
	throws CustomException {
		PdfWriter writer = null;
		Document document = new Document();
		HTMLPage htmlData = filePdf.getHTMLContents(); 
		
		try {
			writer = PdfWriter.getInstance(document, new FileOutputStream(outputFileName));
			ITextPageEvents pageEvents = new ITextPageEvents();
			writer.setPageEvent(pageEvents);

			// Set margins
			setMargins(document, filePdf);
			
			document.open();
			pageEvents.setPageHeader(htmlData.getPageHeader().outerHtml());
			pageEvents.setPageFooter(htmlData.getPageFooter().outerHtml());
			
			HtmlPipelineContext htmlContext = new HtmlPipelineContext(null);
			htmlContext.setTagFactory(Tags.getHtmlTagProcessorFactory());
			CSSResolver cssResolver = XMLWorkerHelper.getInstance().getDefaultCssResolver(true);
			cssResolver.addCss(htmlData.getHTMLStyles().get(0), true);
			
			ElementList elements = new ElementList();
			ElementHandlerPipeline elemHPipeline = new ElementHandlerPipeline(elements, null);
			//PdfWriterPipeline pdfPipeline = new PdfWriterPipeline(document, writer);
			HtmlPipeline htmlPipeline = new HtmlPipeline(htmlContext, elemHPipeline);
			CssResolverPipeline cssRPipeline = new CssResolverPipeline(cssResolver, htmlPipeline);			
			
			XMLWorker xmlWorker = new XMLWorker(cssRPipeline, true);
			XMLParser parser = new XMLParser(xmlWorker);			
			parser.parse(new ByteArrayInputStream(htmlData.getPageBody().outerHtml().getBytes()));			
						
			PdfPTable tbl = null;
			for(Element elemL1: elements) {
				if (elemL1 instanceof PdfPTable) {
					tbl = ((PdfPTable)elemL1);
					tbl.setHeaderRows(1);
				}
				document.add(elemL1);
			}
			
			/*XMLWorkerHelper xmlWorker = XMLWorkerHelper.getInstance();
			
			ByteArrayInputStream inHTML = new ByteArrayInputStream(htmlData.getPageBody().outerHtml().getBytes());
			ArrayList<String> cssStyles = htmlData.getHTMLStyles();
			if (cssStyles.size() > 0) {
				ByteArrayInputStream inCSS = new ByteArrayInputStream(cssStyles.get(0).getBytes());
				xmlWorker.parseXHtml(writer, document, inHTML, inCSS);
			} else {
				xmlWorker.parseXHtml(writer, document, inHTML);
			}*/
			
		} catch (IOException e) {
			e.printStackTrace();
			throw new CustomException("Error while parsing XHML");
		} catch (DocumentException e) {
			e.printStackTrace();
			throw new CustomException("Error while generating document through IText");
		} catch (CssResolverException e) {
			e.printStackTrace();
			throw new CustomException("Error while CSS resolver operations");
		}finally {
			// Close document
			if (document.isOpen()) {
				document.close();
			}
		}
	}
	
	public void setMargins(Document doc, FilePDF filePdf) {
		float mLeft, mTop, mRight, mBottom;
		
		Margins pdfMargins = filePdf.getMargins();

		// Left
		mLeft = pdfMargins.getLeftMargin();
		if (mLeft < 0) {
			mLeft = 36;
		}

		// Right
		mRight = pdfMargins.getRightMargin();
		if (mRight < 0) {
			mLeft = 36;
		}

		// Top
		mTop = pdfMargins.getTopMargin();
		if (mTop < 0) {
			mTop = 36;
		}

		// Bottom
		mBottom = pdfMargins.getBottomMargin();
		if (mBottom < 0) {
			mBottom = 36;
		}
		
		doc.setMargins(mLeft, mRight, mTop, mBottom);
	}
}

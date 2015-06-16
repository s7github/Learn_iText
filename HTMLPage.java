package dgen;

import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.parser.Parser;

public class HTMLPage {
	private String headerTagID;
	private String footerTagID;
	private ArrayList<String> cssStyle;
	private final Document parsedHTML;
	
	public HTMLPage(String htmlContents) {
		cssStyle = new ArrayList<String>();
		this.parsedHTML = Jsoup.parse(htmlContents, "", Parser.xmlParser());
	}
	
	public Document getParsedHTML() {
		return this.parsedHTML;
	}
	
	public boolean hasContents() {
		if (getParsedHTML().children().size() > 0) {
			return true;
		}
		
		return false;
	}

	private String getHTMLWrapperTop() {
		StringBuilder htmlWrapperTop = new StringBuilder();
		
		for (Node htmlNode: getParsedHTML().childNodes()) {
			if (htmlNode instanceof DocumentType) {
				DocumentType docType = (DocumentType) htmlNode;
				htmlWrapperTop.append(docType.toString());
			}
		}
		htmlWrapperTop.append("<html>");
		
		return htmlWrapperTop.toString();
	}

	private String getHTMLWrapperBottom() {
		StringBuilder htmlWrapperBottom = new StringBuilder();
		htmlWrapperBottom.append("</html>");
		
		return htmlWrapperBottom.toString();
	}
	
	private Element getHTMLHead() {
		return getParsedHTML().head();
	}

	public Element getPageBody() {
		Document docHTML = getParsedHTML().clone();
		
		// Remove page header
		if(getHeaderID().length() > 0) {
			Element elemHeader = docHTML.getElementById(getHeaderID());
			elemHeader.remove();
		}
		// Remove page footer
		if (getFooterID().length() > 0) {
			Element elemFooter = docHTML.getElementById(getFooterID());
			elemFooter.remove();
		}
		
		return docHTML.body();
	}
	
	public void mapHeaderID(String htmlElemID) {
		this.headerTagID = htmlElemID;
	}
	
	private String getHeaderID() {
		return this.headerTagID;
	}

	public Element getPageHeader() {
		return getParsedHTML().getElementById(getHeaderID());
	}
	
	public void mapFooterID(String htmlElemID) {
		this.footerTagID = htmlElemID;
	}
	
	private String getFooterID() {
		return this.footerTagID;
	}

	public Element getPageFooter() {
		return getParsedHTML().getElementById(getFooterID());
	}

	public void addHTMLStyle(String cssContents) {
		this.cssStyle.add(cssContents);
	}
	
	public ArrayList<String> getHTMLStyles() {
		return this.cssStyle;
	}
	
	public String getCompletePage() {
		StringBuilder htmlPage = new StringBuilder();		
		
		// HTML wrapper top
		htmlPage.append(getHTMLWrapperTop());
			// head
			htmlPage.append(getHTMLHead());
			// body wrapper top
			htmlPage.append("<body" + getPageBody().attributes().html() + ">");
				// header
				htmlPage.append(getPageHeader());
				// body contents
				htmlPage.append(getPageBody().html());
				// footer
				htmlPage.append(getPageFooter());
			// body wrapper bottom
			htmlPage.append("</body");
		// HTML wrapper bottom
		htmlPage.append(getHTMLWrapperBottom());
		
		return htmlPage.toString(); 
	}
}

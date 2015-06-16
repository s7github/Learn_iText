package dgen.files;

import dgen.CustomException;
import dgen.FileContentType;
import dgen.HTMLPage;
import dgen.enums.MeasureUnits;
import dgen.files.builders.IFileBuilder;
import entities.Margins;

public class FilePDF {
	public String fileName;
	private FileContentType contentType;
	private HTMLPage htmlData;
	private String pagePlainText;
	private String pageHeaderText;
	private String pageFooterText;
	private IFileBuilder pdfBuilder;
	private Margins margins;
	private MeasureUnits units;
	
	/************** Constructor ******************/
	public FilePDF(IFileBuilder fileBuilder) {
		this.fileName = null;
		this.pdfBuilder = fileBuilder;
		this.margins = new Margins();
		this.units = MeasureUnits.MM;
	}
	
	/************* Properties *******************/
	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public FileContentType getContentType() {
		return this.contentType;
	}

	public void setContentType(FileContentType contentType) {
		this.contentType = contentType;
	}

	public HTMLPage getHTMLContents() {
		return this.htmlData;
	}

	public void setHTMLContents(HTMLPage htmlData) {
		this.htmlData = htmlData;
		this.contentType = FileContentType.HTML;
	}
	
	public void setContents(String contents) {
		this.pagePlainText = contents;
		this.contentType = FileContentType.PLAIN;
	}
	
	public String getContents() {
		return this.pagePlainText;
	}
	
	public void setHeader(String contents) {
		this.pageHeaderText = contents;
		this.contentType = FileContentType.PLAIN;
	}
	
	public String getHeader() {
		return this.pageHeaderText;
	}
	
	public void setFooter(String contents) {
		this.pageFooterText = contents;
		this.contentType = FileContentType.PLAIN;
	}
	
	public String getFooter() {
		return this.pageFooterText;
	}

	public IFileBuilder getFileBuilder() {
		return this.pdfBuilder;
	}

	public MeasureUnits getUnits() {
		return units;
	}

	public void setUnits(MeasureUnits units) {
		this.units = units;
	}
	
	public void setMargins(float left, float right, float top, float bottom) {
		if (top > 0) {
			this.margins.setTopMargin(top);
		}
		if (right > 0) {
			this.margins.setRightMargin(right);
		}
		if (bottom > 0) {
			this.margins.setBottomMargin(bottom);
		}
		if (left > 0) {
			this.margins.setLeftMargin(left);
		}
	}
	
	public Margins getMargins() {
		return this.margins;
	}

	public void createFile() throws CustomException {
		if (getFileName() == null) {
			throw new CustomException("Output file name was not set.");
		}
		
		if (this.contentType == FileContentType.HTML) {
			IFileBuilder pdfBuilder = getFileBuilder();
			pdfBuilder.generateFromHTML(getFileName(), this);
		}
		else {
			//createPDFFromPlainText();
		}
	}
}

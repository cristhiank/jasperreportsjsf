package net.sf.jasperreports.jsf.context;

import net.sf.jasperreports.jsf.util.ServiceException;

public class UnsupportedContentTypeException extends ServiceException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4102317376355449646L;
	
	private ContentType contentType;
	
	public UnsupportedContentTypeException(ContentType contentType) {
		super(contentType.toString());
		this.contentType = contentType;
	}

	public ContentType getContentType() {
		return contentType;
	}

}

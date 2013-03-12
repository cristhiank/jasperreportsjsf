/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.sf.jasperreports.jsf.sample.simple;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

/**
 *
 * @author aalonsodominguez
 */
public class BooksReportBean {

	@PersistenceContext(unitName = "jrjsf-usecases")
	private EntityManager em;
	
    private List<Book> bookList;
    
    public List<Book> getBookList() {
    	if (bookList == null) {
    		TypedQuery<Book> q = em.createQuery("from Book", Book.class);
    		bookList = q.getResultList();
    	}
    	return bookList;
    }
    
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.jasperreports.jsf.engine.interop;

import java.io.InputStream;
import net.sf.jasperreports.engine.JasperReportsContext;
import net.sf.jasperreports.engine.util.FileResolver;
import net.sf.jasperreports.repo.DefaultRepositoryService;
import net.sf.jasperreports.repo.RepositoryContext;
import net.sf.jasperreports.repo.RepositoryService;
import net.sf.jasperreports.repo.Resource;

/**
 *
 * @author cristhian.lopez
 */
public class FacesRepositoryService implements RepositoryService {

    private DefaultRepositoryService wrapped;

    public FacesRepositoryService(JasperReportsContext ctx) {
        wrapped = new DefaultRepositoryService(ctx);
    }

    public void setContext(RepositoryContext context) {
        wrapped.setContext(context);
    }

    public void revertContext() {
        wrapped.revertContext();
    }

    public InputStream getInputStream(String uri) {
        return wrapped.getInputStream(uri);
    }

    public Resource getResource(String uri) {
        return wrapped.getResource(uri);
    }

    public void saveResource(String uri, Resource resource) {
        wrapped.saveResource(uri, resource);
    }

    public <K extends Resource> K getResource(String uri, Class<K> resourceType) {
        return wrapped.getResource(uri, resourceType);
    }

    public void setFileResolver(FileResolver fr) {
        wrapped.setFileResolver(fr);
    }
}

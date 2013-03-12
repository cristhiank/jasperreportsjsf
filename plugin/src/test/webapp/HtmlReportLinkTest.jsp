<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://jasperreportjsf.sf.net/tld/jasperreports-jsf-1_2.tld" prefix="jr" %>

<%--  
  JasperReports JSF Plugin
  Copyright (C) 2011 A. Alonso Dominguez

  This library is free software; you can redistribute it and/or
  modify it under the terms of the GNU Lesser General Public
  License as published by the Free Software Foundation; either
  version 2.1 of the License, or (at your option) any later version.

  This library is distributed in the hope that it will be useful,
  but WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
  Lesser General Public License for more details.

  You should have received a copy of the GNU Lesser General Public
  License along with this library; if not, write to the Free Software
  Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA

  A. Alonso Dominguez
  alonsoft@users.sf.net
--%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" 
    "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <title>Report Link Test</title>
    </head>

    <body>
        <f:view>
        	<h:form id="reportForm">
	            <jr:reportLink id="reportLink" format="html"
	                           value="/WEB-INF/report/Factura.jasper">
	                <h:outputText>ReportLink</h:outputText>
	            </jr:reportLink>
            </h:form>
        </f:view>
    </body>
</html>
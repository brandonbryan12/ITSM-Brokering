/*
 * Generated by the Jasper component of Apache Tomcat
 * Version: Apache Tomcat/7.0.85
 * Generated at: 2018-04-05 03:10:50 UTC
 * Note: The last modified time of this file was set to
 *       the last modified time of the source file after
 *       generation to assist with modification tracking.
 */
package org.apache.jsp.saml2.jsp;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;
import com.sun.identity.saml2.common.SAML2Constants;
import com.sun.identity.saml2.common.SAML2Utils;
import com.sun.identity.saml.common.SAMLUtils;
import com.sun.identity.saml2.common.SAML2Exception;
import com.sun.identity.saml2.profile.DoManageNameID;
import java.util.HashMap;
import org.owasp.esapi.ESAPI;

public final class idpMNIPOST_jsp extends org.apache.jasper.runtime.HttpJspBase
    implements org.apache.jasper.runtime.JspSourceDependent {

  private static final javax.servlet.jsp.JspFactory _jspxFactory =
          javax.servlet.jsp.JspFactory.getDefaultFactory();

  private static java.util.Map<java.lang.String,java.lang.Long> _jspx_dependants;

  private volatile javax.el.ExpressionFactory _el_expressionfactory;
  private volatile org.apache.tomcat.InstanceManager _jsp_instancemanager;

  public java.util.Map<java.lang.String,java.lang.Long> getDependants() {
    return _jspx_dependants;
  }

  public javax.el.ExpressionFactory _jsp_getExpressionFactory() {
    if (_el_expressionfactory == null) {
      synchronized (this) {
        if (_el_expressionfactory == null) {
          _el_expressionfactory = _jspxFactory.getJspApplicationContext(getServletConfig().getServletContext()).getExpressionFactory();
        }
      }
    }
    return _el_expressionfactory;
  }

  public org.apache.tomcat.InstanceManager _jsp_getInstanceManager() {
    if (_jsp_instancemanager == null) {
      synchronized (this) {
        if (_jsp_instancemanager == null) {
          _jsp_instancemanager = org.apache.jasper.runtime.InstanceManagerFactory.getInstanceManager(getServletConfig());
        }
      }
    }
    return _jsp_instancemanager;
  }

  public void _jspInit() {
  }

  public void _jspDestroy() {
  }

  public void _jspService(final javax.servlet.http.HttpServletRequest request, final javax.servlet.http.HttpServletResponse response)
        throws java.io.IOException, javax.servlet.ServletException {

    final javax.servlet.jsp.PageContext pageContext;
    javax.servlet.http.HttpSession session = null;
    final javax.servlet.ServletContext application;
    final javax.servlet.ServletConfig config;
    javax.servlet.jsp.JspWriter out = null;
    final java.lang.Object page = this;
    javax.servlet.jsp.JspWriter _jspx_out = null;
    javax.servlet.jsp.PageContext _jspx_page_context = null;


    try {
      response.setContentType("text/html");
      pageContext = _jspxFactory.getPageContext(this, request, response,
      			null, true, 8192, true);
      _jspx_page_context = pageContext;
      application = pageContext.getServletContext();
      config = pageContext.getServletConfig();
      session = pageContext.getSession();
      out = pageContext.getOut();
      _jspx_out = out;

      out.write('\n');
      out.write('\n');
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write('\n');

    try {
        HashMap paramsMap = new HashMap();
        paramsMap.put(SAML2Constants.ROLE, SAML2Constants.IDP_ROLE);
        String samlRequest = request.getParameter(SAML2Constants.SAML_REQUEST);

        String samlResponse =
            request.getParameter(SAML2Constants.SAML_RESPONSE);

        if (samlRequest != null) {
            DoManageNameID.processPOSTRequest(request, response, paramsMap);
        }

        if (samlResponse != null) {
            boolean success = DoManageNameID.processMNIResponsePOST(request,
                response, paramsMap);

            if (success == true) {
                String relayState = request.getParameter(SAML2Constants.RELAY_STATE);
                if (!ESAPI.validator().isValidInput("HTTP Parameter Value: " + relayState, relayState, "URL", 2000, true)) {
                    relayState = null;
                }
                if (relayState != null && SAML2Utils.isRelayStateURLValid(request, relayState, SAML2Constants.IDP_ROLE)) {
                    response.sendRedirect(relayState);
                } else {
                    
      out.write("\n");
      out.write("                    ");
      if (true) {
        _jspx_page_context.forward("/saml2/jsp/default.jsp?message=mniSuccess");
        return;
      }
      out.write("\n");
      out.write("                    ");

                }
            } else {
                SAMLUtils.sendError(request, response, response.SC_BAD_REQUEST,
                    "requestProcessingMNIError",
                    SAML2Utils.bundle.getString("requestProcessingMNIError"));
                return;
            }
        }
    } catch (SAML2Exception e) {
        SAML2Utils.debug.error("Error processing ManageNameIDRequest " , e);
        SAMLUtils.sendError(request, response, response.SC_BAD_REQUEST,
            "requestProcessingMNIError",
            SAML2Utils.bundle.getString("requestProcessingMNIError") + " " +
            e.getMessage());
        return;
    }

      out.write('\n');
      out.write('\n');
    } catch (java.lang.Throwable t) {
      if (!(t instanceof javax.servlet.jsp.SkipPageException)){
        out = _jspx_out;
        if (out != null && out.getBufferSize() != 0)
          try {
            if (response.isCommitted()) {
              out.flush();
            } else {
              out.clearBuffer();
            }
          } catch (java.io.IOException e) {}
        if (_jspx_page_context != null) _jspx_page_context.handlePageException(t);
        else throw new ServletException(t);
      }
    } finally {
      _jspxFactory.releasePageContext(_jspx_page_context);
    }
  }
}

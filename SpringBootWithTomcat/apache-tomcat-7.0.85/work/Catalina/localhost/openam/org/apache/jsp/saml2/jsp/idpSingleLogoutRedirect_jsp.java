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
import com.sun.identity.saml.common.SAMLUtils;
import com.sun.identity.saml2.common.SAML2Utils;
import com.sun.identity.saml2.common.SAML2Constants;
import com.sun.identity.saml2.common.SAML2Exception;
import com.sun.identity.saml2.profile.IDPCache;
import com.sun.identity.saml2.profile.IDPSingleLogout;
import org.owasp.esapi.ESAPI;
import java.io.PrintWriter;

public final class idpSingleLogoutRedirect_jsp extends org.apache.jasper.runtime.HttpJspBase
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
      out.write("\n");
      out.write("\n");
      out.write("<html>\n");
      out.write("\n");
      out.write("<head>\n");
      out.write("    <title>SAMLv2 Single Logout Redirect binding at IDP</title>\n");
      out.write("</head>\n");
      out.write("<body bgcolor=\"#FFFFFF\" text=\"#000000\">\n");
      out.write("\n");

    // Retrieves the LogoutRequest or LogoutResponse
    //Retrieves :
    //- RelayState - the target URL on successful Single Logout
    //- SAMLRequest - the LogoutRequest
    //OR
    //- SAMLResponse - the LogoutResponse

    String relayState = request.getParameter(SAML2Constants.RELAY_STATE);
    if (relayState != null) {
        String tmpRs = (String) IDPCache.relayStateCache.remove(relayState);
        if (tmpRs != null) {
            relayState = tmpRs;
        }
    }
    if (!ESAPI.validator().isValidInput("HTTP Query String: " + relayState, relayState, "HTTPQueryString", 2000, true)) {
        relayState = null;
    }
    String samlResponse = request.getParameter(SAML2Constants.SAML_RESPONSE);
    if (samlResponse != null) {
        boolean doRelayState = true;
        try {
        /**
         * Gets and processes the Single <code>LogoutResponse</code> from SP,
         * destroys the local session, checks response's issuer
         * and inResponseTo.
         *
         * @param request the HttpServletRequest.
         * @param response the HttpServletResponse.
         * @param samlResponse <code>LogoutResponse</code> in the
         *          XML string format.
         * @param relayState the target URL on successful
         * <code>LogoutResponse</code>.
         * @throws SAML2Exception if error processing
         *          <code>LogoutResponse</code>.
         */
            doRelayState = IDPSingleLogout.processLogoutResponse(
                request, response,samlResponse, relayState);
        } catch (SAML2Exception sse) {
            SAML2Utils.debug.error("Error processing LogoutResponse :",
                sse);
            SAMLUtils.sendError(request, response, response.SC_BAD_REQUEST,
                "LogoutResponseProcessingError",
                SAML2Utils.bundle.getString("LogoutResponseProcessingError") +
                " " + sse.getMessage());
            return;
        } catch (Exception e) {
            SAML2Utils.debug.error("Error processing LogoutResponse ",e);
            SAMLUtils.sendError(request, response, response.SC_BAD_REQUEST,
                "LogoutResponseProcessingError",
                SAML2Utils.bundle.getString("LogoutResponseProcessingError") +
                " " + e.getMessage());
            return;
        }

        if (!doRelayState) {
            if (relayState != null && SAML2Utils.isRelayStateURLValid(request, relayState, SAML2Constants.IDP_ROLE) &&
                ESAPI.validator().isValidInput("HTTP URL Value: " + relayState, relayState, "URL", 2000, true)) {
                if (relayState.indexOf("?") != -1) {
                    response.sendRedirect(relayState 
                        + "&logoutStatus=logoutSuccess");
                } else {
                    response.sendRedirect(relayState 
                        + "?logoutStatus=logoutSuccess");
                }
            } else {
                
      out.write("\n");
      out.write("                ");
      if (true) {
        _jspx_page_context.forward("/saml2/jsp/default.jsp?message=idpSloSuccess");
        return;
      }
      out.write("\n");
      out.write("                ");

            }    
        }
    } else {
        String samlRequest = request.getParameter(SAML2Constants.SAML_REQUEST);
        if (samlRequest != null) {
            try {
            /**
             * Gets and processes the Single <code>LogoutRequest</code> from SP.
             *
             * @param request the HttpServletRequest.
             * @param response the HttpServletResponse.
             * @param samlRequest <code>LogoutRequest</code> in the
             *          XML string format.
             * @param relayState the target URL on successful
             * <code>LogoutRequest</code>.
             * @throws SAML2Exception if error processing
             *          <code>LogoutRequest</code>.
             */
            IDPSingleLogout.processLogoutRequest(request,response, new PrintWriter(out, true),
                samlRequest,relayState);
            } catch (SAML2Exception sse) {
                SAML2Utils.debug.error("Error processing LogoutRequest :", sse);
                SAMLUtils.sendError(request, response, response.SC_BAD_REQUEST,
                    "LogoutRequestProcessingError",
                    SAML2Utils.bundle.getString("LogoutRequestProcessingError")
                    + " " + sse.getMessage());
                return;
            } catch (Exception e) {
                SAML2Utils.debug.error("Error processing LogoutRequest ",e);
                SAMLUtils.sendError(request, response, response.SC_BAD_REQUEST,
                    "LogoutRequestProcessingError",
                    SAML2Utils.bundle.getString("LogoutRequestProcessingError")
                    + " " + e.getMessage());
                return;
            }
        }
    }

      out.write("\n");
      out.write("\n");
      out.write("</body>\n");
      out.write("</html>\n");
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

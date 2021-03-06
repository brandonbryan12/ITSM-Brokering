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
import com.sun.identity.federation.common.FSUtils;
import com.sun.identity.saml2.common.SAML2Constants;
import com.sun.identity.saml2.common.SAML2Utils;
import com.sun.identity.saml.common.SAMLUtils;
import com.sun.identity.saml2.common.SAML2Exception;
import com.sun.identity.saml2.meta.SAML2MetaManager;
import com.sun.identity.saml2.meta.SAML2MetaUtils;
import com.sun.identity.saml2.profile.DoManageNameID;
import java.util.HashMap;
import org.owasp.esapi.ESAPI;

public final class spMNIRequestInit_jsp extends org.apache.jasper.runtime.HttpJspBase
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
      out.write('\n');
      out.write('\n');

    // Retreive the Request Query Parameters
    // metaAlias, idpEntiyID and RequestType are the required query parameters
    // metaAlias - Hosted Entity Id
    // idpEntityID - Identity Provider Identifier
    // requestType - the request type of ManageNameIDRequest (Terminate / NewID)
    // affiliationID - affiliation entity ID
    // Query parameters supported will be documented.

    if (FSUtils.needSetLBCookieAndRedirect(request, response, false)) {
        return;
    }

    try {
        String metaAlias = request.getParameter("metaAlias");
        if ((metaAlias ==  null) || (metaAlias.length() == 0)) {
            SAMLUtils.sendError(request, response, response.SC_BAD_REQUEST,
                "nullIDPEntityID", 
                SAML2Utils.bundle.getString("nullIDPEntityID"));
            return;
        }

        String idpEntityID = request.getParameter("idpEntityID");

        if ((idpEntityID == null) || (idpEntityID.length() == 0)) {
            SAMLUtils.sendError(request, response, response.SC_BAD_REQUEST,
                "nullIDPEntityID",
                SAML2Utils.bundle.getString("nullIDPEntityID"));
            return;
        }

        String binding = DoManageNameID.getMNIBindingInfo(request, metaAlias,
                                        SAML2Constants.SP_ROLE, idpEntityID);
        SAML2MetaManager metaManager= new SAML2MetaManager();
        String hostEntity = metaManager.getEntityByMetaAlias(metaAlias);
        String realm = SAML2MetaUtils.getRealmByMetaAlias(metaAlias);
        if (!SAML2Utils.isSPProfileBindingSupported(
            realm, hostEntity, SAML2Constants.MNI_SERVICE, binding))
        {
            SAMLUtils.sendError(request, response, response.SC_BAD_REQUEST,
                "unsupportedBinding",
                SAML2Utils.bundle.getString("unsupportedBinding"));
            return;
        }

        String requestType = request.getParameter("requestType");

        if ((requestType == null) || (requestType.length() == 0)) {
            SAMLUtils.sendError(request, response, response.SC_BAD_REQUEST,
                "nullRequestType",
                SAML2Utils.bundle.getString("nullRequestType"));
            return;
        }

        String relayState = request.getParameter(SAML2Constants.RELAY_STATE);
        if (!ESAPI.validator().isValidInput("HTTP Query String: " + relayState, relayState, "HTTPQueryString", 2000, true)) {
            relayState = null;
        }
        
        
        if ((relayState == null) || (relayState.isEmpty())) {
            relayState = SAML2Utils.getAttributeValueFromSSOConfig(
                realm, hostEntity, SAML2Constants.SP_ROLE,
                SAML2Constants.DEFAULT_RELAY_STATE);
        } 

        String affiliationID =
            request.getParameter(SAML2Constants.AFFILIATION_ID);

        HashMap paramsMap = new HashMap();
        paramsMap.put("metaAlias", metaAlias);
        paramsMap.put("idpEntityID", idpEntityID);
        paramsMap.put("requestType", requestType);
        paramsMap.put(SAML2Constants.ROLE, SAML2Constants.SP_ROLE);
        paramsMap.put(SAML2Constants.BINDING, binding);

        if (relayState != null && !relayState.isEmpty()) {
            paramsMap.put(SAML2Constants.RELAY_STATE, relayState);
        }

        if (affiliationID != null) {
            paramsMap.put(SAML2Constants.AFFILIATION_ID, affiliationID);
        }

        Object sess = SAML2Utils.checkSession(request,response,
                                          metaAlias, paramsMap);
        if (sess == null) {
            return;
        }

        DoManageNameID.initiateManageNameIDRequest(request,response,
                                          metaAlias, idpEntityID, paramsMap);

        if (binding.equalsIgnoreCase(SAML2Constants.SOAP)) {
            if (relayState != null && !relayState.isEmpty() &&
                    SAML2Utils.isRelayStateURLValid(request, relayState, SAML2Constants.SP_ROLE) &&
                    ESAPI.validator().isValidInput("HTTP URL Value: " + relayState, relayState, "URL", 2000, true)) {
                response.sendRedirect(relayState);
            } else {
                
      out.write("\n");
      out.write("                ");
      if (true) {
        _jspx_page_context.forward("/saml2/jsp/default.jsp?message=mniSuccess");
        return;
      }
      out.write("\n");
      out.write("                ");

            }
        }
    } catch (SAML2Exception e) {
        SAML2Utils.debug.error("Error sending ManageNameIDRequest " , e);
        SAMLUtils.sendError(request, response, response.SC_BAD_REQUEST,
            "requestProcessingMNIError",
            SAML2Utils.bundle.getString("requestProcessingMNIError") + " " +
            e.getMessage());
        return;
    }

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

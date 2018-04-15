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
import com.sun.identity.sae.api.SecureAttrs;
import com.sun.identity.saml.common.SAMLUtils;
import com.sun.identity.saml2.common.SAML2Utils;
import com.sun.identity.saml2.common.SAML2Constants;
import com.sun.identity.saml2.common.SAML2Exception;
import com.sun.identity.saml2.jaxb.entityconfig.SPSSOConfigElement;
import com.sun.identity.saml2.meta.SAML2MetaUtils;
import com.sun.identity.saml2.meta.SAML2MetaManager;
import com.sun.identity.saml2.profile.CacheObject;
import com.sun.identity.saml2.profile.SPCache;
import com.sun.identity.saml2.profile.SPSingleLogout;
import com.sun.identity.saml2.profile.IDPCache;
import com.sun.identity.saml2.protocol.LogoutRequest;
import com.sun.identity.saml2.profile.IDPProxyUtil;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import org.owasp.esapi.ESAPI;
import java.io.PrintWriter;

public final class spSingleLogoutPOST_jsp extends org.apache.jasper.runtime.HttpJspBase
    implements org.apache.jasper.runtime.JspSourceDependent {


boolean processSAELogout(
    HttpServletRequest request, HttpServletResponse response)
{
    String saeData = request.getParameter(SecureAttrs.SAE_PARAM_APPRETURN);
    if (saeData != null) { // App returned back.
        return false;
    }

    try {
        String metaAlias =
            SAML2MetaUtils.getMetaAliasByUri(request.getRequestURI()) ;
        String realm = SAML2MetaUtils.getRealmByMetaAlias(metaAlias);
        SAML2MetaManager mm = SAML2Utils.getSAML2MetaManager();
        String entityId = mm.getEntityByMetaAlias(metaAlias);
        SPSSOConfigElement spConfig = mm.getSPSSOConfig(realm, entityId);
        String appSLOUrlStr = null;
        if (spConfig != null) {
            appSLOUrlStr = SAML2Utils.getAttributeValueFromSPSSOConfig(
                spConfig, SAML2Constants.SAE_SP_LOGOUT_URL);
        }
        if (appSLOUrlStr == null) {
            SAML2Utils.debug.message(
                "spSLOPOST:SAE:appSLOUrl not configured.");
            return false;
        }

        if (SAML2Utils.debug.messageEnabled()) {
            SAML2Utils.debug.message(
                "spSLOPOST:SAE:processing App SLO"+ appSLOUrlStr);
        }
        StringBuffer appSLOUrl = new StringBuffer(appSLOUrlStr);
        Map hp = SAML2Utils.getSAEAttrs(
            realm, entityId, SAML2Constants.SP_ROLE, appSLOUrlStr);
        if (hp == null) {
            SAML2Utils.debug.error(
                "spSLOPOST:SAE:processing App SLO: getSAEAttrs returned null");
            return false;
        }
        String cryptoType = (String) hp.get(SecureAttrs.SAE_CRYPTO_TYPE);
        String secret = null;
        String encSecret = null;
        String encAlg = (String)hp.get(
            SecureAttrs.SAE_CONFIG_DATA_ENCRYPTION_ALG);
        String encStrength = (String)hp.get(
                 SecureAttrs.SAE_CONFIG_ENCRYPTION_KEY_STRENGTH);
        if (SecureAttrs.SAE_CRYPTO_TYPE_SYM.equals(cryptoType)) {
            // Shared secret between FM-IDP and IDPApp
            secret = (String) hp.get(SecureAttrs.SAE_CONFIG_SHARED_SECRET );
            encSecret = secret;
        } else {
            // IDPApp's public key
            secret = (String) hp.get(SecureAttrs.SAE_CONFIG_PRIVATE_KEY_ALIAS);
            encSecret =
                (String) hp.get(SecureAttrs.SAE_CONFIG_PUBLIC_KEY_ALIAS);
        }
        if (secret == null || secret.length() == 0) {
            SAML2Utils.debug.error(
                "spSLOPOST:SAE:processing App SLO:getSAEAttrs no secret/key");
            return false;
        }
        if (encAlg == null) {
            encSecret = null;
        }

        String returnURL = request.getRequestURL()+
                            "?"+request.getQueryString()+"&"+
                             SecureAttrs.SAE_PARAM_APPRETURN+"=true";
        HashMap map = new HashMap();
        map.put(SecureAttrs.SAE_PARAM_CMD, SecureAttrs.SAE_CMD_LOGOUT);
        map.put(SecureAttrs.SAE_PARAM_APPSLORETURNURL, returnURL);
        String saInstanceName = cryptoType + "_" + encAlg + "_" + encStrength;
        SecureAttrs sa = SecureAttrs.getInstance(saInstanceName);
        if (sa == null) {
            Properties prop = new Properties();
            prop.setProperty(SecureAttrs.SAE_CONFIG_CERT_CLASS,
                "com.sun.identity.sae.api.FMCerts");
            if (encAlg != null) {
                prop.setProperty(
                    SecureAttrs.SAE_CONFIG_DATA_ENCRYPTION_ALG, encAlg);
            }

            if (encStrength != null) {
                prop.setProperty(
                    SecureAttrs.SAE_CONFIG_ENCRYPTION_KEY_STRENGTH,encStrength);            }
            SecureAttrs.init(saInstanceName, cryptoType, prop);
            sa = SecureAttrs.getInstance(saInstanceName);
        }

        if (sa == null) {
            SAML2Utils.debug.error(
                "spSLOPOST:SAE:processing App SLO:null SecureAttrs instance");
            return false;
        }
        String encodedString = sa.getEncodedString(map, secret, encSecret);
        if (encodedString != null) {
            if (appSLOUrl.indexOf("?") > 0) {
                appSLOUrl.append("&").append(SecureAttrs.SAE_PARAM_DATA)
                         .append("=").append(encodedString);
            } else {
                appSLOUrl.append("?").append(SecureAttrs.SAE_PARAM_DATA)
                         .append("=").append(encodedString);
            }
            if (SAML2Utils.debug.messageEnabled()) {
                SAML2Utils.debug.message("spSLOPOST:SAE:about to redirect"+
                                       appSLOUrl);
            }
            response.sendRedirect(appSLOUrl.toString());
            return true;
        } else {
           SAML2Utils.debug.error(
               "spSLOPOST:SAE:SecureAttrs.getEncodedStr failed");
        }
    } catch (Exception ex) {
        SAML2Utils.debug.error("spSLOPOST:SAE:SecureAttrs.Fatal:",ex);
    }
    return false;
}

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

    // Retrieves the LogoutRequest or LogoutResponse
    //Retrieves :
    //- RelayState - the target URL on successful Single Logout
    //- SAMLRequest - the LogoutRequest
    //OR
    //- SAMLResponse - the LogoutResponse

    String relayState = request.getParameter(SAML2Constants.RELAY_STATE);
    if (relayState != null) {
        CacheObject tmpRs= 
            (CacheObject) SPCache.relayStateHash.remove(relayState);
        if ((tmpRs != null)) {
            relayState = (String) tmpRs.getObject();
        }
    }
    if (!ESAPI.validator().isValidInput("HTTP Query String: " + relayState, relayState, "HTTPQueryString", 2000, true)) {
        relayState = null;
    }
    String samlResponse = request.getParameter(SAML2Constants.SAML_RESPONSE);
    if (samlResponse != null) {
        try {
        /**
         * Gets and processes the Single <code>LogoutResponse</code> from IDP,
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
          Map infoMap = 
              SPSingleLogout.processLogoutResponse(request,response,
              samlResponse, relayState);
          String inRes = (String) infoMap.get("inResponseTo"); 
          LogoutRequest origLogoutRequest = (LogoutRequest) 
              IDPCache.proxySPLogoutReqCache.get(inRes); 
          if (origLogoutRequest != null && !origLogoutRequest.equals("")) {
              IDPCache.proxySPLogoutReqCache.remove(inRes);
              IDPProxyUtil.sendProxyLogoutResponse(response, request,
                      origLogoutRequest.getID(), infoMap,
                  origLogoutRequest.getIssuer().getValue(),
                  SAML2Constants.HTTP_POST); 
              return;        
          }          
        } catch (SAML2Exception sse) {
            SAML2Utils.debug.error("Error processing LogoutResponse :", sse);
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

        boolean isRelayStateURLValid = false;
        if (!SPCache.isFedlet) {
            isRelayStateURLValid = relayState != null && !relayState.isEmpty()
                    && SAML2Utils.isRelayStateURLValid(request, relayState, SAML2Constants.SP_ROLE)
                    && ESAPI.validator().isValidInput("RelayState", relayState, "URL", 2000, true);
        } else {
            SAML2MetaManager manager = new SAML2MetaManager();
            String metaAlias = null;
            List<String> spMetaAliases = manager.getAllHostedServiceProviderMetaAliases("/");
            if (spMetaAliases != null && !spMetaAliases.isEmpty()) {
                // get first one
                metaAlias = spMetaAliases.get(0);
            }

            isRelayStateURLValid = relayState != null && !relayState.isEmpty()
                    && SAML2Utils.isRelayStateURLValid(metaAlias, relayState, SAML2Constants.SP_ROLE)
                    && ESAPI.validator().isValidInput("RelayState", relayState, "URL", 2000, true);
        }
        if (isRelayStateURLValid) {
            try {
                 response.sendRedirect(relayState);
            } catch (java.io.IOException ioe) {
                if (SAML2Utils.debug.messageEnabled()) {
                    SAML2Utils.debug.message(
                    "Exception when redirecting to " +
                    relayState, ioe);
                }
            }
        } else {
            
      out.write("\n");
      out.write("            ");
      if (true) {
        _jspx_page_context.forward("/saml2/jsp/default.jsp?message=spSloSuccess");
        return;
      }
      out.write("\n");
      out.write("            ");

        } 
    } else {
        String samlRequest = request.getParameter(SAML2Constants.SAML_REQUEST);
        if (samlRequest != null) {
            // Logout SP app via SAE first. App is obligated to redirect back
            // to complete this SLO request.
            if (!SPCache.isFedlet) {
                if (processSAELogout(request, response)) {
                    return;
                }
            }

            try {
            /**
             * Gets and processes the Single <code>LogoutRequest</code> from 
             * IDP.
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
            SPSingleLogout.processLogoutRequest(request,response, new PrintWriter(out, true),
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

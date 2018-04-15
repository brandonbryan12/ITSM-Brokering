<%--
   DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.

   Copyright (c) 2009 Sun Microsystems Inc. All Rights Reserved

   The contents of this file are subject to the terms
   of the Common Development and Distribution License
   (the License). You may not use this file except in
   compliance with the License.

   You can obtain a copy of the License at
   https://opensso.dev.java.net/public/CDDLv1.0.html or
   opensso/legal/CDDLv1.0.txt
   See the License for the specific language governing
   permission and limitations under the License.

   When distributing Covered Code, include this CDDL
   Header Notice in each file and include the License file
   at opensso/legal/CDDLv1.0.txt.
   If applicable, add the following below the CDDL Header,
   with the fields enclosed by brackets [] replaced by
   your own identifying information:
   "Portions Copyrighted [year] [name of copyright owner]"

   $Id: registerconsumer.jsp,v 1.2 2009/12/15 01:28:22 huacui Exp $

   Portions Copyrighted 2015 ForgeRock AS.

--%>


<%@page contentType="text/html" pageEncoding="UTF-8" import="com.sun.identity.shared.debug.Debug,
        java.io.StringWriter, java.io.PrintWriter" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Consumer Registration</title>
    </head>
    <body>
        <h1>Registering Service...</h1>
        <%
            Debug oauthDebug = Debug.getInstance("amAuth");
            String name = request.getParameter("name");
            String cert = request.getParameter("cert");
            String protocol = request.getScheme();
            String host = request.getServerName();
            int port = request.getServerPort();
            String contextRoot = request.getContextPath();
            StringBuffer sb = new StringBuffer();
            sb.append(protocol).append("://").append(host).append(":");
            sb.append(port).append(contextRoot);
            sb.append("/resources/1/oauth/consumer_registration");
            String regurl = sb.toString();
            try {
                java.net.URL url = new java.net.URL(regurl);
                java.net.URLConnection conn = url.openConnection();
                conn.setDoInput(true);
                conn.setDoOutput(true);
                conn.setUseCaches(false);
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                java.io.DataOutputStream dos = new java.io.DataOutputStream(conn.getOutputStream());
                String postmsg = "name=" + java.net.URLEncoder.encode(name);
                if (cert != null) {
                    postmsg += "&certificate=" + java.net.URLEncoder.encode(cert);
                }
                dos.writeBytes(postmsg);
                dos.flush();
                dos.close();
                java.io.BufferedReader reader = new java.io.BufferedReader(
                        new java.io.InputStreamReader(
                        (java.io.InputStream) conn.getContent()));
                out.println("<h2> Service Consumer registered.</h2>");
                String line;
                StringBuilder buf = new StringBuilder();
                buf.append("<div>");
                while ((line = reader.readLine()) != null) {
                    buf.append(line);
                }
                buf.append("</div>");
                java.util.StringTokenizer tokenizer = new java.util.StringTokenizer(buf.toString(), "&");
                while (tokenizer.hasMoreTokens()) {
                    String token = tokenizer.nextToken();
                    out.println(java.net.URLDecoder.decode(token) + "<br>");
                }
            } catch (Exception ex) {
                if (oauthDebug.errorEnabled()) {
                    oauthDebug.error("Unexpected error occurred while registering consumer", ex);
                }
                out.println("<div> Exception thrown during processing, see Authentication Debug Log for details.<br>" +
                        "Please contact your system administrator. </div>");
            }
        %>
        <hr><br>
        <form name="return_ind" action="index.jsp" method="GET">
            Return to Main Menu <input type="submit" value="Return" name="return_ind" /><br>
        </form>
    </body>
</html>

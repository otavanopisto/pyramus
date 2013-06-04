<%@ page contentType="text/html; charset=utf-8" %>

<%-- Expects to receive help text in "text" parameter --%>

<%-- has help text --%>
<%
if ((request.getParameter("text") != null) && (request.getParameter("text").length() > 0)) 
{
%>
<div style="background-image: url(birt/images/contexthelpicon.png);" class="birtviewer_dialog_contexthelp_container">
  <div class="birtviewer_dialog_contexthelp_text">
    <%= request.getParameter("text") %>
   </div>
  </div>
<%
}
%>
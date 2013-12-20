<%-----------------------------------------------------------------------------
	Copyright (c) 2004 Actuate Corporation and others.
	All rights reserved. This program and the accompanying materials 
	are made available under the terms of the Eclipse Public License v1.0
	which accompanies this distribution, and is available at
	http://www.eclipse.org/legal/epl-v10.html
	
	Contributors:
		Actuate Corporation - Initial implementation.
-----------------------------------------------------------------------------%>
<%@ page contentType="text/html; charset=utf-8" %>
<%@ page session="false" buffer="none" %>
<%@ page import="org.eclipse.birt.report.utility.ParameterAccessor,
				 org.eclipse.birt.report.context.BaseAttributeBean,
				 org.eclipse.birt.report.context.ScalarParameterBean" %>

<%-----------------------------------------------------------------------------
	Expected java beans
-----------------------------------------------------------------------------%>
<jsp:useBean id="attributeBean" type="org.eclipse.birt.report.context.BaseAttributeBean" scope="request" />

<%-----------------------------------------------------------------------------
	Text box parameter control
-----------------------------------------------------------------------------%>
<%
	ScalarParameterBean parameterBean = ( ScalarParameterBean ) attributeBean.getParameterBean( );
	String encodedParameterName = ParameterAccessor.htmlEncode( parameterBean.getName( ) );
%>
<TR>
	<%-- 
	  28.07.2010 Antti Leppä
	  
	  Changed context help to behave as it does in other views of Pyramus   
	--%>
	<TD nowrap="nowrap">
		<IMG SRC="birt/images/parameter.gif" ALT="<%= parameterBean.getDisplayName( ) %>"/>
	</TD>
	<TD nowrap="nowrap">
		<FONT><LABEL FOR="<%= encodedParameterName %>"><%= parameterBean.getDisplayName( ) %>:</LABEL></FONT>
		
		<jsp:include page="ParameterContextHelpFragment.jsp">
		  <jsp:param name="text" value="<%= parameterBean.getToolTip( ) %>"/>
		</jsp:include>
		
		<%-- is required --%>
		<%
		if ( parameterBean.isRequired( ) )
		{
		%>
			<FONT COLOR="red"><LABEL FOR="<%= encodedParameterName %>">*</LABEL></FONT>
		<%
		}
		%>
	</TD>
</TR>
<TR>
	<TD nowrap="nowrap"></TD>
	<TD nowrap="nowrap" WIDTH="100%">
	<INPUT TYPE="HIDDEN" ID="control_type" VALUE="text"/>
	<INPUT TYPE="HIDDEN" ID="data_type" VALUE="<%="" + parameterBean.getParameter( ).getDataType( ) %>"/>
<%
	if ( !parameterBean.isRequired( ) )
	{
%>	
		<LABEL FOR="<%= encodedParameterName + "_radio_notnull" %>" CLASS="birtviewer_hidden_label">Input text</LABEL>	
		<INPUT TYPE="RADIO"
			ID="<%= encodedParameterName + "_radio_notnull" %>"
			VALUE="<%= encodedParameterName %>"
			<%= (parameterBean.getValue( ) != null)? "CHECKED" : "" %>/>
<%
	}
%>	
		<INPUT CLASS="BirtViewer_parameter_dialog_Input"
			TYPE="<%= parameterBean.isValueConcealed( )? "PASSWORD" : "TEXT" %>"
			NAME="<%= encodedParameterName %>"
			ID="<%= encodedParameterName %>" 
			TITLE="<%= parameterBean.getToolTip( ) %>"
			VALUE="<%= ParameterAccessor.htmlEncode( ( parameterBean.getDisplayText( ) == null )? "" : parameterBean.getDisplayText( ) ) %>" 
			<%= ( !parameterBean.isRequired( ) && parameterBean.getValue( ) == null )? "DISABLED='true'" : "" %>
			<%= parameterBean.isRequired( ) ? "aria-required='true'" : "" %>
      <%= parameterBean.getParameter().getDataType() == 7 ? "ix:datefield='true'" : "" %>
            />

		<INPUT TYPE="HIDDEN"
			ID="<%= encodedParameterName + "_value" %>"
			VALUE="<%= ParameterAccessor.htmlEncode( ( parameterBean.getValue( ) == null )? "" : parameterBean.getValue( ) ) %>"
			/>
		
		<INPUT TYPE="HIDDEN"
			ID="<%= encodedParameterName + "_displayText" %>"
			VALUE="<%= ParameterAccessor.htmlEncode( ( parameterBean.getDisplayText( ) == null )? "" : parameterBean.getDisplayText( ) ) %>"
			/>
<%
	if ( !parameterBean.isRequired( ) )
	{
%>
		<BR>
		<LABEL FOR="<%= encodedParameterName + "_radio_null" %>" CLASS="birtviewer_hidden_label">Null Value</LABEL>	
		<INPUT TYPE="RADIO"
			ID="<%= encodedParameterName + "_radio_null"%>"
			VALUE="<%= encodedParameterName %>"
			<%= ( parameterBean.getValue( ) == null )? "CHECKED" : "" %>/> Null Value
<%
	}
%>

		<INPUT TYPE="HIDDEN" ID="isRequired" 
			VALUE = "<%= parameterBean.isRequired( )? "true": "false" %>"/>
	</TD>
</TR>
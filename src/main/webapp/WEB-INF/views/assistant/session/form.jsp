<%--
- form.jsp
-
- Copyright (C) 2012-2023 Rafael Corchuelo.
-
- In keeping with the traditional purpose of furthering education and research, it is
- the policy of the copyright owner to permit non-commercial use and redistribution of
- this software. It has been tested carefully, but it is not guaranteed for any particular
- purposes.  The copyright owner does not offer any warranties or representations, nor do
- they accept any liabilities with respect to them.
--%>

<%@page language="java"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://www.the-acme-framework.org/"%>

<acme:form> 
	<acme:input-textbox code="assistant.session.form.label.title" path="title"/>
	<acme:input-textbox code="assistant.session.form.label.resume" path="resume"/>	
	<acme:input-select code="assistant.session.form.label.sessionType" path="sessionType" choices="${sessionTypes}"/>
	<acme:input-moment  code="assistant.session.form.label.periodStart" path="periodStart"/>
	<acme:input-moment  code="assistant.session.form.label.periodEnd" path="periodEnd"/>
	<acme:input-textbox code="assistant.session.form.label.information" path="information"/>
	<jstl:choose>
		<jstl:when test="${acme:anyOf(_command, 'show|update|delete')}">
			<acme:submit code="assistant.session.form.button.update" action="/assistant/session/update?masterId=${id}"/>
			<acme:submit code="assistant.session.form.button.delete" action="/assistant/session/delete"/>
		</jstl:when>
		<jstl:when test="${_command == 'create'}">
			<acme:submit code="assistant.session.form.button.create" action="/assistant/session/create?masterId=${masterId}"/>
		</jstl:when>		
	</jstl:choose>	
</acme:form>
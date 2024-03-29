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
	<acme:input-textbox code="assistant.tutorial.list.label.code" path="code"/>
	<acme:input-textbox code="assistant.tutorial.list.label.title" path="title"/>	
	<acme:input-textbox code="assistant.tutorial.list.label.goals" path="goals"/>
	<acme:input-textbox code="assistant.tutorial.list.label.resume" path="resume"/>
	<acme:input-double code="assistant.tutorial.form.label.estimatedTotalTime" path="estimatedTotalTime"/>
	<acme:input-select code="assistant.tutorial.list.label.course-title"
		path="course" choices="${courses}" />
	<jstl:if test="${_command == 'show'}">
		<acme:input-integer code="assistant.tutorial.form.label.numSessions" path="numSessions" readonly="true"/>
	</jstl:if>
	<jstl:choose>
		<jstl:when test="${acme:anyOf(_command, 'show|update|delete|publish') && isPublished == false}">
			<acme:submit code="assistant.tutorial.form.button.update" action="/assistant/tutorial/update"/>
			<acme:submit code="assistant.tutorial.form.button.delete" action="/assistant/tutorial/delete"/>
			<acme:submit code="assistant.tutorial.form.button.publish" action="/assistant/tutorial/publish"/>
		</jstl:when>
		<jstl:when test="${acme:anyOf(_command, 'show|update|delete|publish') && isPublished == true}">
			<acme:button code="assistant.tutorial.form.button.session" action="/assistant/session/list-tutorial?masterId=${id}"/>
		</jstl:when>
		<jstl:when test="${_command == 'create'}">
			<acme:submit code="assistant.tutorial.form.button.create" action="/assistant/tutorial/create"/>
		</jstl:when>		
	</jstl:choose>	
</acme:form>
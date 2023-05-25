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
	<acme:input-textbox code="student.activity.form.label.title" path="title"/>
	<acme:input-textarea code="student.activity.form.label.abstract$" path="abstract$"/>
	<acme:input-textbox code="student.activity.form.label.course-type" path="type"/>
	<acme:input-money code="student.activity.form.label.timePeriod" path="timePeriod"/>
	<acme:input-url code="student.activity.form.label.further-information" path="furtherInformation"/>
	
	<jstl:choose>	 
		<jstl:when test="${acme:anyOf(_command, 'show|update|delete') }">
			<acme:submit code="student.activity.form.button.update" action="/student/activity/update"/>
			<acme:submit code="student.activity.form.button.delete" action="/student/activity/delete"/>

		</jstl:when>
		<jstl:when test="${_command == 'create'}">
			<acme:submit code="student.activity.form.button.create" action="/student/activity/create"/>
		</jstl:when>		
	</jstl:choose>
</acme:form>
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
	<acme:input-textbox code="student.enrolment.form.label.code" path="code"/>
	<acme:input-textbox code="student.enrolment.form.label.motivation" path="motivation"/>
	<acme:input-textbox code="student.enrolment.form.label.goals" path="goals"/>
	<acme:input-double code="student.enrolment.form.label.workTime" path="workTime"/>
	
	<jstl:if test="${_command == 'finalise'}">
				<acme:input-textbox code="student.enrolment.form.label.creditCard" path="creditCard"/>
				<acme:input-textbox code="student.enrolment.form.label.holder" path="holder"/>
	</jstl:if>
	<jstl:choose>
			 
		<jstl:when test="${acme:anyOf(_command, 'show|update|delete')}">
			<acme:submit code="student.enrolment.form.button.update" action="/student/enrolment/update"/>
			<acme:submit code="student.enrolment.form.button.delete" action="/student/enrolment/delete"/>
			<acme:button code="student.enrolment.form.button.finalise" action="/student/enrolment/finalise?id=${id}"/>
			
			<acme:button code="student.activity.form.button.create" action="/student/activity/create?id=${id}"/>
			
		</jstl:when>
		<jstl:when test="${_command == 'register'}">
			<acme:submit code="student.enrolment.form.button.register" action="/student/enrolment/register"/>
		</jstl:when>
		<jstl:when test="${_command == 'finalise'}">
			<acme:submit code="student.enrolment.form.button.finalise" action="/student/enrolment/finalise"/>
		</jstl:when>
				
	</jstl:choose>

</acme:form>
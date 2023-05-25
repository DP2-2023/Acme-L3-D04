
package acme.forms;

import acme.framework.data.AbstractForm;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StudentDashboard extends AbstractForm {

	protected static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	Integer						totalNumberOfTheoryWorkbook;
	Integer						totalNumberOfHandsOnWorkbook;
	Double						averagePeriodOfTheActivitiesPerWorkbook;
	Double						minimumPeriodOfTheActivitiesPerWorkbook;
	Double						maximumPeriodOfTheActivitiesPerHerWorkbook;
	Double						deviationOfPeriodOfTheActivitiesPerWorkbook;
	Double						averageLearningTimePerCourse;
	Double						deviationLearningTimePerCourse;
	Double						minimumLearningTimePerCourse;
	Double						maximumLearningTimePerCourse;

}

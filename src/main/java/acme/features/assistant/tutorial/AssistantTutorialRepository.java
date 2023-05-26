
package acme.features.assistant.tutorial;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.entities.courses.Course;
import acme.entities.sessions.Session;
import acme.entities.tutorial.Tutorial;
import acme.framework.repositories.AbstractRepository;
import acme.roles.Assistant;

@Repository
public interface AssistantTutorialRepository extends AbstractRepository {

	@Query("SELECT t FROM Tutorial t WHERE t.assistant.id = :id")
	Collection<Tutorial> findTutorialByAssistantId(int id);

	@Query("select t from Tutorial t where t.id = :id")
	Tutorial findOneTutorialById(int id);

	@Query("select t.course from Tutorial t where t.assistant.id = :assistantId")
	Collection<Course> findManyCoursesByAssistantId(int assistantId);

	@Query("select c from Course c")
	Collection<Course> findAllCourses();

	@Query("select a from Assistant a where a.id = :id")
	Assistant findOneAssistantById(int id);

	@Query("select c from Course c where c.id = :courseId")
	Course findOneCourseById(int courseId);

	@Query("select s from Session s where s.tutorial.id = :tutorialId")
	Collection<Session> findManySessionsByTutorialId(int tutorialId);

	@Query("select c.value from Config c where c.configKey = :key")
	String findOneConfigByKey(String key);

}

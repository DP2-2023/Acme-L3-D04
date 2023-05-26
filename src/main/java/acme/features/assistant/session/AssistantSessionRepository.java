
package acme.features.assistant.session;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.entities.sessions.Session;
import acme.entities.tutorial.Tutorial;
import acme.framework.repositories.AbstractRepository;

@Repository
public interface AssistantSessionRepository extends AbstractRepository {

	@Query("select s from Session s where s.tutorial.assistant.id = :id")
	Collection<Session> findSessionsByAssistantId(int id);

	@Query("select s from Session s where s.id = :id")
	Session findOneSessionById(int id);

	@Query("select t from Tutorial t where t.id = :id")
	Tutorial findOneTutorialById(int id);

	@Query("select s from Session s where s.tutorial.id = :masterId")
	Collection<Session> findManySessionsByMasterId(int masterId);

	@Query("select s.tutorial from Session s where s.id = :id")
	Tutorial findOneTutorialBySessionId(int id);

	@Query("select c.value from Config c where c.configKey = :key")
	String findOneConfigByKey(String key);

}

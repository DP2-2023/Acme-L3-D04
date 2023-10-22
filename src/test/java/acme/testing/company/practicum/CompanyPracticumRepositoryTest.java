
package acme.testing.company.practicum;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;

import acme.entities.practicums.Practicum;
import acme.framework.repositories.AbstractRepository;

public interface CompanyPracticumRepositoryTest extends AbstractRepository {

	@Query("select t from Practicum t where t.company.userAccount.username = :username")
	Collection<Practicum> findManyPracticumsByCompanyUsername(String username);
}

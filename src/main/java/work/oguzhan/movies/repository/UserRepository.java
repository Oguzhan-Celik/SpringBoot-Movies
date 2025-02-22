package work.oguzhan.movies.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import org.springframework.stereotype.Repository;

import work.oguzhan.movies.model.User;

@Repository
public interface UserRepository extends MongoRepository<User, String> {
	Optional<User> findByEmail(String email);
}
package tw.niq.example.spring.rest.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import tw.niq.example.spring.rest.entity.UserEntity;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

	Optional<UserEntity> findByUserId(String userId);
	
	Optional<UserEntity> findByEmail(String email);
	
}

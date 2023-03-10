package tw.niq.example.spring.rest.bootstrap;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import tw.niq.example.spring.rest.entity.AuthorityEntity;
import tw.niq.example.spring.rest.entity.RoleEntity;
import tw.niq.example.spring.rest.entity.UserEntity;
import tw.niq.example.spring.rest.repository.AuthorityRepository;
import tw.niq.example.spring.rest.repository.RoleRepository;
import tw.niq.example.spring.rest.repository.UserRepository;

@Component
public class DataLoader implements CommandLineRunner {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private RoleRepository roleRepository;
	
	@Autowired
	private AuthorityRepository authorityRepository;
	
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@SuppressWarnings("unused")
	@Transactional
	@Override
	public void run(String... args) throws Exception {
		
		AuthorityEntity authorityRead = authorityRepository
				.findByName("AUTHORITY_READ")
				.orElseGet(() -> authorityRepository.save(new AuthorityEntity("AUTHORITY_READ")));

		AuthorityEntity authorityWrite = authorityRepository
				.findByName("AUTHORITY_WRITE")
				.orElseGet(() -> authorityRepository.save(new AuthorityEntity("AUTHORITY_WRITE")));
		
		AuthorityEntity authorityDelete = authorityRepository
				.findByName("AUTHORITY_DELETE")
				.orElseGet(() -> authorityRepository.save(new AuthorityEntity("AUTHORITY_DELETE")));
		
		RoleEntity roleAdmin = roleRepository
				.findByName("ROLE_ADMIN")
				.orElseGet(() -> roleRepository.save(new RoleEntity("ROLE_ADMIN", Set.of(authorityRead, authorityWrite, authorityDelete))));
		
		RoleEntity roleUser = roleRepository
				.findByName("ROLE_USER")
				.orElseGet(() -> roleRepository.save(new RoleEntity("ROLE_USER", Set.of(authorityRead, authorityWrite))));
		
		UserEntity userAdmin = userRepository
				.findByEmail("admin@example.com")
				.orElseGet(() -> userRepository.save(new UserEntity("admin", "admin@example.com", bCryptPasswordEncoder.encode("12345678"), Set.of(roleAdmin, roleUser))));
	}

}

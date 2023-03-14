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

	@Transactional
	@Override
	public void run(String... args) throws Exception {
		
		AuthorityEntity authorityUserRead = authorityRepository
				.findByAuthorityName("user.read")
				.orElseGet(() -> authorityRepository.save(new AuthorityEntity("user.read")));

		AuthorityEntity authorityUserWrite = authorityRepository
				.findByAuthorityName("user.write")
				.orElseGet(() -> authorityRepository.save(new AuthorityEntity("user.write")));
		
		AuthorityEntity authorityUserDelete = authorityRepository
				.findByAuthorityName("user.delete")
				.orElseGet(() -> authorityRepository.save(new AuthorityEntity("user.delete")));
		
		RoleEntity roleAdmin = roleRepository
				.findByRoleName("ADMIN")
				.orElseGet(() -> roleRepository.save(new RoleEntity("ADMIN", Set.of(authorityUserRead, authorityUserWrite, authorityUserDelete))));
		
		RoleEntity roleUser = roleRepository
				.findByRoleName("USER")
				.orElseGet(() -> roleRepository.save(new RoleEntity("USER", Set.of(authorityUserRead, authorityUserWrite))));
		
		userRepository
				.findByEmail("admin@example.com")
				.orElseGet(() -> userRepository.save(new UserEntity("admin", "admin@example.com", bCryptPasswordEncoder.encode("12345678"), Set.of(roleAdmin, roleUser))));
	}

}

package tw.niq.example.spring.rest.security.perms;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import org.springframework.security.access.prepost.PostAuthorize;

@Retention(RetentionPolicy.RUNTIME)
@PostAuthorize("hasRole('ADMIN') or returnObject.userId == principal.userId")
public @interface SelfOnly {

}

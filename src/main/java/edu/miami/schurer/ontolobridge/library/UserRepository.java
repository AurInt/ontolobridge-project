package edu.miami.schurer.ontolobridge.library;

import edu.miami.schurer.ontolobridge.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    Boolean existsByEmail(String email);

    
    <S extends User> S save(S s);
}

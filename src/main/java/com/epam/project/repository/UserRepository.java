package com.epam.project.repository;

import com.epam.project.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

//Repozytorium do rejestrowania użytkowników DB
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    //Głównym identyfikatorem będzie e-mail Opcjonalny zwróci null lub obiekt
    Optional<UserEntity> findByEmail(String email);
}
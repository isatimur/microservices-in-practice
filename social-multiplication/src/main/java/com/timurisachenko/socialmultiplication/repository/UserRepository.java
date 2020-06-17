package com.timurisachenko.socialmultiplication.repository;

import com.timurisachenko.socialmultiplication.domain.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<User, Long>{
    Optional<User> findByAlias(final String alias);
}

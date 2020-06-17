package com.timurisachenko.socialmultiplication.repository;

import com.timurisachenko.socialmultiplication.domain.Multiplication;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MultiplicationRepository extends CrudRepository<Multiplication, Long> {
}

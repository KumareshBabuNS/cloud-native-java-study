package com.example.domain.repository;

import com.example.domain.Baby;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * @author Kj Nam
 * @since 2017-01-02
 */
@RepositoryRestResource
public interface BabyRepository extends JpaRepository<Baby, Long> {
}

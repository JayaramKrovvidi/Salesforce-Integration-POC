package com.integration.poc.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface ICustomJPARepository<A, B>
    extends JpaRepository<A, B>, JpaSpecificationExecutor<A> {

}

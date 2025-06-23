package com.mass_branches.repository;

import com.mass_branches.model.CustomerType;
import com.mass_branches.model.CustomerTypeName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerTypeRepository extends JpaRepository<CustomerType, Long> {
    Optional<CustomerType> findByName(CustomerTypeName name);
}

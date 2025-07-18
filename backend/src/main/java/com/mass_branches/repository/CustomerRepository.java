package com.mass_branches.repository;

import com.mass_branches.model.Customer;
import com.mass_branches.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, String> {
    List<Customer> findAllByUserAndActiveIsTrue(User user);

    Optional<Customer> findByUserAndActiveIsTrueAndId(User user, String id);

    Integer countCustomersByUser(User user);
}

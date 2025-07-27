package com.mass_branches.repository;

import com.mass_branches.model.Customer;
import com.mass_branches.model.CustomerTypeName;
import com.mass_branches.model.User;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, String> {
    List<Customer> findAllByUserAndActiveIsTrue(User user);

    Optional<Customer> findByUserAndActiveIsTrueAndId(User user, String id);

    Integer countCustomersByUser(User user);

    List<Customer> findAllByNameContainingAndType_Name(String name, CustomerTypeName typeName, Sort sort);

    List<Customer> findAllByType_Name(CustomerTypeName typeName, Sort sort);

    List<Customer> findAllByNameContaining(String name, Sort sort);

    List<Customer> findAllByUserAndActiveIsTrueAndNameContainingAndType_Name(User user, String name, CustomerTypeName typeName, Sort sort);

    List<Customer> findAllByUserAndActiveIsTrueAndType_Name(User user, CustomerTypeName typeName, Sort sort);

    List<Customer> findAllByUserAndActiveIsTrueAndNameContaining(User user, String name, Sort sort);

    Optional<Customer> findByIdAndActiveIsTrue(String id);
}

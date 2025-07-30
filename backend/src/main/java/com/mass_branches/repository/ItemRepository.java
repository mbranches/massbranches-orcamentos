package com.mass_branches.repository;

import com.mass_branches.model.Item;
import com.mass_branches.model.User;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {
    List<Item> findAllByUserAndActiveIsTrue(User user);

    Optional<Item> findByIdAndUserAndActiveIsTrue(Long id, User user);

    List<Item> findAllByNameContaining(String name, Sort sort);

    List<Item> findAllByUserAndNameContainingAndActiveIsTrue(User user, String name, Sort sort);

    List<Item> findAllByUserAndActiveIsTrue(User user, Sort sort);

    Optional<Item> findByIdAndActiveIsTrue(Long id);
}

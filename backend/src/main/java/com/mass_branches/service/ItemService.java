package com.mass_branches.service;

import com.mass_branches.dto.request.ItemPostRequest;
import com.mass_branches.dto.request.ItemPutRequest;
import com.mass_branches.dto.response.ItemGetResponse;
import com.mass_branches.dto.response.ItemPostResponse;
import com.mass_branches.exception.BadRequestException;
import com.mass_branches.exception.NotFoundException;
import com.mass_branches.model.Item;
import com.mass_branches.model.User;
import com.mass_branches.repository.BudgetItemRepository;
import com.mass_branches.repository.ItemRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ItemService {
    private final ItemRepository repository;
    private final BudgetItemRepository budgetItemRepository;

    public ItemPostResponse create(User user, ItemPostRequest postRequest) {
        Item itemToSave = Item.builder()
                .name(postRequest.name())
                .unitMeasurement(postRequest.unitMeasurement())
                .unitPrice(postRequest.unitPrice())
                .active(true)
                .user(user)
                .build();

        Item savedItem = repository.save(itemToSave);

        return ItemPostResponse.by(savedItem);
    }

    public List<ItemGetResponse> listAll(Optional<String> name) {
        Sort sort = Sort.by("name").ascending();
        boolean fetchByName = name.isPresent();

        List<Item> items = fetchByName ? repository.findAllByNameContaining(name.get(), sort)
                : repository.findAll(sort);

        return items.stream()
                .map(item -> {
                    long numberOfUses = budgetItemRepository.countBudgetItemByItem(item);
                    return ItemGetResponse.by(item, numberOfUses);
                })
                .toList();
    }

    public List<ItemGetResponse> listAllMy(User user, Optional<String> name) {
        Sort sort = Sort.by("name").ascending();
        boolean fetchByName = name.isPresent();

        List<Item> items = fetchByName ? repository.findAllByUserAndNameContainingAndActiveIsTrue(user, name.get(), sort)
                : repository.findAllByUserAndActiveIsTrue(user, sort);

        return items.stream()
                .map(item -> {
                    long numberOfUses = budgetItemRepository.countBudgetItemByItem(item);
                    return ItemGetResponse.by(item, numberOfUses);
                })
                .toList();
    }

    public ItemGetResponse findById(User user, Long id) {
        Item item = user.isAdmin() ? findByIdOrThrowsNotFoundException(id)
                : findByIdAndUserAndActiveIsTrueOrThrowsNotFoundException(id, user);

        long numberOfUses = budgetItemRepository.countBudgetItemByItem(item);

        return ItemGetResponse.by(item, numberOfUses);
    }

    public Item findByIdOrThrowsNotFoundException(Long id){
        return repository.findById(id)
                .orElseThrow(() -> newItemIdNotFoundException(id));
    }

    public Item findByIdAndActiveIsTrueOrThrowsNotFoundException(Long id){
        return repository.findByIdAndActiveIsTrue(id)
                .orElseThrow(() -> newItemIdNotFoundException(id));
    }

    public Item findByIdAndUserAndActiveIsTrueOrThrowsNotFoundException(Long id, User user){
        return repository.findByIdAndUserAndActiveIsTrue(id, user)
                .orElseThrow(() -> newItemIdNotFoundException(id));
    }

    public NotFoundException newItemIdNotFoundException(Long id) {
        return new NotFoundException("Item with id '%s' not found".formatted(id))   ;
    }

    public void update(User user, Long id, ItemPutRequest request) {
        if (!id.equals(request.id()))
            throw new BadRequestException("The url id (%s) is different from the request body id(%s)".formatted(id, request.id()));

        Item item = user.isAdmin() ? findByIdOrThrowsNotFoundException(id) : findByIdAndUserAndActiveIsTrueOrThrowsNotFoundException(id, user);

        item.setName(request.name());
        item.setUnitMeasurement(request.unitMeasurement());
        item.setUnitPrice(request.unitPrice());

        repository.save(item);
    }

    public void delete(User user, Long id) {
        Item item = user.isAdmin() ? findByIdAndActiveIsTrueOrThrowsNotFoundException(id)
                : findByIdAndUserAndActiveIsTrueOrThrowsNotFoundException(id, user);

        item.setActive(false);

        repository.save(item);
    }
}

package com.mass_branches.service;

import com.mass_branches.dto.request.ItemPostRequest;
import com.mass_branches.dto.response.ItemGetResponse;
import com.mass_branches.dto.response.ItemPostResponse;
import com.mass_branches.exception.NotFoundException;
import com.mass_branches.model.Item;
import com.mass_branches.model.User;
import com.mass_branches.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ItemService {
    private final ItemRepository repository;

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
                .map(ItemGetResponse::by)
                .toList();
    }

    public List<ItemGetResponse> listAllMy(User user, Optional<String> name) {
        Sort sort = Sort.by("name").ascending();
        boolean fetchByName = name.isPresent();

        List<Item> items = fetchByName ? repository.findAllByUserAndNameContainingAndActiveIsTrue(user, name.get(), sort)
                : repository.findAllByUserAndActiveIsTrue(user, sort);

        return items.stream()
                .map(ItemGetResponse::by)
                .toList();
    }

    public ItemGetResponse findById(User user, Long id) {
        Item item = user.isAdmin() ? findByIdOrThrowsNotFoundException(id)
                : findByIdAndUserAndActiveIsTrueOrThrowsNotFoundException(id, user);

        return ItemGetResponse.by(item);
    }

    public Item findByIdOrThrowsNotFoundException(Long id){
        return repository.findById(id)
                .orElseThrow(() -> newItemIdNotFoundException(id));
    }

    public Item findByIdAndUserAndActiveIsTrueOrThrowsNotFoundException(Long id, User user){
        return repository.findByIdAndUserAndActiveIsTrue(id, user)
                .orElseThrow(() -> newItemIdNotFoundException(id));
    }

    public NotFoundException newItemIdNotFoundException(Long id) {
        return new NotFoundException("Item with id '%s' not found".formatted(id))   ;
    }
}

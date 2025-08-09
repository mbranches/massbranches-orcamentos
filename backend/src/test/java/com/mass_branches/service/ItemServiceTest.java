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
import com.mass_branches.utils.ItemUtils;
import com.mass_branches.utils.UserUtils;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ExtendWith(MockitoExtension.class)
class ItemServiceTest {
    @InjectMocks
    private ItemService service;
    @Mock
    private ItemRepository repository;
    @Mock
    private BudgetItemRepository budgetItemRepository;
    private List<Item> itemList;
    private List<ItemGetResponse> itemGetResponseList;
    private final Sort SORT_BY_NAME = Sort.by("name").ascending();
    private final long NUMBER_OF_USES_DEFAULT = 1L;

    @BeforeEach
    void init() {
        itemList = ItemUtils.newItemList();
        itemGetResponseList = ItemUtils.newItemGetResponseList();
    }

    @Test
    @DisplayName("create returns created item when successful")
    @Order(1)
    void create_ReturnsCreatedItem_WhenSuccessful() {
        User user = UserUtils.newUserList().getFirst();

        ItemPostRequest postRequest = ItemUtils.newItemPostRequest();
        ItemPostResponse expectedResponse = ItemUtils.newItemPostResponse();

        Item itemToSave = ItemUtils.newItemToSave();
        Item savedItem = ItemUtils.newItemSaved();

        when(repository.save(itemToSave)).thenReturn(savedItem);

        ItemPostResponse response = service.create(user, postRequest);

        assertThat(response)
                .isNotNull()
                .isEqualTo(expectedResponse);
    }

    @Test
    @DisplayName("findById returns found item of the given user when the user is client")
    @Order(2)
    void findById_ReturnsFoundItemOfTheGivenUser_WhenUserIsClient() {
        User clientUser = UserUtils.newUserList().getLast();

        Item itemToBeFound = itemList.getLast();
        Long idToSearch = itemToBeFound.getId();

        ItemGetResponse expectedResponse = itemGetResponseList.getLast();

        when(repository.findByIdAndUserAndActiveIsTrue(idToSearch, clientUser)).thenReturn(Optional.of(itemToBeFound));
        when(budgetItemRepository.countBudgetItemByItem(itemToBeFound)).thenReturn(expectedResponse.numberOfUses());

        ItemGetResponse response = service.findById(clientUser, idToSearch);

        assertThat(response)
                .isNotNull()
                .isEqualTo(expectedResponse);
    }

    @Test
    @DisplayName("findById returns found item when the given user is admin")
    @Order(3)
    void findById_ReturnsFoundItem_WhenTheGivenUserIsAdmin() {
        User adminUser = UserUtils.newUserList().getFirst();

        Item itemToBeFound = itemList.getLast();
        Long idToSearch = itemToBeFound.getId();

        ItemGetResponse expectedResponse = itemGetResponseList.getLast();

        when(repository.findById(idToSearch)).thenReturn(Optional.of(itemToBeFound));
        when(budgetItemRepository.countBudgetItemByItem(itemToBeFound)).thenReturn(expectedResponse.numberOfUses());

        ItemGetResponse response = service.findById(adminUser, idToSearch);

        assertThat(response)
                .isNotNull()
                .isEqualTo(expectedResponse);
    }

    @Test
    @DisplayName("findById throws NotFoundException when the given id is not found")
    @Order(4)
    void findById_ThrowsNotFoundException_WhenTheGivenIdIsNotFound() {
        User clientUser = UserUtils.newUserList().get(1);

        Long randomId = 999L;

        when(repository.findByIdAndUserAndActiveIsTrue(randomId, clientUser)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.findById(clientUser, randomId))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Item with id '%s' not found".formatted(randomId));
    }

    @Test
    @DisplayName("findById throws NotFoundException when the given id is not found and user is admin")
    @Order(5)
    void findById_ThrowsNotFoundException_WhenTheGivenIdIsNotFoundAndUserIsAdmin() {
        User adminUser = UserUtils.newUserList().getFirst();

        Long randomId = 999L;

        when(repository.findById(randomId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.findById(adminUser, randomId))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Item with id '%s' not found".formatted(randomId));
    }

    @Test
    @DisplayName("listAllMy returns found items of the given user when successful")
    @Order(6)
    void listAllMy_ReturnsFoundItemsOfTheGivenUser_WhenSuccessful() {
        User user = UserUtils.newUserList().getFirst();

        List<Item> itemsOfTheUser = itemList.stream()
                .filter(item -> item.getUser().equals(user))
                .toList();

        List<ItemGetResponse> expectedResponse = itemsOfTheUser.stream()
                .map(item -> ItemGetResponse.by(item, NUMBER_OF_USES_DEFAULT))
                .toList();

        when(repository.findAllByUserAndActiveIsTrue(user, SORT_BY_NAME))
                .thenReturn(itemsOfTheUser);
        when(budgetItemRepository.countBudgetItemByItem(any()))
                .thenReturn(NUMBER_OF_USES_DEFAULT);

        List<ItemGetResponse> response = service.listAllMy(user, Optional.empty());

        assertThat(response)
                .isNotNull()
                .isNotEmpty()
                .containsExactlyElementsOf(expectedResponse);
    }

    @Test
    @DisplayName("listAllMy returns an empty list when the given user does not have items")
    @Order(7)
    void listAllMy_ReturnsEmpty_WhenTheGivenUserDoesNotHaveItems() {
        User user = UserUtils.newUserList().getFirst();

        when(repository.findAllByUserAndActiveIsTrue(user, SORT_BY_NAME))
                .thenReturn(Collections.emptyList());

        List<ItemGetResponse> response = service.listAllMy(user, Optional.empty());

        assertThat(response)
                .isNotNull()
                .isEmpty();
    }

    @Test
    @DisplayName("listAllMy returns found items of the given user when name given is found")
    @Order(8)
    void listAllMy_ReturnsFoundItemsOfTheGivenUser_WhenTheGivenNameIsFound() {
        User user = UserUtils.newUserList().getFirst();

        List<Item> itemsOfTheUser = itemList.stream()
                .filter(item -> item.getUser().equals(user))
                .toList();

        Item itemToBeFound = itemsOfTheUser.getFirst();
        String nameToSearch = itemToBeFound.getName();

        List<ItemGetResponse> expectedResponse = itemsOfTheUser.stream()
                .map(item -> ItemGetResponse.by(item, NUMBER_OF_USES_DEFAULT))
                .toList();

        when(repository.findAllByUserAndNameContainingAndActiveIsTrue(user, nameToSearch, SORT_BY_NAME))
                .thenReturn(List.of(itemToBeFound));
        when(budgetItemRepository.countBudgetItemByItem(any()))
                .thenReturn(NUMBER_OF_USES_DEFAULT);

        List<ItemGetResponse> response = service.listAllMy(user, Optional.of(nameToSearch));

        assertThat(response)
                .isNotNull()
                .isNotEmpty()
                .containsExactlyElementsOf(expectedResponse);
    }

    @Test
    @DisplayName("listAllMy returns an empty list when the given name is not found")
    @Order(9)
    void listAllMy_ReturnsEmpty_WhenTheGivenNameIsNotFound() {
        User user = UserUtils.newUserList().getFirst();

        String randomName = "Random Name";

        when(repository.findAllByUserAndNameContainingAndActiveIsTrue(user, randomName, SORT_BY_NAME))
                .thenReturn(Collections.emptyList());

        List<ItemGetResponse> response = service.listAllMy(user, Optional.of(randomName));

        assertThat(response)
                .isNotNull()
                .isEmpty();
    }

    @Test
    @DisplayName("update updates item when successful")
    @Order(10)
    void update_UpdatesItem_WhenSuccessful() {
        User clientUser = UserUtils.newUserList().get(1);
        ItemPutRequest request = ItemUtils.newItemPutRequest();

        Item itemToUpdate = ItemUtils.newItemList().get(1);
        Item updatedItem = ItemUtils.newUpdatedItem();

        when(repository.findByIdAndUserAndActiveIsTrue(request.id(), clientUser))
                .thenReturn(Optional.of(itemToUpdate));
        when(repository.save(updatedItem)).thenReturn(updatedItem);

        assertThatNoException()
                .isThrownBy(() -> service.update(clientUser, request.id(), request));
    }

    @Test
    @DisplayName("update updates item when the given user is admin")
    @Order(11)
    void update_UpdatesItem_WhenTheGivenUserIsAdmin() {
        User adminUser = UserUtils.newUserList().getFirst();
        ItemPutRequest request = ItemUtils.newItemPutRequest();

        Item itemToUpdate = ItemUtils.newItemList().get(1);
        Item updatedItem = ItemUtils.newUpdatedItem();

        when(repository.findById(request.id()))
                .thenReturn(Optional.of(itemToUpdate));
        when(repository.save(updatedItem)).thenReturn(updatedItem);

        assertThatNoException()
                .isThrownBy(() -> service.update(adminUser, request.id(), request));
    }

    @Test
    @DisplayName("update throws BadRequestException when the url id is different from the request body id")
    @Order(12)
    void update_ThrowsBadRequestException_WhenTheUrlIdIsDifferentFromTheRequestBodyId() {
        User adminUser = UserUtils.newUserList().getFirst();

        Long randomId = 999L;
        ItemPutRequest request = ItemUtils.newItemPutRequest();

        assertThatThrownBy(() -> service.update(adminUser, randomId, request))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("The url id (%s) is different from the request body id(%s)".formatted(randomId, request.id()));
    }

    @Test
    @DisplayName("update throws NotFoundException when the given id is not found")
    @Order(13)
    void update_ThrowsNotFoundException_WhenTheGivenIdIsNotFound() {
        User clientUser = UserUtils.newUserList().get(1);

        Long randomId = 999L;
        ItemPutRequest request = ItemUtils.newItemPutRequest().withId(randomId);

        when(repository.findByIdAndUserAndActiveIsTrue(randomId, clientUser))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.update(clientUser, request.id(), request))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Item with id '%s' not found".formatted(randomId));
    }

    @Test
    @DisplayName("update throws NotFoundException when the given id is not found and user is admin")
    @Order(14)
    void update_ThrowsNotFoundException_WhenTheGivenIdIsNotFoundAndUserIsAdmin() {
        User adminUser = UserUtils.newUserList().getFirst();

        Long randomId = 999L;
        ItemPutRequest request = ItemUtils.newItemPutRequest().withId(randomId);

        when(repository.findById(randomId))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.update(adminUser, request.id(), request))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Item with id '%s' not found".formatted(randomId));
    }

    @Test
    @DisplayName("delete set active false when successful")
    @Order(15)
    void delete_SetActiveFalse_WhenSuccessful() {
        User user = UserUtils.newUserList().get(1);

        Item itemToDelete = itemList.get(1);
        Long idToDelete = itemToDelete.getId();

        when(repository.findByIdAndUserAndActiveIsTrue(idToDelete, user))
                .thenReturn(Optional.of(itemToDelete));
        when(repository.save(itemToDelete.withActive(false)))
                .thenReturn(itemToDelete.withActive(false));

        assertThatNoException()
                .isThrownBy(() -> service.delete(user, idToDelete));
    }

    @Test
    @DisplayName("delete set active false when the given user is admin")
    @Order(16)
    void delete_SetActiveFalse_WhenTheGivenUserIsAdmin() {
        User user = UserUtils.newUserList().getFirst();

        Item itemToDelete = itemList.get(1);
        Long idToDelete = itemToDelete.getId();

        when(repository.findByIdAndActiveIsTrue(idToDelete))
                .thenReturn(Optional.of(itemToDelete));
        when(repository.save(itemToDelete.withActive(false)))
                .thenReturn(itemToDelete.withActive(false));

        assertThatNoException()
                .isThrownBy(() -> service.delete(user, idToDelete));
    }

    @Test
    @DisplayName("delete throws NotFoundException when the given id is not found")
    @Order(16)
    void delete_ThrowsNotFoundException_WhenTheGivenIdIsNotFound() {
        User user = UserUtils.newUserList().get(1);

        Long randomId = 999L;

        when(repository.findByIdAndUserAndActiveIsTrue(randomId, user))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.delete(user, randomId))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Item with id '%s' not found".formatted(randomId));
    }

    @Test
    @DisplayName("delete throws NotFoundException when the given id is not found and user is admin")
    @Order(17)
    void delete_ThrowsNotFoundException_WhenTheGivenIdIsNotFoundAndUserIsAdmin() {
        User user = UserUtils.newUserList().getFirst();

        Long randomId = 999L;

        when(repository.findByIdAndActiveIsTrue(randomId))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.delete(user, randomId))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Item with id '%s' not found".formatted(randomId));
    }
}
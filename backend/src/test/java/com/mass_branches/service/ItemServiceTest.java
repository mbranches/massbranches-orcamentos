package com.mass_branches.service;

import com.mass_branches.dto.request.ItemPostRequest;
import com.mass_branches.dto.response.ItemGetResponse;
import com.mass_branches.dto.response.ItemPostResponse;
import com.mass_branches.exception.NotFoundException;
import com.mass_branches.model.Item;
import com.mass_branches.model.User;
import com.mass_branches.repository.ItemRepository;
import com.mass_branches.utils.ItemUtils;
import com.mass_branches.utils.UserUtils;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ExtendWith(MockitoExtension.class)
class ItemServiceTest {
    @InjectMocks
    private ItemService service;
    @Mock
    private ItemRepository repository;
    private List<Item> itemList;
    private List<ItemGetResponse> itemGetResponseList;

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

        BDDMockito.when(repository.save(itemToSave)).thenReturn(savedItem);

        ItemPostResponse response = service.create(user, postRequest);

        Assertions.assertThat(response)
                .isNotNull()
                .isEqualTo(expectedResponse);
    }

    @Test
    @DisplayName("listAll returns all items when the given user is admin")
    @Order(2)
    void listAll_ReturnsAllItems_WhenTheGivenUserIsAdmin() {
        User adminUser = UserUtils.newUserList().getFirst();

        BDDMockito.when(repository.findAll()).thenReturn(itemList);

        List<ItemGetResponse> response = service.listAll(adminUser, null);

        Assertions.assertThat(response)
                .isNotNull()
                .isNotEmpty()
                .containsExactlyElementsOf(itemGetResponseList);
    }

    @Test
    @DisplayName("listAll returns items of the given user when the user is admin and the argument personal is true")
    @Order(3)
    void listAll_ReturnsItemsOfTheGivenUser_WhenTheUserIsAdminAndTheArgumentPersonalIsTrue() {
        User adminUser = UserUtils.newUserList().getFirst();

        List<Item> expectedItemList = List.of(itemList.getFirst());
        List<ItemGetResponse> expectedResponse = List.of(itemGetResponseList.getFirst());

        BDDMockito.when(repository.findAllByUserAndActiveIsTrue(adminUser)).thenReturn(expectedItemList);

        List<ItemGetResponse> response = service.listAll(adminUser, true);

        Assertions.assertThat(response)
                .isNotNull()
                .isNotEmpty()
                .containsExactlyElementsOf(expectedResponse);
    }

    @Test
    @DisplayName("listAll returns items of the given user when the user is client")
    @Order(4)
    void listAll_ReturnsItemsOfTheGivenUser_WhenTheUserIsClient() {
        User user = UserUtils.newUserList().getLast();

        List<Item> expectedItemList = List.of(itemList.getLast());
        List<ItemGetResponse> expectedResponse = List.of(itemGetResponseList.getLast());

        BDDMockito.when(repository.findAllByUserAndActiveIsTrue(user)).thenReturn(expectedItemList);

        List<ItemGetResponse> response = service.listAll(user, null);

        Assertions.assertThat(response)
                .isNotNull()
                .isNotEmpty()
                .containsExactlyElementsOf(expectedResponse);
    }

    @Test
    @DisplayName("findById returns found item of the given user when the user is client")
    @Order(5)
    void findById_ReturnsFoundItemOfTheGivenUser_WhenUserIsClient() {
        User user = UserUtils.newUserList().getLast();

        Item itemToBeFound = itemList.getLast();
        Long idToSearch = itemToBeFound.getId();

        ItemGetResponse expectedResponse = itemGetResponseList.getLast();

        BDDMockito.when(repository.findByIdAndUserAndActiveIsTrue(idToSearch, user)).thenReturn(Optional.of(itemToBeFound));

        ItemGetResponse response = service.findById(user, idToSearch);

        Assertions.assertThat(response)
                .isNotNull()
                .isEqualTo(expectedResponse);
    }

    @Test
    @DisplayName("findById returns found item when the given user is admin")
    @Order(6)
    void findById_ReturnsFoundItem_WhenTheGivenUserIsAdmin() {
        User user = UserUtils.newUserList().getFirst();

        Item itemToBeFound = itemList.getLast();
        Long idToSearch = itemToBeFound.getId();

        ItemGetResponse expectedResponse = itemGetResponseList.getLast();

        BDDMockito.when(repository.findById(idToSearch)).thenReturn(Optional.of(itemToBeFound));

        ItemGetResponse response = service.findById(user, idToSearch);

        Assertions.assertThat(response)
                .isNotNull()
                .isEqualTo(expectedResponse);
    }

    @Test
    @DisplayName("findById returns found item when the item to be found is not active but the given user is admin")
    @Order(7)
    void findById_ReturnsFoundItem_WhenTheItemToBeFoundIsNotActiveButTheGivenUserIsAdmin() {
        User user = UserUtils.newUserList().getFirst();

        Item itemToBeFound = itemList.get(1);
        Long idToSearch = itemToBeFound.getId();

        ItemGetResponse expectedResponse = itemGetResponseList.get(1);

        BDDMockito.when(repository.findById(idToSearch)).thenReturn(Optional.of(itemToBeFound));

        ItemGetResponse response = service.findById(user, idToSearch);

        Assertions.assertThat(response)
                .isNotNull()
                .isEqualTo(expectedResponse);
    }

    @Test
    @DisplayName("findById throws NotFoundException when the given id is not found")
    @Order(8)
    void findById_ThrowsNotFoundException_WhenTheGivenIdIsNotFound() {
        User user = UserUtils.newUserList().getFirst();

        Long randomId = 999L;

        BDDMockito.when(repository.findById(randomId)).thenReturn(Optional.empty());

        Assertions.assertThatThrownBy(() -> service.findById(user, randomId))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Item with id '%s' not found".formatted(randomId));
    }

    @Test
    @DisplayName("findById throws NotFoundException when the item to be found does not belongs to the given user")
    @Order(9)
    void findById_ThrowsNotFoundException_WhenTheItemToBeFoundDoesNotBelongsToTheGivenUser() {
        User user = UserUtils.newUserList().getLast();

        Item itemToBeFound = itemList.getFirst();
        Long idToSearch = itemToBeFound.getId();

        BDDMockito.when(repository.findByIdAndUserAndActiveIsTrue(idToSearch, user)).thenReturn(Optional.empty());

        Assertions.assertThatThrownBy(() -> service.findById(user, idToSearch))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Item with id '%s' not found".formatted(idToSearch));
    }

    @Test
    @DisplayName("findById throws NotFoundException when the item of the client to be found is not active")
    @Order(10)
    void findById_ThrowsNotFoundException_WhenTheItemOfTheGivenClientToBeFoundIsNotActive() {
        User user = UserUtils.newUserList().get(1);

        Item itemToBeFound = itemList.get(1);
        Long idToSearch = itemToBeFound.getId();

        BDDMockito.when(repository.findByIdAndUserAndActiveIsTrue(idToSearch, user)).thenReturn(Optional.empty());

        Assertions.assertThatThrownBy(() -> service.findById(user, idToSearch))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Item with id '%s' not found".formatted(idToSearch));
    }
}
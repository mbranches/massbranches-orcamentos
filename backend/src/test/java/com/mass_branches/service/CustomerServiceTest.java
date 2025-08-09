package com.mass_branches.service;

import com.mass_branches.dto.request.CustomerPostRequest;
import com.mass_branches.dto.response.CustomerGetResponse;
import com.mass_branches.dto.response.CustomerPostResponse;
import com.mass_branches.dto.response.CustomerPutRequest;
import com.mass_branches.exception.BadRequestException;
import com.mass_branches.exception.NotFoundException;
import com.mass_branches.model.Customer;
import com.mass_branches.model.CustomerType;
import com.mass_branches.model.CustomerTypeName;
import com.mass_branches.model.User;
import com.mass_branches.repository.BudgetRepository;
import com.mass_branches.repository.CustomerRepository;
import com.mass_branches.repository.CustomerTypeRepository;
import com.mass_branches.utils.CustomerTypeUtils;
import com.mass_branches.utils.CustomerUtils;
import com.mass_branches.utils.UserUtils;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class CustomerServiceTest {
    @InjectMocks
    private CustomerService service;
    @Mock
    private CustomerRepository repository;
    @Mock
    private BudgetRepository budgetRepository;
    @Mock
    private CustomerTypeRepository customerTypeRepository;
    private List<Customer> customerList;
    private final long NUMBER_OF_BUDGETS_DEFAULT = 1L;
    private final BigDecimal TOTAL_IN_BUDGETS_WITH_BDI_DEFAULT = BigDecimal.valueOf(10000);
    private final Sort SORT_BY_UPDATED_AT = Sort.by("updatedAt").descending();

    @BeforeEach
    void init() {
        customerList = CustomerUtils.newCustomerList();
    }

    @Test
    @DisplayName("create returns created customer when successful")
    @Order(1)
    void create_ReturnsCreatedCustomer_WhenSuccessful() {
        User user = UserUtils.newUserList().get(1);

        CustomerPostRequest request = CustomerUtils.newCustomerPostRequest();
        CustomerPostResponse expectedResponse = CustomerUtils.newCustomerPostResponse();

        Customer customerToSave = CustomerUtils.newCustomerToSave();
        Customer savedCustomer = CustomerUtils.newCustomerSaved();

        CustomerType pessoaFisica = CustomerTypeUtils.newCustomerTypeList().getLast();

        when(customerTypeRepository.findByName(request.type()))
                .thenReturn(pessoaFisica);
        when(repository.save(customerToSave))
                .thenReturn(savedCustomer);

        CustomerPostResponse response = service.create(user, request);

        assertThat(response)
                .isNotNull()
                .isEqualTo(expectedResponse);
    }

    @Test
    @DisplayName("listAllMy returns found customers of the given user when successful")
    @Order(2)
    void listAllMy_ReturnsFoundCustomersOfTheGivenUser_WhenSuccessful() {
        User user = UserUtils.newUserList().get(1);

        List<Customer> expectedCustomers = customerList.stream().filter(customer -> customer.getUser().equals(user)).toList();
        List<CustomerGetResponse> expectedResponse = expectedCustomers.stream()
                .map(customer -> CustomerGetResponse.by(
                        customer,
                        NUMBER_OF_BUDGETS_DEFAULT,
                        TOTAL_IN_BUDGETS_WITH_BDI_DEFAULT)
                )
                .toList();

        when(repository.findAllByUserAndActiveIsTrue(user, SORT_BY_UPDATED_AT))
                .thenReturn(expectedCustomers);
        when(budgetRepository.countBudgetsByCustomerAndActiveIsTrue(ArgumentMatchers.any()))
                .thenReturn(NUMBER_OF_BUDGETS_DEFAULT);
        when(budgetRepository.sumTotalWithBdiByCustomerAndActiveIsTrue(ArgumentMatchers.any()))
                .thenReturn(TOTAL_IN_BUDGETS_WITH_BDI_DEFAULT);


        List<CustomerGetResponse> response = service.listAllMy(user, Optional.empty(), Optional.empty());

        assertThat(response)
                .isNotNull()
                .isNotEmpty()
                .containsExactlyElementsOf(expectedResponse);
    }

    @Test
    @DisplayName("listAllMy returns found customer of the given user when the given name is found")
    @Order(3)
    void listAllMy_ReturnsFoundCustomersOfTheGivenUser_WhenTheGivenNameIsFound() {
        User user = UserUtils.newUserList().get(1);

        List<Customer> userCustomers = customerList.stream().filter(customer -> customer.getUser().equals(user)).toList();
        Customer customerToBeFound = userCustomers.getFirst();
        String nameToSearch = customerToBeFound.getName();

        List<Customer> foundCustomers = List.of(customerToBeFound);
        List<CustomerGetResponse> expectedResponse = foundCustomers.stream()
                .map(customer -> CustomerGetResponse.by(
                        customer,
                        NUMBER_OF_BUDGETS_DEFAULT,
                        TOTAL_IN_BUDGETS_WITH_BDI_DEFAULT
                    )
                ).toList();

        when(repository.findAllByUserAndActiveIsTrueAndNameContaining(user, nameToSearch, SORT_BY_UPDATED_AT))
                .thenReturn(foundCustomers);
        when(budgetRepository.countBudgetsByCustomerAndActiveIsTrue(ArgumentMatchers.any()))
                .thenReturn(NUMBER_OF_BUDGETS_DEFAULT);
        when(budgetRepository.sumTotalWithBdiByCustomerAndActiveIsTrue(ArgumentMatchers.any()))
                .thenReturn(TOTAL_IN_BUDGETS_WITH_BDI_DEFAULT);


        List<CustomerGetResponse> response = service.listAllMy(user, Optional.of(nameToSearch), Optional.empty());

        assertThat(response)
                .isNotNull()
                .isNotEmpty()
                .containsExactlyElementsOf(expectedResponse);
    }

    @Test
    @DisplayName("listAllMy returns an empty list when the given name is not found")
    @Order(4)
    void listAllMy_ReturnsEmptyList_WhenTheGivenNameIsNotFound() {
        User user = UserUtils.newUserList().get(1);

        String randomName = "Random Name";

        when(repository.findAllByUserAndActiveIsTrueAndNameContaining(user, randomName, SORT_BY_UPDATED_AT))
                .thenReturn(Collections.emptyList());

        List<CustomerGetResponse> response = service.listAllMy(user, Optional.of(randomName), Optional.empty());

        assertThat(response)
                .isNotNull()
                .isEmpty();
    }

    @Test
    @DisplayName("listAllMy returns found customer of the given user when some user customer is of the given type")
    @Order(5)
    void listAllMy_ReturnsFoundCustomersOfTheGivenUser_WhenSomeUserCustomerIsOfTheGivenType() {
        User user = UserUtils.newUserList().get(1);

        CustomerType pessoaFisicaType = CustomerTypeUtils.newCustomerTypeList().getFirst();

        List<Customer> foundCustomers = customerList.stream()
                .filter(customer -> customer.getUser().equals(user) && customer.getType().equals(pessoaFisicaType))
                .toList();

        List<CustomerGetResponse> expectedResponse = foundCustomers.stream()
                .map(customer -> CustomerGetResponse.by(
                                customer,
                                NUMBER_OF_BUDGETS_DEFAULT,
                                TOTAL_IN_BUDGETS_WITH_BDI_DEFAULT
                        )
                ).toList();

        when(repository.findAllByUserAndActiveIsTrueAndType_Name(user, pessoaFisicaType.getName(), SORT_BY_UPDATED_AT))
                .thenReturn(foundCustomers);
        when(budgetRepository.countBudgetsByCustomerAndActiveIsTrue(ArgumentMatchers.any()))
                .thenReturn(NUMBER_OF_BUDGETS_DEFAULT);
        when(budgetRepository.sumTotalWithBdiByCustomerAndActiveIsTrue(ArgumentMatchers.any()))
                .thenReturn(TOTAL_IN_BUDGETS_WITH_BDI_DEFAULT);


        List<CustomerGetResponse> response = service.listAllMy(user, Optional.empty(), Optional.of(pessoaFisicaType.getName()));

        assertThat(response)
                .isNotNull()
                .isNotEmpty()
                .containsExactlyElementsOf(expectedResponse);
    }

    @Test
    @DisplayName("listAllMy returns an empty list when does not exists user customer with given type")
    @Order(6)
    void listAllMy_ReturnsEmptyList_WhenDoesNotExistsUserCustomerWithGivenType() {
        User user = UserUtils.newUserList().getLast();

        CustomerType pessoaFisicaType = CustomerTypeUtils.newCustomerTypeList().getFirst();

        when(repository.findAllByUserAndActiveIsTrueAndType_Name(user, pessoaFisicaType.getName(), SORT_BY_UPDATED_AT))
                .thenReturn(Collections.emptyList());

        List<CustomerGetResponse> response = service.listAllMy(user, Optional.empty(), Optional.of(pessoaFisicaType.getName()));

        assertThat(response)
                .isNotNull()
                .isEmpty();
    }

    @Test
    @DisplayName("listAllMy returns found customers of the given user when  some user customer is of the given type and have the given name")
    @Order(7)
    void listAllMy_ReturnsFoundCustomersOfTheGivenUser_WhenSomeUserCustomerIsOfTheGivenTypeAndHaveTheGivenName() {
        User user = UserUtils.newUserList().get(1);

        List<Customer> userCustomers = customerList.stream()
                .filter(customer -> customer.getUser().equals(user))
                .toList();

        Customer customerToBeFound = userCustomers.getFirst();
        String nameToSearch = customerToBeFound.getName();
        CustomerTypeName typeNameToSearch = customerToBeFound.getType().getName();

        List<Customer> foundCustomers = userCustomers.stream()
                .filter(customer -> customer.getName().equals(nameToSearch) && customer.getType().getName().equals(typeNameToSearch))
                .toList();

        List<CustomerGetResponse> expectedResponse = foundCustomers.stream()
                .map(customer -> CustomerGetResponse.by(
                                customer,
                                NUMBER_OF_BUDGETS_DEFAULT,
                                TOTAL_IN_BUDGETS_WITH_BDI_DEFAULT
                        )
                ).toList();

        when(repository.findAllByUserAndActiveIsTrueAndNameContainingAndType_Name(user, nameToSearch, typeNameToSearch, SORT_BY_UPDATED_AT))
                .thenReturn(foundCustomers);
        when(budgetRepository.countBudgetsByCustomerAndActiveIsTrue(ArgumentMatchers.any()))
                .thenReturn(NUMBER_OF_BUDGETS_DEFAULT);
        when(budgetRepository.sumTotalWithBdiByCustomerAndActiveIsTrue(ArgumentMatchers.any()))
                .thenReturn(TOTAL_IN_BUDGETS_WITH_BDI_DEFAULT);


        List<CustomerGetResponse> response = service.listAllMy(user, Optional.of(nameToSearch), Optional.of(typeNameToSearch));

        assertThat(response)
                .isNotNull()
                .isNotEmpty()
                .containsExactlyElementsOf(expectedResponse);
    }

    @Test
    @DisplayName("listAllMy returns an empty list when does not exists user customer with given type and given name")
    @Order(8)
    void listAllMy_ReturnsEmptyList_WhenDoesNotExistsUserCustomerWithGivenTypeAndGivenName() {
        User user = UserUtils.newUserList().get(1);

        String randomName = "RandomName";
        CustomerTypeName typeNameToSearch = CustomerTypeName.PESSOA_JURIDICA;

        when(repository.findAllByUserAndActiveIsTrueAndNameContainingAndType_Name(user, randomName, typeNameToSearch, SORT_BY_UPDATED_AT))
                .thenReturn(Collections.emptyList());


        List<CustomerGetResponse> response = service.listAllMy(user, Optional.of(randomName), Optional.of(typeNameToSearch));

        assertThat(response)
                .isNotNull()
                .isEmpty();
    }

    @Test
    @DisplayName("findById returns found customer of the given user when user is client")
    @Order(9)
    void findById_ReturnsFoundCustomerOfTheGivenUser_WhenUserIsClient() {
        User clientUser = UserUtils.newUserList().get(1);

        List<Customer> userCustomers = customerList.stream().filter(customer -> customer.getUser().equals(clientUser)).toList();

        Customer customerToBeFound = userCustomers.getFirst();
        String idToSearch = customerToBeFound.getId();

        CustomerGetResponse expectedResponse = CustomerGetResponse.by(customerToBeFound, NUMBER_OF_BUDGETS_DEFAULT, TOTAL_IN_BUDGETS_WITH_BDI_DEFAULT);

        when(repository.findByUserAndActiveIsTrueAndId(clientUser, idToSearch))
                .thenReturn(Optional.of(customerToBeFound));
        when(budgetRepository.countBudgetsByCustomerAndActiveIsTrue(customerToBeFound))
                .thenReturn(NUMBER_OF_BUDGETS_DEFAULT);
        when(budgetRepository.sumTotalWithBdiByCustomerAndActiveIsTrue(customerToBeFound))
                .thenReturn(TOTAL_IN_BUDGETS_WITH_BDI_DEFAULT);
        CustomerGetResponse response = service.findById(clientUser, idToSearch);

        assertThat(response)
                .isNotNull()
                .isEqualTo(expectedResponse);
    }

    @Test
    @DisplayName("findById returns found customer of the given user when the given user is admin")
    @Order(10)
    void findById_ReturnsFoundCustomer_WhenTheGivenUserIsAdmin() {
        User adminUser = UserUtils.newUserList().getFirst();

        Customer customerToBeFound = customerList.getFirst();
        String idToSearch = customerToBeFound.getId();

        CustomerGetResponse expectedResponse = CustomerGetResponse.by(customerToBeFound, NUMBER_OF_BUDGETS_DEFAULT, TOTAL_IN_BUDGETS_WITH_BDI_DEFAULT);

        when(repository.findById(idToSearch))
                .thenReturn(Optional.of(customerToBeFound));
        when(budgetRepository.countBudgetsByCustomerAndActiveIsTrue(customerToBeFound))
                .thenReturn(NUMBER_OF_BUDGETS_DEFAULT);
        when(budgetRepository.sumTotalWithBdiByCustomerAndActiveIsTrue(customerToBeFound))
                .thenReturn(TOTAL_IN_BUDGETS_WITH_BDI_DEFAULT);
        CustomerGetResponse response = service.findById(adminUser, idToSearch);

        assertThat(response)
                .isNotNull()
                .isEqualTo(expectedResponse);
    }

    @Test
    @DisplayName("findById throws NotFoundException when the given id is not found")
    @Order(11)
    void findById_ThrowsNotFoundException_WhenTheGivenIdIsNotFound() {
        User clientUser = UserUtils.newUserList().get(1);

        String randomId = "random-id";

        when(repository.findByUserAndActiveIsTrueAndId(clientUser, randomId))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.findById(clientUser, randomId))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Customer with id '%s' not found".formatted(randomId));
    }

    @Test
    @DisplayName("findById throws NotFoundException when the given id is not found and user is admin")
    @Order(12)
    void findById_ThrowsNotFoundException_WhenTheGivenIdIsNotFoundAndUserIsAdmin() {
        User adminUser = UserUtils.newUserList().getFirst();

        String randomId = "random-id";

        when(repository.findById(randomId))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.findById(adminUser, randomId))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Customer with id '%s' not found".formatted(randomId));
    }

    @Test
    @DisplayName("numberOfCustomers returns number of the given user customers when successful")
    @Order(13)
    void numberOfCustomers_ReturnsNumberOfTheGivenUserCustomers_WhenSuccessful() {
        User user = UserUtils.newUserList().get(1);

        long expectedResponse = customerList.stream().filter(customer -> customer.getUser().equals(user)).count();

        when(repository.countCustomersByUserAndActiveIsTrue(user))
                .thenReturn(expectedResponse);

        long response = service.numberOfCustomers(user);

        assertThat(response)
                .isEqualTo(expectedResponse);
    }

    @Test
    @DisplayName("update updates customer when successful")
    @Order(14)
    void update_UpdatesCustomer_WhenSuccessful() {
        User clientUser = UserUtils.newUserList().get(1);

        CustomerPutRequest request = CustomerUtils.newCustomerPutRequest();

        Customer customerToUpdate = customerList.getFirst();
        Customer updatedCustomer = CustomerUtils.newCustomerUpdated();

        when(repository.findByUserAndActiveIsTrueAndId(clientUser, request.id()))
                .thenReturn(Optional.of(customerToUpdate));
        when(customerTypeRepository.findByName(request.type()))
                .thenReturn(updatedCustomer.getType());
        when(repository.save(updatedCustomer))
                .thenReturn(updatedCustomer);

        assertThatNoException()
                .isThrownBy(() -> service.update(request.id(), request, clientUser));
    }

    @Test
    @DisplayName("update updates customer when the given user is admin")
    @Order(15)
    void update_UpdatesCustomer_WhenTheGivenUserIsAdmin() {
        User adminUser = UserUtils.newUserList().getFirst();

        CustomerPutRequest request = CustomerUtils.newCustomerPutRequest();

        Customer customerToUpdate = customerList.getFirst();
        Customer updatedCustomer = CustomerUtils.newCustomerUpdated();

        when(repository.findById(request.id()))
                .thenReturn(Optional.of(customerToUpdate));
        when(customerTypeRepository.findByName(request.type()))
                .thenReturn(updatedCustomer.getType());
        when(repository.save(updatedCustomer))
                .thenReturn(updatedCustomer);

        assertThatNoException()
                .isThrownBy(() -> service.update(request.id(), request, adminUser));
    }

    @Test
    @DisplayName("update throws BadRequestException when the url id is different from the request body id")
    @Order(16)
    void update_ThrowsBadRequestException_WhenTheUrlIdIsDifferentFromTheRequestBodyId() {
        User clientUser = UserUtils.newUserList().get(1);

        String randomId = "random-id";
        CustomerPutRequest request = CustomerUtils.newCustomerPutRequest();

        assertThatThrownBy(() -> service.update(randomId, request, clientUser))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("The url id (%s) is different from the request body id(%s)".formatted(randomId, request.id()));
    }

    @Test
    @DisplayName("update throws NotFoundException when the given id is not found")
    @Order(17)
    void update_ThrowsNotFoundException_WhenTheGivenIdIsNotFound() {
        User clientUser = UserUtils.newUserList().get(1);

        String randomId = "random-id";
        CustomerPutRequest request = CustomerUtils.newCustomerPutRequest().withId(randomId);

        when(repository.findByUserAndActiveIsTrueAndId(clientUser, request.id()))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.update(randomId, request, clientUser))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Customer with id '%s' not found".formatted(randomId));
    }

    @Test
    @DisplayName("update throws NotFoundException when the given id is not found and user is admin")
    @Order(18)
    void update_ThrowsNotFoundException_WhenTheGivenIdIsNotFoundAndUserIsAdmin() {
        User adminUser = UserUtils.newUserList().getFirst();

        String randomId = "random-id";
        CustomerPutRequest request = CustomerUtils.newCustomerPutRequest().withId(randomId);

        when(repository.findById(request.id()))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.update(randomId, request, adminUser))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Customer with id '%s' not found".formatted(randomId));
    }

    @Test
    @DisplayName("delete set active false when successful")
    @Order(19)
    void delete_SetActiveFalse_WhenSuccessful() {
        User ownerUser = UserUtils.newUserList().get(1);

        Customer customerToDelete = customerList.getFirst();
        String idToDelete = customerToDelete.getId();

        when(repository.findByUserAndActiveIsTrueAndId(ownerUser, idToDelete))
                .thenReturn(Optional.of(customerToDelete));
        when(repository.save(customerToDelete.withActive(false)))
                .thenReturn(customerToDelete.withActive(false));

        assertThatNoException()
                .isThrownBy(() -> service.delete(ownerUser, idToDelete));
    }

    @Test
    @DisplayName("delete set active false when the given user is admin")
    @Order(20)
    void delete_SetActiveFalse_WhenTheGivenUserIsAdmin() {
        User adminUser = UserUtils.newUserList().getFirst();

        Customer customerToDelete = customerList.getFirst();
        String idToDelete = customerToDelete.getId();

        when(repository.findByIdAndActiveIsTrue(idToDelete))
                .thenReturn(Optional.of(customerToDelete));
        when(repository.save(customerToDelete.withActive(false)))
                .thenReturn(customerToDelete.withActive(false));

        assertThatNoException()
                .isThrownBy(() -> service.delete(adminUser, idToDelete));
    }

    @Test
    @DisplayName("delete throws NotFoundException when the given id is not found")
    @Order(21)
    void delete_ThrowsNotFoundException_WhenTheGivenIdIsNotFound() {
        User ownerUser = UserUtils.newUserList().get(1);

        String randomId = "random-id";

        when(repository.findByUserAndActiveIsTrueAndId(ownerUser, randomId))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.delete(ownerUser, randomId))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Customer with id '%s' not found".formatted(randomId));

    }

    @Test
    @DisplayName("delete throws NotFoundException when the given id is not found and user is admin")
    @Order(22)
    void delete_ThrowsNotFoundException_WhenTheGivenIdIsNotFoundAndUserIsAdmin() {
        User adminUser = UserUtils.newUserList().getFirst();

        String randomId = "random-id";

        when(repository.findByIdAndActiveIsTrue(randomId))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.delete(adminUser, randomId))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Customer with id '%s' not found".formatted(randomId));
    }
}
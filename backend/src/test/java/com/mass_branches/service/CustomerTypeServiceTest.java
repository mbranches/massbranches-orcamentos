package com.mass_branches.service;

import com.mass_branches.exception.NotFoundException;
import com.mass_branches.model.CustomerType;
import com.mass_branches.model.CustomerTypeName;
import com.mass_branches.repository.CustomerTypeRepository;
import com.mass_branches.utils.CustomerTypeUtils;
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
public class CustomerTypeServiceTest {
    @InjectMocks
    private CustomerTypeService service;
    @Mock
    private CustomerTypeRepository repository;
    private List<CustomerType> customerTypeList;

    @BeforeEach
    void init() {
        customerTypeList = CustomerTypeUtils.newCustomerTypeList();
    }

    @Test
    @DisplayName("findByNameOrThrowsNotFoundException returns found customerType when successful")
    @Order(1)
    void findByNameOrThrowsNotFoundException_ReturnsFoundCustomerType_WhenSuccessful() {
        CustomerType customerTypeToBeFound = customerTypeList.getFirst();
        CustomerTypeName nameToSearch = customerTypeToBeFound.getName();

        BDDMockito.when(repository.findByName(nameToSearch)).thenReturn(Optional.of(customerTypeToBeFound));

        CustomerType response = service.findByNameOrThrowsNotFoundException(String.valueOf(nameToSearch));

        Assertions.assertThat(response)
                .isNotNull()
                .isEqualTo(customerTypeToBeFound);
    }

    @Test
    @DisplayName("findByNameOrThrowsNotFoundException throws NotFoundException when the given name is invalid")
    @Order(2)
    void findByNameOrThrowsNotFoundException_ThrowsNotFoundException_WhenTheGivenNameIsInvalid() {
        String randomName = "randomName";

        Assertions.assertThatThrownBy(() -> service.findByNameOrThrowsNotFoundException(randomName))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Customer type not found");
    }

    @Test
    @DisplayName("findByNameOrThrowsNotFoundException throws NotFoundException when the given name is invalid")
    @Order(3)
    void findByNameOrThrowsNotFoundException_ThrowsNotFoundException_WhenTheGivenNameIsNotFound() {
        CustomerTypeName nameToSearch = CustomerTypeName.PESSOA_FISICA;

        BDDMockito.when(repository.findByName(nameToSearch)).thenReturn(Optional.empty());

        Assertions.assertThatThrownBy(() -> service.findByNameOrThrowsNotFoundException(String.valueOf(nameToSearch)))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Customer type not found");
    }
}

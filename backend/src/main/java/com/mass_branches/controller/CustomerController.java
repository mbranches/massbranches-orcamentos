package com.mass_branches.controller;

import com.mass_branches.dto.request.CustomerPostRequest;
import com.mass_branches.dto.response.*;
import com.mass_branches.exception.DefaultErrorMessage;
import com.mass_branches.model.CustomerTypeName;
import com.mass_branches.model.User;
import com.mass_branches.service.BudgetService;
import com.mass_branches.service.CustomerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Tag(name = "Customers", description = "Operations with customers")
@SecurityRequirement(name = "Bearer Authentication")
@RequiredArgsConstructor
@RequestMapping("/api/v1/customers")
@RestController
public class CustomerController {
    private final CustomerService service;
    private final BudgetService budgetService;

    @Operation(
            summary = "Create customer",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "customer created successfully",
                            content = @Content(
                                    schema = @Schema(implementation = CustomerPostResponse.class)
                            )
                    ),
                    @ApiResponse(
                            description = "required field not given",
                            responseCode = "400",
                            content = @Content(schema = @Schema(implementation = DefaultErrorMessage.class))
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "the requesting user is not authenticated",
                            content = @Content
                    )
            }
    )
    @PostMapping
    public ResponseEntity<CustomerPostResponse> create(Authentication authentication, @Valid @RequestBody CustomerPostRequest postRequest) {
        User user = (User) authentication.getPrincipal();

        CustomerPostResponse response = service.create(user, postRequest);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(
            summary = "List all the requesting user customers",
            parameters = {
                    @Parameter(
                            name = "name",
                            description = "name to filter the requesting user customers"
                    ),
                    @Parameter(
                            name = "type",
                            description = "type to filter the requesting user customers"
                    )
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "successfully list all the requesting user customers",
                            content = @Content(
                                    array = @ArraySchema(
                                            schema = @Schema(implementation = CustomerGetResponse.class)
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "the requesting user is not authenticated",
                            content = @Content
                    )
            }
    )
    @GetMapping("/my")
    public ResponseEntity<List<CustomerGetResponse>> listAllMy(
            Authentication authentication,
            @RequestParam(required = false) Optional<String> name,
            @RequestParam(required = false) Optional<CustomerTypeName> type
    ) {
        User user = (User) authentication.getPrincipal();

        List<CustomerGetResponse> response = service.listAllMy(user, name, type);
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "List all customer budgets of the requesting user",
            parameters = @Parameter(
                    name = "id",
                    description = "customer id to list budgets"
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "successfully list all customer budget of the requesting user",
                            content = @Content(
                                    array = @ArraySchema(
                                            schema = @Schema(implementation = BudgetGetResponse.class)
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "the requesting user is not authenticated",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "id of the my customer was not found",
                            content = @Content(
                                    schema = @Schema(implementation = DefaultErrorMessage.class)
                            )
                    )
            }
    )
    @GetMapping("/my/{id}/budgets")
    public ResponseEntity<List<BudgetGetResponse>> listBudgetsByCustomerId(Authentication authentication, @PathVariable String id) {
        User user = (User) authentication.getPrincipal();

        List<BudgetGetResponse> response = budgetService.listAllByCustomerId(user, id);

        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Find customer by id",
            parameters = @Parameter(
                    name = "id",
                    description = "customer id to be found"
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "customer found successfully",
                            content = @Content(
                                    schema = @Schema(implementation = CustomerGetResponse.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "the requesting user is not authenticated",
                            content = @Content
                    ),
                    @ApiResponse(
                            description = "id was not found",
                            responseCode = "404",
                            content = @Content(
                                    schema = @Schema(implementation = DefaultErrorMessage.class)
                            )
                    )
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<CustomerGetResponse> findById(Authentication authentication, @PathVariable String id) {
        User user = (User) authentication.getPrincipal();

        CustomerGetResponse response = service.findById(user, id);

        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Get the number of the requesting user customers",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "successfully get the number of the requesting user customers",
                            content = @Content(
                                    schema = @Schema(example = "10", type = "number")
                            )
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "the requesting user is not authenticated",
                            content = @Content
                    )
            }
    )
    @GetMapping("/quantity")
    public ResponseEntity<Integer> numberOfCustomers(Authentication authentication) {
        User user = (User) authentication.getPrincipal();

        Integer response = service.numberOfCustomers(user);

        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Update customer",
            parameters = @Parameter(
                    name = "id",
                    description = "customer id to update"
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "204",
                            description = "customer updated successfully",
                            content = @Content
                    ),
                    @ApiResponse(
                            description = "required field not given or the request body id does not match the url id",
                            responseCode = "400",
                            content = @Content(schema = @Schema(implementation = DefaultErrorMessage.class))
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "the requesting user is not authenticated",
                            content = @Content
                    ),
                    @ApiResponse(
                            description = "id was not found",
                            responseCode = "404",
                            content = @Content(schema = @Schema(implementation = DefaultErrorMessage.class))
                    )
            }
    )
    @PutMapping("/{id}")
    public ResponseEntity<Void> update(
            Authentication authentication,
            @PathVariable String id,
            @Valid @RequestBody CustomerPutRequest request
    ) {
        User user = (User) authentication.getPrincipal();

        service.update(id, request, user);

        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Delete customer",
            parameters = @Parameter(
                    name = "id",
                    description = "customer id to delete"
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "204",
                            description = "customer deleted successfully",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "the requesting user is not authenticated",
                            content = @Content
                    ),
                    @ApiResponse(
                            description = "id was not found",
                            responseCode = "404",
                            content = @Content(schema = @Schema(implementation = DefaultErrorMessage.class))
                    )
            }
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(Authentication authentication, @PathVariable String id) {
        User user = (User) authentication.getPrincipal();

        service.delete(user, id);

        return ResponseEntity.noContent().build();
    }
}

package com.mass_branches.controller;

import com.mass_branches.dto.request.BudgetPutRequest;
import com.mass_branches.dto.response.BudgetElementGetResponse;
import com.mass_branches.dto.request.BudgetItemPostRequest;
import com.mass_branches.dto.request.BudgetPostRequest;
import com.mass_branches.dto.request.StagePostRequest;
import com.mass_branches.dto.response.BudgetGetResponse;
import com.mass_branches.dto.response.BudgetItemPostResponse;
import com.mass_branches.dto.response.BudgetPostResponse;
import com.mass_branches.dto.response.StagePostResponse;
import com.mass_branches.exception.DefaultErrorMessage;
import com.mass_branches.model.BudgetStatus;
import com.mass_branches.model.User;
import com.mass_branches.service.BudgetService;
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
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Tag(name = "Budgets", description = "Operations with budgets")
@SecurityRequirement(name = "Bearer Authentication")
@RequiredArgsConstructor
@RequestMapping("/api/v1/budgets")
@RestController
public class BudgetController {
    private final BudgetService service;

    @Operation(
            summary = "Create budget",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "budget created successfully",
                            content = @Content(
                                    schema = @Schema(implementation = BudgetPostResponse.class)
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
                    ),
                    @ApiResponse(
                            description = "'customerId' was not found",
                            responseCode = "404",
                            content = @Content(schema = @Schema(implementation = DefaultErrorMessage.class))
                    )
            }
    )
    @PostMapping
    public ResponseEntity<BudgetPostResponse> create(Authentication authentication, @Valid @RequestBody BudgetPostRequest postRequest) {
        User user = (User) authentication.getPrincipal();

        BudgetPostResponse response = service.create(user, postRequest);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(
            summary = "List all budgets",
            parameters = {
                    @Parameter(
                            name = "description",
                            description = "description to filter budgets"
                    ),
                    @Parameter(
                            name = "status",
                            description = "status to filter budgets"
                    )
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "successfully list all budgets",
                            content = @Content(
                                    array = @ArraySchema(
                                            schema = @Schema(implementation = BudgetGetResponse.class)
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "the requesting user does not have admin role",
                            content = @Content
                    )
            }
    )
    @GetMapping
    public ResponseEntity<List<BudgetGetResponse>> listAll(
            @RequestParam(required = false) Optional<String> description,
            @RequestParam(required = false) Optional<BudgetStatus> status
    ) {
        List<BudgetGetResponse> response = service.listAll(description, status);

        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "List all the requesting user budgets",
            parameters = {
                    @Parameter(
                            name = "description",
                            description = "description to filter the requesting user budgets"
                    ),
                    @Parameter(
                            name = "status",
                            description = "status to filter the requesting user budgets"
                    )
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "successfully list all the requesting user budgets",
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
                    )
            }
    )
    @GetMapping("/my")
    public ResponseEntity<List<BudgetGetResponse>> listMyAll(
            Authentication authentication,
            @RequestParam(required = false) Optional<String> description,
            @RequestParam(required = false) Optional<BudgetStatus> status
    ) {
        User user = (User) authentication.getPrincipal();

        List<BudgetGetResponse> response = service.listMyAll(user, description, status);

        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Update budget",
            parameters = @Parameter(
                    name = "id",
                    description = "budget id to update"
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "204",
                            description = "budget updated successfully",
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
                            description = "'customerId' or 'budgetId' was not found",
                            responseCode = "404",
                            content = @Content(schema = @Schema(implementation = DefaultErrorMessage.class))
                    )
            }
    )
    @PutMapping("/{id}")
    public ResponseEntity<Void> update(@PathVariable String id, @Valid @RequestBody BudgetPutRequest request, Authentication authentication) {
        User user = (User) authentication.getPrincipal();

        service.update(id, request, user);

        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Delete budget",
            parameters = @Parameter(
                    name = "id",
                    description = "budget id to delete"
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "204",
                            description = "budget deleted successfully",
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
    public ResponseEntity<Void> delete(@PathVariable String id, Authentication authentication) {
        User user = (User) authentication.getPrincipal();

        service.delete(id, user);

        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Export budget",
            description = "export budget to the Mass & Branches proposal file",
            parameters = @Parameter(
                    name = "id",
                    description = "budget id to export"
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "budget exported successfully",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_OCTET_STREAM_VALUE
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
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = DefaultErrorMessage.class)
                            )
                    )
            }
    )
    @GetMapping(value = "/{id}/export", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public ResponseEntity<byte[]> exportBudget(@PathVariable String id, Authentication authentication) {
        User user = (User) authentication.getPrincipal();

        byte[] response = service.exportBudget(id, user);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDisposition(
                ContentDisposition
                        .attachment()
                        .filename("orcamento-%s.xlsx".formatted(id))
                        .build()
        );

        return ResponseEntity.status(HttpStatus.OK).headers(headers).body(response);
    }

    @Operation(
            summary = "Get the number of the requesting user budgets",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "successfully get the number of the requesting user budgets",
                            content = @Content(
                                    schema = @Schema(example = "25", type = "number")
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
    public ResponseEntity<Integer> numberOfBudgets(Authentication authentication) {
        User user = (User) authentication.getPrincipal();

        Integer response = service.numberOfBudgets(user);

        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Find budget by id",
            parameters = @Parameter(
                    name = "id",
                    description = "budget id to be found"
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "budget found successfully",
                            content = @Content(
                                    schema = @Schema(implementation = BudgetGetResponse.class)
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
    public ResponseEntity<BudgetGetResponse> findById(Authentication authentication, @PathVariable String id) throws InterruptedException {
        User user = (User) authentication.getPrincipal();

        BudgetGetResponse response = service.findById(user, id);

        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Add item to budget",
            parameters = @Parameter(
                    name = "id",
                    description = "budget id to add item"
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "item added to budget successfully",
                            content = @Content(
                                    schema = @Schema(implementation = BudgetItemPostResponse.class)
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
                    ),
                    @ApiResponse(
                            description = "budget id or 'itemId' was not found",
                            responseCode = "404",
                            content = @Content(
                                    schema = @Schema(implementation = DefaultErrorMessage.class)
                            )
                    )
            }
    )
    @PostMapping("/{id}/items")
    public ResponseEntity<BudgetItemPostResponse> addItem(
            Authentication authentication,
            @PathVariable String id,
            @Valid @RequestBody BudgetItemPostRequest postRequest
    ) {
        User user = (User) authentication.getPrincipal();

        BudgetItemPostResponse response = service.addItem(user, id, postRequest);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(
            summary = "Remove budget item",
            parameters = {
                    @Parameter(
                            name = "id",
                            description = "budget item id to remove"
                    ),
                    @Parameter(
                            name = "budgetItemId",
                            description = "budget item id to remove"
                    )
            },
            responses = {
                    @ApiResponse(
                            responseCode = "204",
                            description = "budget item removed successfully",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "the requesting user is not authenticated",
                            content = @Content
                    ),
                    @ApiResponse(
                            description = "budget id or budget item id was not found",
                            responseCode = "404",
                            content = @Content(
                                    schema = @Schema(implementation = DefaultErrorMessage.class)
                            )
                    )
            }
    )
    @DeleteMapping("{id}/items/{budgetItemId}")
    public ResponseEntity<Void> removeItem(
            Authentication authentication,
            @PathVariable String id,
            @PathVariable Long budgetItemId
    ) {
        User user = (User) authentication.getPrincipal();

        service.removeItem(user, id, budgetItemId);

        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Add stage to budget",
            parameters = @Parameter(
                    name = "id",
                    description = "budget id to add stage"
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "stage added to budget successfully",
                            content = @Content(
                                    schema = @Schema(implementation = BudgetItemPostResponse.class)
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
                    ),
                    @ApiResponse(
                            description = "budget id was not found",
                            responseCode = "404",
                            content = @Content(
                                    schema = @Schema(implementation = DefaultErrorMessage.class)
                            )
                    )
            }
    )
    @PostMapping("/{id}/stages")
    public ResponseEntity<StagePostResponse> addStage(
            Authentication authentication,
            @PathVariable String id,
            @Valid @RequestBody StagePostRequest postRequest
    ) {
        User user = (User) authentication.getPrincipal();

        StagePostResponse response = service.addStage(user, id, postRequest);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(
            summary = "Remove stage",
            parameters = {
                    @Parameter(
                            name = "id",
                            description = "budget id to update"
                    ),
                    @Parameter(
                            name = "stageId",
                            description = "stage id to remove"
                    )
            },
            responses = {
                    @ApiResponse(
                            responseCode = "204",
                            description = "stage removed successfully",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "the requesting user is not authenticated",
                            content = @Content
                    ),
                    @ApiResponse(
                            description = "budget id or stage id was not found",
                            responseCode = "404",
                            content = @Content(
                                    schema = @Schema(implementation = DefaultErrorMessage.class)
                            )
                    )
            }
    )
    @DeleteMapping("{id}/stages/{stageId}")
    public ResponseEntity<Void> removeStage(
            Authentication authentication,
            @PathVariable String id,
            @PathVariable Long stageId
    ) {
        User user = (User) authentication.getPrincipal();

        service.removeStage(user, id, stageId);

        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "List all budget elements",
            parameters = @Parameter(
                    name = "id",
                    description = "budget id to list elements"
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "successfully list all budget elements",
                            content = @Content(
                                    array = @ArraySchema(
                                            schema = @Schema(implementation = BudgetElementGetResponse.class)
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "the requesting is not authenticated",
                            content = @Content
                    )
            }
    )
    @GetMapping("/{id}/elements")
    public ResponseEntity<List<BudgetElementGetResponse>> listAllElements(
            Authentication authentication,
            @PathVariable String id
    ) {
        User user = (User) authentication.getPrincipal();

        List<BudgetElementGetResponse> response = service.listAllElementsByBudgetId(user, id);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}

package com.mass_branches.controller;

import com.mass_branches.dto.request.ItemPostRequest;
import com.mass_branches.dto.request.ItemPutRequest;
import com.mass_branches.dto.response.ItemGetResponse;
import com.mass_branches.dto.response.ItemPostResponse;
import com.mass_branches.exception.DefaultErrorMessage;
import com.mass_branches.model.User;
import com.mass_branches.service.ItemService;
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

@Tag(name = "Items", description = "Operations with items")
@SecurityRequirement(name = "Bearer Authentication")
@RequiredArgsConstructor
@RequestMapping("/api/v1/items")
@RestController
public class ItemController {
    private final ItemService service;

    @Operation(
            summary = "Create item",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "item created successfully",
                            content = @Content(
                                    schema = @Schema(implementation = ItemPostResponse.class)
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
    public ResponseEntity<ItemPostResponse> create(Authentication authentication, @Valid @RequestBody ItemPostRequest postRequest) {
        User user = (User) authentication.getPrincipal();

        ItemPostResponse response = service.create(user, postRequest);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(
            summary = "List all the requesting user items",
            parameters = @Parameter(
                    name = "name",
                    description = "name to filter the requesting user items"
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "successfully list all the requesting user items",
                            content = @Content(
                                    array = @ArraySchema(
                                            schema = @Schema(implementation = ItemGetResponse.class)
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
    public ResponseEntity<List<ItemGetResponse>> listAllMy(
            Authentication authentication,
            @RequestParam(required = false) Optional<String> name
    ) {
        User user = (User) authentication.getPrincipal();

        List<ItemGetResponse> response = service.listAllMy(user, name);

        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Find item by id",
            parameters = @Parameter(
                    name = "id",
                    description = "item id to be found"
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "item found successfully",
                            content = @Content(
                                    schema = @Schema(implementation = ItemGetResponse.class)
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
    public ResponseEntity<ItemGetResponse> findById(Authentication authentication, @PathVariable Long id) {
        User user = (User) authentication.getPrincipal();

        ItemGetResponse response = service.findById(user, id);

        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Update item",
            parameters = @Parameter(
                    name = "id",
                    description = "item id to update"
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "204",
                            description = "item updated successfully",
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
    public ResponseEntity<Void> update(Authentication authentication, @PathVariable Long id, @Valid @RequestBody ItemPutRequest request) {
        User user = (User) authentication.getPrincipal();

        service.update(user, id, request);

        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Delete item",
            parameters = @Parameter(
                    name = "id",
                    description = "item id to delete"
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "204",
                            description = "item deleted successfully",
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
    public ResponseEntity<Void> delete(Authentication authentication, @PathVariable Long id) {
        User user = (User) authentication.getPrincipal();
        service.delete(user, id);

        return ResponseEntity.noContent().build();
    }
}

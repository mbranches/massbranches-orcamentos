package com.mass_branches.controller;

import com.mass_branches.dto.response.ConversionRateByCustomerType;
import com.mass_branches.dto.response.CustomerByCustomerRank;
import com.mass_branches.model.User;
import com.mass_branches.service.BudgetAnalyticsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;

@Tag(name = "Budget Analytics", description = "Analyses based on the requesting user budgets")
@SecurityRequirement(name = "Bearer Authentication")
@RequiredArgsConstructor
@RequestMapping("/api/v1/budgets/analytics")
@RestController
public class BudgetAnalyticsController {
    private final BudgetAnalyticsService service;

    @Operation(
            summary = "get conversion rate percentage of approved budgets of the requesting user",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "successfully returns conversion rate percentage of approved budgets of the requesting user",
                            content = @Content(schema = @Schema(example = "33.33", type = "number"))
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "the requesting user is not authenticated",
                            content = @Content
                    )
            }
    )
    @GetMapping("/conversion-rate")
    public ResponseEntity<BigDecimal> getBudgetConversionRate(Authentication authentication) {
        User user = (User) authentication.getPrincipal();

        BigDecimal response = service.getConversionRate(user);

        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "get the budgeted total value with bdi of the requesting user",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "successfully returns the budgeted total value with bdi of the requesting user",
                            content = @Content(schema = @Schema(example = "35789.59", type = "number"))
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "the requesting user is not authenticated",
                            content = @Content
                    )
            }
    )
    @GetMapping("/total")
    public ResponseEntity<BigDecimal> getTotalBudgeted(Authentication authentication) {
        User user = (User) authentication.getPrincipal();

        BigDecimal response = service.getTotalBudgeted(user);

        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "get the approved total value with bdi of the requesting user",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "successfully returns the approved total value with bdi of the requesting user",
                            content = @Content(schema = @Schema(example = "19887.00", type = "number"))
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "the requesting user is not authenticated",
                            content = @Content
                    )
            }
    )
    @GetMapping("/total-approved")
    public ResponseEntity<BigDecimal> getTotalApproved(Authentication authentication) {
        User user = (User) authentication.getPrincipal();

        BigDecimal response = service.getTotalApproved(user);

        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "get conversion rate percentage of the requesting user by customer type",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "successfully returns conversion rate percentage of the requesting user by customer type",
                            content = @Content(
                                    schema = @Schema(example = """
                                            [
                                                {
                                                    "type": "PESSOA_FISICA",
                                                    "conversionRate": 68.6
                                                },
                                                {
                                                    "type": "PESSOA_JURIDICA",
                                                    "conversionRate": 47.5
                                                }
                                            ]
                                    """)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "the requesting user is not authenticated",
                            content = @Content
                    )
            }
    )
    @GetMapping("/customers/type/conversion-rate")
    public ResponseEntity<List<ConversionRateByCustomerType>> getConversionRateByCustomerType(Authentication authentication) {
        User user = (User) authentication.getPrincipal();

        List<ConversionRateByCustomerType> response = service.getConversionRateByCustomerType(user);

        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "get top 5 users with most budgets of the requesting user",
                            content = @Content(
                                    array = @ArraySchema(
                                            schema = @Schema(implementation = CustomerByCustomerRank.class)
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
    @GetMapping("/customers/top")
    public ResponseEntity<List<CustomerByCustomerRank>> getCustomerRank(Authentication authentication) {
        User user = (User) authentication.getPrincipal();

        List<CustomerByCustomerRank> response = service.getCustomerRank(user);

        return ResponseEntity.ok(response);
    }
}

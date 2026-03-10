package gritgear.example.GritGear.controller;

import gritgear.example.GritGear.dto.retailer.RetailerRequestDTO;
import gritgear.example.GritGear.dto.retailer.RetailerResponseDTO;
import gritgear.example.GritGear.service.RetailerService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/retailer")

@Tag(name = "Retailer APIs", description = "APIs for managing retailers in the GritGear platform")

public class RetailerController {

    private final RetailerService retailerService;

    public RetailerController(RetailerService retailerService) {
        this.retailerService = retailerService;
    }

    @Operation(
            summary = "Create a retailer",
            description = "Allows ADMIN or RETAILER to create a new retailer",
            security = @SecurityRequirement(name = "bearerAuth")
    )

    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Retailer created successfully"),
            @ApiResponse(responseCode = "403", description = "Access denied")
    })

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'RETAILER')")
    public ResponseEntity<RetailerResponseDTO> createRetailer(
            @Valid @RequestBody RetailerRequestDTO dto) {

        RetailerResponseDTO created = retailerService.createRetailer(dto);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }



    @Operation(
            summary = "Get retailer by ID",
            description = "Retrieve retailer details using retailer ID",
            security = @SecurityRequirement(name = "bearerAuth")
    )

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Retailer retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Retailer not found")
    })

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER', 'RETAILER', 'ADMIN')")
    public ResponseEntity<RetailerResponseDTO> getRetailer(@PathVariable Long id) {

        RetailerResponseDTO got = retailerService.getRetailerById(id);

        if (got == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(got);
    }



    @Operation(
            summary = "Get all retailers",
            description = "Retrieve list of all retailers",
            security = @SecurityRequirement(name = "bearerAuth")
    )

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Retailers retrieved successfully")
    })

    @GetMapping
    @PreAuthorize("hasAnyRole('USER', 'RETAILER', 'ADMIN')")
    public ResponseEntity<List<RetailerResponseDTO>> getAllRetailers() {

        List<RetailerResponseDTO> retailers = retailerService.getAllRetailers();
        return ResponseEntity.ok(retailers);
    }



    @Operation(
            summary = "Update retailer",
            description = "Allows ADMIN or RETAILER to update retailer details",
            security = @SecurityRequirement(name = "bearerAuth")
    )

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Retailer updated successfully"),
            @ApiResponse(responseCode = "404", description = "Retailer not found")
    })

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('RETAILER', 'ADMIN')")
    public ResponseEntity<RetailerResponseDTO> updateRetailer(
            @PathVariable Long id,
            @Valid @RequestBody RetailerRequestDTO dto) {

        RetailerResponseDTO updated = retailerService.updateRetailer(id, dto);

        if (updated == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(updated);
    }



    @Operation(
            summary = "Delete retailer",
            description = "Allows ADMIN to delete a retailer by ID",
            security = @SecurityRequirement(name = "bearerAuth")
    )

    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Retailer deleted successfully"),
            @ApiResponse(responseCode = "403", description = "Access denied")
    })

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteRetailer(@PathVariable Long id) {

        retailerService.deleteRetailer(id);
        return ResponseEntity.noContent().build();
    }
}
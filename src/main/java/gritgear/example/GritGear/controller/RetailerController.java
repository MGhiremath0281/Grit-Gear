package gritgear.example.GritGear.controller;

import gritgear.example.GritGear.dto.retailer.RetailerRequestDTO;
import gritgear.example.GritGear.dto.retailer.RetailerResponseDTO;
import gritgear.example.GritGear.service.RetailerService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/retailer")
public class RetailerController {

    private final RetailerService retailerService;

    public RetailerController(RetailerService retailerService) {
        this.retailerService = retailerService;
    }

    @PreAuthorize("hasAnyRole('ADMIN','RETAILER')")
    @PostMapping
    public ResponseEntity<RetailerResponseDTO> createRetailer(
            @Valid @RequestBody RetailerRequestDTO dto) {

        RetailerResponseDTO created = retailerService.createRetailer(dto);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @PreAuthorize("hasAnyRole('USER','RETAILER','ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<RetailerResponseDTO> getRetailer(@PathVariable Long id) {

        RetailerResponseDTO got = retailerService.getRetailerById(id);

        if (got == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(got);
    }

    @PreAuthorize("hasAnyRole('USER','RETAILER','ADMIN')")
    @GetMapping
    public ResponseEntity<List<RetailerResponseDTO>> getAllRetailers() {

        List<RetailerResponseDTO> retailers = retailerService.getAllRetailers();
        return ResponseEntity.ok(retailers);
    }

    @PreAuthorize("hasAnyRole('RETAILER','ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<RetailerResponseDTO> updateRetailer(
            @PathVariable Long id,
            @Valid @RequestBody RetailerRequestDTO dto) {

        RetailerResponseDTO updated = retailerService.updateRetailer(id, dto);

        if (updated == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(updated);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRetailer(@PathVariable Long id) {

        retailerService.deleteRetailer(id);
        return ResponseEntity.noContent().build();
    }
}
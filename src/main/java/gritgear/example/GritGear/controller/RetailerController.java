package gritgear.example.GritGear.controller;

import gritgear.example.GritGear.dto.RetailerRequestDTO;
import gritgear.example.GritGear.dto.RetailerResponseDTO;
import gritgear.example.GritGear.model.Retailer;
import gritgear.example.GritGear.service.RetailerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/retailer")
@RequiredArgsConstructor
public class RetailerController{

    private final RetailerService retailerService;
    public RetailerController(RetailerService retailerService) {
        this.retailerService = retailerService;
    }

    // CREATE
    @PostMapping
    public ResponseEntity<RetailerResponseDTO> createRetailer(@RequestBody RetailerRequestDTO dto) {
        RetailerResponseDTO created = retailerService.createRetailer(dto);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    // GET BY ID
    @GetMapping("/{id}")
    public ResponseEntity<RetailerResponseDTO> getRetailer(@PathVariable Long id) {
        RetailerResponseDTO got = retailerService.getRetailerById(id);

        if (got == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(got);
    }

    // GET ALL
    @GetMapping
    public ResponseEntity<List<RetailerResponseDTO>> getAllRetailers() {
        List<RetailerResponseDTO> retailers = retailerService.getAllRetailers();
        return ResponseEntity.ok(retailers);
    }

    // UPDATE
    @PutMapping("/{id}")
    public ResponseEntity<RetailerResponseDTO> updateRetailer(
            @PathVariable Long id,
            @RequestBody RetailerRequestDTO dto) {

        RetailerResponseDTO updated = retailerService.updateRetailer(id, dto);

        if (updated == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(updated);
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<RetailerResponseDTO> deleteRetailer(@PathVariable Long id) {

        RetailerResponseDTO deleted = retailerService.deleteRetailer(id);

        if (deleted == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(deleted);
    }
}

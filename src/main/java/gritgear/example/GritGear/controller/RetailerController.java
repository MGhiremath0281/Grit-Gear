package gritgear.example.GritGear.controller;

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
public class RetailerController {

    private final RetailerService retailerService;

    // CREATE
    @PostMapping
    public ResponseEntity<Retailer> createRetailer(@RequestBody Retailer retailer) {
        Retailer created = retailerService.createRetailer(retailer);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    // GET BY ID
    @GetMapping("/{id}")
    public ResponseEntity<Retailer> getRetailer(@PathVariable Long id) {
        Retailer got = retailerService.getRetailerById(id);

        if (got == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(got);
    }

    // GET ALL
    @GetMapping
    public ResponseEntity<List<Retailer>> getAllRetailers() {
        List<Retailer> retailers = retailerService.getAllRetailers();
        return ResponseEntity.ok(retailers);
    }

    // UPDATE
    @PutMapping("/{id}")
    public ResponseEntity<Retailer> updateRetailer(
            @PathVariable Long id,
            @RequestBody Retailer retailer) {

        Retailer updated = retailerService.updateRetailer(id, retailer);

        if (updated == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(updated);
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<Retailer> deleteRetailer(@PathVariable Long id) {

        Retailer deleted = retailerService.deleteRetailer(id);

        if (deleted == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(deleted);
    }
}

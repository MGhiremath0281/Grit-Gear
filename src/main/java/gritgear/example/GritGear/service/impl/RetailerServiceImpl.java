package gritgear.example.GritGear.service.impl;

import gritgear.example.GritGear.dto.RetailerRequestDTO;
import gritgear.example.GritGear.dto.RetailerResponseDTO;
import gritgear.example.GritGear.model.Retailer;
import gritgear.example.GritGear.repositry.RetailerRepositry;
import gritgear.example.GritGear.service.RetailerService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RetailerServiceImpl implements RetailerService {

    private final RetailerRepositry retailerRepositry;

    public RetailerServiceImpl(RetailerRepositry retailerRepositry) {
        this.retailerRepositry = retailerRepositry;
    }

    @Override
    public RetailerResponseDTO createRetailer(RetailerRequestDTO dto) {
            Retailer retailer = new Retailer();
            retailer.setName(dto.getName());
            retailer.setPassword(dto.getPassword());
            retailer.setPhone(dto.getPhone());
            retailer.setAddress(dto.getAddress());
            retailer.setProducts(dto.getProducts());
            Retailer saved = retailerRepositry.save(retailer);
            return  mapToResponseDTO(saved);

    }

    @Override
    public RetailerResponseDTO getRetailerById(Long id) {
        return retailerRepositry.findById(id)
                .map(this::mapToResponseDTO)
                .orElse(null);
    }

    @Override
    public List<RetailerResponseDTO> getAllRetailers() {
        return retailerRepositry.findAll()
                .stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public RetailerResponseDTO updateRetailer(Long id, RetailerRequestDTO dto) {

        Retailer existing = retailerRepositry.findById(id).orElse(null);
        if (existing == null) return null;

        existing.setName(dto.getName());
        existing.setEmail(dto.getEmail());
        existing.setAddress(dto.getAddress());
        existing.setPhone(dto.getPhone());
        existing.setProducts(dto.getProducts());

       Retailer updated = retailerRepositry.save(existing);
       return mapToResponseDTO(updated);
    }

    @Override
    public RetailerResponseDTO deleteRetailer(Long id) {
        retailerRepositry.deleteById(id);
        return null;
    }


    private RetailerResponseDTO mapToResponseDTO(Retailer retailer){
        RetailerResponseDTO response = new RetailerResponseDTO();
        response.setId(retailer.getId());
        response.setName(retailer.getName());
        response.setAddress(retailer.getAddress());
        response.setEmail(retailer.getEmail());
        response.setPhone(retailer.getPhone());
        response.setProducts(retailer.getProducts();
        return response;
    }
}


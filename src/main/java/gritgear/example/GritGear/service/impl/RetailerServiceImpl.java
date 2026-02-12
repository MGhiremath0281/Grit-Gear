package gritgear.example.GritGear.service.impl;

import gritgear.example.GritGear.dto.RetailerRequestDTO;
import gritgear.example.GritGear.dto.RetailerResponseDTO;
import gritgear.example.GritGear.model.Retailer;
import gritgear.example.GritGear.repositry.RetailerRepositry;
import gritgear.example.GritGear.service.RetailerService;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RetailerServiceImpl implements RetailerService {

    private final RetailerRepositry retailerRepositry;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;


    public RetailerServiceImpl(RetailerRepositry retailerRepositry, ModelMapper modelMapper, PasswordEncoder passwordEncoder) {
        this.retailerRepositry = retailerRepositry;
        this.modelMapper = modelMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public RetailerResponseDTO createRetailer(RetailerRequestDTO dto) {

        Retailer retailer = modelMapper.map(dto, Retailer.class);
        retailer.setPassword(passwordEncoder.encode(dto.getPassword()));
        Retailer saved = retailerRepositry.save(retailer);
        return modelMapper.map(saved, RetailerResponseDTO.class);
    }


    @Override
    public RetailerResponseDTO getRetailerById(Long id) {
        return retailerRepositry.findById(id)
                .map(retailer -> modelMapper.map(retailer,RetailerResponseDTO.class))
                .orElse(null);
    }

    @Override
    public List<RetailerResponseDTO> getAllRetailers() {
        return retailerRepositry.findAll()
                .stream()
                .map(retailer -> modelMapper.map(retailer,RetailerResponseDTO.class))
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
       return modelMapper.map(updated,RetailerResponseDTO.class);
    }

    @Override
    public RetailerResponseDTO deleteRetailer(Long id) {
        retailerRepositry.deleteById(id);
        return null;
    }

}


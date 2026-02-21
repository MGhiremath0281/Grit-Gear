package gritgear.example.GritGear.service.impl;

import gritgear.example.GritGear.dto.retailer.RetailerRequestDTO;
import gritgear.example.GritGear.dto.retailer.RetailerResponseDTO;
import gritgear.example.GritGear.exception.RetailernotFoundException;
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

    public RetailerServiceImpl(RetailerRepositry retailerRepositry,
                               ModelMapper modelMapper,
                               PasswordEncoder passwordEncoder) {
        this.retailerRepositry = retailerRepositry;
        this.modelMapper = modelMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public RetailerResponseDTO createRetailer(RetailerRequestDTO dto) {

        Retailer retailer = modelMapper.map(dto, Retailer.class);
        retailer.setPassword(passwordEncoder.encode(dto.getPassword()));

        Retailer saved = retailerRepositry.save(retailer);

        return mapToResponseDTO(saved);
    }

    @Override
    public RetailerResponseDTO getRetailerById(Long id) {

        Retailer retailer = retailerRepositry.findById(id)
                .orElseThrow(() ->
                        new RetailernotFoundException("Retailer not found with id: " + id)
                );

        return mapToResponseDTO(retailer);
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

        Retailer existing = retailerRepositry.findById(id)
                .orElseThrow(() ->
                        new RetailernotFoundException("Retailer not found with id: " + id)
                );

        modelMapper.map(dto, existing);

        if (dto.getPassword() != null && !dto.getPassword().isEmpty()) {
            existing.setPassword(passwordEncoder.encode(dto.getPassword()));
        }

        Retailer updated = retailerRepositry.save(existing);

        return mapToResponseDTO(updated);
    }

    @Override
    public void deleteRetailer(Long id) {

        if (!retailerRepositry.existsById(id)) {
            throw new RetailernotFoundException("Retailer not found with id: " + id);
        }

        retailerRepositry.deleteById(id);
    }

    private RetailerResponseDTO mapToResponseDTO(Retailer retailer) {
        return modelMapper.map(retailer, RetailerResponseDTO.class);
    }
}
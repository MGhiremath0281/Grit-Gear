package gritgear.example.GritGear.service.impl;

import gritgear.example.GritGear.dto.retailer.RetailerRequestDTO;
import gritgear.example.GritGear.dto.retailer.RetailerResponseDTO;
import gritgear.example.GritGear.exception.RetailernotFoundException;
import gritgear.example.GritGear.model.Retailer;
import gritgear.example.GritGear.repositry.RetailerRepositry;
import gritgear.example.GritGear.service.RetailerService;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RetailerServiceImpl implements RetailerService {

    private final RetailerRepositry retailerRepositry;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;

    private static final Logger logger =
            LoggerFactory.getLogger(RetailerServiceImpl.class);

    public RetailerServiceImpl(RetailerRepositry retailerRepositry,
                               ModelMapper modelMapper,
                               PasswordEncoder passwordEncoder) {
        this.retailerRepositry = retailerRepositry;
        this.modelMapper = modelMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public RetailerResponseDTO createRetailer(RetailerRequestDTO dto) {

        logger.info("Creating new retailer with email: {}", dto.getEmail());

        Retailer retailer = modelMapper.map(dto, Retailer.class);
        retailer.setPassword(passwordEncoder.encode(dto.getPassword()));

        Retailer saved = retailerRepositry.save(retailer);

        logger.info("Retailer created successfully with id: {}", saved.getId());

        return mapToResponseDTO(saved);
    }

    @Override
    public RetailerResponseDTO getRetailerById(Long id) {

        logger.info("Fetching retailer with id: {}", id);

        Retailer retailer = retailerRepositry.findById(id)
                .orElseThrow(() -> {
                    logger.error("Retailer not found with id: {}", id);
                    return new RetailernotFoundException("Retailer not found with id: " + id);
                });

        logger.debug("Retailer found: {}", retailer.getEmail());

        return mapToResponseDTO(retailer);
    }

    @Override
    public List<RetailerResponseDTO> getAllRetailers() {

        logger.info("Fetching all retailers");

        List<Retailer> retailers = retailerRepositry.findAll();

        logger.debug("Total retailers found: {}", retailers.size());

        return retailers.stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public RetailerResponseDTO updateRetailer(Long id, RetailerRequestDTO dto) {

        logger.info("Updating retailer with id: {}", id);

        Retailer existing = retailerRepositry.findById(id)
                .orElseThrow(() -> {
                    logger.error("Retailer not found for update with id: {}", id);
                    return new RetailernotFoundException("Retailer not found with id: " + id);
                });

        modelMapper.map(dto, existing);

        if (dto.getPassword() != null && !dto.getPassword().isEmpty()) {
            logger.debug("Updating password for retailer id: {}", id);
            existing.setPassword(passwordEncoder.encode(dto.getPassword()));
        }

        Retailer updated = retailerRepositry.save(existing);

        logger.info("Retailer updated successfully with id: {}", id);

        return mapToResponseDTO(updated);
    }

    @Override
    public void deleteRetailer(Long id) {

        logger.info("Deleting retailer with id: {}", id);

        if (!retailerRepositry.existsById(id)) {
            logger.error("Retailer not found for deletion with id: {}", id);
            throw new RetailernotFoundException("Retailer not found with id: " + id);
        }

        retailerRepositry.deleteById(id);

        logger.info("Retailer deleted successfully with id: {}", id);
    }

    private RetailerResponseDTO mapToResponseDTO(Retailer retailer) {
        return modelMapper.map(retailer, RetailerResponseDTO.class);
    }
}
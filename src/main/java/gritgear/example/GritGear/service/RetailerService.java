package gritgear.example.GritGear.service;

import gritgear.example.GritGear.dto.retailer.RetailerRequestDTO;
import gritgear.example.GritGear.dto.retailer.RetailerResponseDTO;

import java.util.List;

public interface RetailerService {
    RetailerResponseDTO createRetailer(RetailerRequestDTO dto);
    RetailerResponseDTO getRetailerById(Long id);
    List<RetailerResponseDTO> getAllRetailers();
    RetailerResponseDTO updateRetailer(Long id,RetailerRequestDTO dto);
    public void deleteRetailer(Long id);
}

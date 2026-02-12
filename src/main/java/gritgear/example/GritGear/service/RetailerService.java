package gritgear.example.GritGear.service;

import gritgear.example.GritGear.dto.RetailerRequestDTO;
import gritgear.example.GritGear.dto.RetailerResponseDTO;
import gritgear.example.GritGear.model.Retailer;

import java.util.List;

public interface RetailerService {
    RetailerResponseDTO createRetailer(RetailerRequestDTO dto);
    RetailerResponseDTO getRetailerById(Long id);
    List<RetailerResponseDTO> getAllRetailers();
    RetailerResponseDTO updateRetailer(Long id,RetailerRequestDTO dto);
    public void deleteRetailer(Long id);
}

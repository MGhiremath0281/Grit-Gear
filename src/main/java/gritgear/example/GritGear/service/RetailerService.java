package gritgear.example.GritGear.service;

import gritgear.example.GritGear.model.Retailer;

import java.util.List;

public interface RetailerService {
    Retailer createRetailer(Retailer retailer);
    Retailer getRetailerById(Long id);
    List<Retailer> getAllRetailers();
    Retailer updateRetailer(Long id,Retailer retailer);
    void deleteRetailer(Long id);
}

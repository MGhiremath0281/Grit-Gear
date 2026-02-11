package gritgear.example.GritGear.service.impl;

import gritgear.example.GritGear.model.Retailer;
import gritgear.example.GritGear.repositry.RetailerRepositry;
import gritgear.example.GritGear.service.RetailerService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RetailerServiceImpl implements RetailerService {
    private RetailerRepositry retailerRepositry;

    @Override
    public Retailer createRetailer(Retailer retailer) {
        return retailerRepositry.save(retailer) ;
    }

    @Override
    public Retailer getRetailerById(Long id) {
        return retailerRepositry.findById(id).orElse(null);
    }

    @Override
    public List<Retailer> getAllRetailers() {
        return retailerRepositry.findAll();
    }

    @Override
    public Retailer updateRetailer(Long id, Retailer retailer) {
      Retailer existing = retailerRepositry.findById(id).orElse(null);

      if(existing == null)return null;

      existing.setName(retailer.getName());
      existing.setEmail(retailer.getEmail());
      existing.setAddress(retailer.getAddress());
      existing.setPhone(retailer.getPhone());
      existing.setProducts(retailer.getProducts());
      return retailerRepositry.save(existing);
    }

    @Override
    public void deleteRetailer(Long id) {
    retailerRepositry.deleteById(id);
    }
}

package gritgear.example.GritGear.dto;

public class OrderItemRequestDTO {
    private String produts;
    private Integer quantity;
    private Double price;

    public String getProduts() {
        return produts;
    }

    public void setProduts(String produts) {
        this.produts = produts;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }
}

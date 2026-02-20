package gritgear.example.GritGear.exception;

public class CartitemNotFoundException extends RuntimeException{
    public CartitemNotFoundException(String message){
        super(message);
    }
}

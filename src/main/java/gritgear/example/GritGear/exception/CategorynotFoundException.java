package gritgear.example.GritGear.exception;

public class CategorynotFoundException extends RuntimeException{
    public CategorynotFoundException(String message){
        super(message);
    }
}

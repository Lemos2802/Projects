package Exceptions;

public class UserAlreadyExists extends Exception {

    public UserAlreadyExists(String msg){
        super(msg);
    }

}
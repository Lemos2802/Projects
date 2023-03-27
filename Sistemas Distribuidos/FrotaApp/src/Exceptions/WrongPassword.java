package Exceptions;

public class WrongPassword extends Exception {

    public WrongPassword(String msg){
        super(msg);
    }
}

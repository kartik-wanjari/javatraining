package com.azuga.demo.exception;

public class ObjectIdNotFoundException extends Exception{
    public ObjectIdNotFoundException(String msg){
        super(msg);
    }

    public ObjectIdNotFoundException() {
    }
}

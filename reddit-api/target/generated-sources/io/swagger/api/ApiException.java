package io.swagger.api;

@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2020-05-03T18:15:52.254+05:30")

public class ApiException extends Exception{
    private int code;
    public ApiException (int code, String msg) {
        super(msg);
        this.code = code;
    }
}

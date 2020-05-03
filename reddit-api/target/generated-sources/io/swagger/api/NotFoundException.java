package io.swagger.api;

@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2020-05-03T18:15:52.254+05:30")

public class NotFoundException extends ApiException {
    private int code;
    public NotFoundException (int code, String msg) {
        super(code, msg);
        this.code = code;
    }
}

package klay.dictionary.exception;

import java.io.DataOutputStream;

public class DataFormatException extends RuntimeException {

    public DataFormatException() {
        super();
    }

    public DataFormatException(String msg) {
        super(msg);
    }
}

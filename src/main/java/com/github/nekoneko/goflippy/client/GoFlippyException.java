package com.github.nekoneko.goflippy.client;

public class GoFlippyException extends Exception {

    public GoFlippyException(String msg) {
        super(msg);
    }

    public GoFlippyException(String msg, Throwable cause) {
        super(msg, cause);
    }

}

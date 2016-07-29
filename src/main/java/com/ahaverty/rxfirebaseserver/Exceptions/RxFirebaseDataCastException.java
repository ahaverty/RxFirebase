package com.ahaverty.rxfirebaseserver.Exceptions;

public class RxFirebaseDataCastException extends Exception {

    public RxFirebaseDataCastException() {
    }

    public RxFirebaseDataCastException(String detailMessage) {
        super(detailMessage);
    }

    public RxFirebaseDataCastException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public RxFirebaseDataCastException(Throwable throwable) {
        super(throwable);
    }
}
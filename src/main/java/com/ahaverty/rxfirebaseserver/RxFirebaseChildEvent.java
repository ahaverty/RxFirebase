package com.ahaverty.rxfirebaseserver;

public class RxFirebaseChildEvent<T> {


    public enum EventType {
        ADDED,
        CHANGED,
        REMOVED,
        MOVED
    }

    private EventType eventType;
    private T value;
    private String key;
    private String previousChildName;


    public RxFirebaseChildEvent(T data, EventType eventType) {
        this.value = data;
        this.eventType = eventType;
    }

    public RxFirebaseChildEvent(T data,
                                String key,
                                EventType eventType) {
        this(data, eventType);
        this.key = key;
    }

    public RxFirebaseChildEvent(T data,
                                String key,
                                String previousChildName,
                                EventType eventType) {

        this(data, key, eventType);
        this.previousChildName = previousChildName;
    }


    
    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }

    
    public String getPreviousChildName() {
        return previousChildName;
    }

    public void setPreviousChildName(String previousChildName) {
        this.previousChildName = previousChildName;
    }

    
    public EventType getEventType() {
        return eventType;
    }

    public void setEventType(EventType eventType) {
        this.eventType = eventType;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RxFirebaseChildEvent<?> that = (RxFirebaseChildEvent<?>) o;

        if (eventType != that.eventType) return false;
        if (value != null ? !value.equals(that.value) : that.value != null) return false;
        if (key != null ? !key.equals(that.key) : that.key != null) return false;
        return previousChildName != null ? previousChildName.equals(that.previousChildName) : that.previousChildName == null;

    }

    @Override
    public int hashCode() {
        int result = eventType != null ? eventType.hashCode() : 0;
        result = 31 * result + (value != null ? value.hashCode() : 0);
        result = 31 * result + (key != null ? key.hashCode() : 0);
        result = 31 * result + (previousChildName != null ? previousChildName.hashCode() : 0);
        return result;
    }
}
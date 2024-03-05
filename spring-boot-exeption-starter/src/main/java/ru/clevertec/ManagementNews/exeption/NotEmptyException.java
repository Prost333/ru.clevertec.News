package ru.clevertec.ManagementNews.exeption;

public class NotEmptyException extends RuntimeException {

    public NotEmptyException(String message) {
        super(message);
    }
}
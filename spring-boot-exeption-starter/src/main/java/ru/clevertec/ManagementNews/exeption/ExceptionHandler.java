package ru.clevertec.ManagementNews.exeption;


import ru.clevertec.ManagementNews.model.ErrorResponse;

public interface ExceptionHandler {
    ErrorResponse handleException(Exception e);
}

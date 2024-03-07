package ru.clevertec.ManagementNews.exeption;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import ru.clevertec.ManagementNews.model.ErrorResponse;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
@ExtendWith(MockitoExtension.class)
class DefaultExceptionHandlerTest {
    @InjectMocks
    private DefaultExceptionHandler defaultExceptionHandler;

    @Test
    void handelValidatorException_ReturnsErrorResponseWithBadRequestStatusCode() {
        // Arrange
        DefaultExceptionHandler exceptionHandler = new DefaultExceptionHandler();
        MethodArgumentNotValidException exception = mock(MethodArgumentNotValidException.class);
        BindingResult bindingResult = mock(BindingResult.class);
        FieldError fieldError = new FieldError("objectName", "fieldName", "Error message");
        when(bindingResult.getAllErrors()).thenReturn(List.of(fieldError));
        when(exception.getBindingResult()).thenReturn(bindingResult);

        // Act
        ErrorResponse response = exceptionHandler.handelValidatorException(exception);

        // Assert
        assertEquals("Error message", response.getMessage());
        assertEquals(String.valueOf(HttpStatus.BAD_REQUEST.value()), response.getCode());
    }

    @Test
    void shouldReturnHandleException() {
        String message = "Entity not found!";
        EntityNotFoundException exception = new EntityNotFoundException(message);

        ErrorResponse response = defaultExceptionHandler.handleException(exception);

        assertThat(response.getCode()).isEqualTo(String.valueOf(HttpStatus.NOT_FOUND.value()) + " NOT_FOUND");
        assertThat(response.getMessage()).isEqualTo(message);
    }
}
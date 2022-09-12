package com.emmanuela.newecommerce.response;

import lombok.*;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ErrorResponse {

    private HttpStatus httpStatus;
    private String message;
    private String debugMessage;
    private LocalDateTime time = LocalDateTime.now();

    public ErrorResponse(HttpStatus httpStatus){
        this.httpStatus = httpStatus;
    }
}

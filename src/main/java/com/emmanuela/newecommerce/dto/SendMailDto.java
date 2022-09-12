package com.emmanuela.newecommerce.dto;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@RequiredArgsConstructor
public class SendMailDto {
    private String to;
    private String name;
    private String subject;
    private String body;
}

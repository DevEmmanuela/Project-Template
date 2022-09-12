package com.emmanuela.newecommerce.response;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@RequiredArgsConstructor
public class SendMailResponse {
    private String to;
    private String name;
    private String subject;
    private String body;
}

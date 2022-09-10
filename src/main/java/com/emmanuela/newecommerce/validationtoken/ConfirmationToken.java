//package com.emmanuela.newecommerce.validationtoken;
//
//import lombok.*;
//import javax.persistence.*;
//import java.time.LocalDateTime;
//
//@Getter
//@Setter
//@NoArgsConstructor
//@Entity
//public class ConfirmationToken {
//    @SequenceGenerator(
//            name = "confirmation_token",
//            sequenceName = "confirmation_token",
//            allocationSize = 1
//    )
//
//    @Id
//    @GeneratedValue(
//            strategy = GenerationType.SEQUENCE,
//            generator = "confirmation_token")
//    private Long id;
//    private String token;
//    private LocalDateTime createdAt;
//
//    private LocalDateTime confirmedAt;
//    private LocalDateTime expiresAt;
//
//    public ConfirmationToken(String token, LocalDateTime createdAt, LocalDateTime expiresAt) {
//        this.token = token;
//        this.createdAt = createdAt;
//        this.expiresAt = expiresAt;
//    }

//}

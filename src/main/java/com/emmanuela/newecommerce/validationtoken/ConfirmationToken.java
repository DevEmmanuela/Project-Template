package com.emmanuela.newecommerce.validationtoken;

import com.emmanuela.newecommerce.entities.Users;
import lombok.*;
import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class ConfirmationToken {
    @SequenceGenerator(
            name = "confirm_token_sequence",
            sequenceName = "confirm_token_sequence",
            allocationSize = 1
    )

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "confirm_token_sequence")
    private Long id;
    @Column(nullable = false)
    private String token;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    private LocalDateTime confirmedAt;
    @Column(nullable = false)
    private LocalDateTime expiresAt;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private Users users;

    public ConfirmationToken(String token, LocalDateTime createdAt, LocalDateTime expiresAt, Users users) {
        this.token = token;
        this.createdAt = createdAt;
        this.expiresAt = expiresAt;
        this.users = users;
    }

}

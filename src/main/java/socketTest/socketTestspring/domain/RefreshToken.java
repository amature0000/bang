package socketTest.socketTestspring.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String refreshToken;

    @NotBlank
    @Column(unique = true)
    private String memberId;

    public RefreshToken(String token, String id){
        this.refreshToken = token;
        this.memberId = id;
    }
    public RefreshToken updateToken(String token){
        this.refreshToken = token;
        return this;
    }
}

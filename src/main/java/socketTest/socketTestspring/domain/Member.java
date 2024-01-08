package socketTest.socketTestspring.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;
import socketTest.socketTestspring.dto.MemberDto;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String userEmail;
    private String userPassword;
    private String userNickname;

    public static Member toEntity(MemberDto memberDto){
        return Member.builder()
                .userEmail(memberDto.getUserEmail())
                .userPassword(memberDto.getUserPassword())
                .userNickname(memberDto.getUserNickname())
                .build();
    }

}
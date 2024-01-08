package socketTest.socketTestspring.dto;

import lombok.*;
import socketTest.socketTestspring.domain.Member;

@Getter
@Setter
@Builder

public class MemberDto {
    private Long id;
    private String userEmail;
    private String userPassword;
    private String userNickname;

    public static MemberDto toDto(Member entity){
        return MemberDto.builder()
                .id(entity.getId())
                .userEmail(entity.getUserEmail())
                .userPassword(entity.getUserPassword())
                .userNickname(entity.getUserNickname())
                .build();
    }
}
package socketTest.socketTestspring.domain;

//인증 Service 생성하여 로그인한 유저에게 Role 부여하는 기능 구현. 현재 모든 유저들은 ROLE_NOT_PERMITTED 로 등록됨
public enum MemberRole {
    ROLE_NOT_PERMITTED, ROLE_USER, ROLE_ADMIN
}

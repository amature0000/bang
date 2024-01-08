package socketTest.socketTestspring.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import socketTest.socketTestspring.domain.Member;
import socketTest.socketTestspring.dto.LoginDto;
import socketTest.socketTestspring.dto.MemberDto;
import socketTest.socketTestspring.repository.MemberRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {
    @Autowired
    MemberRepository memberRepository;

    public MemberDto createNewMember(MemberDto memberDto){
        Member member = Member.toEntity(memberDto);
        return MemberDto.toDto(memberRepository.save(member)); //log 확인 위해서 reMemberDto 생성 후 리턴
    }


    public MemberDto findById(Long id){
        Optional<Member> byId = memberRepository.findById(id);
        return byId.map(MemberDto::toDto).orElse(null);
    }

    public Long login(LoginDto loginDto){
        Optional<Member> ByEmailAndPassword = memberRepository.findByUserEmailAndUserPassword(loginDto.getEmail(),loginDto.getPassword());
        return ByEmailAndPassword.map(Member::getId).orElse(null);
    }

}
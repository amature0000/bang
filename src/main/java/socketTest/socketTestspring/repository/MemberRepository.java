package socketTest.socketTestspring.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import socketTest.socketTestspring.domain.Member;

import java.util.Optional;

@Repository

public interface MemberRepository extends JpaRepository<Member, Long>{
    Optional<Member> findByUserEmailAndUserPassword(String userEmail, String userPassword);
}
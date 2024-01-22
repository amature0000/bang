package socketTest.socketTestspring.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
public class MemberInfo {

    private String memberId;

    @Override
    public int hashCode() {
        return Objects.hash(memberId);
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MemberInfo memberInfo = (MemberInfo) o;
        return Objects.equals(memberId, memberInfo.memberId);
    }

    public MemberInfo(String memberId) {
        this.memberId = memberId;
    }

    private int hands;
    //etc...
}

package akrnote.core.service;

import akrnote.core.member.Member;

public interface MemberService {
    void join(Member member);
    Member findMember(Long memberId);
}

package akrnote.core.service;

import akrnote.core.Repository.MemoryMemberRepository;
import akrnote.core.member.Member;
import akrnote.core.member.MemberRepository;

public class MemberServiceImplements implements MemberService{

    private final MemberRepository memberRepository = new MemoryMemberRepository();

    @Override
    public void join(Member member) {
        memberRepository.save(member);
    }

    @Override
    public Member findMember(Long memberId) {
        return memberRepository.findById(memberId);
    }
}

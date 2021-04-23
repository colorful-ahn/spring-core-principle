package akrnote.core.service;

import akrnote.core.member.Member;
import akrnote.core.member.MemberRepository;

public class MemberServiceImplements implements MemberService{

    private final MemberRepository memberRepository;

    public MemberServiceImplements(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Override
    public void join(Member member) {
        memberRepository.save(member);
    }

    @Override
    public Member findMember(Long memberId) {
        return memberRepository.findById(memberId);
    }

    public MemberRepository getMemberRepository(){
        return memberRepository;
    }
}

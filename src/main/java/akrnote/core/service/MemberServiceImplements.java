package akrnote.core.service;

import akrnote.core.member.Member;
import akrnote.core.member.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MemberServiceImplements implements MemberService{

    private final MemberRepository memberRepository;

    @Autowired //생성자에 wire 해줌
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

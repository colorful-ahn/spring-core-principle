package akrnote.core.member;

import akrnote.core.service.MemberService;
import akrnote.core.service.MemberServiceImplements;

public class MemberApp {
    public static void main(String[] args) {
        MemberService memberService = new MemberServiceImplements();
        Member member = new Member(1L, "memberA", Grade.VIP);
        memberService.join(member);

        Member findmember = memberService.findMember(1L);
        System.out.println("new mem : "+member.getName());

        System.out.println("find Mem : "+ findmember.getName());
    }
}

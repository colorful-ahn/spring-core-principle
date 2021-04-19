package akrnote.core.Member;

import akrnote.core.AppConfig;
import akrnote.core.member.Grade;
import akrnote.core.member.Member;
import akrnote.core.service.MemberService;
import akrnote.core.service.MemberServiceImplements;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class MemberServiceTest {
    
    MemberService memberService;

    @BeforeEach
    public void beforeEach(){
        AppConfig appConfig = new AppConfig();
        memberService = appConfig.memberService();
    }
    
    @Test
    void join(){
        //given
        Member member= new Member(1L,"memberA", Grade.VIP);
        //when
        memberService.join(member);
        Member findMember = memberService.findMember(1L);

        //then
        Assertions.assertThat(member).isEqualTo(findMember);
    }

}

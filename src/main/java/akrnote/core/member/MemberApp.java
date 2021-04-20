package akrnote.core.member;

import akrnote.core.AppConfig;
import akrnote.core.service.MemberService;
import akrnote.core.service.MemberServiceImplements;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class MemberApp {
    public static void main(String[] args) {
//        AppConfig appConfig = new AppConfig();
//        MemberService memberService = appConfig.memberService();

        ApplicationContext applicationContext = new AnnotationConfigApplicationContext(AppConfig.class);
        MemberService memberService = applicationContext.getBean("memberService", MemberService.class);

        Member member = new Member(1L, "memberA", Grade.VIP);
        memberService.join(member);

        Member findmember = memberService.findMember(1L);
        System.out.println("new mem : "+member.getName());

        System.out.println("find Mem : "+ findmember.getName());
    }
}

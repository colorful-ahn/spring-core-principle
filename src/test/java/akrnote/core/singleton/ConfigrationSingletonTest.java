package akrnote.core.singleton;

import akrnote.core.AppConfig;
import akrnote.core.Order.OrderServiceImpl;
import akrnote.core.member.MemberRepository;
import akrnote.core.service.MemberService;
import akrnote.core.service.MemberServiceImplements;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class ConfigrationSingletonTest {

    @Test
    void configurationTest(){
        ApplicationContext ac = new AnnotationConfigApplicationContext(AppConfig.class);
        MemberServiceImplements memberService = ac.getBean("memberService", MemberServiceImplements.class);
        OrderServiceImpl orderService = ac.getBean("orderService",OrderServiceImpl.class);

        MemberRepository memberRepository1 = memberService.getMemberRepository();
        MemberRepository memberRepository2 = orderService.getMemberRepository();

        System.out.println("memberRepository1 = " +memberRepository1);
        System.out.println("memberRepository2 = "+memberRepository2);

    }

    @Test
    void configurationDeep(){
        ApplicationContext ac = new AnnotationConfigApplicationContext(AppConfig.class);
        AppConfig bean = ac.getBean(AppConfig.class);

    }
}

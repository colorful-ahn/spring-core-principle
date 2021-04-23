package akrnote.core.singleton;

import akrnote.core.AppConfig;
import akrnote.core.service.MemberService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class SingletonTest {

    @Test
    @DisplayName("POJO")
    void pureContainer(){
        AppConfig appConfig = new AppConfig();
        //1. 조회 : 호출 할때 마다 객체를 생성
        MemberService memberService1 = appConfig.memberService();
        //2. 조회 : 호출 할 때 마다 객체생성
        MemberService memberService2 = appConfig.memberService();

        //chk ref val
        System.out.println("memberService1 : " +memberService1);
        System.out.println("memberService2 : " +memberService2);

        Assertions.assertThat(memberService1).isNotSameAs(memberService2);

    }
    @Test
    @DisplayName("singleton pattern")
    void singletonServiceTest(){
        SingletonService singletonService1 = SingletonService.getInstance();
        SingletonService singletonService2 = SingletonService.getInstance();

        Assertions.assertThat(singletonService1).isSameAs(singletonService2);
        //same == 객체 인스턴스
        //equal 자바의 equals

    }

    @Test
    @DisplayName("spring container and singleton")
    void springContainer(){
        AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(AppConfig.class);
        MemberService memberService1 = ac.getBean("memberService", MemberService.class);
        MemberService memberService2 = ac.getBean("memberService", MemberService.class);

        Assertions.assertThat(memberService1).isSameAs(memberService2);


    }
}

package akrnote.core.beanfind;

import akrnote.core.AppConfig;
import akrnote.core.service.MemberService;
import akrnote.core.service.MemberServiceImplements;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class ApplicationContextBasicFindTest {

    AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(AppConfig.class);

    @Test
    @DisplayName("빈 이름으로 조회")
    void findBeanByName(){
        MemberService memberService = ac.getBean("memberService",MemberService.class);
        Assertions.assertThat(memberService).isInstanceOf(MemberServiceImplements.class);
    }

    @Test
    @DisplayName("이름 없이 타입으로만 조회")
    void findBeanByType(){
        MemberService memberService = ac.getBean(MemberService.class);
        Assertions.assertThat(memberService).isInstanceOf(MemberServiceImplements.class);
    }

    @Test
    @DisplayName("구체 타입으로 조회")
    void findBeanByName2(){
        MemberServiceImplements memberService = ac.getBean("memberService",MemberServiceImplements.class);
        Assertions.assertThat(memberService).isInstanceOf(MemberServiceImplements.class);
    }

    @Test
    @DisplayName("빈 이름으로 조회 X")
    void findBeanByNameX(){
        //ac.getBean("xxxxx",MEmberService,class);
        //MemberService xxxxx = ac.getBean("xxxxx",MemberService.class);
        org.junit.jupiter.api.Assertions.assertThrows(NoSuchBeanDefinitionException.class,
                () -> ac.getBean("xxxxx",MemberService.class));
    }
}

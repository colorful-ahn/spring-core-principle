package akrnote.core;

import akrnote.core.Order.OrderService;
import akrnote.core.Order.OrderServiceImpl;
import akrnote.core.Repository.MemoryMemberRepository;
import akrnote.core.discount.DiscountPolicy;
import akrnote.core.discount.FixDiscountPolicy;
import akrnote.core.member.MemberRepository;
import akrnote.core.service.MemberService;
import akrnote.core.service.MemberServiceImplements;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
//POJO로 구현한 간단한 IOC, DI 컨테이너

@Configuration
public class AppConfig {

    @Bean
    public MemberRepository memberRepository(){
        return new MemoryMemberRepository();
    }
    @Bean
    public DiscountPolicy discountPolicy(){
        return new FixDiscountPolicy();
    }
    @Bean
    public MemberService memberService(){
        return new MemberServiceImplements(memberRepository());
    }
    @Bean
    public OrderService orderService(){
        return new OrderServiceImpl(memberRepository(),discountPolicy());
    }
}

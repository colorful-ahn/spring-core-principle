package akrnote.core.Order;

import akrnote.core.annotation.MainDiscountPolicy;
import akrnote.core.discount.DiscountPolicy;
import akrnote.core.member.Member;
import akrnote.core.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
//@RequiredArgsConstructor  //final이 붙은 건 필수 값이 되기에 생성자를 생성해줌
public class OrderServiceImpl implements OrderService{
    
    private final MemberRepository memberRepository;
    private final DiscountPolicy discountPolicy;

    @Autowired
    public OrderServiceImpl(MemberRepository memberRepository, @MainDiscountPolicy DiscountPolicy discountPolicy) {
        this.memberRepository = memberRepository;
        this.discountPolicy = discountPolicy;
    }

    //private final DiscountPolicy discountPolicy = new FixDiscountPolicy();
    @Override
    public Order createOrder(Long memberId, String itemName, int itemPrice) {
        Member member = memberRepository.findById(memberId);
        int discountPrice = discountPolicy.discount(member, itemPrice);

        return  new Order(memberId, itemName, itemPrice,discountPrice);

    }
    public MemberRepository getMemberRepository(){
        return memberRepository;
    }
}

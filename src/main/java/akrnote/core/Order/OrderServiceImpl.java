package akrnote.core.Order;

import akrnote.core.Repository.MemoryMemberRepository;
import akrnote.core.discount.DiscountPolicy;
import akrnote.core.discount.FixDiscountPolicy;
import akrnote.core.member.Member;
import akrnote.core.member.MemberRepository;

public class OrderServiceImpl implements OrderService{
    
    private final MemberRepository memberRepository = new MemoryMemberRepository();
    private final DiscountPolicy discountPolicy = new FixDiscountPolicy();
    
    @Override
    public Order createOrder(Long memberId, String itemName, int itemPrice) {
        Member member = memberRepository.findById(memberId);
        int discountPrice = discountPolicy.discount(member, itemPrice);

        return  new Order(memberId, itemName, itemPrice,discountPrice);

    }
}

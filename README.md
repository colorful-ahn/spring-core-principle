스프링의 핵심 개념을 코드로 학습해보자.
---------

기본 핵심 개념
----------
SOLID 원칙
1. SCP(단일 원칙 책임)
>한 클래스는 하나의 책임만을 가져야 한다.  
>코드의 변경이 있을 때 파급 효과가 적어야한다.  
>즉 "객체" 라는 개념을 잘 이용해야 이 원칙을 지킬 수 있다.
2. OCP(개방-폐쇄 원칙)
>확장에는 열려있으나 변경에는 닫혀 있어야 한다.  
>즉, 다형성. 
>>다만 Client가 직접 다형성을 이용하여 변경을 하다보면 OCP가 깨진다.  
>>Ioc, DI 등 Spring의 기능을 백번 활용해야 한다.  
>>직접 OCP를 준수하기 위해 POJO를 구현하는 경우 배보다 배꼽이 더 커질 수 있다.  
3. LSP(리스코프 치환 원칙)
>인터페이스가 있고 구현체가 있다고 하였을 때 구현체는 인터페이스의 규약을 지켜줘야 한다.  
>예를 들어, 인터페이스에서 특정 버튼 클릭 시 count가 1씩 증가해달라고 하였는데 구현체에서 -1을 시킨다면 이는 원칙 위반에 해당된다.
>또 자동차 엑셀을 밟으면 앞으로 가야한다고 명시가 되어있는데 구현체에서 뒤로가게 만들었다면 이 또한 위반이다.  
4. ISP(인터페이스 분리 원칙)
>특정 Client를 위한 인터페이스가 좋다.  
>인터페이스가 자동차일 경우 단순히 자동차 안에 운전, 정비 등을 다 넣는게 아니다.  
>인터페이스를 운전자만들 위한 인터페이스 정비사 만을 위한 인터페이스 등 으로 나누어 놓는 것이 좋다.  
>Interface가 명확해지며, 대체 가능성 또한 높아진다.
5. DIP(의존 관계 역전 원칙)
>추상화에 의존 해야지 구현체에 의존하면 안된다.
>반드시 의존은 Interface만!

요약
----------
객체 지향의 핵심은 다형성이다. 다만 다형성 만으로는 구현 객체를 변경할 때 client 코드 또한 변경될 수 밖에 없다.
즉, 다형성 만으로는 OCP, DIP 원칙을 지킬 수 없다.

POJO 코드이다.  

public class MemberServiceImplements implements MemberService{

    private final MemberRepository memberRepository = new MemoryMemberRepository();

    @Override
    public void join(Member member) {
        memberRepository.save(member);
    }

    @Override
    public Member findMember(Long memberId) {
        return memberRepository.findById(memberId);
    }
}  


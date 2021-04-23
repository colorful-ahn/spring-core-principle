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

OPP
----------
객체 지향의 핵심은 다형성이다. 다만 다형성 만으로는 구현 객체를 변경할 때 client 코드 또한 변경될 수 밖에 없다.
즉, 다형성 만으로는 OCP, DIP 원칙을 지킬 수 없다.

POJO 코드이다.  

```java

    private final MemberRepository memberRepository = new MemoryMemberRepository();

    @Override
    public void join(Member member) {
        memberRepository.save(member);
    }

    @Override
    public Member findMember(Long memberId) {
        return memberRepository.findById(memberId);
    } 
```

이 코드를 보았을 때 memberRepository는 인터페이스인 MemberRepository에 의존한다. 하지만 그 객체는 MemoryMemberRepository이다.  즉, 리포지토리 객체 변경 시 반드시 객체 생서 부분에 코드 변화가 일어나게 된다.  이는 OCP, DIP에 위배된다.

스프링 기반 코드 변경
----------
이를 해결하기 위해 Spring Framework에서는 Ioc컨테이너, DI컨테이너를 제공한다.  
AppConfig.java 코드를 통해 이를 알 수 있다.  
```java
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
```
AppConfig를 통해 각 구현 객체들을 빈에 등록한다. 이를 통해 얻을 수 있는 장점은 다음과 같다.  
>이제는 그 어떤 구현체도 다른 구현체를 의존하지 않는다. 즉 인터페이스만을 의존하게 된다.  
>이러한 빈들의 관리는 프레임워크가 모두 지원해준다.  

```java
private final MemberRepository memberRepository;

    public MemberServiceImplements(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Override
    public void join(Member member) {
        memberRepository.save(member);
    }

    @Override
    public Member findMember(Long memberId) {
        return memberRepository.findById(memberId);
    }
```
이 코드는 위에 적었던 POJO 코드를 스프링 기반으로 변경시킨 것이다. memberRepository는 생성자에 의해 구현체가 정해지게 된다. 이때 이 코드 즉, 구현체는 memberRepository에 어떤 구현체가 들어오던지에 관계없이 자신만의 로직을 수행할 수 있다. 다른 구현체에 의존하지 않기 때문이다. 이를 통해 우리는 OCP와 DIP 원칙을 지킬 수 있게 된다.  

싱글톤 컨테이너
------------
싱글톤 이란?  
>만약 다수의 클라이언트가 서비스에 대해 다중 요청을 보낼 경우 객체가 무수히 많이 생산되게 된다.  
>이는 메모리 낭비가 굉장히 심하다. POJO로는 이러한 문제를 막기 위해 다음과 같은 코드 형식을 제안한다.  
```java
private static final SingletonService instance = new SingletonService();

    public static SingletonService getInstance(){
        return instance;
    }
private SingletonService{}
```  
Static 영역에 객체를 딱 1개만 생성해 둔 뒤 public get method를 통해 인스턴스에 접근한다.  
또한 생성자를 private로 선언하여 외부에서 new 키워드를 사용하지 못하게 막아준다.  
이 경우 객체를 딱 1개만 생성하기 때문에 효율적이라고 볼 수 있다.  
다만 코드 자체가 너무 길어지고 테스트하기도 어려운 데다 클라이언트가 구체 클래스에 의존하기 때문에 DIP, OCP등의 원칙에 위배된다.  
**Spring은 이에 대한 간단하고 명료한 답을 제시한다.**  
스프링에 bean으로 등록하는 행위 자체가 싱글톤이 된다.  
다만 이는 @Autowired 혹은 @Configuration이 있을 때 보장 된다.  
```java
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
```  
이 코드를 보았을 때 memberRepository는 2번 호출 된다.  
이때 스프링 싱글톤 컨테이너는 @Configuration을 통해 내부의 로직을 통해 해당 Bean을 싱글톤으로 관리한다.  
만약 @Configuration을 선언해 주지 않는 다면 싱글톤을 보장할 수 없다.  
**주의 사항으로 Spring Bean은 항상 Stateless하게 설계해야 한다.**  
공유 필드에 대해 stateful하다면 어떤 문제가 생길 지 아무도 보장할 수 없다.  
묻지도 따지지도 말고 항상 stateless 하게 설계하자.  

Component 스캔과 의존관계 자동 주입
-----------------------------
스프링은 자동으로 빈을 등록하고 서로의 의존 관계를 주입 해준다. 다음 코드는 Scan을 사용하기 전과 후 코드이다.
```java
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
```
```java
@ComponentScan
public class AutoAppConfig {

}
```
이 처럼 Config코드가 매우매우 간결해진다.  
Scan은 기본적으로 모든 프로젝트를 둘러보며 컴포넌트들을 찾는다. (CoreApplication에 설정이 되어있기 때문이다.)  
다만 @ComponentScan(...) 을 통해 스프링이 검색을 하는 시발점, 위치, 제외할 클래스 등등 여러모로 설정을 해줄 수 가 있다.  
원래 @Bean으로 등록해 주었던 컴포넌트 들은 다음과 같이 선언을 하여 스프링에게 Bean임을 인식시킨다.  
```java
@Component
public class MemberServiceImplements implements MemberService{

    private final MemberRepository memberRepository;

    @Autowired //생성자에 wire 해줌
    public MemberServiceImplements(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }
}
```
**@Component라는 선언을 해주면 스프링에서 빈임을 인식하고 이를 등록한다.**    
또한 의존관계는 @Autowired라는 어노테이션을 통해 이어지는데 이는 형식에 맞는 애들을 자동으로 주입해 주는 것이다.  
>Member Repository 인터페이스를 상속받는 구현 객체를 주입받음.  
만약, 구현 객체가 중복이 된다면 에러가 발생하게 된다.  
직접 @Bean을 넣어 객체를 생성할 수도 있다. 이 경우 이름이 똑같다면 우선권은 **수동**이 가지게 된다.  
**다만 협업 개발의 경우 수동으로 지정할 경우 혼란을 야기할 수 있기때문에 되도록 지양하자**  


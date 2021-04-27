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

의존관계 자동 주입
-------------
의존관계 주입은 크게 4가지가 있다. 
>생성자 주입  
>수정자 주입  
>필드 주입  
>메서드 주입  

생성자 주입은 constructor를 이용하여 의존 관계를 주입 받는다.  
생성자 호출 시점에 딱 1번만 호출 되는 것이 보장된다.  
때문에 **불변,필수** 의존관계에 사용하는 것이 바람직하다.  
  
수정자 주입은 setter method를 통해 이루어 진다.  
**선택, 변경**이 있을 시 사용 된다.  
다만 일반적으로 중간에 bean을 교체하는 것은 많이 이루어지는 상황이 아니기때문에 권장되지 않는다.  
또한 변경이 가능하다는 특성때문에 다른 곳에서 호출 시 매우 조심해야 한다.  
  
필드, 메서드 주입은 거의 사용되지 않는다.  

대체로 Spring framework는 Bean의 불변성에 focusing을 가지기 때문에 생성자 주입을 권장 한다.  
특히, setter나 필드 생성자의 경우 POJO 환경에서는 spring의 DI container가 역할을 하지 못하기 때문에 Unit TEST시 매우 부적합하다.  
또한 생성자 주입은 final 키워드 라는 강력한 무기를 가지게 된다.  
반드시 값이 설정되야 하기 때문에 NPE를 막아 준다는 장점이 있다.  
다음은 final과 생성자 주입을 보여준 좋은 코드이다. 
```java
@Component
public class OrderServiceImpl implements OrderService{
    
    private final MemberRepository memberRepository;
    private final DiscountPolicy discountPolicy;

    @Autowired
    public OrderServiceImpl(MemberRepository memberRepository, DiscountPolicy discountPolicy) {
        this.memberRepository = memberRepository;
        this.discountPolicy = discountPolicy;
    }
```  

이러한 final과 생성자 주입이 권장됨에 따라 라이브러리 중 lombok이 사용되는 추세이다.  
이 라이브러리는 final이 붙은 필드를 모아 생성자를 자동으로 만들어 준다.  
```java
@Component
  @RequiredArgsConstructor
  public class OrderServiceImpl implements OrderService {
      private final MemberRepository memberRepository;
      private final DiscountPolicy discountPolicy;
}
```  
이 코드에는 생성자가 없지만 lombok을 통해 생성자가 만들어 진다.  
이는 프로젝트 out 폴더 classes에서 확인 할 수 있다.
```java
@Autowired
    public OrderServiceImpl(MemberRepository memberRepository, @MainDiscountPolicy DiscountPolicy discountPolicy) {
        this.memberRepository = memberRepository;
        this.discountPolicy = discountPolicy;
    }
```
이렇듯 자동으로 구현되어 있다!  
  
프로젝트 중 조회 할 수 있는 Bean이 2개 이상일 경우  
특수한 경우이지만 때에따라 DB를 바꿔쓰거나 고객의 할인정책 선택 등등 다형성을 따르되 Bean으로 중복 등록을 해놓아야 하는 상황이 있다.  
이때 아무런 조치도 취해주지 않는 다면 spring은 **NoUniqueBeanDefinitionException**을 발생 시킨다. 이를 해결하기 위한 방법은 2가지가 있다.  
**첫번째는 @Qualifier의 사용이다.** 이는 빈의 이름을 바꾸는 것이 아니라 별도의 식별자를 생성하는 것이다.  
**두번째는 @Primary이다.** 이는 단순히 이 어노테이션이 붙은 컴포넌트가 우위에 있다는 것을 의미한다.  
다만 @Qualifier를 구현 객체 코드에 사용 시 name이 string이기 때문에 오타가 있더라도 complie 오류를 발생시키지 않는다.  
따라서 이때는 별도의 @Annotation을 생성해줘 오타 시 compile error가 발생하게 조작해주자.  
  
대체로 spring은 현재 모든 Bean을 불변, 자동 으로 설정하기를 권장한다. 또한 이 방법이 유지,보수 또한 매우 간편하다.  
다만, 수동 빈 등록이 그렇다고 배제될 수 는 없다. 이 이야기를 하기 전에 2가지 개념을 짚고 넘어가야 한다.  
**업무 로직 빈** : 웹을 지원하는 컨트롤러, 핵심 비즈니스 로직이 있는 서비스, 데이터 계층의 로직을 처리하는 리포지토리등이 모두 업무 로직이다. 이러한 요구사항은 개발할 때 추가되거나 변경된다.  
**기술 지원 빈** : 기술적인 문제나 AOP를 처리할 때 주로 사용된다. 데이터베이스 연결이나, 공통 로그 처리 처럼 업무 로직을 지원하기 위한 하부 기술이나 공통 기술들이다.  
업무 로직은 그 양이 방대하지만 컨트롤러, 서비스, 리포지토리 등 어느정도 유사한 패턴이 있다.  
때문에 자동 기능을 적극 사용하는 것이 좋다.  
기술 지원 로직은 양은 적지만 어플리케이션 전반에 걸쳐 광범위 하게 영향을 미친다.  또 이러한 로직이 모두 잘 적용이 되는지 파악이 어렵다.  
따라서 기술 지원 로직들은 수동 빈 등록을 사용하여 명확하게 드러내는 것이 좋다.  
**즉, 편리한 자동 기능을 기본적으로 사용하되 유지, 보수, 인수인계 시 이해가 쉽게 다형성을 적극 활용하는 비즈니스 로직들은 수동 등록을 고민해 보아야 하며 직접 등록하는 기술 지원 객체는 수동 등록을 이용하도록 하자. 
많이 변하거나 변할 거 같으면 수동 등록을 해서 보기 쉽게 만들자!**

Bean 생명주기 콜백
---------------
Bean은 어떠한 라이프 사이클을 가지고 있을까?  
**스프링컨테이너생성-> 스프링빈생성-> 의존관계주입-> 초기화콜백-> 사용-> 소멸전콜백-> 스프링 종료**  
이때 개발자는 초기화 콜백을 통해 객체가 잘 생성되고 의존 주입 또한 잘 이루어 졌는지 또 소멸 전 콜백을 통해 객체가 안전하게 소멸됐는 지 알 필요가 있다. 
따라서 스프링은 이에대한 간단한 해결책을 지원한다. 
어떠한 코드에서 객체를 구현하였다.
```java 
@Bean
      public NetworkClient networkClient() {
          NetworkClient networkClient = new NetworkClient();
          networkClient.setUrl("http://hello-spring.dev");
          return networkClient;
}
```  
이때 객체 코드에 
```java 
@PostConstruct
    public void init() {
System.out.println("NetworkClient.init"); connect();
call("초기화 연결 메시지");
}
    @PreDestroy
    public void close() {
        System.out.println("NetworkClient.close");
        disConnect();
    }
```  
이 2가지 어노테이션이 있다면 각각 초기화 콜백, 소멸 전 콜백으로 작동하게 된다.  
다만 이러한 방법은 외부 라이브러리일 경우 사용하기 매우 힘들다.  
라이브러리 객체의 코드를 바꿀 수는 없기 때문이다.  
이때는
```java  
@Bean(initMethod = "init", destroyMethod = "close")
      public NetworkClient networkClient() {
          NetworkClient networkClient = new NetworkClient();
          networkClient.setUrl("http://hello-spring.dev");
          return networkClient;
}
```  
Bean에 직접 init과 destroy를 설정해 줌으로서 2가지 콜백 함수를 실행할 수 있다.  

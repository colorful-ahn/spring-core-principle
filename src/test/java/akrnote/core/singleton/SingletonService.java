package akrnote.core.singleton;

public class SingletonService {
    //static 영역 공부하기
    private static final SingletonService instance = new SingletonService();

    public static SingletonService getInstance(){
        return instance;
    }

    private SingletonService(){
    }
    public void logic(){
        System.out.println("생글톤 객체 로직 호출"+instance);
    }
}

package wang.yuqi.springsecurityyuqi.bean;

public record Greeting(Long id, String name) {
    public static Greeting getInstance(Long id, String name) {
        synchronized (Greeting.class) {
            return new Greeting(id, name);
        }
    }
}

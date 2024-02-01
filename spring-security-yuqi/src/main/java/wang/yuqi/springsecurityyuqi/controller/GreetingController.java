package wang.yuqi.springsecurityyuqi.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import wang.yuqi.springsecurityyuqi.bean.Greeting;

import java.util.concurrent.atomic.AtomicLong;

@RestController
@RequestMapping("/api/v1/greeting")
public class GreetingController {
    private final AtomicLong counter = new AtomicLong();

    @GetMapping()
    public ResponseEntity<String> sayHello(String name){
        String template = String.format("Hello %s!",name==null?"world":name);
        return ResponseEntity.ok(template);
    }

    @GetMapping("/sayBye")
    public ResponseEntity<String> sayBye(String name){
        String template = String.format("Bye %s!",name);
        return ResponseEntity.ok(template);
    }

    @GetMapping("/sayHelloJson")
    public Greeting sayHelloJson(@RequestParam("name") String name){
        String template = String.format("Hello %s!",name==null?"world":name);
        //return new Greeting(counter.incrementAndGet(),template);
       return Greeting.getInstance(counter.incrementAndGet(), template);
    }
}

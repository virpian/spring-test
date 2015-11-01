package hello;

import java.util.concurrent.atomic.AtomicLong;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.*;

@RestController
public class GreetingController {

    private static final String template = "Hello, %s!";
    private final AtomicLong counter = new AtomicLong();

    @RequestMapping("/greeting")
    @ApiOperation(value="Apik greetuings",notes = "notatka", response=Greetings.class)
    public Greetings greeting(@RequestParam(value="name", defaultValue="World") String name) {
        Greetings g = new Greetings(counter.incrementAndGet(),
                            String.format(template, name));
        g.add(linkTo(methodOn(GreetingController.class).greeting(name)).withSelfRel());
        return g;
        
    }
}
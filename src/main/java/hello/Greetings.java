package hello;

import org.springframework.hateoas.ResourceSupport;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Greetings extends ResourceSupport {

    private final long id;
    private final String content;

    @JsonCreator
    public Greetings(@JsonProperty("id") long id, @JsonProperty("content") String content) {
        this.id = id;
        this.content = content;
    }

    public long getIdentity() {
        return id;
    }

    public String getContent() {
        return content;
    }
}
package test;

import com.example.jeemigrationdemo.Greeting;
import com.github.t1.testcontainers.jee.JeeContainer;
import com.example.jeemigrationdemo.XmlMessageBodyReaderWriter;
import org.eclipse.microprofile.rest.client.RestClientBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import jakarta.json.JsonObject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;

import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;
import static jakarta.ws.rs.core.MediaType.APPLICATION_XML;
import static jakarta.ws.rs.core.MediaType.TEXT_PLAIN;
import static org.assertj.core.api.BDDAssertions.then;

@Testcontainers
class GreetingsIT {
    @Container static JeeContainer CONTAINER = JeeContainer.create("wildfly:27.0.1.Final-jdk17")
        .withDeployment("target/ROOT.war");

    @Path("/greetings")
    public interface Api {
        @GET @Produces(TEXT_PLAIN)
        String textGreeting();

        @GET @Produces(APPLICATION_JSON)
        JsonObject jsonObjectGreeting();

        @GET @Produces(APPLICATION_JSON)
        Greeting jsonGreeting();

        @GET @Produces(APPLICATION_XML)
        Greeting xmlGreeting();
    }

    Api api;

    @BeforeEach
    void setUp() {
        api = RestClientBuilder.newBuilder()
            .baseUri(CONTAINER.baseUri())
            .register(XmlMessageBodyReaderWriter.class)
            .build(Api.class);
    }

    @Test
    void shouldGetTextGreeting() {
        var greeting = api.textGreeting();

        then(greeting).isEqualTo("Hello, World!");
    }

    @Test
    void shouldGetJsonObjectGreeting() {
        var greeting = api.jsonObjectGreeting();

        then(greeting.getString("hello")).isEqualTo("Hello");
        then(greeting.getString("who")).isEqualTo("World");
    }

    @Test
    void shouldGetJsonGreeting() {
        var greeting = api.jsonGreeting();

        then(greeting.getHello()).isEqualTo("Hello");
        then(greeting.getWho()).isEqualTo("World");
    }

    @Test
    void shouldGetXmlGreeting() {
        var greeting = api.xmlGreeting();

        then(greeting.getHello()).isEqualTo("Hello");
        then(greeting.getWho()).isEqualTo("World");
    }
}

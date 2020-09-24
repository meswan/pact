package io.openliberty.guides.consumingrest;

import au.com.dius.pact.consumer.dsl.PactDslWithProvider;
import au.com.dius.pact.consumer.junit.PactProviderRule;
import au.com.dius.pact.consumer.junit.PactVerification;
import au.com.dius.pact.core.model.RequestResponsePact;
import au.com.dius.pact.core.model.annotations.Pact;
import org.junit.Rule;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class PactTest {
    Consumer consumer;

    @Rule
    public PactProviderRule mockProvider = new PactProviderRule("Provider", this);

//    @Pact(consumer = "Consumer")
//    public RequestResponsePact createPact(PactDslWithProvider builder) {
//        Map<String, String> headers = new HashMap<String, String>();
//        headers.put("Content-Type", "application/json;charset=UTF-8");
//
//        return builder
//                .given("")
//                .uponReceiving("Pact JVM example Pact interaction")
//                .path("/artists/total")
////                .query("")
//                .method("GET")
//                .willRespondWith()
//                .headers(headers)
//                .status(200)
//                .body("3")
//                .toPact();
//    }

    @Pact(consumer = "Consumer")
    public RequestResponsePact createPact(PactDslWithProvider builder) {
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("Content-Type", "application/json");

        return builder
                .given("")
                .uponReceiving("a request for artists")
                .path("/artists")
//                .query("")
                .method("GET")
                .willRespondWith()
                .headers(headers)
                .status(200)
                .body("[{\"name\":\"foo\",\"albums\":[{\"title\":\"album_one\",\"artist\":\"foo\",\"ntracks\":12},{\"title\":\"album_two\",\"artist\":\"foo\",\"ntracks\":15}]},{\"name\":\"bar\",\"albums\":[{\"title\":\"foo walks into a bar\",\"artist\":\"bar\",\"ntracks\":12}]},{\"name\":\"dj\",\"albums\":[]}]")
                .toPact();
    }

    @Test
    @PactVerification("Provider")
    public void runTest() {

        Consumer consumer = new Consumer(mockProvider.getUrl());
        String result = consumer.getArtists();
//        String result = consumer.getTotal();
//        String body = "[{\\\"name\\\":\\\"foo\\\",\\\"albums\\\":[{\\\"title\\\":\\\"album_one\\\",\\\"artist\\\":\\\"foo\\\",\\\"ntracks\\\":12},{\\\"title\\\":\\\"album_two\\\",\\\"artist\\\":\\\"foo\\\",\\\"ntracks\\\":15}]},{\\\"name\\\":\\\"bar\\\",\\\"albums\\\":[{\\\"title\\\":\\\"foo walks into a bar\\\",\\\"artist\\\":\\\"bar\\\",\\\"ntracks\\\":12}]},{\\\"name\\\":\\\"dj\\\",\\\"albums\\\":[]}]";
//        assertEquals(result, "3");
    }
}

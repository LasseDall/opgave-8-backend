package com.example.semster3thirdapi.api;

import com.example.semster3thirdapi.dto.AgeResponse;
import com.example.semster3thirdapi.dto.CombinedResponse;
import com.example.semster3thirdapi.dto.CountryResponse;
import com.example.semster3thirdapi.dto.GenderResponse;
import com.example.semster3thirdapi.entity.Age;
import com.example.semster3thirdapi.entity.Country;
import com.example.semster3thirdapi.entity.Gender;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Controller;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class RemoteApiTester implements CommandLineRunner {

  private Mono<String> callSlowEndpoint(){
    Mono<String> slowResponse = WebClient.create()
        .get()
        .uri("http://localhost:8080/random-string-slow")
        .retrieve()
        .bodyToMono(String.class)
        .doOnError(e-> System.out.println("UUUPS : "+e.getMessage()));
    return slowResponse;
  }

  public void callSlowEndpointBlocking(){
    long start = System.currentTimeMillis();
    List<String> ramdomStrings = new ArrayList<>();

    Mono<String> slowResponse = callSlowEndpoint();
    ramdomStrings.add(slowResponse.block()); //Three seconds spent

    slowResponse = callSlowEndpoint();
    ramdomStrings.add(slowResponse.block());//Three seconds spent

    slowResponse = callSlowEndpoint();
    ramdomStrings.add(slowResponse.block());//Three seconds spent
    long end = System.currentTimeMillis();
    ramdomStrings. add(0,"Time spent BLOCKING (ms): "+(end-start));

    System.out.println(ramdomStrings.stream().collect(Collectors.joining(",")));
  }

  public void callSlowEndpointNonBlocking(){
    long start = System.currentTimeMillis();
    Mono<String> sr1 = callSlowEndpoint();
    Mono<String> sr2 = callSlowEndpoint();
    Mono<String> sr3 = callSlowEndpoint();

    var rs = Mono.zip(sr1,sr2,sr3).map(t-> {
      List<String> randomStrings = new ArrayList<>();
      randomStrings.add(t.getT1());
      randomStrings.add(t.getT2());
      randomStrings.add(t.getT3());
      long end = System.currentTimeMillis();
      randomStrings.add(0,"Time spent NON-BLOCKING (ms): "+(end-start));
      return randomStrings;
    });
    List<String> randoms = rs.block(); //We only block when all the three Mono's has fulfilled
    System.out.println(randoms.stream().collect(Collectors.joining(",")));
  }

  Mono<Gender> getGenderForName(String name) {
    WebClient client = WebClient.create();
    Mono<Gender> gender = client.get()
        .uri("https://api.genderize.io?name="+name)
        .retrieve()
        .bodyToMono(Gender.class);
    return gender;
  }

  Mono<Age> getAgeForName(String name) {
    WebClient client = WebClient.create();
    Mono<Age> age = client.get()
        .uri("https://api.agify.io?name="+name)
        .retrieve()
        .bodyToMono(Age.class);
    return age;
  }

  Mono<Country> getCountryForName(String name) {
    WebClient client = WebClient.create();
    Mono<Country> country = client.get()
        .uri("https://api.nationalize.io?name="+name)
        .retrieve()
        .bodyToMono(JsonNode.class)
        .map(json -> {
          String countryID = json.get("country").get(0).get("country_id").asText();
          double probability = json.get("country").get(0).get("probability").asDouble();
          return new Country(countryID, name, probability);
        });
    return country;
  }

  List<String> names = Arrays.asList("lars", "peter", "sanne", "kim", "david", "maja");


  public void getGendersBlocking() {
    long start = System.currentTimeMillis();
    List<Gender> genders = names.stream().map(name -> getGenderForName(name).block()).toList();
    long end = System.currentTimeMillis();
    System.out.println("Time for six external requests, BLOCKING: "+ (end-start));
  }

  public void getGendersNonBlocking() {
    long start = System.currentTimeMillis();
    var genders = names.stream().map(name -> getGenderForName(name)).toList();
    Flux<Gender> flux = Flux.merge(Flux.concat(genders));
    List<Gender> res = flux.collectList().block();
    long end = System.currentTimeMillis();
    System.out.println("Time for six external requests, NON-BLOCKING: "+ (end-start) + res);
  }

  public CombinedResponse getCombinedNonBlocking(String name) {
    Mono<Age> age = getAgeForName(name);
    Mono<Country> country = getCountryForName(name);
    Mono<Gender> gender = getGenderForName(name);
    Mono<CombinedResponse> combinedResponse = Mono.zip(age, country, gender)
        .map(tuple -> {
          Age ageResult = tuple.getT1();
          Country countryResult = tuple.getT2();
          Gender genderResult = tuple.getT3();
          return new CombinedResponse(name, genderResult.getGender(), genderResult.getProbability(), ageResult.getAge(), ageResult.getCount(), countryResult.getCountry(), countryResult.getProbability());
        });
    CombinedResponse res = combinedResponse.block();
    return res;
  }






  @Override
  public void run(String... args) throws Exception {
    System.out.println(getCombinedNonBlocking("lasse"));

  }
}


package com.example.semster3thirdapi.api;

import com.example.semster3thirdapi.dto.CombinedResponse;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
public class DemoController {

  RemoteApiTester remoteApiTester;

  DemoController(RemoteApiTester remoteApiTester) {
    this.remoteApiTester = remoteApiTester;
  }
  private final int SLEEP_TIME = 1000*3;

  @GetMapping(value = "/random-string-slow")
  public String slowEndpoint() throws InterruptedException {
    Thread.sleep(SLEEP_TIME);
    return RandomStringUtils.randomAlphanumeric(10);
  }

  @GetMapping("/api/{name}")
  public CombinedResponse endpoint(@PathVariable("name") String name) {
    return remoteApiTester.getCombinedNonBlocking(name);
  }

}

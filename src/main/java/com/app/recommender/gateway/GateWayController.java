package com.app.recommender.gateway;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GateWayController {

    @Autowired
    DiscoveryClient discoveryClient;

    @GetMapping(value = "/test")
    public ResponseEntity testGateway() {
        return ResponseEntity.status(200).body("ciaone");
    }

//    @GetMapping(value = "/physicalactivities/presentation", produces = MediaType.APPLICATION_JSON_VALUE)
//    public ResponseEntity getPhysicalActivitiesPresentation(@RequestParam(value = "userId") String userId) throws ExecutionException, InterruptedException {

//        Optional<ServiceInstance> dietMicroservice = discoveryClient.getInstances("diet-microservice").stream()
//                .findAny();
//        Optional<ServiceInstance> userMicroservice = discoveryClient.getInstances("registrations-microservice").stream()
//                .findAny();
//        CompletableFuture<Diet> dietCompletableFuture = new CompletableFuture<>();
//        CompletableFuture<List<FoodRdf>> rdfsFuture = new CompletableFuture<>();
//
//
//
////      CompletableFuture<List<FoodRdf>> result = dietCompletableFuture.thenApply(diet -> {
////
////      })
//        if (dietMicroservice.isPresent()) {
//
//            ServiceInstance dietMicroserviceInstance = dietMicroservice.get();
//            RestTemplate dietClient = new RestTemplate();
//
//            HttpHeaders headers = new HttpHeaders();
//            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
//
//            String uri = "http://localhost:" + dietMicroserviceInstance.getPort() + "/current?userId=" + userId;
//
//            HttpEntity<String> entity = new HttpEntity<>("parameters", headers);
//
//            ResponseEntity<Diet> response = dietClient.exchange(uri, HttpMethod.GET, entity, Diet.class);
//
//            dietCompletableFuture.complete(response.getBody());
//        }
//        if (userMicroservice.isPresent()) {
//            ServiceInstance userMicroserviceInstance = userMicroservice.get();
//            RestTemplate dietClient = new RestTemplate();
//            HttpHeaders headers = new HttpHeaders();
//            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
//
//            HttpEntity<String> entity = new HttpEntity<>("parameters", headers);
//
//            String uri = "http://localhost:" + userMicroserviceInstance.getPort() + "/registrations/" + userId;
//            ResponseEntity<User> response = dietClient.exchange(uri, HttpMethod.GET, entity, User.class);
//
//
//            rdfsFuture.complete(response.getBody());
//        }
//        if (result.get().isEmpty()) {
//            return ResponseEntity.status(400).body("Error");
//
//        }
//        return ResponseEntity.status(200).body(result.get());

//    }


}

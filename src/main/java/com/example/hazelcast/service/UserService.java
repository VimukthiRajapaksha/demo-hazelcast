package com.example.hazelcast.service;

import com.example.hazelcast.domain.UserDTO;
import com.hazelcast.core.HazelcastInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class UserService {

    private static final String CACHE_NAME = "demo-hazelcast-cached-users";
    private final WebClient webClient;
    private final HazelcastInstance hazelcast;

    @Autowired
    UserService(WebClient webClient, HazelcastInstance hazelcast) {
        this.webClient = webClient;
        this.hazelcast = hazelcast;
    }

    public List<UserDTO> getAllUsers() {
        Map<String, List<UserDTO>> cacheMap = hazelcast.getMap("demo-hazelcast-distributed-map");
        List<UserDTO> cachedUsers = cacheMap.get(CACHE_NAME);

        if (cachedUsers != null && !cachedUsers.isEmpty()) {
            System.out.println("Data found in cache. Get data from cache");
            return cachedUsers;
        }

        try {
            TimeUnit.SECONDS.sleep(3);
        } catch (InterruptedException e) {
            e.printStackTrace();
            Thread.currentThread().interrupt();
        }

        System.out.println("Fetching data from /users API");
        List<UserDTO> users = this.webClient
                .method(HttpMethod.GET)
                .uri("/users")
                .retrieve()
                .bodyToFlux(UserDTO.class)
                .collectList()
                .block();

        System.out.println("Data not present in cache. Add data to cache");
        cacheMap.put(CACHE_NAME, users);

        return users;
    }
}

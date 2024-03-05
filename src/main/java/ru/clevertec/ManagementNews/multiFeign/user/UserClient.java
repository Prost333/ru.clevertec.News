package ru.clevertec.ManagementNews.multiFeign.user;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;


@FeignClient(name = "user-service", url = "localhost:8081")
public interface UserClient {
    @GetMapping("/users/{userId}")
    User getUser(@PathVariable Long userId);

    @RequestMapping(method = RequestMethod.GET, value = "/users/usersDetails/{username}")
    UserDetails loadUserByUsername(@PathVariable("username") String username);

    @GetMapping("/auth/getBody")
    String  getDto();

    @GetMapping("/auth/getEntity")
    User getEntity();


}

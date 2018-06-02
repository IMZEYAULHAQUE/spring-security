package com.ilm.learning.mvcsecurity.controller;

import com.ilm.learning.mvcsecurity.model.User;
import com.ilm.learning.mvcsecurity.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ResolvableType;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationTrustResolver;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class UserController {

   @Autowired
   private OAuth2AuthorizedClientService authorizedClientService;

   private static String authorizationRequestBaseUri = "oauth2/authorization";

   @Autowired
   private ClientRegistrationRepository clientRegistrationRepository;

   @Autowired
   private AuthenticationTrustResolver authenticationTrustResolver;

   @Autowired
   private UserService userService;


   @GetMapping(value = "/login")
   public String login(/*@RequestParam(name = "username", required = true) String username, @RequestParam(name = "password", required = true) String password, */Model model) {

      if (authenticationTrustResolver.isAnonymous(SecurityContextHolder.getContext().getAuthentication())) {
         return "login";
      } else {
         return "redirect:/users";
      }
   }

   @GetMapping(value = "/oauth-login")
   public String oauthLogin(Model model) {

      if (authenticationTrustResolver.isAnonymous(SecurityContextHolder.getContext().getAuthentication())) {

         Map<String, String> oauth2AuthenticationUrls = new HashMap<>();
         Iterable<ClientRegistration> clientRegistrations = null;
         ResolvableType type = ResolvableType.forInstance(clientRegistrationRepository).as(Iterable.class);
         if (type != ResolvableType.NONE && ClientRegistration.class.isAssignableFrom(type.resolveGenerics()[0])) {
            clientRegistrations = (Iterable<ClientRegistration>) clientRegistrationRepository;
         }

         clientRegistrations.forEach(registration -> oauth2AuthenticationUrls.put(registration.getClientName(), authorizationRequestBaseUri + "/" + registration.getRegistrationId()));
         model.addAttribute("urls", oauth2AuthenticationUrls);

         return "login";
      } else {
         return "redirect:/users";
      }
   }

   @PostMapping("/loginFailed")
   public String loginFailed(@RequestParam(name = "username", required = false) String username, Model model) {
      model.addAttribute("username", username);
      model.addAttribute("accessDeniedError", "Login Failed. Invalid Username and/or Password. Try again");
      return "redirect:/login?error";
   }

   @GetMapping("/accessDenied")
   public String accessDenied(@RequestParam(name = "username", required = false) String username, Model model) {
      model.addAttribute("username", username);
      model.addAttribute("accessDeniedError", "Access Denied. Invalid Username and/or Password. Try again");
      return "accessDenied";
   }

   @GetMapping(value = "/")
   public String index(Model model, OAuth2AuthenticationToken authentication) {
      OAuth2AuthorizedClient authorizedClient = this.getAuthorizedClient(authentication);
      model.addAttribute("userName", authentication.getName());
      model.addAttribute("clientName", authorizedClient.getClientRegistration().getClientName());
      return "redirect:/login";
   }



   @GetMapping(value = "/home")
   public String hello(@RequestParam(name = "name", defaultValue = "There", required = false) String name, Model model) {
      model.addAttribute("name", name);

      if (authenticationTrustResolver.isAnonymous(SecurityContextHolder.getContext().getAuthentication())) {
         return "home";
      } else {
         return "redirect:/users";
      }

   }

   /*@PostMapping("/logout") // not request as cofiguration done in SpringConfiguration.java
   public String logout(Model model) {
      return "/login";
   }*/


   @RequestMapping(value = "/logout", method = RequestMethod.GET)
   public String logoutPage(HttpServletRequest request, HttpServletResponse response) {
      Authentication auth = SecurityContextHolder.getContext().getAuthentication();
      if (auth != null) {
         new SecurityContextLogoutHandler().logout(request, response, auth);
      }
      return "redirect:/login?logout";//You can redirect wherever you want, but generally it's a good practice to show login screen again.
   }


   @PostMapping("/dashboard")
   public String dashboard(Model model) {

      return "/dashboard";
   }

   @GetMapping("/users/createUser")
   public String usersTest(Model model) {

      return "/createUser";
   }

   @GetMapping("/users/user/{userId}")
   public String getUser(@PathVariable Long userId, Model model) {

      return "/showUserDetail";
   }

   @GetMapping("/users")
   public String users(Model model) {
      return "users";
   }

   //@GetMapping(value = "/login/oauth2/code/google") // this is the url, google transfer after successful authentication
   @GetMapping(value = "/login/oauth2/dashboard-outh") // customized in config file
   public String googleOAuthSuccessfulHandler(Model model, OAuth2AuthenticationToken authentication) {

      OAuth2AuthorizedClient authorizedClient = this.getAuthorizedClient(authentication);
      model.addAttribute("userName", authentication.getName());
      model.addAttribute("clientName", authorizedClient.getClientRegistration().getClientName());

      // send a request to the client’s user info endpoint and retrieve the userAttributes Map:
      String userInfoEndpointUri = authorizedClient.getClientRegistration().getProviderDetails().getUserInfoEndpoint().getUri();

      if (!StringUtils.isEmpty(userInfoEndpointUri)) {
         RestTemplate restTemplate = new RestTemplate();
         HttpHeaders headers = new HttpHeaders();
         headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + authorizedClient.getAccessToken().getTokenValue());
         HttpEntity entity = new HttpEntity("", headers);
         ResponseEntity<Map> response = restTemplate.exchange(userInfoEndpointUri, HttpMethod.GET, entity, Map.class);
         Map userAttributes = response.getBody();
         model.addAttribute("oauthUser", userAttributes.get("name"));
         model.addAttribute("userAttributes", userAttributes);
      }
      return "dashboard";

   }

   @PostMapping("/oauth-loginFailed")
   public String oauthAuthenicationFailure(Model model, OAuth2AuthenticationToken authentication) {
      model.addAttribute("accessDeniedError", "Login Failed. Invalid Username and/or Password. Try again");
      OAuth2AuthorizedClient authorizedClient = this.getAuthorizedClient(authentication);
      model.addAttribute("userName", authentication.getName());
      model.addAttribute("clientName", authorizedClient.getClientRegistration().getClientName());
      return "redirect:/login?error";
   }

   private OAuth2AuthorizedClient getAuthorizedClient(OAuth2AuthenticationToken authentication) {
      return this.authorizedClientService.loadAuthorizedClient(authentication.getAuthorizedClientRegistrationId(), authentication.getName());
   }
}

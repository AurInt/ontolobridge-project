package edu.miami.schurer.ontolobridge.Controllers;

import edu.miami.schurer.ontolobridge.Responses.JwtResponse;
import edu.miami.schurer.ontolobridge.models.Detail;
import edu.miami.schurer.ontolobridge.models.Role;
import edu.miami.schurer.ontolobridge.models.RoleName;
import edu.miami.schurer.ontolobridge.models.User;
import edu.miami.schurer.ontolobridge.utilities.*;
import io.sentry.Sentry;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import lombok.Data;
import org.hibernate.exception.ConstraintViolationException;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.time.Instant;
import java.util.*;

import static edu.miami.schurer.ontolobridge.utilities.DbUtil.genRandomString;

// AuthController class is encompassing all subsequent potential actions within in, for ex. login, register, verify, etc.
// TODO Break down classes, register, verify, etc by @GetMapping paths

@Data
class LoginDetails{
    private String email;
    private String password;
}

// TASK: Switch to OAuth2, following practice https://spring.io/guides/tutorials/spring-boot-oauth2
//@Configuration
//@EnableWebSecurity
//class WebSecurity extends WebSecurityConfigurerAdapter{
//    @Bean
//    public AuthenticationManager getAuthenticationManager() throws Exception {
//        return super.authenticationManagerBean();
//    }
//}

@RestController
@RequestMapping("/auth")
public class AuthController extends BaseController {

    @GetMapping("/user")
    public Map<String, Object> user(@AuthenticationPrincipal OAuth2User principal) {
        return Collections.singletonMap("name", principal.getAttribute("name"));
    }
    @GetMapping("/unauthenticated")
    public String unauthenticated() {
        return "redirect:/?error=true";
    }

    @GetMapping("/error")
    public String error() {
        return "redirect:/?error=true";
    }

    @GetMapping("/logout")
    public String logout() {
        return "redirect:/?logout=true";
    }

    @GetMapping("/login")
    public String login() {
        return "redirect:/?login=true";
    }

    @GetMapping("/oauth2/authorization/google")
    public String google() {
        return "redirect:/?login=true";
    }

    @GetMapping("/oauth2/authorization/github")
    public String github() {
        return "redirect:/?login=true";
    }

//    @Autowired
//    AuthenticationManager authenticationManager;

//    private OntoloUserDetailsService userService;

//    OntoloSecurityService securityService;
//
//
//    PasswordEncoder encoder;

//    @Autowired
//    JwtProvider jwtProvider;

//    @ExceptionHandler(ConstraintViolationException.class)
//    public ResponseEntity<Object> duplicateEmailException(HttpServletRequest req, ConstraintViolationException e) {
//        return RestResponseExceptionHandler.generateResponse(400,"Error Processing Requests:"+e.getMessage(),HttpStatus.UNAUTHORIZED);
//    }

    //Redirect the root request to swagger page
//    @PostMapping(path = "/register", produces = {"application/json"})
//    public Object register(@Parameter(name = "Email for user") @RequestParam(value = "email", defaultValue = "") String email,
//                           @Parameter(name = "Name for user") @RequestParam(value = "name", defaultValue = "") String name,
//                           @Parameter(name = "password") @RequestParam(value = "password", defaultValue = "") String password,
//                           @Parameter(name = "Anonymize Email") @RequestParam(value = "anon", defaultValue = "false") boolean anonymize) throws OntoloException {
//
//        if (userService.emailExists(email)) {
//            return RestResponseExceptionHandler.generateResponse(400, "Email already in use\r\n", HttpStatus.UNAUTHORIZED);
//        }
//
//        // Creating user's account
//        User user = new User(name,
//                email, password); //encode their password
//
//        String vCode = genRandomString(10);
//        HashSet<Detail> details = new HashSet<>();
//        details.add(new Detail("verification", vCode));
//        user.setDetails(details);//add an email verification field
//
//        HashMap<String, Object> emailVariables = new HashMap<>();
//        emailVariables.put("verification", vCode);
//        emailVariables.put("linkVerification", this.frontendURL + "/verify?verify=" + vCode);
//        emailVariables.put("user_name", name);
//        Integer notificationID = notLib.InsertEmail(JDBCTemplate, "/emails/verificationTemplate.email", "Verification Email", email, emailVariables);
//
//        try {
//            userService.saveUser(user);
//        } catch (jakarta.validation.ConstraintViolationException e) {
//            notLib.RemoveNotification(JDBCTemplate, notificationID); //We didn't register the user, remove the notification
//            StringBuilder output = new StringBuilder();
//            output.append("Error Processing Requests:\r\n");
//            for (ConstraintViolation c : e.getConstraintViolations()) {
//                output.append(c.getPropertyPath());
//                output.append(c.getMessage());
//                output.append("\r\n");
//            }
//            return RestResponseExceptionHandler.generateResponse(400, output.toString(), HttpStatus.UNAUTHORIZED);
//        } catch (jakarta.validation.UnexpectedTypeException e) {
//            notLib.RemoveNotification(JDBCTemplate, notificationID); //We didn't register the user, remove the notification
//            StringBuilder output = new StringBuilder();
//            Sentry.captureException(e);
//            output.append("Error Processing Requests:\r\n");
//            return RestResponseExceptionHandler.generateResponse(400, output.toString(), HttpStatus.UNAUTHORIZED);
//        }
//
//        return formatResultsWithoutCount("User registered successfully!");
//    }
//    @GetMapping(path="/verify", produces={"application/json"})
//    public Object verify(@Parameter(name = "Value used for Email Verification") @RequestParam(value="verify",defaultValue = "")@NotBlank String verify ) throws OntoloException {
//        Integer id = 0;
//        try {
//            id = auth.VerifyEmail(verify); //verify the verification code
//        }catch(EmptyResultDataAccessException e){
//            throw new OntoloException("Code not Found",HttpStatus.BAD_REQUEST); //code not found throw error
//        }
//        Optional<User> u = userService.findByUserId(Long.valueOf(id));
//        if(!u.isPresent()){
//            throw new OntoloException("User not found",HttpStatus.BAD_REQUEST); //user not found, throw error. SHOULD NEVER HAPPEN
//        }
//        User user = u.get();
//        Set<Detail> details = user.getDetails();
//        details.removeIf(d -> d.getField().equals("verification")); //remove the verification from the user
//        user.setDetails(details); //save details
//
//        HashSet<Role> roles = new HashSet<>(); //add user role now that user is verified
//        Role verifiedRole = roleRepository.findByName(RoleName.ROLE_VERIFIED)
//                .orElseThrow(() -> new OntoloException(" User Role not found."));
//        roles.add(verifiedRole);
//        user.setRoles(roles); //give the new user the role of user
//
//        userService.saveUser(user); //save user
//        return formatResultsWithoutCount("Email Verified");
//    }

//    @PostMapping(path="/login", produces={"application/json"})
//    public ResponseEntity<String> authenticateUser(@RequestBody LoginDetails loginDetails)
//            throws OntoloException{
//
//        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(loginDetails.getEmail(), loginDetails.getPassword());
//        //Sentry.capture(email+";"+password);
//        Authentication authentication = authenticationManager.authenticate(token);               //request to test login against login table
//
//        if(!hasRole(authentication,RoleName.ROLE_VERIFIED))
//            throw new OntoloException("Email not verified",6,HttpStatus.BAD_REQUEST).DoNotLog(); //code not found throw error
//        SecurityContextHolder.getContext().setAuthentication(authentication);
//
//        String jwt = jwtProvider.generateJwtToken(authentication);      // Uses jwtProvider to manually create a jwt token within JwtProvider; TODO replace with standard jwtUtil method
//        return ResponseEntity.ok(new JwtResponse(jwt, authentication.getName(),((UserPrinciple) authentication.getPrincipal()).getId()));
//    }



//    @GetMapping(path="/checkJWTToken", produces={"application/json"})
//    @PreAuthorize("isAuthenticated()")
//    public Object checkJWTToken(){
//        if(!securityService.isRegistered(SecurityContextHolder.getContext().getAuthentication())){
//            Map<String,Object> responseBody = new HashMap<>();
//            responseBody.put("error",5);
//            responseBody.put("message","Registration not complete");
//            responseBody.put("timestamp", Instant.now());
//            //return a server error
//            return new ResponseEntity<Object>(responseBody,HttpStatus.OK);
//        }
//        return true;
//    }


//    @GetMapping(path="/getAllDetails", produces={"application/json"})
//    public Object AllDetails(){
//        return formatResults(auth.GetAllDetails());
//    }
//    @RequestMapping(path="/retrieveMissingDetails", method= RequestMethod.GET, produces={"application/json"})
//    @PreAuthorize("isAuthenticated()")
//    //@Operation(value = "", authorizations = { @Authorization(value="jwtToken") })
//    public Object CheckUserDetails(){
//        User user =  userService.findByUserId(((UserPrinciple)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId()).get();
//        Set<Detail> details = user.getDetails();
//        List<Map<String,Object>> requiredDetails = auth.GetAllDetails();
//        List<Map<String,Object>> missingDetails = new ArrayList<>(requiredDetails);
//        for(Map<String,Object> m: requiredDetails){
//            for(Detail d:details){
//                if(m.get("field").equals(d.getField()) && Integer.parseInt(m.get("required").toString()) == 1){
//                    missingDetails.remove(m);
//                }
//            }
//            if(Integer.parseInt(m.get("required").toString()) != 1){
//                missingDetails.remove(m);
//            }
//        }
//        return formatResults(missingDetails);
//    }
}
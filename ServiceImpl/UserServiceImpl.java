

package com.cafe.cafe_management.ServiceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.cafe.cafe_management.Config.CostumUserDetailsService;
import com.cafe.cafe_management.Constants.CafeConstants;
import com.cafe.cafe_management.Filter.JwtFilter;
import com.cafe.cafe_management.JWT.JwtUtil;
import com.cafe.cafe_management.Model.User;
import com.cafe.cafe_management.Repo.UserRepo;
import com.cafe.cafe_management.Service.UserService;
import com.cafe.cafe_management.Wrapper.UserWrapper;
import com.cafe.cafe_management.utils.CafeUtils;
import com.cafe.cafe_management.utils.EmailUtils;
import com.google.common.base.Strings;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepo userRepo;

    @Autowired
    AuthenticationManager  authenticationManager;

    @Autowired
    CostumUserDetailsService costumUserDetailsService;

    @Autowired
    JwtUtil jwtUtil;
    @Autowired
    JwtFilter  jwtFilter;
    @Autowired
    EmailUtils  emailUtils;

    @Override
    public ResponseEntity<String> signUp(Map<String, String> requestMap) {

        log.info("Inside signup{}", requestMap);
        try {
            if (validatesSignUpMap(requestMap)) {
                User user = userRepo.findByEmailId(requestMap.get("email"));
                if (Objects.isNull(user)) {
                    userRepo.save(getUserFromMap(requestMap));
                    return CafeUtils.getResponseEntity("Successfully Registerd", HttpStatus.OK);
                } else {
                    return CafeUtils.getResponseEntity("Emails already exists", HttpStatus.BAD_REQUEST);
                }

            } else {
                return CafeUtils.getResponseEntity(CafeConstants.INVALID_DATA, HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);

    }

    private boolean validatesSignUpMap(Map<String, String> requestMap) {
        if (requestMap.containsKey("name") && requestMap.containsKey("contactnumber")
                && requestMap.containsKey("email") && requestMap.containsKey("password")) {
            return true;
        }
        return false;
    }

    private User getUserFromMap(Map<String, String> requestMap) {
        User user = new User();
        user.setName(requestMap.get("name"));
        user.setContactNumber(requestMap.get("contactNumber"));
        user.setEmail(requestMap.get("email"));
        user.setPassword(requestMap.get("password"));
        user.setStatus(false);
        user.setRole("user");

        return user;

    }

    @Override
    public ResponseEntity<Object> login(Map<String, String> requestMap) {
       log.info("Inside login");
       try{
        Authentication auth = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                requestMap.get("email"),requestMap.get("password"))
                );



                // if (auth.isAuthenticated()) {
                //     if(costumUserDetailsService.getUserDetail().getStatus().equals("true")){
                //         return new ResponseEntity<String>("{\"token\":\""+
                //         jwtUtil.generateToken(costumUserDetailsService.getUserDetail().getEmail(), costumUserDetailsService.getUserDetail()
                //         .getRole()) + "\"}" , HttpStatus.OK);
                //     }
                    
                // }

                if (auth.isAuthenticated()) {
                    if(costumUserDetailsService.getUserDetail()
                    .getStatus().equals("true")){
                        return new ResponseEntity<Object>("{\"token\":\""+
                        jwtUtil.generateToken(costumUserDetailsService.getUserDetail().getEmail(),
                         costumUserDetailsService.getUserDetail()
                         .getRole()) + "\"}",HttpStatus.OK);
                    

                }


                // if (auth.isAuthenticated()) {
                //     if (costumUserDetailsService.getUserDetail().getStatus().equals(auth)) {
                //         String email = costumUserDetailsService.getUserDetail().getEmail();
                //         String role = costumUserDetailsService.getUserDetail().getRole();
                //         String token = jwtUtil.generateToken(email, role);
                        
                //         // Construct the response JSON
                //         String jsonResponse = "{\"token\":\"" + token + "\"}";
                        
                //         // Return the ResponseEntity with the JSON response and HTTP status OK
                //         return ResponseEntity.ok(jsonResponse);
                //     }
                // }
                }else{
                    return new ResponseEntity<Object>("{\"message\":\""+"wait for admin approval."+"\"}",HttpStatus.BAD_REQUEST);
                }
                
                // Return an error response if the conditions are not met
                // return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("wait for admin approval");
                
       }catch(Exception ex){
        log.error("{}", ex);

       }
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("wait for admin approval");
        
       }

    @Override
    public ResponseEntity<List<UserWrapper>> getAllUser() {
        try {
            if(jwtFilter.isAdmin()){
                // return  new ResponseEntity<>(userService.findAll
                return  new ResponseEntity<>(userRepo.getAllUser() , HttpStatus.OK);
            }else{
                return new ResponseEntity<>(new ArrayList<>(),HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return  new ResponseEntity<>(new ArrayList<>() , HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> update(Map<String, String> requestMap) {
        try {
            if(jwtFilter.isUser()){
                Optional<User> optional=    userRepo.findById(Integer.parseInt(requestMap.get("id")));
                if(!optional.isEmpty()){
                    userRepo.updateStatus(requestMap.get("status") , Integer.parseInt(requestMap.get("id")));

                    sendMailToAllAdmin(requestMap.get("status"),optional.get().getEmail(),userRepo.getAllAdmin());
                    return CafeUtils.getResponseEntity("User status Update", HttpStatus.OK);
                }else{
                    return CafeUtils.getResponseEntity("URL id doesn't exist", HttpStatus.OK);
                }
            }
            else{
                return CafeUtils.getResponseEntity(CafeConstants.UNAUTHERIZED_ACCESS,HttpStatus.UNAUTHORIZED);
            }
          } catch (Exception e) {
            e.printStackTrace();
        }
        return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG,HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private void sendMailToAllAdmin(String status, String user, List<String> allAdmin) {

        allAdmin.remove(jwtFilter.getCurrentUser()); 
        
        // if(status != null && status.)
        if(status != null && status.equalsIgnoreCase("true")){
            emailUtils.sendSimpleMessage(jwtFilter.getCurrentUser(), "Account Approved", "USER:- " + user + " \n is approved by \nADMIN:-"+ jwtFilter.getCurrentUser(),allAdmin);
    }else{
        emailUtils.sendSimpleMessage(jwtFilter.getCurrentUser(), "Account Disable", "USER:- " + user + " \n is disabled by \nADMIN:-"+ jwtFilter.getCurrentUser(),allAdmin);

    }
        }

    @Override
    public ResponseEntity<String> checkToken() {
        return  CafeUtils.getResponseEntity("true", HttpStatus.OK);
    }

    @Override
    public ResponseEntity<String> changePassword(Map<String, String> requestMap) {
        try {
            
            User userObj = userRepo.findByemail(jwtFilter.getCurrentUser());
            if(!userObj.equals(null)){
                if(userObj.getPassword().equals(requestMap.get("oldPassword"))){
                    userObj.setPassword(requestMap.get("newPassword"));
                    userRepo.save(userObj);
                    return CafeUtils.getResponseEntity("Password Updated Successfully", HttpStatus.OK);
                }
                return CafeUtils.getResponseEntity("Incorrect old Password", HttpStatus.BAD_REQUEST);
            }
            return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return  CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);

    }

    @Override
    public ResponseEntity<String> forgotPassword(Map<String, String> requestMap) {
        try {
            User user = userRepo.findByEmailId(null);
            if(!Objects.isNull(user) && !Strings.isNullOrEmpty(user.getEmail())){
                emailUtils.forgetMail(user.getEmail(), "Credential by Signature", user.getPassword());
                return CafeUtils.getResponseEntity("Check your mail for Credentials", HttpStatus.OK);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }



       
    }

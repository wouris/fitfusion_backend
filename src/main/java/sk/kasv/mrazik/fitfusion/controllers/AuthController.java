package sk.kasv.mrazik.fitfusion.controllers;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.bson.types.ObjectId;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sk.kasv.mrazik.fitfusion.database.UserRepository;
import sk.kasv.mrazik.fitfusion.models.user.User;
import sk.kasv.mrazik.fitfusion.models.user.auth.UserAuth;
import sk.kasv.mrazik.fitfusion.models.util.JsonResponse;
import sk.kasv.mrazik.fitfusion.utils.GsonUtil;
import sk.kasv.mrazik.fitfusion.utils.TokenUtil;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    private final UserRepository userRepo;

    public AuthController(UserRepository userRepo) {
        this.userRepo = userRepo;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserAuth data){
        String username = data.username();
        String password = data.password();

        if(StringUtils.isBlank(username) || StringUtils.isBlank(password)) {
            JsonResponse response = new JsonResponse("Username or Password is blank!");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(GsonUtil.getInstance().toJson(response));
        }

        User user = userRepo.findByUsername(username);

        if(user == null){
            JsonResponse response = new JsonResponse("User not found!");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(GsonUtil.getInstance().toJson(response));
        }

        // hash password sent in request
        password = this.encoder.encode(password);

        if(!user.password().equals(password)){
            JsonResponse response = new JsonResponse("Wrong password!");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(GsonUtil.getInstance().toJson(response));
        } else {
            String token = RandomStringUtils.random(40);
            TokenUtil.getInstance().addToken(user.id(), token);
            Map<String, String> response = Map.of("token", token);
            return ResponseEntity.status(HttpStatus.OK).body(GsonUtil.getInstance().toJson(response));
        }
    }

    /**
     * This endpoint is used every time application is called to check if token is still valid
     * @param data
     * @return true if token is valid, false if token is not valid or user not found
     */
    @PostMapping("/checkToken")
    public ResponseEntity<Boolean> checkToken(@RequestBody User data) {

        User user = userRepo.findByUsername(data.username());

        if(user != null){
            boolean valid = TokenUtil.getInstance().getToken(user.id())
                    .expires().isAfter(LocalDateTime.now());

            return ResponseEntity.status(HttpStatus.OK).body(valid);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(false);
        }
    }
}

package sk.kasv.mrazik.fitfusion.controllers;


import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sk.kasv.mrazik.fitfusion.database.UserRepository;
import sk.kasv.mrazik.fitfusion.models.enums.ResponseType;
import sk.kasv.mrazik.fitfusion.models.enums.Role;
import sk.kasv.mrazik.fitfusion.models.user.User;
import sk.kasv.mrazik.fitfusion.models.user.auth.AuthResponse;
import sk.kasv.mrazik.fitfusion.models.user.auth.UserAuth;
import sk.kasv.mrazik.fitfusion.models.util.JsonResponse;
import sk.kasv.mrazik.fitfusion.utils.GsonUtil;
import sk.kasv.mrazik.fitfusion.utils.TokenUtil;

import java.util.UUID;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    private final UserRepository userRepo;

    public AuthController(UserRepository userRepo) {
        this.userRepo = userRepo;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserAuth data) {
        String username = data.username();
        String password = data.password();

        if (StringUtils.isBlank(username) || StringUtils.isBlank(password)) {
            JsonResponse response = new JsonResponse(ResponseType.ERROR, "Username or Password is blank!");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(GsonUtil.getInstance().toJson(response));
        }

        User user = userRepo.findByUsername(username);

        if (user == null) {
            JsonResponse response = new JsonResponse(ResponseType.ERROR, "User not found!");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(GsonUtil.getInstance().toJson(response));
        }

        if (!this.encoder.matches(password, user.password())) {
            JsonResponse response = new JsonResponse(ResponseType.ERROR, "Wrong password!");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(GsonUtil.getInstance().toJson(response));
        } else {
            String token = TokenUtil.generateToken();
            TokenUtil.getInstance().addToken(user.id(), token);
            AuthResponse response = new AuthResponse(token, user.id().toString(), user.role().toString());
            return ResponseEntity.status(HttpStatus.OK).body(GsonUtil.getInstance().toJson(response));
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody UserAuth data) {
        String email = data.email();
        String username = data.username();
        String password = data.password();

        if (StringUtils.isBlank(email) || StringUtils.isBlank(username) || StringUtils.isBlank(password)) {
            JsonResponse response = new JsonResponse(ResponseType.ERROR, "Some of the data provided is blank!");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(GsonUtil.getInstance().toJson(response));
        }

        User user = userRepo.findByUsername(username);

        if (user != null) {
            JsonResponse response = new JsonResponse(ResponseType.ERROR, "User already exists!");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(GsonUtil.getInstance().toJson(response));
        }

        // hash password sent in request
        password = this.encoder.encode(password);

        user = new User(UUID.randomUUID(), username, password, Role.USER);
        userRepo.save(user);

        String token = TokenUtil.generateToken();
        TokenUtil.getInstance().addToken(user.id(), token);
        AuthResponse response = new AuthResponse(token, user.id().toString(), user.role().toString());
        return ResponseEntity.status(HttpStatus.OK).body(GsonUtil.getInstance().toJson(response));
    }
}

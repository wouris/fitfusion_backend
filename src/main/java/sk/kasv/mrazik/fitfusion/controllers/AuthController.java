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
import sk.kasv.mrazik.fitfusion.exceptions.classes.BadCredentialsException;
import sk.kasv.mrazik.fitfusion.exceptions.classes.BlankDataException;
import sk.kasv.mrazik.fitfusion.exceptions.classes.NoRecordException;
import sk.kasv.mrazik.fitfusion.exceptions.classes.RecordExistsException;
import sk.kasv.mrazik.fitfusion.models.classes.user.User;
import sk.kasv.mrazik.fitfusion.models.classes.user.auth.UserAuth;
import sk.kasv.mrazik.fitfusion.models.classes.user.responses.AuthResponse;
import sk.kasv.mrazik.fitfusion.models.enums.Role;
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
            throw new BlankDataException("Username or Password is blank!");
        }

        User user = userRepo.findByUsername(username);

        if (user == null) {
            throw new NoRecordException("User not found!");
        }

        if (!this.encoder.matches(password, user.password())) {
            throw new BadCredentialsException("Username or Password is incorrect!");
        } else {
            String token = TokenUtil.generateToken();
            TokenUtil.getInstance().addToken(user.id(), token);
            AuthResponse response = new AuthResponse(token, user.id().toString(), user.role().toString());
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody UserAuth data) {
        String email = data.email();
        String username = data.username();
        String password = data.password();

        if (StringUtils.isBlank(email) || StringUtils.isBlank(username) || StringUtils.isBlank(password)) {
            throw new BlankDataException("Missing Data!");
        }

        User user = userRepo.findByUsernameOrEmail(username, email);

        if (user != null) {
            String emailResponse = "Email already exists!";
            String usernameResponse = "Username already exists!";
            throw new RecordExistsException(user.username().equals(username) ? usernameResponse : emailResponse);
        }

        // hash password sent in request
        password = this.encoder.encode(password);

        user = new User(UUID.randomUUID(), username, password, Role.USER);
        userRepo.save(user);

        String token = TokenUtil.generateToken();
        TokenUtil.getInstance().addToken(user.id(), token);
        AuthResponse response = new AuthResponse(token, user.id().toString(), user.role().toString());
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}

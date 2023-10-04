package sk.kasv.mrazik.fitfusion.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sk.kasv.mrazik.fitfusion.database.UserRepository;
import sk.kasv.mrazik.fitfusion.exceptions.classes.InvalidTokenException;
import sk.kasv.mrazik.fitfusion.exceptions.classes.NoRecordException;
import sk.kasv.mrazik.fitfusion.models.classes.user.User;
import sk.kasv.mrazik.fitfusion.utils.TokenUtil;

import java.util.UUID;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserRepository userRepo;

    public UserController(UserRepository userRepo) {
        this.userRepo = userRepo;
    }

    @GetMapping("/{id}/info")
    public ResponseEntity<?> getUserInfo(@PathVariable UUID id, @RequestHeader("Authorization") String token, @RequestHeader("USER_ID") UUID userId, @RequestBody String data) {

        if (TokenUtil.getInstance().isInvalidToken(userId, token)) {
            throw new InvalidTokenException("Wrong Token or user UUID, please re-login!");
        }

        // search for user and get his info
        User user = userRepo.findById(id).orElse(null);

        if (user == null) {
            throw new NoRecordException("User not found!");
        }

        return ResponseEntity.ok().body(user);


    }
}

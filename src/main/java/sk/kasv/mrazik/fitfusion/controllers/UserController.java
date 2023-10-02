package sk.kasv.mrazik.fitfusion.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sk.kasv.mrazik.fitfusion.database.UserRepository;
import sk.kasv.mrazik.fitfusion.models.enums.ResponseType;
import sk.kasv.mrazik.fitfusion.models.user.User;
import sk.kasv.mrazik.fitfusion.models.user.responses.JsonResponse;
import sk.kasv.mrazik.fitfusion.utils.GsonUtil;
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
            JsonResponse response = new JsonResponse(ResponseType.ERROR, "Wrong Token, please re-login!");
            return ResponseEntity.badRequest().body(GsonUtil.getInstance().toJson(response));
        }

        // search for user and get his info
        User user = userRepo.findById(id).orElse(null);

        if (user == null) {
            JsonResponse response = new JsonResponse(ResponseType.ERROR, "User not found!");
            return ResponseEntity.badRequest().body(GsonUtil.getInstance().toJson(response));
        }

        return ResponseEntity.ok().body(GsonUtil.getInstance().toJson(user));


    }
}

package sk.kasv.mrazik.fitfusion.controllers.social;

import java.util.UUID;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import sk.kasv.mrazik.fitfusion.databases.SocialInfoRepository;
import sk.kasv.mrazik.fitfusion.databases.UserRepository;
import sk.kasv.mrazik.fitfusion.exceptions.classes.NoRecordException;
import sk.kasv.mrazik.fitfusion.models.classes.user.SocialInfo;
import sk.kasv.mrazik.fitfusion.models.classes.user.User;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*") // this is for development only
@RequestMapping("/api/social/user")
public class UserController {

    private final UserRepository userRepo;
    private final SocialInfoRepository socialInfoRepo;

    public UserController(UserRepository userRepo, SocialInfoRepository socialInfoRepo) {
        this.userRepo = userRepo;
        this.socialInfoRepo = socialInfoRepo;
    }

    @GetMapping(value = "/{id}/info", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SocialInfo> getUserInfo(@PathVariable UUID id, @RequestHeader("Authorization") String token, @RequestHeader("USER_ID") UUID userId) {

        // search for user and get his info
        User user = userRepo.findById(id).orElse(null);

        if (user == null) {
            throw new NoRecordException("User not found!");
        }

        SocialInfo socialInfo = socialInfoRepo.getSocialInfo(id);

        socialInfo.username(user.username()); // this needs to be here as for some reason database returns username as null
        return ResponseEntity.ok(socialInfo);
    }
}

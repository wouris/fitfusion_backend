package sk.kasv.mrazik.fitfusion.controllers.social;

import java.util.List;
import java.util.UUID;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.transaction.Transactional;
import sk.kasv.mrazik.fitfusion.databases.FollowsRepository;
import sk.kasv.mrazik.fitfusion.databases.SocialInfoRepository;
import sk.kasv.mrazik.fitfusion.databases.UserRepository;
import sk.kasv.mrazik.fitfusion.exceptions.classes.BlankDataException;
import sk.kasv.mrazik.fitfusion.exceptions.classes.InvalidInteractionException;
import sk.kasv.mrazik.fitfusion.exceptions.classes.NoRecordException;
import sk.kasv.mrazik.fitfusion.exceptions.classes.RecordExistsException;
import sk.kasv.mrazik.fitfusion.models.classes.user.SocialInfo;
import sk.kasv.mrazik.fitfusion.models.classes.user.User;
import sk.kasv.mrazik.fitfusion.models.classes.user.UserSearchDTO;
import sk.kasv.mrazik.fitfusion.models.classes.user.interactions.Follow;
import sk.kasv.mrazik.fitfusion.models.classes.user.responses.JsonResponse;
import sk.kasv.mrazik.fitfusion.models.enums.ResponseType;
import sk.kasv.mrazik.fitfusion.utils.GsonUtil;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*") // this is for development only
@RequestMapping("/api/social/user")
public class UserController {

    private final UserRepository userRepo;
    private final SocialInfoRepository socialInfoRepo;
    private final FollowsRepository followingRepo;

    public UserController(UserRepository userRepo, SocialInfoRepository socialInfoRepo,
            FollowsRepository followingRepo) {
        this.userRepo = userRepo;
        this.socialInfoRepo = socialInfoRepo;
        this.followingRepo = followingRepo;
    }

    @GetMapping(value = "/{id}/info", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SocialInfo> getUserInfo(@PathVariable UUID id) {

        // search for user and get his info
        User user = userRepo.findById(id).orElse(null);

        if (user == null) {
            throw new NoRecordException("User not found!");
        }

        SocialInfo socialInfo = socialInfoRepo.getSocialInfo(id);

        return ResponseEntity.ok(socialInfo);
    }

    @PostMapping("/follow")
    public JsonResponse followUser(@RequestBody String followindData, @RequestHeader("USER_ID") UUID userId) {
        Follow data = GsonUtil.getInstance().fromJson(followindData, Follow.class);

        // check if followingId is present
        if (data.followingId() == null) {
            throw new BlankDataException("Following id is missing!");
        }

        // check if user is trying to follow himself
        if (data.followingId().equals(userId)) {
            throw new InvalidInteractionException("You can't follow yourself!");
        }

        // check if user exists
        User user = userRepo.findById(data.followingId()).orElse(null);
        if (user == null) {
            throw new NoRecordException("User not found!");
        }

        // check if followingId is already followed by the userId
        boolean isFollowed = followingRepo.existsByFollowerIdAndFollowingId(userId, data.followingId());
        if (isFollowed) {
            throw new RecordExistsException("You are already following this user!");
        }

        // add following
        followingRepo.save(new Follow(userId, data.followingId()));

        return new JsonResponse(ResponseType.SUCCESS, "User followed successfully!");
    }

    @Transactional
    @DeleteMapping("/unfollow")
    public JsonResponse unfollowUser(@RequestBody String followindData, @RequestHeader("USER_ID") UUID userId) {
        Follow data = GsonUtil.getInstance().fromJson(followindData, Follow.class);

        // check if followingId is present
        if (data.followingId() == null) {
            throw new BlankDataException("Following id is missing!");
        }

        // check if user exists
        User user = userRepo.findById(data.followingId()).orElse(null);
        if (user == null) {
            throw new NoRecordException("User not found!");
        }

        // check if followingId is already followed by the userId
        boolean isFollowed = followingRepo.existsByFollowerIdAndFollowingId(userId, data.followingId());
        if (!isFollowed) {
            throw new NoRecordException("User is not following this user!");
        }

        // remove following
        followingRepo.deleteByFollowerIdAndFollowingId(userId, data.followingId());

        return new JsonResponse(ResponseType.SUCCESS, "User unfollowed successfully!");
    }

    @GetMapping("/all")
    public ResponseEntity<List<UserSearchDTO>> getAllUsers() {
        return ResponseEntity.ok(userRepo.getAllSearchDTOs());
        
    }
}
package sk.kasv.mrazik.fitfusion.controllers.social;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.JsonParser;

import sk.kasv.mrazik.fitfusion.database.CommentRepository;
import sk.kasv.mrazik.fitfusion.database.PostRepository;
import sk.kasv.mrazik.fitfusion.database.UserRepository;
import sk.kasv.mrazik.fitfusion.exceptions.classes.BlankDataException;
import sk.kasv.mrazik.fitfusion.exceptions.classes.InvalidTokenException;
import sk.kasv.mrazik.fitfusion.exceptions.classes.NoRecordException;
import sk.kasv.mrazik.fitfusion.exceptions.classes.UnauthorizedActionException;
import sk.kasv.mrazik.fitfusion.models.classes.social.Comment;
import sk.kasv.mrazik.fitfusion.models.classes.user.User;
import sk.kasv.mrazik.fitfusion.models.classes.user.responses.JsonResponse;
import sk.kasv.mrazik.fitfusion.models.enums.ResponseType;
import sk.kasv.mrazik.fitfusion.models.enums.Role;
import sk.kasv.mrazik.fitfusion.utils.GsonUtil;
import sk.kasv.mrazik.fitfusion.utils.TokenUtil;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*") // this is for development only
@RequestMapping("/api/social/comment")
public class CommentController {

    private final CommentRepository commentRepo;
    private final PostRepository postRepo;
    private final UserRepository userRepo;

    public CommentController(CommentRepository commentRepo, PostRepository postRepo, UserRepository userRepo) {
        this.commentRepo = commentRepo;
        this.postRepo = postRepo;
        this.userRepo = userRepo;
    }

    @GetMapping("/get")
    public ResponseEntity<?> getComments(@RequestBody String data, @RequestHeader("Authorization") String token, @RequestHeader("USER_ID") UUID id) {
        String postId = JsonParser.parseString(data).getAsJsonObject().get("postId").getAsString();

        if (TokenUtil.getInstance().isInvalidToken(id, token)) {
            throw new InvalidTokenException("Wrong Token or user UUID, please re-login!");
        }

        if (postId == null) {
            throw new BlankDataException("PostID is blank!");
        }

        UUID postUUID = UUID.fromString(postId);

        // check if the post exists
        if (!postRepo.existsById(postUUID)) {
            throw new NoRecordException("Post not found!");
        }

        return ResponseEntity.ok().body(commentRepo.findAllByPostId(postUUID));
    }

    @PostMapping("/upload")
    public ResponseEntity<?> commentPost(@RequestBody String data, @RequestHeader("Authorization") String token, @RequestHeader("USER_ID") UUID id) {
        Comment comment = GsonUtil.getInstance().fromJson(data, Comment.class);

        if (TokenUtil.getInstance().isInvalidToken(id, token)) {
            throw new InvalidTokenException("Wrong Token or user UUID, please re-login!");
        }

        // check if the post exists
        if (!postRepo.existsById(comment.postId())) {
            throw new NoRecordException("Post not found!");
        }

        comment.id(UUID.randomUUID());
        comment.userId(id);

        if (comment.userId() == null || comment.postId() == null || comment.content() == null) {
            throw new BlankDataException("Missing data!");
        }

        commentRepo.save(comment);

        JsonResponse response = new JsonResponse(ResponseType.SUCCESS, "Comment created!");
        return ResponseEntity.ok().body(response);
    }

    @DeleteMapping("/remove")
    public ResponseEntity<?> removeComment(@RequestBody String data, @RequestHeader("Authorization") String token, @RequestHeader("USER_ID") UUID id) {
        Comment comment = GsonUtil.getInstance().fromJson(data, Comment.class);

        if (TokenUtil.getInstance().isInvalidToken(id, token)) {
            throw new InvalidTokenException("Wrong Token or user UUID, please re-login!");
        }

        if (comment.id() == null) {
            throw new BlankDataException("CommentID is blank!");
        }

        // check if the comment exists
        if (!commentRepo.existsById(comment.id())) {
            throw new NoRecordException("Comment you're trying to remove does not exist!");
        }

        // check requester's role
        User user = userRepo.findById(id).orElse(null);
        if (user == null) {
            throw new NoRecordException("User not found!");
        }

        // check if the comment belongs to the user
        if (user.role() != Role.ADMIN) {
            if (!commentRepo.findById(comment.id()).get().userId().equals(id)) {
                throw new UnauthorizedActionException("Comment does not belong to the user!");
            }
        }

        commentRepo.deleteById(comment.id());

        JsonResponse response = new JsonResponse(ResponseType.SUCCESS, "Comment deleted!");
        return ResponseEntity.ok().body(response);
    }
}
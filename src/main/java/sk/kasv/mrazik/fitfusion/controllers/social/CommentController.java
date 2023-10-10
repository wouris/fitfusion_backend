package sk.kasv.mrazik.fitfusion.controllers.social;

import com.google.gson.JsonParser;
import org.springframework.web.bind.annotation.*;
import sk.kasv.mrazik.fitfusion.databases.CommentLikesRepository;
import sk.kasv.mrazik.fitfusion.databases.CommentRepository;
import sk.kasv.mrazik.fitfusion.databases.PostRepository;
import sk.kasv.mrazik.fitfusion.databases.UserRepository;
import sk.kasv.mrazik.fitfusion.exceptions.classes.BlankDataException;
import sk.kasv.mrazik.fitfusion.exceptions.classes.InvalidTokenException;
import sk.kasv.mrazik.fitfusion.exceptions.classes.NoRecordException;
import sk.kasv.mrazik.fitfusion.exceptions.classes.UnauthorizedActionException;
import sk.kasv.mrazik.fitfusion.models.classes.social.comment.Comment;
import sk.kasv.mrazik.fitfusion.models.classes.social.comment.CommentDTO;
import sk.kasv.mrazik.fitfusion.models.classes.social.comment.CommentLike;
import sk.kasv.mrazik.fitfusion.models.classes.user.User;
import sk.kasv.mrazik.fitfusion.models.classes.user.responses.JsonResponse;
import sk.kasv.mrazik.fitfusion.models.enums.ResponseType;
import sk.kasv.mrazik.fitfusion.models.enums.Role;
import sk.kasv.mrazik.fitfusion.utils.GsonUtil;
import sk.kasv.mrazik.fitfusion.utils.TokenUtil;

import java.util.Set;
import java.util.UUID;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*") // this is for development only
@RequestMapping("/api/social/comment")
public class CommentController {

    private final CommentRepository commentRepo;
    private final CommentLikesRepository commentLikesRepo;
    private final PostRepository postRepo;
    private final UserRepository userRepo;

    public CommentController(
            CommentRepository commentRepo,
            PostRepository postRepo,
            UserRepository userRepo,
            CommentLikesRepository commentLikesRepo) {
        this.commentRepo = commentRepo;
        this.postRepo = postRepo;
        this.userRepo = userRepo;
        this.commentLikesRepo = commentLikesRepo;
    }

    @PostMapping("/get")
    public Set<CommentDTO> getComments(@RequestBody String data) {
        String postId = JsonParser.parseString(data).getAsJsonObject().get("postId").getAsString();

        if (postId == null) {
            throw new BlankDataException("PostID is blank!");
        }

        UUID postUUID = UUID.fromString(postId);

        // check if the post exists
        if (!postRepo.existsById(postUUID)) {
            throw new NoRecordException("Post not found!");
        }

        return commentRepo.findAllByPostId(postUUID);
    }

    @PostMapping("/upload")
    public JsonResponse commentPost(@RequestBody String data, @RequestHeader("USER_ID") UUID id) {
        Comment comment = GsonUtil.getInstance().fromJson(data, Comment.class);

        // check if the post exists
        if (!postRepo.existsById(comment.postId())) {
            throw new NoRecordException("Post not found!");
        }

        comment.id(UUID.randomUUID());
        comment.userId(id);

        if (comment.postId() == null || comment.content() == null) {
            throw new BlankDataException("Missing data!");
        }

        commentRepo.save(comment);

        return new JsonResponse(ResponseType.SUCCESS, "Comment created!");
    }

    @DeleteMapping("/remove")
    public JsonResponse removeComment(@RequestBody String data, @RequestHeader("USER_ID") UUID id) {

        UUID commentId = verifyCommentId(data);

        // check requester's role
        User user = userRepo.findById(id).orElse(null);
        if (user == null) {
            throw new NoRecordException("User not found!");
        }

        // check if the comment belongs to the user
        if (user.role() != Role.ADMIN) {
            if (!commentRepo.findById(commentId).get().userId().equals(id)) {
                throw new UnauthorizedActionException("Comment does not belong to the user!");
            }
        }

        commentRepo.deleteById(commentId);

        return new JsonResponse(ResponseType.SUCCESS, "Comment removed!");
    }

    @PostMapping("/like")
    public JsonResponse likeComment(@RequestBody String data, @RequestHeader("USER_ID") UUID id) {
        UUID commentId = verifyCommentId(data);

        if (commentLikesRepo.existsByUserIdAndCommentId(id, commentId)) {
            throw new UnauthorizedActionException("User already liked this comment!");
        }

        commentLikesRepo.save(new CommentLike(id, commentId));

        return new JsonResponse(ResponseType.SUCCESS, "Comment liked!");
    }

    @DeleteMapping("/unlike")
    public JsonResponse dislikeComment(@RequestBody String data, @RequestHeader("USER_ID") UUID id) {
        UUID commentId = verifyCommentId(data);

        if (!commentLikesRepo.existsByUserIdAndCommentId(id, commentId)) {
            throw new UnauthorizedActionException("User has not liked this comment!");
        }

        commentLikesRepo.deleteById(id);

        return new JsonResponse(ResponseType.SUCCESS, "Comment unliked!");
    }

    private UUID verifyCommentId(String data) {
        String commentIdString = JsonParser.parseString(data).getAsJsonObject().get("commentId").getAsString();

        if (commentIdString == null) {
            throw new BlankDataException("CommentId is blank!");
        }

        UUID commentId = UUID.fromString(commentIdString);

        // check if the comment exists
        if (!commentRepo.existsById(commentId)) {
            throw new NoRecordException("Comment not found!");
        }

        return commentId;
    }
}
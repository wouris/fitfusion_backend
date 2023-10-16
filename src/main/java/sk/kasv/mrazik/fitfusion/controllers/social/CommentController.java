package sk.kasv.mrazik.fitfusion.controllers.social;

import com.google.gson.JsonParser;
import jakarta.transaction.Transactional;
import org.springframework.web.bind.annotation.*;
import sk.kasv.mrazik.fitfusion.databases.*;
import sk.kasv.mrazik.fitfusion.exceptions.classes.BlankDataException;
import sk.kasv.mrazik.fitfusion.exceptions.classes.InternalServerErrorException;
import sk.kasv.mrazik.fitfusion.exceptions.classes.NoRecordException;
import sk.kasv.mrazik.fitfusion.exceptions.classes.UnauthorizedActionException;
import sk.kasv.mrazik.fitfusion.models.classes.social.comment.Comment;
import sk.kasv.mrazik.fitfusion.models.classes.social.comment.CommentDTO;
import sk.kasv.mrazik.fitfusion.models.classes.social.comment.CommentLike;
import sk.kasv.mrazik.fitfusion.models.classes.social.comment.Reply;
import sk.kasv.mrazik.fitfusion.models.classes.user.User;
import sk.kasv.mrazik.fitfusion.models.classes.user.responses.JsonResponse;
import sk.kasv.mrazik.fitfusion.models.enums.ResponseType;
import sk.kasv.mrazik.fitfusion.models.enums.Role;
import sk.kasv.mrazik.fitfusion.utils.GsonUtil;

import java.sql.Timestamp;
import java.util.Set;
import java.util.UUID;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*") // this is for development only
@RequestMapping("/api/social/comment")
public class CommentController {

    private final CommentRepository commentRepo;
    private final CommentLikesRepository commentLikesRepo;
    private final ReplyRepository replyRepo;
    private final PostRepository postRepo;
    private final UserRepository userRepo;

    public CommentController(
            CommentRepository commentRepo,
            PostRepository postRepo,
            UserRepository userRepo,
            CommentLikesRepository commentLikesRepo,
            ReplyRepository replyRepo) {
        this.commentRepo = commentRepo;
        this.postRepo = postRepo;
        this.userRepo = userRepo;
        this.commentLikesRepo = commentLikesRepo;
        this.replyRepo = replyRepo;
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

        Set<CommentDTO> comments = commentRepo.findAllByPostId(postUUID);

        comments.forEach(comment -> {
            comment.replies(replyRepo.findAllByCommentId(comment.id()));
        });

        return comments;
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
        comment.createdAt(new Timestamp(System.currentTimeMillis()));

        if (comment.postId() == null || comment.content() == null) {
            throw new BlankDataException("Missing data!");
        }

        commentRepo.save(comment);

        return new JsonResponse(ResponseType.SUCCESS, "Comment created!");
    }

    @Transactional
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

        if (commentLikesRepo.existsByUserIdAndCommentId(id, commentId) ||
            commentLikesRepo.existsByUserIdAndReplyId(id, commentId)) {
            throw new UnauthorizedActionException("User already liked this comment!");
        }
        
        if(commentRepo.existsById(commentId)){
            commentLikesRepo.save(new CommentLike(id, commentId, null));
        } else {
            commentLikesRepo.save(new CommentLike(id, null, commentId));
        }
        

        return new JsonResponse(ResponseType.SUCCESS, "Comment liked!");
    }

    @Transactional
    @DeleteMapping("/unlike")
    public JsonResponse dislikeComment(@RequestBody String data, @RequestHeader("USER_ID") UUID id) {
        UUID commentId = verifyCommentId(data);

        if (!commentLikesRepo.existsByUserIdAndCommentId(id, commentId)) {
            throw new UnauthorizedActionException("User has not liked this comment!");
        }

        commentLikesRepo.deleteById(id);

        return new JsonResponse(ResponseType.SUCCESS, "Comment unliked!");
    }

    @PostMapping("/reply")
    public JsonResponse replyComment(@RequestBody String data, @RequestHeader("USER_ID") UUID id) {
        UUID commentId = verifyCommentId(data);

        String content = JsonParser.parseString(data).getAsJsonObject().get("content").getAsString();

        if (content == null) {
            throw new BlankDataException("Content is blank!");
        }

        replyRepo.save(new Reply(id, commentId, content, new Timestamp(System.currentTimeMillis())));

        return new JsonResponse(ResponseType.SUCCESS, "Comment replied!");
    }

    private UUID verifyCommentId(String data) {
        String commentIdString = JsonParser.parseString(data).getAsJsonObject().get("commentId").getAsString();

        if (commentIdString == null) {
            throw new BlankDataException("CommentId is blank!");
        }

        UUID commentId;
        try {
            commentId = UUID.fromString(commentIdString);
        } catch (IllegalArgumentException ex) {
            throw new InternalServerErrorException("CommentId is not a valid UUID!");
        }

        // check if the comment exists
        if (!commentRepo.existsById(commentId)) {
            if(!replyRepo.existsById(commentId)) {
                throw new NoRecordException("Comment not found!");
            }
        }

        return commentId;
    }
}
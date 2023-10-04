package sk.kasv.mrazik.fitfusion.controllers;

import com.google.gson.JsonParser;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sk.kasv.mrazik.fitfusion.database.CommentRepository;
import sk.kasv.mrazik.fitfusion.database.PostRepository;
import sk.kasv.mrazik.fitfusion.database.UserRepository;
import sk.kasv.mrazik.fitfusion.exceptions.classes.*;
import sk.kasv.mrazik.fitfusion.models.classes.social.Comment;
import sk.kasv.mrazik.fitfusion.models.classes.social.Post;
import sk.kasv.mrazik.fitfusion.models.classes.user.User;
import sk.kasv.mrazik.fitfusion.models.classes.user.responses.JsonResponse;
import sk.kasv.mrazik.fitfusion.models.enums.ResponseType;
import sk.kasv.mrazik.fitfusion.models.enums.Role;
import sk.kasv.mrazik.fitfusion.utils.GsonUtil;
import sk.kasv.mrazik.fitfusion.utils.TokenUtil;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.plugins.jpeg.JPEGImageWriteParam;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.Iterator;
import java.util.UUID;


@RestController
@RequestMapping("/api/social")
public class SocialController {

    private final PostRepository postRepo;
    private final CommentRepository commentRepo;
    private final UserRepository userRepo;

    public SocialController(PostRepository postRepo, CommentRepository commentRepo, UserRepository userRepo) {
        this.postRepo = postRepo;
        this.commentRepo = commentRepo;
        this.userRepo = userRepo;
    }

    @PostMapping("/post/upload")
    public ResponseEntity<?> uploadPost(@RequestBody String data, @RequestHeader("Authorization") String token, @RequestHeader("USER_ID") UUID id) {

        Post post = GsonUtil.getInstance().fromJson(data, Post.class);

        if (TokenUtil.getInstance().isInvalidToken(id, token)) {
            throw new InvalidTokenException("Wrong Token or user UUID, please re-login!");
        }

        // don't have to check for post.authorId() because it is in request header and checked by token
        if (post.image() == null || post.description() == null) {
            throw new InvalidTokenException("Missing data!");
        }

        byte[] imageBytes = Base64.getDecoder().decode(post.image());

        ByteArrayInputStream bis = new ByteArrayInputStream(imageBytes);

        try {
            BufferedImage bufferedImage = ImageIO.read(bis);

            // Create an output stream to store the compressed image
            ByteArrayOutputStream bos = new ByteArrayOutputStream();

            // Create a JPEG writer with specific compression quality
            Iterator<ImageWriter> imageWriters = ImageIO.getImageWritersByFormatName("jpeg");
            ImageWriter imageWriter = imageWriters.next();
            ImageWriteParam writeParam = new JPEGImageWriteParam(null);
            writeParam.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
            writeParam.setCompressionQuality(0.3f); // 0.0f - 1.0f

            imageWriter.setOutput(ImageIO.createImageOutputStream(bos));
            imageWriter.write(null, new IIOImage(bufferedImage, null, null), writeParam);

            // Get the compressed image as bytes
            byte[] compressedImageBytes = bos.toByteArray();

            // Convert the compressed image bytes back to Base64
            String compressedBase64Image = Base64.getEncoder().encodeToString(compressedImageBytes);

            post = new Post(compressedBase64Image, post.description(), id);

            postRepo.save(post);

            JsonResponse response = new JsonResponse(ResponseType.SUCCESS, "Post created!");
            return ResponseEntity.ok().body(response);
        } catch (IOException e) {
            e.printStackTrace();
            throw new InternalServerErrorException("Error while compressing image!");
        }
    }

    @DeleteMapping("/post/remove")
    public ResponseEntity<?> removePost(@RequestBody String postId, @RequestHeader("Authorization") String token, @RequestHeader("USER_ID") UUID id) {
        Post post = GsonUtil.getInstance().fromJson(postId, Post.class);

        if (TokenUtil.getInstance().isInvalidToken(id, token)) {
            throw new InvalidTokenException("Wrong Token or user UUID, please re-login!");
        }

        if (post.id() == null) {
            throw new BlankDataException("PostID is blank!");
        }

        // check if the post exists
        if (!postRepo.existsById(post.id())) {
            throw new NoRecordException("Post not found!");
        }

        // check requester's role
        User user = userRepo.findById(id).orElse(null);
        if (user == null) {
            throw new NoRecordException("User not found!");
        }

        // check if the post belongs to the user
        if (user.role() != Role.ADMIN) {
            if (!postRepo.findById(post.id()).get().userId().equals(id)) {
                throw new UnauthorizedActionException("Post does not belong to the user!");
            }
        }

        postRepo.deleteById(post.id());

        JsonResponse response = new JsonResponse(ResponseType.SUCCESS, "Post deleted!");
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/comment/get")
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

    @PostMapping("/comment/upload")
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

    @DeleteMapping("/comment/remove")
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
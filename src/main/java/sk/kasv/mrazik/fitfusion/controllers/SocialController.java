package sk.kasv.mrazik.fitfusion.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sk.kasv.mrazik.fitfusion.database.CommentRepository;
import sk.kasv.mrazik.fitfusion.database.PostRepository;
import sk.kasv.mrazik.fitfusion.database.UserRepository;
import sk.kasv.mrazik.fitfusion.models.enums.ResponseType;
import sk.kasv.mrazik.fitfusion.models.enums.Role;
import sk.kasv.mrazik.fitfusion.models.social.Comment;
import sk.kasv.mrazik.fitfusion.models.social.Post;
import sk.kasv.mrazik.fitfusion.models.user.User;
import sk.kasv.mrazik.fitfusion.models.user.responses.JsonResponse;
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
    public ResponseEntity<String> uploadPost(@RequestBody String data, @RequestHeader("Authorization") String token, @RequestHeader("USER_ID") UUID id) {

        Post post = GsonUtil.getInstance().fromJson(data, Post.class);

        if (TokenUtil.getInstance().isInvalidToken(id, token)) {
            JsonResponse response = new JsonResponse(ResponseType.ERROR, "Wrong Token or user UUID, please re-login!");
            return ResponseEntity.badRequest().body(GsonUtil.getInstance().toJson(response));
        }

        // don't have to check for post.authorId() because it is in request header and checked by token
        if (post.image() == null || post.description() == null) {
            JsonResponse response = new JsonResponse(ResponseType.ERROR, "Missing data!");
            return ResponseEntity.badRequest().body(GsonUtil.getInstance().toJson(response));
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
            writeParam.setCompressionQuality(0.3f); // Set compression quality here (0.0 to 1.0)

            imageWriter.setOutput(ImageIO.createImageOutputStream(bos));
            imageWriter.write(null, new IIOImage(bufferedImage, null, null), writeParam);

            // Get the compressed image as bytes
            byte[] compressedImageBytes = bos.toByteArray();

            // Convert the compressed image bytes back to Base64
            String compressedBase64Image = Base64.getEncoder().encodeToString(compressedImageBytes);

            post = new Post(compressedBase64Image, post.description(), id);

            postRepo.save(post);

            JsonResponse response = new JsonResponse(ResponseType.SUCCESS, "Post created!");
            return ResponseEntity.ok().body(GsonUtil.getInstance().toJson(response));
        } catch (IOException e) {
            e.printStackTrace();
            JsonResponse response = new JsonResponse(ResponseType.ERROR, "Error while reading image!");
            return ResponseEntity.internalServerError().body(GsonUtil.getInstance().toJson(response));
        }
    }

    @DeleteMapping("/post/remove")
    public ResponseEntity<String> removePost(@RequestBody String postId, @RequestHeader("Authorization") String token, @RequestHeader("USER_ID") UUID id) {
        Post post = GsonUtil.getInstance().fromJson(postId, Post.class);

        if (TokenUtil.getInstance().isInvalidToken(id, token)) {
            JsonResponse response = new JsonResponse(ResponseType.ERROR, "Wrong Token or user UUID, please re-login!");
            return ResponseEntity.badRequest().body(GsonUtil.getInstance().toJson(response));
        }

        if (post.id() == null) {
            JsonResponse response = new JsonResponse(ResponseType.ERROR, "Missing data!");
            return ResponseEntity.badRequest().body(GsonUtil.getInstance().toJson(response));
        }

        // check if the post exists
        if (!postRepo.existsById(post.id())) {
            JsonResponse response = new JsonResponse(ResponseType.ERROR, "Post not found!");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(GsonUtil.getInstance().toJson(response));
        }

        // check requester's role
        User user = userRepo.findById(id).orElse(null);
        if (user == null) {
            JsonResponse response = new JsonResponse(ResponseType.ERROR, "User not found!");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(GsonUtil.getInstance().toJson(response));
        }

        // check if the post belongs to the user
        if (user.role() != Role.ADMIN) {
            if (!postRepo.findById(post.id()).get().authorId().equals(id)) {
                JsonResponse response = new JsonResponse(ResponseType.ERROR, "Post does not belong to the user!");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(GsonUtil.getInstance().toJson(response));
            }
        }

        postRepo.deleteById(post.id());

        JsonResponse response = new JsonResponse(ResponseType.SUCCESS, "Post deleted!");
        return ResponseEntity.ok().body(GsonUtil.getInstance().toJson(response));
    }

    @PostMapping("/comment/upload")
    public ResponseEntity<String> commentPost(@RequestBody String data, @RequestHeader("Authorization") String token, @RequestHeader("USER_ID") UUID id) {
        Comment comment = GsonUtil.getInstance().fromJson(data, Comment.class);

        if (TokenUtil.getInstance().isInvalidToken(id, token)) {
            JsonResponse response = new JsonResponse(ResponseType.ERROR, "Wrong Token or user UUID, please re-login!");
            return ResponseEntity.badRequest().body(GsonUtil.getInstance().toJson(response));
        }

        comment.id(UUID.randomUUID());
        comment.userId(id);

        if (comment.userId() == null || comment.postId() == null || comment.content() == null) {
            JsonResponse response = new JsonResponse(ResponseType.ERROR, "Missing data!");
            return ResponseEntity.badRequest().body(GsonUtil.getInstance().toJson(response));
        }

        commentRepo.save(comment);

        JsonResponse response = new JsonResponse(ResponseType.SUCCESS, "Comment created!");
        return ResponseEntity.ok().body(GsonUtil.getInstance().toJson(response));
    }

    @DeleteMapping("/comment/remove")
    public ResponseEntity<String> removeComment(@RequestBody String data, @RequestHeader("Authorization") String token, @RequestHeader("USER_ID") UUID id) {
        Comment comment = GsonUtil.getInstance().fromJson(data, Comment.class);

        if (TokenUtil.getInstance().isInvalidToken(id, token)) {
            JsonResponse response = new JsonResponse(ResponseType.ERROR, "Wrong Token or user UUID, please re-login!");
            return ResponseEntity.badRequest().body(GsonUtil.getInstance().toJson(response));
        }

        if (comment.id() == null) {
            JsonResponse response = new JsonResponse(ResponseType.ERROR, "Missing data!");
            return ResponseEntity.badRequest().body(GsonUtil.getInstance().toJson(response));
        }

        // check if the comment exists
        if (!commentRepo.existsById(comment.id())) {
            JsonResponse response = new JsonResponse(ResponseType.ERROR, "Comment not found!");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(GsonUtil.getInstance().toJson(response));
        }

        // check requester's role
        User user = userRepo.findById(id).orElse(null);
        if (user == null) {
            JsonResponse response = new JsonResponse(ResponseType.ERROR, "User not found!");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(GsonUtil.getInstance().toJson(response));
        }

        // check if the comment belongs to the user
        if (user.role() != Role.ADMIN) {
            if (!commentRepo.findById(comment.id()).get().userId().equals(id)) {
                JsonResponse response = new JsonResponse(ResponseType.ERROR, "Comment does not belong to the user!");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(GsonUtil.getInstance().toJson(response));
            }
        }

        commentRepo.deleteById(comment.id());

        JsonResponse response = new JsonResponse(ResponseType.SUCCESS, "Comment deleted!");
        return ResponseEntity.ok().body(GsonUtil.getInstance().toJson(response));
    }
}

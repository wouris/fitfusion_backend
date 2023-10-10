package sk.kasv.mrazik.fitfusion.controllers.social;


import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Iterator;
import java.util.Set;
import java.util.UUID;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.plugins.jpeg.JPEGImageWriteParam;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.JsonParser;

import jakarta.transaction.Transactional;
import sk.kasv.mrazik.fitfusion.databases.CommentLikesRepository;
import sk.kasv.mrazik.fitfusion.databases.CommentRepository;
import sk.kasv.mrazik.fitfusion.databases.LikeRepository;
import sk.kasv.mrazik.fitfusion.databases.PostRepository;
import sk.kasv.mrazik.fitfusion.databases.UserRepository;
import sk.kasv.mrazik.fitfusion.exceptions.classes.BlankDataException;
import sk.kasv.mrazik.fitfusion.exceptions.classes.InternalServerErrorException;
import sk.kasv.mrazik.fitfusion.exceptions.classes.NoRecordException;
import sk.kasv.mrazik.fitfusion.exceptions.classes.RecordExistsException;
import sk.kasv.mrazik.fitfusion.exceptions.classes.UnauthorizedActionException;
import sk.kasv.mrazik.fitfusion.models.classes.social.comment.CommentDTO;
import sk.kasv.mrazik.fitfusion.models.classes.social.likes.Like;
import sk.kasv.mrazik.fitfusion.models.classes.social.post.Post;
import sk.kasv.mrazik.fitfusion.models.classes.social.post.PostDTO;
import sk.kasv.mrazik.fitfusion.models.classes.social.post.PostRequest;
import sk.kasv.mrazik.fitfusion.models.classes.user.User;
import sk.kasv.mrazik.fitfusion.models.classes.user.responses.JsonResponse;
import sk.kasv.mrazik.fitfusion.models.enums.ResponseType;
import sk.kasv.mrazik.fitfusion.models.enums.Role;
import sk.kasv.mrazik.fitfusion.utils.GsonUtil;


@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*") // this is for development only
@RequestMapping("/api/social/post")
public class PostController {

    private final PostRepository postRepo;
    private final CommentRepository commentRepo;
    private final CommentLikesRepository commentLikesRepo;
    private final UserRepository userRepo;

    private final LikeRepository likeRepo;

    public PostController(PostRepository postRepo, UserRepository userRepo, CommentRepository commentRepo, LikeRepository likeRepo, CommentLikesRepository commentLikesRepo) {
        this.postRepo = postRepo;
        this.userRepo = userRepo;
        this.commentRepo = commentRepo;
        this.likeRepo = likeRepo;
        this.commentLikesRepo = commentLikesRepo;
    }

    // TODO: Test this method properly after filling the database with posts and followings
    @PostMapping("/get")
    public Set<PostDTO> getPosts(@RequestBody PostRequest postRequest, @RequestHeader("USER_ID") UUID id) {

        Set<PostDTO> posts = postRepo.findPostsByFollowing(id, postRequest.pageSize(), postRequest.pageOffset());

        if (posts.isEmpty()) {
            posts.addAll(postRepo.findRandomPosts(id, postRequest.pageSize()));
        } else if (posts.size() < 5) {
            posts.addAll(postRepo.findRandomPosts(id, postRequest.pageSize() - posts.size()));
        }

        posts.forEach(post -> {
            post.likes(likeRepo.countByPostId(post.id()));

            Set<CommentDTO> comments = commentRepo.findTopByPostId(post.id());

            comments.forEach(comment -> {
                comment.likes(commentLikesRepo.countByCommentId(comment.id()));
            });

            post.topComments(comments);
        });

        return posts;
    }

    @PostMapping("/upload")
    public JsonResponse uploadPost(@RequestBody String data, @RequestHeader("USER_ID") UUID id) {

        Post post = GsonUtil.getInstance().fromJson(data, Post.class);

        // don't have to check for post.authorId() because it is in request header and checked by token
        if (post.image() == null || post.description() == null) {
            throw new BlankDataException("Missing data!");
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

            post = new Post(compressedBase64Image, post.description(), id, Timestamp.valueOf(LocalDateTime.now()));

            postRepo.save(post);

            return new JsonResponse(ResponseType.SUCCESS, "Post created!");
        } catch (IOException e) {
            e.printStackTrace();
            throw new InternalServerErrorException("Error while compressing image!");
        }
    }

    @DeleteMapping("/remove")
    public JsonResponse removePost(@RequestBody String data, @RequestHeader("USER_ID") UUID id) {
        UUID postId = verifyPostId(data);

        // check requester's role
        User user = userRepo.findById(id).orElse(null);
        if (user == null) {
            throw new NoRecordException("User not found!");
        }

        // check if the post belongs to the user
        if (user.role() != Role.ADMIN) {
            if (!postRepo.findById(postId).get().userId().equals(id)) {
                throw new UnauthorizedActionException("Post does not belong to the user!");
            }
        }

        postRepo.deleteById(postId);

        return new JsonResponse(ResponseType.SUCCESS, "Post deleted!");
    }

    @PostMapping("/like")
    public JsonResponse likePost(@RequestBody String data, @RequestHeader("USER_ID") UUID id) {

        UUID postId = verifyPostId(data);

        // check if the user already liked the post
        if (likeRepo.existsByUserIdAndPostId(id, postId)) {
            throw new RecordExistsException("User already liked the post!");
        }

        likeRepo.save(new Like(id, postId));

        return new JsonResponse(ResponseType.SUCCESS, "Post liked!");
    }

    @Transactional
    @DeleteMapping("/unlike")
    public JsonResponse unlikePost(@RequestBody String data, @RequestHeader("USER_ID") UUID id) {

        UUID postId = verifyPostId(data);

        // check if the user already liked the post
        if (!likeRepo.existsByUserIdAndPostId(id, postId)) {
            throw new NoRecordException("User did not like the post!");
        }

        likeRepo.deleteByUserIdAndPostId(id, postId);

        return new JsonResponse(ResponseType.SUCCESS, "Post unliked!");

    }

    private UUID verifyPostId(String data) {
        String postIdString = JsonParser.parseString(data).getAsJsonObject().get("postId").getAsString();

        if (postIdString == null) {
            throw new BlankDataException("PostID is blank!");
        }

        UUID postId = UUID.fromString(postIdString);

        // check if the post exists
        if (!postRepo.existsById(postId)) {
            throw new NoRecordException("Post not found!");
        }

        return postId;
    }
}
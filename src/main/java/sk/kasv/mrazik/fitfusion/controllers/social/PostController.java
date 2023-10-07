package sk.kasv.mrazik.fitfusion.controllers.social;


import org.springframework.web.bind.annotation.*;
import sk.kasv.mrazik.fitfusion.database.PostRepository;
import sk.kasv.mrazik.fitfusion.database.UserRepository;
import sk.kasv.mrazik.fitfusion.exceptions.classes.*;
import sk.kasv.mrazik.fitfusion.models.classes.social.post.Post;
import sk.kasv.mrazik.fitfusion.models.classes.social.post.PostDTO;
import sk.kasv.mrazik.fitfusion.models.classes.social.post.PostRequest;
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
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Iterator;
import java.util.Set;
import java.util.UUID;


@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*") // this is for development only
@RequestMapping("/api/social/post")
public class PostController {

    private final PostRepository postRepo;
    private final UserRepository userRepo;

    public PostController(PostRepository postRepo, UserRepository userRepo) {
        this.postRepo = postRepo;
        this.userRepo = userRepo;
    }

    // TODO: Test this method properly after filling the database with posts and followings
    @PostMapping("/get")
    public Set<PostDTO> getPosts(@RequestBody PostRequest postRequest, @RequestHeader("Authorization") String token, @RequestHeader("USER_ID") UUID id) {

        if (TokenUtil.getInstance().isInvalidToken(id, token)) {
            throw new InvalidTokenException("Wrong Token or user UUID, please re-login!");
        }

        Set<PostDTO> posts = postRepo.findPostsByFollowing(id, postRequest.pageSize(), postRequest.pageOffset());

        if (posts.isEmpty()) {
            posts.addAll(postRepo.findRandomPosts(id, postRequest.pageSize()));
        } else if (posts.size() < 5) {
            posts.addAll(postRepo.findRandomPosts(id, postRequest.pageSize() - posts.size()));
        }

        return posts;
    }

    @PostMapping("/upload")
    public JsonResponse uploadPost(@RequestBody String data, @RequestHeader("Authorization") String token, @RequestHeader("USER_ID") UUID id) {

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

            post = new Post(compressedBase64Image, post.description(), id, Timestamp.valueOf(LocalDateTime.now()));

            postRepo.save(post);

            return new JsonResponse(ResponseType.SUCCESS, "Post created!");
        } catch (IOException e) {
            e.printStackTrace();
            throw new InternalServerErrorException("Error while compressing image!");
        }
    }

    @DeleteMapping("/remove")
    public JsonResponse removePost(@RequestBody String postId, @RequestHeader("Authorization") String token, @RequestHeader("USER_ID") UUID id) {
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

        return new JsonResponse(ResponseType.SUCCESS, "Post deleted!");
    }
}
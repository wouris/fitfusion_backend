package sk.kasv.mrazik.fitfusion.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import sk.kasv.mrazik.fitfusion.database.PostRepository;
import sk.kasv.mrazik.fitfusion.models.enums.ResponseType;
import sk.kasv.mrazik.fitfusion.models.social.Post;
import sk.kasv.mrazik.fitfusion.models.util.JsonResponse;
import sk.kasv.mrazik.fitfusion.utils.GsonUtil;
import sk.kasv.mrazik.fitfusion.utils.TokenUtil;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.Iterator;
import java.util.UUID;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.plugins.jpeg.JPEGImageWriteParam;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;


@RestController
@RequestMapping("/api/posts")
public class PostController {

    private final PostRepository postRepo;

    public PostController(PostRepository postRepo) {
        this.postRepo = postRepo;
    }
    
    @PostMapping("/upload")
    public ResponseEntity<?> postMethodName(@RequestBody String data, @RequestHeader("Authorization") String token, @RequestHeader("USER_ID") UUID id) {

        Post post = GsonUtil.getInstance().fromJson(data, Post.class);

        if(!TokenUtil.getInstance().isTokenValid(id, token)){
            JsonResponse response = new JsonResponse(ResponseType.ERROR, "Wrong Token or user UUID, please re-login!");
            return ResponseEntity.badRequest().body(GsonUtil.getInstance().toJson(response));
        }

        if(post.image() == null || post.description() == null || post.author() == null){
            JsonResponse response = new JsonResponse(ResponseType.ERROR, "Missing data!");
            return ResponseEntity.badRequest().body(GsonUtil.getInstance().toJson(response));
        }
        
        byte[] imageBytes = Base64.getDecoder().decode(post.image());

        ByteArrayInputStream bis = new ByteArrayInputStream(imageBytes);
        
        try{
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

            post = new Post(compressedBase64Image, post.description(), post.author());

            postRepo.save(post);

            JsonResponse response = new JsonResponse(ResponseType.SUCCESS, "Post created!");
            return ResponseEntity.ok().body(GsonUtil.getInstance().toJson(response));
        } catch(IOException e){
            e.printStackTrace();
            JsonResponse response = new JsonResponse(ResponseType.ERROR, "Error while reading image!");
            return ResponseEntity.internalServerError().body(GsonUtil.getInstance().toJson(response));
        }
    }
}

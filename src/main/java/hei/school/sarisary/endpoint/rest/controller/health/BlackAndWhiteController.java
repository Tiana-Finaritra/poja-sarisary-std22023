package hei.school.sarisary.endpoint.rest.controller.health;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/black-and-white")
public class BlackAndWhiteController {

    @PutMapping("/{id}")
    public ResponseEntity<Void> uploadImage(@PathVariable String id, @RequestParam("file") MultipartFile file) {
        try {
            byte[] imageBytes = file.getBytes();
            ConvertBlackWhite.convertToBlackAndWhite(imageBytes, id);
            return ResponseEntity.ok().build();
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, String>> getImageUrls(@PathVariable String id) {
        String transformedImageUrl = ConvertBlackWhite.getTransformedImageUrl(id);
        Map<String, String> response = new HashMap<>();
        response.put("transformed_url", transformedImageUrl);
        return ResponseEntity.ok(response);
    }

    public static class ConvertBlackWhite {
        public static String convertToBlackAndWhite(byte[] imageBytes, String id) throws IOException {
            ByteArrayInputStream bis = new ByteArrayInputStream(imageBytes);
            BufferedImage image = ImageIO.read(bis);
            BufferedImage blackAndWhiteImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_BYTE_BINARY);
            blackAndWhiteImage.getGraphics().drawImage(image, 0, 0, null);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ImageIO.write(blackAndWhiteImage, "png", bos);
            bos.toByteArray();
            return "https://transformed.url/" + id;
        }

        public static String getTransformedImageUrl(String id) {
            return "https://transformed.url/" + id;
        }
    }
}
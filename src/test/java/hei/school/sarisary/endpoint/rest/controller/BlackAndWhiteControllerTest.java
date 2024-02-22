package hei.school.sarisary.endpoint.rest.controller;

import hei.school.sarisary.endpoint.rest.controller.health.BlackAndWhiteController;
import hei.school.sarisary.endpoint.rest.controller.health.BlackAndWhiteController.ConvertBlackWhite;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BlackAndWhiteControllerTest {

    @Mock
    ConvertBlackWhite convertBlackWhite;

    @InjectMocks
    BlackAndWhiteController blackAndWhiteController;

    @Test
    void testUploadImage_Success() throws IOException {
        // Given
        String id = "123";
        byte[] content = {0x42, 0x4D}; // Example content of a BMP file header
        MultipartFile file = new MockMultipartFile("file", "test.bmp", "image/bmp", content);
        String expectedTransformedImageUrl = "https://transformed.url/" + id;
        when(convertBlackWhite.convertToBlackAndWhite(any(byte[].class), any(String.class))).thenReturn(expectedTransformedImageUrl);

        // When
        ResponseEntity<Void> responseEntity = blackAndWhiteController.uploadImage(id, file);

        // Then
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    void testUploadImage_Failure() throws IOException {
        // Given
        String id = "123";
        byte[] content = {0x42, 0x4D}; // Example content of a BMP file header
        MultipartFile file = new MockMultipartFile("file", "test.bmp", "image/bmp", content);
        when(convertBlackWhite.convertToBlackAndWhite(any(byte[].class), any(String.class))).thenThrow(new IOException());

        // When
        ResponseEntity<Void> responseEntity = blackAndWhiteController.uploadImage(id, file);

        // Then
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
    }

    @Test
    void testGetImageUrls() {
        // Given
        String id = "123";
        String expectedTransformedImageUrl = "https://transformed.url/" + id;
        when(convertBlackWhite.getTransformedImageUrl(any(String.class))).thenReturn(expectedTransformedImageUrl);

        // When
        ResponseEntity<Map<String, String>> responseEntity = blackAndWhiteController.getImageUrls(id);

        // Then
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(expectedTransformedImageUrl, responseEntity.getBody().get("transformed_url"));
    }
}

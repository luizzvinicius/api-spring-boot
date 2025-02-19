package luiz.api.products.utils;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class Utils {
    public boolean isValidImageType(MultipartFile imageFile) {
        // não tem risco de ser nulo porque a validação do dto garante
        return switch (imageFile.getContentType()) {
            case "image/jpeg", "image/png", "image/webp", "image/bmp" -> true;
            default -> throw new RuntimeException("Invalid image type");
        };
    }
}
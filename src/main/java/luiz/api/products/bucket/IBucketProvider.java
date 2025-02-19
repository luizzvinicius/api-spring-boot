package luiz.api.products.bucket;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IBucketProvider {
    String uploadImage(MultipartFile file, String folder);

    List<String> listFiles(String folder);

    void deleteFiles(List<String> imageUrls);

    List<String> updateFiles(List<MultipartFile> localFiles, String folder);
}
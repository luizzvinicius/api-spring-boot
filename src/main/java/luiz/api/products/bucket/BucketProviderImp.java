package luiz.api.products.bucket;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Component
public class BucketProviderImp implements IBucketProvider {
    private final S3BucketImp bucketImp;

    public BucketProviderImp(S3BucketImp bucketImp) {
        this.bucketImp = bucketImp;
    }

    @Override
    public String uploadImage(MultipartFile file, String folder) {
        return bucketImp.uploadImage(file, folder);
    }

    @Override
    public List<String> listFiles(String folder) {
        return bucketImp.listFiles(folder);
    }

    @Override
    public void deleteFiles(List<String> imageUrls) {
        bucketImp.deleteFiles(imageUrls);
    }

    @Override
    public List<String> updateFiles(List<MultipartFile> localFiles, String folder) {
        return bucketImp.updateFiles(localFiles, folder);
    }
}
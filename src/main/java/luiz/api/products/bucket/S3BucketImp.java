package luiz.api.products.bucket;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ListObjectsV2Request;
import com.amazonaws.services.s3.model.ListObjectsV2Result;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import luiz.api.products.exceptions.CustomIOException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class S3BucketImp implements IBucketProvider {
    private final AmazonS3 s3Client;
    @Value("${aws.bucketName}")
    private String bucketName;

    public S3BucketImp(AmazonS3 s3Client) {
        this.s3Client = s3Client;
    }

    @Override
    public String uploadImage(MultipartFile file, String folder) {
        String fileName = String.format("%s/%s", folder, file.getOriginalFilename());
        File convertedFile = this.convertMiltiPartToFile(file);

        s3Client.putObject(bucketName, fileName, convertedFile);

        try {
            Files.deleteIfExists(convertedFile.toPath());
        } catch (IOException e) {
            throw new CustomIOException("Error on delete File: " + e.getMessage());
        }

        return s3Client.getUrl(bucketName, fileName).toString();
    }

    @Override
    public List<String> listFiles(String folder) {
        try {
            ListObjectsV2Result requestResult = s3Client.listObjectsV2(
                    new ListObjectsV2Request().withBucketName(bucketName).withPrefix(folder)
            );
            List<S3ObjectSummary> objects = requestResult.getObjectSummaries();
            return objects.stream().map(obj -> s3Client.getUrl(bucketName, obj.getKey()).toString()).toList();
        } catch (SdkClientException sdkEx) {
            throw new RuntimeException("Error while listing objects " + sdkEx.getMessage());
        }
    }

    @Override
    public void deleteFiles(List<String> imageUrls) {
        List<String> objectKeys = extractFileNameFromUrl(imageUrls);

        try {
            objectKeys.forEach(obj -> s3Client.deleteObject(bucketName, obj));
        } catch (AmazonServiceException e) {
            throw new RuntimeException("Error while deleting objects " + e.getMessage());
        }
    }

    @Override
    public List<String> updateFiles(List<MultipartFile> localFiles, String folder) {
        Map<String, MultipartFile> localFilesMap = localFiles.stream().collect(Collectors
                .toMap(file -> String.format("%s/%s", folder, file.getOriginalFilename()),
                        file -> file
                )); // O(n)

        Set<String> s3FileNames = new HashSet<>(this.listFileNames(folder)); // 2 O(n)
        Set<String> localFileNames = new HashSet<>(this.addFoldertoLocalFile(localFiles, folder)); // 2 O(n)
        Set<String> localFileNamesCopy = Set.copyOf(localFileNames); // O(n) sim ou não

        localFileNames.removeAll(s3FileNames); // O(n)
        s3FileNames.removeAll(localFileNamesCopy); // O(n)

        localFileNames.forEach(localFile -> uploadImage(localFilesMap.get(localFile), folder)); // O(n)

        deleteFiles(s3FileNames.stream().toList()); // O(n)

        return listFiles(folder); // O(n)
    }

    private File convertMiltiPartToFile(MultipartFile file) {
        String name = file.getOriginalFilename().substring(0, file.getOriginalFilename().lastIndexOf("."));
        String extension = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));

        try {
            Path tempFile = Files.createTempFile("", name + extension);
            Files.copy(file.getInputStream(), tempFile, StandardCopyOption.REPLACE_EXISTING);
            return tempFile.toFile();
        } catch (IOException e) {
            throw new CustomIOException("Error on create File: " + e.getMessage());
        }
    }

    private List<String> listFileNames(String folder) {
        try {
            ListObjectsV2Result requestResult = s3Client.listObjectsV2(
                    new ListObjectsV2Request().withBucketName(bucketName).withPrefix(folder)
            );
            List<S3ObjectSummary> objects = requestResult.getObjectSummaries();
            return objects.stream().map(S3ObjectSummary::getKey).toList();
        } catch (SdkClientException sdkEx) {
            throw new RuntimeException("Error while listing objects " + sdkEx.getMessage());
        }
    }

    private List<String> extractFileNameFromUrl(List<String> localFileRequest) {
        return localFileRequest.stream().map(file -> {
            var splitted = file.split("/");
            return String.format("%s/%s", splitted[splitted.length - 2], splitted[splitted.length - 1]);
        }).toList();
    }

    private List<String> addFoldertoLocalFile(List<MultipartFile> localFileRequest, String folder) {
        return localFileRequest.stream().map(file -> String.format("%s/%s", folder, file.getOriginalFilename())).toList();
    }
}
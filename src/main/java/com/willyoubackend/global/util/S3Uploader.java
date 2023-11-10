package com.willyoubackend.global.util;


import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Component
@Service
public class S3Uploader {
    private final AmazonS3Client amazonS3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    public String upload(MultipartFile multipartFile, String dirName) throws IOException {
        File uploadFile = convert(multipartFile)
                .orElseThrow(() -> new IllegalArgumentException("MultipartFile -> File 전환 실패"));
        return upload(uploadFile, dirName);
    }

    private String upload(File uploadFile, String dirName) {
        log.info(uploadFile.getName());
        String fileName = dirName + "/" + uploadFile.getName();
        String uploadImageUrl = putS3(uploadFile, fileName);
        removeNewFile(uploadFile);
        return uploadImageUrl;
    }

    private String putS3(File uploadFile, String fileName) {
        amazonS3Client.putObject(
                new PutObjectRequest(bucket, fileName, uploadFile)
                        .withCannedAcl(CannedAccessControlList.PublicRead)
        );
        return amazonS3Client.getUrl(bucket, fileName).toString();
    }

    private void removeNewFile(File targetFile) {
        if (targetFile.delete()) {
            log.info("파일이 삭제되었습니다.");
        } else {
            log.info("파일이 삭제되지 못했습니다.");
        }
    }

    private Optional<File> convert(MultipartFile file) throws IOException {
        if (imageFormatCheck(file)) {
            BufferedImage heicBufferedImage = ImageIO.read(new ByteArrayInputStream(file.getBytes()));
            BufferedImage jpgBufferedImage = new BufferedImage(heicBufferedImage.getWidth(), heicBufferedImage.getHeight(), BufferedImage.TYPE_INT_RGB);
            jpgBufferedImage.createGraphics().drawImage(heicBufferedImage, 0, 0, null);

            File jpgFile = new File(Objects.requireNonNull(file.getOriginalFilename()));
            ImageIO.write(jpgBufferedImage, "jpg", jpgFile);

            return Optional.of(jpgFile);
        } else {
            File convertFile = new File(Objects.requireNonNull(file.getOriginalFilename()));
            if (convertFile.createNewFile()) {
                try (FileOutputStream fos = new FileOutputStream(convertFile)) {
                    fos.write(file.getBytes());
                }
                return Optional.of(convertFile);
            }
            return Optional.empty();
        }
    }

    private Boolean imageFormatCheck(MultipartFile file) throws IOException {
        byte[] headerBytes = new byte[8]; // 최소 8바이트 필요
        int bytesRead = file.getInputStream().read(headerBytes);

        if (bytesRead >= 8 && isHEIC(headerBytes)) {
            return true;
        } else {
            return false;
        }
    }

    private boolean isHEIC(byte[] headerBytes) {
        if (headerBytes.length < 12) {
            return false;
        }
        return (headerBytes[4] == 0x66 && headerBytes[5] == 0x74 &&
                headerBytes[6] == 0x79 && headerBytes[7] == 0x70 &&
                headerBytes[8] == 0x68 && headerBytes[9] == 0x65 &&
                headerBytes[10] == 0x69 && headerBytes[11] == 0x63);
    }

    public void delete(String url) {
        String[] urlArray = url.split("/");
        String fileName = String.join("/",Arrays.copyOfRange(urlArray,3,urlArray.length));
        amazonS3Client.deleteObject(bucket, fileName);
    }
}

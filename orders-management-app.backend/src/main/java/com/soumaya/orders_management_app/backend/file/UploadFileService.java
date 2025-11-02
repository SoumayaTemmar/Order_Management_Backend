package com.soumaya.orders_management_app.backend.file;

import com.soumaya.orders_management_app.backend.ExceptionHandling.FileStorageException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
@RequiredArgsConstructor
@Slf4j
public class UploadFileService {

    @Value("${application.file.upload.photo-output-path}")
    private String uploadDir;

    public String saveFile(MultipartFile imageFile){

        String extension = getExtension(imageFile);

        String uniqueImageName = extension.isEmpty()
                ?String.valueOf(System.currentTimeMillis())
                :System.currentTimeMillis()+ "." + extension;

        String finalFilePath = uploadDir + File.separator + uniqueImageName;
        Path filePath = Paths.get(finalFilePath);

        // create directories
        File folder = new File(uploadDir);
        if (!folder.exists()){

            boolean created = folder.mkdirs();
            if (!created){
                log.warn("Failed to create the target folder");
                throw new FileStorageException("image non enregistrée," +
                        " error lors de la creation du dossier" + uploadDir);
            }
        }
        try {
            Files.write(filePath, imageFile.getBytes());
            log.info("file created");
            return "uploads/"+ uniqueImageName;

        } catch (IOException e) {
            log.error("file was not created", e);
            throw new FileStorageException("image non enregistrée");
        }

    }

    private String getExtension(MultipartFile imageFile){
        //get the image name
        String originalFilename = imageFile.getOriginalFilename();

        if (originalFilename == null || originalFilename.isEmpty()){
            return "";
        }
        // get the dot index
        int index = originalFilename.lastIndexOf(".");

        if (index == -1){
            return "";
        }
        // return the extension
        return originalFilename.substring(index + 1);

    }

    public void deleteImage(String imagePath){

        //check if there is an image to delete or not
        if (imagePath == null || imagePath.isBlank()){
            return;
        }

        //generate the actual path
        Path path = Paths.get(uploadDir, imagePath.replace("uploads/",""));

        try{
            Files.deleteIfExists(path);
            log.info("deleted image: {}",path);
        } catch (IOException e) {
            log.error("failed to delete image: {}",path, e);
        }
    }
}

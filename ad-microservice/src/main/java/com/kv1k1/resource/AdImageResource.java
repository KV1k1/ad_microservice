package com.kv1k1.resource;

import jakarta.annotation.security.RolesAllowed;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.jboss.resteasy.reactive.RestForm;
import org.jboss.resteasy.reactive.multipart.FileUpload;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Path("/ads/admin")
@RolesAllowed("ADMIN")
public class AdImageResource {

    // These should point to your Apache web server directory
    private static final String UPLOAD_DIR = "/var/www/html/ads/images/";
    private static final String BASE_URL = "http://192.168.1.170/ads/images/";

    @POST
    @Path("/upload-image")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public Response uploadAdImage(@RestForm("file") FileUpload file) {
        try {
            if (file == null || file.fileName() == null || file.fileName().isEmpty()) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("{\"error\":\"No file provided\"}")
                        .build();
            }

            if (!isValidImageFile(file)) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("{\"error\":\"Invalid file type. Only images are allowed.\"}")
                        .build();
            }

            String fileName = saveImageFile(file);
            String imageUrl = BASE_URL + fileName;

            return Response.ok()
                    .entity("{\"imageUrl\":\"" + imageUrl + "\"}")
                    .build();

        } catch (IOException e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\":\"Failed to upload image: " + e.getMessage() + "\"}")
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\":\"Unexpected error: " + e.getMessage() + "\"}")
                    .build();
        }
    }

    private boolean isValidImageFile(FileUpload file) {
        String fileName = file.fileName().toLowerCase();
        return fileName.endsWith(".jpg") || fileName.endsWith(".jpeg") ||
                fileName.endsWith(".png") || fileName.endsWith(".gif") ||
                fileName.endsWith(".bmp") || fileName.endsWith(".webp");
    }

    private String saveImageFile(FileUpload file) throws IOException {
        String originalFileName = file.fileName();
        String fileExtension = getFileExtension(originalFileName);
        String uniqueFileName = UUID.randomUUID().toString() + fileExtension;

        // Create target path
        java.nio.file.Path targetPath = Paths.get(UPLOAD_DIR, uniqueFileName);

        // Ensure the directory exists
        Files.createDirectories(targetPath.getParent());

        // Copy the file using InputStream (more reliable approach)
        try (InputStream inputStream = Files.newInputStream(file.uploadedFile())) {
            Files.copy(inputStream, targetPath, StandardCopyOption.REPLACE_EXISTING);
        }

        return uniqueFileName;
    }

    private String getFileExtension(String fileName) {
        if (fileName != null && fileName.contains(".")) {
            return fileName.substring(fileName.lastIndexOf("."));
        }
        return ".jpg";
    }
}
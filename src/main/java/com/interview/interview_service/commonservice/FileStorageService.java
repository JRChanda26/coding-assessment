package com.interview.interview_service.commonservice;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class FileStorageService {
    

    private final Path fileStorageLocation;


     @Autowired
  public FileStorageService(Environment env) {
    this.fileStorageLocation = Paths.get(env.getProperty("app.file.upload-dir", "./uploads/files"))
        .toAbsolutePath().normalize();

    try {
      Files.createDirectories(this.fileStorageLocation);
    } catch (Exception ex) {
      throw new RuntimeException(
          "Could not create the directory where the uploaded files will be stored.", ex);
    }
  }

    public String downloadAndStoreFile(String fileUrl, String meetingId) {
    try {
        // Create a URL object
        URL url = new URL(fileUrl);
        
        // Open a connection to the URL
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        
        // Check if the connection was successful
        if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
            // Get the input stream from the connection
            InputStream inputStream = connection.getInputStream();
            
            // Create the directory for the meeting ID if it doesn't exist
            Path meetingDir = this.fileStorageLocation.resolve(meetingId);
            if (!Files.exists(meetingDir)) {
                Files.createDirectories(meetingDir);
            }
            
            // Create a file path to store the downloaded file
            String fileName = getFileNameFromUrl(fileUrl);
            Path targetLocation = meetingDir.resolve(fileName);
            
            // Copy the input stream to the target file
            Files.copy(inputStream, targetLocation, StandardCopyOption.REPLACE_EXISTING);
            
            // Close the input stream
            inputStream.close();
            
            return targetLocation.toString();
        } else {
            throw new RuntimeException("Failed to download file from URL: " + fileUrl);
        }
    } catch (Exception ex) {
        throw new RuntimeException("Error downloading file from URL: " + fileUrl, ex);
    }
}

private String getFileNameFromUrl(String fileUrl) {
  // Extract the part of the URL after the last '/'
  String fileNameWithQuery = fileUrl.substring(fileUrl.lastIndexOf("/") + 1);
  
  // Remove query parameters (anything after '?')
  String fileName = fileNameWithQuery.split("\\?")[0];
  
  return fileName;
}
}

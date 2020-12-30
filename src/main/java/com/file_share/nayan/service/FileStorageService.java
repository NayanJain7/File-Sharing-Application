package com.file_share.nayan.service;



import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.file_share.nayan.exception.FileStorageException;
import com.file_share.nayan.exception.MentionedFileNotFoundException;
import com.file_share.nayan.pojo.FileStoragePojo;



@Service
public class FileStorageService {

    private final Path fileStorageLocation;
	
	
	
	@Autowired
    public FileStorageService(FileStoragePojo fileStoragePojo) {
        this.fileStorageLocation = Paths.get(fileStoragePojo.getUploadDir())
                .toAbsolutePath().normalize();

        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception ex) {
            throw new FileStorageException("Unable to create the directory where the uploaded files will be stored.", ex);
        }
    }
	
	
	 public  Map<String, String> storeFile(MultipartFile file) {
	        // Normalize file name
			String filesName = StringUtils.cleanPath(file.getOriginalFilename()); 
	        
		 // make unique file name
	        String fileName= Math.round(Math.random()*1E9)+"-"+new Date().getTime()+"."+StringUtils.getFilenameExtension(filesName);
	        
	        
	        
	        Map<String,String> map = new HashMap<String,String>();
	  
	        try {
	            // Check if the file's name contains invalid characters
	            if(filesName.contains("..")) {
	                throw new FileStorageException("Sorry! Filename contains invalid path sequence " + fileName);
	            }

	            // Copy file to the target location (Replacing existing file with the same name)
	            Path targetLocation = this.fileStorageLocation.resolve(fileName);
	            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
	            
	            String filePath = targetLocation.toString();
	            
	            map.put("fileName", fileName);
	            map.put("filePath", filePath);
	            
	            
	            return map;
	            
	        } catch (IOException ex) {
	            throw new FileStorageException("Could not store file " + fileName + ". Please try again!", ex);
	        }
	    }

	
	   public Resource loadFileAsResource(String fileName) {
	        try {
	            Path filePath = this.fileStorageLocation.resolve(fileName).normalize();
	            
	            Resource resource = new UrlResource(filePath.toUri());
	            if(resource.exists()) {
	            	System.out.println("resource is => "+resource);
	            	System.out.println("filepath is => "+filePath);
	                return resource;
	                
	            } else {
	                throw new MentionedFileNotFoundException("File not found " + fileName);
	            }
	        } catch (MalformedURLException ex) {
	            throw new MentionedFileNotFoundException("File not found " + fileName, ex);
	        }
	    }
	
	
}

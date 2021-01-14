package com.file_share.nayan.controller;
import java.io.IOException;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.file_share.nayan.Dao.FileRepository;
import com.file_share.nayan.EmailService.SendEmail;
import com.file_share.nayan.pojo.EmailDetails;
import com.file_share.nayan.pojo.UploadFileResponse;
import com.file_share.nayan.service.FileStorageService;

@RestController
public class Home {

	@Autowired
    private FileStorageService fileStorageService;

	@Autowired
	private FileRepository fileRepo;
	
	
	 @PostMapping("/upload-single-file")
	    public UploadFileResponse uploadSingleFile(@RequestParam("file") MultipartFile file) {
		 
		 Map<String, String> map = fileStorageService.storeFile(file);
	        
	        String uuid = UUID.randomUUID().toString();
	        
			
			/*
			 * String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
			 * .path("/download-file/") .path(fileName) .toUriString();
			 */
			 
			 String shareLink = ServletUriComponentsBuilder.fromCurrentContextPath()
					  .path("/file/") .path(uuid) .toUriString();
	        
	        
	         UploadFileResponse uploadFileResponse = new UploadFileResponse(uuid,map.get("fileName"),file.getContentType(), file.getSize(),map.get("filePath"),shareLink);
	         
	         fileRepo.save(uploadFileResponse);
	         
	         
	         
	         return(uploadFileResponse);
	    }
	 
	 @PostMapping("/upload-multiple-files")
	    public List<UploadFileResponse> uploadMultipleFiles(@RequestParam("files") MultipartFile[] files) {
	        return Arrays.asList(files)
	                .stream()
	                .map(file -> uploadSingleFile(file))
	                .collect(Collectors.toList());
	    }
	 
	
	 @PostMapping(value="/upload-file/send")
	 	public ResponseEntity<?> sendEmail(@RequestBody EmailDetails emailDetails) {
		 
		 
		  UploadFileResponse uploadFileResponse = fileRepo.findById(emailDetails.getUuid()).get();
		 
		  SendEmail.setFileSize(uploadFileResponse.getSize());
		  SendEmail.setShareLink(uploadFileResponse.getShareLink());
		  System.out.println("getEmail to "+emailDetails.getEmailTo()+" getEmail from is "+emailDetails.getEmailFrom());
		boolean isSendMail = SendEmail.sendMail(emailDetails.getEmailTo(), emailDetails.getEmailFrom());
		
		if(isSendMail) {
				
			 return ResponseEntity.ok("true");
			 
		}
		else {
			
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("false");
		}
		
	 }

	
	    @GetMapping("/download-file/{fileName:.+}")
	    public ResponseEntity<Resource> downloadFile(@PathVariable String fileName, HttpServletRequest request) {
	        // Load file as Resource
	    	
	    
	        Resource resource = fileStorageService.loadFileAsResource(fileName);

	      
	        // Try to determine file's content type
	        String contentType = null;
	        try {
	            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
	        } catch (IOException ex) {
	            System.out.print("Could not determine file type.");
	        }

	        // Fallback to the default content type if type could not be determined
	        if(contentType == null) {
	            contentType = "application/octet-stream";
	        }

	        return ResponseEntity.ok()
	                .contentType(MediaType.parseMediaType(contentType))
	                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
	                .body(resource);
	    }

	    @GetMapping("/file/{uuid}")
	    public ModelAndView showDownloadPage(@PathVariable("uuid") String uuid) {
	    	
	    	UploadFileResponse uploadFileResponse = fileRepo.findById(uuid).get();
	    	
	    	ModelAndView modelAndView = new ModelAndView();
	    	modelAndView.setViewName("downloadPage");
	    	modelAndView.addObject("fileName",uploadFileResponse.getFileName());
	    	modelAndView.addObject("fileSize",Math.round(uploadFileResponse.getSize()/1024));
	    	
	    	
	    	return modelAndView;
	    }
	   
	
	   
		 
		  
		  @GetMapping("/showVar") public String show() {
		  System.out.println("-----------------------");
			  
		  
		  
		  return SendEmail.showDetails();
		  
		  }
		 

}

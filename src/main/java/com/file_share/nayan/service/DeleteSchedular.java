package com.file_share.nayan.service;

import java.io.File;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.file_share.nayan.Dao.FileRepository;
import com.file_share.nayan.pojo.UploadFileResponse;

@Component
public class DeleteSchedular {
	
	
	@Autowired
	private FileRepository fileRepo;
	
	// time is in seconds that is 24 hour 
	@Scheduled(fixedDelay = 24 * 60 * 60 * 1000 * 10 )
	public void deleteFile() {
	
		
		
		long pastDate = new Date().getTime() - 24 * 60 * 60 * 1000;
		
		List<UploadFileResponse> oldFiles = fileRepo.findFilesByCreated(pastDate);
		
		for (UploadFileResponse file : oldFiles) {
			  String filePath = file.getFilePath();
			  
			  File f = new File(filePath);
			  try {
				  
				  UploadFileResponse uploadFileResponse = fileRepo.findById(file.getUuid()).get();
				  fileRepo.delete(uploadFileResponse);
				  f.delete();
				  System.out.println("File deleted from everywhere");
			  }
			  catch(Exception e) {
				  System.out.println("------------------------------");
				  System.out.println("File deleted error in Delete schdeular");
				  e.printStackTrace();
				  System.out.println("------------------------------");
			  }
		}
		
	}

}

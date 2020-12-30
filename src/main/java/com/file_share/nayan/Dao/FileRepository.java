package com.file_share.nayan.Dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.file_share.nayan.pojo.UploadFileResponse;


public interface FileRepository extends JpaRepository<UploadFileResponse, String> {
	
	@Query("from UploadFileResponse as ufr where ufr.created <:pastDate")
	public List<UploadFileResponse> findFilesByCreated(@Param("pastDate") long pastDate);

}

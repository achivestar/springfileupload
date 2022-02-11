package org.zerock.web;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

@Controller
public class UploadController {

	private static final Logger logger = LoggerFactory.getLogger(UploadController.class);
	
	@RequestMapping(value="/uploadForm", method=RequestMethod.GET)
	public void uploadForm() {

	}
	
	
	@RequestMapping(value="/uploadForm", method=RequestMethod.POST)
	public String uploadForm(@RequestParam("input_file") List<MultipartFile> multipartFile, Model model, HttpServletRequest request) throws IOException {
		
		
		String root_path = request.getSession().getServletContext().getRealPath("/");  
		String attach_path = "resources/upload/";
		String UPLOAD_PATH = root_path+attach_path;
		
		
		for(MultipartFile file:multipartFile) {
			logger.info("originalName : " + file.getOriginalFilename());
			logger.info("size : "+ file.getSize());
			logger.info("contentType : "+ file.getContentType());
			
		
			String savedName = uploadFile(file.getOriginalFilename(),file.getBytes(),UPLOAD_PATH);
			
			model.addAttribute("savedName",savedName);
		}
	
		return "uploadResult";
		
		
		
	}
	
	
	
	
	@RequestMapping(value="uploadAjax", method = RequestMethod.GET) 
	public void uploadAjax() {
		
	}
	
	
	@RequestMapping(value="uploadAjax2", method = RequestMethod.GET) 
	public void uploadAjax2() {
		
	}
	
	
	@ResponseBody
	@RequestMapping(value = "/file-upload", method = RequestMethod.POST)
	public String fileUpload(
			@RequestParam("article_file") List<MultipartFile> multipartFile
			, HttpServletRequest request) {
		
		String strResult = "{ \"result\":\"FAIL\" }";
		String contextRoot = new HttpServletRequestWrapper(request).getRealPath("/");
		String fileRoot;
		
		try {
			// 파일이 있을때 탄다.
			if(multipartFile.size() > 0 && !multipartFile.get(0).getOriginalFilename().equals("")) {
				
				for(MultipartFile file:multipartFile) {
					fileRoot = contextRoot + "resources/upload/";
					System.out.println("fileRoot"+fileRoot);
					
					String originalFileName = file.getOriginalFilename();	//오리지날 파일명
					String extension = originalFileName.substring(originalFileName.lastIndexOf("."));	//파일 확장자
					String savedFileName = UUID.randomUUID() + extension;	//저장될 파일 명
					
					File targetFile = new File(fileRoot + savedFileName);	
					try {
						InputStream fileStream = file.getInputStream();
						FileUtils.copyInputStreamToFile(fileStream, targetFile); //파일 저장
						
					} catch (Exception e) {
						//파일삭제
						FileUtils.deleteQuietly(targetFile);	//저장된 현재 파일 삭제
						e.printStackTrace();
						break;
					}
				}
				strResult = "{ \"result\":\"OK\" }";
			}
			// 파일 아무것도 첨부 안했을때 탄다.(게시판일때, 업로드 없이 글을 등록하는경우)
			else
				strResult = "{ \"result\":\"OK\" }";
		}catch(Exception e){
			e.printStackTrace();
		}
		return strResult;
	}
	
	
	
	
	@ResponseBody
	@RequestMapping(value="/uploadAjax", method=RequestMethod.POST)
	public ResponseEntity<String> uploadAjax(@RequestParam("input_file") List<MultipartFile> multipartFile, Model model, HttpServletRequest request) throws IOException {
		
	
		String root_path = request.getSession().getServletContext().getRealPath("/");  
		String attach_path = "resources/upload/";
		String UPLOAD_PATH = root_path+attach_path;
		ResponseEntity<String> entity = null;
		for(MultipartFile file:multipartFile) {
			
			
			logger.info("originalName : " + file.getOriginalFilename());
			logger.info("size : "+ file.getSize());
			logger.info("contentType : "+ file.getContentType());
			
		
			String savedName = uploadFile(file.getOriginalFilename(),file.getBytes(),UPLOAD_PATH);
			
			model.addAttribute("savedName",savedName);
			
			entity = new ResponseEntity<String>("success",HttpStatus.OK);
			
		}
		return entity;
	
	
		
		
		
	}
	


	
	@RequestMapping(value = "/multipartUpload.do", method = RequestMethod.POST) 
	public @ResponseBody Map<String, Object> multipartUpload(MultipartHttpServletRequest request) throws Exception { 
		List<MultipartFile> fileList = request.getFiles("file"); 

		for (MultipartFile mf : fileList) { 
			String originFileName = mf.getOriginalFilename(); // 원본 파일 명 
			
			System.out.println(originFileName);
			
		}
		return null;

	}


		
		
	private String uploadFile(String originalFilename, byte[] fileData,String upload_path) throws IOException {
		
		
		
		UUID uid = UUID.randomUUID();
		
		String savedName = uid.toString()+"_"+originalFilename;
		
		File target = new File(upload_path,savedName);
		
		FileCopyUtils.copy(fileData, target);
		
		return savedName;
	}
}

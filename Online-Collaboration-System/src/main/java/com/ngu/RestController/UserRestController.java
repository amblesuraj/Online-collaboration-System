/**
 * 
 */
package com.ngu.RestController;

import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ngu.Model.AppResponse;
import com.ngu.Model.User;
import com.ngu.Model.UserModel;
import com.ngu.Service.FileStorageService;
import com.ngu.Service.UserService;

/**
 * @author SURAJ
 *@Date Nov 26, 2019
 */
@Controller

public class UserRestController {

	@Autowired
	UserService userService;
	@Autowired
	FileStorageService fileStorageService;
	ObjectMapper objectMapper = new ObjectMapper();		
	@Autowired
	HttpSession session;
	
	@PostMapping(value = "/user/saveProfileImage",consumes = MediaType.ALL_VALUE)
	public ResponseEntity<?> saveProfilePicture(@RequestParam(required = true, value = "profileImage") MultipartFile file,
			@RequestParam Map<MultipartFile,String> params)throws JsonParseException,JsonMappingException
	{
		
		try
		{
			
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			
			
				User user = userService.findByUsername(authentication.getName());
				if(file.isEmpty()) {
					System.out.println("user profile picture path "+user.getProfilePicture());
					user.setProfilePicture(user.getProfilePicture());
					
				}
		
				
				
				if(user != null) {
				
					
					String url = user.getProfilePicture();
					System.out.println("Url from db"+url);
					String profilePath = url.substring( url.lastIndexOf('/')+1, url.length() );
					System.out.println("Extract file name :"+profilePath);
					if(!url.isEmpty()) {
						fileStorageService.deleteFile(profilePath);	
					}
//					
					
					
					System.out.println(user.getProfilePicture());
				
				String fileName = fileStorageService.storeFile(file);
				String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath().path("/downloadFile/")
						.path(fileName).toUriString();
				 
				user.setProfilePicture(fileDownloadUri);
				userService.update(user);
				UserModel userModel = new UserModel();
				
				userModel.setId(user.getId());
				userModel.setFname(user.getFname());
				userModel.setLname(user.getLname());
				userModel.setFullName(user.getFname() + " " + user.getLname());
				userModel.setEmail(user.getEmail());
				userModel.setUsername(user.getUsername());
				userModel.setProfilePicture(user.getProfilePicture());
				userModel.setPhone(user.getPhone());
				userModel.setUser(user);
				
				session.setAttribute("userModel", userModel);
				
				return new ResponseEntity<>("success",HttpStatus.OK);
				}
		}
		catch (Exception e) {
			// TODO: handle exception
		}
		
		
		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	}
	
	
	
	
	
	
	
}

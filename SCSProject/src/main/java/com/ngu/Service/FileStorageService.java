/**
 * 
 */
package com.ngu.Service;

import java.io.IOException;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author SURAJ
 *@Date Jan 21, 2020
 */
public interface FileStorageService
{

	/**
	 * @param file
	 * @return
	 * @throws IOException
	 */
	String storeFile(MultipartFile file) throws IOException;

	/**
	 * @param fileName
	 * @return
	 */
	Resource loadFileAsResource(String fileName);

}

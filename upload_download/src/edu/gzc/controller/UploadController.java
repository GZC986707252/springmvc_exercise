package edu.gzc.controller;

import java.io.File;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;

import edu.gzc.pojo.MultipleFiles;
import edu.gzc.pojo.SingleFile;

@Controller
@RequestMapping("/upload")
public class UploadController {
	// 创建日志记录对象
	private static final Log logger = LogFactory.getLog(UploadController.class);

	/**
	 * 单文件上传处理
	 * 
	 * @param singleFile
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/singlefile", method = RequestMethod.POST)
	public String singleFileUpload(@ModelAttribute SingleFile singleFile, HttpServletRequest request) {

		// 获取文件上传到具体文件夹的绝对路径
		String realpath = request.getServletContext().getRealPath("upload");
		// 获取上传的文件名
		String fileName = singleFile.getMyfile().getOriginalFilename();
		// 根据路径构建文件对象
		File uploadFile = new File(realpath, fileName);
		// 判断指定文件夹uploadfiles是否存在，不存在就创建
		if (!uploadFile.exists()) {
			uploadFile.mkdir();
		}
		// 上传文件
		try {
			singleFile.getMyfile().transferTo(uploadFile);
			logger.info("上传成功");
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return "result";
	}

	@RequestMapping("/multipartfile")
	public String multipartFileUpload(@ModelAttribute MultipleFiles multipleFiles, HttpServletRequest request) {
		// 获取上传路径
		String realPath = request.getServletContext().getRealPath("upload");
		// 构建文件对象
		File uploadDir = new File(realPath);
		// 判断文件夹是否存在，不存在则创建
		if (!uploadDir.exists()) {
			uploadDir.mkdir();
		}
		// 获取上传文件列表
		List<MultipartFile> myfiles = multipleFiles.getMyfiles();
		// 上传文件
		for (MultipartFile myfile : myfiles) {
			File targetFile = new File(realPath, myfile.getOriginalFilename());
			try {
				myfile.transferTo(targetFile);
				logger.info(myfile.getOriginalFilename() + "上传成功");
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		}
		return "result";
	}
}

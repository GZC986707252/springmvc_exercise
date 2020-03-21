package edu.gzc.controller;

import java.io.File;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import edu.gzc.pojo.SingleFile;

@Controller
@RequestMapping("/upload")
public class SingleFileUploadController {
	// 创建日志记录对象
	private static final Log logger = LogFactory.getLog(SingleFileUploadController.class);

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
}

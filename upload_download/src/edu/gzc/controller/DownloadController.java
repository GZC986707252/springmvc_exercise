package edu.gzc.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class DownloadController {
	// 创建日志记录对象
	private static final Log logger = LogFactory.getLog(DownloadController.class);

	@RequestMapping("/showDownloadFiles")
	public String showDownloadFiles(Model model, HttpServletRequest request) {
		// 获取下载的文件路径
		String realPath = request.getServletContext().getRealPath("upload");
		// 获得文件对象
		File dir = new File(realPath);
		// 获取文件夹中的所有文件(包含目录)
		File[] files = dir.listFiles();
		// 获取所有文件的文件名
		ArrayList<String> fileNames = new ArrayList<>();
		for (int i = 0; i < files.length; i++) {
			if (files[i].isFile()) { // 排除文件夹
				fileNames.add(files[i].getName());
			}
		}
		model.addAttribute("fileNames", fileNames);
		return "result";
	}

	/**
	 * 执行下载
	 * 
	 * @param filename
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/download")
	public String downloadFile(@RequestParam String filename, HttpServletRequest request,
			HttpServletResponse response) {
		// 获取下载的文件路径
		String uploadFilePath = request.getServletContext().getRealPath("upload");
		// 设置下载文件时的响应报头
		response.setHeader("Content-Type", "application/x-msdownload");
		response.setHeader("Content-Disposition", "attachment;filename=" + toUTF8String(filename));

		try {
			// 获取文件输入流
			FileInputStream in = new FileInputStream(new File(uploadFilePath, filename));

			// 获得响应对象的输出流，用于向客户端输出二进制数据
			ServletOutputStream out = response.getOutputStream();
			out.flush();

			int aRead = 0;
			byte[] b = new byte[1024];
			// 写到响应输出流
			while ((aRead = in.read(b)) != -1 && in != null) {
				out.write(b, 0, aRead);
			}
			out.flush();
			// 关闭IO对象
			in.close();
			out.close();
			logger.info("下载成功");
		} catch (Throwable e) {
			// TODO: handle exception
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * 下载时保存中文文件名的字符编码转换方法
	 */
	public String toUTF8String(String str) {
		StringBuffer sb = new StringBuffer();
		int len = str.length();
		for (int i = 0; i < len; i++) {
			// 取出字符串中的每个字符
			char c = str.charAt(i);
			// Unicode码值为0-255时，不作处理
			if (c >= 0 && c <= 255) {
				sb.append(c);
			} else {
				// 转换成utf-8编码
				byte[] b;
				try {
					b = Character.toString(c).getBytes("UTF-8");
				} catch (UnsupportedEncodingException e) {
					// TODO: handle exception
					e.printStackTrace();
					b = null;
				}
				// 转换为%HH的字符串形式
				for (int j = 0; j < b.length; j++) {
					int k = b[j];
					if (k < 0) {
						k &= 255;
					}
					sb.append("%" + Integer.toHexString(k).toUpperCase());
				}
			}
		}
		return sb.toString();
	}
}

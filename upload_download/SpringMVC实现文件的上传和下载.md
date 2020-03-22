@[TOC](SpringMVC实现文件的上传和下载)

> [练习源码【upload_download】](https://gitee.com/zchao666/springmvc_exercise)

## 文件上传
SpringMVC 的文件上传是基于commons-fileupload组件的文件上传。SpringMVC在原有文件上传组件做了进一步封装，简化了文件上传的代码实现。
因此，实现文件上传我们需要以下相关jar包：

- [commons-fileupload.jar](https://mvnrepository.com/artifact/commons-fileupload/commons-fileupload)
- [commons-io.jar](https://mvnrepository.com/artifact/commons-io/commons-io)

### 基于表单的文件上传
```html
<form action="xxx" method="post" enctype="multipart/form-data">
	选择文件:<input type="file" name="myfile"><br>
	......
	<input type="submit" value="提交">
</form>
```
要注意的是，基于表单的文件上传，需要设置`enctype`属性，并将它的值设置为`multipart/form-data`，同时将表单设置为`post`提交方式。
表单的`enctype`属性指定的是表单数据的编码方式，该属性有3个值：

- `application/x-www-form-urlencoded` ：默认编码方式，只处理表单域的value属性值。
- `multipart/form-data` ：该编码方式以二进制流的方式处理表单数据，并将文件域指定文件的内容封装到请求参数里。
- `text/plain` ：该编码方式只有在`action`属性值为`mailto:URL`的形式时才使用，主要适用于直接通过表单发送邮件的方式。

### MultipartFile 接口
在SpringMVC中，上传文件时将文件的相关信息以及操作封装到了`MultipartFile`对象中，因此，只需要使用`MultipartFile`类型声明模型类的一个属性即可对被上传文件进行操作。

**`MultipartFile`接口的相关方法如下：**

- `byte[] getBytes()` ：以字节数组的形式返回文件内容。
- `String getContentType()` ：返回文件内容类型。
- `InputStream getInputStream()` ：返回一个`InputStream`，从中读取文件内容。
- `String getName()` ：返回请求参数的名称。
- `String getOriginalFilename()` ：返回上传文件的文件名。
- `long getSize()` ：返回文件的大小，单位为字节。
- `boolean isEmpty()` ：判断被上传文件是否为空。
- `void transferTo(File destination)` ：将上传文件保存到目标目录下。

### 配置
在上传文件时需要在配置文件中对`org.springframework.web.multipart.commons.CommonsMultipartResolver`类进行相关配置。

```html
	<!-- 使用Spring的commonsMultipartResovler配置MultipartResovler用于文件上传  -->
	<bean id="multipartResolver" class="org.springframework.web.multipart.commons.CommonsMultipartResolver">
		<!-- 设置请求的编码格式, 默认为iso-8859-1 -->
		<property name="defaultEncoding" value="utf-8"/>
		<!-- 设置允许上传文件的最大值, 单位为 字节 -->
		<property name="maxUploadSize" value="5400000"/>
		<!-- 设置上传文件的临时路径 -->
		<property name="uploadTempDir" value="upload/temp"/>
	</bean>
```
### 单文件上传Demo
- **创建单文件的上传模型类 SingleFile.java**
```java
public class SingleFile {
	private String description;
	private MultipartFile myfile;
	//省略getter 和setter方法
}
```
- **创建前台页面选择文件上传 oneFileUpload.jsp**
```java
<form action="${ pageContext.request.contextPath }/upload/singlefile" method="post" enctype="multipart/form-data">
	选择文件:<input type="file" name="myfile"><br>
	文件描述：<textarea rows="3" name="description"></textarea><br>
	<input type="submit" value="提交">
</form>
```
- **编写处理上传文件的控制器 UploadController.java**

```java
@Controller
@RequestMapping("/upload")
public class UploadController {
	// 创建日志记录对象
	private static final Log logger = LogFactory.getLog(UploadController.class);
	/**
	 * 单文件上传处理 
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
			e.printStackTrace();
		}
		return "result";
	}
```
### 多文件上传Demo
- **创建单文件的上传模型类 MultipleFiles.java**
```java
public class MultipleFiles {
	private List<String> description;
	private List<MultipartFile> myfiles;
	//省略getter 和setter方法
}
```
- **创建前台页面选择文件上传 multipartFileUpload.jsp**
```html
<form action="${ pageContext.request.contextPath }/upload/multipartfile" method="post" enctype="multipart/form-data">
	<fieldset>
		<legend>上传文件1</legend>
		选择文件:<input type="file" name="myfiles"><br>
		文件描述：<textarea rows="3" name="description"></textarea><br>
	</fieldset>
	<fieldset>
		<legend>上传文件2</legend>
		选择文件:<input type="file" name="myfiles"><br>
		文件描述：<textarea rows="3" name="description"></textarea><br>
	</fieldset>
	<fieldset>
		<legend>上传文件3</legend>
		选择文件:<input type="file" name="myfiles"><br>
		文件描述：<textarea rows="3" name="description"></textarea><br>
	</fieldset>
	<input type="submit" value="提交">
</form>
```
- **编写多文件上传处理方法**
```java
@Controller
@RequestMapping("/upload")
public class UploadController {
	//省略单文件上传处理方法
	
	/**
	 * 多文件上传处理
	 */
	@RequestMapping(value = "/multipartfile", method = RequestMethod.POST)
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
				e.printStackTrace();
			}
		}
		return "result";
	}	
}
```
## 文件下载
文件下载的实现有两种方式，一种是通过超链接实现下载，另一种是通过程序编码方式实现下载。前者虽然实现简单，但暴露了文件的真实位置，而且文件需要放在web程序的目录下才能下载。后者能够增加安全访问控制，还可以从任意位置提供下载数据，可以将文件存放web程序目录以外，也可以将文件存放在数据库中。

**通过程序编码实现下载需要设置两个响应报头：**

- `Content-Type`  ：告诉浏览器其输出的内容不是普通文本文件或者HTML文件，而是一个保存到本地的文件，需要设置该报头的值为`application/x-msdownload`。在不知道文件的mime类型时，也可以设置值为`application/octet-stream`，该属性值表示任意的字节流。
- `Content-Disposition`  ：告诉浏览器不直接处理相应实体内容，由用户选择将相应的实体内容保存到一个文件中，需要设置值为`attachment;filename=xxx`，指定接收程序处理数据内容的方式。在HTTP应用中，`attachment`是标准的方式，表示要求用户干预。`attachment`后面指定的`filename`参数表示服务器建议浏览器将内容保存到文件的文件名称（**对于中文名称，需要通过编码转换，否则会出现乱码**）。
```java
response.setHeader("Content-Type", "application/x-msdownload");
response.setHeader("Content-Disposition", "attachment;filename=" + toUTF8String(filename));
```

### 实现的关键代码
通过请求的文件名参数，去相关目录下查找该文件，并将该文件写进Response的响应输出流，响应给浏览器。
```java
@Controller
public class DownloadController {
	// 创建日志记录对象
	private static final Log logger = LogFactory.getLog(DownloadController.class);
	/**
	 * 执行下载
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
```
## 运行效果
###  单文件上传
![在这里插入图片描述](https://img-blog.csdnimg.cn/20200322122146242.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzQxMTkzMTMz,size_16,color_FFFFFF,t_70)![在这里插入图片描述](https://img-blog.csdnimg.cn/2020032212343566.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzQxMTkzMTMz,size_16,color_FFFFFF,t_70)
### 多文件上传
![在这里插入图片描述](https://img-blog.csdnimg.cn/2020032212392480.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzQxMTkzMTMz,size_16,color_FFFFFF,t_70)![在这里插入图片描述](https://img-blog.csdnimg.cn/2020032212394893.png)
### 文件下载
![在这里插入图片描述](https://img-blog.csdnimg.cn/20200322124558398.png)
当点击download时
![在这里插入图片描述](https://img-blog.csdnimg.cn/2020032212473190.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzQxMTkzMTMz,size_16,color_FFFFFF,t_70)

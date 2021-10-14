package com.c3stones.controller;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.http.HttpServletResponse;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.jodconverter.core.DocumentConverter;
import org.jodconverter.core.office.OfficeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import cn.hutool.core.io.IORuntimeException;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.StrUtil;

/**
 * 附件预览Controller
 * 
 * @author CL
 *
 */
@Controller
@RequestMapping(value = "/file")
public class FilePreviewController {

	private static final Logger log = LoggerFactory.getLogger(FilePreviewController.class);

	@Autowired
	private DocumentConverter documentConverter;

	/**
	 * 跳转到附件预览和下载页面
	 * 
	 * @return
	 */
	@RequestMapping(value = "")
	public String index() {
		return "fileIndex";
	}

	/**
	 * 附件预览
	 * 
	 * @param file     附件
	 * @param response
	 */
	@RequestMapping(value = "/preview")
	@ResponseBody
	public void preview(MultipartFile file, HttpServletResponse response) {
		if (file == null) {
			return;
		}
		InputStream inputStream = null;
		OutputStream outputStream = null;
		try {
			inputStream = file.getInputStream();
			outputStream = response.getOutputStream();
			String fileName = file.getOriginalFilename();
			if (StrUtil.endWithAnyIgnoreCase(fileName, ".doc", ".docx", ".xls", ".xlsx", ".csv", ".ppt", ".pptx")) {
				// 转为PDF
				documentConverter.convert(inputStream).to(outputStream)
						.as(documentConverter.getFormatRegistry().getFormatByExtension("pdf")).execute();
			} else if (StrUtil.endWithAnyIgnoreCase(fileName, ".pdf", ".txt", ".xml", ".md", ".json", ".html", ".htm",
					".gif", ".jpg", ".jpeg", ".png", ".ico", ".bmp")) {
				IoUtil.copy(inputStream, outputStream);
			} else {
				outputStream.write("暂不支持预览此类型附件".getBytes());
			}
		} catch (IORuntimeException e) {
			log.error("附件预览IO运行异常：{}", e.getMessage());
		} catch (IOException e) {
			log.error("附件预览IO异常：{}", e.getMessage());
		} catch (OfficeException e) {
			log.error("附件预览Office异常：{}", e.getMessage());
		} finally {
			IOUtils.closeQuietly(inputStream);
		}
		IoUtil.writeUtf8(outputStream, true);
	}

}

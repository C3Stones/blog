package com.c3stones.wapper;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.github.tobato.fastdfs.domain.fdfs.MetaData;
import com.github.tobato.fastdfs.domain.fdfs.StorePath;
import com.github.tobato.fastdfs.domain.proto.storage.DownloadByteArray;
import com.github.tobato.fastdfs.service.FastFileStorageClient;

/**
 * FastDFS客户端包装类
 * 
 * @author CL
 *
 */
@Component
public class FdfsClientWrapper {

	@Autowired
	private FastFileStorageClient fastFileStorageClient;

	public String uploadFile(MultipartFile file) throws IOException {
		if (file != null) {
			byte[] bytes = file.getBytes();
			long fileSize = file.getSize();
			String originalFilename = file.getOriginalFilename();
			String extension = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
			return this.uploadFile(bytes, fileSize, extension);
		}
		return null;
	}

	/**
	 * 文件上传
	 * 
	 * @param bytes     文件字节
	 * @param fileSize  文件大小
	 * @param extension 文件扩展名
	 * @return 返回文件路径（卷名和文件名）
	 */
	public String uploadFile(byte[] bytes, long fileSize, String extension) {
		ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
		// 元数据
		Set<MetaData> metaDataSet = new HashSet<MetaData>();
		metaDataSet.add(new MetaData("dateTime", LocalDateTime.now().toString()));
		StorePath storePath = fastFileStorageClient.uploadFile(bais, fileSize, extension, metaDataSet);
		return storePath.getFullPath();
	}

	/**
	 * 下载文件
	 * 
	 * @param filePath 文件路径
	 * @return 文件字节
	 * @throws IOException
	 */
	public byte[] downloadFile(String filePath) throws IOException {
		byte[] bytes = null;
		if (StringUtils.isNotBlank(filePath)) {
			String group = filePath.substring(0, filePath.indexOf("/"));
			String path = filePath.substring(filePath.indexOf("/") + 1);
			DownloadByteArray byteArray = new DownloadByteArray();
			bytes = fastFileStorageClient.downloadFile(group, path, byteArray);
		}
		return bytes;
	}

	/**
	 * 删除文件
	 * 
	 * @param filePath 文件路径
	 */
	public void deleteFile(String filePath) {
		if (StringUtils.isNotBlank(filePath)) {
			fastFileStorageClient.deleteFile(filePath);
		}
	}

}
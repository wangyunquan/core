package com.buswe.module.cms.editor;

import com.buswe.ContextHolder;
import com.buswe.module.cms.service.ResouceService;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Map;


public class BinaryUploader {

	public static final State save(HttpServletRequest request,
			Map<String, Object> conf) {
	//	FileItemStream fileStream = null;
		boolean isAjaxUpload = request.getHeader( "X_Requested_With" ) != null;

		if (!ServletFileUpload.isMultipartContent(request)) {
			return new BaseState(false, AppInfo.NOT_MULTIPART_CONTENT);
		}

//		ServletFileUpload upload = new ServletFileUpload(
//				new DiskFileItemFactory());
//
//        if ( isAjaxUpload ) {
//            upload.setHeaderEncoding( "UTF-8" );
//        }
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;

		try {

			MultipartFile multipartFile = multipartRequest.getFile(conf.get("fieldName").toString());
			if(multipartFile==null){
				return new BaseState( false, AppInfo.NOTFOUND_UPLOAD_DATA);
			}

//			FileItemIterator iterator = upload.getItemIterator(request);

//			while (iterator.hasNext()) {
//				fileStream = iterator.next();
//
//				if (!fileStream.isFormField())
//					break;
//				fileStream = null;
//			}
//
//			if (fileStream == null) {
//				return new BaseState(false, AppInfo.NOTFOUND_UPLOAD_DATA);
//			}

			String savePath = (String) conf.get("savePath");
			String originFileName = multipartFile.getOriginalFilename();
			String suffix = FileType.getSuffixByFilename(originFileName);

			originFileName = originFileName.substring(0,originFileName.length() - suffix.length());
			savePath = savePath + suffix;
			long maxSize = ((Long) conf.get("maxSize")).longValue();

			if (!validType(suffix, (String[]) conf.get("allowFiles"))) {
				return new BaseState(false, AppInfo.NOT_ALLOW_FILE_TYPE);
			}

			savePath = PathFormat.parse(savePath, originFileName);

			String physicalPath = (String) conf.get("rootPath") + savePath;
			InputStream is = multipartFile.getInputStream();
			
			
			
			State storageState = StorageManager.saveFileByInputStream(is,physicalPath, maxSize);
			is.close();
			String key =null;
			ResouceService brs= ContextHolder.getBean(ResouceService.class);
			try {
				key=brs.uploadFile(physicalPath, multipartFile.getOriginalFilename(), multipartFile.getContentType());

			} catch (Exception e) {
				e.printStackTrace();
			}
			if (storageState.isSuccess()) {//PathFormat.format(savePath)
				storageState.putInfo("url","http://p1573hye9.bkt.clouddn.com/"+key);
				storageState.putInfo("type", suffix);
				storageState.putInfo("original", multipartFile.getOriginalFilename());
				storageState.putInfo("title", multipartFile.getOriginalFilename());
			}
			return storageState;
		} catch (Exception e) {
			return new BaseState(false, AppInfo.PARSE_REQUEST_ERROR);
		}

	}

	private static boolean validType(String type, String[] allowTypes) {
		List<String> list = Arrays.asList(allowTypes);

		return list.contains(type);
	}
}

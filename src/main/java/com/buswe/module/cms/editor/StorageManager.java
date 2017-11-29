package com.buswe.module.cms.editor;

 

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.commons.io.FileUtils;
import org.springframework.web.multipart.MultipartFile;

public class StorageManager {
	public static final int BUFFER_SIZE = 8192;

	public StorageManager() {
	}

	public static State saveBinaryFile(byte[] data, String path) {
		File file = new File(path);

		State state = valid(file);

		if (!state.isSuccess()) {
			return state;
		}

		try {
			BufferedOutputStream bos = new BufferedOutputStream(
					new FileOutputStream(file));
			bos.write(data);
			bos.flush();
			bos.close();
		} catch (IOException ioe) {
			return new BaseState(false, AppInfo.IO_ERROR);
		}

		state = new BaseState(true, file.getAbsolutePath());
		state.putInfo( "size", data.length );
		state.putInfo( "title", file.getName() );
		return state;
	}

	public static State saveFileByInputStream(InputStream is, String path,
			long maxSize) {
		State state  =new BaseState(true) ;

		File tmpFile = getTmpFile();

		byte[] dataBuf = new byte[ 2048 ];
		BufferedInputStream bis = new BufferedInputStream(is, StorageManager.BUFFER_SIZE);

		try {
			BufferedOutputStream bos = new BufferedOutputStream(
					new FileOutputStream(tmpFile), StorageManager.BUFFER_SIZE);

			int count = 0;
			while ((count = bis.read(dataBuf)) != -1) {
				bos.write(dataBuf, 0, count);
			}
			bos.flush();
			bos.close();

			if (tmpFile.length() > maxSize) {
				tmpFile.delete();
				return new BaseState(false, AppInfo.MAX_SIZE);
			}

			state.putInfo( "size", tmpFile.length() );
			state.putInfo( "title", tmpFile.getName() );
			state = saveTmpFile(tmpFile, path);

			if (!state.isSuccess()) {
				tmpFile.delete();
			}

			return state;
			
		} catch (IOException e) {
		}
		return new BaseState(false, AppInfo.IO_ERROR);
	}

	public static State saveFileByInputStream(InputStream is, String path) {
		State state = new BaseState(true);
		try {
			Path pathnio = Paths.get(path);
			Path parent = pathnio.getParent();
			if (!Files.exists(parent))
				Files.createDirectories(parent);
		
			Files.copy(is, Paths.get(path));
			state.putInfo("size", Files.size(pathnio));
			state.putInfo("title", new File(path).getName());
		} catch (IOException e) {
			return new BaseState(false, AppInfo.IO_ERROR);
		}
		return state;
	}

	private static File getTmpFile() {
		File tmpDir = FileUtils.getTempDirectory();
		String tmpFileName = (Math.random() * 10000 + "").replace(".", "");
		return new File(tmpDir, tmpFileName);
	}

	private static State saveTmpFile(File tmpFile, String path) {
		State state = null;
	//	File targetFile = new File(path);
    path=path.substring(1);
//		if (targetFile.canWrite()) {
//			return new BaseState(false, AppInfo.PERMISSION_DENIED);
//		}
		try {
			Path pathnio=Paths.get(path);
			Path parent =pathnio.getParent();
			if(!Files.exists(parent))
			Files.createDirectories(parent);
	  Files.move(tmpFile.toPath(), Paths.get(path));
		//	FileUtils.moveFile(tmpFile, targetFile);
		 
		} catch (IOException e) {
			return new BaseState(false, AppInfo.IO_ERROR);
		}

		state = new BaseState(true);
		
		
		return state;
	}

	private static State valid(File file) {
		File parentPath = file.getParentFile();

		if ((!parentPath.exists()) && (!parentPath.mkdirs())) {
			return new BaseState(false, AppInfo.FAILED_CREATE_FILE);
		}

		if (!parentPath.canWrite()) {
			return new BaseState(false, AppInfo.PERMISSION_DENIED);
		}

		return new BaseState(true);
	}
	
	public static State saveFileByMultipartFile(MultipartFile upfile, String physicalPath, long maxSize) {

		if (upfile.getSize() > maxSize) {
			return new BaseState(false, AppInfo.MAX_SIZE);
		}
		State state = new BaseState(true);
		try {
			String path = physicalPath;
			Path pathnio = Paths.get(path);
			Path parent = pathnio.getParent();
			if (!Files.exists(parent))
				Files.createDirectories(parent);
			state.putInfo("size", upfile.getSize());
			state.putInfo("title", upfile.getName());
			Files.copy(upfile.getInputStream(), Paths.get(path));

		} catch (IOException e) {
			return new BaseState(false, AppInfo.IO_ERROR);
		}
		return state;
	}
}

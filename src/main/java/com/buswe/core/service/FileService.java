package com.buswe.core.service;

import javax.servlet.http.HttpServletRequest; /**
 * 资源管理类
 */

public interface FileService {
  public   String uploadFile(String physicalPath, String name, String contentType, HttpServletRequest request);
}

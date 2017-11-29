package com.buswe.module.cms.controller;

import java.nio.file.Paths;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.buswe.module.cms.editor.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;



@Controller
public class EditorController {
 
	protected Logger logger = LoggerFactory.getLogger(getClass());
	private final ResourceLoader resourceLoader;
	
	@Autowired
	public EditorController(ResourceLoader resourceLoader) {
		this.resourceLoader = resourceLoader;
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/upload/{filetype}/{date}/{filename:.+}")
	@ResponseBody
	public ResponseEntity<?> getFile(@PathVariable String filetype,@PathVariable String date,@PathVariable String filename) {

		try {
			String filepath="file:" + Paths.get("upload"+"/"+filetype+"/"+date, filename).toString();
			return ResponseEntity.ok(resourceLoader.getResource(filepath));
		} catch (Exception e) {
			return ResponseEntity.notFound().build();
		}
	}
	 @RequestMapping("/admin/cms/editor")
	 public @ResponseBody String editor(HttpServletRequest request)
	 { 
			String callbackName = request.getParameter("callback");
		 String actionType=request.getParameter("action");
		 String result="";
			if ( callbackName != null ) {
				if ( !validCallbackName( callbackName ) ) {
					result= new BaseState( false, AppInfo.ILLEGAL ).toJSONString();
				}
				
				result= callbackName+"("+this.invoke(actionType,request)+");";
				
			} else {
				result= this.invoke(actionType,request);
			}
			logger.info(" the upload return value is:"+result);
			return result;
	 }
		
		public String invoke(String actionType,HttpServletRequest request) {
			
			 ConfigManager configManager= ConfigManager.getInstance();
			if ( actionType == null || !ActionMap.mapping.containsKey( actionType ) ) {
				return new BaseState( false, AppInfo.INVALID_ACTION ).toJSONString();
			}
			
			if ( configManager == null || !configManager.valid() ) {
				return new BaseState( false, AppInfo.CONFIG_ERROR ).toJSONString();
			}
			
			State state = null;
			
			int actionCode = ActionMap.getType( actionType );
			
			Map<String, Object> conf = null;
			
			switch ( actionCode ) {
			
				case ActionMap.CONFIG:
					return configManager.getAllConfig().toString();
					
				case ActionMap.UPLOAD_IMAGE:
				case ActionMap.UPLOAD_SCRAWL:
				case ActionMap.UPLOAD_VIDEO:
				case ActionMap.UPLOAD_FILE:
					conf = configManager.getConfig( actionCode );
					state = new Uploader( request, conf ).doExec();
					break;
					
				case ActionMap.CATCH_IMAGE:
					conf = configManager.getConfig( actionCode );
					String[] list =request.getParameterValues( (String)conf.get("fieldName" ) );
					state = new ImageHunter( conf ).capture( list );
					break;
					
				case ActionMap.LIST_IMAGE:
				case ActionMap.LIST_FILE:
					conf = configManager.getConfig( actionCode );
					int start = this.getStartIndex(request);
					state = new FileManager( conf ).listFile( start );
					break;
					
			}
			
			return state.toJSONString();
			
		}
		
		public int getStartIndex (HttpServletRequest request) {
			
			String start =request.getParameter( "start" );
			
			try {
				return Integer.parseInt( start );
			} catch ( Exception e ) {
				return 0;
			}
			
		}
		/**
		 * callback参数验证
		 */
		private boolean validCallbackName ( String name ) {
			
			if ( name.matches( "^[a-zA-Z_]+[\\w0-9_]*$" ) ) {
				return true;
			}
			
			return false;
			
		}
}

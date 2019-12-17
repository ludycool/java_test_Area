package com.topband.bluetooth.common.model;

/**
 * 常量类.
 * 
 * @author wujiahuai
 *
 */
public class SystemConstants {
	
	private SystemConstants() {
		
	}

	/**
	 * 请求 header 中用于保存 token 的参数名.
	 */
	public static final String TOKEN_HEADER = "Authorization";


	/**
	 * 语言请求头
	 */
	public static final String LANGUAGE_HEADER = "Language";

	/**
	 * 默认语言
	 */
	public static final String LANGUAGE_DEFAULT = "zh_CN";

	/**
	 * 数据库转实体 字段是否转下划线驼峰且首字母小写
	 */
	public  final  static  boolean DB_FIELD_CAMEL_CASE=true;



	public static class FilePostFix{
		public static final String ZIP_FILE =".zip";

		public static final String [] IMAGES ={"jpg", "jpeg", "JPG", "JPEG", "gif", "GIF", "bmp", "BMP", "png"};
		public static final String [] ZIP ={"ZIP","zip","rar","RAR"};
		public static final String [] VIDEO ={"mp4","MP4","mpg","mpe","mpa","m15","m1v", "mp2","rmvb"};
		public static final String [] APK ={"apk","exe"};
		public static final String [] OFFICE ={"xls","xlsx","docx","doc","ppt","pptx"};

	}
	public class FileType{
		public static final int FILE_IMG = 1;
		public static final int FILE_ZIP = 2;
		public static final int FILE_VEDIO= 3;
		public static final int FILE_APK = 4;
		public static final int FIVE_OFFICE = 5;
		public static final String FILE_IMG_DIR= "/img/";
		public static final String FILE_ZIP_DIR= "/zip/";
		public static final String FILE_VEDIO_DIR= "/video/";
		public static final String FILE_APK_DIR= "/apk/";
		public static final String FIVE_OFFICE_DIR= "/office/";
	}


}

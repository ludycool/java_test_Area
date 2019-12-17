package com.topband.bluetooth.api.error;

import com.topband.bluetooth.common.enums.CommonError;
import com.topband.bluetooth.common.model.ResultModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

/**
 * 主要用途统一处理错误/异常(针对控制层).
 */
@Controller
@RequestMapping("${server.error.path:/error}")
@Slf4j
public class GlobalErrorController implements ErrorController {

	/**
	 * 错误信息的构建工具.
	 */
	@Autowired
	private ErrorInfoBuilder errorInfoBuilder;

	/**
	 * 错误信息页的路径.
	 */
	private final static String DEFAULT_ERROR_VIEW = "error";

	/**
	 * 获取错误控制器的映射路径.
	 */
	public String getErrorPath() {
		return errorInfoBuilder.getErrorProperties().getPath();
	}

	/**
	 * 情况1：若预期返回类型为text/html,s则返回错误信息页(View).
	 *
	 * @param request
	 * @return
	 */
	@RequestMapping(produces = MediaType.TEXT_HTML_VALUE)
	@ResponseBody
	public ModelAndView errorHtml(HttpServletRequest request) {
		return new ModelAndView(DEFAULT_ERROR_VIEW, "errorInfo", errorInfoBuilder.getErrorInfo(request));
	}

	/**
	 * 情况2：其它预期类型 则返回详细的错误信息(JSON).
	 *
	 * @param request
	 * @return
	 */
	@RequestMapping
	@ResponseBody
	public ResultModel error(HttpServletRequest request) {
		ErrorInfo errorInfo = errorInfoBuilder.getErrorInfo(request);

		ResultModel obj = new ResultModel();
		obj.setStatus(CommonError.STATUS_500301.getStatus());
		log.error(errorInfo.getError());
		return obj;
	}
}

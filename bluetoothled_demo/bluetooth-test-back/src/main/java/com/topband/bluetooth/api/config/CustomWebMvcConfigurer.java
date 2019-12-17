package com.topband.bluetooth.api.config;

import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.serializer.SimpleDateFormatSerializer;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.ArrayList;
import java.util.List;

@Configuration
@Slf4j
public class CustomWebMvcConfigurer implements WebMvcConfigurer {
	/**
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		List<String> excludeList = new ArrayList<String>();
		//放行swagger
		excludeList.add("/swagger-ui.html");
		excludeList.add("/swagger-resources/**" );
		excludeList.add("/v2/api-docs");
		excludeList.add("/webjars/springfox-swagger-ui/**");

		registry.addInterceptor(authorizationInterceptor()).addPathPatterns("/**").excludePathPatterns(excludeList);

	}
	*/
	@Bean
	public HttpMessageConverters fastJsonHttpMessageConverters() {
		ParserConfig.getGlobalInstance().addAccept("com.topband.");

		SerializeConfig serializeConfig = new SerializeConfig();

		serializeConfig.put(java.util.Date.class, new SimpleDateFormatSerializer("yyyy-MM-dd HH:mm:ss"));
		serializeConfig.put(java.sql.Date.class, new SimpleDateFormatSerializer("yyyy-MM-dd"));
		serializeConfig.put(java.sql.Timestamp.class, new SimpleDateFormatSerializer("yyyy-MM-dd HH:mm:ss"));

		SerializerFeature[] serializerFeatureArr = new SerializerFeature[] { 
					SerializerFeature.PrettyFormat,
					SerializerFeature.WriteMapNullValue, SerializerFeature.WriteNullListAsEmpty,
					SerializerFeature.WriteNonStringKeyAsString,
					SerializerFeature.DisableCircularReferenceDetect
				};

		// 创建FastJson信息转换对象
		FastJsonHttpMessageConverter fastJsonHttpMessageConverter = new FastJsonHttpMessageConverter();

		// 创建Fastjosn对象并设定序列化规则
		FastJsonConfig fastJsonConfig = new FastJsonConfig();
		fastJsonConfig.setSerializerFeatures(serializerFeatureArr);

		// 设定 json 格式且编码为 UTF-8.
		List<MediaType> mediaTypes = new ArrayList<>();
		mediaTypes.add(MediaType.APPLICATION_JSON_UTF8);
		fastJsonHttpMessageConverter.setSupportedMediaTypes(mediaTypes);

		fastJsonConfig.setSerializeConfig(serializeConfig);

		// 规则赋予转换对象
		fastJsonHttpMessageConverter.setFastJsonConfig(fastJsonConfig);

		return new HttpMessageConverters(fastJsonHttpMessageConverter);

	}

	/**
	 * 大小写不敏感
	 * @param configurer
	 */
	@Override
	public void configurePathMatch(PathMatchConfigurer configurer) {
		AntPathMatcher matcher = new AntPathMatcher();
		matcher.setCaseSensitive(false);
		configurer.setPathMatcher(matcher);
	}
}

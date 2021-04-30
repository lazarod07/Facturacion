package co.cristian.springboot.app;

import java.util.Locale;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

@Configuration
public class MvcConfig implements WebMvcConfigurer {

	/*
	 * @Override public void addResourceHandlers(ResourceHandlerRegistry registry) {
	 * 
	 * WebMvcConfigurer.super.addResourceHandlers(registry);
	 * 
	 * String resourcePath =
	 * Paths.get("uploads").toAbsolutePath().toUri().toString();
	 * 
	 * registry.addResourceHandler("/uploads/**").addResourceLocations(resourcePath)
	 * ;
	 * 
	 * }
	 */

	public void addViewControllers(ViewControllerRegistry registry) {

		registry.addViewController("/error_403").setViewName("error_403");

	}

	@Bean
	public BCryptPasswordEncoder passwordEncoder() {

		return new BCryptPasswordEncoder();

	}

	@Bean
	public LocaleResolver localeResolver() {

		SessionLocaleResolver localResolver = new SessionLocaleResolver();

		localResolver.setDefaultLocale(new Locale("es", "ES"));

		return localResolver;

	}

	@Bean
	public LocaleChangeInterceptor localeChangeInterceptor() {

		LocaleChangeInterceptor localeInterceptor = new LocaleChangeInterceptor();

		localeInterceptor.setParamName("lang");

		return localeInterceptor;

	}

	@Bean
	public Jaxb2Marshaller jaxb2Marshaller() {

		Jaxb2Marshaller marshaller = new Jaxb2Marshaller();

		marshaller.setClassesToBeBound(new Class[] { co.cristian.springboot.app.view.xml.ClienteList.class });

		return marshaller;

	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {

		registry.addInterceptor(localeChangeInterceptor());

	}

}

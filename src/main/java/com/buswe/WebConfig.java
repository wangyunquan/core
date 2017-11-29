package com.buswe;

import com.alibaba.druid.support.http.StatViewServlet;
import freemarker.template.utility.XmlEscape;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.web.servlet.config.annotation.ViewResolverRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;
import org.springframework.web.servlet.view.freemarker.FreeMarkerViewResolver;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import static com.buswe.Constants.ENV_DEVELOPMENT;
import static com.buswe.Constants.ENV_PRODUCTION;


/**
 * @author Raysmond<i@raysmond.com>.
 */
@Configuration
@ConfigurationProperties
public class WebConfig extends WebMvcConfigurerAdapter {
  
    @Autowired
    private Environment env;

    public String getApplicationEnv(){
        return this.env.acceptsProfiles(ENV_PRODUCTION) ? ENV_PRODUCTION : ENV_DEVELOPMENT;
    }



    public void configureViewResolvers(ViewResolverRegistry registry) {

        FreeMarkerViewResolver viewResolverHtml =new FreeMarkerViewResolver();
        viewResolverHtml.setViewClass(org.springframework.web.servlet.view.freemarker.FreeMarkerView.class);
        viewResolverHtml.setContentType("text/html; charset=utf-8");
        viewResolverHtml.setSuffix(".html");
        viewResolverHtml.setOrder(0);
        registry.viewResolver(viewResolverHtml);
    }

    @Bean
    public ServletRegistrationBean servletRegistrationBean(){
        StatViewServlet DruidStatView = new StatViewServlet();
        return new ServletRegistrationBean(DruidStatView,"/druid//*");
    }
    
//    @Bean
//    public FilterRegistrationBean siteMeshFilter() {
//        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
//        filterRegistrationBean.setFilter(new ConfigurableSiteMeshFilter() {
//            protected void applyCustomConfiguration(SiteMeshFilterBuilder builder) {
//                builder.addDecoratorPath("/*.jsp", "/sitemesh/layout.html");
//                builder.addExcludedPath("*login*");
//            }
//        });
//        filterRegistrationBean.setDispatcherTypes(EnumSet.allOf(DispatcherType.class));
//        return filterRegistrationBean;
//    }
    

//    @Bean
//    public FilterableHandlerMethodArgumentResolver filterableHandlerMethodArgumentResolver()
//    {
//    	return new FilterableHandlerMethodArgumentResolver();
//    }
    
    
}

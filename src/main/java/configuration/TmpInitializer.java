package configuration;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;
 
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;
 
public class TmpInitializer implements WebApplicationInitializer {
 
    public void onStartup(ServletContext container) throws ServletException {
 
        AnnotationConfigWebApplicationContext ctx = new AnnotationConfigWebApplicationContext();
        ctx.register(TmpConfiguration.class);
        ctx.setServletContext(container);
 
        ServletRegistration.Dynamic servlet = container.addServlet(
                "dispatcher", new DispatcherServlet(ctx));
 
        servlet.setLoadOnStartup(1);
        servlet.addMapping("/");
        
        AnnotationConfigWebApplicationContext ctx_swagger = new AnnotationConfigWebApplicationContext();
        ctx.register(SpringfoxConfiguration.class);
        ctx.setServletContext(container);
 
        ServletRegistration.Dynamic servlet_swagger = container.addServlet(
                "springfox", new DispatcherServlet(ctx_swagger));
 
        servlet_swagger.setLoadOnStartup(2);
        servlet_swagger.addMapping("/swagger/*");
        
        
    }
 
}

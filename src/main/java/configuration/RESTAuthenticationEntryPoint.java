package configuration;

 
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import onwelo.skel.infrastructure.JsonExporter;
import onwelo.skel.pojo.ErrorInfo;
 
@Component
public class RESTAuthenticationEntryPoint implements AuthenticationEntryPoint {
    

	 private JsonExporter<ErrorInfo> errorInfo  = new JsonExporter<ErrorInfo> (ErrorInfo.class); 
	    
	
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
            throws IOException, ServletException {
        
    	response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        String queryString = request.getQueryString();
        String requestBody  = read (request.getInputStream());
        System.out.println(requestBody);
        ErrorInfo ei = new ErrorInfo(request.getRequestURI()+(queryString != null ? "?"+queryString:""), "Bad Credentials");
        System.out.println(errorInfo.getJson(ei));
        response.getOutputStream().println(errorInfo.getJson(ei));
        
        
    }
    
    public static String read(InputStream input) throws IOException {
        try (BufferedReader buffer = new BufferedReader(new InputStreamReader(input))) {
            return buffer.lines().collect(Collectors.joining("\n"));
        }
    }
    
    
}
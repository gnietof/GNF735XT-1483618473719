package wasdev.sample.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import redis.clients.jedis.Jedis;

/**
 * Servlet implementation class SimpleServlet
 */
@WebServlet("/Listen")
public class ListenerServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	
    	String hr = request.getParameter("hr");
    	System.out.println(hr);
    	
    	String lat = request.getParameter("lat");
    	System.out.println(lat);
    	
    	String lng = request.getParameter("lng");
    	System.out.println(lng);
    	
    	String spd = request.getParameter("spd");
    	System.out.println(spd);
    	
		Object[] serviceInfo = getServiceInfo();
      	Jedis jedis = new Jedis((String)serviceInfo[0], (Integer)serviceInfo[1]);
      	
        response.setContentType("application/json");
        response.getWriter().print("{\"rc\":\"200\"}");
    }
    
    
	public Object[] getServiceInfo() throws Exception{
	    CloudEnvironment environment = new CloudEnvironment();
	    if ( environment.getServiceDataByLabels("compose-for-redis").size() == 0 ) {
	        throw new Exception( "No Redis service is bund to this app!!" );
	    }
	
	    Object[] info = new Object[3];
	    Map credential = (Map)((Map)environment.getServiceDataByLabels("redis").get(0)).get( "credentials" );    
	
	    info[0] = (String)credential.get( "host" );
	    info[1] = (Integer)credential.get( "port" );
	    info[2] = (String)credential.get( "password" );
	
	    return info;
	}    

}

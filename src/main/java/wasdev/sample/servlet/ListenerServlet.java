package wasdev.sample.servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Enumeration;


import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
//import redis.clients.jedis.Jedis;
//import org.cloudfoundry.runtime.env.CloudEnvironment;

//import com.ibm.json.java.JSONObject;

import com.ibm.json.java.JSONObject;
import com.ibm.misc.BASE64Encoder;

/**
 * Servlet implementation class SimpleServlet
 */
@WebServlet("/Listen")
public class ListenerServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
	private static final String USER = "use-token-auth";
	private static final String SECRET = "gnf735XT";

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	}

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
/*
		BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream()));
		String s="";
		while ((s = br.readLine()) != null) {
			System.out.println(s);
		}
		br.close();		
*/		
//		JSONObject ji = JSONObject.parse(request.getInputStream());

		JSONObject ji = new JSONObject(); 
		Enumeration<String> e = request.getParameterNames();
		while (e.hasMoreElements()) {
			String name = e.nextElement();
			String value = request.getParameter(name);
			ji.put(name, value);
		}
		JSONObject ji2 = new JSONObject(); 
		ji2.put("d",ji);

		
/*
    	String hr = request.getParameter("hr");
    	System.out.println(hr);
    	
 		String lat = request.getParameter("lat");
    	System.out.println(lat);
    	
    	String lng = request.getParameter("lng");
    	System.out.println(lng);
    	
    	String spd = request.getParameter("spd");
    	System.out.println(spd);
 */   	
//		Object[] serviceInfo = getServiceInfo();

        response.setContentType("application/json");
        response.getWriter().print("{\"rc\":\"200\"}");
        
        callREST("http://vzzmqi.messaging.internetofthings.ibmcloud.com:1883/api/v0002/device/types/Garmin/devices/gnf735xt/events/HR","POST",ji.toString(),USER,SECRET);

		URL url = new URL("http://vzzmqi.messaging.internetofthings.ibmcloud.com:1883/api/v0002/device/types/Garmin/devices/gnf735xt/events/HR");

    }
    
    private String callREST(String href,String method,String data,String user,String pwd) {
        StringBuffer sb = new  StringBuffer();

		try {
			URL url = new URL(href);
			HttpURLConnection uc = (HttpURLConnection)url.openConnection();
			uc.setDoInput(true);
			uc.setDoOutput(true);
			uc.setRequestMethod(method);
	//		uc.setRequestProperty("Accept","application/json");
			uc.setRequestProperty("Content-Type","application/json");
			if (user!=null) {
				BASE64Encoder b64 = new BASE64Encoder();
				String encoded = b64.encode((user + ":" + pwd).getBytes());
				uc.setRequestProperty("Authorization", "Basic " + encoded);
			} 
	
			if (data!=null) {
				OutputStream os = uc.getOutputStream();
				os.write(data.getBytes());
				os.close();
			}
	
			if (uc.getResponseCode() != HttpURLConnection.HTTP_OK) {
				throw new RuntimeException("Failed : HTTP error code : "+ uc.getResponseCode());
			}
	
			BufferedReader br = new BufferedReader(new InputStreamReader(uc.getInputStream()));
	        String s = "";
	        while ((s = br.readLine()) != null) {
				sb.append(s);
			}
			br.close();
		} catch (Exception ex) {
			ex.printStackTrace();			
		}

    	return (sb.toString());
    }
    
    
/*	public Object[] getServiceInfo() throws Exception{
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
*/
}

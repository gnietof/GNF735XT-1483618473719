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
import com.ibm.json.java.JSONArray;
import com.ibm.misc.BASE64Encoder;
import java.text.SimpleDateFormat;

/**
 * Servlet implementation class SimpleServlet
 */
@WebServlet("/Weather")
public class WeatherServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
	private static final String USER = "ca8f5e30-8831-472d-9d06-d08e2fdf1f28";
	private static final String PWD = "hLrSPTwgSG";
	private static final SimpleDateFormat sdf = new SimpleDateFormat("HH:mmm");

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	}
	
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

/*
		BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream()));
		String s="";
		while ((s = br.readLine()) != null) {
			System.out.println(s);
		}
		br.close();		
*/		
//		JSONObject ji = JSONObject.parse(request.getInputStream());

/*
		JSONObject ji = new JSONObject(); 
		Enumeration<String> e = request.getParameterNames();
		while (e.hasMoreElements()) {
			String name = e.nextElement();
			String value = request.getParameter(name);
			ji.put(name, value);
		}
*/
		String lat = request.getParameter("latitude");
		String lng = request.getParameter("longitude");
        JSONObject ji = callREST("https://twcservice.mybluemix.net/api/weather/v1/geocode/"+lat+"/"+lng+"/forecast/hourly/48hour.json?units=m","GET",null,USER,PWD);
        JSONArray forecasts = (JSONArray) ji.get("forecasts");
        JSONArray ja = new JSONArray();
        for (int i=0;i<forecasts.size();i++) {
	        JSONObject forecast = (JSONObject)(forecasts.get(0));
	        JSONObject jai = new JSONObject();
	        long valid = forecast.get("fcst_valid");
	        jai.put("valid",sdf.format(valid));
	        jai.put("temp",forecast.get("temp"));
	        jai.put("icon",forecast.get("icon_code"));
	        jai.put("feels",forecast.get("feels_like"));
	        jai.put("wspd",forecast.get("wspd"));
	        jai.put("wdir",forecast.get("wdir_cardinal"));
	        ja.add(jai);
        }


        response.setContentType("application/json");
        response.getWriter().print(ja);
        ;

    }
    
    private JSONObject callREST(String href,String method,String data,String user,String pwd) {
    	JSONObject jo = null;

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
	
			int rc = uc.getResponseCode();
//			if (rc != HttpURLConnection.HTTP_OK) {
//				throw new RuntimeException("Failed : HTTP error code : "+ uc.getResponseCode());
//			}
			
			if (rc == HttpURLConnection.HTTP_MOVED_TEMP
				|| rc == HttpURLConnection.HTTP_MOVED_PERM
				|| rc == HttpURLConnection.HTTP_SEE_OTHER) {

				String url2 = uc.getHeaderField("Location");
//				String cookies = uc.getHeaderField("Set-Cookie");

				uc = (HttpURLConnection) new URL(url2).openConnection();
//		conn.setRequestProperty("Cookie", cookies);
//		conn.addRequestProperty("Accept-Language", "en-US,en;q=0.8");
//		conn.addRequestProperty("User-Agent", "Mozilla");
//		conn.addRequestProperty("Referer", "google.com");

			}			
	

			StringBuffer sb = new StringBuffer();
			BufferedReader br = new BufferedReader(new InputStreamReader(uc.getInputStream()));
	        String s = "";
	        while ((s = br.readLine()) != null) {
				sb.append(s);
			}
			br.close();
			System.out.println(sb.toString());
			jo = JSONObject.parse(sb.toString());
			
//			jo = JSONObject.parse(uc.getInputStream());
			
		} catch (Exception ex) {
			ex.printStackTrace();			
		}

    	return (jo);
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

package wasdev.sample.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
	
/**
 * Servlet implementation class SimpleServlet
 */
@WebServlet("/Test")
public class SimpleServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	String hr = request.getParameter("hr");
    	System.out.println(hr);
    	
    	
        response.setContentType("application/json");
        response.getWriter().print("{\"name\":\"Genaro Nieto\"}");
    }

}

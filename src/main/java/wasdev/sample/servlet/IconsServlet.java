package wasdev.sample.servlet;

import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.awt.Graphics2D;

/**
 * Servlet implementation class Icons
 */
@WebServlet("/Icons")
public class IconsServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public IconsServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		String code = req.getParameter("icon");
		ZipFile zip = new ZipFile(req.getSession().getServletContext().getResource("/weatherinsightsicons.zip").getFile());
		
		ServletOutputStream os = res.getOutputStream();
		
		ZipEntry icon = zip.getEntry("weathericons/icon"+code+".png");
		if (icon!=null) {
			System.out.println("IconSize: "+icon.getSize());
			InputStream zis = zip.getInputStream(icon);
/*
  			byte[] bb = new byte[(int) icon.getSize()];
			zis.read(bb , 0, (int) icon.getSize());
			os.write(bb);
*/
			BufferedImage bi = ImageIO.read(zis);
			zis.close();

			BufferedImage bo = new BufferedImage(100,100,bi.getType());
			Graphics2D g2d = outputImage.createGraphics();
        	g2d.drawImage(inputImage, 0, 0, scaledWidth, scaledHeight, null);
        	g2d.dispose();
        	ImageIO.write(bo,"png",os);
		}
		os.close();
		zip.close();
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
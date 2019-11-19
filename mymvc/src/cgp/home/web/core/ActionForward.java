package cgp.home.web.core;

import java.io.IOException;
import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ActionForward {
	//path是个逻辑名
	private String path = null;
	private boolean redirect = false;
	public ActionForward() {
		// TODO Auto-generated constructor stub
	}
	public ActionForward(String path) {
		this(path,false);
	}
	public ActionForward(String path,boolean redirect) {
		this.path = path;
		this.redirect = redirect;
	}
	public void forward(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException{
		String uname = request.getParameter("uname");
		request.setAttribute("uname", uname);
		//从应用程序中拿出配置文件
		Properties config = (Properties)(request.getSession().getServletContext().getAttribute("config"));
		//应该根据逻辑名找配置中对应的实际路径
		if(redirect){
			response.sendRedirect(config.getProperty(path));
		}else{
			request.getRequestDispatcher(config.getProperty(path)).forward(request, response);
		}
	}
}

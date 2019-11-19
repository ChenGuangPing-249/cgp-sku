package cgp.home.web.core;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class ActionServlet extends HttpServlet {

	/**
		 * Constructor of the object.
		 */
	public ActionServlet() {
		super();
	}

	/**
		 * Destruction of the servlet. <br>
		 */
	public void destroy() {
		super.destroy(); // Just puts "destroy" string in log
		// Put your code here
	}

	/**
		 * The doGet method of the servlet. <br>
		 *
		 * This method is called when a form has its tag value method equals to get.
		 * 
		 * @param request the request send by the client to the server
		 * @param response the response send by the server to the client
		 * @throws ServletException if an error occurred
		 * @throws IOException if an error occurred
		 */
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		this.doPost(request, response);
	}

	/**
		 * The doPost method of the servlet. <br>
		 *
		 * This method is called when a form has its tag value method equals to post.
		 * 
		 * @param request the request send by the client to the server
		 * @param response the response send by the server to the client
		 * @throws ServletException if an error occurred
		 * @throws IOException if an error occurred
		 */
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//拿出应用程序中的action池(key:类的全路径,value：该类对应的实例)
		Properties actionPool = (Properties)this.getServletContext().getAttribute("actionPool");
		//拿出应用程序中的配置文件
		Properties config = (Properties)this.getServletContext().getAttribute("config");
		//获得提交的路径
		String uri = request.getRequestURI();
		//截取提交的uri
		int a = uri.lastIndexOf("/");
		int b = uri.lastIndexOf(".");
		if(a!=-1 && b!=-1 && b>a){
			uri = uri.substring(a+1, b);
		}
		//通过uri找到要封装数据的ActionForm
		String formuri = uri+"Form";
		String classFormName = config.getProperty(formuri);
		//反射出form的实例
		ActionForm form = null;
		try {
			Class c = Class.forName(classFormName);
			form = (ActionForm)c.newInstance();
			//拿到请求所有的参数
			Set<Map.Entry<String,String[]>> set = request.getParameterMap().entrySet();
			for (Map.Entry<String, String[]> entry : set) {
				//param===(uname,upass)
				String param = entry.getKey();
				//找到对应的set方法
				Method m = c.getDeclaredMethod("set"+param.substring(0, 1).toUpperCase()+param.substring(1), String.class);
				m.invoke(form, entry.getValue()[0]);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		//通过uri找到要处理该uri的类的全路径
		String className = config.getProperty(uri);
		Action action = null;
		try {
			if(className!=null){
				//先去actionPool中拿对应的实例
				Object obj = actionPool.get(className);
				if(obj==null){
					//表示第一次访问
					action = (Action)Class.forName(className).newInstance();
					//将实例存入池中（第二次访问可直接拿到）
					actionPool.put(className, action);
				}else{
					//第N次访问
					action = (Action)obj;
				}
				//调用父类方法，多态调子类
				//返回一个actionForward对象
				ActionForward af = action.execute(request, response,form);
				//跳转
				af.forward(request, response);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
		 * Initialization of the servlet. <br>
		 *
		 * @throws ServletException if an error occurs
		 */
	public void init() throws ServletException {
		//准备一个存Action实例的map集合
		Properties actionPool = new Properties();
		//得到配置文件的路径
		String path = this.getServletConfig().getInitParameter("configLocation");
		String configPath = this.getServletContext().getRealPath("/")+path;
		//加载配置文件，只会加载一次
		Properties config = new Properties();
		try {
			config.load(new FileInputStream(configPath));
			//将整个配置文件存在应用程序的作用域中
			this.getServletContext().setAttribute("config", config);
			//将action池存入应用程序的作用域中
			this.getServletContext().setAttribute("actionPool", actionPool);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}

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
		//�ó�Ӧ�ó����е�action��(key:���ȫ·��,value�������Ӧ��ʵ��)
		Properties actionPool = (Properties)this.getServletContext().getAttribute("actionPool");
		//�ó�Ӧ�ó����е������ļ�
		Properties config = (Properties)this.getServletContext().getAttribute("config");
		//����ύ��·��
		String uri = request.getRequestURI();
		//��ȡ�ύ��uri
		int a = uri.lastIndexOf("/");
		int b = uri.lastIndexOf(".");
		if(a!=-1 && b!=-1 && b>a){
			uri = uri.substring(a+1, b);
		}
		//ͨ��uri�ҵ�Ҫ��װ���ݵ�ActionForm
		String formuri = uri+"Form";
		String classFormName = config.getProperty(formuri);
		//�����form��ʵ��
		ActionForm form = null;
		try {
			Class c = Class.forName(classFormName);
			form = (ActionForm)c.newInstance();
			//�õ��������еĲ���
			Set<Map.Entry<String,String[]>> set = request.getParameterMap().entrySet();
			for (Map.Entry<String, String[]> entry : set) {
				//param===(uname,upass)
				String param = entry.getKey();
				//�ҵ���Ӧ��set����
				Method m = c.getDeclaredMethod("set"+param.substring(0, 1).toUpperCase()+param.substring(1), String.class);
				m.invoke(form, entry.getValue()[0]);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		//ͨ��uri�ҵ�Ҫ�����uri�����ȫ·��
		String className = config.getProperty(uri);
		Action action = null;
		try {
			if(className!=null){
				//��ȥactionPool���ö�Ӧ��ʵ��
				Object obj = actionPool.get(className);
				if(obj==null){
					//��ʾ��һ�η���
					action = (Action)Class.forName(className).newInstance();
					//��ʵ��������У��ڶ��η��ʿ�ֱ���õ���
					actionPool.put(className, action);
				}else{
					//��N�η���
					action = (Action)obj;
				}
				//���ø��෽������̬������
				//����һ��actionForward����
				ActionForward af = action.execute(request, response,form);
				//��ת
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
		//׼��һ����Actionʵ����map����
		Properties actionPool = new Properties();
		//�õ������ļ���·��
		String path = this.getServletConfig().getInitParameter("configLocation");
		String configPath = this.getServletContext().getRealPath("/")+path;
		//���������ļ���ֻ�����һ��
		Properties config = new Properties();
		try {
			config.load(new FileInputStream(configPath));
			//�����������ļ�����Ӧ�ó������������
			this.getServletContext().setAttribute("config", config);
			//��action�ش���Ӧ�ó������������
			this.getServletContext().setAttribute("actionPool", actionPool);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}

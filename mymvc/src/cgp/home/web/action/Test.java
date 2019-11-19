package cgp.home.web.action;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cgp.home.web.core.Action;
import cgp.home.web.core.ActionForm;
import cgp.home.web.core.ActionForward;

public class Test extends Action{
	public Test() {
		System.out.println("开始测试");
	}
	@Override
	public ActionForward execute(HttpServletRequest ruquest, HttpServletResponse response,ActionForm form)
			throws ServletException, IOException  {
		System.out.println("这个是测试的execute");
		return new ActionForward("actiontest",true);
	}
}

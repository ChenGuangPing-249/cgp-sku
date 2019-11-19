package cgp.home.web.action;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cgp.home.web.core.Action;
import cgp.home.web.core.ActionForm;
import cgp.home.web.core.ActionForward;

public class LoginAction extends Action{
	public LoginAction() {
		System.out.println("LoginAction³öÀ´ÁË");
	}
	
	@Override
	public ActionForward execute(HttpServletRequest request, HttpServletResponse response,ActionForm form) 
			throws ServletException, IOException {
		System.out.println(form);
		return new ActionForward("success");
	}
}

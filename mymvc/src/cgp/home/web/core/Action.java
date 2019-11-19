package cgp.home.web.core;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public abstract  class Action {
	public abstract ActionForward execute(HttpServletRequest request,HttpServletResponse response,ActionForm form)
			throws ServletException, IOException;
}

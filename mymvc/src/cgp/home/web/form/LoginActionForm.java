package cgp.home.web.form;

import cgp.home.web.core.ActionForm;

public class LoginActionForm extends ActionForm{
	private String uname;
	private String upass;
	
	public String getUname() {
		return uname;
	}

	public void setUname(String uname) {
		this.uname = uname;
	}


	public String getUpass() {
		return upass;
	}

	public void setUpass(String upass) {
		this.upass = upass;
	}

	
	@Override
	public String toString() {
		return "LoginActionForm [uname=" + uname + ", upass=" + upass + "]";
	}

	@Override
	public boolean validate() {
		return false;
	}

}

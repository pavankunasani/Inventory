package org.application.service;

import org.application.dao.ILoginDao;
import org.application.dao.LoginDaoImpl;

public class LoginServiceImpl implements ILoginService {

	private ILoginDao loginDao;

	@Override
	public boolean userLogin(String username, String password) {

		loginDao = new LoginDaoImpl();

		return loginDao.userLogin(username, password);
	}

}

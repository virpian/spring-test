package onwelo.skel.infrastructure;

import onwelo.skel.dao.DocDbDao;
import onwelo.skel.dao.TodoDao;

public class TodoDaoFactory {

	private static TodoDao Dao;

	public static TodoDao getDao() {
		if (Dao == null) {
			Dao = new DocDbDao();
		}

		return Dao;
	}

}

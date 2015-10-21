package nmd.infrastructure;

import nmd.dao.DocDbDao;
import nmd.dao.TodoDao;

public class TodoDaoFactory {

	private static TodoDao Dao;

	public static TodoDao getDao() {
		if (Dao == null) {
			Dao = new DocDbDao();
		}

		return Dao;
	}

}

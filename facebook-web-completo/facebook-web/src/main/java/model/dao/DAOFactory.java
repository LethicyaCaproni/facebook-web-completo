package model.dao;

import java.util.HashMap;
import java.util.Map;

public class DAOFactory {

    private static final Map<Class<?>, Object> listDAOsInterfaces =
            new HashMap<>();

    private DAOFactory() {
        // Impede a instanciação da fábrica.
    }

    static {
        listDAOsInterfaces.put(PostDAO.class, new MySQLPostDAO());
        listDAOsInterfaces.put(UserDAO.class, new MySQLUserDAO());
        listDAOsInterfaces.put(CommentDAO.class, new MySQLCommentDAO());
    }

    public static <T> T createDAO(Class<T> daoInterface) {
        Object dao = listDAOsInterfaces.get(daoInterface);

        if (dao == null) {
            throw new IllegalArgumentException(
                    "DAO não registrado: " + daoInterface.getName()
            );
        }

        return daoInterface.cast(dao);
    }
}
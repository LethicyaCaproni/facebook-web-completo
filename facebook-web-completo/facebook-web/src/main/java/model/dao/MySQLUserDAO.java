
package model.dao;

import java.util.ArrayList;
import java.util.List;

import model.ModelException;
import model.User;

public class MySQLUserDAO implements UserDAO {

    @Override
    public boolean save(User user) throws ModelException {

        DBHandler db = new DBHandler();

        String sqlInsert = "INSERT INTO users "
                + "(id, nome, sexo, email, password) "
                + "VALUES (DEFAULT, ?, ?, ?, ?)";

        db.prepareStatement(sqlInsert);
        db.setString(1, user.getName());
        db.setString(2, user.getGender());
        db.setString(3, user.getEmail());
        db.setString(4, user.getPassword());

        return db.executeUpdate() > 0;
    }

    @Override
    public boolean update(User user) throws ModelException {

        DBHandler db = new DBHandler();

        String password = user.getPassword();

        if (password == null || password.isBlank()) {
            User existingUser = findById(user.getId());

            if (existingUser == null) {
                throw new ModelException(
                        "Usuário não encontrado para atualização."
                );
            }

            password = existingUser.getPasswordHash();
        }

        String sqlUpdate = "UPDATE users SET "
                + "nome = ?, "
                + "sexo = ?, "
                + "email = ?, "
                + "password = ? "
                + "WHERE id = ?";

        db.prepareStatement(sqlUpdate);
        db.setString(1, user.getName());
        db.setString(2, user.getGender());
        db.setString(3, user.getEmail());
        db.setString(4, password);
        db.setInt(5, user.getId());

        return db.executeUpdate() > 0;
    }

    @Override
    public boolean delete(User user) throws ModelException {

        DBHandler db = new DBHandler();

        String sqlDeletePosts =
                "DELETE FROM posts WHERE user_id = ?";

        db.prepareStatement(sqlDeletePosts);
        db.setInt(1, user.getId());
        db.executeUpdate();

        db = new DBHandler();

        String sqlDeleteUser =
                "DELETE FROM users WHERE id = ?";

        db.prepareStatement(sqlDeleteUser);
        db.setInt(1, user.getId());

        return db.executeUpdate() > 0;
    }

    @Override
    public List<User> listAll() throws ModelException {

        DBHandler db = new DBHandler();
        List<User> users = new ArrayList<>();

        String sqlQuery = "SELECT * FROM users ORDER BY id";

        db.createStatement();
        db.executeQuery(sqlQuery);

        while (db.next()) {
            users.add(createUser(db));
        }

        return users;
    }

    @Override
    public User findById(int id) throws ModelException {

        DBHandler db = new DBHandler();

        String sql = "SELECT * FROM users WHERE id = ?";

        db.prepareStatement(sql);
        db.setInt(1, id);
        db.executeQuery();

        User user = null;

        if (db.next()) {
            user = createUser(db);
        }

        return user;
    }

    @Override
    public User findByEmail(String email) throws ModelException {

        DBHandler db = new DBHandler();

        String sql = "SELECT * FROM users WHERE email = ?";

        db.prepareStatement(sql);
        db.setString(1, email);
        db.executeQuery();

        User user = null;

        if (db.next()) {
            user = createUser(db);

            // Diagnóstico temporário do login.
            String hashEsperado =
                    "CrfAL+DxFDIK7MHQYUsjahTMfI+MyVtxMwjyzxDj49Q=";

            String hashBanco = user.getPasswordHash();

            System.out.println(
                    "TESTE DAO: usuário encontrado = "
                            + user.getEmail()
            );

            System.out.println(
                    "TESTE DAO: hash esperado = "
                            + hashEsperado.equals(hashBanco)
            );

            System.out.println(
                    "TESTE DAO: tamanho do hash = "
                            + (hashBanco == null ? 0 : hashBanco.length())
            );
        }

        return user;
    }

    private User createUser(DBHandler db) throws ModelException {

        User user = new User(db.getInt("id"));

        user.setName(db.getString("nome"));
        user.setGender(db.getString("sexo"));
        user.setEmail(db.getString("email"));

        // Não mantém a senha original em memória.
        user.setPassword("");
        user.setPasswordHash(db.getString("password"));

        return user;
    }
}

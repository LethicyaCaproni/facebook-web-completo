
package controller;

import java.io.IOException;
import java.util.List;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import model.ModelException;
import model.User;
import model.dao.DAOFactory;
import model.dao.UserDAO;
import model.utils.PasswordEncryptor;

@WebServlet(urlPatterns = {
    "/users",
    "/users/save",
    "/users/update",
    "/users/delete"
})
public class UsersController extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        // Obtém a rota sem o contexto da aplicação.
        String contextPath = req.getContextPath();
        String action = req.getRequestURI().substring(contextPath.length());

        System.out.println("URL: " + action);

        switch (action) {
            case "/users": {
                loadUsers(req);

                RequestDispatcher rd =
                        req.getRequestDispatcher("/users.jsp");
                rd.forward(req, resp);
                break;
            }

            case "/users/save": {
                saveOrUpdateUser(req);

                resp.sendRedirect(contextPath + "/users");
                break;
            }

            case "/users/update": {
                loadUser(req);

                RequestDispatcher rd =
                        req.getRequestDispatcher("/users_form.jsp");
                rd.forward(req, resp);
                break;
            }

            case "/users/delete": {
                deleteUser(req);

                resp.sendRedirect(contextPath + "/users");
                break;
            }

            default: {
                String message =
                        String.format("URL não suportada: '%s'.", action);

                System.out.println(message);
                resp.sendError(HttpServletResponse.SC_NOT_FOUND, message);
                break;
            }
        }
    }

    private void deleteUser(HttpServletRequest req) {
        String userIdStr = req.getParameter("userId");

        if (userIdStr == null || userIdStr.isBlank()) {
            return;
        }

        try {
            int userId = Integer.parseInt(userIdStr);
            User userToBeDeleted = new User(userId);

            UserDAO dao = DAOFactory.createDAO(UserDAO.class);
            dao.delete(userToBeDeleted);

        } catch (NumberFormatException e) {
            System.err.println("ID de usuário inválido.");
        } catch (ModelException e) {
            e.printStackTrace();
        }
    }

    private void loadUser(HttpServletRequest req) {
        String userIdStr = req.getParameter("userId");

        if (userIdStr == null || userIdStr.isBlank()) {
            req.setAttribute("user", null);
            return;
        }

        try {
            int userId = Integer.parseInt(userIdStr);

            UserDAO dao = DAOFactory.createDAO(UserDAO.class);
            User userToBeUpdated = dao.findById(userId);

            req.setAttribute("user", userToBeUpdated);

        } catch (NumberFormatException e) {
            System.err.println("ID de usuário inválido.");
            req.setAttribute("user", null);
        } catch (ModelException e) {
            e.printStackTrace();
            req.setAttribute("user", null);
        }
    }

    private void saveOrUpdateUser(HttpServletRequest req) {
        String userIdStr = req.getParameter("user_id");

        boolean isUpdate =
                userIdStr != null && !userIdStr.isBlank();

        User user;

        try {
            if (isUpdate) {
                user = new User(Integer.parseInt(userIdStr));
            } else {
                user = new User();
            }

            String userName = req.getParameter("user_name");
            String userGender = req.getParameter("user_gender");
            String userEmail = req.getParameter("user_email");
            String userPW = req.getParameter("user_pw");

            // Valida os campos obrigatórios.
            if (userName == null || userName.isBlank()
                    || userEmail == null || userEmail.isBlank()) {
                System.err.println("Nome e e-mail são obrigatórios.");
                return;
            }

            user.setName(userName.trim());
            user.setGender(userGender);
            user.setEmail(userEmail.trim());

            // Na edição, uma senha vazia preserva a senha atual.
            if (userPW != null && !userPW.isBlank()) {
                user.setPassword(
                        PasswordEncryptor.hashPassword(userPW)
                );
            } else {
                user.setPassword("");
            }

            // No cadastro, a senha é obrigatória.
            if (!isUpdate && (userPW == null || userPW.isBlank())) {
                System.err.println(
                        "A senha é obrigatória para cadastrar um usuário."
                );
                return;
            }

            UserDAO dao = DAOFactory.createDAO(UserDAO.class);

            if (isUpdate) {
                dao.update(user);
            } else {
                dao.save(user);
            }

        } catch (NumberFormatException e) {
            System.err.println("ID de usuário inválido.");
        } catch (ModelException e) {
            e.printStackTrace();
        }
    }

    private void loadUsers(HttpServletRequest req) {
        UserDAO dao = DAOFactory.createDAO(UserDAO.class);

        List<User> users = List.of();

        try {
            users = dao.listAll();
        } catch (ModelException e) {
            e.printStackTrace();
        }

        req.setAttribute("users_list", users);
    }
}

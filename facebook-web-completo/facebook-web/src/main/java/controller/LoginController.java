
package controller;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import model.ModelException;
import model.User;
import model.dao.DAOFactory;
import model.dao.UserDAO;
import model.utils.PasswordEncryptor;

@WebServlet(urlPatterns = {"/login", "/logout"})
public class LoginController extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(
            HttpServletRequest req,
            HttpServletResponse resp
    ) throws ServletException, IOException {

        String contextPath = req.getContextPath();

        if ("/logout".equals(req.getServletPath())) {
            HttpSession session = req.getSession(false);

            if (session != null) {
                session.invalidate();
            }
        }

        resp.sendRedirect(contextPath + "/login.jsp");
    }

    @Override
    protected void doPost(
            HttpServletRequest req,
            HttpServletResponse resp
    ) throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");

        String contextPath = req.getContextPath();
        String email = req.getParameter("user_login");
        String senha = req.getParameter("user_pw");

        System.out.println("========== LOGIN ==========");
        System.out.println("URI: " + req.getRequestURI());
        System.out.println("Contexto: " + contextPath);

        if (email == null || email.isBlank()
                || senha == null || senha.isBlank()) {
            resp.sendRedirect(contextPath + "/login.jsp?erro=true");
            return;
        }

        try {
            UserDAO dao = DAOFactory.createDAO(UserDAO.class);
            User user = dao.findByEmail(email.trim());

            if (user == null) {
                System.out.println("Usuario nao encontrado.");
                resp.sendRedirect(contextPath + "/login.jsp?erro=true");
                return;
            }

            String hashBanco = user.getPasswordHash();

            if (hashBanco == null || hashBanco.isBlank()
                    || !PasswordEncryptor.checkPassword(senha, hashBanco)) {
                System.out.println("Senha nao confere.");
                resp.sendRedirect(contextPath + "/login.jsp?erro=true");
                return;
            }

            HttpSession session = req.getSession(true);
            session.setAttribute("user_logged", user);

            String destino = contextPath + "/index.jsp";

            System.out.println("Login confirmado.");
            System.out.println("Redirecionando para: " + destino);

            resp.sendRedirect(resp.encodeRedirectURL(destino));

        } catch (ModelException e) {
            System.err.println("Erro durante a autenticacao:");
            e.printStackTrace();

            resp.sendRedirect(contextPath + "/login.jsp?erro=true");
        }
    }
}

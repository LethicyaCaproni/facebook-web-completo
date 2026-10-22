package controller;

import java.io.IOException;
import java.util.List;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Comment;
import model.ModelException;
import model.Post;
import model.User;
import model.dao.CommentDAO;
import model.dao.DAOFactory;
import model.dao.PostDAO;
import model.dao.UserDAO;

@WebServlet(urlPatterns = {"/comments", 
		                   "/comments/create", 
		                   "/comments/save", 
		                   "/comments/update",
		                   "/comments/delete"})
public class CommentsController extends HttpServlet {
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) 
			throws ServletException, IOException {
		
		//TODO: Log - apagar ao final
		String action = req.getRequestURI();
		System.out.println("URL: " + action);
		
		switch (action) {
		case "/facebook/comments": {
			loadComments(req);
			
			RequestDispatcher rd = req.getRequestDispatcher("/comments.jsp");
			rd.forward(req, resp);
			break;
		}
		case "/facebook/comments/create": {
			loadPosts(req);
			loadUsers(req);
			
			RequestDispatcher rd = req.getRequestDispatcher("/comment-form.jsp");
			rd.forward(req, resp);
			break;
		}
		case "/facebook/comments/save": {
			saveOrUpdateComment(req);
			
			resp.sendRedirect("/facebook/comments");
			break;
		}
		case "/facebook/comments/update": {
			loadComment(req);
			loadPosts(req);
			loadUsers(req);
			
			RequestDispatcher rd = req.getRequestDispatcher("/comment-form.jsp");
			rd.forward(req, resp);
			break;
		}
		case "/facebook/comments/delete": {
			deleteComment(req);
			resp.sendRedirect("/facebook/comments");
			break;
		}
		default:
			String message = String.format("URL não suportada: '%s'.", action);
			
			System.out.println(message);
			throw new IllegalArgumentException(message);
		}
	}
	
	private void deleteComment(HttpServletRequest req) {
		String commentIdStr = req.getParameter("commentId");
		
		int commentId = Integer.parseInt(commentIdStr);
		Comment commentToBeDeleted = new Comment(commentId);
		
		CommentDAO dao = DAOFactory.createDAO(CommentDAO.class);
		try {
			dao.delete(commentToBeDeleted);
		} catch (ModelException e) {
			// Log no servidor
			e.printStackTrace();
		}
	}
	
	private void loadComment(HttpServletRequest req) {
		String commentIdStr = req.getParameter("commentId");
		int commentId = Integer.parseInt(commentIdStr);
	
		CommentDAO dao = DAOFactory.createDAO(CommentDAO.class);
		
		Comment commentToBeUpdated = null;
		try {
			commentToBeUpdated = dao.findById(commentId);
		} catch (ModelException e) {
			// Log no servidor
			e.printStackTrace();
		}
		
		req.setAttribute("comment", commentToBeUpdated);
	}
	
	private void saveOrUpdateComment(HttpServletRequest req) {
		String commentIdStr = req.getParameter("comment_id");
		boolean isUpdate = false;
		
		Comment comment = null;
		if ((commentIdStr == null) || commentIdStr.isBlank())
			comment = new Comment();
		else { 
			comment = new Comment(Integer.parseInt(commentIdStr));
			isUpdate = true;
		}
		
		// Recuperar os dados da requisição
		String commentContent = req.getParameter("comment_content");
		String postIdStr = req.getParameter("post_id");
		String userIdStr = req.getParameter("user_id");
		
		comment.setContent(commentContent);
		
		PostDAO postDAO = DAOFactory.createDAO(PostDAO.class);
		UserDAO userDAO = DAOFactory.createDAO(UserDAO.class);
		CommentDAO dao = DAOFactory.createDAO(CommentDAO.class);
		
		// Salvar o comentário no Banco
		try {
			// Busca o post escolhido no select
			Post post = postDAO.findById(Integer.parseInt(postIdStr));
			comment.setPost(post);
			
			// Busca o usuário (quem comentou) escolhido no select
			User user = userDAO.findById(Integer.parseInt(userIdStr));
			comment.setUser(user);
			
			if (isUpdate) dao.update(comment);
			else dao.save(comment);
		} catch (ModelException e) {
			// Log no servidor
			e.printStackTrace();
		}
	}
	
	private void loadComments(HttpServletRequest req) {
		CommentDAO dao = DAOFactory.createDAO(CommentDAO.class);
		
		List<Comment> comments = List.of();
		try {
			comments = dao.listAll();
		} catch (ModelException me) {
			// TODO Log no servidor
			me.printStackTrace();
		}
		
		req.setAttribute("comments_list", comments);
	}
	
	// Carrega os posts para o <select> do formulário
	private void loadPosts(HttpServletRequest req) {
		PostDAO dao = DAOFactory.createDAO(PostDAO.class);
		
		List<Post> posts = List.of();
		try {
			posts = dao.listAll();
		} catch (ModelException me) {
			// TODO Log no servidor
			me.printStackTrace();
		}
		
		req.setAttribute("posts_list", posts);
	}
	
	// Carrega os usuários para o <select> do formulário
	private void loadUsers(HttpServletRequest req) {
		UserDAO dao = DAOFactory.createDAO(UserDAO.class);
		
		List<User> users = List.of();
		try {
			users = dao.listAll();
		} catch (ModelException me) {
			// TODO Log no servidor
			me.printStackTrace();
		}
		
		req.setAttribute("users_list", users);
	}
}

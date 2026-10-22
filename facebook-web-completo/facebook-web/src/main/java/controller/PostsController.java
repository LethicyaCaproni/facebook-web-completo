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
import model.Post;
import model.User;
import model.dao.DAOFactory;
import model.dao.PostDAO;
import model.dao.UserDAO;

@WebServlet(urlPatterns = {"/posts", "/posts/create", "/posts/save", 
		                   "/posts/update", "/posts/delete"})
public class PostsController extends HttpServlet {
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) 
			throws ServletException, IOException {
		
		//TODO: Log - apagar ao final
		String action = req.getRequestURI();
		System.out.println("URL: " + action);
		
		switch (action) {
		case "/facebook/posts": {
			loadPosts(req);
			
			RequestDispatcher rd = req.getRequestDispatcher("/posts.jsp");
			rd.forward(req, resp);
			break;
		}
		case "/facebook/posts/create": {
			loadUsers(req);
			
			RequestDispatcher rd = req.getRequestDispatcher("/post-form.jsp");
			rd.forward(req, resp);
			break;
		}
		case "/facebook/posts/save": {
			saveOrUpdatePost(req);
			
			resp.sendRedirect("/facebook/posts");
			break;
		}
		case "/facebook/posts/update": {
			loadPost(req);
			loadUsers(req);
			
			RequestDispatcher rd = req.getRequestDispatcher("/post-form.jsp");
			rd.forward(req, resp);
			break;
		}
		case "/facebook/posts/delete": {
			deletePost(req);
			resp.sendRedirect("/facebook/posts");
			break;
		}
		default:
			String message = String.format("URL não suportada: '%s'.", action);
			
			System.out.println(message);
			throw new IllegalArgumentException(message);
		}
	}
	
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
	
	private void loadPost(HttpServletRequest req) {
		String postIdStr = req.getParameter("postId");
		int postId = Integer.parseInt(postIdStr);
		
		PostDAO dao = DAOFactory.createDAO(PostDAO.class);
		
		Post postToBeUpdated = null;
		try {
			postToBeUpdated = dao.findById(postId);
		} catch (ModelException e) {
			// Log no servidor
			e.printStackTrace();
		}
		
		req.setAttribute("post", postToBeUpdated);
	}
	
	private void saveOrUpdatePost(HttpServletRequest req) {
		String postIdStr = req.getParameter("post_id");
		boolean isUpdate = false;
		
		Post post = null;
		if ((postIdStr == null) || postIdStr.isBlank())
			post = new Post();
		else {
			post = new Post(Integer.parseInt(postIdStr));
			isUpdate = true;
		}
		
		// Recuperar os dados da requisição
		String postContent = req.getParameter("post_content");
		String userIdStr = req.getParameter("user_id");
		
		post.setContent(postContent);
		
		UserDAO userDAO = DAOFactory.createDAO(UserDAO.class);
		PostDAO dao = DAOFactory.createDAO(PostDAO.class);
		
		try {
			// Busca o usuário escolhido no select
			User user = userDAO.findById(Integer.parseInt(userIdStr));
			post.setUser(user);
			
			if (isUpdate) dao.update(post);
			else dao.save(post);
		} catch (ModelException e) {
			// Log no servidor
			e.printStackTrace();
		}
	}
	
	private void deletePost(HttpServletRequest req) {
		String postIdStr = req.getParameter("postId");
		
		int postId = Integer.parseInt(postIdStr);
		Post postToBeDeleted = new Post(postId);
		
		PostDAO dao = DAOFactory.createDAO(PostDAO.class);
		try {
			dao.delete(postToBeDeleted);
		} catch (ModelException e) {
			// Log no servidor
			e.printStackTrace();
		}
	}
}

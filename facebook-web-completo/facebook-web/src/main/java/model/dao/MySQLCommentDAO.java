package model.dao;

import java.util.ArrayList;
import java.util.List;

import model.Comment;
import model.ModelException;
import model.Post;
import model.User;

public class MySQLCommentDAO implements CommentDAO {

	@Override
	public boolean save(Comment comment) throws ModelException {
		
		DBHandler db = new DBHandler();
		
		String sqlInsert = "INSERT INTO comments "
				+ " (content, comment_date, post_id, user_id) VALUES "
				+ " (?, CURDATE(), ?, ?);";
		
		db.prepareStatement(sqlInsert);
		db.setString(1, comment.getContent());
		db.setInt(2, comment.getPost().getId());
		db.setInt(3, comment.getUser().getId());
		  
		return db.executeUpdate() > 0;
	}

	@Override
	public boolean update(Comment comment) throws ModelException {
		
		DBHandler db = new DBHandler();
		
		String sqlUpdate = "UPDATE comments "
						 + " SET content = ? , "
						 + " post_id = ? , "
						 + " user_id = ? "
						 + " WHERE id = ?;";
		
		db.prepareStatement(sqlUpdate);
		
		db.setString(1, comment.getContent());
		db.setInt(2, comment.getPost().getId());
		db.setInt(3, comment.getUser().getId());
		db.setInt(4, comment.getId());
		
		return db.executeUpdate() > 0;
	}

	@Override
	public boolean delete(Comment comment) throws ModelException {
		
		DBHandler db = new DBHandler();
		
		String sqlDelete = " DELETE FROM comments "
				         + " WHERE id = ?;";

		db.prepareStatement(sqlDelete);		
		db.setInt(1, comment.getId());
		
		return db.executeUpdate() > 0;
	}

	@Override
	public List<Comment> listAll() throws ModelException {
	
		DBHandler db = new DBHandler();
		
		List<Comment> comments = new ArrayList<Comment>();
			
		// Declara um instrução SQL
		String sqlQuery = "SELECT * FROM comments ORDER BY id";
		
		db.createStatement();
	
		db.executeQuery(sqlQuery);

		while (db.next()) {
			Comment c = createComment(db);
			comments.add(c);
		}
		
		return comments;
	}

	@Override
	public Comment findById(int id) throws ModelException {
		
		DBHandler db = new DBHandler();
				
		String sql = "SELECT * FROM comments WHERE id = ?;";
		
		db.prepareStatement(sql);
		db.setInt(1, id);
		db.executeQuery();
		
		Comment c = null;
		while (db.next()) {
			c = createComment(db);
			break;
		}
		
		return c;
	}
	
	private Comment createComment(DBHandler db) throws ModelException {
		Comment c = new Comment(db.getInt("id"));
		c.setContent(db.getString("content"));
		c.setCommentDate(db.getDate("comment_date"));
		
		PostDAO postDAO = DAOFactory.createDAO(PostDAO.class); 
		
		Post post = postDAO.findById(db.getInt("post_id"));
		c.setPost(post);
		
		UserDAO userDAO = DAOFactory.createDAO(UserDAO.class);
		
		User user = userDAO.findById(db.getInt("user_id"));
		c.setUser(user);
		
		return c;
	}
}

package model;

import java.util.Calendar;
import java.util.Date;

public class Comment {
	private int id;
	private String content;
	private Date commentDate;
	private Post post;
	private User user;
	
	public Comment() {
		this(0);
	}
	
	public Comment(int id) {
		this.id = id;
		setContent("");
		setCommentDate(Calendar.getInstance().getTime());
	}
	
	public int getId() {
		return id;
	}
	
	public String getContent() {
		return content;
	}
	
	public void setContent(String content) {
		this.content = content;
	}
	
	public Date getCommentDate() {
		return commentDate;
	}
	
	public void setCommentDate(Date commentDate) {
		this.commentDate = commentDate;
	}
	
	public Post getPost() {
		return post;
	}
	
	public void setPost(Post post) {
		this.post = post;
	}
	
	public User getUser() {
		return user;
	}
	
	public void setUser(User user) {
		this.user = user;
	}
}

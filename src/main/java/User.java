package main.java;

public class User {
	int id;
	String desc;
	String status;
	String createdAt;
	String updatedAt;
	
	

	public User() {
		super();
	}



	public User(int id, String desc, String status, String createdAt) {
		super();
		this.id = id;
		this.desc = desc;
		this.status = status;
		this.createdAt = createdAt;
	}
	
	

	public User(int id, String desc, String status, String createdAt, String updatedAt) {
		super();
		this.id = id;
		this.desc = desc;
		this.status = status;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
	}



	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getcreatedAt() {
		return createdAt;
	}

	public void setcreatedAt(String createdAt) {
		this.createdAt = createdAt;
	}

	public String getupdatedAt() {
		return updatedAt;
	}

	public void setupdatedAt(String updatedAt) {
		this.updatedAt = updatedAt;
	}

}

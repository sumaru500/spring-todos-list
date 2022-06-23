package todos.eesolutions.com.websocket.model;

public class WSMessage {
	public static final String INSERT = "insert";
	public static final String UPDATE = "update";
	public static final String DELETE = "delete";

	private String type;
	private String id;

	public WSMessage() {

	}

	public WSMessage(String type, String id) {
		super();
		this.type = type;
		this.id = id;
	}


	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	

	public String getId() {
		return id;
	}


	public void setId(String id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return "WSMessage [type=" + type + ", id=" + id + "]";
	}
}

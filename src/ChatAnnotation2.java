
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.atomic.AtomicInteger;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@ServerEndpoint(value = "/websocket/chat2")
public class ChatAnnotation2 {

	// private static final Log log = LogFactory.getLog(ChatAnnotation.class);

	private static final String GUEST_PREFIX = "Guest";
	private static final AtomicInteger connectionIds = new AtomicInteger(0);
	private static final Set<ChatAnnotation2> connections = new CopyOnWriteArraySet<ChatAnnotation2>();

	private final String nickname;
	private Session session;
	/*
	 * private List<ChatAnnotation> userList = new ArrayList<ChatAnnotation>();
	 */

	public ChatAnnotation2() {
		nickname = GUEST_PREFIX + connectionIds.getAndIncrement();
	}

	// user:储存用户名的数组
	private static List<String> users = new ArrayList<String>();
	/* private JSONArray result = JSONArray.fromObject(users); */
	private static JSONArray result = new JSONArray();

	@OnOpen
	public void start(Session session) {
		this.session = session;
		// 每次打开一个页面，把当前对象加入集合当中
		connections.add(this);
		/*String message = String.format("* %s %s", nickname, "has joined.");
		broadcast(message,toUser);*/
		users.add(nickname);
		result.add("{\"nickname\":\"" + nickname + "\"}");
		System.out.println(result.toString());
		printUser();
	}

	@OnClose
	public void end() {
		connections.remove(this);
		users.remove(this.nickname);
		JSONObject j = JSONObject.fromObject("{\"nickname\":\"" + this.nickname + "\"}");
		result.remove(j);
		/*String message = String.format("* %s %s", nickname, "has disconnected.");
		broadcast(message,toUser);*/
	}

	@OnMessage
	public void incoming(String message) {
		// Never trust the client
		/*if (message.startsWith("102")) {
			broadcast("102b" + result.toString());
			System.out.println("102b" + result.toString());
		} else if (message.startsWith("101")) {
			String a = message.replace("101", "");
			String filteredMessage = String.format("%s: %s", nickname, a.toString());
			broadcast("101b" + filteredMessage);
		}*/
			
			String a = message.replace("101", "");
			String toUser = a.substring(a.length()-6);
			String bg = a.replace(toUser, "");
			String filteredMessage = String.format("%s: %s", nickname, bg);
			System.out.println("77777777777");
			System.out.println(filteredMessage);
			System.out.println("88888888888");
			broadcast(filteredMessage,toUser);
		
	}

	private void broadcast(String filteredMessage, String toUser) {
		// TODO Auto-generated method stub
		  for (ChatAnnotation2 client : connections) {
	        	if(client.nickname.equals(toUser)){
	        		System.out.println("okokok");
	        		System.out.println(client.nickname);
	        		System.out.println("alralralr");
		            try {
		            	System.out.println("*********************************");
		                synchronized (client) {
		                	System.out.println("1111111111");
		                    client.session.getBasicRemote().sendText(filteredMessage);
		                    System.out.println("999999999");
		                }
		            } catch (IOException e) {
		                //log.debug("Chat Error: Failed to send message to client", e);
		            	System.out.println("Chat Error: Failed to send message to client");
		                connections.remove(client);
		                try {
		                    client.session.close();
		                } catch (IOException e1) {
		                    // Ignore
		                }
		            
		                String message = String.format("* %s %s",
		                        client.nickname, "has been disconnected.");
		                broadcast(message,toUser);
		                /*users.remove();*/
		            }
	            }
	        }
	}

	@OnError
	public void onError(Throwable t) throws Throwable {
		// log.error("Chat Error: " + t.toString(), t);
		System.out.println("Chat Error: " + t.toString());
	}

	// Check connected user...
	public void printUser() {
		for (int i = 0; i < users.size(); i++) {
			String name = users.get(i);
			System.out.println("name:" + name);
		}
	}

/*	private static void broadcast(String msg) {
        for (ChatAnnotation client : connections) {
        	if(client.nickname.equals(toUser)){
	            try {
	            	System.out.println("*********************************");
	                synchronized (client) {
	                    client.session.getBasicRemote().sendText(msg);
	                }
	            } catch (IOException e) {
	                //log.debug("Chat Error: Failed to send message to client", e);
	            	System.out.println("Chat Error: Failed to send message to client");
	                connections.remove(client);
	                try {
	                    client.session.close();
	                } catch (IOException e1) {
	                    // Ignore
	                }
	            
	                String message = String.format("* %s %s",
	                        client.nickname, "has been disconnected.");
	                broadcast(message);
	                users.remove();
	            }
            }
        }
    }*/
}

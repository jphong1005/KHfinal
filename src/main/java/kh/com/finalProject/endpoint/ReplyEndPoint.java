package kh.com.finalProject.endpoint;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;
import javax.websocket.EndpointConfig;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import kh.com.finalProject.configurator.WSConfig;
import kh.com.finalProject.member.MemberDTO;
import kh.com.finalProject.reply.ReplyDTO;

@ServerEndpoint(value = "/reply", configurator = WSConfig.class)
public class ReplyEndPoint {
	private static Map<String, Session> clients = Collections.synchronizedMap(new HashMap<>());
	private HttpSession session;
	private MemberDTO dto;
	private ReplyDTO rdto;

	@OnOpen
	public void onConnect(Session session, EndpointConfig config) {
		System.out.println("============================== WebSocket접속됨 ==============================");

		this.session = (HttpSession) config.getUserProperties().get("hSession");

		// 사용자의 ID를 키값으로 WS 의 session 객체 담아주기(사용자의 id + WS session(아이피 주소 담고 있음)을 매칭
		String id = ((MemberDTO)(session)).getId();
		System.out.println("id : " + id);
		clients.put(id, session);
	}

	@OnMessage
	public void onReceive(String replyData, Session session) {
		String[] reply = replyData.split(",");
		String board_writer = reply[1];
		String board_seq = reply[2];
		try {
			// 여기서는 replyDTO service 로 넘겨 insert 해주기~
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
		synchronized (clients) {

			if (clients.get(board_writer) != null) { // 만약 board_writer(글쓴이)가 접속 중이라면 해당 글쓴이에게 알림 전송해주기
				if (board_writer != rdto.getReply_writer_id()) { // 해당 글을 쓴게 나고, 댓글 역시도 내가 쓴거라면 나한테는 안보내기
					try {
						clients.get(board_writer).getBasicRemote().sendText("여기가 실제 알림을 보내는 내용 ");
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

	@OnClose // 연결이 끊어지면 이 메서드가 실행 됨
	public void onClose(Session session) {
		// 맵에서 현재 로그인한 클라이언트 정보를 담은 멤버필드 session 을 이용해 id 값을 꺼내 클라이언트 목록에서 지워주기
		clients.remove((this.session).getId()); // 그럼 해당하는 세션값이 리스트에서 지워짐
		System.out.println("============================== 클라이언트의 접속이 끊어졌습니다. ==============================");
	}
}
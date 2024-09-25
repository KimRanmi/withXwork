package com.codenal.chat.config;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.codenal.chat.domain.ChatMsg;
import com.codenal.chat.domain.ChatMsgDto;
import com.codenal.chat.domain.ChatRoom;
import com.codenal.chat.service.ChatService;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class ChatWebSocketHandler extends TextWebSocketHandler{
	
	private final ChatService chatService;
	
	@Autowired
	public ChatWebSocketHandler(ChatService chatService) {
		this.chatService = chatService;
	}
	

	 private Map<Integer,WebSocketSession> clients = new
	 HashMap<Integer,WebSocketSession>();
	 
		
		// 클라이언트가 연결되었을 때 설정
		@Override
		public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		    System.out.println("New connection established: " + session.getId());

		    int roomNo = Integer.parseInt((String) session.getAttributes().get("room_no"));
		    Long participantId = Long.valueOf(session.getPrincipal().getName());
		    ChatRoom room = chatService.selectChatRoomOne(roomNo, participantId);
		    	    
		    	    for (ChatMsg msg : room.getMessages()) {
		    	        Map<String, Object> resultMap = new HashMap<>();
						resultMap.put("res_code", "200");
						resultMap.put("res_msg", "채팅 메시지 조회를 완료했습니다.");
		    	        resultMap.put("send_date", msg.getSendDate());
		    	        resultMap.put("msg_type", Character.toString(msg.getMsgType()));
		    	        resultMap.put("msg_content", msg.getMsgContent());
		    	        resultMap.put("sender_no", msg.getChatParticipant().getParticipantNo());
		    	        
		    	        String json = new ObjectMapper().writeValueAsString(resultMap);
		    	        session.sendMessage(new TextMessage(json)); // 초기 메시지 전송
		    	    }

		}
	
		// 클라이언트가 웹소켓 서버로 메시지를 전송했을 때 설정
		@Override
		protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {

			// 클라이언트가 보낸 메시지 
			String payload = message.getPayload();
			System.out.println("Received payload: " + payload);
			// Jackson ObjectMapper 객체 생성
			ObjectMapper objectMapper = new ObjectMapper();
			// JSON -> SendMessage 형태 변환
			ChatMsgDto msg = objectMapper.readValue(payload, ChatMsgDto.class);
			
			Map<String,String> resultMap = new HashMap<String,String>();
			
			switch(msg.getChat_type()){
				case "open":
					clients.put(msg.getSender_no(), session); // 클라이언트 등록
					System.out.println("=== 등록 확인 ===");
					System.out.println("현재 접속중인 클라이언트: " + clients);
				break;
				case "msg":
					  // 메시지를 DB에 저장
					  ChatMsg chat = chatService.createChatMsg(msg);
					  if(chat != null) {
						  resultMap.put("res_code", "200");
						  resultMap.put("res_msg", "채팅 전송을 완료했습니다.");
						  resultMap.put("msg_content", chat.getMsgContent());
						  resultMap.put("sender_no", String.valueOf(chat.getChatParticipant().getParticipantNo()));
						  DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
						  LocalDateTime sendDate = chat.getSendDate();
						  resultMap.put("send_date", sendDate.format(formatter)); // LocalDateTime을 문자열로 변환하여 Map에 저장
						  resultMap.put("msg_type", String.valueOf(chat.getMsgType()));
						  TextMessage resultMsg 
							= new TextMessage(objectMapper.writeValueAsString(resultMap));
							
						  for (WebSocketSession client : clients.values()) {
							        if (client.isOpen()) {  // 세션이 열려있는지 확인
							            System.out.println("Sending message to client: " + client);
							            client.sendMessage(resultMsg);
							        } else {
							            System.out.println("Client " + " is not connected.");
							        }
							    }
					  }
				break;
			}
		}
		

	
		// 클라이언트가 연결을 끊었을 때 설정
		@Override
		public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
			// 나간 사용자의 세션을 clients 맵에서 제거
		    // 나머지 사용자들의 세션은 여전히 clients 맵에 남아있으므로, 채팅 가능
			clients.values().removeAll(Arrays.asList(session));
			System.out.println("=== 삭제 확인 ===");
			for(Integer senderNo : clients.keySet()) {
				System.out.println(senderNo +" : "+clients.get(senderNo));
			}		
		}
		
}

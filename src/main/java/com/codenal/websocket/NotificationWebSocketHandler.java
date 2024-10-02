package com.codenal.websocket;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.codenal.chat.domain.ChatParticipantsDto;
import com.codenal.chat.service.ChatService;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class NotificationWebSocketHandler extends TextWebSocketHandler{
		
		private final ChatService chatService;
		
		@Autowired
		public NotificationWebSocketHandler(ChatService chatService) {
			this.chatService = chatService;
		}
		

		private Map<String, WebSocketSession> clients = new ConcurrentHashMap<>();

		// 클라이언트가 연결되었을 때 세션 등록
		@Override
		public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		    System.out.println("New connection established: " + session.getId());
		    clients.put(session.getId(), session);  // 클라이언트 세션 등록
		}


		// 클라이언트로부터 메시지를 받았을 때 처리(채팅하고는 별개로 가능하게 해뒀으니까 사용할 사람은 쓰세요)
		@Override
		protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
		    String receivedMessage = message.getPayload();
		    System.out.println("Received message: " + receivedMessage);
		    System.out.println("확인용 :" + message);

		    ObjectMapper objectMapper = new ObjectMapper();
//		    ChatMsgDto chatMessage = objectMapper.readValue(message.getPayload(), ChatMsgDto.class);  // 참고하세요
		    
		}

		// 특정 대상자에게만 알림 메시지 전송
		public void sendNotificationToSpecificUsers(List<ChatParticipantsDto> targetParticipants, String notificationMessage) throws Exception {
		    System.out.println("notificationMessage: " + notificationMessage);

		    for (WebSocketSession session : clients.values()) {
		        if (session.isOpen()) {
		            try {
		                System.out.println("현재 채팅 메시지 오는 중");
		                
		                // 클라이언트 세션에서 empId 가져오기
		                String clientEmpId = session.getPrincipal().getName();
		                System.out.println(clientEmpId);
		                // targetParticipants 리스트에서 각 참가자와 empId 비교
		                for (ChatParticipantsDto participant : targetParticipants) {
		                    String empIdAsString = String.valueOf(participant.getEmp_id()).trim();
		                    
		                    if (Objects.equals(empIdAsString, clientEmpId.trim())) {
		                        System.out.println("알림 전송 대상: " + clientEmpId);
		                        session.sendMessage(new TextMessage(notificationMessage));
		                        break;
		                    }
		                }
		            } catch (Exception e) {
		                e.printStackTrace();
		            }
		        }
		    }
		}
	

		
		// 클라이언트가 연결을 끊었을 때 설정
		@Override
		public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
			// 나간 사용자의 세션을 clients 맵에서 제거
			// 나머지 사용자들의 세션은 여전히 clients 맵에 남아있으므로, 채팅 가능
		     clients.remove(session.getId());  // 세션 제거
			//clients.values().removeAll(Arrays.asList(session));
			System.out.println("=== 삭제 확인 ===");
			for(String senderNo : clients.keySet()) {
				System.out.println(senderNo +" : "+clients.get(senderNo));
			}		
		}

}

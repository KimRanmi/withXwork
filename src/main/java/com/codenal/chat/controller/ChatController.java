package com.codenal.chat.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.codenal.chat.domain.ChatMsg;
import com.codenal.chat.domain.ChatMsgDto;
import com.codenal.chat.domain.ChatParticipants;
import com.codenal.chat.domain.ChatRoom;
import com.codenal.chat.domain.ChatRoomDto;
import com.codenal.chat.service.ChatService;
import com.codenal.employee.domain.EmployeeDto;
import com.codenal.employee.service.EmployeeService;

@Controller
public class ChatController {

	private final EmployeeService employeeService;
	private final ChatService chatService;
	
	@Autowired
	public ChatController(EmployeeService employeeService, ChatService chatService) {
		this.employeeService = employeeService;
		this.chatService = chatService;
	}
	
	// 참여중인 채팅방 목록 조회
	@GetMapping("/chatList")
	public String chat(Model model) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		User user = (User)authentication.getPrincipal();
		String username = user.getUsername();
        model.addAttribute("username", username);
        
//        List<ChatRoomDto> chatList = chatService.chatListSelect(username);

        List<ChatParticipants> participantList = chatService.participantListSelect(username);  	// 활성 상태의 참여중인 채팅 목록 조회
        model.addAttribute("chatList",participantList);
        
		List<EmployeeDto> employeeList = employeeService.getActiveEmployeeList(username);  // 채팅방 초대버튼 클릭시 조회할 직원목록
		model.addAttribute("employeeList",employeeList);
		return "apps/chat";
	}
	
	@ResponseBody
	@PostMapping("/chatList/chatRoom/create")
	public Map<String, String> chatRoomCreate(Model model, @ModelAttribute ChatRoomDto roomDto,
					@RequestParam("emp_id") List<String> empIds) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		User user = (User)authentication.getPrincipal();
		String username = user.getUsername();
		Long userId = Long.parseLong(username);
        model.addAttribute("username", username);
        
     // username을 empIds 리스트에 추가
        empIds.add(username);
        
    	// 조건 추가 -> 있는 1:1 채팅인 경우 새로운 방을 생성하지 않고 기존 채팅방으로 이동함@@@@@@@@@

        
        Map<String, String> resultMap = new HashMap<>();
        ChatRoom chatRoom = chatService.createChat(roomDto, empIds, userId);
        if(chatRoom != null) {
        	resultMap.put("res_code", "200");
        	resultMap.put("res_msg", "채팅방을 생성했습니다.");
//        	resultMap.put("roomNo", String.valueOf(chatRoom.getRoomNo()));  채팅방 조회 넘어가는 url
        }
        return resultMap;
	}
	
//	// 채팅방 상세 조회 --> 여기서부터 웹소켓 연결해야함  --> 웹소켓으로 옮기기
//	@GetMapping("/chatList/{roomNo}")
//	public String startChatting(@PathVariable("roomNo") int roomNo, Model model) {
//		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//		User user = (User)authentication.getPrincipal();
//		String username = user.getUsername();
//		Long empId = Long.parseLong(username);
//        model.addAttribute("username", username);
//		
//		List<ChatParticipants> participantList = chatService.participantListSelect(username);  	// 활성 상태의 참여중인 채팅 목록 조회
//		model.addAttribute("chatList",participantList);
//		
//		List<EmployeeDto> employeeList = employeeService.getActiveEmployeeList(username);  // 채팅방 초대버튼 클릭시 조회할 직원목록
//		model.addAttribute("employeeList",employeeList);
//		
//		List<ChatMsg> chat = chatService.selectChatRoomOne(roomNo, username);
//		model.addAttribute("chat",chat);
//		
//		
////		List<ChatMsgDto> resultList = chatService.selectChatMsgList(roomNo, empId);
////		model.addAttribute("resultList", resultList);
//		
//		return "apps/chat";
//	}
	
	// 채팅 메시지 전송
	// @PostMapping
	
}

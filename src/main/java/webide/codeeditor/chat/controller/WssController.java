package webide.codeeditor.chat.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import webide.codeeditor.chat.repository.Message;
import webide.codeeditor.chat.service.ChatService;

@RequiredArgsConstructor
@Controller
@Slf4j
public class WssController {

    private final ChatService chatService;
    private final SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/chat/message/{chatRoomId}/{memberId}")
    public void recievedMessage(
            @DestinationVariable Long chatRoomId,
            @DestinationVariable Long memberId,
            Message msg
    ) {
        log.info("Message Received -> From: {}, msg: {}", memberId, msg.getMessage());

        chatService.saveChatMessage(msg, chatRoomId, memberId);

        msg.setMemberId(memberId);

        messagingTemplate.convertAndSend("/sub/chat/" + chatRoomId, msg);
    }

}

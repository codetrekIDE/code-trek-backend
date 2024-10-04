package webide.codeeditor.chat.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import webide.codeeditor.chat.repository.Message;

@RequiredArgsConstructor
@Controller
@Slf4j
public class WssController {

    @MessageMapping("/chat/message/{memberId}")
    @SendTo("/sub/chat")
    public Message recievedMessage(
            @DestinationVariable Long memberId,
            Message msg
    ) {
        log.info("Message Received -> From: {}, msg: {}", memberId, msg.getMessage());
        return msg;
    }

}

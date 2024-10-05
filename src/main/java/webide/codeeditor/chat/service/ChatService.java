package webide.codeeditor.chat.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import webide.codeeditor.chat.repository.Chat;
import webide.codeeditor.chat.repository.ChatRepository;
import webide.codeeditor.chat.repository.Message;

import java.sql.Timestamp;

@RequiredArgsConstructor
@Service
public class ChatService {

    private final ChatRepository chatRepository;

    @Transactional(transactionManager = "createChatTransactionManager")
    public void saveChatMessage(Message msg, Long chatRoomId, Long memberId) {
        Chat chat = Chat.builder().
                chatRoomId(chatRoomId).
                memberId(memberId).
                message(msg.getMessage()).
                created_at(new Timestamp(System.currentTimeMillis())).build();

        chatRepository.save(chat);
    }
}

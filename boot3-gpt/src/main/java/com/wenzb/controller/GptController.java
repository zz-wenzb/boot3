package com.wenzb.controller;

import com.theokanning.openai.OpenAiService;
import com.theokanning.openai.completion.chat.ChatCompletionChoice;
import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatCompletionResult;
import com.theokanning.openai.completion.chat.ChatMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author wenzb
 */
@RestController
@RequestMapping("/gpt")
public class GptController {

    @Autowired
    private OpenAiService openAiService;

    @GetMapping("/question")
    public String question(String prompt) throws IOException {
        List<ChatMessage> messages = buildChatMessage(prompt);
        ChatCompletionRequest completionRequest = ChatCompletionRequest.builder()
                .model("gpt-3.5-turbo")
                .messages(messages)
                .build();
        ChatCompletionResult completionResult = openAiService.createChatCompletion(completionRequest);
        List<ChatCompletionChoice> choices = completionResult.getChoices();
        if(CollectionUtils.isEmpty(choices)){
            return "";
        }
        return choices.get(0).getMessage().getContent();
    }

    /**
     * 构建消息类
     * @param prompt
     * @return
     */
    private List<ChatMessage> buildChatMessage(String prompt){
        List<ChatMessage> messages = new ArrayList<>();
        ChatMessage systemMsg = new ChatMessage();
        systemMsg.setRole("system");
        systemMsg.setContent("我是一名JAVA程序员，可以回答任何JAVA相关的问题。");
        messages.add(systemMsg);
        ChatMessage userMsg = new ChatMessage();
        userMsg.setRole("user");
        userMsg.setContent(prompt);
        messages.add(userMsg);
        return messages;
    }
}

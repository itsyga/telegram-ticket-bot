package ru.itsyga.telegramticketbot.director;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardRemove;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import ru.itsyga.telegramticketbot.util.Button;

import java.util.List;

@Component
public class SendMessageDirector {
    private SendMessage.SendMessageBuilder<?, ?> createBaseSendMessageBuilder(Long chatId, String msgText) {
        return SendMessage.builder()
                .chatId(chatId)
                .text(msgText);
    }

    public SendMessage build(Long chatId, String msgText) {
        return createBaseSendMessageBuilder(chatId, msgText)
                .build();
    }

    public SendMessage buildWithRemoveKeyboard(Long chatId, String msgText) {
        return createBaseSendMessageBuilder(chatId, msgText)
                .replyMarkup(ReplyKeyboardRemove.builder()
                        .removeKeyboard(true)
                        .build())
                .build();
    }

    public SendMessage buildWithBaseKeyboard(Long chatId, String msgText) {
        return createBaseSendMessageBuilder(chatId, msgText)
                .replyMarkup(
                        ReplyKeyboardMarkup.builder()
                                .keyboard(
                                        List.of(
                                                new KeyboardRow(Button.BACK.getButton()),
                                                new KeyboardRow(Button.RESTART.getButton())
                                        ))
                                .resizeKeyboard(true)
                                .build())
                .build();
    }

    public SendMessage buildWithBaseInlineKeyboard(Long chatId, String msgText, String callbackData) {
        return createBaseSendMessageBuilder(chatId, msgText)
                .replyMarkup(
                        InlineKeyboardMarkup.builder()
                                .keyboardRow(
                                        new InlineKeyboardRow(
                                                InlineKeyboardButton.builder()
                                                        .text("Выбрать")
                                                        .callbackData(callbackData)
                                                        .build()
                                        ))
                                .build())
                .build();
    }
}

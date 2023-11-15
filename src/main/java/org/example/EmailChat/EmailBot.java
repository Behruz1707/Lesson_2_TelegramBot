package org.example.EmailChat;

import lombok.SneakyThrows;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.regex.Pattern;


public class EmailBot extends TelegramLongPollingBot {

    public EmailBot() {
        super(
                "6784682031:AAHKp8v686_6KaCUdgsAfw2SH4hqe6pMCNw");
    }


    @SneakyThrows
    @Override
    public void onUpdateReceived(Update update) {

        if(update.hasMessage()){
            Message message=update.getMessage();
            if(message.hasText()){
                final String text=message.getText();
                if(text.equalsIgnoreCase("/start")){
                    final String text1=message.getText();
                    SendMessage sendMessage=new SendMessage(message.getChatId().toString(),"Hello "+message.getFrom().getFirstName()+
                            " enter email:");
                    Management.USER_STEPS.put(message.getChatId(),Step.MY_EMAIL);
                    this.execute(sendMessage);
                } else if (Management.USER_STEPS.get(message.getChatId()) == Step.MY_EMAIL) {
                    final String email=message.getText();

                    SenderInformation senderInformation=new SenderInformation();
                    senderInformation.setSenderEmail(email);
                    Management.USER_SENDER.put(message.getChatId(),senderInformation);
                    if(Pattern.matches("[a-z]+[0-9]{2}\\@gmail.com",email)){
                        SendMessage sendMessage=new SendMessage(message.getChatId().toString(),"Enter token (myaccount.google.com/apppasswords): ");
                        Management.USER_STEPS.put(message.getChatId(),Step.MY_TOKEN);
                        this.execute(sendMessage);
                    }else {
                        this.execute(new SendMessage(message.getChatId().toString(),"re-enter the information: "));
                    }
                } else if (Management.USER_STEPS.get(message.getChatId()) == Step.MY_TOKEN) {
                    final String token=message.getText();

                     Management.USER_SENDER.get(message.getChatId()).setToken(token);
                     if(Pattern.matches("[a-z]{16}",token)){
                         Management.USER_STEPS.put(message.getChatId(),Step.RECIPIENT_EMAIL);
                         this.execute(new SendMessage(message.getChatId().toString(),"Enter recipent email: "));
                     }else {
                         this.execute(new SendMessage(message.getChatId().toString(),"re-enter the information: "));
                     }
                } else if (Management.USER_STEPS.get(message.getChatId()) == Step.RECIPIENT_EMAIL) {
                    final String recipent=message.getText();

                    Information information=new Information();
                    information.setRecipient(recipent);
                    Management.USER_MASSAGES.put(message.getChatId(),information);
                    if (Pattern.matches("[a-z]+[0-9]{2}\\@gmail.com", recipent)) {
                        Management.USER_STEPS.put(message.getChatId(),Step.SENDING_SUBJECT);
                        SendMessage sendMessage=new SendMessage(message.getChatId().toString()," Enter subject: ");
                        this.execute(sendMessage);
                    }else {
                        this.execute(new SendMessage(message.getChatId().toString(),"re-enter the information: "));
                    }
                } else if (Management.USER_STEPS.get(message.getChatId()) == Step.SENDING_SUBJECT) {
                    final String subject=message.getText();

                    Management.USER_MASSAGES.get(message.getChatId()).setSubject(subject);
                    Management.USER_STEPS.put(message.getChatId(),Step.SENDING_TEXT);
                    this.execute(new SendMessage(message.getChatId().toString(),"Enter massage: "));
                } else if (Management.USER_STEPS.get(message.getChatId()) == Step.SENDING_TEXT) {
                    final String message2=message.getText();

                    Management.USER_MASSAGES.get(message.getChatId()).setMassage(message2);
                    EmailManager.EmailSend(Management.USER_MASSAGES.get(message.getChatId()),Management.USER_SENDER.get(message.getChatId()));

                    this.execute(new SendMessage(message.getChatId().toString(),"Your message successfuly sent to "+
                            Management.USER_MASSAGES.get(message.getChatId()).getRecipient()));
                    Management.USER_STEPS.put(message.getChatId(),Step.APP_FINISHED);
                } else if (Management.USER_STEPS.get(message.getChatId()) == Step.APP_FINISHED) {
                    this.execute(new SendMessage(message.getChatId().toString(),"/start"));
                }
            }
        }

    }

    @Override
    public String getBotUsername() {
        return "t.me/tester_behruz159_bot";
    }
}
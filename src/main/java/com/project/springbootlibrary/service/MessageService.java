package com.project.springbootlibrary.service;

import com.project.springbootlibrary.Dao.MessageDao;
import com.project.springbootlibrary.entity.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class MessageService {
    private MessageDao messageDao;
    @Autowired
    MessageService(MessageDao messageDao){
        this.messageDao = messageDao;
    }

    public void postMessage(Message messageRequest , String userEmail){
        Message message = new Message(messageRequest.getTitle() , messageRequest.getQuestion());
        message.setUserEmail(userEmail);
        messageDao.save(message);
    }
}

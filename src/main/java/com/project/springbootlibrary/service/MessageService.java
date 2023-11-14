package com.project.springbootlibrary.service;

import com.project.springbootlibrary.Dao.MessageDao;
import com.project.springbootlibrary.entity.Message;
import com.project.springbootlibrary.requestmodels.AdminQuestionRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

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

    public void putMessage(AdminQuestionRequest adminQuestionRequest , String userEmail) throws Exception{
        Optional<Message> message = messageDao.findById(adminQuestionRequest.getId());
        if(!message.isPresent()){
            throw new Exception("Message Was not found");
        }
        message.get().setAdminEmail(userEmail);
        message.get().setResponse(adminQuestionRequest.getResponse());
        message.get().setClosed(true);
        messageDao.save(message.get());
    }
}

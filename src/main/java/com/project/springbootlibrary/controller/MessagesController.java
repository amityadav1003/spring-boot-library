package com.project.springbootlibrary.controller;

import com.project.springbootlibrary.entity.Message;
import com.project.springbootlibrary.service.MessageService;
import com.project.springbootlibrary.utils.ExtractJWT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


@CrossOrigin("http://localhost:3000")
@RestController
@RequestMapping("/api/messages")
public class MessagesController {

    private MessageService messageService;
    @Autowired
    private MessagesController(MessageService messageService){
        this.messageService = messageService;
    }

    @PostMapping("/secure/addmessage")
    public void postMessage(@RequestHeader(value = "Authorization")String token , @RequestBody Message messageBody){
        String userEmail = ExtractJWT.payloadJWTExtraction(token , "\"sub\"");
        messageService.postMessage(messageBody , userEmail);
    }

}

package com.project.springbootlibrary.controller;

import com.project.springbootlibrary.requestmodels.PaymentInfoRequest;
import com.project.springbootlibrary.service.PaymentService;
import com.project.springbootlibrary.utils.ExtractJWT;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin("httpd://localhost:3000")
@RestController
@RequestMapping("/api/payment/secure")
public class PaymentController {

    private PaymentService paymentService;
    @Autowired
    public PaymentController(PaymentService paymentService){
        this.paymentService = paymentService;
    }

    @PostMapping("/paymentCreate")
    public ResponseEntity<String> createPaymentIntent(@RequestBody PaymentInfoRequest paymentInfoRequest) throws StripeException {
        PaymentIntent paymentIntent = paymentService.createPaymentIntent(paymentInfoRequest);
        String paymentIntentString = paymentIntent.toJson();
        return new ResponseEntity<>(paymentIntentString , HttpStatus.OK);
    }

    @PutMapping("/paymentUpdate")
    public ResponseEntity<String> stripePaymentComplete(@RequestHeader(value = "Authorization") String token) throws Exception{
        String userEmail = ExtractJWT.payloadJWTExtraction(token , "\"sub\"");
        if(userEmail  == null){
            throw new Exception("User Email is Missing");
        }
        else{
            return paymentService.stripePayment(userEmail);
        }
    }

}

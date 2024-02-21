package com.project.springbootlibrary.service;

import com.project.springbootlibrary.Dao.PaymentRepository;
import com.project.springbootlibrary.entity.Payment;
import com.project.springbootlibrary.requestmodels.PaymentInfoRequest;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.model.PaymentMethod;
import com.stripe.model.StripeObjectInterface;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional
public class PaymentService {

    private PaymentRepository paymentRepository;

    @Autowired
    public PaymentService(PaymentRepository paymentRepository , @Value("${stripe.secret.key}") String secretKey){
        this.paymentRepository = paymentRepository;
        Stripe.apiKey = secretKey;
    }

    public PaymentIntent createPaymentIntent(PaymentInfoRequest paymentInfoRequest) throws StripeException {
        List<String> paymentMethodList = new ArrayList<>();
        paymentMethodList.add("card");
        Map<String , Object>  objectMap = new HashMap<>();
        objectMap.put("amount" , paymentInfoRequest.getAmount());
        objectMap.put("currency" , paymentInfoRequest.getCurrency());
        objectMap.put("paymentMethods" , paymentMethodList);
        return PaymentIntent.create(objectMap);
    }
    public ResponseEntity<String> stripePayment(String userEmail) throws Exception{
       Payment payment = paymentRepository.findByUserEmail(userEmail);
       if(payment == null){
           throw new Exception("Payment Information  is missing");
       }
       else{
           payment.setAmount(0.00);
           paymentRepository.save(payment);
       }
       return new ResponseEntity<String>(HttpStatus.OK);
    }




}

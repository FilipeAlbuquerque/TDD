package com.tdd.TDD.controller;

import com.tdd.TDD.exceptions.OrderAlreadyPaid;
import com.tdd.TDD.model.Payment;
import com.tdd.TDD.model.Receipt;
import com.tdd.TDD.response.PaymentResponse;
import com.tdd.TDD.resquest.PaymentRequest;
import com.tdd.TDD.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestController
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @PostMapping("/order/{id}/payment")
    public ResponseEntity<PaymentResponse> pay( @PathVariable("id") Long orderId,
                                                @RequestBody @Validated PaymentRequest paymentRequest,
                                                UriComponentsBuilder uriComponentsBuilder) {

        Payment payment = orderService.pay(orderId, paymentRequest.getCreditCardNumber());
        URI location = uriComponentsBuilder.path("/order/{id}/receipt")
                       .buildAndExpand(orderId).toUri();
        PaymentResponse response = new PaymentResponse(
                payment.getOrder().getId(),
                payment.getCreditCardNumber()
        );
        return ResponseEntity.created(location).body(response);
    }

    @GetMapping("/order/{id}/receipt")
    public ResponseEntity<Receipt> getReceipt(@PathVariable("id") Long orderId) {
        Receipt receipt = orderService.getReceipt(orderId);
        return ResponseEntity.ok().body(receipt);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    @ResponseBody
    public String handleOrderAlreadyPaid(OrderAlreadyPaid orderAlreadyPaid) {
        return orderAlreadyPaid.getMessage();
    }
}

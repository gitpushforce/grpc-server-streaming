package com.study.client;

import com.study.models.Money;
import io.grpc.stub.StreamObserver;

import java.util.concurrent.CountDownLatch;

public class MoneyStreamingResponse implements StreamObserver<Money> {
    private CountDownLatch latch;

    public MoneyStreamingResponse(CountDownLatch latch) {
        this.latch = latch;
    }

    @Override
    public void onNext(Money money) {
        System.out.println("Received async: " + money.getValue());
    }

    @Override
    public void onError(Throwable t) {
        System.out.println(t.getMessage());
        latch.countDown();
    }

    @Override
    public void onCompleted() {
        System.out.println("Server is Done!");
        latch.countDown();
    }
}

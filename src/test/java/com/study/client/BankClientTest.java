package com.study.client;

import com.google.common.util.concurrent.Uninterruptibles;
import com.study.models.BankServiceGrpc;
import com.study.models.WithDrawRequest;
import io.grpc.ManagedChannel;
import io.grpc.netty.shaded.io.grpc.netty.NettyChannelBuilder;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class BankClientTest {

    private BankServiceGrpc.BankServiceBlockingStub blockingStub;
    private BankServiceGrpc.BankServiceStub bankServiceStub;

    @BeforeAll
    public void setup() {
        ManagedChannel managedChannel = NettyChannelBuilder.forAddress("localhost", 8089)
                .usePlaintext()
                .build();

        this.blockingStub = BankServiceGrpc.newBlockingStub(managedChannel);
        this.bankServiceStub = BankServiceGrpc.newStub(managedChannel);
    }

    // Blocking Stub (synchronous)
    @Test
    public void withdrawBlockingStubTest() {
        WithDrawRequest withDrawRequest = WithDrawRequest.newBuilder()
                                                        .setAccountNumber(7)
                                                        .setAmount(40)
                                                        .build();
        this.blockingStub.withdraw(withDrawRequest)
                .forEachRemaining(money -> System.out.println("Received: " + money.getValue()));
    }

    // Blocking Stub (synchronous)
    @Test
    public void withdrawAsyncTest() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        WithDrawRequest withDrawRequest = WithDrawRequest.newBuilder()
                .setAccountNumber(7)
                .setAmount(40)
                .build();
        this.bankServiceStub.withdraw(withDrawRequest, new MoneyStreamingResponse(latch));
        latch.await();
    }
}
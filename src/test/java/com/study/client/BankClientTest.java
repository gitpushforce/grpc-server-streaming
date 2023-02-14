package com.study.client;

import com.study.models.BankServiceGrpc;
import com.study.models.WithDrawRequest;
import io.grpc.ManagedChannel;
import io.grpc.netty.shaded.io.grpc.netty.NettyChannelBuilder;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class BankClientTest {

    private BankServiceGrpc.BankServiceBlockingStub blockingStub;

    @BeforeAll
    public void setup() {
        ManagedChannel managedChannel = NettyChannelBuilder.forAddress("localhost", 8089)
                .usePlaintext()
                .build();

        this.blockingStub = BankServiceGrpc.newBlockingStub(managedChannel);
    }

    @Test
    public void withdrawTest() {
        WithDrawRequest withDrawRequest = WithDrawRequest.newBuilder()
                                                        .setAccountNumber(7)
                                                        .setAmount(40)
                                                        .build();
        this.blockingStub.withdraw(withDrawRequest)
                .forEachRemaining(money -> System.out.println("Received: " + money.getValue()));
    }
}

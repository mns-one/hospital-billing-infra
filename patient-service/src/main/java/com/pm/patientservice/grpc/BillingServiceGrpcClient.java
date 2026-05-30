package com.pm.patientservice.grpc;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import billing.BillingRequest;
import billing.BillingResponse;
import billing.BillingServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class BillingServiceGrpcClient {

    private final String serverAddress;
    private final int serverPort;
    private final BillingServiceGrpc.BillingServiceBlockingStub blockingStub;
    
    // constuctor for grpc client
    public BillingServiceGrpcClient(
        @Value("${billing.service.address}") String serverAddress,
        @Value("${billing.service.grpc.port}") int serverPort
    ){
        this.serverAddress = serverAddress;
        this.serverPort = serverPort;

        log.info("Connecting to Billing gRPC {}:{}", serverAddress, serverPort);

        ManagedChannel channel = ManagedChannelBuilder.forAddress(serverAddress, serverPort).usePlaintext().build();

        blockingStub = BillingServiceGrpc.newBlockingStub(channel);
    }

    // grpc methods

    public BillingResponse createBillingAccount(
        String patientId,
        String name,
        String email
    ){

        BillingRequest request = BillingRequest.newBuilder()
                                    .setPatientId(patientId)
                                    .setName(name)
                                    .setEmail(email)
                                    .build();
        
        BillingResponse response = blockingStub.createBillingAccount(request);
        log.info("Recived gRPC response {}", response);
        return response;

    }
    
}

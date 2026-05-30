package com.pm.billingservice.grpc;

import billing.BillingRequest;
import billing.BillingResponse;
import billing.BillingServiceGrpc.BillingServiceImplBase;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import lombok.extern.slf4j.Slf4j;

@GrpcService
@Slf4j
public class BillingGrpcService extends BillingServiceImplBase {

    @Override
    public void createBillingAccount(BillingRequest  billingRequest, StreamObserver<BillingResponse> responseObserver){

        log.info("gRPC request received {}", billingRequest.toString());

        // billing bussiness logic here

        BillingResponse response = BillingResponse.newBuilder()
                                        .setAccountId("2842")
                                        .setStatus("Success!")
                                        .build();
        
        responseObserver.onNext(response);
        responseObserver.onCompleted();

    }

}



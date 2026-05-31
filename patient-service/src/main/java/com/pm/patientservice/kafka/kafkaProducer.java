package com.pm.patientservice.kafka;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.pm.patientservice.model.Patient;

import lombok.extern.slf4j.Slf4j;
import patient.events.PatientEvent;

@Service
@Slf4j
public class kafkaProducer {

    private final KafkaTemplate<String, byte[]> kafkaTemplate;

    public kafkaProducer(KafkaTemplate<String, byte[]> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendEvent(Patient patient) {

        PatientEvent event = PatientEvent.newBuilder()
                                .setName(patient.getName())
                                .setEmail(patient.getEmail())
                                .setEventType("PATIENT_CREATED")
                                .build();
        
        try{
            kafkaTemplate.send("patient", event.toByteArray());
            log.info("Kafka event Send!");
        }
        catch(Exception e){
            log.error("Error sending Patient_Created event: {}", e);
        }

    }

}


package com.pm.patientservice.dto;

import lombok.Data;

@Data
public class PatientRecordPublicDTO {

    private String name;
    private String email;
    private String address;
    private String dateOfBirth;
    private String registeredDate;
    
}

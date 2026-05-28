package com.pm.patientservice.utils;

import com.pm.patientservice.dto.PatientRecordPublicDTO;
import com.pm.patientservice.model.Patient;
import org.springframework.stereotype.Component;

@Component
public class MapToPublicRecordDTO {
    
    // helper func to convert db entity to public dto
    public void toPublicRecord(Patient p, PatientRecordPublicDTO res) {
        res.setName(p.getName());
        res.setEmail(p.getEmail());
        res.setAddress(p.getAddress());
        res.setDateOfBirth(p.getDateOfBirth().toString());
        res.setRegisteredDate(p.getRegisteredDate().toString());
    }
    
}

package com.pm.patientservice.dto.deletepatient;

import java.util.UUID;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class DeletePatientRecordRequestDTO {
    
    @NotNull
    private UUID id;
    
}

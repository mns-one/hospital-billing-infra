package com.pm.patientservice.dto.updatepatient;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class UpdatePatientRecordRequestDTO {
    
    private String name;

    @Email(message = "Email must be valid")
    private String email;
    
    private String address;

    @Pattern(regexp = "\\d{4}-\\d{2}-\\d{2}", message = "Date must be in YYYY-MM-DD format")
    private String dateOfBirth;
    
}

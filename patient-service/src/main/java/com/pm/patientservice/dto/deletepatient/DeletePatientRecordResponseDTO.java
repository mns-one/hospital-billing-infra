package com.pm.patientservice.dto.deletepatient;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class DeletePatientRecordResponseDTO {

    @NotNull
    private String msg;
}

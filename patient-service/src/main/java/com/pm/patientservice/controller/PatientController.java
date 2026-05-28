package com.pm.patientservice.controller;

import org.springframework.web.bind.annotation.RestController;

import com.pm.patientservice.dto.createpatient.CreatePatientRequestDTO;
import com.pm.patientservice.dto.createpatient.CreatePatientResponseDTO;
import com.pm.patientservice.dto.deletepatient.DeletePatientRecordRequestDTO;
import com.pm.patientservice.dto.deletepatient.DeletePatientRecordResponseDTO;
import com.pm.patientservice.dto.getallpatient.GetAllPatientRecordsResponseDTO;
import com.pm.patientservice.dto.updatepatient.UpdatePatientRecordRequestDTO;
import com.pm.patientservice.dto.updatepatient.UpdatePatientRecordResponseDTO;
import com.pm.patientservice.service.PatientService;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.UUID;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;




@RestController
@RequestMapping("/patients")
public class PatientController {

    private PatientService service;

    PatientController(PatientService service){
        this.service = service;
    }
    
    @PostMapping("/create")
    public CreatePatientResponseDTO createPatient(@Valid @RequestBody CreatePatientRequestDTO body){
        return service.createPatientRecord(body);
    }

    @GetMapping("/getall")
    public List<GetAllPatientRecordsResponseDTO> getMethodName() {
        return service.getAllPatientRecords();
    }

    @PutMapping("update/{id}")
    public UpdatePatientRecordResponseDTO putMethodName(
        @PathVariable UUID id, @Valid @RequestBody UpdatePatientRecordRequestDTO body)
    {
        return service.updatePatientRecord(id, body);
    }

    @DeleteMapping("delete/{id}")
    public DeletePatientRecordResponseDTO putMethodName(@PathVariable String id) {
        
        DeletePatientRecordRequestDTO data = new DeletePatientRecordRequestDTO();
        data.setId(UUID.fromString(id));

        return service.deletePatientRecord(data);
    }
    
    
}

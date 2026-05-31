package com.pm.patientservice.service;

import com.pm.patientservice.kafka.kafkaProducer;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.pm.patientservice.dto.createpatient.CreatePatientRequestDTO;
import com.pm.patientservice.dto.createpatient.CreatePatientResponseDTO;
import com.pm.patientservice.dto.deletepatient.DeletePatientRecordRequestDTO;
import com.pm.patientservice.dto.deletepatient.DeletePatientRecordResponseDTO;
import com.pm.patientservice.dto.getallpatient.GetAllPatientRecordsResponseDTO;
import com.pm.patientservice.dto.updatepatient.UpdatePatientRecordRequestDTO;
import com.pm.patientservice.dto.updatepatient.UpdatePatientRecordResponseDTO;
import com.pm.patientservice.exception.CommonException;
import com.pm.patientservice.exception.DuplicateEmailException;
import com.pm.patientservice.grpc.BillingServiceGrpcClient;
import com.pm.patientservice.model.Patient;
import com.pm.patientservice.repository.PatientRepository;
import com.pm.patientservice.utils.MapToPublicRecordDTO;

import billing.BillingResponse;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class PatientService {

    private final kafkaProducer kafkaProducer;
    private PatientRepository repo;
    private MapToPublicRecordDTO mapper;
    private BillingServiceGrpcClient billingServiceGrpcClient;

    PatientService(PatientRepository repo, MapToPublicRecordDTO mapper, BillingServiceGrpcClient grpcClient, kafkaProducer kafkaProducer){
        this.repo = repo;
        this.mapper = mapper;
        this.billingServiceGrpcClient = grpcClient;
        this.kafkaProducer = kafkaProducer;
    }
     
    // Create a new Patient record
    public CreatePatientResponseDTO createPatientRecord(CreatePatientRequestDTO data){

        if(repo.existsByEmail(data.getEmail())){
            throw new DuplicateEmailException();
        }

        Patient pt = new Patient();

        pt.setName(data.getName());
        pt.setEmail(data.getEmail());
        pt.setAddress(data.getAddress());
        pt.setDateOfBirth(LocalDate.parse(data.getDateOfBirth()));

        Patient repoRecord = repo.save(pt);
        
        CreatePatientResponseDTO res = new CreatePatientResponseDTO();
        mapper.toPublicRecord(repoRecord, res);

        //call billing service
        try{
            billingServiceGrpcClient.createBillingAccount(repoRecord.getId().toString(), repoRecord.getName(), repoRecord.getEmail());
        }
        catch(Exception e){
            throw new CommonException("gRPC server connection failed");
        }

        kafkaProducer.sendEvent(repoRecord);
        log.info("Kafka event send from service! {}", repoRecord);
        
        return res;

    }

    // Get all Patient records from DB
    public List<GetAllPatientRecordsResponseDTO> getAllPatientRecords(){

        List<Patient> plist = repo.findAll();

        List<GetAllPatientRecordsResponseDTO> res = plist.stream().map(p -> {
            GetAllPatientRecordsResponseDTO pt = new GetAllPatientRecordsResponseDTO();
            mapper.toPublicRecord(p, pt);
            return pt;
        }).toList();

        return res;

    }
    
    // Get all Patient records from DB
    public UpdatePatientRecordResponseDTO updatePatientRecord(UUID id, UpdatePatientRecordRequestDTO data){

        Patient p = repo.findById(id).orElse(null);

        if(p == null){
            throw new CommonException("No Patient record found for given ID");
        }

        if(repo.existsByEmail(data.getEmail())){
            throw new DuplicateEmailException();
        }

        if(data.getName() != null) p.setName(data.getName());
        if(data.getEmail() != null) p.setEmail(data.getEmail());
        if(data.getAddress() != null) p.setAddress(data.getAddress());
        if(data.getDateOfBirth() != null) p.setDateOfBirth(LocalDate.parse(data.getDateOfBirth()));

        repo.save(p);

        UpdatePatientRecordResponseDTO res = new UpdatePatientRecordResponseDTO();
        mapper.toPublicRecord(p, res);

        return res;

    }

    // Delete a Patient record
    public DeletePatientRecordResponseDTO deletePatientRecord(DeletePatientRecordRequestDTO data){

        Patient p = repo.findById(data.getId()).orElse(null);

        if(p == null){
            throw new CommonException("No Patient record found for given ID");
        }

        repo.delete(p);

        DeletePatientRecordResponseDTO res = new DeletePatientRecordResponseDTO();
        res.setMsg("Patient record deleted successfully");

        return res;
    }
    
}

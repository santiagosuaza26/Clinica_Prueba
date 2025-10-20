package app.clinic.patient.application.mapper;

import java.time.LocalDateTime;

import app.clinic.patient.application.dto.EmergencyContactDto;
import app.clinic.patient.application.dto.InsuranceDto;
import app.clinic.patient.application.dto.PatientRequestDto;
import app.clinic.patient.application.dto.PatientResponseDto;
import app.clinic.patient.domain.model.EmergencyContact;
import app.clinic.patient.domain.model.Insurance;
import app.clinic.patient.domain.model.Patient;

public class PatientMapper {

    public static Patient toDomain(PatientRequestDto dto, Long createdByUserId) {
        return new Patient(
                null,
                dto.username(),
                dto.password(),
                dto.fullName(),
                dto.cedula(),
                dto.birthDate(),
                dto.gender(),
                dto.address(),
                dto.phone(),
                dto.email(),
                toEmergencyContact(dto.emergencyContact()),
                toInsurance(dto.insurance()),
                createdByUserId,
                LocalDateTime.now()
        );
    }

    public static PatientResponseDto toResponse(Patient patient) {
        return new PatientResponseDto(
                patient.getId(),
                patient.getUsername(),
                patient.getFullName(),
                patient.getCedula(),
                patient.getBirthDate(),
                patient.getGender(),
                patient.getAddress(),
                patient.getPhone(),
                patient.getEmail(),
                toEmergencyContactDto(patient.getEmergencyContact()),
                toInsuranceDto(patient.getInsurance()),
                patient.getCreatedAt()
        );
    }

    private static EmergencyContact toEmergencyContact(EmergencyContactDto dto) {
        if (dto == null) return null;
        return new EmergencyContact(dto.name(), dto.relation(), dto.phone());
    }

    private static Insurance toInsurance(InsuranceDto dto) {
        if (dto == null) return null;
        return new Insurance(dto.companyName(), dto.policyNumber(), dto.active(), dto.expiryDate());
    }

    private static EmergencyContactDto toEmergencyContactDto(EmergencyContact contact) {
        if (contact == null) return null;
        return new EmergencyContactDto(contact.getName(), contact.getRelation(), contact.getPhone());
    }

    private static InsuranceDto toInsuranceDto(Insurance insurance) {
        if (insurance == null) return null;
        return new InsuranceDto(
                insurance.getCompanyName(),
                insurance.getPolicyNumber(),
                insurance.isActive(),
                insurance.getExpiryDate()
        );
    }
}

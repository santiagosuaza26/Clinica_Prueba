package app.clinic.patient.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import app.clinic.patient.application.usecase.CreatePatientUseCase;
import app.clinic.patient.application.usecase.DeletePatientUseCase;
import app.clinic.patient.application.usecase.GetAllPatientsUseCase;
import app.clinic.patient.application.usecase.GetPatientByCedulaUseCase;
import app.clinic.patient.application.usecase.UpdatePatientUseCase;
import app.clinic.patient.domain.repository.PatientRepository;
import app.clinic.patient.domain.service.PatientValidatorService;

@Configuration
public class PatientConfig {

    @Bean
    public PatientValidatorService patientValidatorService() {
        return new PatientValidatorService();
    }

    @Bean
    public CreatePatientUseCase createPatientUseCase(PatientRepository repository, PatientValidatorService validator) {
        return new CreatePatientUseCase(repository, validator);
    }

    @Bean
    public GetAllPatientsUseCase getAllPatientsUseCase(PatientRepository repository) {
        return new GetAllPatientsUseCase(repository);
    }

    @Bean
    public GetPatientByCedulaUseCase getPatientByCedulaUseCase(PatientRepository repository) {
        return new GetPatientByCedulaUseCase(repository);
    }

    @Bean
    public UpdatePatientUseCase updatePatientUseCase(PatientRepository repository, PatientValidatorService validator) {
        return new UpdatePatientUseCase(repository, validator);
    }

    @Bean
    public DeletePatientUseCase deletePatientUseCase(PatientRepository repository) {
        return new DeletePatientUseCase(repository);
    }
}

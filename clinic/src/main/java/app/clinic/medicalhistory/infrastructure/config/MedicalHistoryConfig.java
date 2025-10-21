package app.clinic.medicalhistory.infrastructure.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;

import app.clinic.medicalhistory.application.usecase.CreateOrUpdateVisitUseCase;
import app.clinic.medicalhistory.application.usecase.DeleteHistoryUseCase;
import app.clinic.medicalhistory.application.usecase.DeleteVisitUseCase;
import app.clinic.medicalhistory.application.usecase.GetHistoryByPatientUseCase;
import app.clinic.medicalhistory.domain.repository.MedicalHistoryRepository;
import app.clinic.medicalhistory.domain.service.MedicalHistoryValidator;
import app.clinic.medicalhistory.infrastructure.mapper.MedicalHistoryDocumentMapper;
import app.clinic.medicalhistory.infrastructure.repository.MongoMedicalHistoryRepository;

@Configuration
@EnableMongoRepositories(
    basePackageClasses = MongoMedicalHistoryRepository.class,
    mongoTemplateRef = "medicalHistoryMongoTemplate"
)
public class MedicalHistoryConfig {

    @Value("${spring.data.mongodb.host:localhost}")
    private String mongoHost;

    @Value("${spring.data.mongodb.port:27017}")
    private int mongoPort;

    @Value("${spring.data.mongodb.database:clinic_medical_history}")
    private String mongoDatabase;

    @Bean("medicalHistoryMongoTemplate")
    public MongoTemplate medicalHistoryMongoTemplate() {
        MongoClient mongoClient = MongoClients.create("mongodb://" + mongoHost + ":" + mongoPort);
        return new MongoTemplate(mongoClient, mongoDatabase);
    }

    @Bean
    public MedicalHistoryDocumentMapper medicalHistoryDocumentMapper() {
        return new MedicalHistoryDocumentMapper();
    }


    @Bean
    public MedicalHistoryValidator medicalHistoryValidator() {
        return new MedicalHistoryValidator();
    }

    @Bean
    public CreateOrUpdateVisitUseCase createOrUpdateVisitUseCase(
            MedicalHistoryRepository repository,
            MedicalHistoryValidator validator) {
        return new CreateOrUpdateVisitUseCase(repository, validator);
    }

    @Bean
    public GetHistoryByPatientUseCase getHistoryByPatientUseCase(MedicalHistoryRepository repository) {
        return new GetHistoryByPatientUseCase(repository);
    }

    @Bean
    public DeleteVisitUseCase deleteVisitUseCase(MedicalHistoryRepository repository) {
        return new DeleteVisitUseCase(repository);
    }

    @Bean
    public DeleteHistoryUseCase deleteHistoryUseCase(MedicalHistoryRepository repository) {
        return new DeleteHistoryUseCase(repository);
    }
}

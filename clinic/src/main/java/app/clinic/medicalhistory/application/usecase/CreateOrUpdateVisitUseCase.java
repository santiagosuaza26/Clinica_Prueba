package app.clinic.medicalhistory.application.usecase;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import app.clinic.medicalhistory.domain.exception.InvalidMedicalRecordException;
import app.clinic.medicalhistory.domain.exception.MedicalHistoryNotFoundException;
import app.clinic.medicalhistory.domain.model.MedicalHistory;
import app.clinic.medicalhistory.domain.model.MedicalVisit;
import app.clinic.medicalhistory.domain.repository.MedicalHistoryRepository;
import app.clinic.medicalhistory.domain.service.MedicalHistoryValidator;

public class CreateOrUpdateVisitUseCase {

    private static final Logger logger = LoggerFactory.getLogger(CreateOrUpdateVisitUseCase.class);

    private final MedicalHistoryRepository repository;
    private final MedicalHistoryValidator validator;

    public CreateOrUpdateVisitUseCase(MedicalHistoryRepository repository, MedicalHistoryValidator validator) {
        this.repository = repository;
        this.validator = validator;
    }

    @Transactional
    public void execute(String patientCedula, MedicalVisit visit) {
        logger.debug("Iniciando creación/actualización de visita para paciente: {}", patientCedula);
        logger.debug("Fecha de visita: {}", visit.getDate());

        try {
            validator.validate(visit);
            logger.debug("Validación de visita exitosa");

            MedicalHistory history = repository.findByPatientCedula(patientCedula)
                .orElse(new MedicalHistory(patientCedula));
            logger.debug("Historia médica obtenida, número de visitas existentes: {}",
                        (history.getVisits() != null ? history.getVisits().size() : 0));

            history.addVisit(visit);
            logger.debug("Visita agregada exitosamente");

            repository.save(history);
            logger.debug("Historia médica guardada exitosamente");

        } catch (InvalidMedicalRecordException e) {
            logger.error("Error de validación en registro médico: {}", e.getMessage());
            throw e;
        } catch (MedicalHistoryNotFoundException e) {
            logger.error("Historia médica no encontrada: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Error inesperado al procesar visita: {}", e.getMessage(), e);
            throw new RuntimeException("Error interno del servidor al procesar la visita médica", e);
        }
    }
}

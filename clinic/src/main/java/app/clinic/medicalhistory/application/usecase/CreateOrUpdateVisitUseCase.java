package app.clinic.medicalhistory.application.usecase;

import app.clinic.medicalhistory.domain.model.MedicalHistory;
import app.clinic.medicalhistory.domain.model.MedicalVisit;
import app.clinic.medicalhistory.domain.repository.MedicalHistoryRepository;
import app.clinic.medicalhistory.domain.service.MedicalHistoryValidator;

public class CreateOrUpdateVisitUseCase {

    private final MedicalHistoryRepository repository;
    private final MedicalHistoryValidator validator;

    public CreateOrUpdateVisitUseCase(MedicalHistoryRepository repository, MedicalHistoryValidator validator) {
        this.repository = repository;
        this.validator = validator;
    }

    public void execute(String patientCedula, MedicalVisit visit) {
        System.out.println("[DEBUG] Iniciando creación/actualización de visita para paciente: " + patientCedula);
        System.out.println("[DEBUG] Fecha de visita: " + visit.getDate());

        try {
            validator.validate(visit);
            System.out.println("[DEBUG] Validación de visita exitosa");

            MedicalHistory history = repository.findByPatientCedula(patientCedula)
                .orElse(new MedicalHistory(patientCedula));
            System.out.println("[DEBUG] Historia médica obtenida, número de visitas existentes: " +
                             (history.getVisits() != null ? history.getVisits().size() : 0));

            history.addVisit(visit);
            System.out.println("[DEBUG] Visita agregada exitosamente");

            repository.save(history);
            System.out.println("[DEBUG] Historia médica guardada exitosamente");

        } catch (Exception e) {
            System.out.println("[ERROR] Error al procesar visita: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }
}

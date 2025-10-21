package app.clinic.medicalhistory.application.usecase;

import app.clinic.medicalhistory.domain.repository.MedicalHistoryRepository;

public class DeleteVisitUseCase {

    private final MedicalHistoryRepository repository;

    public DeleteVisitUseCase(MedicalHistoryRepository repository) {
        this.repository = repository;
    }

    public void execute(String patientCedula, String date) {
        System.out.println("[DEBUG] Iniciando eliminación de visita para paciente: " + patientCedula + ", fecha: " + date);

        try {
            var historyOpt = repository.findByPatientCedula(patientCedula);
            if (historyOpt.isPresent()) {
                var history = historyOpt.get();
                System.out.println("[DEBUG] Historia médica encontrada, número de visitas: " +
                                 (history.getVisits() != null ? history.getVisits().size() : 0));

                if (history.getVisits() != null && history.getVisits().containsKey(date)) {
                    System.out.println("[DEBUG] Visita encontrada, procediendo con eliminación");
                    history.getVisits().remove(date);
                    repository.save(history);
                    System.out.println("[DEBUG] Visita eliminada exitosamente");
                } else {
                    System.out.println("[WARNING] Visita no encontrada para la fecha especificada: " + date);
                }
            } else {
                System.out.println("[WARNING] No se encontró historia médica para el paciente: " + patientCedula);
            }
        } catch (Exception e) {
            System.out.println("[ERROR] Error al eliminar visita: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }
}

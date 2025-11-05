package app.clinic.medicalhistory.application.usecase;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import app.clinic.medicalhistory.domain.exception.MedicalHistoryNotFoundException;
import app.clinic.medicalhistory.domain.repository.MedicalHistoryRepository;

public class DeleteVisitUseCase {

    private static final Logger logger = LoggerFactory.getLogger(DeleteVisitUseCase.class);

    private final MedicalHistoryRepository repository;

    public DeleteVisitUseCase(MedicalHistoryRepository repository) {
        this.repository = repository;
    }

    public void execute(String patientCedula, String date) {
        logger.debug("Iniciando eliminación de visita para paciente: {}, fecha: {}", patientCedula, date);

        try {
            var historyOpt = repository.findByPatientCedula(patientCedula);
            if (historyOpt.isPresent()) {
                var history = historyOpt.get();
                logger.debug("Historia médica encontrada, número de visitas: {}",
                            (history.getVisits() != null ? history.getVisits().size() : 0));

                if (history.getVisits() != null && history.getVisits().containsKey(date)) {
                    logger.debug("Visita encontrada, procediendo con eliminación");
                    history.getVisits().remove(date);
                    repository.save(history);
                    logger.debug("Visita eliminada exitosamente");
                } else {
                    logger.warn("Visita no encontrada para la fecha especificada: {}", date);
                }
            } else {
                logger.warn("No se encontró historia médica para el paciente: {}", patientCedula);
            }
        } catch (MedicalHistoryNotFoundException e) {
            logger.error("Historia médica no encontrada: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Error inesperado al eliminar visita: {}", e.getMessage(), e);
            throw new RuntimeException("Error interno del servidor al eliminar la visita médica", e);
        }
    }
}

package app.clinic.inventory.application.usecase;

import app.clinic.inventory.domain.exception.ItemNotFoundException;
import app.clinic.inventory.domain.model.InventoryType;
import app.clinic.inventory.domain.repository.DiagnosticAidRepository;
import app.clinic.inventory.domain.repository.MedicationRepository;
import app.clinic.inventory.domain.repository.ProcedureRepository;

/**
 * Caso de uso para eliminar un elemento del inventario por ID y tipo.
 * Este caso de uso maneja la eliminación de medicamentos, procedimientos y ayudas diagnósticas.
 * Valida la existencia del elemento antes de proceder con la eliminación.
 */
public class DeleteInventoryItemUseCase {

    private final MedicationRepository medicationRepository;
    private final ProcedureRepository procedureRepository;
    private final DiagnosticAidRepository diagnosticAidRepository;

    /**
     * Constructor que inyecta las dependencias necesarias.
     *
     * @param medicationRepository repositorio para operaciones con medicamentos
     * @param procedureRepository repositorio para operaciones con procedimientos
     * @param diagnosticAidRepository repositorio para operaciones con ayudas diagnósticas
     */
    public DeleteInventoryItemUseCase(
        MedicationRepository medicationRepository,
        ProcedureRepository procedureRepository,
        DiagnosticAidRepository diagnosticAidRepository
    ) {
        this.medicationRepository = medicationRepository;
        this.procedureRepository = procedureRepository;
        this.diagnosticAidRepository = diagnosticAidRepository;
    }

    /**
     * Ejecuta la eliminación de un elemento del inventario por ID y tipo.
     *
     * @param id el ID del elemento a eliminar
     * @param type el tipo de elemento del inventario
     * @throws ItemNotFoundException si no se encuentra el elemento con el ID especificado
     * @throws IllegalArgumentException si el ID o tipo proporcionado es nulo
     */
    public void execute(Long id, InventoryType type) {
        if (id == null) {
            throw new IllegalArgumentException("El ID no puede ser nulo");
        }

        if (type == null) {
            throw new IllegalArgumentException("El tipo de inventario no puede ser nulo");
        }

        switch (type) {
            case MEDICATION -> {
                medicationRepository.findById(id)
                    .orElseThrow(() -> new ItemNotFoundException("Medicamento no encontrado con ID: " + id));
                medicationRepository.deleteById(id);
            }
            case PROCEDURE -> {
                procedureRepository.findById(id)
                    .orElseThrow(() -> new ItemNotFoundException("Procedimiento no encontrado con ID: " + id));
                procedureRepository.deleteById(id);
            }
            case DIAGNOSTIC_AID -> {
                diagnosticAidRepository.findById(id)
                    .orElseThrow(() -> new ItemNotFoundException("Ayuda diagnóstica no encontrada con ID: " + id));
                diagnosticAidRepository.deleteById(id);
            }
        }
    }
}

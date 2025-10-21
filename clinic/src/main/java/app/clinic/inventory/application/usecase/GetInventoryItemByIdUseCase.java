package app.clinic.inventory.application.usecase;

import app.clinic.inventory.domain.exception.ItemNotFoundException;
import app.clinic.inventory.domain.model.InventoryType;
import app.clinic.inventory.domain.model.Medication;
import app.clinic.inventory.domain.model.Procedure;
import app.clinic.inventory.domain.model.DiagnosticAid;
import app.clinic.inventory.domain.repository.DiagnosticAidRepository;
import app.clinic.inventory.domain.repository.MedicationRepository;
import app.clinic.inventory.domain.repository.ProcedureRepository;

/**
 * Caso de uso para obtener un elemento del inventario por ID y tipo.
 * Este caso de uso maneja la recuperación de medicamentos, procedimientos y ayudas diagnósticas.
 */
public class GetInventoryItemByIdUseCase {

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
    public GetInventoryItemByIdUseCase(
        MedicationRepository medicationRepository,
        ProcedureRepository procedureRepository,
        DiagnosticAidRepository diagnosticAidRepository
    ) {
        this.medicationRepository = medicationRepository;
        this.procedureRepository = procedureRepository;
        this.diagnosticAidRepository = diagnosticAidRepository;
    }

    /**
     * Ejecuta la búsqueda de un elemento del inventario por ID y tipo.
     *
     * @param id el ID del elemento a buscar
     * @param type el tipo de elemento del inventario
     * @return el elemento encontrado (Medication, Procedure, o DiagnosticAid)
     * @throws ItemNotFoundException si no se encuentra el elemento con el ID especificado
     * @throws IllegalArgumentException si el tipo proporcionado no es válido
     */
    public Object execute(Long id, InventoryType type) {
        if (id == null) {
            throw new IllegalArgumentException("El ID no puede ser nulo");
        }

        if (type == null) {
            throw new IllegalArgumentException("El tipo de inventario no puede ser nulo");
        }

        return switch (type) {
            case MEDICATION -> medicationRepository.findById(id)
                    .orElseThrow(() -> new ItemNotFoundException("Medicamento no encontrado con ID: " + id));
            case PROCEDURE -> procedureRepository.findById(id)
                    .orElseThrow(() -> new ItemNotFoundException("Procedimiento no encontrado con ID: " + id));
            case DIAGNOSTIC_AID -> diagnosticAidRepository.findById(id)
                    .orElseThrow(() -> new ItemNotFoundException("Ayuda diagnóstica no encontrada con ID: " + id));
        };
    }

    /**
     * Método específico para obtener un medicamento por ID con type safety mejorado.
     *
     * @param id el ID del medicamento
     * @return el medicamento encontrado
     * @throws ItemNotFoundException si no se encuentra el medicamento
     */
    public Medication getMedicationById(Long id) {
        return medicationRepository.findById(id)
                .orElseThrow(() -> new ItemNotFoundException("Medicamento no encontrado con ID: " + id));
    }

    /**
     * Método específico para obtener un procedimiento por ID con type safety mejorado.
     *
     * @param id el ID del procedimiento
     * @return el procedimiento encontrado
     * @throws ItemNotFoundException si no se encuentra el procedimiento
     */
    public Procedure getProcedureById(Long id) {
        return procedureRepository.findById(id)
                .orElseThrow(() -> new ItemNotFoundException("Procedimiento no encontrado con ID: " + id));
    }

    /**
     * Método específico para obtener una ayuda diagnóstica por ID con type safety mejorado.
     *
     * @param id el ID de la ayuda diagnóstica
     * @return la ayuda diagnóstica encontrada
     * @throws ItemNotFoundException si no se encuentra la ayuda diagnóstica
     */
    public DiagnosticAid getDiagnosticAidById(Long id) {
        return diagnosticAidRepository.findById(id)
                .orElseThrow(() -> new ItemNotFoundException("Ayuda diagnóstica no encontrada con ID: " + id));
    }
}

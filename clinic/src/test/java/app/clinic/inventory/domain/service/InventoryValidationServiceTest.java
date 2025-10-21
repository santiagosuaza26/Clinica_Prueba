package app.clinic.inventory.domain.service;

import app.clinic.inventory.domain.exception.*;
import app.clinic.inventory.domain.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Pruebas del servicio de validación de inventario")
class InventoryValidationServiceTest {

    private InventoryValidationService validationService;

    @BeforeEach
    void setUp() {
        validationService = new InventoryValidationService();
    }

    @Test
    @DisplayName("Debe validar medicamento correctamente con datos válidos")
    void shouldValidateMedicationWithValidData() {
        // Given
        Medication medication = new Medication(
            1L, "Paracetamol", "500mg", 30, 15.50, false
        );

        // When & Then
        assertDoesNotThrow(() -> validationService.validateMedication(medication));
    }

    @Test
    @DisplayName("Debe lanzar excepción cuando el nombre del medicamento está vacío")
    void shouldThrowExceptionWhenMedicationNameIsEmpty() {
        // Given
        Medication medication = new Medication(
            1L, "", "500mg", 30, 15.50, false
        );

        // When & Then
        InvalidMedicationNameException exception = assertThrows(
            InvalidMedicationNameException.class,
            () -> validationService.validateMedication(medication)
        );
        assertTrue(exception.getMessage().contains("nombre del medicamento no puede estar vacío"));
    }

    @Test
    @DisplayName("Debe lanzar excepción cuando el nombre del medicamento es muy corto")
    void shouldThrowExceptionWhenMedicationNameIsTooShort() {
        // Given
        Medication medication = new Medication(
            1L, "A", "500mg", 30, 15.50, false
        );

        // When & Then
        InvalidMedicationNameException exception = assertThrows(
            InvalidMedicationNameException.class,
            () -> validationService.validateMedication(medication)
        );
        assertTrue(exception.getMessage().contains("debe tener al menos 2 caracteres"));
    }

    @Test
    @DisplayName("Debe lanzar excepción cuando el nombre del medicamento contiene caracteres inválidos")
    void shouldThrowExceptionWhenMedicationNameContainsInvalidCharacters() {
        // Given
        Medication medication = new Medication(
            1L, "Paracetamol@#$", "500mg", 30, 15.50, false
        );

        // When & Then
        InvalidMedicationNameException exception = assertThrows(
            InvalidMedicationNameException.class,
            () -> validationService.validateMedication(medication)
        );
        assertTrue(exception.getMessage().contains("caracteres no válidos"));
    }

    @Test
    @DisplayName("Debe lanzar excepción cuando la dosis tiene formato inválido")
    void shouldThrowExceptionWhenDosageFormatIsInvalid() {
        // Given
        Medication medication = new Medication(
            1L, "Paracetamol", "dosis inválida", 30, 15.50, false
        );

        // When & Then
        InvalidMedicationDosageException exception = assertThrows(
            InvalidMedicationDosageException.class,
            () -> validationService.validateMedication(medication)
        );
        assertTrue(exception.getMessage().contains("formato de dosis no es válido"));
    }

    @Test
    @DisplayName("Debe lanzar excepción cuando la duración es negativa")
    void shouldThrowExceptionWhenDurationIsNegative() {
        // Given
        Medication medication = new Medication(
            1L, "Paracetamol", "500mg", -5, 15.50, false
        );

        // When & Then
        InvalidMedicationDurationException exception = assertThrows(
            InvalidMedicationDurationException.class,
            () -> validationService.validateMedication(medication)
        );
        assertTrue(exception.getMessage().contains("duración debe ser al menos"));
    }

    @Test
    @DisplayName("Debe lanzar excepción cuando el costo tiene más de 2 decimales")
    void shouldThrowExceptionWhenCostHasMoreThanTwoDecimals() {
        // Given
        Medication medication = new Medication(
            1L, "Paracetamol", "500mg", 30, 15.555, false
        );

        // When & Then
        InvalidMedicationCostException exception = assertThrows(
            InvalidMedicationCostException.class,
            () -> validationService.validateMedication(medication)
        );
        assertTrue(exception.getMessage().contains("no puede tener más de 2 decimales"));
    }

    @Test
    @DisplayName("Debe validar procedimiento correctamente con datos válidos")
    void shouldValidateProcedureWithValidData() {
        // Given
        Procedure procedure = new Procedure(
            1L, "Consulta médica", 1, "única", 50.00, false, null
        );

        // When & Then
        assertDoesNotThrow(() -> validationService.validateProcedure(procedure));
    }

    @Test
    @DisplayName("Debe lanzar excepción cuando las repeticiones son inválidas")
    void shouldThrowExceptionWhenRepetitionsAreInvalid() {
        // Given
        Procedure procedure = new Procedure(
            1L, "Consulta médica", -1, "única", 50.00, false, null
        );

        // When & Then
        InvalidProcedureRepetitionsException exception = assertThrows(
            InvalidProcedureRepetitionsException.class,
            () -> validationService.validateProcedure(procedure)
        );
        assertTrue(exception.getMessage().contains("repeticiones deben ser al menos 1"));
    }

    @Test
    @DisplayName("Debe lanzar excepción cuando la frecuencia es inválida")
    void shouldThrowExceptionWhenFrequencyIsInvalid() {
        // Given
        Procedure procedure = new Procedure(
            1L, "Consulta médica", 1, "frecuencia inválida", 50.00, false, null
        );

        // When & Then
        InvalidProcedureFrequencyException exception = assertThrows(
            InvalidProcedureFrequencyException.class,
            () -> validationService.validateProcedure(procedure)
        );
        assertTrue(exception.getMessage().contains("frecuencia debe ser un valor válido"));
    }

    @Test
    @DisplayName("Debe validar ayuda diagnóstica correctamente con datos válidos")
    void shouldValidateDiagnosticAidWithValidData() {
        // Given
        DiagnosticAid diagnosticAid = new DiagnosticAid(
            1L, "Radiografía de tórax", 1, 25.00, false, null
        );

        // When & Then
        assertDoesNotThrow(() -> validationService.validateDiagnosticAid(diagnosticAid));
    }

    @Test
    @DisplayName("Debe lanzar excepción cuando la cantidad es inválida")
    void shouldThrowExceptionWhenQuantityIsInvalid() {
        // Given
        DiagnosticAid diagnosticAid = new DiagnosticAid(
            1L, "Radiografía de tórax", -1, 25.00, false, null
        );

        // When & Then
        InvalidDiagnosticAidQuantityException exception = assertThrows(
            InvalidDiagnosticAidQuantityException.class,
            () -> validationService.validateDiagnosticAid(diagnosticAid)
        );
        assertTrue(exception.getMessage().contains("cantidad debe ser al menos 1"));
    }

    @Test
    @DisplayName("Debe lanzar excepción cuando requiere especialista pero no se especifica el tipo")
    void shouldThrowExceptionWhenRequiresSpecialistButTypeNotSpecified() {
        // Given
        Procedure procedure = new Procedure(
            1L, "Cirugía especializada", 1, "única", 100.00, true, null
        );

        // When & Then
        InvalidInventoryOperationException exception = assertThrows(
            InvalidInventoryOperationException.class,
            () -> validationService.validateProcedure(procedure)
        );
        assertTrue(exception.getMessage().contains("tipo de especialista es requerido"));
    }

    @Test
    @DisplayName("Debe lanzar excepción cuando no requiere especialista pero se especifica el tipo")
    void shouldThrowExceptionWhenNotRequiresSpecialistButTypeSpecified() {
        // Given
        DiagnosticAid diagnosticAid = new DiagnosticAid(
            1L, "Análisis básico", 1, 10.00, false, SpecialistType.GENERAL
        );

        // When & Then
        InvalidInventoryOperationException exception = assertThrows(
            InvalidInventoryOperationException.class,
            () -> validationService.validateDiagnosticAid(diagnosticAid)
        );
        assertTrue(exception.getMessage().contains("tipo de especialista no debe especificarse"));
    }
}
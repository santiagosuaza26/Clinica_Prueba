package app.clinic.insurance.domain.service;

import java.time.LocalDate;

import app.clinic.insurance.domain.model.Insurance;

public class InsuranceValidator {

    public void validate(Insurance insurance) {
        if (insurance.getInsuranceCompany() == null || insurance.getInsuranceCompany().isBlank())
            throw new IllegalArgumentException("El nombre de la compañía de seguros es obligatorio.");

        if (insurance.getPolicyNumber() == null || insurance.getPolicyNumber().isBlank())
            throw new IllegalArgumentException("El número de póliza es obligatorio.");

        if (insurance.getStartDate() == null || insurance.getEndDate() == null)
            throw new IllegalArgumentException("Las fechas de inicio y fin son obligatorias.");

        if (insurance.getStartDate().isAfter(insurance.getEndDate()))
            throw new IllegalArgumentException("La fecha de inicio no puede ser posterior a la de fin.");

        if (insurance.getEndDate().isBefore(LocalDate.now()))
            insurance.updateStatusByDate(LocalDate.now());
    }
}

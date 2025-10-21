package app.clinic.inventory.domain.service;

import app.clinic.inventory.domain.model.*;
import java.util.List;
import java.util.stream.Collectors;

public class InventorySearchService {

    public List<Medication> filterMedicationsByName(List<Medication> meds, String name) {
        return meds.stream()
                .filter(m -> m.getName().toLowerCase().contains(name.toLowerCase()))
                .collect(Collectors.toList());
    }

    public List<Procedure> filterProceduresBySpecialist(List<Procedure> procedures, SpecialistType type) {
        return procedures.stream()
                .filter(p -> p.getSpecialistType() == type)
                .collect(Collectors.toList());
    }

    public List<DiagnosticAid> filterAidsBySpecialist(List<DiagnosticAid> aids, SpecialistType type) {
        return aids.stream()
                .filter(a -> a.getSpecialistType() == type)
                .collect(Collectors.toList());
    }
}

package app.clinic.medicalhistory.domain.model;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

public class MedicalHistory {

    private final String patientCedula;
    private final Map<String, MedicalVisit> visits;

    public MedicalHistory(String patientCedula) {
        this.patientCedula = patientCedula;
        this.visits = new LinkedHashMap<>();
    }

    public String getPatientCedula() {
        return patientCedula;
    }

    public Map<String, MedicalVisit> getVisits() {
        return visits;
    }

    public void addVisit(MedicalVisit visit) {
        if (visits.containsKey(visit.getDate())) {
            throw new IllegalArgumentException("Visit already exists for date: " + visit.getDate());
        }
        visits.put(visit.getDate(), visit);
    }

    public Optional<MedicalVisit> getVisitByDate(String date) {
        return Optional.ofNullable(visits.get(date));
    }
}

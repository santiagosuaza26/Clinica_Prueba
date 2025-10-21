package app.clinic.medicalhistory.infrastructure.document;

public class VitalSignsDocument {
    private double bloodPressure;
    private double temperature;
    private int pulse;
    private double oxygenLevel;

    public VitalSignsDocument() {}

    public VitalSignsDocument(double bloodPressure, double temperature, int pulse, double oxygenLevel) {
        this.bloodPressure = bloodPressure;
        this.temperature = temperature;
        this.pulse = pulse;
        this.oxygenLevel = oxygenLevel;
    }

    public double getBloodPressure() { return bloodPressure; }
    public double getTemperature() { return temperature; }
    public int getPulse() { return pulse; }
    public double getOxygenLevel() { return oxygenLevel; }

    public void setBloodPressure(double bloodPressure) { this.bloodPressure = bloodPressure; }
    public void setTemperature(double temperature) { this.temperature = temperature; }
    public void setPulse(int pulse) { this.pulse = pulse; }
    public void setOxygenLevel(double oxygenLevel) { this.oxygenLevel = oxygenLevel; }
}

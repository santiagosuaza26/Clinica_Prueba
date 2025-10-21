package app.clinic.medicalhistory.domain.model;

public class VitalSigns {
    private double bloodPressure;
    private double temperature;
    private int pulse;
    private double oxygenLevel;

    public VitalSigns(double bloodPressure, double temperature, int pulse, double oxygenLevel) {
        this.bloodPressure = bloodPressure;
        this.temperature = temperature;
        this.pulse = pulse;
        this.oxygenLevel = oxygenLevel;
    }

    public double getBloodPressure() { return bloodPressure; }
    public double getTemperature() { return temperature; }
    public int getPulse() { return pulse; }
    public double getOxygenLevel() { return oxygenLevel; }
}

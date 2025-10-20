package app.clinic.insurance.domain.model;

public class BillingDetail {

    private String description;   // Medicamento, procedimiento, ayuda diagnóstica
    private double cost;
    private String type;          // "MEDICATION", "PROCEDURE", "DIAGNOSTIC"

    public BillingDetail(String description, double cost, String type) {
        this.description = description;
        this.cost = cost;
        this.type = type;
    }

    public String getDescription() { return description; }
    public double getCost() { return cost; }
    public String getType() { return type; }
}

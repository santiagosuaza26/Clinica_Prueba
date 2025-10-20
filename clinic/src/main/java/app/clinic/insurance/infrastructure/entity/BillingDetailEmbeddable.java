package app.clinic.insurance.infrastructure.entity;

import jakarta.persistence.Embeddable;

@Embeddable
public class BillingDetailEmbeddable {

    private String description;
    private double cost;
    private String type;

    public BillingDetailEmbeddable() {}

    public BillingDetailEmbeddable(String description, double cost, String type) {
        this.description = description;
        this.cost = cost;
        this.type = type;
    }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public double getCost() { return cost; }
    public void setCost(double cost) { this.cost = cost; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
}

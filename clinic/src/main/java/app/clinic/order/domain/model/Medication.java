package app.clinic.order.domain.model;

/**
 * Representa un medicamento dentro del inventario o recetado en una orden.
 */
public class Medication {
    private final String id;
    private final String name;
    private final double cost;
    private final String dosage;
    private final int durationDays;

    public Medication(String id, String name, double cost, String dosage, int durationDays) {
        this.id = id;
        this.name = name;
        this.cost = cost;
        this.dosage = dosage;
        this.durationDays = durationDays;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public double getCost() { return cost; }
    public String getDosage() { return dosage; }
    public int getDurationDays() { return durationDays; }
}

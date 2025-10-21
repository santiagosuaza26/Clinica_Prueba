package app.clinic.order.domain.model;

/**
 * Representa una ayuda diagnóstica (examen, análisis, imagen).
 */
public class DiagnosticAid {
    private final String id;
    private final String name;
    private final int quantity;
    private final double cost;
    private final boolean requiresSpecialist;
    private final SpecialistType specialistType;

    public DiagnosticAid(String id, String name, int quantity, double cost,
                         boolean requiresSpecialist, SpecialistType specialistType) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
        this.cost = cost;
        this.requiresSpecialist = requiresSpecialist;
        this.specialistType = specialistType;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public int getQuantity() { return quantity; }
    public double getCost() { return cost; }
    public boolean isRequiresSpecialist() { return requiresSpecialist; }
    public SpecialistType getSpecialistType() { return specialistType; }

    public double calculateTotalCost() {
        return cost * quantity;
    }
}

package app.clinic.inventory.domain.model;

public class DiagnosticAid {
    private Long id;
    private String name;
    private int quantity;
    private double cost;
    private boolean requiresSpecialist;
    private SpecialistType specialistType;

    public DiagnosticAid(Long id, String name, int quantity, double cost,
                         boolean requiresSpecialist, SpecialistType specialistType) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
        this.cost = cost;
        this.requiresSpecialist = requiresSpecialist;
        this.specialistType = specialistType;
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public int getQuantity() { return quantity; }
    public double getCost() { return cost; }
    public boolean isRequiresSpecialist() { return requiresSpecialist; }
    public SpecialistType getSpecialistType() { return specialistType; }

    public void setId(Long id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    public void setCost(double cost) { this.cost = cost; }
    public void setRequiresSpecialist(boolean requiresSpecialist) { this.requiresSpecialist = requiresSpecialist; }
    public void setSpecialistType(SpecialistType specialistType) { this.specialistType = specialistType; }
}

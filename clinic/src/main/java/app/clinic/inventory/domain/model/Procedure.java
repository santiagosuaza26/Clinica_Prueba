package app.clinic.inventory.domain.model;

public class Procedure {
    private Long id;
    private String name;
    private int repetitions;
    private String frequency;
    private double cost;
    private boolean requiresSpecialist;
    private SpecialistType specialistType;

    public Procedure(Long id, String name, int repetitions, String frequency, double cost,
                     boolean requiresSpecialist, SpecialistType specialistType) {
        this.id = id;
        this.name = name;
        this.repetitions = repetitions;
        this.frequency = frequency;
        this.cost = cost;
        this.requiresSpecialist = requiresSpecialist;
        this.specialistType = specialistType;
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public int getRepetitions() { return repetitions; }
    public String getFrequency() { return frequency; }
    public double getCost() { return cost; }
    public boolean isRequiresSpecialist() { return requiresSpecialist; }
    public SpecialistType getSpecialistType() { return specialistType; }

    public void setId(Long id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setRepetitions(int repetitions) { this.repetitions = repetitions; }
    public void setFrequency(String frequency) { this.frequency = frequency; }
    public void setCost(double cost) { this.cost = cost; }
    public void setRequiresSpecialist(boolean requiresSpecialist) { this.requiresSpecialist = requiresSpecialist; }
    public void setSpecialistType(SpecialistType specialistType) { this.specialistType = specialistType; }
}

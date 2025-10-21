package app.clinic.order.domain.model;

/**
 * Representa un procedimiento médico registrado o solicitado.
 */
public class Procedure {
    private final String id;
    private final String name;
    private final int repetitions;
    private final String frequency;
    private final double cost;
    private final boolean requiresSpecialist;
    private final SpecialistType specialistType;

    public Procedure(String id, String name, int repetitions, String frequency,
                     double cost, boolean requiresSpecialist, SpecialistType specialistType) {
        this.id = id;
        this.name = name;
        this.repetitions = repetitions;
        this.frequency = frequency;
        this.cost = cost;
        this.requiresSpecialist = requiresSpecialist;
        this.specialistType = specialistType;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public int getRepetitions() { return repetitions; }
    public String getFrequency() { return frequency; }
    public double getCost() { return cost; }
    public boolean isRequiresSpecialist() { return requiresSpecialist; }
    public SpecialistType getSpecialistType() { return specialistType; }
}

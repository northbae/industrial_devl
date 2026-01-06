package staff;

import behaviors.Healable;

public interface MedicalExaminer {
    void examine(Healable animal);
    void treat(Healable animal);
    void prepareForExamination();
    String getExaminerName();
}

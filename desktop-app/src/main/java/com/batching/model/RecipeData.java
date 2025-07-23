package com.batching.model;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class RecipeData {

    private final SimpleIntegerProperty recipeId = new SimpleIntegerProperty();
    private final SimpleStringProperty recipeName = new SimpleStringProperty();
    private final SimpleStringProperty recipeTotal = new SimpleStringProperty();
    private final SimpleStringProperty timestamp = new SimpleStringProperty();

    private final SimpleStringProperty[] materials = new SimpleStringProperty[20];
    private final SimpleStringProperty[] setpoints = new SimpleStringProperty[20];

    public RecipeData() {
        for (int i = 0; i < 20; i++) {
            materials[i] = new SimpleStringProperty("");
            setpoints[i] = new SimpleStringProperty("");
        }
    }

    // --- Recipe ID ---
    public int getRecipeId() {
        return recipeId.get();
    }

    public void setRecipeId(int id) {
        this.recipeId.set(id);
    }

    public SimpleIntegerProperty recipeIdProperty() {
        return recipeId;
    }

    // --- Recipe Name ---
    public String getRecipeName() {
        return recipeName.get();
    }

    public void setRecipeName(String name) {
        this.recipeName.set(name);
    }

    public SimpleStringProperty recipeNameProperty() {
        return recipeName;
    }

    // --- Recipe Total ---
    public String getRecipeTotal() {
        return recipeTotal.get();
    }

    public void setRecipeTotal(String total) {
        this.recipeTotal.set(total);
    }

    public SimpleStringProperty recipeTotalProperty() {
        return recipeTotal;
    }

    // --- Timestamp ---
    public String getTimestamp() {
        return timestamp.get();
    }

    public void setTimestamp(String ts) {
        this.timestamp.set(ts);
    }

    public SimpleStringProperty timestampProperty() {
        return timestamp;
    }

    // --- Materials (1–20) ---
    public String getMaterial1() { return materials[0].get(); }
    public void setMaterial1(String v) { materials[0].set(v); }
    public SimpleStringProperty material1Property() { return materials[0]; }

    public String getMaterial2() { return materials[1].get(); }
    public void setMaterial2(String v) { materials[1].set(v); }
    public SimpleStringProperty material2Property() { return materials[1]; }

    public String getMaterial3() { return materials[2].get(); }
    public void setMaterial3(String v) { materials[2].set(v); }
    public SimpleStringProperty material3Property() { return materials[2]; }

    public String getMaterial4() { return materials[3].get(); }
    public void setMaterial4(String v) { materials[3].set(v); }
    public SimpleStringProperty material4Property() { return materials[3]; }

    public String getMaterial5() { return materials[4].get(); }
    public void setMaterial5(String v) { materials[4].set(v); }
    public SimpleStringProperty material5Property() { return materials[4]; }

    public String getMaterial6() { return materials[5].get(); }
    public void setMaterial6(String v) { materials[5].set(v); }
    public SimpleStringProperty material6Property() { return materials[5]; }

    public String getMaterial7() { return materials[6].get(); }
    public void setMaterial7(String v) { materials[6].set(v); }
    public SimpleStringProperty material7Property() { return materials[6]; }

    public String getMaterial8() { return materials[7].get(); }
    public void setMaterial8(String v) { materials[7].set(v); }
    public SimpleStringProperty material8Property() { return materials[7]; }

    public String getMaterial9() { return materials[8].get(); }
    public void setMaterial9(String v) { materials[8].set(v); }
    public SimpleStringProperty material9Property() { return materials[8]; }

    public String getMaterial10() { return materials[9].get(); }
    public void setMaterial10(String v) { materials[9].set(v); }
    public SimpleStringProperty material10Property() { return materials[9]; }

    public String getMaterial11() { return materials[10].get(); }
    public void setMaterial11(String v) { materials[10].set(v); }
    public SimpleStringProperty material11Property() { return materials[10]; }

    public String getMaterial12() { return materials[11].get(); }
    public void setMaterial12(String v) { materials[11].set(v); }
    public SimpleStringProperty material12Property() { return materials[11]; }

    public String getMaterial13() { return materials[12].get(); }
    public void setMaterial13(String v) { materials[12].set(v); }
    public SimpleStringProperty material13Property() { return materials[12]; }

    public String getMaterial14() { return materials[13].get(); }
    public void setMaterial14(String v) { materials[13].set(v); }
    public SimpleStringProperty material14Property() { return materials[13]; }

    public String getMaterial15() { return materials[14].get(); }
    public void setMaterial15(String v) { materials[14].set(v); }
    public SimpleStringProperty material15Property() { return materials[14]; }

    public String getMaterial16() { return materials[15].get(); }
    public void setMaterial16(String v) { materials[15].set(v); }
    public SimpleStringProperty material16Property() { return materials[15]; }

    public String getMaterial17() { return materials[16].get(); }
    public void setMaterial17(String v) { materials[16].set(v); }
    public SimpleStringProperty material17Property() { return materials[16]; }

    public String getMaterial18() { return materials[17].get(); }
    public void setMaterial18(String v) { materials[17].set(v); }
    public SimpleStringProperty material18Property() { return materials[17]; }

    public String getMaterial19() { return materials[18].get(); }
    public void setMaterial19(String v) { materials[18].set(v); }
    public SimpleStringProperty material19Property() { return materials[18]; }

    public String getMaterial20() { return materials[19].get(); }
    public void setMaterial20(String v) { materials[19].set(v); }
    public SimpleStringProperty material20Property() { return materials[19]; }

    // --- Setpoints (1–20) ---
    public String getSetpoint1() { return setpoints[0].get(); }
    public void setSetpoint1(String v) { setpoints[0].set(v); }
    public SimpleStringProperty setpoint1Property() { return setpoints[0]; }

    public String getSetpoint2() { return setpoints[1].get(); }
    public void setSetpoint2(String v) { setpoints[1].set(v); }
    public SimpleStringProperty setpoint2Property() { return setpoints[1]; }

    public String getSetpoint3() { return setpoints[2].get(); }
    public void setSetpoint3(String v) { setpoints[2].set(v); }
    public SimpleStringProperty setpoint3Property() { return setpoints[2]; }

    public String getSetpoint4() { return setpoints[3].get(); }
    public void setSetpoint4(String v) { setpoints[3].set(v); }
    public SimpleStringProperty setpoint4Property() { return setpoints[3]; }

    public String getSetpoint5() { return setpoints[4].get(); }
    public void setSetpoint5(String v) { setpoints[4].set(v); }
    public SimpleStringProperty setpoint5Property() { return setpoints[4]; }

    public String getSetpoint6() { return setpoints[5].get(); }
    public void setSetpoint6(String v) { setpoints[5].set(v); }
    public SimpleStringProperty setpoint6Property() { return setpoints[5]; }

    public String getSetpoint7() { return setpoints[6].get(); }
    public void setSetpoint7(String v) { setpoints[6].set(v); }
    public SimpleStringProperty setpoint7Property() { return setpoints[6]; }

    public String getSetpoint8() { return setpoints[7].get(); }
    public void setSetpoint8(String v) { setpoints[7].set(v); }
    public SimpleStringProperty setpoint8Property() { return setpoints[7]; }

    public String getSetpoint9() { return setpoints[8].get(); }
    public void setSetpoint9(String v) { setpoints[8].set(v); }
    public SimpleStringProperty setpoint9Property() { return setpoints[8]; }

    public String getSetpoint10() { return setpoints[9].get(); }
    public void setSetpoint10(String v) { setpoints[9].set(v); }
    public SimpleStringProperty setpoint10Property() { return setpoints[9]; }

    public String getSetpoint11() { return setpoints[10].get(); }
    public void setSetpoint11(String v) { setpoints[10].set(v); }
    public SimpleStringProperty setpoint11Property() { return setpoints[10]; }

    public String getSetpoint12() { return setpoints[11].get(); }
    public void setSetpoint12(String v) { setpoints[11].set(v); }
    public SimpleStringProperty setpoint12Property() { return setpoints[11]; }

    public String getSetpoint13() { return setpoints[12].get(); }
    public void setSetpoint13(String v) { setpoints[12].set(v); }
    public SimpleStringProperty setpoint13Property() { return setpoints[12]; }

    public String getSetpoint14() { return setpoints[13].get(); }
    public void setSetpoint14(String v) { setpoints[13].set(v); }
    public SimpleStringProperty setpoint14Property() { return setpoints[13]; }

    public String getSetpoint15() { return setpoints[14].get(); }
    public void setSetpoint15(String v) { setpoints[14].set(v); }
    public SimpleStringProperty setpoint15Property() { return setpoints[14]; }

    public String getSetpoint16() { return setpoints[15].get(); }
    public void setSetpoint16(String v) { setpoints[15].set(v); }
    public SimpleStringProperty setpoint16Property() { return setpoints[15]; }

    public String getSetpoint17() { return setpoints[16].get(); }
    public void setSetpoint17(String v) { setpoints[16].set(v); }
    public SimpleStringProperty setpoint17Property() { return setpoints[16]; }

    public String getSetpoint18() { return setpoints[17].get(); }
    public void setSetpoint18(String v) { setpoints[17].set(v); }
    public SimpleStringProperty setpoint18Property() { return setpoints[17]; }

    public String getSetpoint19() { return setpoints[18].get(); }
    public void setSetpoint19(String v) { setpoints[18].set(v); }
    public SimpleStringProperty setpoint19Property() { return setpoints[18]; }

    public String getSetpoint20() { return setpoints[19].get(); }
    public void setSetpoint20(String v) { setpoints[19].set(v); }
    public SimpleStringProperty setpoint20Property() { return setpoints[19]; }
}

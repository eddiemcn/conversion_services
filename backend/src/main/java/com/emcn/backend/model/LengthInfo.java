package com.emcn.backend.model;

public class LengthInfo {
  private String formula;
  private String displayFormula;

  public LengthInfo(String formula, String displayFormula) {
    this.formula = formula;
    this.displayFormula = displayFormula;
  }

  public String getDisplayFormula() {
    return displayFormula;
  }

  public String getFormula() {
    return formula;
  }
}
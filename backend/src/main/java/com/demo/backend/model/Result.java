package com.demo.backend.model;

public class Result {
  private final String value;
  private final String formula;

  public Result(String value, String formula) {
    this.value = value;
    this.formula = formula;
  }
  
  public String getValue() {
    return value;
  }

  public String getFormula() {
    return formula;
  }
}
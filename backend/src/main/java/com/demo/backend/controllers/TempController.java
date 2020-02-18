package com.demo.backend.controllers;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.demo.backend.util.NumberUtils;
import com.demo.backend.model.Result;

@RestController
@CrossOrigin
@RequestMapping("/temp")
public class TempController {
  final String DEGREE_CELSIUS = "&#x2103";
  final String DEGREE_FAHRENHEIT = "&#x2109";
  final String DEGREE_KELVIN = "&#x212A";
  final String CF_FORMULA = "(%s<strong>"+DEGREE_CELSIUS+"</strong> x 9/5) + 32 = %s<strong>"+DEGREE_FAHRENHEIT+"</strong>";
  final String CK_FORMULA = "%s<strong>"+DEGREE_CELSIUS+"</strong> + 273.15 = %s<strong>"+DEGREE_KELVIN+"</strong>";
  final String FC_FORMULA = "(%s<strong>"+DEGREE_FAHRENHEIT+"</strong> - 32) x 5/9 = %s<strong>"+DEGREE_CELSIUS+"</strong>";
  final String FK_FORMULA = "(%s<strong>"+DEGREE_FAHRENHEIT+"</strong> - 32) x 5/9 + 273.15 = %s<strong>"+DEGREE_KELVIN+"</strong>";
  final String KC_FORMULA = "%s<strong>"+DEGREE_KELVIN+"</strong> - 273.15 = %s<strong>"+DEGREE_CELSIUS+"</strong>";
  final String KF_FORMULA = "(%s<strong>"+DEGREE_KELVIN+"</strong> - 273.15) x 9/5 = %s<strong>"+DEGREE_FAHRENHEIT+"</strong>";

  @GetMapping("/*")
  public String home() {
      return "Use /{fromScale}/{toScale}/{valueToConvert}";
  }

  @RequestMapping(value="/{fromScale}/{toScale}/{valueToConvert}", method=RequestMethod.GET)
  public Result getCentigrade(
    @PathVariable("fromScale") String fromScale,
    @PathVariable("toScale") String toScale,
    @PathVariable("valueToConvert") String valueToConvert
  ) {
    NumberUtils nu = new NumberUtils();
    float f = nu.stringToFloat(valueToConvert);

    String output;
    String format = "";
    String formula = "";

    boolean success = true;
    // calculate formula result and format formula string with input and result
    String conversion = fromScale + toScale.toUpperCase();
    switch (conversion) {
      case "CF":
        output = nu.floatToString((f * 9.0f / 5.0f) + 32.0f);
        format = CF_FORMULA;
        break;
      case "CK":
        output = nu.floatToString(f + 273.15f);
        format = CK_FORMULA;
        break;
      case "FC":
        output = nu.floatToString((f -32.0f) * 5.0f / 9.0f);
        format = FC_FORMULA;
        break;
      case "FK":
        output = nu.floatToString((f - 32.0f) * 5/9 + 273.15f);
        format = FK_FORMULA;
        break;
      case "KC":
        output = nu.floatToString(f - 273.15f);
        format = KC_FORMULA;
        break;
      case "KF":
          output = nu.floatToString(f - 273.15f / 9/5);
          format = KF_FORMULA;
          break;
      default:
        success = false;
        output = "-1";
        formula = String.format("ERROR: unrecognise function requested: '%s' with parameter '%s'.", conversion, f);
        break;
    }

    if (success) {
      formula = String.format(format, valueToConvert, output);
    }

    return new Result(output, formula);
  }
}
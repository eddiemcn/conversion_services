package com.emcn.backend.controllers;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.expression.EvaluationException;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.ParseException;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.emcn.backend.model.LengthInfo;
import com.emcn.backend.model.Result;
import com.emcn.backend.util.NumberUtils;

@RestController
@CrossOrigin
@RequestMapping("length")
public class LengthController {
  Logger logger = LoggerFactory.getLogger(LengthController.class);
  NumberUtils NU = new NumberUtils();

  private static final Map<String, LengthInfo> conversions = new HashMap<String, LengthInfo>();
  static {
    /*
      { value: 'mm', text: 'Millimetre' },
      { value: 'cm', text: 'Centimetre' },
      { value: 'm', text: 'Metre' },
      { value: 'km', text: 'Kilometre' },
      { value: 'in', text: 'Inch' },
      { value: 'ft', text: 'Foot' },
      { value: 'yd', text: 'Yard' },
      { value: 'M', text: 'Mile' }
    */
    // millimetre to...
    conversions.put("mmcm", new LengthInfo("%s / 10f", "%smm / 10 = %scm"));
    conversions.put("mmmt", new LengthInfo("%s / 100f", "%smm / 1000 = %sm"));
    conversions.put("mmkm", new LengthInfo("%s / 1e6f", "%smm / 1e6 = %skm"));
    conversions.put("mmin", new LengthInfo("%s / 5.4f", "%smm / 5.4 = %sin (approx.)"));
    conversions.put("mmft", new LengthInfo("%s / 305f", "%smm / 305 = %sft (approx.)"));
    conversions.put("mmyd", new LengthInfo("%s / 914f", "%smm / 914 = %syd (approx.)"));
    conversions.put("mmM", new LengthInfo("%s / 1.609e+6f", "%smm / 1.609e+6 = %sM (approx.)"));
    // centimetre to...
    conversions.put("cmmm", new LengthInfo("%s * 10f", "%scm X 10 = %smm"));
    conversions.put("cmmt", new LengthInfo("%s / 100f", "%scm / 100 = %sm"));
    conversions.put("cmkm", new LengthInfo("%s / 100000f", "%scm / 100000 = %skm"));
    conversions.put("cmin", new LengthInfo("%s / 2.54f", "%scm / 2.54 = %sin (approx.)"));
    conversions.put("cmft", new LengthInfo("%s / 30.48f", "%scm / 30.48 = %sft (approx.)"));
    conversions.put("cmyd", new LengthInfo("%s / 91.4f", "%scm / 91.4 = %syd (approx.)"));
    conversions.put("cmM", new LengthInfo("%s / 160934f", "%scm / 160934 = %sM (approx.)"));
    // metre to...
    conversions.put("mtmm", new LengthInfo("%s * 1000f", "%sm X 1000 = %smm"));
    conversions.put("mtcm", new LengthInfo("%s / 10f", "%sm / 10 = %scm"));
    conversions.put("mtkm", new LengthInfo("%s / 1000f", "%sm / 1000 = %skm"));
    conversions.put("mtin", new LengthInfo("%s * 39.37f", "%sm X 39.37 = %sin (approx.)"));
    conversions.put("mtft", new LengthInfo("%s * 3.281f", "%sm X 3.281 = %sft (approx.)"));
    conversions.put("mtyd", new LengthInfo("%s * 1.094f", "%sm X 1.094 = %syd (approx.)"));
    conversions.put("mtM", new LengthInfo("%s / 1609f", "%sm / 1609 = %sM (approx.)"));
    // kilometre to...
    conversions.put("kmmm", new LengthInfo("%s * 1e6f", "%skm X 1e6 = %smm"));
    conversions.put("kmcm", new LengthInfo("%s * 100000f", "%skm X 100000 = %scm"));
    conversions.put("kmmt", new LengthInfo("%s * 1000f", "%skm * 1000 = %sm"));
    conversions.put("kmin", new LengthInfo("%s * 39370f", "%skm X 39370 = %sin (approx.)"));
    conversions.put("kmft", new LengthInfo("%s * 3281f", "%skm X 3281 = %sft (approx.)"));
    conversions.put("kmyd", new LengthInfo("%s * 1094f", "%skm X 1094 = %syd (approx.)"));
    conversions.put("kmM", new LengthInfo("%s / 1.609f", "%skm X 1.609 = %sM (approx.)"));
    // inch to...
    conversions.put("inmm", new LengthInfo("%s * 25.4f", "%sin X 25.4 = %smm (approx.)"));
    conversions.put("incm", new LengthInfo("%s * 2.54f", "%sin X 2.54 = %scm (approx.)"));
    conversions.put("inmt", new LengthInfo("%s / 39.37f", "%sin / 39.37 = %sm (approx.)"));
    conversions.put("inkm", new LengthInfo("%s / 39370f", "%sin / 39370 = *skm (approx.)"));
    conversions.put("inft", new LengthInfo("%s / 12f", "%sin / 12 = %sft"));
    conversions.put("inyd", new LengthInfo("%s / 36f", "%sin / 36 = %syd"));
    conversions.put("inM", new LengthInfo("%s / 63360f", "%sin / 63360 = %sM"));
    // yard to...
    conversions.put("ydmm", new LengthInfo("%s * 914.4f", "%syd X 914.4 = %smm (approx.)"));
    conversions.put("ydcm", new LengthInfo("%s * 91.44f", "%syd X 91.44 = %scm (approx.)"));
    conversions.put("ydmt", new LengthInfo("%s * 0.9144f", "%syd X 0.9144 = %sm (approx.)"));
    conversions.put("ydkm", new LengthInfo("%s * 0.0009144f", "%syd X 0.9144 = %skm (approx.)"));
    conversions.put("ydin", new LengthInfo("%s * 36f", "%syd * 36 = %sin"));
    conversions.put("ydft", new LengthInfo("%s * 3f", "%syd * 3 = %sft"));
    conversions.put("ydM", new LengthInfo("%s * 0.00056818182f", "%syd X 0.00056818182 = %sM"));
    // Mile to...
    conversions.put("mtmm", new LengthInfo("%s * 1000f", "%sm X 1000 = %smm"));
    conversions.put("mtcm", new LengthInfo("%s * 100f", "%sm X 100 = %scm"));
    conversions.put("mtkm", new LengthInfo("%s / 1000f", "%sm / 1000 = %skm"));
    conversions.put("mtin", new LengthInfo("%s * 39.37f", "%sm X 39.37 = %sin"));
    conversions.put("mtft", new LengthInfo("%s * 3.281f", "%sm X 3.281 = %sft"));
    conversions.put("mtyd", new LengthInfo("%s * 1.094f", "%sm X 1.094 = %syd"));
    conversions.put("mtM", new LengthInfo("%s / 1609f", "%sm / 1609 = %sM"));

  }

  @GetMapping("/*")
  public String home() {
      return "Use /{fromScale}/{toScale}/{valueToConvert}";
  }

  @RequestMapping(value="/{fromScale}/{toScale}/{valueToConvert}", method=RequestMethod.GET)
  public Result convert(
    @PathVariable("fromScale") String fromScale,
    @PathVariable("toScale") String toScale,
    @PathVariable("valueToConvert") String valueToConvert
  ) {
    logger.info(String.format("Inputs: from, to, value: %s, %s, %s.", fromScale, toScale, valueToConvert));
    String displayFormula = String.format("ERROR: Unknown conversion: %s to %s with %s", fromScale, toScale, valueToConvert);
    String output = "";

    boolean success = true;
    // calculate formula result and format formula string with input and result
    LengthInfo li = (LengthInfo)conversions.get(fromScale + toScale);
    String calculation = String.format(li.getFormula(), valueToConvert);
    ExpressionParser ep = new SpelExpressionParser();
    try {
      Expression e = ep.parseExpression(calculation);
      output = NU.floatToString((Float)e.getValue());
      displayFormula = li.getDisplayFormula();
    } catch (ParseException pe) {
      success = false;
    } catch (EvaluationException ee) {
      success = false;
    }

    String formula;
    if (success) {
      logger.info(String.format("FORMULA: '%s' with output '%s'.", displayFormula, output));
      formula = String.format(displayFormula, valueToConvert, output);
    } else {
      formula = displayFormula;
    }

    return new Result(output, formula);  
  }
}
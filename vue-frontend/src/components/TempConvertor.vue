<template>
  <div class="temperature">
    <b-container>
      <b-row>
        <b-col cols="5">
          <b-form-input id="value1" v-model="value1" step=".01" @input.native="valueChanged"></b-form-input>
        </b-col>
        <b-col cols="2">=</b-col>
        <b-col cols="5">
          <b-form-input id="value2" v-model="value2" step=".01" @input.native="valueChanged"></b-form-input>
        </b-col>
      </b-row>
      <b-row>
          <b-col cols="5">
            <div class="select-scale">
              <b-form-select id="scale1" v-model="scale1" :options="scale_options" @input.native="scaleChanged"></b-form-select>
            </div>
          </b-col>
          <b-col cols="2"></b-col>
          <b-col cols="5">
            <div class="select-scale">
              <b-form-select id="scale2" v-model="scale2" :options="scale_options" @input.native="scaleChanged"></b-form-select>
            </div>
          </b-col>
      </b-row>
      <b-row class="pt-4">
        <b-col cols="1"></b-col>
        <b-col cols="2" class="formula">
          <p class="">Formula</p>
        </b-col>
        <b-col cols="9">
          <p v-html="formula" class="text-left"></p>
        </b-col>
      </b-row>
    </b-container>
  </div>
</template>
<script>
export default {
  name: 'temp-convertor',
  data () {
    return {
      scale_options: [
        { value: 'C', text: 'Celsius' },
        { value: 'F', text: 'Fahrenheit' },
        { value: 'K', text: 'Kelvin' }
      ],
      scale1: 'C',
      scale2: 'F',
      value1: '0',
      value2: '32',
      formula: '',
      rootURL: 'http://localhost:8080/api/temp/'
    }
  },
  methods: {
    convert: function (fromScale, toScale, valueToConvert) {
      console.log('convert fromScale, toScale, value=' + fromScale + ', ' + toScale + ', ' + valueToConvert)

      var targetURL = this.rootURL + fromScale + '/' + toScale + '/' + valueToConvert
      console.log('Target is ' + targetURL)

      return new Promise(function (resolve, reject) {
        fetch(targetURL)
          .then(response => response.json()
          )
          .then(data => {
            resolve({ success: true, data: data })
          })
      })
    },
    scaleChanged: function (event) {
      console.log('scale1Value =' + this.value1)
      console.log('scale2Value =' + this.value2)

      if (event) {
        var inputScale
        var outputScale
        if (event.target.id === 'scale1') {
          if (event.target.value === this.scale2) {
            console.log('setting scale2 to ' + this.scale1)
            this.scale2 = this.scale1
          }

          inputScale = event.target.value
          outputScale = this.scale2
        } else {
          if (event.target.value === this.scale1) {
            console.log('setting scale1 to ' + this.scale2)
            this.scale1 = this.scale2
          }

          inputScale = this.scale1
          outputScale = event.target.value
        }

        this.convert(inputScale, outputScale, this.value1)
          .then(result => {
            if (result.success) {
              console.log(result)
              this.value2 = result.data.value
              this.formula = result.data.formula
            }
          })
      }
    },
    valueChanged: function (event) {
      if (event) {
        if (event.target.id === 'value1') {
          this.convert(this.scale1, this.scale2, this.value1)
            .then(result => {
              console.log(result)
              this.value2 = result.data.value
              this.formula = result.data.formula
            })
        } else {
          this.convert(this.scale1, this.scale2, this.value2)
            .then(result => {
              console.log(result)
              this.value1 = result.data.value
              this.formula = result.data.formula
            })
        }
      }
    },
    init: function () {
      this.convert(this.scale1, this.scale2, this.value1)
        .then(result => {
          console.log(result)
          this.value2 = result.data.value
          this.formula = result.data.formula
        })
    }
  },
  mounted () {
    // initialise the display
    this.init()
  }
}
</script>

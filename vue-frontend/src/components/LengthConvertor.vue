<template>
  <div class="temperature">
    <b-container>
      <b-row>
        <b-col cols="5">
          <b-form-input v-model="scale1Value" step=".01" @change="value1Changed"></b-form-input>
        </b-col>
        <b-col cols="2">=</b-col>
        <b-col cols="5">
          <b-form-input v-model="scale2Value" step=".01" @change="value2Changed"></b-form-input>
        </b-col>
      </b-row>
      <b-row>
          <b-col cols="5">
            <div class="select-scale">
              <b-form-select v-model="scale1" :options="scale_options" @change="scale1Changed"></b-form-select>
            </div>
          </b-col>
          <b-col cols="2"></b-col>
          <b-col cols="5">
            <div class="select-scale">
              <b-form-select v-model="scale2" :options="scale_options" @change="scale2Changed"></b-form-select>
            </div>
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
      scale1: 'C',
      scale2: 'F',
      scale_options: [
        { value: 'C', text: 'Celsius' },
        { value: 'F', text: 'Fahrenheit' },
        { value: 'K', text: 'Kelvin' }
      ],
      scale1Value: '0',
      scale2Value: '32',
      rootURL: 'http://localhost:8080/'
    }
  },
  methods: {
    convert: function (fromScale, toScale, value, deflt) {
      console.log('convert fromScale, toScale, value=' + fromScale + ', ' + toScale + ', ' + value)
      var urlEnd
      if (fromScale === toScale) {
        return deflt
      }

      if (fromScale === 'C' && toScale === 'F') {
        urlEnd = 'toF/'
      } else if (fromScale === 'F' && toScale === 'C') {
        urlEnd = 'toC/'
      }

      var targetURL = this.rootURL + urlEnd + value
      console.log('Target is ' + targetURL)
      fetch(targetURL)
        .then(response => {
          return response.text()
        }) 
    },
    scale1Changed: function (arg) {
      console.log('Scale1Changed arg=' + arg)
      console.log('scale1Value =' + this.scale1Value)
      console.log('scale2Value =' + this.scale2Value)

      this.scale2Value = this.convert(this.scale1, this.scale2, this.scale1Value, this.scale2Value)
    },
    scale2Changed: function (arg) {
      console.log('Scale1Changed arg=' + arg)
      console.log('scale1Value =' + this.scale1Value)
      console.log('scale2Value =' + this.scale2Value)

      this.scale1Value = this.convert(this.scale2, this.scale1, this.scale2Value, this.scale1Value)
    },
    value1Changed: function (arg) {
      this.scale1Changed(arg)
    },
    value2Changed: function (arg) {
      this.scale2Changed(arg)
    }
  }
}
  </script>

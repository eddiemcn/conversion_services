<template>
  <div class="home">
    <b-container>
      <b-row>
        <b-col>
          <b-form-select v-model="selected" :options="options"></b-form-select>
        </b-col>
      </b-row>
    </b-container>
    <component :is="currentConvertor"></component>
  </div>
</template>

<script>
// @ is an alias to /src
import lengthConvertor from '@/components/LengthConvertor.vue'
import tempConvertor from '@/components/TempConvertor.vue'

export default {
  name: 'home',
  components: {
    lengthConvertor,
    tempConvertor
  },
  data () {
    return {
      selected: 'temp',
      options: [
        { value: 'temp', text: 'Temperature' },
        { value: 'length', text: 'Length' }
      ],
      result: '',
      valueToConvert: '',
      rootURL: 'http://localhost:8080/'
    }
  },
  computed: {
    currentConvertor: function () {
      return this.selected + '-convertor'
    }
  }
}
</script>

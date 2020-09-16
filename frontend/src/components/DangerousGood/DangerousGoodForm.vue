<template>
    <div class="row p-3">
        <div class="col-6 col-sm-4 col-lg-3 col-xl px-1">
            <h5 class="mt-4 mt-lg-3 mt-xl-0">UN NUMBER</h5>
            <b-form-input type="number" min="1" v-model="unNumber" @change="validateDangerousGood"></b-form-input>
        </div>
        <div class="col-6 col-sm-4 col-lg-3 col-xl px-1">
            <h5 class="mt-4 mt-lg-3 mt-xl-0">SHIPPING NAME</h5>
            <b-form-input type="text" :disabled=true v-model="shippingName"></b-form-input>
        </div>
        <div class="col-6 col-sm-4 col-lg-3 col-xl px-1">
            <h5 class="mt-4 mt-lg-3 mt-xl-0">COMMON NAME</h5>
            <b-form-input type="text" v-model="commonName" @change="validateDangerousGood"></b-form-input>
        </div>
        <div class="col-6 col-sm-4 col-lg-3 col-xl px-1">
            <h5 class="mt-4 mt-lg-3 mt-xl-0">HAZARD CLASS</h5>
            <b-form-input type="text" :disabled=true v-model="hazardClass"></b-form-input>
        </div>
        <div class="col-6 col-sm-5 col-lg-3 col-xl px-1">
            <h5 class="mt-4 mt-lg-3 mt-xl-0">PACKAGING GROUP</h5>
            <b-form-input type="text" v-model="typeOfPackaging" @change="validateDangerousGood"></b-form-input>
        </div>
        <div class="col-6 col-sm-3 col-lg-3 col-xl px-1">
            <h5 class="mt-4 mt-lg-3 mt-xl-0">SIZE</h5>
            <b-input-group append="kg">
                <b-form-input type="number" v-model="weight" @change="validateDangerousGood"></b-form-input>
            </b-input-group>
        </div>
        <div class="col-6 col-sm-4 col-lg-3 col-xl px-1">
            <h5 class="mt-4 mt-lg-3 mt-xl-0">SUB RISK</h5>
            <b-form-input type="text" v-model="subRisk" @change="validateDangerousGood"></b-form-input>
        </div>
        <div class="px-2 pt-3">
            <b-button class="delete-btn" v-on:click="deleteDangerousGood">
                <i class="fas fa-trash-alt fa-lg mt-2"/>
            </b-button>
        </div>
    </div>
</template>

<script lang="ts">
import { Component, Prop, Vue } from 'vue-property-decorator'
import { DangerousGood } from '@/model/DangerousGood'

@Component
export default class DangerousGoodForm extends Vue {
    @Prop({ required: true }) dangerousGood: DangerousGood
    unNumber: number = null
    hazardClass: string = null
    subRisk: string = null
    typeOfPackaging: string = null
    weight: number = null
    shippingName: string = null
    commonName: string = null

    deleteDangerousGood () {
      this.$emit('delete-dangerous-good', this.dangerousGood)
    }

    validateDangerousGood () {
      if (
        !this.unNumber ||
        // !this.hazardClass ||
        !this.subRisk ||
        !this.typeOfPackaging ||
        !this.weight ||
        // !this.shippingName ||
        !this.commonName
      ) {
        return false
      }
      this.emitDangerousGood()
    }

    emitDangerousGood () {
      this.$emit('emitted-dangerous-good', this.getDangerousGood(), this.dangerousGood)
    }

    getDangerousGood () {
      return new DangerousGood(
        this.dangerousGood.id,
        null,
        this.unNumber,
        this.hazardClass,
        this.subRisk,
        this.typeOfPackaging,
        this.weight,
        this.shippingName,
        this.commonName
      )
    }
}
</script>

<style scoped lang="scss" src="./DangerousGoodForm.scss"></style>

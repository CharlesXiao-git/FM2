<template>
    <!--Added dangerous-good-form-container so the styling doesnt clash with ItemForm.scss-->
    <div class="dangerous-good-form-container">
        <div class="form-control-box">
            <div class="row px-3 pt-3 pb-0">
                <h3>HAZARDOUS ITEM DETAILS</h3>
            </div>
            <div v-for="dangerousGood in dangerousGoods" :key="dangerousGood.id">
                <DangerousGoodForm :dangerous-good="dangerousGood" @delete-dangerous-good="deleteDangerousGood" @emitted-dangerous-good="emittedDangerousGood"></DangerousGoodForm>
            </div>
            <!--Buttons to add dg-->
            <b-button class="primary-button m-3" v-on:click="addDangerousGood"><i class="fas mr-2 fa-plus"/>Add</b-button>
        </div>
    </div>
</template>

<script lang="ts">
import { Component, Vue, Watch } from 'vue-property-decorator'
import DangerousGoodForm from '@/components/DangerousGood/DangerousGoodForm.vue'
import { DangerousGood } from '@/model/DangerousGood'

@Component({
  components: { DangerousGoodForm }
})
export default class DangerousGoodPanel extends Vue {
  id = 0;
  dangerousGoods: DangerousGood[] = [new DangerousGood(this.id)]

  addDangerousGood () {
    if (this.dangerousGoods.length !== 0) {
      this.id = this.dangerousGoods[this.dangerousGoods.length - 1].id
    }
    this.dangerousGoods.push(new DangerousGood(this.id + 1))
  }

  deleteDangerousGood (dangerousGood: DangerousGood) {
    this.dangerousGoods.splice(this.dangerousGoods.indexOf(dangerousGood), 1)
    this.$forceUpdate()
  }

  emittedDangerousGood (dangerousGood: DangerousGood, oldDangerousGood: DangerousGood) {
    if (this.dangerousGoods.indexOf(oldDangerousGood) !== -1) {
      this.dangerousGoods[this.dangerousGoods.indexOf(oldDangerousGood)] = dangerousGood
    }

    this.$forceUpdate()
  }

  @Watch('dangerousGoods', { immediate: true, deep: true })
  onChangeDangerousGoods () {
    this.$emit('emitted-dangerous-goods', this.dangerousGoods)
  }
}
</script>

<style scoped lang="scss" src="./DangerousGoodForm.scss"></style>

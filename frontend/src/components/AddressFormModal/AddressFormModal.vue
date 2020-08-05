<template>
    <b-modal @shown="populateAddress" :id="modalId" ref="modal" :title="headerTitle" size="lg" @ok="handleSubmit" @cancel="resetModal" @hidden="resetModal">
        <form ref="form">
            <div>
                <div class="form-group row">
                    <label class="col-lg-3 col-form-label form-control-label">{{ idLabel }}</label>
                    <div class="col-lg-9">
                        <b-form-input v-model="referenceId" class="form-control" type="text" placeholder="Optional" />
                    </div>
                </div>
                <div class="form-group row">
                    <label class="col-lg-3 col-form-label form-control-label">Company Name</label>
                    <div class="col-lg-9">
                        <b-form-input required v-model="companyName" class="form-control" :state="validateCompanyNameFlag" />
                        <b-form-invalid-feedback>Must enter a valid company</b-form-invalid-feedback>
                    </div>
                </div>
                <div class="form-group row">
                    <label class="col-lg-3 col-form-label form-control-label">Contact Name</label>
                    <div class="col-lg-9">
                        <b-form-input required v-model="contactName" class="form-control" :state="validateContactNameFlag" />
                        <b-form-invalid-feedback>Must enter a valid contact</b-form-invalid-feedback>
                    </div>
                </div>
                <div class="form-group row">
                    <label class="col-lg-3 col-form-label form-control-label">Contact Number</label>
                    <div class="col-lg-9">
                        <b-form-input v-model="contactNumber" class="form-control" placeholder="Optional" />
                    </div>
                </div>
                <div class="form-group row">
                    <label class="col-lg-3 col-form-label form-control-label">Contact Email</label>
                    <div class="col-lg-9">
                        <b-form-input v-model="contactEmail" class="form-control" type="email" :state="validateContactEmailFlag" placeholder="Optional" />
                        <b-form-invalid-feedback>Must enter a valid email</b-form-invalid-feedback>
                    </div>
                </div>
                <div class="form-group row">
                    <label class="col-lg-3 col-form-label form-control-label">Address Line 1</label>
                    <div class="col-lg-9">
                        <b-form-input required v-model="addressLine1" class="form-control" type="text" :state="validateAddressLineFlag" />
                        <b-form-invalid-feedback>Address must be a <b>number</b> followed by the <b>Street name</b> and <b>type</b><br>E.g. 32 Example St</b-form-invalid-feedback>
                    </div>
                </div>
                <div class="form-group row">
                    <label class="col-lg-3 col-form-label form-control-label">Address Line 2</label>
                    <div class="col-lg-9">
                        <b-form-input v-model="addressLine2" class="form-control" type="text" placeholder="Optional" />
                    </div>
                </div>
                <div class="form-group row">
                    <label class="col-lg-3 col-form-label form-control-label">Suburb / Postcode</label>
                    <div class="col-lg-9">
                        <cool-select
                                v-model="selectedLocation"
                                :items="items"
                                item-text="location"
                                disable-filtering-by-search
                                @search="onSearch">
                            <template #no-data>
                                {{ noData ? "No address found." : "We need at least 3 letters / numbers to search" }}
                            </template>
                            <template #item="{ item }">
                                <div class="item">
                                    {{ item.location }} , {{ item.state }} , {{ item.postcode }}
                                </div>
                            </template>
                            <template slot="selection" slot-scope="{ item }">
                                <div class="item">
                                    {{ item.location }} , {{ item.state }} , {{ item.postcode }}
                                </div>
                            </template>
                        </cool-select>
                        <p class="helper-text" v-if="showHelperText">Previous selection : {{ address.town }} , {{ address.state }}, {{ address.postcode }} </p>
                        <span class="error" v-if="validateLocationFlag">Please make a valid selection</span>
                    </div>
                </div>
                <div class="form-group row">
                    <label class="col-lg-3 col-form-label form-control-label">Special Instructions</label>
                    <div class="col-lg-9">
                        <b-form-input v-model="specialInstructions" class="form-control" type="text" placeholder="Optional"/>
                    </div>
                </div>
            </div>
        </form>
        <template v-slot:modal-footer="{ ok, cancel }">
            <b-button class="primary-button" type="submit" @click="ok()">{{ buttonName }}</b-button>
            <b-button class="secondary-button" @click="cancel()">Close</b-button>
        </template>
    </b-modal>
</template>

<script lang="ts">
import { Component, Prop, Vue } from 'vue-property-decorator'
import { BvModalEvent } from 'bootstrap-vue'
import { CoolSelect } from 'vue-cool-select'
import { getAuthenticatedToken } from '@/service/AuthService'
import { Address } from '@/model/Address'
import { validateEmailField, validateStringField } from '@/helpers/Validator'

@Component({
  components: {
    CoolSelect
  }
})
export default class AddressFormModal extends Vue {
    @Prop({ required: true }) modalId: string
    @Prop({ required: true }) headerTitle: string
    @Prop({ required: true }) idLabel: string
    @Prop({ required: true }) buttonName: string
    @Prop() address: Address

    referenceId: string = null
    companyName: string = null
    addressLine1: string = null
    addressLine2: string = null
    contactName: string = null
    contactNumber: string = null
    contactEmail: string = null
    specialInstructions: string = null

    validateCompanyNameFlag: boolean = null
    validateContactNameFlag: boolean = null
    validateContactEmailFlag: boolean = null
    validateAddressLineFlag: boolean = null
    validateLocationFlag: boolean = null

    items: Array<object> = []
    selectedLocation = {
      location: null as string,
      state: null as string,
      postcode: null as number
    }

    showHelperText = false

    populateAddress () {
      if (this.address) {
        this.referenceId = this.address.referenceId
        this.companyName = this.address.companyName
        this.addressLine1 = this.address.addressLine1
        this.addressLine2 = this.address.addressLine2
        this.contactName = this.address.contactName
        this.contactNumber = this.address.contactNo
        this.contactEmail = this.address.contactEmail
        this.specialInstructions = this.address.notes
        this.onSearch(this.address.postcode.toString())
        this.showHelperText = true
      }
    }

    noData: boolean = null

    onSearch (search: string) {
      const lettersLimit = 3

      this.noData = false
      if (search.length < lettersLimit) {
        this.items = []
        return
      }

      const config = {
        headers: getAuthenticatedToken(),
        params: {
          locality: search
        }
      }

      this.$axios.get('/api/v1/address/locality', config)
        .then(response => {
          this.items = response.data.localities
        }, error => {
          if (!this.items.length) {
            this.noData = true
          } else {
            this.$log.error(error)
          }
        })
    }

    resetModal () {
      this.referenceId = null
      this.companyName = null
      this.addressLine1 = null
      this.addressLine2 = null
      this.contactName = null
      this.contactNumber = null
      this.contactEmail = null
      this.specialInstructions = null
      this.validateCompanyNameFlag = null
      this.validateContactNameFlag = null
      this.validateContactEmailFlag = null
      this.validateAddressLineFlag = null
      this.selectedLocation = null
      this.validateLocationFlag = null
      this.items = []
      this.noData = null
    }

    handleSubmit (bvModalEvt: BvModalEvent) {
      bvModalEvt.preventDefault()
      this.submitAddressForm()
    }

    submitAddressForm (): void {
      this.validateCompanyName()
      this.validateContactName()
      this.validateContactEmail()
      this.validateAddressLine()
      this.validateLocation()

      if (this.validateCompanyNameFlag && this.validateContactNameFlag && (this.validateContactEmailFlag === null || this.validateContactEmailFlag) && this.validateAddressLineFlag && !this.validateLocationFlag) {
        const emitAddress = new Address(
          this.getId(),
          this.referenceId,
          this.companyName,
          this.addressLine1,
          this.addressLine2,
          this.selectedLocation.location,
          this.selectedLocation.postcode,
          this.selectedLocation.state,
          this.contactName,
          this.contactNumber,
          this.contactEmail,
          this.specialInstructions
        )
        this.$emit('emit-address', emitAddress)
        this.$nextTick(() => {
          this.$bvModal.hide(this.modalId)
        })
      }
    }

    getId () {
      if (this.address) {
        return this.address.id
      }
      return null
    }

    validateCompanyName () {
      if (!validateStringField(this.companyName)) {
        this.validateCompanyNameFlag = false
        return false
      }
      this.validateCompanyNameFlag = true
      return true
    }

    validateContactName () {
      if (!validateStringField(this.contactName)) {
        this.validateContactNameFlag = false
        return false
      }
      this.validateContactNameFlag = true
      return true
    }

    /**
    * Validating email against the universal email regex
    */
    validateContactEmail () {
      if (this.contactEmail) {
        if (!validateEmailField(this.contactEmail)) {
          this.validateContactEmailFlag = false
          return false
        } else {
          this.validateContactEmailFlag = true
          return true
        }
      }
      this.validateContactEmailFlag = null
    }

    validateAddressLine () {
      if (!validateStringField(this.addressLine1)) {
        this.validateAddressLineFlag = false
        return false
      }
      this.validateAddressLineFlag = true
      return true
    }

    validateLocation () {
      if (!this.selectedLocation || !this.selectedLocation.location || !this.selectedLocation.state || !this.selectedLocation.postcode) {
        this.validateLocationFlag = true
        return true
      }
      this.validateLocationFlag = false
      return false
    }
}
</script>

<style scoped lang="scss" src="./AddressFormModal.scss"></style>

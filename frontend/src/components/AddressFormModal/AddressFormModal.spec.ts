import { createLocalVue, mount, shallowMount } from '@vue/test-utils'
import AddressFormModal from '@/components/AddressFormModal/AddressFormModal.vue'
import localVue from '@/helpers/test/LocalVueHelpers'
import '@testing-library/jest-dom'

describe('AddressFormModal.vue', () => {
  it('renders AddressFormModal component correctly', () => {
    const wrapper = shallowMount(AddressFormModal, {
      localVue,
      propsData: {
        modalId: 'new-address-modal',
        headerTitle: 'New Address',
        idLabel: 'Receiver ID',
        buttonName: 'Add'
      }
    })

    const modal = wrapper.find('#new-address-modal')

    expect(modal.exists()).toBeTruthy()
    expect(modal.element).toBeVisible()
    expect(modal.findAll('label').at(0).text()).toEqual('Receiver ID')
    expect(modal.findAll('label').at(1).text()).toEqual('Company Name')
    expect(modal.findAll('label').at(2).text()).toEqual('Contact Name')
    expect(modal.findAll('label').at(3).text()).toEqual('Contact Number')
    expect(modal.findAll('label').at(4).text()).toEqual('Contact Email')
    expect(modal.findAll('label').at(5).text()).toEqual('Address Line 1')
    expect(modal.findAll('label').at(6).text()).toEqual('Address Line 2')
    expect(modal.findAll('label').at(7).text()).toEqual('Suburb / Postcode')
    expect(modal.findAll('label').at(8).text()).toEqual('Special Instructions')
  })

  it('renders error when data is invalid', () => {
    const wrapper = shallowMount(AddressFormModal, {
      localVue,
      propsData: {
        modalId: 'new-address-modal',
        headerTitle: 'New Address',
        idLabel: 'Receiver ID',
        buttonName: 'Add'
      }
    })

    wrapper.vm.$data.companyName = ''
    wrapper.vm.$data.contactEmail = 'abc'
    expect(wrapper.vm.$data.validateCompanyNameFlag).toBeNull()
    expect(wrapper.vm.$data.validateContactEmailFlag).toBeNull()
    wrapper.trigger('ok')
    expect(wrapper.vm.$data.validateCompanyNameFlag).toBeFalsy()
    expect(wrapper.vm.$data.validateContactEmailFlag).toBeFalsy()
  })
})

import { shallowMount } from '@vue/test-utils'
import DatePicker from '@/components/DatePicker/DatePicker.vue'
import localVue from '@/helpers/test/LocalVueHelpers'

describe('<DatePicker />', () => {
  let propsData: object = {}
  let wrapper: any
  let label: any
  let datepicker: any

  beforeEach(() => {
    wrapper = shallowMount(DatePicker, {
      localVue,
      propsData: propsData
    })

    label = wrapper.find('.datepicker-label')
    datepicker = wrapper.find('b-form-datepicker-stub')
  })

  describe('when label prop is provided', () => {
    const labelText = 'Delivery date'
    beforeAll(() => {
      propsData = {
        label: labelText
      }
    })

    it('renders label', () => {
      expect(label.exists()).toBeTruthy()
      expect(label.text()).toEqual(labelText)
    })

    it('renders datepicker', () => {
      expect(datepicker.exists()).toBeTruthy()
    })
  })

  describe('when label prop is not provided', () => {
    beforeAll(() => {
      propsData = {}
    })

    it('doesn\'t render label', () => {
      expect(label.exists()).toBeFalsy()
    })

    it('renders datepicker', () => {
      expect(datepicker.exists()).toBeTruthy()
    })
  })
})

import { shallowMount } from '@vue/test-utils'
import TimeSlotPicker from '@/components/TimeSlotPicker/TimeSlotPicker.vue'
import localVue from '@/helpers/test/LocalVueHelpers'

describe('<TimeSlotPicker />', () => {
  const propsData = {
    baseDate: new Date()
  }

  const wrapper = shallowMount(TimeSlotPicker, {
    localVue,
    propsData: propsData
  })

  it('renders the component correctly', () => {
    expect(wrapper.findAll('b-form-timepicker-stub')).toHaveLength(2)
  })
})

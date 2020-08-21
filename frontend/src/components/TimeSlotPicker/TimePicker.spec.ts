import { shallowMount } from '@vue/test-utils'
import TimePicker from '@/components/TimePicker/TimePicker.vue'
import localVue from '@/helpers/test/LocalVueHelpers'

describe('<TimePicker />', () => {
  const propsData = {
    name: 'from-timepicker'
  }

  const wrapper = shallowMount(TimePicker, {
    localVue,
    propsData: propsData
  })

  const timePicker = wrapper.find('b-form-timepicker-stub')

  it('renders the component correctly', () => {
    expect(timePicker.exists()).toBeTruthy()
  })
})

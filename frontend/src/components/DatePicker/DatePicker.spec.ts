import { shallowMount } from '@vue/test-utils'
import DatePicker from '@/components/DatePicker/DatePicker.vue'
import localVue from '@/helpers/test/LocalVueHelpers'

describe('<DatePicker />', () => {
  const wrapper = shallowMount(DatePicker, {
    localVue,
    propsData: {
      name: 'datepicker'
    }
  })

  it('renders datepicker', () => {
    expect(wrapper.find('b-form-datepicker-stub').exists()).toBeTruthy()
  })
})

import { shallowMount } from '@vue/test-utils'
import Alert from '@/components/Alert/Alert.vue'
import localVue from '@/helpers/test/LocalVueHelpers'

describe('<Alert />', () => {
  const propsData = {
    variant: 'warning',
    text: 'This is a warning message!'
  }

  const wrapper = shallowMount(Alert, {
    localVue,
    propsData: propsData
  })

  const alert = wrapper.find('b-alert-stub')

  it('renders the component correctly', () => {
    expect(alert.exists()).toBeTruthy()
    expect(alert.text()).toEqual(propsData.text)
    expect(alert.attributes().variant).toEqual(propsData.variant)
  })
})

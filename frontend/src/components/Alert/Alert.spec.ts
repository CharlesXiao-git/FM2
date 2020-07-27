import { shallowMount } from '@vue/test-utils'
import Alert from '@/components/Alert/Alert.vue'

describe('<Alert />', () => {
  const propsData = {
    variant: 'warning',
    text: 'This is a warning message!'
  }

  const wrapper = shallowMount(Alert, {
    propsData: propsData
  })

  const alert = wrapper.find('b-alert')

  it('renders the component correctly', () => {
    expect(alert.exists()).toBeTruthy()
    expect(alert.text()).toEqual(propsData.text)
    expect(alert.attributes().variant).toEqual(propsData.variant)
  })
})

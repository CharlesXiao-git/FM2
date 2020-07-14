import { shallowMount } from '@vue/test-utils'
import Login from '@/views/Login/Login.vue'

describe('Login.vue', () => {
  it('renders Login component correctly', () => {
    const wrapper = shallowMount(Login)
    expect(wrapper.findAll('b-form-input').length).toEqual(2)
    expect(wrapper.find('button').text()).toEqual('Sign in')
    expect(wrapper.vm.$data.validation).toBeNull();
  })

  it('displays the default error message on error occurrence', () => {
    const wrapper = shallowMount(Login)

    wrapper.vm.$data.username = 'abc'
    wrapper.vm.$data.password = 'abc'
    wrapper.find('button').trigger('click');
    expect(wrapper.vm.$data.validation).toBeFalsy();
    expect(wrapper.find('b-form-invalid-feedback').text()).toEqual('Error logging in! Please try again or press the reset password button below.')
  })
})

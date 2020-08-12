// Reference: https://vue-test-utils.vuejs.org/guides/common-tips.html#applying-global-plugins-and-mixins
import BootstrapVue from 'bootstrap-vue'
import { createLocalVue } from '@vue/test-utils'

const localVue = createLocalVue()
localVue.use(BootstrapVue)

export default localVue

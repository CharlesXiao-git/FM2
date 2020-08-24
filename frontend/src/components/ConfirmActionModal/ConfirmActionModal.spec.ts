import { mount } from '@vue/test-utils'
import ConfirmActionModal from '@/components/ConfirmActionModal/ConfirmActionModal.vue'
import localVue from '@/helpers/test/LocalVueHelpers'
import '@testing-library/jest-dom'

describe('<ConfirmActionModal />', () => {
  const confirmFunction = jest.fn()
  const contentText = 'Are you sure?'

  const wrapper = mount(ConfirmActionModal, {
    localVue,
    slots: {
      content: `<p>${contentText}</p>`
    },
    propsData: {
      modalId: 'confirm-action-modal',
      buttonName: 'confirm'
    }
  })

  wrapper.trigger('ok')
  let modal = wrapper.find('#confirm-action-modal')

  describe('initial state', () => {
    it('hides the modal', () => {
      expect(modal.exists()).toBeTruthy()
      expect(modal.element).not.toBeVisible()
    })
  })

  describe('modal buttons interaction', () => {
    beforeEach(async () => {
      await wrapper.trigger('ok')
      modal = wrapper.find('#confirm-action-modal')
    })

    describe('clicking on the OK button', () => {
      const okBtn = modal.find('.modal-footer').findAll('button').at(1)

      it('calls the confirm function and closes the modal', async () => {
        await okBtn.trigger('click')
        expect(modal.element).not.toBeVisible()
      })
    })

    describe('clicking on the Cancel button', () => {
      const cancelBtn = modal.find('.modal-footer').findAll('button').at(0)

      it('closes the modal', async () => {
        await cancelBtn.trigger('click')
        expect(modal.element).not.toBeVisible()
      })
    })
  })
})

import { mount } from '@vue/test-utils'
import ConfirmActionModal from '@/components/ConfirmActionModal/ConfirmActionModal.vue'
import localVue from '@/helpers/test/LocalVue'
import '@testing-library/jest-dom'

describe('<ConfirmActionModal />', () => {
  const confirmFunction = jest.fn()
  const contentText = 'Are you sure?'

  const wrapper = mount(ConfirmActionModal, {
    localVue,
    slots: {
      trigger: '<b-button class="delete-btn">Delete</b-button>',
      content: `<p>${contentText}</p>`
    },
    propsData: {
      confirmed: confirmFunction
    }
  })

  const triggerBtn = wrapper.find('.delete-btn')
  let modal = wrapper.find('#confirm-action-modal')

  describe('initial state', () => {
    it('renders the trigger element', () => {
      expect(triggerBtn.exists()).toBeTruthy()
    })

    it('hides the modal', () => {
      expect(modal.exists()).toBeTruthy()
      expect(modal.element).not.toBeVisible()
    })
  })

  describe('clicking on the trigger element', () => {
    beforeAll(async () => {
      await triggerBtn.trigger('click')
      modal = wrapper.find('#confirm-action-modal')
    })

    it('opens the modal', async () => {
      expect(modal.element).toBeVisible()
    })

    it('renders modal content correctly', () => {
      expect(modal.find('.modal-body').text()).toEqual(contentText)
      expect(modal.find('.modal-footer').findAll('button')).toHaveLength(2)
    })
  })

  describe('modal buttons interaction', () => {
    beforeEach(async () => {
      await triggerBtn.trigger('click')
      modal = wrapper.find('#confirm-action-modal')
    })

    describe('clicking on the OK button', () => {
      const okBtn = modal.find('.modal-footer').findAll('button').at(1)

      it('calls the confirm function and closes the modal', async () => {
        await okBtn.trigger('click')
        expect(confirmFunction).toHaveBeenCalledTimes(1)
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

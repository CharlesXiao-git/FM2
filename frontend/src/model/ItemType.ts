import {ItemTemplate} from '@/model/ItemTemplate'

export class ItemType {
  id: number
  type: string
  itemTemplate: ItemTemplate
  isMutable: boolean
}

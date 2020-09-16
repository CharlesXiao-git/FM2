import {Consignment} from '@/model/Consignment'

export class Manifest {
    id: string
    manifestNumber: string
    goodsPickupStatus: boolean
    userClientId: number
    readyTime: string
    closingTime: string
    dataStatus: string
    consignments: Consignment []

    constructor (id: string = null, manifestNumber: string = null, goodsPickupStatus: boolean = null, userClientId: number = null,
                 readyTime: string = null, closingTime: string = null, dataStatus: string = null, consignments: Consignment [] = null) {

        this.id = id
        this.manifestNumber= manifestNumber
        this.goodsPickupStatus = goodsPickupStatus
        this.userClientId = userClientId
        this.readyTime = readyTime
        this.closingTime = closingTime
        this.dataStatus = dataStatus
        this.consignments = consignments
    }
}

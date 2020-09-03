<template>
    <div>
        <div class="menu-toggle d-block d-md-none">
            <b-button v-on:click="toggleMenuBar()" :class="visible ? null:'collapsed'" v-b-toggle.sidebar>
                <i class="fa ml-3 mr-2 fa-bars"></i>
                Menu
            </b-button>
        </div>
        <b-collapse id="sidebar" v-model="visible" >
            <div class="sidebar" :class="sidebarClass">
                <b-nav vertical>
                    <b-nav-item>
                        <b-button>
                            <i class="fas mr-1 fa-chalkboard"></i>Dashboard
                        </b-button>
                    </b-nav-item>
                    <b-nav-item>
                        <b-button class="dropdown-toggle" v-b-toggle.create-new-collapse>
                            <i class="fas mr-1 fa-plus"></i>
                            Create New
                        </b-button>
                        <b-collapse class="menu-items" id="create-new-collapse">
                            <b-nav-item to="/consignment/default">
                                Consignment
                            </b-nav-item>
                            <b-nav-item to="/consignment/return">
                                Return
                            </b-nav-item>
                            <b-nav-item to="/consignment/third-party">
                                3rd Party Pickup
                            </b-nav-item>
                            <b-nav-item>
                                Bulk Address Import
                            </b-nav-item>
                            <b-nav-item>
                                Bulk CSV Consignment Import
                            </b-nav-item>
                            <b-nav-item>
                                Bulk XML Consignment Import
                            </b-nav-item>
                        </b-collapse>
                    </b-nav-item>
                    <b-nav-item>
                        <b-button to="/consignment/list">
                            <i class="fas mr-1 fa-folder-open"></i>
                            Consignments
                        </b-button>
                    </b-nav-item>
                    <b-nav-item>
                        <b-button>
                            <i class="fas mr-1 fa-book-open"></i>
                            Manifests
                        </b-button>
                    </b-nav-item>
                    <b-nav-item>
                        <b-button>
                            <i class="fas mr-1 fa-map-marker-alt"></i>
                            Track and Trace
                        </b-button>
                    </b-nav-item>
                    <b-nav-item>
                        <b-button class="dropdown-toggle" v-b-toggle.administration-collapse>
                            <i class="fas mr-1 fa-chart-line"></i>
                            Administration
                        </b-button>
                        <b-collapse class="menu-items" id="administration-collapse">
                            <b-nav-item>
                                Customer Reporting
                            </b-nav-item>
                            <b-nav-item>
                                Carrier Invoicing Upload
                            </b-nav-item>
                            <b-nav-item>
                                Customer Invoicing
                            </b-nav-item>
                        </b-collapse>
                    </b-nav-item>
                    <b-nav-item>
                        <b-button class="dropdown-toggle" v-b-toggle.admin-collapse>
                            <i class="fas mr-1 fa-user-secret"></i>
                            Admin
                        </b-button>
                        <b-collapse class="menu-items" id="admin-collapse">
                            <b-nav-item>
                                <i class="fas mr-1 fa-clock"></i>
                                Transit Times
                            </b-nav-item>
                            <b-nav-item>
                                <i class="fas mr-1 fa-map-signs"></i>
                                Zones
                            </b-nav-item>
                            <b-nav-item>
                                Resend Data
                            </b-nav-item>
                        </b-collapse>
                    </b-nav-item>
                    <b-nav-item>
                        <b-button class="dropdown-toggle" v-b-toggle.broker-collapse>
                            <i class="fas mr-1 fa-bookmark"></i>
                            Broker
                        </b-button>
                        <b-collapse class="menu-items" id="broker-collapse">
                            <b-nav-item>
                                Broker Customer Reporting
                            </b-nav-item>
                            <b-nav-item>
                                Carrier rules
                            </b-nav-item>
                            <b-nav-item>
                                <i class="fas mr-1 fa-users"></i>
                                Users
                            </b-nav-item>
                            <b-nav-item>
                                <i class="fas mr-1 fa-truck"></i>
                                Carriers
                            </b-nav-item>
                            <b-nav-item>
                                <i class="fas mr-1 fa-user"></i>
                                Account managers
                            </b-nav-item>
                        </b-collapse>
                    </b-nav-item>
                    <b-nav-item>
                        <b-button>
                            <i class="fas mr-1 fa-calculator"></i>
                            Quick Quote
                        </b-button>
                    </b-nav-item>
                    <b-nav-item>
                        <b-button :to="{ name : 'AddressBook' }">
                            <i class="fas mr-1 fa-address-book"></i>
                            Address Book
                        </b-button>
                    </b-nav-item>
                    <b-nav-item>
                        <b-button>
                            <i class="fas mr-1 fa-cog"></i>
                            Settings
                        </b-button>
                    </b-nav-item>
                </b-nav>
            </div>
        </b-collapse>
    </div>
</template>

<script lang="ts">
import { Component, Vue } from 'vue-property-decorator'
import { userRole } from '@/helpers/auth/UserHelpers'

@Component
export default class Sidebar extends Vue {
  visible = true
  sidebarClass = 'd-none d-md-block'
  role: string = userRole()

  toggleMenuBar () {
    this.sidebarClass = ''
  }
}
</script>

<style scoped lang="scss" src="./Sidebar.scss"></style>

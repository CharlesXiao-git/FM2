package com.freightmate.harbour.repository;

import com.freightmate.harbour.model.CarrierAccount;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface CarrierAccountRepository extends PagingAndSortingRepository<CarrierAccount, Long> {
    String CARRIERACCOUNT_BY_USERID = "SELECT ca.id, ca.account_number, ca.carrier_id, ca.user_broker_id, ca.user_customer_id, ca.is_default, ca.rule_res_address_cost,\n" +
            "       ca.rule_tailgate_cost, ca.rule_dg_cost, ca.rule_oversize_cat1_cost, ca.rule_oversize_cat2_cost, \n" +
            "       ca.rule_oversize_cat3_cost, ca.rule_oversize_cat4_cost, ca.rule_oversize_cat5_cost, ca.rule_oversize_cat1_size, \n" +
            "       ca.rule_oversize_cat2_size, ca.rule_oversize_cat3_size, ca.rule_oversize_cat4_size, ca.rule_oversize_cat5_size,\n" +
            "       ca.rule_manual_handling_max_l, ca.rule_manual_handling_max_w, ca.rule_manual_handling_max_h, \n" +
            "       ca.rule_manual_handling_min_kg, ca.rule_manual_handling_max_kg, ca.rule_manual_handling_charge_per, \n" +
            "       ca.rule_manual_handling_charge, ca.fuel, ca.per_kg_cubic, ca.carton_cubic, ca.carton_max_h, ca.carton_max_l, \n" +
            "       ca.carton_max_w, ca.carton_max_kg, ca.pallet_cubic, ca.pallet_max_h, ca.pallet_max_l, ca.pallet_max_w, \n" +
            "       ca.pallet_max_kg, ca.carton_average_weight, ca.pallet_overcharge, ca.half_pallet_max_h, ca.half_pallet_max_l, \n" +
            "       ca.half_pallet_max_w, ca.half_pallet_max_kg, ca.rule_cat_format_1, ca.rule_cat_format_2, ca.rule_cat_format_3, \n" +
            "       ca.rule_cat_format_4, ca.rule_cat_format_5, ca.created_at, ca.created_by, \n" +
            "       ca.updated_at, ca.updated_by, ca.is_deleted, ca.deleted_at, ca.deleted_by\n" +
            "FROM carrier_account ca\n" +
            "LEFT JOIN user_broker ub ON ca.user_broker_id = ub.id\n" +
            "LEFT JOIN user_customer uc ON ca.user_customer_id = uc.id\n" +
            "WHERE ca.is_deleted = 0\n" +
            "AND ub.user_id = ?1";

    String USER_CARRIERACCOUNT = "SELECT ca.id, ca.account_number, ca.carrier_id, ca.user_broker_id, ca.user_customer_id, ca.is_default, ca.rule_res_address_cost,\n" +
            "       ca.rule_tailgate_cost, ca.rule_dg_cost, ca.rule_oversize_cat1_cost, ca.rule_oversize_cat2_cost, \n" +
            "       ca.rule_oversize_cat3_cost, ca.rule_oversize_cat4_cost, ca.rule_oversize_cat5_cost, ca.rule_oversize_cat1_size, \n" +
            "       ca.rule_oversize_cat2_size, ca.rule_oversize_cat3_size, ca.rule_oversize_cat4_size, ca.rule_oversize_cat5_size,\n" +
            "       ca.rule_manual_handling_max_l, ca.rule_manual_handling_max_w, ca.rule_manual_handling_max_h, \n" +
            "       ca.rule_manual_handling_min_kg, ca.rule_manual_handling_max_kg, ca.rule_manual_handling_charge_per, \n" +
            "       ca.rule_manual_handling_charge, ca.fuel, ca.per_kg_cubic, ca.carton_cubic, ca.carton_max_h, ca.carton_max_l, \n" +
            "       ca.carton_max_w, ca.carton_max_kg, ca.pallet_cubic, ca.pallet_max_h, ca.pallet_max_l, ca.pallet_max_w, \n" +
            "       ca.pallet_max_kg, ca.carton_average_weight, ca.pallet_overcharge, ca.half_pallet_max_h, ca.half_pallet_max_l, \n" +
            "       ca.half_pallet_max_w, ca.half_pallet_max_kg, ca.rule_cat_format_1, ca.rule_cat_format_2, ca.rule_cat_format_3, \n" +
            "       ca.rule_cat_format_4, ca.rule_cat_format_5, ca.created_at, ca.created_by, \n" +
            "       ca.updated_at, ca.updated_by, ca.is_deleted, ca.deleted_at, ca.deleted_by\n" +
            "FROM carrier_account ca\n" +
            "LEFT JOIN user_broker ub ON ca.user_broker_id = ub.id\n" +
            "WHERE ca.is_deleted = 0\n" +
            "AND ub.user_id = ?1\n" +
            "AND ca.id IN ?2";

    String CARRIERACCOUNT_BY_IDS = "SELECT ca.id, ca.account_number, ca.carrier_id, ca.user_broker_id, ca.user_customer_id, ca.is_default, ca.rule_res_address_cost,\n" +
            "       ca.rule_tailgate_cost, ca.rule_dg_cost, ca.rule_oversize_cat1_cost, ca.rule_oversize_cat2_cost, \n" +
            "       ca.rule_oversize_cat3_cost, ca.rule_oversize_cat4_cost, ca.rule_oversize_cat5_cost, ca.rule_oversize_cat1_size, \n" +
            "       ca.rule_oversize_cat2_size, ca.rule_oversize_cat3_size, ca.rule_oversize_cat4_size, ca.rule_oversize_cat5_size,\n" +
            "       ca.rule_manual_handling_max_l, ca.rule_manual_handling_max_w, ca.rule_manual_handling_max_h, \n" +
            "       ca.rule_manual_handling_min_kg, ca.rule_manual_handling_max_kg, ca.rule_manual_handling_charge_per, \n" +
            "       ca.rule_manual_handling_charge, ca.fuel, ca.per_kg_cubic, ca.carton_cubic, ca.carton_max_h, ca.carton_max_l, \n" +
            "       ca.carton_max_w, ca.carton_max_kg, ca.pallet_cubic, ca.pallet_max_h, ca.pallet_max_l, ca.pallet_max_w, \n" +
            "       ca.pallet_max_kg, ca.carton_average_weight, ca.pallet_overcharge, ca.half_pallet_max_h, ca.half_pallet_max_l, \n" +
            "       ca.half_pallet_max_w, ca.half_pallet_max_kg, ca.rule_cat_format_1, ca.rule_cat_format_2, ca.rule_cat_format_3, \n" +
            "       ca.rule_cat_format_4, ca.rule_cat_format_5, ca.created_at, ca.created_by, \n" +
            "       ca.updated_at, ca.updated_by, ca.is_deleted, ca.deleted_at, ca.deleted_by\n" +
            "FROM carrier_account ca\n" +
            "WHERE ca.is_deleted = 0\n" +
            "AND ca.id IN ?1";

    String DELETE_BY_IDS = "UPDATE carrier_account ca\n" +
            "INNER JOIN user_broker ub ON ca.user_broker_id = ub.id\n" +
            "SET ca.is_deleted = 1,\n" +
            "    ca.deleted_by = ?2,\n" +
            "    ca.deleted_at = NOW()\n" +
            "WHERE ca.is_deleted = 0\n" +
            "AND ca.id IN ?1\n" +
            "AND ub.user_id = ?2";

    // No query needed, this is a named function
    List<CarrierAccount> findAllByUserBrokerId(long userBrokerId);

    @Query(value = CARRIERACCOUNT_BY_USERID, nativeQuery = true)
    List<CarrierAccount> getCarrierAccounts(long userId);

    @Query(value = USER_CARRIERACCOUNT, nativeQuery = true)
    List<CarrierAccount> getCarrierAccounts(long userId, List<Long> ids);

    @Query(value = CARRIERACCOUNT_BY_IDS, nativeQuery = true)
    List<CarrierAccount> getCarrierAccounts(List<Long> ids);

    @Query(value = DELETE_BY_IDS, nativeQuery = true)
    @Modifying
    void deleteCarrierAccounts(List<Long> ids, long userId);
}

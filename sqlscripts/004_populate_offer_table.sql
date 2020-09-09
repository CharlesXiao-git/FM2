USE freightmate;

INSERT into offer (carrier_account_id, ETA, freight_cost,
                   category_1_fees, category_2_fees, fuel_surcharge
                   , gst, total_cost, selected, created_by)
VALUES
    (1, 5, 7.5, 0, 0, 1.05, 0.86, 9.41, 1, 1),
    (2, 3, 3, 3, 3, 3, 3, 15, 0, 1),
    (3, 4, 4, 4, 4, 4, 4, 20, 0, 1),
    (4, 5, 5, 5, 5, 5, 5, 25, 0, 1),
    (5, 6, 6, 6, 6, 6, 6, 30, 0, 1);

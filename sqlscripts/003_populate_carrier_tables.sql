USE freightmate;


INSERT INTO carrier (name, display_name, is_data_transfer_required, data_transfer_frequency,
                     label_type, has_suburb_surcharge, has_sender_id, service_types, pricing,
                     metro_zones, is_internal_invoicing, is_tracking_difot, tracking_url, created_by)
VALUES
       ( 'startrack', 'Startrack', TRUE, 'INSTANT', 'CUSTOM', FALSE, FALSE, '["prm","exp","fpp"]', '["kg"]', '["MEL","SYD","BRS","PER","ADL","GEE"]', TRUE, TRUE, 'https://msto.startrack.com.au/track-trace/?ID=%CONNOTE_NUMBER%',1),
       ( 'startrack2', 'Startrack', TRUE, 'INSTANT', 'CUSTOM', FALSE, FALSE, '["prm","exp","fpp"]', '["kg"]', '["MEL","SYD","BRS","PER","ADL","GEE"]', TRUE, TRUE, 'https://msto.startrack.com.au/track-trace/?ID=%CONNOTE_NUMBER%',1),
       ( 'ipec', 'IPEC', TRUE, 'INSTANT', 'CUSTOM', TRUE, TRUE, '["road_express","local"]', '["kg","carton"]', '["MEL","SYD","BNE","PER","ADL","IVIC"]', TRUE, TRUE, 'https://www.mytoll.com/?externalSearchQuery=%CONNOTE_NUMBER%&op=Search&url=',1),
       ( 'toll_priority', 'Toll Priority', TRUE, 'INSTANT', 'CUSTOM', TRUE, FALSE, '["parcel_overnight","auswide_satchel"]', '["kg","carton"]', '["MEL","SYD","BNE","PER","ADL","IVIC"]', FALSE, TRUE, 'https://www.mytoll.com/?externalSearchQuery=%CONNOTE_NUMBER%&op=Search&url=',1),
       ( 'toll_express', 'Toll Express', TRUE, 'INSTANT', 'CUSTOM', FALSE, TRUE, '["general","express","premium","refrigerated"]', '["kg"]', '["MELBOURNE","SYDNEY","BRISBANE","PERTH","ADELAIDE","GEELONG"]', FALSE, TRUE, 'https://www.mytoll.com/?externalSearchQuery=%CONNOTE_NUMBER%&op=Search&url=',1),
       ( 'hunter', 'Hunter', TRUE, 'INSTANT', 'CUSTOM', FALSE, FALSE, '["road","air"]', '["kg"]', '["MELBOURNE","SYDNEY","BRISBANE","PERTH","ADELAIDE","GEELONG"]', FALSE, FALSE, 'https://track.aftership.com/hunter-express/CONNOTE_NUMBER%?',1),
       ( 'tnt', 'TNT', TRUE, 'INSTANT', 'CUSTOM', FALSE, TRUE, '["overnight_express","road_express"]', '["kg"]', '["MEL","SYD","BNE","PTH","ADL","GLG"]', TRUE, TRUE, 'https://www.tntexpress.com.au/cct/TrackResultsCon.asp?User=nawctrack&Password=UmxLHf3t&con=%CONNOTE_NUMBER%',1),
       ( 'northline', 'Northline', TRUE, 'INSTANT', 'CUSTOM', FALSE, FALSE, '["northline"]', '["kg"]', '["MEL","SYD","BNE","PTH","ADL","GEE"]', FALSE, TRUE, 'https://track.northline.com.au/status/%CONNOTE_NUMBER%',1),
       ( 'national', 'National', TRUE, 'INSTANT', 'CUSTOM', FALSE, FALSE, '["general","express"]', '["kg","pallet","bulka_bag"]', '["MELBOURNE","SYDNEY","BRISBANE","PERTH","ADELAIDE","GEELONG"]', FALSE, TRUE, NULL,1),
       ( 'sadleirs', 'Sadleirs', FALSE, NULL, 'CUSTOM', FALSE, FALSE, '["rail"]', '["kg","pallet"]', '["MELBOURNE","SYDNEY","BRISBANE","PERTH","ADELAIDE","GEELONG"]', FALSE, FALSE, 'https://portal.sadleirs.com.au/fr8portal/Home/PublicConsignment?track=%CONNOTE_NUMBER%',1),
       ( 'vfs', 'VFS', TRUE, 'INSTANT', 'CUSTOM', FALSE, FALSE, '["general_road","express_road"]', '["carton","pallet","skid"]', '["Z1",null,null,null,null,"Z2"]', FALSE, TRUE, 'http://web.victorianfreight.com.au/webconsignment/ftssearch.aspx?connum=%CONNOTE_NUMBER%&Go=Submit',1),
       ( 'tuco', 'Tuco', FALSE, NULL, 'CUSTOM', FALSE, FALSE, '["general","express","hazardous"]', '["kg","pallet"]', '["MELBOURNE","SYDNEY","BRISBANE","PERTH","ADELAIDE","GEELONG"]', FALSE, FALSE, NULL,1),
       ( 'tas_freight', 'TAS Freight', TRUE, 'INSTANT', 'CUSTOM', FALSE, FALSE, '["general","express"]', '["kg","pallet"]', '["MEL","SYD","BNE","600","ADL","303"]', FALSE, TRUE, NULL,1),
       ( 'jayde', 'Jayde', TRUE, 'INSTANT', 'CUSTOM', FALSE, FALSE, '["general","express","premium"]', '["kg"]', '["MEL","SYD","BNE","PTH","ADL","31"]', FALSE, FALSE, NULL,1),
       ( 'fresh_freight', 'Fresh Freight', FALSE, NULL, 'CUSTOM', FALSE, FALSE, '["road_freight"]', '["kg","pallet","chilled","frozen"]', '["MEL",null,null,null,null,null]', FALSE, FALSE, 'https://ffglogistics.com/track-and-trace/',1),
       ( 'followmont', 'Followmont', TRUE, 'INSTANT', 'CUSTOM', FALSE, FALSE, '["express"]', '["kg"]', '["TMEL","SYD","TBRI",null,null,null]', FALSE, FALSE, 'http://www.followmont.com.au/customer-support/2428-2/',1),
       ( 'followmont2', 'Followmont', TRUE, 'INSTANT', 'CUSTOM', FALSE, FALSE, '["express"]', '["kg"]', '["TMEL","SYD","TBRI",null,null,null]', FALSE, FALSE, 'http://www.followmont.com.au/customer-support/2428-2/',1),
       ( 'victas', 'VicTas', TRUE, 'INSTANT', 'CUSTOM', FALSE, FALSE, '["road_freight"]', '["carton","pallet","skid"]', '["M1",null,null,null,"ADL","V1"]', FALSE, FALSE, 'http://portal.vtfe.com.au/Track/VTFE/%CONNOTE_NUMBER%',1),
       ( 'go_logistics', 'Go Logistics', TRUE, 'INSTANT', 'CUSTOM', FALSE, FALSE, '["general"]', '["carton"]', '["MEL","1",null,null,null,"MEL"]', FALSE, TRUE, 'https://www.gologistics.com.au/track-your-parcel',1),
       ( 'ryanston', 'Ryanston', TRUE, 'DAILY', 'CUSTOM', FALSE, FALSE, '["express"]', '["kg"]', '["MEL","SYD","BNE",null,"ADL",null]', FALSE, FALSE, NULL,1),
       ( 'labels', 'Labels', FALSE, NULL, 'GENERIC', FALSE, FALSE, '["aus_wide"]', '["kg","pallet","carton"]', '["AUS_WIDE"]', FALSE, FALSE, NULL,1),
       ( 'dxt', 'DXT', TRUE, 'INSTANT', 'CUSTOM', FALSE, FALSE, '["general","express"]', '["kg","pallet"]', '[null,"NM1",null,null,null,null]', FALSE, FALSE, NULL,1),
       ( 'abs', 'ABS', FALSE, NULL, 'GENERIC', FALSE, FALSE, '["express"]', '["pallet"]', '["MELBOURNE","SYDNEY","BRISBANE","PERTH","ADELAIDE","GEELONG"]', FALSE, FALSE, NULL,1),
       ( 'railroad', 'Railroad', FALSE, 'INSTANT', 'CUSTOM', FALSE, FALSE, '["general","road_freight","express","local"]', '["kg","pallet"]', '["MEL","SYD","BRISBANE","PTH","ADE","VIC-GL"]', FALSE, FALSE, NULL,1),
       ( 'josies', 'Josies', TRUE, 'INSTANT', 'GENERIC', FALSE, FALSE, '["same_day","next_day","express","rocket"]', '["kg","pallet","carton"]', '["4",null,null,null,null,"6"]', FALSE, FALSE, NULL,1),
       ( 'jit', 'JIT', FALSE, NULL, 'CUSTOM', FALSE, FALSE, '["express"]', '["pallet","chilled","frozen"]', '["MELBOURNE","SYDNEY","BRISBANE","PERTH","ADELAIDE","GEELONG"]', FALSE, FALSE, NULL,1),
       ( 'bf_freight', 'BF Freight', FALSE, NULL, 'GENERIC', FALSE, FALSE, '["express"]', '["carton","skid","pallet"]', '["MELBOURNE","SYDNEY","BRISBANE","PERTH",null,"GEELONG"]', FALSE, FALSE, NULL,1),
       ( 'sct_logistics', 'SCT Logistics', FALSE, NULL, 'GENERIC', FALSE, FALSE, '["general"]', '["kg","pallet","chilled","frozen"]', '["MELBOURNE","SYDNEY","BRISBANE","PERTH","ADELAIDE","GEELONG"]', FALSE, FALSE, NULL,1),
       ( 'bluestar_logistics', 'Bluestar Logistics', TRUE, 'INSTANT', 'GENERIC', FALSE, FALSE, '["express"]', '["kg"]', '["VIC (Zone 1)","NSW (Zone 1)","QLD (Zone 1)","WA (Zone 1)","SA (Zone 1)","VIC (Zone 2)"]', FALSE, FALSE, NULL,1),
       ( 'bluestar_logistics2', 'Bluestar Logistics', TRUE, 'INSTANT', 'GENERIC', FALSE, FALSE, '["express"]', '["kg"]', '["VIC (Zone 1)","NSW (Zone 1)","QLD (Zone 1)","WA (Zone 1)","SA (Zone 1)","VIC (Zone 2)"]', FALSE, FALSE, NULL,1),
       ( 'slade_transport', 'Slade Transport', FALSE, NULL, 'GENERIC', FALSE, FALSE, '["express"]', '["kg","carton","pallet","chilled","frozen"]', '[null,"SYDNEY","BRISBANE",null,null,null]', FALSE, FALSE, NULL,1),
       ( 'cool_couriers', 'Cool Couriers', FALSE, NULL, 'GENERIC', FALSE, FALSE, '["road_freight","express"]', '["carton","pallet","chilled","frozen"]', '["MELBOURNE","SYDNEY",null,null,null,null]', FALSE, FALSE, NULL,1),
       ( 'gippsland_freight', 'Gippsland Freight', FALSE, NULL, 'GENERIC', FALSE, FALSE, '["road_freight","general_road"]', '["kg","skid","pallet","carton"]', '["2",null,null,null,null,null]', FALSE, FALSE, NULL,1),
       ( 'hi_trans', 'Hi Trans', TRUE, 'INSTANT', 'CUSTOM', FALSE, FALSE, '["general","express"]', '["kg","skid","pallet","carton"]', '["MEL","SYD","BNE","PER","ADE","GEE"]', FALSE, FALSE, NULL,1),
       ( 'hi_trans2', 'Hi Trans', TRUE, 'INSTANT', 'CUSTOM', FALSE, FALSE, '["general","express"]', '["kg","skid","pallet","carton"]', '["MEL","SYD","BNE","PER","ADE","GEE"]', FALSE, FALSE, NULL,1),
       ( 'pep_transport', 'PEP Transport', TRUE, 'INSTANT', 'CUSTOM', FALSE, FALSE, '["general","express"]', '["kg","skid","pallet","carton"]', '[null,"SYD",null,"Metro",null,null]', FALSE, FALSE, NULL,1),
       ( 'searoad', 'Searoad', FALSE, NULL, 'GENERIC', FALSE, FALSE, '["general"]', '["cubic"]', '["MEL","SYDNEY","BRISBANE",null,null,"GEELONG"]', FALSE, FALSE, NULL,1),
       ( 'sprint_freight', 'Sprint Freight', TRUE, 'INSTANT', 'GENERIC', FALSE, FALSE, '["general"]', '["kg"]', '["10","10","10","10","9","10"]', FALSE, FALSE, NULL,1),
       ( 'jna', 'JNA', FALSE, NULL, 'CUSTOM', FALSE, FALSE, '["road_express"]', '["kg"]', '["MEL","SYD","BNE","PTH","ADL","GEE"]', FALSE, FALSE, NULL,1),
       ( 'tfmxpress', 'TFMXpress', TRUE, 'INSTANT', 'CUSTOM', FALSE, FALSE, '["road_express"]', '["kg"]', '["MEL","SYD","BNE","PER","ADE","GEE"]', FALSE, FALSE, 'http://tfmxpress.com.au/track---trace.html',1),
       ( 'metrostate_security_courier', 'Metrostate Security Courier', TRUE, 'INSTANT', 'CUSTOM', FALSE, FALSE, '["general"]', '["kg","pallet"]', '[null,"1",null,null,null,null]', FALSE, TRUE, 'https://www.transvirtual.com.au/Track/42802/%CONNOTE_NUMBER%',1),
       ( 'first_express', 'First Express', TRUE, 'INSTANT', 'CUSTOM', FALSE, TRUE, '["general"]', '["kg","pallet"]', '["MEL","SYD","BNE","PER","ADL","VC1"]', FALSE, FALSE, NULL,1),
       ( 'freight_assist', 'Freight Assist', FALSE, NULL, 'CUSTOM', FALSE, TRUE, '["general"]', '["kg","pallet"]', '["MELBOURNE","SYDNEY","BRISBANE","PERTH","ADELAIDE","GEELONG"]', FALSE, FALSE, NULL,1),
       ( 'regional_freight_express', 'Regional Freight Express', FALSE, NULL, 'CUSTOM', FALSE, TRUE, '["general"]', '["kg","pallet"]', '["MELBOURNE","SYDNEY","BRISBANE","PERTH","ADELAIDE","GEELONG"]', FALSE, FALSE, NULL,1),
       ( 'blacks_transport', 'Blacks Transport', FALSE, NULL, 'GENERIC', FALSE, FALSE, '["general"]', '["kg","pallet"]', '["MELBOURNE","SYDNEY","BRISBANE","PERTH","ADELAIDE","GEELONG"]', FALSE, FALSE, NULL,1);

INSERT INTO carrier_account (account_number, carrier_id, user_broker_id, is_default, created_by)
SELECT 'ABCD',c.id, ub.id,0, 1
       FROM user_broker ub
CROSS JOIN carrier c
JOIN user u on ub.user_id = u.id
WHERE lower(u.username) = 'tuco';
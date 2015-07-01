
UPDATE T_MENU SET menu_name = '沃阅读短代',display_order= '2' WHERE id = '32' ;
UPDATE T_MENU SET menu_name = '查看订单', display_order= '1' WHERE id = '33' ;


insert into T_MENU (id, menu_name, display_order, parent_id, menu_level, sub_system, sub_module, url, status, remark, create_time)
values (35, '翼支付短验', 3, 31, 2, null, null, 'mall/YGPAY/buy', 1, null, to_date('16-06-2015', 'dd-mm-yyyy'));

commit;


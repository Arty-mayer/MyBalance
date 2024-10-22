package com.example.mybalance.Utils;

import java.util.List;
import java.util.Stack;

public class DbTableSQL {
    public static String[] table_currency_create = {
            "DROP TABLE IF EXISTS `Currency`",
            "CREATE TABLE `Currency` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `code` INTEGER NOT NULL, `char_code` TEXT, `symbol` TEXT, `name` TEXT, `name_de` TEXT, `name_ru` TEXT)"
    };
    public static String[] table_currency_date = {
            "INSERT INTO currency VALUES (1,8,'ALL','','Lek','Lek','Лек');",
            "INSERT INTO currency VALUES (2,12,'DZD','','Algerian Dinar','Algerischer Dinar','Алжирский динар');",
            "INSERT INTO currency VALUES (3,32,'ARS','','Argentine Peso','Argentinischer Peso','Аргентинский песо');",
            "INSERT INTO currency VALUES (4,36,'AUD','','Australian Dollar','Australischer Dollar','Австралийский доллар');",
            "INSERT INTO currency VALUES (5,44,'BSD','','Bahamian Dollar','Bahama-Dollar','Багамский доллар');",
            "INSERT INTO currency VALUES (6,48,'BHD','','Bahraini Dinar','Bahrain-Dinar','Бахрейнский динар');",
            "INSERT INTO currency VALUES (7,50,'BDT','','Taka','Taka','Така');",
            "INSERT INTO currency VALUES (8,51,'AMD','','Armenian Dram','Armenischer Dram','Армянский драм');",
            "INSERT INTO currency VALUES (9,52,'BBD','','Barbados Dollar','Barbados-Dollar','Барбадосский доллар');",
            "INSERT INTO currency VALUES (10,60,'BMD','','Bermudian Dollar','Bermudischer Dollar','Бермудский доллар');",
            "INSERT INTO currency VALUES (11,64,'BTN','','Ngultrum','Ngultrum','Нгултрум');",
            "INSERT INTO currency VALUES (12,68,'BOB','','Boliviano','Boliviano','Боливиано');",
            "INSERT INTO currency VALUES (13,72,'BWP','','Pula','Pula','Пула');",
            "INSERT INTO currency VALUES (14,84,'BZD','','Belize Dollar','Belize-Dollar','Белизский доллар');",
            "INSERT INTO currency VALUES (15,90,'SBD','','Solomon Islands Dollar','Dollar der Salomonen','Доллар Соломоновых Островов');",
            "INSERT INTO currency VALUES (16,96,'BND','','Brunei Dollar','Brunei-Dollar','Брунейский доллар');",
            "INSERT INTO currency VALUES (17,104,'MMK','','Kyat','Kyat','Кьят');",
            "INSERT INTO currency VALUES (18,108,'BIF','','Burundi Franc','Burundischer Franc','Бурундийский франк');",
            "INSERT INTO currency VALUES (19,116,'KHR','','Riel','Riel','Риель');",
            "INSERT INTO currency VALUES (20,124,'CAD','','Canadian Dollar','Kanadischer Dollar','Канадский доллар');",
            "INSERT INTO currency VALUES (21,132,'CVE','','Cabo Verde Escudo','Cabo Verde Escudo','Эскудо Кабо-Верде');",
            "INSERT INTO currency VALUES (22,136,'KYD','','Cayman Islands Dollar','Dollar der Kaimaninseln','Доллар Каймановых островов');",
            "INSERT INTO currency VALUES (23,144,'LKR','','Sri Lanka Rupee','Sri-Lanka-Rupie','Рупия Шри-Ланки');",
            "INSERT INTO currency VALUES (24,152,'CLP','','Chilean Peso','Chilenischer Peso','Чилийский песо');",
            "INSERT INTO currency VALUES (25,156,'CNY','','Yuan Renminbi','Yuan Renminbi','Юань Жэньминьби');",
            "INSERT INTO currency VALUES (26,170,'COP','','Colombian Peso','Kolumbianischer Peso','Колумбийский песо');",
            "INSERT INTO currency VALUES (27,174,'KMF','','Comoro Franc','Komoren-Franc','Коморский франк');",
            "INSERT INTO currency VALUES (28,188,'CRC','','Costa Rican Colon','Costa-Rica-Colón','Костариканский колон');",
            "INSERT INTO currency VALUES (29,192,'CUP','','Cuban Peso','Kubanischer Peso','Кубинский песо');",
            "INSERT INTO currency VALUES (30,203,'CZK','','Czech Koruna','Tschechische Krone','Чешская крона');",
            "INSERT INTO currency VALUES (31,208,'DKK','','Danish Krone','Dänische Krone','Датская крона');",
            "INSERT INTO currency VALUES (32,214,'DOP','','Dominican Peso','Dominikanischer Peso','Доминиканский песо');",
            "INSERT INTO currency VALUES (33,222,'SVC','','El Salvador Colon','Kolón von El Salvador','Колон Сальвадора');",
            "INSERT INTO currency VALUES (34,230,'ETB','','Ethiopian Birr','Äthiopischer Birr','Эфиопский быр');",
            "INSERT INTO currency VALUES (35,232,'ERN','','Nakfa','Nakfa','Накфа');",
            "INSERT INTO currency VALUES (36,238,'FKP','','Falkland Islands Pound','Falklandinseln-Pfund','Фунт Фолклендских островов');",
            "INSERT INTO currency VALUES (37,242,'FJD','','Fiji Dollar','Fidschi-Dollar','Доллар Фиджи');",
            "INSERT INTO currency VALUES (38,262,'DJF','','Djibouti Franc','Dschibuti-Franc','Джибутийский франк');",
            "INSERT INTO currency VALUES (39,270,'GMD','','Dalasi','Dalasi','Даласи');",
            "INSERT INTO currency VALUES (40,292,'GIP','','Gibraltar Pound','Gibraltar-Pfund','Гибралтарский фунт');",
            "INSERT INTO currency VALUES (41,320,'GTQ','','Quetzal','Quetzal','Кетсаль');",
            "INSERT INTO currency VALUES (42,324,'GNF','','Guinea Franc','Guineafranc','Гвинейский франк');",
            "INSERT INTO currency VALUES (43,328,'GYD','','Guyana Dollar','Guyana-Dollar','Гайанский доллар');",
            "INSERT INTO currency VALUES (44,332,'HTG','','Gourde','Gourde','Гурд');",
            "INSERT INTO currency VALUES (45,340,'HNL','','Lempira','Lempira','Лемпира');",
            "INSERT INTO currency VALUES (46,344,'HKD','','Hong Kong Dollar','Hongkong-Dollar','Гонконгский доллар');",
            "INSERT INTO currency VALUES (47,348,'HUF','','Forint','Forint','Форинт');",
            "INSERT INTO currency VALUES (48,352,'ISK','','Iceland Krona','Isländische Krone','Исландская крона');",
            "INSERT INTO currency VALUES (49,356,'INR','','Indian Rupee','Indische Rupie','Индийская рупия');",
            "INSERT INTO currency VALUES (50,360,'IDR','','Rupiah','Rupiah','Рупия');",
            "INSERT INTO currency VALUES (51,364,'IRR','','Iranian Rial','Iranscher Rial','Иранский риал');",
            "INSERT INTO currency VALUES (52,368,'IQD','','Iraqi Dinar','Iraker Dinar','Иракский динар');",
            "INSERT INTO currency VALUES (53,376,'ILS','','New Israeli Sheqel','Neuer Israelischer Schekel','Новый израильский шекель');",
            "INSERT INTO currency VALUES (54,388,'JMD','','Jamaican Dollar','Jamaika-Dollar','Ямайский доллар');",
            "INSERT INTO currency VALUES (55,392,'JPY','','Yen','Yen','Йена');",
            "INSERT INTO currency VALUES (56,398,'KZT','','Tenge','Tenge','Тенге');",
            "INSERT INTO currency VALUES (57,400,'JOD','','Jordanian Dinar','Jordanischer Dinar','Иорданский динар');",
            "INSERT INTO currency VALUES (58,404,'KES','','Kenyan Shilling','Kenianischer Schilling','Кенийский шиллинг');",
            "INSERT INTO currency VALUES (59,408,'KPW','','North Korean Won','Nordkoreanischer Won','Вона КНДР');",
            "INSERT INTO currency VALUES (60,410,'KRW','','Won','Won','Вона');",
            "INSERT INTO currency VALUES (61,414,'KWD','','Kuwaiti Dinar','Kuwait-Dinar','Кувейтский динар');",
            "INSERT INTO currency VALUES (62,417,'KGS','','Som','Som','Сом');",
            "INSERT INTO currency VALUES (63,418,'LAK','','Kip','Kip','Кип');",
            "INSERT INTO currency VALUES (64,422,'LBP','','Lebanese Pound','Libanesisches Pfund','Ливанский фунт');",
            "INSERT INTO currency VALUES (65,426,'LSL','','Loti','Loti','Лоти');",
            "INSERT INTO currency VALUES (66,430,'LRD','','Liberian Dollar','Liberianischer Dollar','Либерийский доллар');",
            "INSERT INTO currency VALUES (67,434,'LYD','','Libyan Dinar','Libyscher Dinar','Ливийский динар');",
            "INSERT INTO currency VALUES (68,446,'MOP','','Pataca','Pataca','Патака');",
            "INSERT INTO currency VALUES (69,454,'MWK','','Kwacha','Kwacha','Квача');",
            "INSERT INTO currency VALUES (70,458,'MYR','','Malaysian Ringgit','Malaysischer Ringgit','Малайзийский ринггит');",
            "INSERT INTO currency VALUES (71,462,'MVR','','Rufiyaa','Rufiyaa','Руфия');",
            "INSERT INTO currency VALUES (72,480,'MUR','','Mauritius Rupee','Mauritius-Rupie','Рупия Маврикия');",
            "INSERT INTO currency VALUES (73,484,'MXN','','Mexican Peso','Mexikanischer Peso','Мексиканский песо');",
            "INSERT INTO currency VALUES (74,496,'MNT','','Tugrik','Tugrik','Тугрик');",
            "INSERT INTO currency VALUES (75,498,'MDL','','Moldovan Leu','Moldawischer Leu','Молдавский лей');",
            "INSERT INTO currency VALUES (76,504,'MAD','','Moroccan Dirham','Marokkanischer Dirham','Марокканский дирхам');",
            "INSERT INTO currency VALUES (77,512,'OMR','','Rial Omani','Omanischer Rial','Оманский риал');",
            "INSERT INTO currency VALUES (78,516,'NAD','','Namibia Dollar','Namibia-Dollar','Намибийский доллар');",
            "INSERT INTO currency VALUES (79,524,'NPR','','Nepalese Rupee','Nepalese Rupie','Непальская рупия');",
            "INSERT INTO currency VALUES (80,532,'ANG','','Netherlands Antillean Guilder','Niederländischer Antillengulden','Нидерландский антильский гульден');",
            "INSERT INTO currency VALUES (81,533,'AWG','','Aruban Florin','Aruba-Florin','Арубанский флорин');",
            "INSERT INTO currency VALUES (82,548,'VUV','','Vatu','Vatu','Вату');",
            "INSERT INTO currency VALUES (83,554,'NZD','','New Zealand Dollar','Neuseeland-Dollar','Новозеландский доллар');",
            "INSERT INTO currency VALUES (84,558,'NIO','','Cordoba Oro','Cordoba Oro','Кордоба Оро');",
            "INSERT INTO currency VALUES (85,566,'NGN','','Naira','Naira','Найра');",
            "INSERT INTO currency VALUES (86,578,'NOK','','Norwegian Krone','Norwegische Krone','Норвежская крона');",
            "INSERT INTO currency VALUES (87,586,'PKR','','Pakistan Rupee','Pakistanische Rupie','Пакистанская рупия');",
            "INSERT INTO currency VALUES (88,590,'PAB','','Balboa','Balboa','Бальбоа');",
            "INSERT INTO currency VALUES (89,598,'PGK','','Kina','Kina','Кина');",
            "INSERT INTO currency VALUES (90,600,'PYG','','Guarani','Guarani','Гуарани');",
            "INSERT INTO currency VALUES (91,604,'PEN','','Nuevo Sol','Nuevo Sol','Новый соль');",
            "INSERT INTO currency VALUES (92,608,'PHP','','Philippine Peso','Philippinischer Peso','Филиппинский песо');",
            "INSERT INTO currency VALUES (93,634,'QAR','','Qatari Rial','Katar-Rial','Катарский риал');",
            "INSERT INTO currency VALUES (94,643,'RUB','₽','Russian Ruble','Russischer Rubel','Российский рубль');",
            "INSERT INTO currency VALUES (95,646,'RWF','','Rwanda Franc','Ruanda-Franc','Рвандийский франк');",
            "INSERT INTO currency VALUES (96,654,'SHP','','Saint Helena Pound','Pfund von St. Helena','Фунт острова Святой Елены');",
            "INSERT INTO currency VALUES (97,682,'SAR','','Saudi Riyal','Saudi-Rial','Саудовский риял');",
            "INSERT INTO currency VALUES (98,690,'SCR','','Seychelles Rupee','Seychellen-Rupie','Сейшельская рупия');",
            "INSERT INTO currency VALUES (99,702,'SGD','','Singapore Dollar','Singapur-Dollar','Сингапурский доллар');",
            "INSERT INTO currency VALUES (100,704,'VND','','Dong','Dong','Донг');",
            "INSERT INTO currency VALUES (101,706,'SOS','','Somali Shilling','Somalischer Schilling','Сомалийский шиллинг');",
            "INSERT INTO currency VALUES (102,710,'ZAR','','Rand','Rand','Ранд');",
            "INSERT INTO currency VALUES (103,728,'SSP','','South Sudanese Pound','Südsudanesisches Pfund','Южносуданский фунт');",
            "INSERT INTO currency VALUES (104,748,'SZL','','Lilangeni','Lilangeni','Лилангени');",
            "INSERT INTO currency VALUES (105,752,'SEK','','Swedish Krona','Schwedische Krone','Шведская крона');",
            "INSERT INTO currency VALUES (106,756,'CHF','','Swiss Franc','Schweizer Franken','Швейцарский франк');",
            "INSERT INTO currency VALUES (107,760,'SYP','','Syrian Pound','Syrisches Pfund','Сирийский фунт');",
            "INSERT INTO currency VALUES (108,764,'THB','','Baht','Baht','Бат');",
            "INSERT INTO currency VALUES (109,776,'TOP','','Pa’anga','Pa’anga','Паанга');",
            "INSERT INTO currency VALUES (110,780,'TTD','','Trinidad and Tobago Dollar','Dollar von Trinidad und Tobago','Доллар Тринидада и Тобаго');",
            "INSERT INTO currency VALUES (111,784,'AED','','UAE Dirham','VAE-Dirham','Дирхам ОАЭ');",
            "INSERT INTO currency VALUES (112,788,'TND','','Tunisian Dinar','Tunesischer Dinar','Тунисский динар');",
            "INSERT INTO currency VALUES (113,800,'UGX','','Uganda Shilling','Ugandischer Schilling','Угандийский шиллинг');",
            "INSERT INTO currency VALUES (114,807,'MKD','','Denar','Denar','Денар');",
            "INSERT INTO currency VALUES (115,818,'EGP','','Egyptian Pound','Ägyptisches Pfund','Египетский фунт');",
            "INSERT INTO currency VALUES (116,826,'GBP','','Pound Sterling','Pfund Sterling','Фунт стерлингов');",
            "INSERT INTO currency VALUES (117,834,'TZS','','Tanzanian Shilling','Tansanischer Schilling','Танзанийский шиллинг');",
            "INSERT INTO currency VALUES (118,840,'USD','$','US Dollar','US-Dollar','Доллар США');",
            "INSERT INTO currency VALUES (119,858,'UYU','','Peso Uruguayo','Uruguayer Peso','Уругвайский песо');",
            "INSERT INTO currency VALUES (120,860,'UZS','','Uzbekistan Sum','Usbekistan-Sum','Узбекский сум');",
            "INSERT INTO currency VALUES (121,882,'WST','','Tala','Tala','Тала');",
            "INSERT INTO currency VALUES (122,886,'YER','','Yemeni Rial','Jemen-Rial','Йеменский риал');",
            "INSERT INTO currency VALUES (123,901,'TWD','','New Taiwan Dollar','Neuer Taiwan-Dollar','Новый тайваньский доллар');",
            "INSERT INTO currency VALUES (124,925,'SLE','','Leone','Leone','Леоне');",
            "INSERT INTO currency VALUES (125,926,'VED','','Bolivar','Bolivar','Боливар');",
            "INSERT INTO currency VALUES (126,929,'MRU','','Ouguiya','Ouguiya','Угия');",
            "INSERT INTO currency VALUES (127,930,'STN','','Dobra','Dobra','Добра');",
            "INSERT INTO currency VALUES (128,931,'CUC','','Peso Convertible','Peso Convertible','Конвертируемый песо');",
            "INSERT INTO currency VALUES (129,932,'ZWL','','Zimbabwe Dollar','Simbabwe-Dollar','Зимбабвийский доллар');",
            "INSERT INTO currency VALUES (130,933,'BYN','','Belarussian Ruble','Belarussischer Rubel','Белорусский рубль');",
            "INSERT INTO currency VALUES (131,934,'TMT','','Turkmenistan New Manat','Turkmenistan Neuer Manat','Туркменский новый манат');",
            "INSERT INTO currency VALUES (132,936,'GHS','','Ghana Cedi','Ghana-Cedi','Ганский седи');",
            "INSERT INTO currency VALUES (133,937,'VEF','','Bolivar','Bolivar','Боливар');",
            "INSERT INTO currency VALUES (134,938,'SDG','','Sudanese Pound','Sudanesisches Pfund','Суданский фунт');",
            "INSERT INTO currency VALUES (135,940,'UYI','','Uruguay Peso (URUIURUI)','Uruguayer Peso (URUIURUI)','Песо Уругвая (URUIURUI)');",
            "INSERT INTO currency VALUES (136,941,'RSD','','Serbian Dinar','Serbischer Dinar','Сербский динар');",
            "INSERT INTO currency VALUES (137,943,'MZN','','Mozambique Metical','Mosambikanischer Metical','Мозамбикский метикал');",
            "INSERT INTO currency VALUES (138,944,'AZN','','Azerbaijanian Manat','Aserbaidschanischer Manat','Азербайджанский манат');",
            "INSERT INTO currency VALUES (139,946,'RON','','Romanian Leu','Rumänischer Leu','Румынский лей');",
            "INSERT INTO currency VALUES (140,947,'CHE','','WIR Euro','WIR-Euro','WIR евро');",
            "INSERT INTO currency VALUES (141,948,'CHW','','WIR Franc','WIR-Franc','WIR франк');",
            "INSERT INTO currency VALUES (142,949,'TRY','','Turkish Lira','Türkische Lira','Турецкая лира');",
            "INSERT INTO currency VALUES (143,950,'XAF','','CFA Franc BEAC','CFA-Franc BEAC','Франк КФА BEAC');",
            "INSERT INTO currency VALUES (144,951,'XCD','','East Caribbean Dollar','Ostkaribischer Dollar','Восточно-карибский доллар');",
            "INSERT INTO currency VALUES (145,952,'XOF','','CFA Franc BCEAO','CFA-Franc BCEAO','Франк КФА BCEAO');",
            "INSERT INTO currency VALUES (146,953,'XPF','','CFP Franc','CFP-Franc','Франк КФП');",
            "INSERT INTO currency VALUES (147,960,'XDR','','SDR (Special Drawing Right)','Sonderziehungsrecht (SDR)','СПЗ (Специальные права заимствования)');",
            "INSERT INTO currency VALUES (148,965,'XUA','','ADB Unit of Account','ADB Rechnungseinheit','Единица счета АБР');",
            "INSERT INTO currency VALUES (149,967,'ZMW','','Zambian Kwacha','Sambische Kwacha','Замбийская квача');",
            "INSERT INTO currency VALUES (150,968,'SRD','','Surinam Dollar','Surinam-Dollar','Суринамский доллар');",
            "INSERT INTO currency VALUES (151,969,'MGA','','Malagasy Ariary','Madagassischer Ariary','Малагасийский ариари');",
            "INSERT INTO currency VALUES (152,970,'COU','','Unidad de Valor Real','Einheit des Realwerts','Единица реальной стоимости');",
            "INSERT INTO currency VALUES (153,971,'AFN','','Afghani','Afghani','Афгани');",
            "INSERT INTO currency VALUES (154,972,'TJS','','Somoni','Somoni','Сомони');",
            "INSERT INTO currency VALUES (155,973,'AOA','','Kwanza','Kwanza','Кванза');",
            "INSERT INTO currency VALUES (156,975,'BGN','','Bulgarian Lev','Bulgarischer Lew','Болгарский лев');",
            "INSERT INTO currency VALUES (157,976,'CDF','','Congolese Franc','Kongolesischer Franc','Конголезский франк');",
            "INSERT INTO currency VALUES (158,977,'BAM','','Convertible Mark','Konvertible Mark','Конвертируемая марка');",
            "INSERT INTO currency VALUES (159,978,'EUR','€','Euro','Euro','Евро');",
            "INSERT INTO currency VALUES (160,979,'MXV','','Mexican Unidad de Inversion (UDI)','Mexikanische Investitionseinheit (UDI)','Мексиканская единица учета (UDI)');",
            "INSERT INTO currency VALUES (161,980,'UAH','','Hryvnia','Hryvnia','Гривна');",
            "INSERT INTO currency VALUES (162,981,'GEL','','Lari','Lari','Лари');",
            "INSERT INTO currency VALUES (163,984,'BOV','','Mvdol','Mvdol','МВдол');",
            "INSERT INTO currency VALUES (164,985,'PLN','','Zloty','Zloty','Злотый');",
            "INSERT INTO currency VALUES (165,986,'BRL','','Brazilian Real','Brasilianischer Real','Бразильский реал');",
            "INSERT INTO currency VALUES (166,990,'CLF','','Unidad de Fomento','Entwicklungswert','Единица развития');",
            "INSERT INTO currency VALUES (167,994,'XSU','','Sucre','Sucre','Сукре');"
            };

    public static String[] migration_2_3_DBUpdate = {
            "ALTER TABLE Accounts ADD COLUMN currencyId INTEGER",
            "UPDATE Accounts SET currencyId = 0",
            "CREATE TABLE Accounts_temp ("
                    + "id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, "
                    + "name TEXT, "
                    + "description TEXT, "
                    + "amount REAL NOT NULL, "
                    + "lastIncomeDate TEXT, "
                    + "lastIncomeAmount REAL NOT NULL, "
                    + "lastExpensesDate TEXT, "
                    + "lastExpensesAmount REAL NOT NULL, "
                    + "currencyId INTEGER NOT NULL DEFAULT 0)",
            "INSERT INTO Accounts_temp (id, name, description, amount, lastIncomeDate, lastIncomeAmount, lastExpensesDate, lastExpensesAmount, currencyId) "
                    + "SELECT id, name, description, amount, lastIncomeDate, lastIncomeAmount, lastExpensesDate, lastExpensesAmount, currencyId FROM Accounts",
            "DROP TABLE Accounts",
            "ALTER TABLE Accounts_temp RENAME TO Accounts"
    };
}

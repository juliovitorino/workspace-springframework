

CREATE TABLE public.user_punter (
	id_user_punter bigserial NOT NULL,
	nickname varchar(255) NOT NULL,
	btc_address varchar(255) NOT NULL,
	status varchar(1) NULL,
	date_updated timestamp NULL,
	date_created timestamp NULL,
	CONSTRAINT user_punter_pkey PRIMARY KEY (id_user_punter)
);

CREATE TABLE public.bet_object (
	id_bet_object bigserial NOT NULL,
	who varchar(255) NULL,
	externaluuid uuid NULL,
	jackpot float8 NULL,
	jackpot_pending float8 NULL,
	status varchar(1) NULL,
	date_created timestamp NULL,
	date_updated timestamp NULL,
	CONSTRAINT bet_object_pkey PRIMARY KEY (id_bet_object)
);

CREATE TABLE public.bet (
	id_bet bigserial NOT NULL,
	id_punter int8 NULL,
	id_bet_object int8 NULL,
	ticket uuid NULL,
	btc_address varchar(255) NULL,
	bet float8 NULL,
	death_date timestamp NULL,
	status varchar(1) NULL,
	date_updated timestamp NULL,
	date_created timestamp NULL,
	CONSTRAINT bet_pkey PRIMARY KEY (id_bet)
);

CREATE TABLE public.jackpot_history (
	id_jackpot bigserial NOT NULL,
	type varchar(1) NOT NULL,
	description varchar(255) NOT NULL,
	bet_value float8 NOT NULL,
	id_punter int8 NOT NULL,
	ticket uuid NOT NULL,
	status varchar(1) NULL,
	date_created timestamp NULL,
	date_updated timestamp NULL,
	CONSTRAINT jackpot_history_pkey PRIMARY KEY (id_jackpot)
);


CREATE TABLE public.user_punter (
	id_user_punter bigserial NOT NULL,
	nickname varchar(255) NOT NULL,
	btc_address varchar(255) NOT NULL,
	status varchar(1) NOT NULL,
	date_updated timestamp NOT NULL,
	date_created timestamp NOT NULL,
	CONSTRAINT user_punter_pkey PRIMARY KEY (id_user_punter)
);

CREATE TABLE public.bet_object (
	id_bet_object bigserial NOT NULL,
	who varchar(255) NOT NULL,
	externaluuid uuid NOT NULL,
	jackpot float8 default 0 NOT NULL,
	jackpot_pending float8 default 0 NOT NULL,
	status varchar(1) NOT NULL,
	date_created timestamp NOT NULL,
	date_updated timestamp NOT NULL,
	CONSTRAINT bet_object_pkey PRIMARY KEY (id_bet_object)
);

CREATE TABLE public.bet (
	id_bet bigserial NOT NULL,
	id_punter int8 NOT NULL,
	id_bet_object int8 NOT NULL,
	ticket uuid NOT NULL,
	btc_address varchar(255) NOT NULL,
	bet float8 NOT NULL,
	death_date date NOT NULL,
	status varchar(1) NOT NULL,
	date_updated timestamp NOT NULL,
	date_created timestamp NOT NULL,
	CONSTRAINT bet_pkey PRIMARY KEY (id_bet)
);


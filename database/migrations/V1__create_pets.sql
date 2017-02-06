create table "pets" (
  "id" uuid not null,
  "name" VARCHAR(255) not null,
  "strength" int not null,
  "speed" int not null,
  "intelligence" int not null,
  "integrity" int not null
);

CREATE UNIQUE INDEX name_unique_idx ON pets (name);
DROP TABLE vet_specialties IF EXISTS;
DROP TABLE vets IF EXISTS;
DROP TABLE specialties IF EXISTS;
DROP TABLE visits IF EXISTS;
DROP TABLE companies IF EXISTS;
DROP TABLE types IF EXISTS;
DROP TABLE positions IF EXISTS;


CREATE TABLE vets (
  id         INTEGER IDENTITY PRIMARY KEY,
  area VARCHAR(50),
  position  VARCHAR(100)
);
CREATE INDEX vets_position ON vets (position);

CREATE TABLE specialties (
  id   INTEGER IDENTITY PRIMARY KEY,
  name VARCHAR(80)
);
CREATE INDEX specialties_name ON specialties (name);

CREATE TABLE vet_specialties (
  vet_id       INTEGER NOT NULL,
  specialty_id INTEGER NOT NULL
);
ALTER TABLE vet_specialties ADD CONSTRAINT fk_vet_specialties_vets FOREIGN KEY (vet_id) REFERENCES vets (id);
ALTER TABLE vet_specialties ADD CONSTRAINT fk_vet_specialties_specialties FOREIGN KEY (specialty_id) REFERENCES specialties (id);

CREATE TABLE types (
  id   INTEGER IDENTITY PRIMARY KEY,
  name VARCHAR(80)
);
CREATE INDEX types_name ON types (name);

CREATE TABLE positions (
  id         INTEGER IDENTITY PRIMARY KEY,
  area VARCHAR(50),
  position  VARCHAR_IGNORECASE(100),
  start_date DATE,
  end_date DATE
);
CREATE INDEX positions_position ON positions (position);

CREATE TABLE companies (
  id         INTEGER IDENTITY PRIMARY KEY,
  name       VARCHAR(30),
  birth_date DATE,
  type_id    INTEGER NOT NULL,
  position_id   INTEGER NOT NULL
);
ALTER TABLE companies ADD CONSTRAINT fk_companies_positions FOREIGN KEY (position_id) REFERENCES positions (id);
ALTER TABLE companies ADD CONSTRAINT fk_companies_types FOREIGN KEY (type_id) REFERENCES types (id);
CREATE INDEX companies_name ON companies (name);

CREATE TABLE visits (
  id          INTEGER IDENTITY PRIMARY KEY,
  company_id      INTEGER NOT NULL,
  visit_date  DATE,
  description VARCHAR(255)
);
ALTER TABLE visits ADD CONSTRAINT fk_visits_companies FOREIGN KEY (company_id) REFERENCES companies (id);
CREATE INDEX visits_company_id ON visits (company_id);

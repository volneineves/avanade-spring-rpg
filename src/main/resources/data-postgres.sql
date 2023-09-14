INSERT INTO characters (id, name, type, health, strength, defense, agility, num_dice, faces)
VALUES ('a8a767f8-435f-4d67-a77a-564ad4ee891a', 'Guerreiro', 'HERO', 20, 7, 5, 6, 1, 'D12')
ON CONFLICT (id) DO NOTHING;

INSERT INTO characters (id, name, type, health, strength, defense, agility, num_dice, faces)
VALUES ('2d2e026b-ded5-48d0-8b5a-244d3fc5e315', 'Bárbaro', 'HERO', 21, 10, 2, 5, 2, 'D8')
ON CONFLICT (id) DO NOTHING;

INSERT INTO characters (id, name, type, health, strength, defense, agility, num_dice, faces)
VALUES ('6eb211d4-77b9-403e-b42d-bb051c3736cf', 'Cavaleiro', 'HERO', 26, 6, 8, 3, 2, 'D6')
ON CONFLICT (id) DO NOTHING;

INSERT INTO characters (id, name, type, health, strength, defense, agility, num_dice, faces)
VALUES ('f9302aa0-d14e-4a58-b132-1eb11dfea5ec', 'Orc', 'MONSTER', 42, 7, 1, 2, 3, 'D4')
ON CONFLICT (id) DO NOTHING;

INSERT INTO characters (id, name, type, health, strength, defense, agility, num_dice, faces)
VALUES ('a3d9f3cb-9bb6-40e1-b0bd-562e1fd2f3a4', 'Gigante', 'MONSTER', 34, 10, 4, 4, 2, 'D6')
ON CONFLICT (id) DO NOTHING;

INSERT INTO characters (id, name, type, health, strength, defense, agility, num_dice, faces)
VALUES ('0ae5b7e1-e87c-4a6e-b7e3-e05039a0f661', 'Lobisomen', 'MONSTER', 34, 7, 4, 7, 2, 'D4')
ON CONFLICT (id) DO NOTHING;

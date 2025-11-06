-- ============================================
-- Dungeons & Dragons Database Schema
-- ============================================

-- Create the database (run this via API or console first)
-- CREATE DATABASE DungeonAndDragons;

-- ============================================
-- VERTEX TYPES
-- ============================================

-- Room Vertex Type
CREATE VERTEX TYPE Room IF NOT EXISTS;
CREATE PROPERTY Room.name STRING;
CREATE PROPERTY Room.description STRING;
CREATE PROPERTY Room.size STRING;
CREATE PROPERTY Room.lightLevel STRING;
CREATE INDEX ON Room (name) UNIQUE;

-- Monster Vertex Type
CREATE VERTEX TYPE Monster IF NOT EXISTS;
CREATE PROPERTY Monster.name STRING;
CREATE PROPERTY Monster.cr INTEGER;  -- Challenge Rating
CREATE PROPERTY Monster.xp INTEGER;  -- Experience Points
CREATE PROPERTY Monster.hp INTEGER;  -- Hit Points
CREATE PROPERTY Monster.ac INTEGER;  -- Armor Class
CREATE PROPERTY Monster.type STRING; -- Beast, Dragon, Humanoid, etc.
CREATE INDEX ON Monster (name) NOTUNIQUE;

-- Treasure Vertex Type
CREATE VERTEX TYPE Treasure IF NOT EXISTS;
CREATE PROPERTY Treasure.name STRING;
CREATE PROPERTY Treasure.gp INTEGER;     -- Gold Pieces value
CREATE PROPERTY Treasure.type STRING;    -- coins, weapon, armor, magic item
CREATE PROPERTY Treasure.rarity STRING;  -- common, uncommon, rare, legendary
CREATE PROPERTY Treasure.description STRING;
CREATE INDEX ON Treasure (name) NOTUNIQUE;

-- ============================================
-- EDGE TYPES
-- ============================================

-- Room connects to Room (navigation between rooms)
CREATE EDGE TYPE ConnectsTo IF NOT EXISTS;
CREATE PROPERTY ConnectsTo.direction STRING; -- north, south, east, west
CREATE PROPERTY ConnectsTo.doorType STRING;  -- open, locked, secret, trapped
CREATE PROPERTY ConnectsTo.distance INTEGER; -- distance in feet

-- Monster is located in Room
CREATE EDGE TYPE LocatedIn IF NOT EXISTS;
CREATE PROPERTY LocatedIn.quantity INTEGER;  -- number of monsters
CREATE PROPERTY LocatedIn.behavior STRING;   -- aggressive, passive, guarding

-- Treasure is stored in Room
CREATE EDGE TYPE StoredIn IF NOT EXISTS;
CREATE PROPERTY StoredIn.hidden BOOLEAN;     -- is treasure hidden?
CREATE PROPERTY StoredIn.containerType STRING; -- chest, pile, vault, etc.

-- Monster guards Treasure
CREATE EDGE TYPE Guards IF NOT EXISTS;
CREATE PROPERTY Guards.awareness STRING;     -- aware, unaware
CREATE PROPERTY Guards.priority INTEGER;     -- 1-10, how important

-- ============================================
-- Sample Data
-- ============================================

-- Create Rooms
CREATE VERTEX Room SET name = 'Statue Room', description = 'A grand hall with ancient statues', size = 'large', lightLevel = 'dim';
CREATE VERTEX Room SET name = 'Barracks', description = 'Old living quarters with bunk beds', size = 'medium', lightLevel = 'dark';
CREATE VERTEX Room SET name = 'Armoury', description = 'Weapon racks line the walls', size = 'medium', lightLevel = 'dark';

-- Create Monsters
CREATE VERTEX Monster SET name = 'Alice the Elf', cr = 7, xp = 350, hp = 45, ac = 15, type = 'Humanoid';
CREATE VERTEX Monster SET name = 'Bob the Ogre', cr = 5, xp = 250, hp = 59, ac = 11, type = 'Giant';

-- Create Treasures
CREATE VERTEX Treasure SET name = 'coins', gp = 250, type = 'currency', rarity = 'common', description = 'A pile of gold coins';
CREATE VERTEX Treasure SET name = '+1 sword', gp = 1250, type = 'weapon', rarity = 'uncommon', description = 'A finely crafted longsword with magical properties';

-- Create Edges: Room to Room connections
CREATE EDGE ConnectsTo FROM (SELECT FROM Room WHERE name = 'Statue Room') TO (SELECT FROM Room WHERE name = 'Barracks') SET direction = 'south', doorType = 'open', distance = 30;
CREATE EDGE ConnectsTo FROM (SELECT FROM Room WHERE name = 'Barracks') TO (SELECT FROM Room WHERE name = 'Armoury') SET direction = 'west', doorType = 'locked', distance = 20;
CREATE EDGE ConnectsTo FROM (SELECT FROM Room WHERE name = 'Statue Room') TO (SELECT FROM Room WHERE name = 'Armoury') SET direction = 'southwest', doorType = 'open', distance = 40;

-- Create Edges: Monster in Room
CREATE EDGE LocatedIn FROM (SELECT FROM Monster WHERE name = 'Alice the Elf') TO (SELECT FROM Room WHERE name = 'Statue Room') SET quantity = 1, behavior = 'guarding';
CREATE EDGE LocatedIn FROM (SELECT FROM Monster WHERE name = 'Bob the Ogre') TO (SELECT FROM Room WHERE name = 'Barracks') SET quantity = 1, behavior = 'aggressive';

-- Create Edges: Treasure in Room
CREATE EDGE StoredIn FROM (SELECT FROM Treasure WHERE name = 'coins') TO (SELECT FROM Room WHERE name = 'Armoury') SET hidden = false, containerType = 'pile';
CREATE EDGE StoredIn FROM (SELECT FROM Treasure WHERE name = '+1 sword') TO (SELECT FROM Room WHERE name = 'Barracks') SET hidden = true, containerType = 'chest';

-- Create Edges: Monster guards Treasure
CREATE EDGE Guards FROM (SELECT FROM Monster WHERE name = 'Bob the Ogre') TO (SELECT FROM Treasure WHERE name = '+1 sword') SET awareness = 'aware', priority = 8;

-- ============================================
-- Useful Queries
-- ============================================

-- Find all rooms connected to Statue Room
-- SELECT expand(out('ConnectsTo')) FROM Room WHERE name = 'Statue Room';

-- Find all monsters in a specific room
-- SELECT expand(in('LocatedIn')) FROM Room WHERE name = 'Statue Room';

-- Find all treasures guarded by monsters
-- SELECT expand(out('Guards')) FROM Monster;

-- Find the path from one room to another
-- SELECT expand(shortestPath(@rid, (SELECT FROM Room WHERE name = 'Armoury')[0], 'ConnectsTo')) FROM Room WHERE name = 'Statue Room';

-- Find total gold value in a room
-- SELECT sum(gp) as totalGold FROM (SELECT expand(in('StoredIn')) FROM Room WHERE name = 'Barracks');
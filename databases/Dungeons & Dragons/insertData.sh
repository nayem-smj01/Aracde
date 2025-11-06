#!/bin/bash

# ============================================
# Complete Dungeon & Dragons Data Setup
# Using REST API with cURL
# ============================================

BASE_URL="http://localhost:8086/api/v1"

echo "🏰 Creating Dungeons & Dragons Dungeon..."
echo ""

# ============================================
# STEP 1: CREATE ROOMS
# ============================================

echo "📍 Creating Rooms..."

# Create Statue Room
echo "Creating Statue Room..."
STATUE_ROOM=$(curl -s -X POST "$BASE_URL/rooms" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Statue Room",
    "description": "A grand hall with ancient statues",
    "size": "large",
    "lightLevel": "dim"
  }' )

echo "✓ Statue Room created: $STATUE_ROOM"

# Create Barracks
echo "Creating Barracks..."
BARRACKS=$(curl -s -X POST "$BASE_URL/rooms" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Barracks",
    "description": "Old living quarters with bunk beds",
    "size": "medium",
    "lightLevel": "dark"
  }' )

echo "✓ Barracks created: $BARRACKS"

# Create Armoury
echo "Creating Armoury..."
ARMOURY=$(curl -s -X POST "$BASE_URL/rooms" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Armoury",
    "description": "Weapon racks line the walls",
    "size": "medium",
    "lightLevel": "dark"
  }' )

echo "✓ Armoury created: $ARMOURY"
echo ""

# ============================================
# STEP 2: CREATE MONSTERS
# ============================================

echo "👹 Creating Monsters..."

# Create Alice the Elf
echo "Creating Alice the Elf..."
ALICE=$(curl -s -X POST "$BASE_URL/monsters" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Alice the Elf",
    "cr": 7,
    "xp": 350,
    "hp": 45,
    "ac": 15,
    "type": "Humanoid"
  }' )

echo "✓ Alice the Elf created: $ALICE"

# Create Bob the Ogre
echo "Creating Bob the Ogre..."
BOB=$(curl -s -X POST "$BASE_URL/monsters" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Bob the Ogre",
    "cr": 5,
    "xp": 250,
    "hp": 59,
    "ac": 11,
    "type": "Giant"
  }' )

echo "✓ Bob the Ogre created: $BOB"
echo ""

# ============================================
# STEP 3: CREATE TREASURES
# ============================================

echo "💰 Creating Treasures..."

# Create Coins
echo "Creating coins treasure..."
COINS=$(curl -s -X POST "$BASE_URL/treasures" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "coins",
    "gp": 250,
    "type": "currency",
    "rarity": "common",
    "description": "A pile of gold coins"
  }' )

echo "✓ Coins created: $COINS"

# Create +1 Sword
echo "Creating +1 sword treasure..."
SWORD=$(curl -s -X POST "$BASE_URL/treasures" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "+1 sword",
    "gp": 1250,
    "type": "weapon",
    "rarity": "uncommon",
    "description": "A finely crafted longsword with magical properties"
  }' )

echo "✓ +1 Sword created: $SWORD"
echo ""

# ============================================
# STEP 4: CONNECT ROOMS
# ============================================

echo "🚪 Connecting Rooms..."

# Statue Room -> Barracks
echo "Connecting Statue Room to Barracks (south)..."
curl -s -X POST "$BASE_URL/edges/connect-rooms" \
  -H "Content-Type: application/json" \
  -d "{
    \"fromRoomId\": \"$STATUE_ROOM\",
    \"toRoomId\": \"$BARRACKS\",
    \"direction\": \"south\",
    \"doorType\": \"open\",
    \"distance\": 30
  }" > /dev/null

echo "✓ Statue Room -> Barracks connected"

# Barracks -> Armoury
echo "Connecting Barracks to Armoury (west)..."
curl -s -X POST "$BASE_URL/edges/connect-rooms" \
  -H "Content-Type: application/json" \
  -d "{
    \"fromRoomId\": \"$BARRACKS\",
    \"toRoomId\": \"$ARMOURY\",
    \"direction\": \"west\",
    \"doorType\": \"locked\",
    \"distance\": 20
  }" > /dev/null

echo "✓ Barracks -> Armoury connected"

# Statue Room -> Armoury
echo "Connecting Statue Room to Armoury (southwest)..."
curl -s -X POST "$BASE_URL/edges/connect-rooms" \
  -H "Content-Type: application/json" \
  -d "{
    \"fromRoomId\": \"$STATUE_ROOM\",
    \"toRoomId\": \"$ARMOURY\",
    \"direction\": \"southwest\",
    \"doorType\": \"open\",
    \"distance\": 40
  }" > /dev/null

echo "✓ Statue Room -> Armoury connected"
echo ""

# ============================================
# STEP 5: PLACE MONSTERS IN ROOMS
# ============================================

echo "👾 Placing Monsters in Rooms..."

# Alice in Statue Room
echo "Placing Alice the Elf in Statue Room..."
curl -s -X POST "$BASE_URL/edges/place-monster" \
  -H "Content-Type: application/json" \
  -d "{
    \"monsterId\": \"$ALICE\",
    \"roomId\": \"$STATUE_ROOM\",
    \"quantity\": 1,
    \"behavior\": \"guarding\"
  }" > /dev/null

echo "✓ Alice placed in Statue Room"

# Bob in Barracks
echo "Placing Bob the Ogre in Barracks..."
curl -s -X POST "$BASE_URL/edges/place-monster" \
  -H "Content-Type: application/json" \
  -d "{
    \"monsterId\": \"$BOB\",
    \"roomId\": \"$BARRACKS\",
    \"quantity\": 1,
    \"behavior\": \"aggressive\"
  }" > /dev/null

echo "✓ Bob placed in Barracks"
echo ""

# ============================================
# STEP 6: STORE TREASURES IN ROOMS
# ============================================

echo "🗝️ Storing Treasures in Rooms..."

# Coins in Armoury
echo "Storing coins in Armoury..."
curl -s -X POST "$BASE_URL/edges/store-treasure" \
  -H "Content-Type: application/json" \
  -d "{
    \"treasureId\": \"$COINS\",
    \"roomId\": \"$ARMOURY\",
    \"hidden\": false,
    \"containerType\": \"pile\"
  }" > /dev/null

echo "✓ Coins stored in Armoury"

# +1 Sword in Barracks
echo "Storing +1 sword in Barracks..."
curl -s -X POST "$BASE_URL/edges/store-treasure" \
  -H "Content-Type: application/json" \
  -d "{
    \"treasureId\": \"$SWORD\",
    \"roomId\": \"$BARRACKS\",
    \"hidden\": true,
    \"containerType\": \"chest\"
  }" > /dev/null

echo "✓ +1 Sword stored in Barracks"
echo ""

# ============================================
# STEP 7: MONSTERS GUARD TREASURES
# ============================================

echo "🛡️ Setting up Guards..."

# Bob guards the +1 Sword
echo "Setting Bob to guard the +1 sword..."
curl -s -X POST "$BASE_URL/edges/monster-guards" \
  -H "Content-Type: application/json" \
  -d "{
    \"monsterId\": \"$BOB\",
    \"treasureId\": \"$SWORD\",
    \"awareness\": \"aware\",
    \"priority\": 8
  }" > /dev/null

echo "✓ Bob is now guarding the +1 sword"
echo ""

# ============================================
# SUMMARY
# ============================================

echo "═══════════════════════════════════════════"
echo "🎉 Dungeon Creation Complete!"
echo "═══════════════════════════════════════════"
echo ""
echo "📊 Created Entities:"
echo "  • 3 Rooms"
echo "  • 2 Monsters"
echo "  • 2 Treasures"
echo "  • 3 Room connections"
echo "  • 2 Monster placements"
echo "  • 2 Treasure locations"
echo "  • 1 Guard assignment"
echo ""
echo "🔍 Verify the data:"
echo "  Rooms:     curl $BASE_URL/rooms"
echo "  Monsters:  curl $BASE_URL/monsters"
echo "  Treasures: curl $BASE_URL/treasures"
echo ""
echo "🗺️ Entity IDs:"
echo "  Statue Room: $STATUE_ROOM"
echo "  Barracks:    $BARRACKS"
echo "  Armoury:     $ARMOURY"
echo "  Alice:       $ALICE"
echo "  Bob:         $BOB"
echo "  Coins:       $COINS"
echo "  +1 Sword:    $SWORD"
echo ""
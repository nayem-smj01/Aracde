package org.example.infrastructure.service;

import com.arcadedb.query.sql.executor.Result;
import com.arcadedb.query.sql.executor.ResultSet;
import com.arcadedb.remote.RemoteDatabase;
import org.example.domain.model.ConnectsTo;
import org.example.domain.model.Guards;
import org.example.domain.model.LocatedIn;
import org.example.domain.model.StoredIn;
import org.example.domain.service.EdgeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ArcadeDbEdgeService implements EdgeService {

    private static final Logger logger = LoggerFactory.getLogger(ArcadeDbEdgeService.class);

    private final RemoteDatabase database;

    public ArcadeDbEdgeService(RemoteDatabase database) {
        this.database = database;
    }

    @Override
    public ConnectsTo connectRooms(String fromRoomId, String toRoomId, ConnectsTo connection) {
        logger.info("Connecting rooms: {} -> {}", fromRoomId, toRoomId);

        String sql = String.format(
                "CREATE EDGE ConnectsTo FROM %s TO %s SET direction = '%s', doorType = '%s', distance = %d",
                fromRoomId,
                toRoomId,
                escape(connection.getDirection()),
                escape(connection.getDoorType()),
                connection.getDistance()
        );

        try {
            ResultSet result = database.command("sql", sql);
            if (result.hasNext()) {
                Result record = result.next();
                connection.setId(record.getIdentity().get().toString());
                connection.setFromRoomId(fromRoomId);
                connection.setToRoomId(toRoomId);
                logger.info("Rooms connected with edge ID: {}", connection.getId());
            }
            return connection;
        } catch (Exception e) {
            logger.error("Error connecting rooms: {}", e.getMessage(), e);
            throw new DatabaseOperationException("Failed to connect rooms", e);
        }
    }

    @Override
    public LocatedIn placeMonsterInRoom(String monsterId, String roomId, LocatedIn location) {
        logger.info("Placing monster {} in room {}", monsterId, roomId);

        String sql = String.format(
                "CREATE EDGE LocatedIn FROM %s TO %s SET quantity = %d, behavior = '%s'",
                monsterId,
                roomId,
                location.getQuantity(),
                escape(location.getBehavior())
        );

        try {
            ResultSet result = database.command("sql", sql);
            if (result.hasNext()) {
                Result record = result.next();
                location.setId(record.getIdentity().get().toString());
                location.setMonsterId(monsterId);
                location.setRoomId(roomId);
                logger.info("Monster placed with edge ID: {}", location.getId());
            }
            return location;
        } catch (Exception e) {
            logger.error("Error placing monster in room: {}", e.getMessage(), e);
            throw new DatabaseOperationException("Failed to place monster in room", e);
        }
    }

    @Override
    public StoredIn storeTreasureInRoom(String treasureId, String roomId, StoredIn storage) {
        logger.info("Storing treasure {} in room {}", treasureId, roomId);

        String sql = String.format(
                "CREATE EDGE StoredIn FROM %s TO %s SET hidden = %b, containerType = '%s'",
                treasureId,
                roomId,
                storage.getHidden(),
                escape(storage.getContainerType())
        );

        try {
            ResultSet result = database.command("sql", sql);
            if (result.hasNext()) {
                Result record = result.next();
                storage.setId(record.getIdentity().get().toString());
                storage.setTreasureId(treasureId);
                storage.setRoomId(roomId);
                logger.info("Treasure stored with edge ID: {}", storage.getId());
            }
            return storage;
        } catch (Exception e) {
            logger.error("Error storing treasure in room: {}", e.getMessage(), e);
            throw new DatabaseOperationException("Failed to store treasure in room", e);
        }
    }

    @Override
    public Guards monsterGuardsTreasure(String monsterId, String treasureId, Guards guard) {
        logger.info("Monster {} guards treasure {}", monsterId, treasureId);

        String sql = String.format(
                "CREATE EDGE Guards FROM %s TO %s SET awareness = '%s', priority = %d",
                monsterId,
                treasureId,
                escape(guard.getAwareness()),
                guard.getPriority()
        );

        try {
            ResultSet result = database.command("sql", sql);
            if (result.hasNext()) {
                Result record = result.next();
                guard.setId(record.getIdentity().get().toString());
                guard.setMonsterId(monsterId);
                guard.setTreasureId(treasureId);
                logger.info("Guard relationship created with edge ID: {}", guard.getId());
            }
            return guard;
        } catch (Exception e) {
            logger.error("Error creating guard relationship: {}", e.getMessage(), e);
            throw new DatabaseOperationException("Failed to create guard relationship", e);
        }
    }

    @Override
    public void deleteEdge(String edgeId) {
        logger.info("Deleting edge: {}", edgeId);

        String sql = String.format("DELETE EDGE %s", edgeId);

        try {
            database.command("sql", sql);
            logger.info("Edge deleted: {}", edgeId);
        } catch (Exception e) {
            logger.error("Error deleting edge: {}", e.getMessage(), e);
            throw new DatabaseOperationException("Failed to delete edge", e);
        }
    }

    @Override
    public List<ConnectsTo> getRoomConnections(String roomId) {
        logger.debug("Fetching connections for room: {}", roomId);

        String sql = String.format("SELECT FROM ConnectsTo WHERE out = %s OR in = %s", roomId, roomId);

        try {
            ResultSet result = database.query("sql", sql);
            List<ConnectsTo> connections = new ArrayList<>();

            while (result.hasNext()) {
                connections.add(mapToConnectsTo(result.next()));
            }

            logger.info("Found {} connections for room", connections.size());
            return connections;
        } catch (Exception e) {
            logger.error("Error fetching room connections: {}", e.getMessage(), e);
            throw new DatabaseOperationException("Failed to fetch room connections", e);
        }
    }

    private ConnectsTo mapToConnectsTo(Result record) {
        ConnectsTo connection = new ConnectsTo();
        connection.setId(record.getIdentity().get().toString());
        connection.setDirection(record.getProperty("direction"));
        connection.setDoorType(record.getProperty("doorType"));
        connection.setDistance(record.getProperty("distance"));
        // Note: fromRoomId and toRoomId would need to be extracted from 'out' and 'in' properties
        return connection;
    }

    private String escape(String value) {
        if (value == null) return "";
        return value.replace("'", "\\'").replace("\"", "\\\"");
    }
}

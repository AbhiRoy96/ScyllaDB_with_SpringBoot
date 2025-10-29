### What is ScyllaDB?

ScyllaDB is:

- A NoSQL wide-column database
- Fully compatible with Cassandra
- Built in C++ instead of Java → much faster & lower latency
- Auto-sharding (each node uses all CPU cores efficiently)
- Used for real-time, low-latency, high-write-throughput workloads

Used by: Disney+, Discord, Starbucks, Zillow, Grab, Expedia, etc.

When to use ScyllaDB?

✅ Real-time analytics
✅ High write-heavy workloads
✅ Time-series events
✅ microservices storing user/session data
✅ messaging, IoT logs, metrics


### M0. Lets create a ScyllaDB Instance.

```
docker run --name scylla -d --hostname scylla \
  -p 9042:9042 scylladb/scylla:latest
```

Log into the ScyllaDB container using 'cqlsh'

```
docker exec -it scylla cqlsh
```

If required to check the Logs use: 
```
docker logs -f scylla
```

### M1. Setting up ScyllaDB

#### 1. Creating a Keyspace

As ScyllaDB is based on Cassandra hence most of the features are very similar to what Cassandra offers.

```
CREATE KEYSPACE IF NOT EXISTS feed_service WITH replication = {'class': 'SimpleStrategy', 'replication_factor': 3};
```

#### 2. Create Activity/Feed Table

```

USE feed_service;

CREATE TABLE IF NOT EXISTS user_feed (
    user_id UUID,
    event_time TIMESTAMP,
    event_type TEXT,
    payload TEXT,
    PRIMARY KEY((user_id), event_time)
) WITH CLUSTERING ORDER BY (event_time DESC);

```


#### 3. Insert some data into the table

```

INSERT INTO user_feed (user_id, event_time, event_type, payload)
VALUES (uuid(), toTimestamp(now()), 'LOGIN', '{"ip":"192.168.1.7"}');

```


#### 4. Read back the Data Inserted

```

SELECT * FROM user_feed LIMIT 10;

```


### M2. Setting up Spring Boot Project to Integrate with ScyllaDB

#### 1. Run the following maven command to build the project.

```

mvn clean package -DskipTests

```




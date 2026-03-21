# JcacheApplication

Spring Boot sample application that demonstrates **JSR-107 (JCache)** patterns with **Hazelcast** and a MySQL-backed persistence layer.

The project focuses on:

- Read-through caching
- Write-through caching
- Cache entry listeners and filters
- Cache annotations (`@CacheResult`, `@CachePut`, `@CacheRemove`, `@CacheRemoveAll`)
- EntryProcessor usage

## Tech Stack

- Java (configured in `pom.xml` as `java.version=14`)
- Spring Boot `4.0.1`
- JCache API (`javax.cache:cache-api`)
- Hazelcast `5.6.0`
- Spring Data JPA + MySQL
- Docker Compose (optional, for local MySQL + Hazelcast cluster)

## Prerequisites

- Java 14+
- Docker + Docker Compose (optional but recommended)
- Maven (required if Maven Wrapper is not fully available)

## Configuration

Application properties are in `src/main/resources/application.properties`:

- DB URL: `jdbc:mysql://localhost:3306/localdb`
- Username: `${username}`
- Password: `${password}`

Set credentials before running (example):

```sh
export username=kiran
export password=kiran
```

## Local Setup

1) Start infrastructure (MySQL + Hazelcast nodes)

```sh
docker compose up -d
```

2) Create schema and tables

```sh
docker exec -i mysql-local mysql -ukiran -pkiran < src/main/resources/sqls/tables.sql
```

3) Run the application

```sh
mvn spring-boot:run
```

If your environment uses the wrapper, you can also try:

```sh
./mvnw spring-boot:run
```

## API Endpoints

Base URL: `http://localhost:8080`

### Customers (`CustomersController`)

- `GET /customers/{id}` - Fetch customer from cache (read-through enabled)
- `POST /customers/put` - Put customer into cache (write-through enabled)
- `GET /customers/load-cache-data` - Load random sample customer data (`id` 1..99)
- `DELETE /customers/remove-cache-element/{key}` - Remove one cache entry
- `DELETE /customers/ClearCache` - Clear the full customers cache
- `GET /customers/get-all-cache-data` - Return cached entries (limited by current implementation)
- `GET /customers/age?keys=1,2&uppercase=true` - Deprecated age + lastname transform endpoint
- `GET /customers/correct-age?keys=1,2&uppercase=true` - Batch EntryProcessor variant

Example payload for `POST /customers/put`:

```json
{
  "id": 1,
  "firstName": "John",
  "lastName": "Doe",
  "dob": "10/01/1995"
}
```

### Address (`AddressController`)

- `GET /customers/{addressId}` - Fetch address (`@CacheResult`, cache name `addresses`)
- `DELETE /customers/{addressId}` - Delete address (`@CacheRemove`)
- `DELETE /customers` - Delete all addresses (`@CacheRemoveAll`)
- `POST /customers/post-address` - Save address (`@CachePut`)

## Cache Behavior

### `customers` cache

Defined in `JcacheApplication#createCustomersCacheConfig`:

- Read-through + write-through enabled
- Loader: `CustomerCacheLoader`
- Writer: `CustomersCacheWriter`
- Expiry: touched policy, 60 seconds
- Listener: `CustomerCacheEntryListener`
- Event filter: `CustomerCacheEntryEventFilter` (only even keys)
- Statistics and management enabled

### `addresses` cache

- Basic cache configuration with statistics and management enabled
- Primarily used through JCache annotations in `AddressController`

## Project Structure

- `src/main/java/com/kiran/jcache/controllers` - REST endpoints
- `src/main/java/com/kiran/jcache/model` - JPA entities (`Customer`, `Address`)
- `src/main/java/com/kiran/jcache/repository` - Spring Data repositories
- `src/main/java/com/kiran/jcache/util` - Loader/writer/listener/filter/factories/processor
- `src/main/resources/sqls/tables.sql` - DB schema
- `docker-compose.yaml` - Local MySQL + Hazelcast services

## Test

```sh
mvn test
```

If Maven is not installed locally, install Maven first or add the missing Maven Wrapper files.

# Project Overview

This project involves the development of a service that facilitates communication with drones through a REST API. It encompasses functionalities such as drone registration, loading medication items onto drones, checking loaded medication items for a given drone, verifying available drones for loading, and monitoring the battery level of a specific drone.

## Build Commands

To work with the project, use the following commands:

- Build:
    - Run: `./mvnw spring-boot:run`
    - Test: `./mvnw test`

## Drone Entity

A Drone is characterized by the following attributes:

- Serial Number (up to 100 characters)
- Model (Lightweight, Middleweight, Cruiserweight, Heavyweight)
- Weight Limit (maximum 500kg)
- Battery Capacity (percentage)
- State (IDLE, LOADING, LOADED, DELIVERING, DELIVERED, RETURNING)

## Medication Entity

Each Medication has the following properties:

- Name (allowed only letters, numbers, ‘-‘, ‘\ _’)
- Weight
- Code (allowed only uppercase letters, underscore, and numbers)
- Image (picture of the medication case)

## API Documentation

### Medications

#### GET http://localhost:8080/api/v1/medications

- Retrieves all Medications.

**Request Example:**

```shell
curl --location 'http://localhost:8080/api/v1/medications'
```

- Query Parameters:
    - `pageNumber`: Integer [default value = 0]
    - `pageSize`: Integer [default value = 25]

**Response Example:**

```json
{
    "content": [
        {
            "id": 1,
            "name": "amoxicillin",
            "weight": "1.50",
            "code": "MED_7I_XQGI0KG",
            "createdAt": "2023-09-13T11:29:01+0000"
        },
        // ... (other medications)
    ],
    "number": 0,
    "size": 25,
    "totalElements": 10,
    // ... (pagination information)
}
```

#### POST http://localhost:8080/api/v1/medications

- Creates a new medication.

**Request Example:**

```shell
curl --location 'http://localhost:8080/api/v1/medications' \
--form 'medicationsImage=@"/C:/Users/Azubike/Downloads/carbon (6).png"' \
--form 'name="Raba"' \
--form 'weight="3"'
```

**Response Example:**

```json
{
    "id": 11,
    "name": "Raba",
    "weight": "3",
    "code": "MED_GKT73W59QG",
    "createdAt": "2023-09-14T13:12:17+0000"
}
```

#### POST http://localhost:8080/api/v1/medications/{id}

- Updates a medication.

**Request Example:**

```shell
curl --location 'http://localhost:8080/api/v1/medications/11' \
--form 'name="Raba"' \
--form 'weight="3"'
```

**Response Example:**

```json
{
    "id": 11,
    "name": "King",
    "weight": "3",
    "code": "MED_GKT73W59QG",
    "createdAt": "2023-09-14T13:12:17+0000"
}
```

#### GET http://localhost:8080/api/v1/medications/{id}

- Finds medication by ID.

**Request Example:**

```shell
curl --location 'http://localhost:8080/api/v1/medications/11'
```

**Response Example:**

```json
{
    "id": 11,
    "name": "King",
    "weight": "3",
    "code": "MED_GKT73W59QG",
    "createdAt": "2023-09-14T13:12:17+0000"
}
```

#### DELETE http://localhost:8080/api/v1/medications/{id}

- Deletes a medication.

**Request Example:**

```shell
curl --location --request DELETE 'http://localhost:8080/api/v1/medications/11'
```

**Response Example:**

```
MEDICATION SUCCESSFULLY DELETED
```

### Drones

#### GET http://localhost:8080/api/v1/drones

- Retrieves all drones.

**Request Example:**

```shell
curl --location 'http://localhost:8080/api/v1/drones'
```

**Response Example:**

```json
[
    {
        "id": 1,
        "model": "CRUISERWEGHT",
        "serialNumber": "o2QcMTTaemWWZ53nQVVQ",
        "weightLimit": 25.00,
        "batteryCapacity": 100,
        "state": "IDLE",
        "medications": []
    },
    // ... (other drones)
]
```

#### POST http://localhost:8080/api/v1/drones

- Registers a drone.

**Example Request:**

```shell
curl --location 'http://localhost:8080/api/v1/drones' \
--header 'Content-Type: application/json' \
--data '{
  "droneModel" : "MIDDLEWEIGHT" ,
  "weightLimit" : 10
}'
```

**Request Body:**

```json
{
  "droneModel" : "MIDDLEWEIGHT" ,
  "weightLimit" : 10
}
```

**Example Response:**

```json
{
    "id": 5,
    "model": "MIDDLEWEIGHT",
    "serialNumber": "BbkkrQ7UDJTZuFJSsYEx",
    "weightLimit": 10,
    "batteryCapacity": 100,
    "state": "IDLE",
    "medications": null
}
```

#### POST http://localhost:8080/api/v1/drones/{drone_serial_number}/load

- Loads medications to drones.

**Example Request:**

```shell
curl --location 'http://localhost:8080/api/v1/drones/BbkkrQ7UDJTZuFJSsYEx/load' \
--header 'Content-Type: application/json' \
--data '{
    "medicationCodes" : [
     "MED_7I_XQGI0KG",
    "MED_4N_THX1GUV"
    ]
}'
```

**Request Body:**

```json
{
    "medicationCodes" : [
     "MED_7I_XQGI0KG",
    "MED_4N_THX1GUV"
    ]
}
```

**Example Response:**

```json
{
    "id": 5,
    "model": "MIDDLEWEIGHT",
    "serialNumber": "BbkkrQ7UDJTZuFJSsYEx",
    "weightLimit": 10.00,
    "batteryCapacity": 100,
    "state": "LOADING",
    "medications": [
        {
            "id": 1,
            "name": "am

oxicillin",
            "weight": "1.50",
            "code": "MED_7I_XQGI0KG",
            "createdAt": "2023-09-13T11:29:01+0000"
        },
        {
            "id": 2,
            "name": "doxycycline",
            "weight": "0.50",
            "code": "MED_4N_THX1GUV",
            "createdAt": "2023-09-13T11:29:01+0000"
        }
    ]
}
```

#### GET http://localhost:8080/api/v1/drones/{drone_serial_number}/loaded-medications

- Gets all loaded medications in a drone.

**Example Request:**

```shell
curl --location 'http://localhost:8080/api/v1/drones/BbkkrQ7UDJTZuFJSsYEx/loaded-medications'
```

**Example Response:**

```json
[
    {
        "id": 1,
        "name": "amoxicillin",
        "weight": "1.50",
        "code": "MED_7I_XQGI0KG",
        "createdAt": "2023-09-13T11:29:01+0000"
    },
    {
        "id": 2,
        "name": "doxycycline",
        "weight": "0.50",
        "code": "MED_4N_THX1GUV",
        "createdAt": "2023-09-13T11:29:01+0000"
    }
]
```

#### GET http://localhost:8080/api/v1/drones/available-for-loading

- Gets all drones available for loading.

**Example Request:**

```shell
curl --location 'http://localhost:8080/api/v1/drones/available-for-loading'
```

**Example Response:**

```json
[
    {
        "id": 1,
        "model": "CRUISERWEGHT",
        "serialNumber": "o2QcMTTaemWWZ53nQVVQ",
        "weightLimit": 25.00,
        "batteryCapacity": 100,
        "state": "IDLE",
        "medications": []
    },
    // ... (other drones)
]
```

#### GET http://localhost:8080/api/v1/drones/{drone_serial_number}/battery-level

- Gets the battery level of a drone.

**Example Request:**

```shell
curl --location 'http://localhost:8080/api/v1/drones/o2QcMTTaemWWZ53nQVVQ/battery-level'
```

**Example Response:**

```
The Battery capacity of the drone with serialNumber o2QcMTTaemWWZ53nQVVQ is 100 percent
```

#### PUT http://localhost:8080/api/v1/drones/{drone_serial_number}

- Updates a given drone.

**Example Request:**

```json
{
  "serialNumber": "string",
  "droneModel": "string",
  "weightLimit": 500,
  "batteryCapacity": 0,
  "droneState": "string"
}
```

**Example Response:**

```json
{
  "id": 0,
  "model": "LIGHTWEIGHT",
  "serialNumber": "string",
  "weightLimit": 500,
  "batteryCapacity": 0,
  "state": "IDLE",
  "medications": [
    {
      "id": 0,
      "name": "2kZcdtNI1lNWf-Lo6yWRDXuLxh\\9GRe1KQyvpOgFua\\6y\\HxwozmbSudO8bTsZIsKhsA3WokoGI9iuOpUj1",
      "weight": 0,
      "code": "WEY6XU2JADOVXUZHZL709M4FM2AASIJLHJ1J0",
      "createdAt": "2023-09-14T13:36:51.268Z"
    }
  ]
}
```

#### DELETE http://localhost:8080/api/v1/drones/{drone_serial_number}

- Deletes a given drone.

**Example Request:**

```shell
curl --location --request DELETE 'http://localhost:8080/api/v1/drones/o2QcMTTaemWWZ53nQVVQ'
```

**Example Response:**

```
DRONE WITH SERIAL NUMBER o2QcMTTaemWWZ53nQVVQ DELETED SUCCESSFULLY!
```

# Architecture Pattern

The project employs the MVC (Model-View-Controller) pattern to ensure separation of concerns and adhere to the single responsibility design pattern. Controllers delegate tasks to services, and services access data through the data-access layer. This design allows for easy substitution of one data storage solution for another by swapping out data access functions while maintaining data format consistency.

# Design Considerations

In-memory H2 database is used for persistence, and images are stored in the database as byte values. While this approach simplifies the application and makes it self-contained, it may not be performant in a production environment. For production, a better approach would involve using presigned URLs and delegating image storage to a third-party cloud storage service like AWS S3 or Cloudinary.


# Swagger Docs 

Additionally the swagger docs can be found in http://localhost:8080/swagger-ui/index.html
# Get Note Stats

Retrieves a note from the database and returns the count of every distinct word in its text.

**URL** : `/api/v1/notes/{noteId}/stats`

**Method** : `GET`

## Parameters

### noteId

**Description**: ID of note to retrieve the stats for. Should be a valid mongoDB objectID, i.e. hexadecimal string.

**Parameter Type**: Path Variable

**Data Type**: String


### Sample Request

```
GET /api/v1/notes/67524c7a06bbdc38dacf50d6/stats
```

## Success Response

**Code** : `200 OK`

**Content example**

```json
{
  "hello": 1,
  "this": 1,
  "is": 1,
  "an": 1,
  "updated": 1,
  "note": 1
}
```
## Not Found Response

**Condition** : If _noteId_ does not match any notes in database

**Code** : `404 NOT FOUND`

**Content** :

```json
{
  "description": "Note not found",
  "details": [
    "No note matching id 67524c7a06bbdc38dacf50d7"
  ]
}
```


## Error Response

**Condition** : If _noteId_ is not a valid ObjectId.

**Code** : `400 BAD REQUEST`

**Content** :

```json
{
  "description": "Validation Failed",
  "details": [
    "Invalid ObjectId"
  ]
}
```
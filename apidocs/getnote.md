# Get Note

Retrieves a Note from database

**URL** : `/api/v1/notes/{noteId}`

**Method** : `GET`

## Parameters

### noteId

**Description**: ID of note to be retrieved. Should be a valid mongoDB objectID, i.e. hexadecimal string.

**Parameter Type**: Path Variable

**Data Type**: String


### Sample Request

```
GET /api/v1/notes/67524c7a06bbdc38dacf50d6
```

## Success Response

**Code** : `200 OK`

**Content example**

```json
{
  "id": "67524c7a06bbdc38dacf50d6",
  "title": "Sample title",
  "createdDate": "2024-12-06",
  "text": "Hello this is a note",
  "tags": [
    "IMPORTANT"
  ]
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
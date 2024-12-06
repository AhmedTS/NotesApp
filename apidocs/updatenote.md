# Update Note

Updates existing note in database and returns updated node. Note that the update can only be in the _title_, _text_ and _tags_ fields.

**URL** : `/api/v1/notes/{noteId}`

**Method** : `PUT`

## Parameters

### noteId

**Description**: ID of note to be updated. Should be a valid mongoDB objectID, i.e. hexadecimal string.

**Parameter Type**: Path Variable

**Data Type**: String

### note

**Description**: Updated note to be saved. Note that the id and createdDate attributes since they will not be modified.

**Parameter Type**: RequestBody

**Data Type**: WebNote

**Data Constraint**: title and text attributes cannot be blank.

### Sample Request

```
PUT /api/v1/notes/67524c7a06bbdc38dacf50d6
```
```json
{
  "title": "Updated title",
  "text": "Hello this is an updated note",
  "tags": ["IMPORTANT", "PERSONAL"]
}
```

## Success Response

**Code** : `200 OK`

**Content example**

```json
{
  "id": "67524c7a06bbdc38dacf50d6",
  "title": "Updated title",
  "createdDate": "2024-12-06",
  "text": "Hello this is an updated note",
  "tags": [
    "IMPORTANT",
    "PERSONAL"
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

## Error Response: _note_

**Condition** : If either _note.text_ or _note.title_ is blank.

**Code** : `400 BAD REQUEST`

**Content** :

```json
{
  "description": "Validation Failed",
  "details": [
    "Title must not be blank."
  ]
}
```

## Error Response: _noteId_

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
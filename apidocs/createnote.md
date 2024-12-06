# Create Note

Creates and saves a new note and returns the saved note.

**URL** : `/api/v1/notes`

**Method** : `POST`

## Parameters

### note

**Description**: New note to be saved. Note that the id and createdDate attributes can be null since they will be set by the application automatically.

**Parameter Type**: RequestBody

**Data Type**: WebNote

**Data Constraint**: title and text attributes cannot be blank.

### Sample Request

```
PUT /api/v1/notes
```
```json
{
  "title": "Sample title",
  "text": "Hello this is a note",
  "tags": ["IMPORTANT"]
}
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

## Error Response

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
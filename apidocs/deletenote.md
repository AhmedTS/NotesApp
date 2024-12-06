# Delete Notes

Deletes a Note from database

**URL** : `/api/v1/notes/{noteId}`

**Method** : `DELETE`

## Parameters

### noteId

**Description**: ID of note to be deleted. Should be a valid mongoDB objectID, i.e. hexadecimal string.

**Parameter Type**: Path Variable

**Data Type**: String


### Sample Request

```
DELETE /api/v1/notes/67524c7a06bbdc38dacf50d6
```

## Success Response

**Code** : `204 No Content`

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
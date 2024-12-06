# List Notes

Returns Paginated List of Notes (only id, title and createdDate attributes) sorted by newest. User may filter the list by tags.

**URL** : `/api/v1/notes`

**Method** : `GET`

## Parameters

### filter

**Description**: Indicates whether the list of notes is to be filtered. If false, all notes are retrieved, otherwise the notes are filtered by the _tags_ parameter.

**Parameter Type**: Request parameter

**Data Type**: boolean

**Default Value**: _false_

### tags

**Description**: List of tags which are used to filter notes, if _filter_ is true. If _tags_ is empty then only notes with empty tags are returned, otherwise any note that contains **ALL** the tags defined in _tags_ are returned.

**Parameter Type**: Request parameter

**Data Type**: List<Tags>

**Default Value**: []

### page

**Description**: Defines which page of the results should be returned.

**Parameter Type**: Request parameter

**Data Type**: int

**Default Value**: 0

**Data constraints**: Must be equal to 0 or greater.

### size

**Description**: Defines number of notes in each page.

**Parameter Type**: Request parameter

**Data Type**: int

**Default Value**: 5

**Data constraints**: Must be equal to 1 or greater.

### Sample Request

```
GET /api/v1/notes?filter=false&page=1
```

## Success Response

**Code** : `200 OK`

**Content example**

```json
{
  "content": [
    {
      "id": "674eb23ce942365b86c514b8",
      "title": "Note 47",
      "createdDate": "2023-09-25"
    },
    {
      "id": "674eb23ce942365b86c514af",
      "title": "Note 38",
      "createdDate": "2023-09-15"
    },
    {
      "id": "674eb23ce942365b86c5148d",
      "title": "Note 4",
      "createdDate": "2023-08-01"
    },
    {
      "id": "674eb23ce942365b86c514aa",
      "title": "Note 33",
      "createdDate": "2023-07-11"
    },
    {
      "id": "674eb23ce942365b86c514a5",
      "title": "Note 28",
      "createdDate": "2023-05-23"
    }
  ],
  "page": {
    "size": 5,
    "number": 1,
    "totalElements": 18,
    "totalPages": 4
  }
}
```

## Error Response

**Condition** : If either _page_ or _size_ is invalid.

**Code** : `400 BAD REQUEST`

**Content** :

```json
{
  "description": "Validation Failed",
  "details": [
    "Size must be 1 or greater.",
    "Page must be 0 or greater."
  ]
}
```
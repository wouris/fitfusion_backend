# FitFusion Backend Repository

This is a backend part of our application called **FitFusion**. Application is for fitness enthusiasts, where you can
connect with people that have same goals, being fit! You can share your progress, workout routines and more!

## Available Endpoints

Here are all the endpoints with descriptions and responses

### Auth

|     Endpoint     | Method |    Description     |              Request Body Example              |           Response Body Example           |
|:----------------:|:------:|:------------------:|:----------------------------------------------:|:-----------------------------------------:|
| `/auth/register` | `POST` | Registers the user | `{"username": "string", "password": "string"}` | `{"type": "string", "message": "string"}` |
|  `/auth/login`   | `POST` | Signs in the user  | `{"username": "string", "password": "string"}` | `{"type": "string", "message": "string"}` |

All the requests for the endpoints below must include `Authorization` (Token) and `USER_ID` (User UUID) headers

### Social

## Post

|         Endpoint          |  Method  |            Description            |                             Request Body Example                              |           Response Body Example           |
|:-------------------------:|:--------:|:---------------------------------:|:-----------------------------------------------------------------------------:|:-----------------------------------------:|
| `/api/social/post/upload` |  `POST`  | Uploads the image and description | `{"image": "string (base64)", "description": "string", "author": "ObjectId"}` | `{"type": "string", "message": "string"}` |
| `/api/social/post/remove` | `DELETE` |         Removes the post          |                             `{"postId": "UUID"}`                              | `{"type": "string", "message": "string"}` |

## Comment

|           Endpoint           |  Method  |         Description         |           Request Body Example            |           Response Body Example           |
|:----------------------------:|:--------:|:---------------------------:|:-----------------------------------------:|:-----------------------------------------:|
| `/api/social/comment/upload` |  `POST`  |  Sends a comment on a post  | `{"postId": "UUID", "content": "string"}` | `{"type": "string", "message": "string"}` |
| `/api/social/comment/remove` | `DELETE` | Removes a comment on a post |           `{"postId": "UUID"}`            | `{"type": "string", "message": "string"}` |

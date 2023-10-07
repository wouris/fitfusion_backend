# FitFusion Backend Repository

This is a backend part of our application called **FitFusion**. Application is for fitness enthusiasts, where you can
connect with people that have same goals, being fit! You can share your progress, workout routines and more!

## Available Endpoints

Here are all the endpoints with descriptions and responses

> [!NOTE]
> In the event of an unsuccessful request, the response body will consistently
> contain `{"type": "ERROR", "message": "string"}`.
> Response body examples are provided for successful requests.

## Auth

|     Endpoint     | Method |    Description     |                       Request Body Example                       |                        Response Body Example                         |
|:----------------:|:------:|:------------------:|:----------------------------------------------------------------:|:--------------------------------------------------------------------:|
| `/auth/register` | `POST` | Registers the user | `{"email": "string","username": "string", "password": "string"}` | `{"Authorization": "string", "USER_ID": "string", "ROLE": "string"}` |
|  `/auth/login`   | `POST` | Signs in the user  |          `{"username": "string", "password": "string"}`          | `{"Authorization": "string", "USER_ID": "string", "ROLE": "string"}` |

> [!IMPORTANT]
> All the requests for the endpoints below must include `Authorization` (Token) and `USER_ID` (User UUID) headers

## Auth

|    Endpoint    | Method |    Description     | Request Body Example |                        Response Body Example                         |
|:--------------:|:------:|:------------------:|:--------------------:|:--------------------------------------------------------------------:|
| `/auth/logout` | `POST` | Signs out the user |         `{}`         | `{"Authorization": "string", "USER_ID": "string", "ROLE": "string"}` |

## Social

### Post

|         Endpoint          |  Method  |            Description            |                           Request Body Example                            |                                                                                                           Response Body Example                                                                                                            |
|:-------------------------:|:--------:|:---------------------------------:|:-------------------------------------------------------------------------:|:------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------:|
|  `/api/social/post/get`   |  `POST`  | Returns posts based on parameters |            `{"pageSize": "integer", "pageOffset": "integer"}`             | `[{"image": "string (base64), "description": "string", username: "string", "createdAgo": "string", "likes": "integer", "topComments": [{"id": "UUID", "postId": "UUID", "username": "string", "content": "string", "likes": "integer"}]}]` |
| `/api/social/post/upload` |  `POST`  | Uploads the image and description | `{"image": "string (base64)", "description": "string", "author": "UUID"}` |                                                                                                 `{"type": "string", "message": "string"}`                                                                                                  |
| `/api/social/post/remove` | `DELETE` |         Removes the post          |                           `{"postId": "UUID"}`                            |                                                                                                 `{"type": "string", "message": "string"}`                                                                                                  |
|  `/api/social/post/like`  |  `POST`  |          Likes the post           |                           `{"postId": "UUID"}`                            |                                                                                                 `{"type": "string", "message": "string"}`                                                                                                  |
| `/api/social/post/unlike` | `DELETE` |         Unlikes the post          |                           `{"postId": "UUID"}`                            |                                                                                                 `{"type": "string", "message": "string"}`                                                                                                  |

### Comment

|           Endpoint           |  Method  |          Description           |           Request Body Example            |                                       Response Body Example                                        |
|:----------------------------:|:--------:|:------------------------------:|:-----------------------------------------:|:--------------------------------------------------------------------------------------------------:|
|  `/api/social/comment/get`   |  `POST`  | Returns all comments on a post |           `{"postId": "UUID"}`            | `[{"id": "UUID", "postId":"UUID", "username": "string", "content": "string", "likes": "integer"}]` |
| `/api/social/comment/upload` |  `POST`  |   Sends a comment on a post    | `{"postId": "UUID", "content": "string"}` |                             `{"type": "string", "message": "string"}`                              |
| `/api/social/comment/remove` | `DELETE` |  Removes a comment on a post   |          `{"commendId": "UUID"}`          |                             `{"type": "string", "message": "string"}`                              |
|  `/api/social/comment/like`  |  `POST`  |        Likes a comment         |          `{"commendId": "UUID"}`          |                             `{"type": "string", "message": "string"}`                              |
| `/api/social/comment/unlike` | `DELETE` |       Unlikes a comment        |          `{"commendId": "UUID"}`          |                             `{"type": "string", "message": "string"}`                              |

### Social Info

|            Endpoint            | Method |           Description            | Request Body Example |                                      Response Body Example                                      |
|:------------------------------:|:------:|:--------------------------------:|:--------------------:|:-----------------------------------------------------------------------------------------------:|
| `/api/social/user/{UUID}/info` | `GET`  | Returns social info about a user |         `{}`         | `{"username": "string", "workouts": "integer", "followers": "integer", "following": "integer"}` |

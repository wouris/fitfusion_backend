# FitFusion Backend Repository

This is a backend part of our application called **FitFusion**. Application is for fitness enthusiasts, where you can connect with people that have same goals, being fit! You can share your progress, workout routines and more!

## Available Endpoints

Here are all the endpoints with descriptions and responses

### Auth

|     Endpoint     |    Description     |              Request Body Example              |           Response Body Example           |
|:----------------:|:------------------:|:----------------------------------------------:|:-----------------------------------------:|
| `/auth/register` | Registers the user | `{"username": "string", "password": "string"}` | `{"type": "string", "message": "string"}` |
|  `/auth/login`   | Signs in the user  | `{"username": "string", "password": "string"}` | `{"type": "string", "message": "string"}` |


### Social

|       Endpoint       |            Description            |                             Request Body Example                              |           Response Body Example           |
|:--------------------:|:---------------------------------:|:-----------------------------------------------------------------------------:|:-----------------------------------------:|
| `/api/posts/upload`  | Uploads the image and description | `{"image": "string (base64)", "description": "string", "author": "ObjectId"}` | `{"type": "string", "message": "string"}` |
| `/api/posts/comment` |     Sends a comment on a post     |                   `{"postId": "UUID", "content": "string"}`                   | `{"type": "string", "message": "string"}` |

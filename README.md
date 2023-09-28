# FitFusion Backend Repository

This is a backend part of our application called **FitFusion**. Application is for fitness enthusiasts, where you can connect with people that have same goals, being fit! You can share your progress, workout routines and more!

## Available Endpoints

Here are all of the endpoints with descriptions and responses
### Auth

| Endpoint         | Description        | Request Body Example                           | Response Body Example    |
|------------------|--------------------|------------------------------------------------|--------------------------|
| `/auth/register` | Registers the user | `{"username": "string", "password": "string"}` | `{"response": "string"}` |
| `/auth/login`    | Signs in the user  | `{"username": "string", "password": "string"}` | `{"response": "string"}` |

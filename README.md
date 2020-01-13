# Voting System 
(REST service to vote for restaurants)

<h2>Постановка задачи:</h2>

Design and implement a REST API using Hibernate/Spring/SpringMVC (or Spring-Boot) without frontend.

Build a voting system for deciding where to have lunch.

* 2 types of users: admin and regular users
* Admin can input a restaurant and it's lunch menu of the day (2-5 items usually, just a dish name and price)
* Menu changes each day (admins do the updates)
* Users can vote on which restaurant they want to have lunch at
* Only one vote counted per user
* If user votes again the same day:
    - If it is before 11:00 we asume that he changed his mind.
    - If it is after 11:00 then it is too late, vote can't be changed
    
Each restaurant provides new menu each day.

## Run
```
$ mvn package
$ mvn cargo:run
```

## Users
After start available 2 users

Username | Password | Role
-------- | -------- | ----
admin@email.com | admin_password | ADMIN
user@email.com | user_password | USER

After this commands you will start hosting website on page `localhost:8080/RestaurantVoting`

# API.

## Profile API

### URL Pages 

Description | Method | URL | Body | User
----------- | ------ | --- | ---- | ----
Get current | GET | `{URL}/rest/profile` | none | Authorized
Update current | PUT | `{URL}/rest/profile/` | Update Body | Authorized
Delete current | DELETE | `{URL}/rest/profile` | none | Authorized
Get All |GET |`{URL}/rest/admin/users` | none |Only Admin
Get  | GET | `{URL}/rest/admin/users/{ID}` | none | Only Admin
Delete  | DELETE | `{URL}/rest/admin/users/(ID}` | none | Only Admin
Update | PUT | `{URL}/rest/admin/users` |Update Body| Only Admin
Create | POST | `{URL}/rest/admin/users` | Create Body | Only Admin
Register Profile | POST | `{URL}/rest/profile/register` | Register Body | Not Authorized

## Bodies
#### Register Body
```json
{
    "name": "New User",
    "email": "newUser@gmail.com",
    "password": "12345",
    "roles": [
            "ROLE_USER"
        ]
}
``` 

#### Update Body
```json
{
    "id"  : 102,
    "name": "Anton",
    "email": "newmail@gmail.com",
    "password": "newPass",
    "roles": [
                "ROLE_USER"
            ]
}
```

#### Create Body
```json
{
    "name": "Nikita",
    "email": "nikita@gmail.com",
    "password": "12345",
    "roles": [
            "ROLE_USER"
        ]
}
``` 
or
```json
{
    "name": "Nikita",
    "email": "nikita@gmail.com",
    "password": "12345",
    "roles": [
            "ROLE_USER", "ROLE_ADMIN"
        ]
}
```
+ [Curl samples for Profile API](curls.md#Curls for profile api:)

## Restaurant API

### URL Pages 

Description | Method | URL | Body | User
----------- | ------ | --- | ---- | ----
Get | GET | `{URL}/rest/restaurants/{ID}` | none | Only Admin
Get All |GET |`{URL}/rest/restaurants` | none |Only Admin
Delete  | DELETE | `{URL}/rest/restaurants/(ID}` | none | Only Admin
Update | PUT | `{URL}/rest/restaurants/{ID}` | Update Body |Only Admin
Create | POST | `{URL}/rest/restaurants` | Create Body | Only Admin
Find by name | GET | `{URL}/rest/restaurants/by?name=[name]` | none | Only Admin

## Bodies
#### Update Body
```json
{
    "name": "Update restaurant"
}
```

#### Create Body
```json
{
    "name": "New restaurant"
}
```
+ [Curl samples for Restaurants API](curls.md#Curls for  Restaurants:)

## Dishes API

### URL Pages 

Description | Method | URL | Body | User
----------- | ------ | --- | ---- | ----
Get | GET | `{URL}/rest/dishes/{ID}/menus/{menuID}` | none | Only Admin
Get All |GET |`{URL}/rest/dishes` | none |Only Admin
Delete  | DELETE | `{URL}/rest/dishes/{ID}/menus/{menuID}` | none | Only Admin
Update | PUT | `{URL}/rest/dishes/{ID}/menus/{menuID}` | Update Body |Only Admin
Create | POST | `{URL}/rest/dishes/menus/{menuID}` | Create Body | Only Admin
Find by date | GET | `{URL}/rest/dishes/by?date=[yyyy-MM-dd]` | none | Only Admin
Find by dishes | GET | `{URL}/rest/dishes/menus/{menuID}` | none | Only Admin

## Bodies
#### Update Body
```json
{
    "name": "Update dish",
    "price" : 10000
}
```

#### Create Body
```json
{
    "name"  : "New dish",
    "price" :  10000
}
```
+ [Curl samples for Dishes API](curls.md#Curls for  Dishes:)

## Menus API

### URL Pages 

Description | Method | URL | Body | User
----------- | ------ | --- | ---- | ----
Get | GET | `{URL}/rest/menus/{ID}/restaurants/{restaurantID}` | none | Only Admin
Get All |GET |`{URL}/rest/menus` | none |Only Admin
Delete  | DELETE | `{URL}/rest/menus/{ID}/restaurants/{restaurantID}` | none | Only Admin
Update | PUT | `{URL}/rest/menus/{ID}/restaurants/{restaurantID}` | Update Body |Only Admin
Create | POST | `{URL}/rest/menus/restaurants/{restaurantID}` | Create Body | Only Admin
Find by date with dishes| GET | `{URL}/rest/menus/byDate?date=[yyyy-MM-dd]` | none | Not Authorized
Find by restaurant | GET | `{URL}/rest/menus/byRestaurant?name=[name]` | none | Only Admin
Find by restaurant and date | GET | `{URL}/rest/menus/byRestaurantAndDate?name=[name]&date=[yyyy-MM-dd]` | none | Only Admin
Find by id | GET | `{URL}/rest/menus/byId/byId?id=[id]` | none | Only Admin

## Bodies
#### Update Body
```json
{
    "date": "2019-09-15"
}
```

#### Create Body
```json
{
    "date"  : "2019-09-15"
}
```
+ [Curl samples for Menus API](curls.md#Curls for Menus:)

## Vote API

### URL Pages 

Description | Method | URL | Body | User
----------- | ------ | --- | ---- | ----
Get All by date  |GET |`{URL}/rest/votes/byDate?date=[yyyy-MM-dd]` | none |Only Admin
Create Vote for menu  |POST |`{URL}/rest/votes/menus/{menuID}` | none |Authorized
Update Vote for menu  |PUT |`{URL}/rest/votes/menus/{menuID}` | none |Authorized

+ [Curl samples for Votes API](curls.md#Curls for Votes:)
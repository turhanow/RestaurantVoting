# Curls for Users:
Get user for id: 100000:
```sh
curl -XGET -H "Content-type: application/json" "http://localhost:8080/RestaurantVoting/rest/profile" ^ -v -u user@email.com:user_password
```
Delete user for id: 100000:
```sh
curl -XDELETE -H "Content-type: application/json" "http://localhost:8080/RestaurantVoting/rest/profile" ^ -v -u user@email.com:user_password
```
Update user for id: 100000:
```sh
curl -H "Content-Type: application/json" -X PUT http://localhost:8080/RestaurantVoting/rest/profile -d "{\"name\":\"New777\",\"email\": \"new777@yandex.ru\",\"password\":\"passwordNew\"}" ^ -v -u user@email.com:user_password
```
# Curls for Admins:
Get all users:
```sh
curl -XGET "http://localhost:8080/RestaurantVoting/rest/admin/users" ^ -v -u admin@email.com:admin_password
```
Create new user:
```sh
curl -H "Content-Type: application/json" -X POST http://localhost:8080/RestaurantVoting/rest/admin/users -d "{\"name\":\"NewUser\",\"email\": \"new@yandex.ru\",\"password\":\"password\",\"roles\":[\"ROLE_USER\"]}" ^ -v -u admin@email.com:admin_password
```
Get user with id: 100000 for Admin:
```sh
curl -XGET -H "Content-type: application/json" "http://localhost:8080/RestaurantVoting/rest/admin/users/100000" ^ -v -u admin@email.com:admin_password
```
Delete user with id: 100000 for Admin:
```sh
curl -XDELETE -H "Content-type: application/json" "http://localhost:8080/RestaurantVoting/rest/admin/users/100000" ^ -v -u admin@email.com:admin_password
```
Update user with id: 100000 for Admin:
```sh
curl -H "Content-Type: application/json" -X PUT http://localhost:8080/RestaurantVoting/rest/admin/users/100000 -d "{\"name\":\"New777\",\"email\": \"new777@yandex.ru\",\"password\":\"passwordNew\"}" ^ -v -u admin@email.com:admin_password
```
Get user with id: 100000 by email for Admin:
```sh
curl -XGET -H "Content-type: application/json" "http://localhost:8080/RestaurantVoting/rest/admin/users/by?email=user@yandex.ru" ^ -v -u admin@email.com:admin_password
```
# Curls for  Dishes:
Get all dishes:
```sh
curl -XGET "http://localhost:8080/RestaurantVoting/rest/dishes"  ^ -v -u admin@email.com:admin_password
```
Get  dish with id 100010 for menu id 100005:
```sh
curl -XGET -H "Content-type: application/json" "http://localhost:8080/RestaurantVoting/rest/dishes/100005/100010" ^ -v -u admin@email.com:admin_password
```
Create new dish for menu id 100005:
```sh
curl -XPOST -H "Content-type: application/json" -d "{\"name\":\"Created\",\"price\":100000}" "http://localhost:8080/RestaurantVoting/rest/dishes/100005" -v -u admin@email.com:admin_password
```
Delete dish with id 100010 for menu id 100005:
```sh
curl -XGET "http://localhost:8080/RestaurantVoting/rest/dishes/100005/100010"  ^ -v -u admin@email.com:admin_password
```
Update dish id 100010  for menu id 100005:
```sh
curl -XPUT -H "Content-type: application/json" -d "{\"id\":100010,\"name\":\"Update\",\"price\":100500}" "http://localhost:8080/RestaurantVoting/rest/dishes/100005/100010" -v -u admin@email.com:admin_password
```
Get  dishes by date: 2019-06-11:
```sh
curl -XGET -H "Content-type: application/json" "http://localhost:8080/RestaurantVoting/rest/dishes/by?date=2019-06-11" ^ -v -u admin@email.com:admin_password
```
Get  dishes by menu id 100007:
```sh
curl -XGET -H "Content-type: application/json" "http://localhost:8080/RestaurantVoting/rest/dishes/100007" ^ -v -u admin@email.com:admin_password
```
# Curls for  Restaurants:
Get all restaurants:
```sh
curl -XGET "http://localhost:8080/RestaurantVoting/rest/restaurants"  ^ -v -u admin@email.com:admin_password
```
Get  restaurant with id 100002:
```sh
curl -XGET -H "Content-type: application/json" "http://localhost:8080/RestaurantVoting/rest/restaurants/100002" ^ -v -u admin@email.com:admin_password
```
Create new restaurant:
```sh
curl -XPOST -H "Content-type: application/json" -d "{\"name\":\"Created\"}" "http://localhost:8080/RestaurantVoting/rest/restaurants" -v -u admin@email.com:admin_password
```
Delete restaurant with id 100002:
```sh
curl -XGET "http://localhost:8080/RestaurantVoting/rest/restaurants/100002"  ^ -v -u admin@email.com:admin_password
```
Update restaurant id 100002:
```sh
curl -XPUT -H "Content-type: application/json" -d "{\"id\":100002,\"name\":\"Update\"}" "http://localhost:8080/RestaurantVoting/rest/restaurants/100002" -v -u admin@email.com:admin_password
```
# Curls for  menus:
Get all menus:
```sh
curl -XGET "http://localhost:8080/RestaurantVoting/rest/menus"  ^ -v -u admin@email.com:admin_password
```
Get  menu with id 100005 for restaurant id 100002:
```sh
curl -XGET -H "Content-type: application/json" "http://localhost:8080/RestaurantVoting/rest/menus/100002/100005" ^ -v -u admin@email.com:admin_password
```
Create new menu for restaurant id 100002:
```sh
curl -XPOST -H "Content-type: application/json" -d "{\"date\":\"2019-06-11\", \"menu\": [{ "name": "dish_1", "price": 100500}]}" "http://localhost:8080/RestaurantVoting/rest/menus/100002" -v -u admin@email.com:admin_password
```
Delete restaurant with id 100002:
```sh
curl -XGET "http://localhost:8080/RestaurantVoting/rest/restaurants/100002"  ^ -v -u admin@email.com:admin_password
```
Update restaurant id 100002:
```sh
curl -XPUT -H "Content-type: application/json" -d "{\"id\":100002,\"name\":\"Update\"}" "http://localhost:8080/RestaurantVoting/rest/restaurants/100002" -v -u admin@email.com:admin_password
```


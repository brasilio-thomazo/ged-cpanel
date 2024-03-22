curl --location 'http://localhost:9000/api/roles' \
--header 'Content-Type: application/json' \
--data '{
    "permission": "admin",
    "description": "Acesso administrativo ao sistema"
}'
echo "completed"

curl --location 'http://localhost:9000/api/roles' \
--header 'Content-Type: application/json' \
--data '{
    "permission": "user_admin",
    "description": "Acesso completo aos dados dos usuários"
}'
echo "completed"

curl --location 'http://localhost:9000/api/roles' \
--header 'Content-Type: application/json' \
--data '{
    "permission": "user_read",
    "description": "Acesso de leitura aos dados dos usuários"
}'
echo "completed"

curl --location 'http://localhost:9000/api/roles' \
--header 'Content-Type: application/json' \
--data '{
    "permission": "user_edit",
    "description": "Acesso de alterar os dados dos usuários"
}'
echo "completed"

curl --location 'http://localhost:9000/api/roles' \
--header 'Content-Type: application/json' \
--data '{
    "permission": "group_admin",
    "description": "Acesso completo aos dados dos grupos"
}'
echo "completed"

curl --location 'http://localhost:9000/api/roles' \
--header 'Content-Type: application/json' \
--data '{
    "permission": "group_read",
    "description": "Acesso de leitura aos dados dos grupos"
}'
echo "completed"

curl --location 'http://localhost:9000/api/groups' \
--header 'Content-Type: application/json' \
--data '{
    "name": "administrdores",
    "description": "Grupo de administrdores do sistema",
    "roles": [1]
}'
echo "completed"

curl --location 'http://localhost:9000/api/groups' \
--header 'Content-Type: application/json' \
--data '{
    "name": "usuários administrdores",
    "description": "Grupo de usuários administrdores",
    "roles": [2, 5]
}'
echo "completed"
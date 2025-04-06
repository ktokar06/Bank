| Endpoint                                      | Method | Description                              | Required Role | Headers                               | Body (JSON example)                                                                 |
|-----------------------------------------------|--------|------------------------------------------|---------------|---------------------------------------|------------------------------------------------------------------------------------|
| `/api/auth/login`                             | POST   | Логин пользователя                       | -             | -                                     | `{"username": "new_admin", "password": "admin_password"}`                          |
| `/api/auth/register`                          | POST   | Регистрация пользователя                 | -             | -                                     | `{"username": "new_user", "password": "new_password", "email": "user@example.com", "role": "USER"}` |
| `/api/cards`                                  | GET    | Получить все карты                       | ADMIN         | `Authorization: Bearer <token>`       | -                                                                                  |
| `/api/cards/{id}`                             | GET    | Получить карту по ID                     | USER, ADMIN   | `Authorization: Bearer <token>`       | -                                                                                  |
| `/api/cards`                                  | POST   | Создать новую карту                      | ADMIN         | `Authorization: Bearer <token>`, `Content-Type: application/json` | `{"number": "4111111111111111", "expirationDate": "2025-12-31", ...}`              |
| `/api/transactions/card/{cardId}`             | GET    | Получить транзакции по карте             | USER, ADMIN   | `Authorization: Bearer <token>`       | -                                                                                  |
| `/api/transactions/transfer`                  | POST   | Выполнить перевод                        | USER          | `Authorization: Bearer <token>`, `Content-Type: application/json` | `{"fromCardId": 5, "toCardId": 4, "amount": 100.00, "description": "Test transfer"}` |
| `/api/users`                                  | GET    | Получить всех пользователей              | ADMIN         | `Authorization: Bearer <token>`       | -                                                                                  |
| `/api/users/{id}`                             | GET    | Получить пользователя по ID              | ADMIN         | `Authorization: Bearer <token>`       | -                                                                                  |

**Примеры пользователей и карт:**
```json
[
  {
    "username": "alex_petrov",
    "password": "securePass123",
    "email": "alex.petrov@example.com",
    "role": "USER"
  },
  {
    "number": "5555345678901234",
    "expirationDate": "2026-05-31",
    "cvv": "123",
    "ownerName": "Alex Petrov",
    "balance": 2500.00,
    "status": "ACTIVE",
    "dailyLimit": 3000.00,
    "monthlyLimit": 10000.00,
    "userId": 3
  }
]
```

`Генерация токена:` для  jwt.secret в application.properties
```bash
openssl rand -base64 32
```

## Поднятие контейнера с помощью Docker Compose

**Убедитесь, что в вашей директории находится файл `docker-compose.yml`**

Выполните в терминале:

```bash
  ls -l *.yml *.yaml
```

**Запустите контейнеры**

Выполните в терминале:

```bash
docker compose up
```

## Подключение к MariaDB в DBeaver после запуска контейнера

Настройки подключения:

```
Host: localhost
Port: 3306 (стандартный порт для MySQL/MariaDB)
Database: bank_card_db -  если ее нет создайте 
Username: root
Password: password
```

Тестовые данные должны были сохранится 

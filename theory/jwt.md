### JSON Web Token

Если у нас один общий auth server для разных приложений, то можно использоваться jwt
токен, чтобы не проходить аутентификацию на каждом их сервисов.

![jwt-usage](../diagrams/jwt-usage.png)

![jwt-workflow](../diagrams/jwt-workflow.png)

Токен состоит из 3 частей
- header - информация о токене (алгоритм шифрования)
- body(payload) - информация о пользователе (username, password, authorities...)
- verify signature - секретный ключ, благодаря которому можно расшифровать header и body

![jwt-token-structure](../diagrams/img.png)
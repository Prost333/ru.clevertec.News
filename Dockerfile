# Используйте официальный образ Gradle для сборки вашего приложения
FROM gradle:7.4-jdk17 AS build

# Установите рабочую директорию в /app
WORKDIR /app

# Скопируйте ваш проект Gradle в контейнер
COPY . .

# Соберите приложение с помощью Gradle
RUN gradle bootJar

# Используйте официальный образ OpenJDK для запуска вашего приложения
FROM openjdk:17-jdk-slim

# Установите рабочую директорию в /app
WORKDIR /app

# Обновите список пакетов и установите psql
RUN apt-get update && apt-get install -y postgresql-client

# Установите netcat
RUN apt-get install -y netcat

# Скопируйте JAR-файл вашего приложения из стадии сборки
COPY --from=build /app/build/libs/*.jar app.jar

# Добавьте скрипт ожидания
COPY wait-for-it.sh ./
RUN chmod +x ./wait-for-it.sh

# Запустите ваше приложение
CMD ["./wait-for-it.sh", "db:5432", "--", "java", "-jar", "app.jar"]
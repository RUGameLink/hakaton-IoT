/*
  Rui Santos
  Complete project details at https://RandomNerdTutorials.com/esp-now-many-to-one-esp8266-nodemcu/ 
*/

/*
 MAC адрес платы:  48:3F:DA:54:F5:31 Главная(Приёмник)
 */



#include <ESP8266WiFi.h>
#include <espnow.h>
#include "DHT.h"

#define DHTPIN 5                    //  Куда подключен датчик

  #define DHTTYPE DHT11             // DHT11 Версия датчика
//#define DHTTYPE DHT22             // DHT 22  (AM2302), AM2321
//#define DHTTYPE DHT21             // DHT 21 (AM2301)
  DHT dht(DHTPIN, DHTTYPE);
  float h,t;
uint8_t broadcastAddress[] = {0x8C, 0xAA, 0xB5, 0x59, 0x30, 0x31};        // МАС адрес получателя
        // МАС адрес получателя

#define BOARD_ID 2                                                        // Устанавливаем ID платы как 2

// структуры для отправки данных
typedef struct struct_message {
    int id;
    int x;
    int y;
} struct_message;

int analogMQ7 = A0; 

struct_message myData;                                                    // Создайте сообщение с именем myData для хранения отправляемых переменных

unsigned long lastTime = 0;                                               // Переменная для хранения времени
unsigned long timerDelay = 100;                                         // Задержка в 10 сек между отправками пакетов данных

// Callback-функция при отправке сообщения
void OnDataSent(uint8_t *mac_addr, uint8_t sendStatus) {
  Serial.print("\r\ Delivery status: ");
  if (sendStatus == 0){ Serial.println("Delivery seccess"); }
  else{ Serial.println("Delivery fail"); }
}


void setup() {  
  Serial.begin(9600);                                                   // Запускаем монитор порта на скорости 115200 бод
  dht.begin();
  WiFi.mode(WIFI_STA);    
  pinMode(analogMQ7, INPUT);                                                // Режим работы Клиент
  WiFi.disconnect();
  
  // Инициализируем ESP-NOW
  if (esp_now_init() != 0) {
    Serial.println("Error init ESP-NOW");
    return;
  } 
  
  // Роль платы в ESP-NOW
  esp_now_set_self_role(ESP_NOW_ROLE_CONTROLLER);  
  esp_now_register_send_cb(OnDataSent);                                   // Получаем сообщение об отправке
  esp_now_add_peer(broadcastAddress, ESP_NOW_ROLE_SLAVE, 1, NULL, 0);     // Регистрируем пиры
}

void loop() {
  if ((millis() - lastTime) > timerDelay) {                               // Если прошло больше 10 секунд
  h =analogRead(analogMQ7);     
  Serial.print(h);                                            // Считываем Влажность
  t = 0;                                              // Считываем Температуру
    myData.id = BOARD_ID;
    myData.x = h;                                                         // Отправляем данные
    myData.y = t;                                                         // Отправляем данные
    esp_now_send(0, (uint8_t *) &myData, sizeof(myData));                 // Отправляем сообщение
    lastTime = millis();
  }
//  Serial.print(F("Влажность:\t"));
//  Serial.print(h);
//  Serial.print(F("%\tТемпература:\t"));
//  Serial.print(t);
//  Serial.println(F("*C"));
//  delay(2000);
}
